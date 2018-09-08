package junit.com.mushiny.wms.application;


import com.mushiny.wms.common.utils.JSONUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Administrator on 2018/7/7 0007.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestController {

    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;

    @Before
    public void init(){
        mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
    }

    @Test
    public void testInboundInstructControllerSave() throws Exception{
        Map<String, Object> map = new HashMap();
        map.put("INV_ORG_ID", "20180707");
        map.put("BILL_TYPE", "3");
        MvcResult result = mockMvc.perform(get("/InboundInstruct").contentType(MediaType.APPLICATION_JSON).content(JSONUtil.mapToJSon(map)))
                .andExpect(status().isOk())// 模拟向testRest发送get请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();// 返回执行请求的结果

        System.out.println(" - - - > > > 执行结果："+result.getResponse().getContentAsString());

    }

    @Test
    public void testOutboundInstructControllerSave() throws Exception{
        Map<String, Object> map = new HashMap();
        map.put("INV_ORG_ID", "20180707");
        map.put("BILL_TYPE", "5");
        map.put("BILL_NO", "123");
        map.put("LABEL_NO", "123456");
        map.put("INV_CODE", "C001");
        MvcResult result = mockMvc.perform(get("/outboundInstruct").contentType(MediaType.APPLICATION_JSON).content(JSONUtil.mapToJSon(map)))
                .andExpect(status().isOk())// 模拟向testRest发送get请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();// 返回执行请求的结果

        System.out.println(" - - - > > > 执行结果："+result.getResponse().getContentAsString());

    }


    @Test
    public void testRestTemplate() throws Exception{
        MvcResult result = mockMvc.perform(get("/mItems").contentType(MediaType.APPLICATION_JSON).content(""))
                .andExpect(status().isOk())// 模拟向testRest发送get请求
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))// 预期返回值的媒体类型text/plain;charset=UTF-8
                .andReturn();// 返回执行请求的结果

        System.out.println(" - - - > > > 执行结果："+result.getResponse().getContentAsString());
    }





}
