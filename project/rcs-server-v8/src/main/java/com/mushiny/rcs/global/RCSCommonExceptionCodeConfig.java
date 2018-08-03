/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mushiny.rcs.global;

/**
 *异常错误代码表
 * @author 陈庆余 <13469592826@163.com>
 */
public class RCSCommonExceptionCodeConfig {
    //1.WCS路径串中的地址码不存在地图中
    public final static int MCS_SP_ADDRESSCODEID_NOT_IN_MAP = 0x01;
    //2.WCS路径串中的地址码含有不可通过CELL
    public final static int MCS_SP_ADDRESSCODEID_NO_WALKED = 0x02;
    //3.WCS路径串不符合RCS定义串
    public final static int WCS_SP_NOT_RCS_SERIESPATH = 0X03;
    //4.WCS路径串中，UP OR DOWN POD路径中，POD所在的地址码中且仅有一次
    public final static int WCS_SP_UPDOWN_POD_ERROR_ADDRESSCODEID = 0x04;
    
}
