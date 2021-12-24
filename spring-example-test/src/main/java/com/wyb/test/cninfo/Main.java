package com.wyb.test.cninfo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wyb.test.cninfo.config.Config;
import com.wyb.test.cninfo.entity.Announcement;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.CollectionUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Description:
 *
 * @author: Marcher丶
 * @Date: 2021-12-19 22:05
 **/
//@Slf4j
public class Main {

    public static void main(String[] args) throws Exception {
        DateTimeFormatter format = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG);
        // 读取所有stock
//        List<Stock> stocks = getStockListFromFile();
//        String data = requestDataItem("600685");
//        System.out.println(data);

        // 读取公告
        String stockStr = requestAnnouncement(null);
        Announcement announcement = Announcement.fromJson(stockStr);
        List<Announcement.AnnouncementsDTO> announcements = announcement.filterAnnouncementsByDate();
        if (!CollectionUtils.isEmpty(announcements)) {
            announcements.forEach(announcementsDTO -> {
                LocalDateTime dateTime = LocalDateTime.now();
                String num = requestDataItemForSz(announcementsDTO.getSecCode());
                if (StringUtils.isEmpty(num)) {
                    System.out.println("股票名称:" + announcementsDTO.getSecName() + ",股票代码:" + announcementsDTO.getSecCode() + ",深市暂无数据,请求沪市");
                    num = requestDataItemForSh(announcementsDTO.getSecCode());
                }
                if (StringUtils.isEmpty(num)) {
                    System.out.println("股票名称:" + announcementsDTO.getSecName() + ",股票代码:" + announcementsDTO.getSecCode() + ",两市都无数据");
                    return;
                }
                System.out.println("股票名称:" + announcementsDTO.getSecName() + ",股票代码:" + announcementsDTO.getSecCode() + ",报告名称:" + announcementsDTO.getAnnouncementTitle() +
                        ",当前时间:" + dateTime.format(format) + ",实时涨幅:" + num);
            });
        }

//        stocks = stocks.stream().filter(x -> x.getCode().equals("001227")).collect(Collectors.toList());
//        stocks.forEach(stock -> {
//            String stockStr = queryAnnouncement(request, stock);
//            Announcement announcement = Announcement.fromJson(stockStr);
//            List<Announcement.AnnouncementsDTO> announcements = announcement.filterAnnouncementsByDate();
//            System.out.println("code:" + stock.getCode() + ",orgId:" + stock.getOrgId() + ",list:" + announcements.toString());
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        });
    }


    private static List<Stock> getStockListFromFile() throws Exception {
//        FileInputStream inputStream = new FileInputStream("classpath:static/szse_stock.json");
        String path = Main.class.getClassLoader().getResource("static/szse_stock.json").getPath();
        InputStream in = new BufferedInputStream(new FileInputStream(path));
        String conf = IOUtils.toString(in, String.valueOf(StandardCharsets.UTF_8));
        return JSON.parseArray(conf, Stock.class);
    }

    private static String requestAnnouncement(Stock stock) {
        Request request = new Request(UrlConstants.QUERY_ANNOUNCEMENT,
                new Config());
        request.setMethod("post");
        request.setContentType("application/json;charset=UTF-8");

        Map<Object, Object> params = new HashMap<>();
//        params.put("stock", stock.getCode() + "," + stock.getOrgId());
        params.put("stock", "");
        params.put("tabName", "fulltext");
        params.put("pageSize", "30");
        params.put("pageNum", "1");
        params.put("column", "szse");
        params.put("category", "category_ndbg_szsh;category_bndbg_szsh;category_yjdbg_szsh;category_yjygjxz_szsh;category_sjdbg_szsh;");
//        params.put("category", "");
        params.put("plate", "sz");
        LocalDate localDate = LocalDate.now();
        params.put("seDate", localDate.toString() + "~" + localDate.toString());
        params.put("searchkey", "");
        params.put("secid", "");
        params.put("sortName", "");
        params.put("sortType", "");
        params.put("isHLtitle", "true");
        return doRequest(request, params);
    }

    private static String requestDataItemForSz(String code) {
        String url = UrlConstants.QUERY_DATA_ITEM;
        String randomString = getRandomString(17);
        long currentTimeMillis = System.currentTimeMillis();
        url = String.format(url, randomString, currentTimeMillis, "sz" + code, currentTimeMillis + 1);
        System.out.println("请求的url为：" + url);
        Request request = new Request(url,
                new Config());
        String data = doRequest(request, null);
        if (!data.contains("{")) {
            return null;
        }
        data = data.substring(data.indexOf("{", 0), data.indexOf("}", 1) + 1);
        JSONObject obj = JSONObject.parseObject(data);
        return (String) obj.get("3672520");
    }

    private static String requestDataItemForSh(String code) {
        String url = UrlConstants.QUERY_DATA_ITEM;
        String randomString = getRandomString(17);
        long currentTimeMillis = System.currentTimeMillis();
        url = String.format(url, randomString, currentTimeMillis, "sh" + code, currentTimeMillis + 1);
        System.out.println("请求的url为：" + url);
        Request request = new Request(url,
                new Config());
        return doRequest(request, null);
    }

    private static String requestDataItem(String code, String plate) {
        String url = UrlConstants.QUERY_DATA_ITEM;
        String randomString = getRandomString(17);
        long currentTimeMillis = System.currentTimeMillis();
        url = String.format(url, randomString, currentTimeMillis, (StringUtils.isEmpty(plate) ? "sz" : plate) + code, currentTimeMillis + 1);
        System.out.println("请求的url为：" + url);
        Request request = new Request(url,
                new Config());
        return doRequest(request, null);
    }

    private static String doRequest(Request request, Map<Object, Object> params) {

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

    //length用户要求产生字符串的长度
    public static String getRandomString(int length) {
        String str = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(10);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }
}
