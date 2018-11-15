package com.mushiny.wcs.business;

import com.mushiny.wcs.Bean.Message;
import com.mushiny.wcs.Bean.Module;
import com.mushiny.wcs.Bean.Mushiny;
import com.mushiny.wcs.common.utils.JSONUtil;
import com.mushiny.wcs.common.utils.XmlUtils;
import com.mushiny.wcs.entity.Project;
import com.mushiny.wcs.respository.ProjectRepository;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.Zip64Mode;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.servlet.ServletContext;
import java.io.*;
import java.nio.charset.Charset;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/12/6.
 */
@Component
public class ModuleBusiness implements ApplicationContextAware {
    private static final Logger LOGGER = LoggerFactory.getLogger(ModuleBusiness.class);
    private String uploadPath = "/";
    private String zipPath = "/";
    private Pattern zipPattern = Pattern.compile("jar|zip|war");
    private String separator = File.separator;
    private XmlUtils xu = new XmlUtils();
    private final String PROJECTDIR = "/home/mslab/wms_v8/";
    private List<String> wcsRcsCmd = Arrays.asList(new String[]{"wcs-server-v8", "rcs-server-v8"});
    @Autowired
    private ProjectRepository projectRepository;

    // private final  String PROJECTDIR="E:/back/";
    public int uploadFile(MultipartFile file, Map<String, String> params, Message message) {
        int result = 1;

        String fileAllName = file.getOriginalFilename();
        String contentType = FilenameUtils.getExtension(fileAllName);
        String fileName = FilenameUtils.getBaseName(fileAllName);
        Matcher matcher = zipPattern.matcher(contentType);
        if (matcher.matches()) {
            try {
                String savePath = uploadPath + separator + params.get("projectVersion") + separator + fileName + separator;
                String saveFile = savePath + fileAllName;
                String dockerFile = savePath + "Dockerfile";
                String angularConf = savePath + "angular.conf";

                String releaseXml = uploadPath + separator + params.get("projectVersion") + separator + "release.xml";
                File upLoadFile = new File(saveFile);
                FileUtils.touch(upLoadFile);
                FileOutputStream out = new FileOutputStream(saveFile);
                BufferedOutputStream bs = new BufferedOutputStream(out);
                FileCopyUtils.copy(file.getInputStream(), bs);
                generateDockFile(dockerFile, angularConf, fileName);
                createXmlFile(releaseXml, params);
                saveProject(params, savePath);
            } catch (IOException ioe) {
                result = 0;
                message.setMessage("上传失败");
                LOGGER.error(ioe.getMessage(), ioe);
            }


        } else {
            result = 0;
            message.setMessage("不支持文件类型" + contentType + ",只支持jar类型的文件");
            LOGGER.warn("不支持文件类型{}", contentType);
        }


        message.setResult(result);
        if (result == 1) {
            message.setMessage("上传成功");
        }
        return result;
    }

