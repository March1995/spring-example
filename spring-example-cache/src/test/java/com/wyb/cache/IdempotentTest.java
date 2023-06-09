package com.wyb.cache;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

/**
 * @author Marcher丶
 * @date 2022-08-11
 **/
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
@WebAppConfiguration
@AutoConfigureMockMvc
public class IdempotentTest {
    @Autowired
    private MockMvc mockMvc;

    /**
     * .perform() : 执行一个MockMvcRequestBuilders的请求；MockMvcRequestBuilders有.get()、.post()、.put()、.delete()等请求。 .andDo() :
     * 添加一个MockMvcResultHandlers结果处理器,可以用于打印结果输出(MockMvcResultHandlers.print())。 .andExpect :
     * 添加MockMvcResultMatchers验证规则，验证执行结果是否正确。
     */
    @Test
    public void idempotentTest() throws Exception {
        /*
         * 1、mockMvc.perform执行一个请求。
         * 2、MockMvcRequestBuilders.get("XXX")构造一个请求。
         * 3、ResultActions.param添加请求传值
         * 4、ResultActions.accept(MediaType.TEXT_HTML_VALUE))设置返回类型
         * 5、ResultActions.andExpect添加执行完成后的断言。
         * 6、ResultActions.andDo添加一个结果处理器，表示要对结果做点什么事情 比如此处使用MockMvcResultHandlers.print()输出整个响应结果信息。
         * 7、ResultActions.andReturn表示执行完成后返回相应的结果。
         */
        // 请求
        RequestBuilder request = MockMvcRequestBuilders
                // post请求
                .get("/cache/getToken")
                // 数据类型
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE);
        // 请求头
//                .headers(headers)
        // 请求体
//                .content(jsonString);

        MvcResult mvcResult = mockMvc.perform(request)
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        //从返回值获取响应的内容
        String contentAsString = mvcResult.getResponse().getContentAsString();

        Thread t1 = new Thread(() -> {
            try {
                mockMvc.perform(getMockHttpServletRequestBuilder(contentAsString))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andDo(MockMvcResultHandlers.print());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t1.start();

        Thread t2 = new Thread(() -> {
            try {
                mockMvc.perform(getMockHttpServletRequestBuilder(contentAsString))
                        .andExpect(MockMvcResultMatchers.status().isOk())
                        .andDo(MockMvcResultHandlers.print());
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t2.start();

        t1.join();
        t1.join();
        System.out.println("finish");

    }

    private RequestBuilder getMockHttpServletRequestBuilder(String token) {
        return MockMvcRequestBuilders.get("/cache/idempotent").header("token", token)
                // 设置返回值类型为utf-8，否则默认为ISO-8859-1
                .accept(MediaType.APPLICATION_JSON_UTF8_VALUE);
    }
}
