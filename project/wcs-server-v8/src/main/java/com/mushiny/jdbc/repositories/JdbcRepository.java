package com.mushiny.jdbc.repositories;

import com.mushiny.beans.*;
import com.mushiny.beans.order.EnroutePod;
import com.mushiny.beans.order.Order;
import com.mushiny.beans.order.OrderPosition;
import com.mushiny.comm.CommonUtils;
import com.mushiny.comm.JsonUtils;
import com.mushiny.jdbc.config.SqlMapper;
import com.mushiny.jdbc.domain.IParam;
import com.mushiny.jdbc.domain.InOutParam;
import com.mushiny.jdbc.domain.OutParam;
import com.mushiny.mq.MessageSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.CallableStatementCallback;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by Tank.li on 2017/6/13.
 */
@Repository
@Transactional
@org.springframework.core.annotation.Order(value = 8)
public class JdbcRepository implements CommandLineRunner {
    private final static Logger logger = LoggerFactory.getLogger(JdbcRepository.class);
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private SqlMapper sqlMapper;


    private LinkedBlockingQueue<BaseObject> bos = new LinkedBlockingQueue();

    public int persistBoSize(){
        return bos.size();
    }

    public void addBo(BaseObject msg){
        try {
            msg.setActiveThread(Thread.currentThread().getName());
            bos.put(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public BaseObject getBo(){
        //取消息 阻塞
        try {
            return bos.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对象状态变化后更新数据库表
     * @param baseObject
     */
    public void updateBusinessObject(BaseObject baseObject){
        //更新小车与货架的位置
        if((baseObject instanceof Robot)
                || (baseObject instanceof Pod)
                || (baseObject instanceof OrderPosition)
                || (baseObject instanceof Order)
                || (baseObject instanceof Address)){
            BaseBpo bo = genNewBo(baseObject);
            //TODO 20180711
            bo.setId(baseObject.getId());
            this.addBo(bo);
            logger.debug("清空baseObject的更新状态:"+baseObject);
            if (baseObject.getDelCon() != null && !baseObject.getDelCon().isEmpty()) {
                baseObject.setDelCon(null);
            }else {
                baseObject.clearKV();
            }
            logger.debug("异步持久化BO : "+baseObject);
            return;
        }
        Map newValue = baseObject.getNewValue();
        if(newValue == null || newValue.isEmpty()){
            //logger.debug("未做更新操作，表"+baseObject.getTable()+"的状态是空:"+newValue);
            return;
        }
        CommonUtils.modifyUselessInfo(newValue);
        Map con = new HashMap();
        con.put(baseObject.getIdName(),baseObject.getId());
        this.updateRecords(baseObject.getTable(),newValue,con);
        baseObject.clearKV();
        logger.debug("BO:"+baseObject.getTable()+"状态已刷新到数据库!"
                + baseObject.getIdName() + ":" +baseObject.getId());
    }

    private BaseBpo genNewBo(BaseObject baseObject) {
        BaseBpo baseBpo = new BaseBpo();
        baseBpo.setIdName(baseObject.getIdName());
        baseBpo.setId(baseObject.getId());
        baseBpo.setTable(baseObject.getTable());
        //赋值操作条件
        if (baseObject.getDelCon() != null) {
            baseBpo.setDelCon(new HashMap<>(baseObject.getDelCon()));
        }
        if (baseObject.getCon() != null) {
            baseBpo.setCon(new HashMap<>(baseObject.getCon()));
        }
        if (baseObject.getKv() != null) {
            baseBpo.setKv(new HashMap<>(baseObject.getKv()));
        }

        logger.debug("生成数据库操作对象:["+baseBpo.getTable()+"]"+baseBpo.getIdName()+":"+baseBpo.getId());
        if(baseBpo.getDelCon()!=null && !baseBpo.getDelCon().isEmpty()){
            logger.debug("数据库操作对象的删除条件:["+baseBpo.getDelCon()+"]");
        }else if(baseBpo.getKv()!=null && !baseBpo.getKv().isEmpty()){
            logger.debug("数据库操作对象的更新值:["+baseBpo.getKv()+"] "+baseBpo.getIdName()+" == "+baseBpo.getId());
        }
        return baseBpo;
    }

    /**
     * 对象状态变化后更新数据库表
     * @param baseObject
     */
    public void insertBusinessObject(BaseObject baseObject){
        Map newValue = baseObject.getNewValue();
        if(newValue == null || newValue.isEmpty()){
            logger.debug("未做新增操作，表"+baseObject.getTable()+"的状态是空:"+newValue);
            return;
        }
        CommonUtils.genUselessInfo(newValue);
        /*Map con = new HashMap();
        con.put(baseObject.getIdName(),baseObject.getId());*/
        this.insertRecord(baseObject.getTable(),newValue);
        logger.debug("BO:"+baseObject.getTable()+"状态已刷新，新增到数据库!"
                + baseObject.getActiveThread()+" "+baseObject.getKv());
        baseObject.clearKV();

    }

    /**
     * 通过key更新 params是?的参数
     *
     * @param key
     * @param params
     * @return
     */
    public int updateByKey(String key, List params) {
        logger.debug("sql key:" + key);
        String sql = sqlMapper.getSqls().getProperty(key);
        return updateBySql(sql, params);
    }

    /**
     * 通过key更新 params是?的参数
     *
     * @param key
     * @param params
     * @return
     */
    public int updateByKey(String key, Object... params) {
        logger.debug("sql key:" + key);
        String sql = sqlMapper.getSqls().getProperty(key);
        return updateBySql(sql, params);
    }

    /**
     * 通过key更新
     *
     * @param key
     * @return
     */
    public int updateByKey(String key) {
        logger.debug("sql key:" + key);
        String sql = sqlMapper.getSqls().getProperty(key);
        return updateBySql(sql);
    }

    /**
     * 通过SQL更新
     *
     * @param sql
     * @return
     */
    public int updateBySql(String sql) {
        return updateBySql(sql, new ArrayList());
    }

    /**
     * * 通过key更新 params是?的参数
     *
     * @param sql
     * @param params
     * @return
     */
    public int updateBySql(String sql, List params) {
        logger.debug("SQL:" + sql + " params:" + params);
        long start = System.currentTimeMillis();
        int count = this.jdbcTemplate.update(sql, params.toArray());
        logger.debug(count + "条记录更新！SQL更新耗时:"+(System.currentTimeMillis()-start)+"毫秒!");
        return count;
    }

    public int updateBySql(String sql, Object... params) {
        logger.debug("SQL:" + sql.toUpperCase() + " params:" + paramsStr(params));
        long start = System.currentTimeMillis();
        int count = this.jdbcTemplate.update(sql.toUpperCase(), params);
        logger.debug(count + "条记录更新！SQL更新耗时:"+(System.currentTimeMillis()-start)+"毫秒!");
        return count;
    }

    /**
     * 通过SQLkey去查询 配置在sql.xml
     *
     * @param key
     * @param params
     * @return
     */
    public List<Map> queryByKey(String key, List params) {
        logger.debug("sql key:" + key);
        if (key == null || "".equals(key)) {
            logger.error("key is null :" + key);
            throw new RuntimeException("key is null:" + key);
        }
        String sql = sqlMapper.getSqls().getProperty(key);
        if (sql == null || "".equals(sql)) {
            logger.error("sql is null :" + key);
            throw new RuntimeException("sql is null:" + key);
        }

        return queryBySql(sql, params);
    }

    /**
     * 通过SQL key 查找 没有参数
     *
     * @param key
     * @return
     */
    public List<Map> queryByKey(String key) {
        List params = new ArrayList();
        return queryByKey(key, params);
    }

    public List<Map> queryByKey(String key,Object... pars) {
        List params = new ArrayList();
        for (int i = 0; i < pars.length; i++) {
            Object object = pars[i];
            params.add(object);
        }
        return queryByKey(key, params);
    }

    public List<Map> queryBySql(String sql,Object... pars) {
        List params = new ArrayList();
        for (int i = 0; i < pars.length; i++) {
            Object object = pars[i];
            params.add(object);
        }
        return queryBySql(sql, params);
    }

    /**
     * SQL执行 带? 参数
     *
     * @param sql
     * @param params
     * @return
     */
    @Transactional
    public List<Map> queryBySql(String sql, List params) {
        if (params == null) {
            params = new ArrayList();
        }
        Object[] _params = params.toArray();
        String _paramsStr = paramsStr(_params);
        logger.debug("查询语句是:" + sql.toUpperCase() + " 参数是:" + _paramsStr);
        long start = System.currentTimeMillis();
        List<Map> rows = this.jdbcTemplate.query(sql.toUpperCase(), _params, new RowMap());
        logger.debug("查询结果总数:"+rows.size()+" SQL查询耗时:"+(System.currentTimeMillis() -start)+"毫秒!");
        return  rows;
    }

    private String paramsStr(Object[] params) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        for (int i = 0; i < params.length; i++) {
            Object param = params[i];
            if(i<params.length-1){
                stringBuilder.append(param==null? "" : param.toString()).append(",");
            }else{
                stringBuilder.append(param.toString());
            }
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    @Transactional
    public List<Map> queryBySql(String sql) {
        Object[] _params = new Object[]{};
        return this.jdbcTemplate.query(sql, _params, new RowMap());
    }

    /**
     * 根据whereValue与tableName将表字段更新成newValue
     *
     * @param tableName  表名
     * @param newValue   字段及更新后的值
     * @param whereValue 更新条件：字段及对应值
     * @return
     * @Method update
     * @Function 功能描述：可能存在多行被更新
     * @Date 2010-11-24
     */
    @Transactional
    public int updateRecords(String tableName, Map newValue, Map whereValue) {
        //1、生成update **** set **=? and **=? where **id1= ?  and id2=?
        //2、为？号赋值
        if (newValue == null
                || tableName == null
                || whereValue == null
                || "".equals(tableName)
                || whereValue.size() == 0
                || newValue.size() == 0) {
            logger.error("表名或值或条件为空，不满足更新条件!!");
            return 0;//
        }
        List values = new ArrayList();
        StringBuffer sb = new StringBuffer();
        sb.append(" update ");
        sb.append(tableName);
        sb.append(" set ");
        sb.append(buildUpdateParams(newValue, values, ","));
        sb.append(" where ");
        sb.append(buildUpdateParams(whereValue, values, "and"));//通过原先的值判断该条记录
        String sql = sb.toString().toUpperCase();
        logger.debug("更新记录时生成的SQL语句是:" + sql + " 参数:" + this.paramsStr(values.toArray()));
        long start = System.currentTimeMillis();
        //下面生成为参数赋值 执行update的sql语句
        int count = this.jdbcTemplate.update(sql, values.toArray());

        logger.debug("Table:["+tableName+"]的 "+count + " 条记录更新！耗时:"
                +(System.currentTimeMillis()-start)+"毫秒");
        return count;
    }

    /**
     * @param tableName
     * @param whereValue
     * @return
     */
    @Transactional
    public int deleteRecords(String tableName, Map whereValue) {
        if (tableName == null
                || whereValue == null
                || "".equals(tableName)
                || whereValue.size() == 0) {
            logger.error("表名或条件为空，不满足删除条件!!");
            return 0;
        }
        List values = new ArrayList();
        StringBuffer buffer = new StringBuffer();
        buffer.append("delete from ");
        buffer.append(tableName);
        buffer.append(" where ");
        buffer.append(buildUpdateParams(whereValue, values, "and"));
        String sql = buffer.toString().toUpperCase();
        logger.debug("删除记录语句是:" + sql);
        int count = this.jdbcTemplate.update(sql, values.toArray());
        logger.debug("表"+tableName+"记录删除 数量:"+count);
        return count;
    }

    //link 是连接符 如，或 and
    private static String buildUpdateParams(Map newValue, List values, String link) {
        if (newValue.size() == 0) {
            return "";
        }
        List list = new ArrayList(newValue.keySet());
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < list.size(); i++) {
            String key = (String) list.get(i);
            values.add(newValue.get(key));
            if (i < list.size() - 1) {
                sb.append(key).append("=? ").append(link).append(" ");
            } else {
                sb.append(key).append("=? ");
            }
        }

        return sb.toString(); //去掉第一个","号
    }

    /**
     * @param tableName 表名
     * @param record    记录的Map形式 key为字段名 value为字段值
     * @return
     * @Method insertRecord
     * @Function 功能描述：插入一条记录
     * @Date 2010-11-24
     */
    @Transactional
    public int insertRecord(String tableName, Map record) {
        //1、生成insert into tableName (column1,column2.....) values (?,?......)
        //2、为？号赋值
        if (record == null
                || tableName == null
                || "".equals(tableName)
                || record.size() == 0) {
            logger.error("表名或记录为空，不执行插入语句!!");
            return 0;//
        }
        int result = 0;
        List values = new ArrayList();
        String sql = buildInsertSql(tableName, record, values).toUpperCase();//mysql要转成大写

        logger.debug("增加记录时生成的SQL语句是:" + sql);

        result = this.jdbcTemplate.update(sql, values.toArray());

        logger.debug(result + "条记录增加！");

        return result;
    }

    private static String buildInsertSql(String tableName, Map record, List values) {
        StringBuffer sb = new StringBuffer();
        sb.append(" insert into ");
        sb.append(tableName);
        sb.append(" (");
        sb.append(buildInsertParams(record, values));
        sb.append(") ");
        sb.append(" values (");
        sb.append(buildInsertValues(record.size()));
        sb.append(") ");

        return sb.toString();
    }

    private static String buildInsertParams(Map record, List values) {
        StringBuffer buffer = new StringBuffer();
        Set keySet = record.keySet();
        for (Iterator iterator = keySet.iterator(); iterator.hasNext(); ) {
            String key = (String) iterator.next();
            buffer.append(",").append(key);
            values.add(record.get(key));
        }

        return buffer.substring(1);//去掉第一个逗号
    }

    private static String buildInsertValues(int count) { //count 是?的数量
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < count; i++) {
            buffer.append(",").append("?");
        }
        return buffer.substring(1);
    }

    /*==========================存储过程调用=============================*/

    /**
     * 因为存储过程可能返回不同的多个结果，所以将值放参数列表里一块带回来
     * @param procName
     * @param params
     */
    public void callProc(String procName, List<IParam> params) {
        logger.debug("procName:"+procName+"params:"+params);
        jdbcTemplate.execute(
                new CallableStatementCreator() {
                    public CallableStatement createCallableStatement(Connection con) throws SQLException {
                        String storedProc = buildProcSql(procName,params);
                        logger.debug("storedProc = "+storedProc);
                        //String storedProc = "{call sp_list_table(?,?)}";// 调用的sql
                        CallableStatement cs = con.prepareCall(storedProc);
                        prepareStatement(cs,params);
                        return cs;
                    }
                }, new CallableStatementCallback() {
                    public Object doInCallableStatement(CallableStatement cs) throws SQLException, DataAccessException {
                        long startTime = System.currentTimeMillis();
                        boolean hasResults = cs.execute();
                        logger.debug("执行存储过程:"+procName+" 耗时(MS):"+(System.currentTimeMillis()-startTime));
                        for (int i = 0; i < params.size(); i++) {
                            IParam iParam = params.get(i);
                            if(iParam instanceof OutParam || iParam instanceof InOutParam){
                               Object obj = cs.getObject(iParam.getIndex());
                               if(obj instanceof ResultSet){
                                   ResultSet rs = (ResultSet) obj;
                                   iParam.setValue(rs2List(rs));
                               }else{
                                   logger.debug("第"+iParam.getIndex()+"个参数的返回值是:"+obj);
                                   iParam.setValue(obj);
                               }
                            }
                        }
                        //处理Mysql的存储过程返回的游标
                        while (hasResults) {
                            ResultSet rs = cs.getResultSet();
                            OutParam outParam = new OutParam();
                            outParam.setValue(rs2List(rs));
                            outParam.setIndex(params.size());//扔到列表后面 加个索引值
                            params.add(outParam);
                            hasResults = cs.getMoreResults(); //检查是否存在更多结果集
                        }
                        return params;
                    }
                });
        logger.debug("存储过程["+procName+"]执行成功!");
    }

    /*多个结果集*/
    /*CREATE PROCEDURE test_proc_multi_select()
        BEGIN
                 select * from testproc;
                 select * from testproc where id=1;
        END;  */
    /*con = MConnection.getConn();
       String sql = "{call test_proc_multi_select()}";
      cs = con.prepareCall(sql);
      boolean hadResults = cs.execute();
      int i=0;
      while (hadResults) {
          System.out.println("result No:----"+(++i));
          ResultSet rs = cs.getResultSet();
          while (rs != null && rs.next()) {
             int id1 = rs.getInt(1);
             String name1 = rs.getString(2);
             System.out.println(id1 + ":" + name1);
          }
          hadResults = cs.getMoreResults(); //检查是否存在更多结果集
      }  */

    private static List rs2List(ResultSet rs) {
        List resultsMap = new ArrayList();
        try {
            while (rs.next()) {// 转换每行的返回值到Map中
                Map rowMap = new HashMap();
                int count = rs.getMetaData().getColumnCount();
                for (int i = 0; i < count; i++) {
                    String column = rs.getMetaData().getColumnName(i+1);
                    Object value = rs.getObject(column);
                    rowMap.put(column.toUpperCase(),value);
                }
                logger.debug("rowMap==>"+rowMap);
                resultsMap.add(rowMap);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return resultsMap;
    }

    private static void prepareStatement(CallableStatement cs, List<IParam> params) {
        for (int i = 0; i < params.size(); i++) {
            IParam iParam = params.get(i);
            iParam.registerParam(cs);
        }
    }


    private static String buildProcSql(String pname, List params) {
        StringBuffer sb = new StringBuffer("{ call ");
        sb.append(pname).append("(");

        //拼参数。。
        if (params != null) {
            for (int i = 0; i < params.size(); i++) {
                sb.append("?");
                if (i != params.size() - 1) {//不是最后一项
                    sb.append(",");
                }
            }
        }
        sb.append(") }");
        return sb.toString();
    }
    /*==========================存储过程结束=============================*/

    @Override
    public void run(String... strings) throws Exception {
        BoPersister boPersister = new BoPersister();
        boPersister.setDaemon(true);
        boPersister.setName("BoPersister");
        boPersister.setJdbcRepository(this);
        boPersister.start();

        logger.info("启动异步持久化线程:"+boPersister.getName());
    }

    public void deleteBo(EnroutePod enroutePod) {
        this.addBo(enroutePod);
        logger.info("异步删除表记录 : "+enroutePod.getTable()+" 删除条件是"+enroutePod.getDelCon());
    }

    public void asynPersist(BaseObject baseObject) {
        BaseBpo baseBpo = new BaseBpo();
        Map<String,Object> record = new HashMap();
        record.putAll(baseObject.getKv());
        baseBpo.setKv(record);
        baseBpo.setTable(baseObject.getTable());
        this.addBo(baseBpo);
    }
}
