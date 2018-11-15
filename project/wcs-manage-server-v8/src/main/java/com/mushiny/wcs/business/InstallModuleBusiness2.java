package com.mushiny.wcs.business;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author:
 * @Description: Created by Laptop-8 on 2017/12/13.
 */
@Component
public class InstallModuleBusiness2 {
    private static final Logger LOGGER = LoggerFactory.getLogger(InstallModuleBusiness2.class);

    public int install() {

        try {

            String cmdstring = "chmod a+x /home/mslab/wms_v8/start3.sh";
            Process proc = Runtime.getRuntime().exec(cmdstring);
            proc.waitFor(); //阻塞，直到上述命令执行完
            LOGGER.info("执行完chmod");


            String shpath = "sh /home/mslab/wms_v8/start3.sh wcs-driveallocation-server-v8  ./wcs-driveallocation-server-v8/ 12004 /home/mslab/logs:/home/log";

            Process ps = Runtime.getRuntime().exec(shpath);
            ps.waitFor();

            BufferedReader br = new BufferedReader(new InputStreamReader(ps.getInputStream()));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append(IOUtils.LINE_SEPARATOR);
            }
            br.close();
            String result = sb.toString();
            BufferedReader er = new BufferedReader(new InputStreamReader(ps.getErrorStream()));
            while ((line = er.readLine()) != null) {
                sb.append(line).append(IOUtils.LINE_SEPARATOR);
            }
            er.close();
            LOGGER.info("返回结果yyy" + result);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return 1;
    }
}