    public int generateDockFile(String dockerFileName, String angularConfig, String moduleName) {
        int result = 1;
        Collection<String> dockerFileTemplate = new ArrayList<>();


        File dockerFile = new File(dockerFileName);
        try {
            FileUtils.touch(dockerFile);
            if (moduleName.equals("wms-client-v8")) {
                dockerFileTemplate.add("FROM nginx");
                dockerFileTemplate.add("COPY angular.conf /etc/nginx/conf.d/angular.conf");

                dockerFileTemplate.add("COPY ./wms-client-v8 /usr/share/nginx/html");

                dockerFileTemplate.add("EXPOSE 80");

            } else {
                String moduleJar = moduleName + ".jar";
                dockerFileTemplate.add("FROM java:openjdk-8-jre-alpine");
                dockerFileTemplate.add("VOLUME /tmp");
                dockerFileTemplate.add("ADD " + moduleJar + " " + moduleJar);
                dockerFileTemplate.add("RUN sh -c 'touch /" + moduleJar + "'");
                dockerFileTemplate.add("ENTRYPOINT [\"java\",\"-Djava.security.egd=file:/dev/./urandom\",\"-jar\",\"/" + moduleJar + "\"]");
            }
            FileUtils.writeLines(dockerFile, dockerFileTemplate);

            if (moduleName.equals("wms-client-v8")) {
                Collection<String> angularConfigTemp = new ArrayList<>();
                File angularConfigFile = new File(angularConfig);
                angularConfigTemp.add("server { listen       8000;server_name  localhost;location / { root   /app; index  index.html index.htm;try_files $uri $uri/ /index.html =404;}}");
                FileUtils.writeLines(angularConfigFile, angularConfigTemp);
            }
        } catch (IOException e) {
            result = 0;
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    public void test(String moduleName, Message message) {
        String cmd = "";
        Pattern testParrern;
        if (wcsRcsCmd.contains(moduleName)) {
            cmd = "sh /home/mslab/wms_v8/startNew.sh rcs_wcs_check  " + moduleName;
            testParrern = Pattern.compile("check .* success pid .*");
        } else if(moduleName.contains("websocket")) {
            cmd = "sh /home/mslab/wms_v8/startNew.sh check " + moduleName;
            testParrern = Pattern.compile("([a-zA-z0-9]*)[\\s]*tomcat"+ ".*");
        }else{
            cmd = "sh /home/mslab/wms_v8/startNew.sh check " + moduleName;
            testParrern = Pattern.compile("([a-zA-z0-9]*)[\\s]*" + moduleName + ".*");
        }
        exec(cmd, null, message, moduleName);


        if (testParrern.matcher(message.getMessage()).matches()) {
            message.setResult(1);
        } else {
            message.setResult(0);
        }

    }

    public List<Map<String, String>> getAllPackage() {
        List<Map<String, String>> results = new ArrayList<>();
        Collection<File> files = FileUtils.listFiles(new File(zipPath), null, true);
        if (!CollectionUtils.isEmpty(files)) {
            Iterator<File> temp = files.iterator();
            while (temp.hasNext()) {
                File fileTemp = temp.next();
                LOGGER.info("------------" + fileTemp.getAbsolutePath());
                String[] fileSp = fileTemp.getAbsolutePath().split(separator);
                LOGGER.info("------------{}", JSONUtil.toJSon(fileSp));
                int len = fileSp.length;
                Map<String, String> temp1 = new HashMap<>();
                temp1.put("projectVersion", fileSp[len - 2]);
                temp1.put("projectName", fileSp[len - 1]);
                results.add(temp1);


            }


        }
        return results;
    }

    public File downLoad(String fileName) {
        return new File(zipPath + separator + fileName);
    }

    public File compass(String version, List<String> module) {
        File out = null;
        try {
            out = new File(zipPath + separator + version + separator + "wms_v8.zip");
            FileUtils.touch(out);

            ZipArchiveOutputStream zos = (ZipArchiveOutputStream) new ArchiveStreamFactory()
                    .createArchiveOutputStream("zip", new FileOutputStream(out));
            zos.setEncoding("utf-8");
            String rootPath = uploadPath + separator + version;  //获取要压缩文件根路径
            ZipArchiveEntry ze;
            Collection<File> files = FileUtils.listFiles(new File(rootPath), null, true);
            for (File f : files) {
                if (!f.exists())
                    continue;
                LOGGER.info("------------" + FilenameUtils.getBaseName(f.getAbsolutePath()));
                if (FilenameUtils.getBaseName(f.getAbsolutePath()).equals("release.xml") && !module.contains(getFileParent(f.getAbsolutePath())))
                    continue;
                ze = new ZipArchiveEntry(getEntryName(f, rootPath));
                zos.putArchiveEntry(ze);
                //folder
                if (ze.isDirectory()) {
                    zos.closeArchiveEntry();
                    continue;
                }
                //file
                FileInputStream fis = new FileInputStream(f);
                IOUtils.copy(fis, zos);
                fis.close();
                zos.closeArchiveEntry();

            }
            zos.close();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }


        return out;


    }

    private String getFileParent(String filename) {
        String[] fileSp = filename.split(separator);
        LOGGER.info("------------" + filename + "--------{}", JSONUtil.toJSon(fileSp));
        String result = fileSp[fileSp.length - 2];
        return result;
    }

    private String getEntryName(File f, String rootPath) {
        String entryName;
        String fPath = f.getAbsolutePath();
        if (fPath.indexOf(rootPath) != -1)
            entryName = fPath.substring(rootPath.length() + 1);
        else
            entryName = f.getName();

        if (f.isDirectory())
            entryName += "/";
        return entryName;
    }

    /**
     * 把文件压缩成zip格式
     *
     * @param files       需要压缩的文件
     * @param zipFilePath 压缩后的zip文件路径   ,如"D:/test/aa.zip";
     */
    public void compressFiles2Zip(File[] files, String zipFilePath) {
        if (files != null && files.length > 0) {
            if (isEndsWithZip(zipFilePath)) {
                ZipArchiveOutputStream zaos = null;
                try {
                    File zipFile = new File(zipFilePath);
                    zaos = new ZipArchiveOutputStream(zipFile);
                    //Use Zip64 extensions for all entries where they are required
                    zaos.setUseZip64(Zip64Mode.AsNeeded);

                    //将每个文件用ZipArchiveEntry封装
                    //再用ZipArchiveOutputStream写到压缩文件中
                    for (File file : files) {
                        if (file != null) {
                            ZipArchiveEntry zipArchiveEntry = new ZipArchiveEntry(file, file.getName());
                            zaos.putArchiveEntry(zipArchiveEntry);
                            InputStream is = null;
                            try {
                                is = new BufferedInputStream(new FileInputStream(file));
                                byte[] buffer = new byte[1024 * 5];
                                int len = -1;
                                while ((len = is.read(buffer)) != -1) {
                                    //把缓冲区的字节写入到ZipArchiveEntry
                                    zaos.write(buffer, 0, len);
                                }
                                //Writes all necessary data for this entry.
                                zaos.closeArchiveEntry();
                            } catch (Exception e) {
                                LOGGER.error(e.getMessage(), e);
                            } finally {
                                if (is != null)
                                    is.close();
                            }

                        }
                    }
                    zaos.finish();
                } catch (Exception e) {
                    LOGGER.error(e.getMessage(), e);
                } finally {
                    try {
                        if (zaos != null) {
                            zaos.close();
                        }
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage(), e);
                    }
                }

            }

        }

    }

    public static boolean isEndsWithZip(String fileName) {
        boolean flag = false;
        if (fileName != null && !"".equals(fileName.trim())) {
            if (fileName.endsWith(".ZIP") || fileName.endsWith(".zip")) {
                flag = true;
            }
        }
        return flag;
    }


    /**
     * 把zip文件解压到指定的文件夹
     *
     * @param zipFilePath zip文件路径, 如 "D:/test/aa.zip"
     * @param saveFileDir 解压后的文件存放路径, 如"D:/test/" ()
     */
    public void unzip(String zipFilePath, String saveFileDir) {
        if (!saveFileDir.endsWith("\\") && !saveFileDir.endsWith("/")) {
            saveFileDir += File.separator;
        }
        File dir = new File(saveFileDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File file = new File(zipFilePath);
        if (file.exists()) {
            InputStream is = null;
            ZipArchiveInputStream zais = null;
            try {
                is = new FileInputStream(file);
                zais = new ZipArchiveInputStream(is);
                ArchiveEntry archiveEntry = null;
                while ((archiveEntry = zais.getNextEntry()) != null) {
                    // 获取文件名
                    String entryFileName = archiveEntry.getName();
                    // 构造解压出来的文件存放路径
                    String entryFilePath = saveFileDir + entryFileName;
                    OutputStream os = null;
                    try {
                        // 把解压出来的文件写到指定路径
                        File entryFile = new File(entryFilePath);
                        if (entryFileName.endsWith("/")) {
                            entryFile.mkdirs();
                        } else {
                            os = new BufferedOutputStream(new FileOutputStream(
                                    entryFile));
                            byte[] buffer = new byte[1024];
                            int len = -1;
                            while ((len = zais.read(buffer)) != -1) {
                                os.write(buffer, 0, len);
                            }
                        }
                    } catch (IOException e) {
                        LOGGER.error(e.getMessage(), e);
                    } finally {
                        IOUtils.closeQuietly(os);

                    }

                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            } finally {
                IOUtils.closeQuietly(zais);
                IOUtils.closeQuietly(is);

            }
        }
    }

    public List<String> getUploadFile() {
        List<String> result = new ArrayList<>();
        Iterator<File> files = FileUtils.iterateFiles(new File(uploadPath), new String[]{"zip"}, false);
        while (files.hasNext()) {
            File temp = files.next();
            result.add(temp.getName());
        }
        return result;
    }

    public Collection<File> searchFile(String folder, final String fileName) {
        RegexFileFilter rf = new RegexFileFilter(Pattern.compile(fileName));
        File directory = new File(folder);
        Collection<File> search = FileUtils.listFiles(directory, rf, rf);
        return search;
    }

    @Transactional
    public Project saveProject(Map<String, String> param, String fileSaveDir) {


        Project hasProject = projectRepository.getProjectByVersion(param.get("projectVersion"), param.get("moduleName"));

        if (ObjectUtils.isEmpty(hasProject)) {
            hasProject = new Project();
            hasProject.setFileSaveDir(fileSaveDir);
            hasProject.setModuleDir(param.get("moduleDir"));
            hasProject.setModuleLog(param.get("moduleLog"));
            hasProject.setModuleName(param.get("moduleName"));
            hasProject.setModulePort(param.get("modulePort"));
            hasProject.setProjectName(param.get("projectName"));
            hasProject.setProjectVersion(param.get("projectVersion"));
        } else {
            hasProject.setFileSaveDir(fileSaveDir);
            hasProject.setModuleDir(param.get("moduleDir"));
            hasProject.setModuleLog(param.get("moduleLog"));
            hasProject.setModulePort(param.get("modulePort"));
            hasProject.setProjectName(param.get("projectName"));
        }

        return projectRepository.saveAndFlush(hasProject);
    }

    public boolean createXmlFile(String relaeseFile, Map<String, String> param) {

        try {
            File release = new File(relaeseFile);
            Mushiny mushiny = null;
            if (!release.exists()) {
                LOGGER.info(release.getAbsolutePath());
                FileUtils.touch(release);
                mushiny = new Mushiny();
                mushiny.setProjectName(param.get("projectName"));
                mushiny.setProjectVersion(param.get("projectVersion"));
                Module module = new Module();
                module.setModuleName(param.get("moduleName"));
                module.setModuleLog(param.get("moduleLog"));
                module.setModuleDir(param.get("moduleDir"));
                module.setModulePort(param.get("modulePort"));
                mushiny.getModules().add(module);
            } else if (release.length() != 0) {
                LOGGER.info(release.getAbsolutePath());
                mushiny = xu.load(release, Mushiny.class);
                Module module = new Module();
                boolean exited = false;
                for (Module temp : mushiny.getModules()) {
                    if (param.get("moduleName").equals(temp.getModuleName())) {
                        module = temp;
                        exited = true;
                        break;
                    }
                }
                module.setModuleName(param.get("moduleName"));
                module.setModuleLog(param.get("moduleLog"));
                module.setModuleDir(param.get("moduleDir"));
                module.setModulePort(param.get("modulePort"));
                if (!exited) {
                    mushiny.getModules().add(module);
                }

            }
            if (!ObjectUtils.isEmpty(mushiny)) {
                xu.java2xmlFile(relaeseFile, mushiny);
            }
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
        }

        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        WebApplicationContext webApplicationContext = (WebApplicationContext) applicationContext;
        ServletContext servletContext = webApplicationContext.getServletContext();
        String savePath = servletContext.getRealPath("/");
        uploadPath = savePath + "fileUpload";
        zipPath = savePath + "fileZip";
    }

    public int install(WebSocketSession webSocketSession, Map<String, String> param) {
        int finalResult = 1;
        WebSocketMessage message1;
        try {
            message1 = new TextMessage("开始复制安装包" + param.get("moduleName") + "安装文件 到" + PROJECTDIR + param.get("moduleName"));
            webSocketSession.sendMessage(message1);
            int copyResult = copyModule(param.get("moduleName"), param.get("projectVersion"));
            if (copyResult == 1) {
                message1 = new TextMessage("复制安装包" + param.get("moduleName") + "成功!");
                webSocketSession.sendMessage(message1);
                message1 = new TextMessage("开始安装" + param.get("moduleName") + " ....");
                webSocketSession.sendMessage(message1);
                StringBuilder cmd = new StringBuilder();
                if (wcsRcsCmd.contains(param.get("moduleName"))) {
                    cmd.append("sh /home/mslab/wms_v8/startNew.sh rcs_wcs_start ")
                            .append(" " + "/home/mslab/wms_v8/" + param.get("moduleName") + "/")
                            .append(param.get("moduleName")).append(".jar");

                }else{
                    cmd.append("sh /home/mslab/wms_v8/startNew.sh start")
                            .append(" " + param.get("moduleName"))
                            .append(" " + "/home/mslab/wms_v8/" + param.get("moduleName") + "/")
                            .append(" " + param.get("modulePort"))
                            .append(" /home/mslab/logs:/home/log");
                }

                exec(cmd.toString(), webSocketSession, null, param.get("moduleName"));

            } else {
                message1 = new TextMessage("复制安装包" + param.get("moduleName") + "失败!");
                webSocketSession.sendMessage(message1);
            }
        } catch (Exception e) {


        }

        return finalResult;
    }

    public int stop(String moduleName, Message message) {
        String cmd = "";
        Pattern testParrern;
        if (wcsRcsCmd.contains(moduleName)) {
            cmd = "sh /home/mslab/wms_v8/startNew.sh rcs_wcs_stop " + moduleName;
            testParrern = Pattern.compile(".*杀死进程.*|程序没有启");
        } else {
            cmd = "sh /home/mslab/wms_v8/startNew.sh stop " + moduleName;
            testParrern = Pattern.compile(moduleName);
        }
        int result = exec(cmd, null, message, moduleName);
        if (testParrern.matcher(message.getMessage()).matches()) {
            message.setResult(1);

        } else {
            message.setResult(0);
        }

        return result;
    }

    private int exec(String cmd, WebSocketSession webSocketSession, Message message, String moduleName) {
        int finalResult = 1;
        WebSocketMessage message1;
        StringBuilder sb = new StringBuilder();
        try {

            Process process = Runtime.getRuntime().exec(cmd);

            String line;
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.forName("utf-8")));
            BufferedReader erInput = new BufferedReader(new InputStreamReader(process.getErrorStream(), Charset.forName("utf-8")));
            while ((line = stdInput.readLine()) != null) {
                if (!ObjectUtils.isEmpty(webSocketSession)) {
                    message1 = new TextMessage(line);
                    webSocketSession.sendMessage(message1);
                } else {
                    sb.append(line);
                }

                while ((line = erInput.readLine()) != null) {
                    if (!ObjectUtils.isEmpty(webSocketSession)) {
                        message1 = new TextMessage(line);
                        webSocketSession.sendMessage(message1);
                    } else {
                        sb.append(line);
                    }

                }
            }

            process.waitFor();
        } catch (Exception e) {
            finalResult = 0;
            if (!ObjectUtils.isEmpty(webSocketSession)) {
                message1 = new TextMessage("安装" + moduleName + "失败\r" + e.getMessage());

                try {
                    webSocketSession.sendMessage(message1);
                } catch (IOException e1) {
                    LOGGER.error(e1.getMessage(), e1);
                }
            } else {
                sb.append(e.getMessage());
            }

            LOGGER.error(e.getMessage(), e);

        }

        if (!ObjectUtils.isEmpty(message)) {
            message.setResult(finalResult);
            message.setMessage(sb.toString());
        }
        return finalResult;

    }

    public int copyModule(String moduleName, String moduleVersion) {
        int result = 1;
        String moduleSrcDir = uploadPath + separator + moduleVersion + separator + moduleName + separator;
        String moduleDir = PROJECTDIR + moduleName + separator;
        File moduleSrcDirFile = new File(moduleSrcDir);
        File moduleDirFile = new File(moduleDir);
        try {
            FileUtils.copyDirectory(moduleSrcDirFile, moduleDirFile);
            if (moduleName.equals("wms-client-v8")) {
                unzip(moduleSrcDir + moduleName + ".zip", moduleDir + moduleName + separator);
            }

        } catch (IOException e) {
            result = 0;
            LOGGER.error(e.getMessage(), e);
        }
        return result;


    }
}
