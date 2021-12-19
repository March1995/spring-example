package com.wyb.test.cninfo;

import com.alibaba.fastjson.JSON;
import com.wyb.test.cninfo.config.Config;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Description:
 *
 * @author: Marcher丶
 * @Date: 2021-12-19 22:05
 **/
//@Slf4j
public class Main {

    public static void main(String[] args) throws Exception {
        // 读取所有stock
        List<Stock> stocks = getStockListFromFile();
        // 读取公告
        Request request = new Request(UrlConstants.QUERY_ANNOUNCEMENT,
                new Config());
        request.setMethod("post");
        request.setContentType("application/json;charset=UTF-8");
        String anno = queryAnnouncement(request, stocks.get(0));
        System.out.println(anno);
//        stocks.forEach(x -> {
//            System.out.println(x.toString());
//        });
    }


    private static List<Stock> getStockListFromFile() throws Exception {
//        FileInputStream inputStream = new FileInputStream("classpath:static/szse_stock.json");
        String path = Main.class.getClassLoader().getResource("static/szse_stock.json").getPath();
        InputStream in = new BufferedInputStream(new FileInputStream(path));
        String conf = IOUtils.toString(in, String.valueOf(StandardCharsets.UTF_8));
        return JSON.parseArray(conf, Stock.class);
    }

    private static String queryAnnouncement(Request request, Stock stock) {
        Map<Object, Object> params = new HashMap<>();
        params.put("stock", stock.getCode() + "," + stock.getOrgId());
        params.put("tabName", "fulltext");
        params.put("pageSize", "30");
        params.put("pageNum", "1");
        params.put("column", "szse");
        params.put("category", "");
        params.put("plate", "sz");
        params.put("seDate", "");
        params.put("searchkey", "");
        params.put("secid", "");
        params.put("sortName", "");
        params.put("sortType", "");
        params.put("isHLtitle", "true");
        io.github.biezhi.request.Request httpReq = null;
        if ("get".equalsIgnoreCase(request.method())) {
            httpReq = io.github.biezhi.request.Request.get(request.getUrl());
        }
        if ("post".equalsIgnoreCase(request.method())) {
            httpReq = io.github.biezhi.request.Request.post(request.getUrl(), params, true);
        }

        InputStream result = httpReq.contentType(request.contentType()).headers(request.getHeaders())
                .connectTimeout(request.getConfig().timeout())
                .readTimeout(request.getConfig().timeout())
                .stream();

//        log.debug("[{}] 下载完毕", request.getUrl());
        StringBuilder html = new StringBuilder(100);
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(result, request.charset()));
            String temp;
            while ((temp = br.readLine()) != null) {
                html.append(temp).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return html.toString();
//        log.debug("页面数据：[{}] ", html.toString());
    }
}
