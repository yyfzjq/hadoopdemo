package com.example.hadoopdemo.analyze;

import lombok.Data;
import lombok.SneakyThrows;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * @author Ruison
 * on 2019/7/22 - 10:16
 */
@Data
@ToString
public class Kpi {
    private String remoteAddr;// 记录客户端的ip地址
    private String remoteUser;// 记录客户端用户名称,忽略属性"-"
    private String timeLocal;// 记录访问时间与时区
    private String request;// 记录请求的url与http协议
    private String requestMethod; // 请求类型
    private String status;// 记录请求状态；成功是200
    private String bodyBytesSent;// 记录发送给客户端文件主体内容大小
    private String httpReferer;// 用来记录从那个页面链接访问过来的
    private String httpUserAgent;// 记录客户浏览器的相关信息
    private boolean valid = true;// 判断数据是否合法

    /** 获取时间 */
    @SneakyThrows
    public Date getTimeLocalDate() {
        if (this.timeLocal == null) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.US);
        return df.parse(this.timeLocal);
    }

    /** 获取格式化后的时间 */
    public String getTimeLocal() {
        Date date = this.getTimeLocalDate();
        if (date == null) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return df.format(this.getTimeLocalDate());
    }

    @SneakyThrows
    public String getTimeLocalDateHour() {
        Date date = this.getTimeLocalDate();
        if (date == null) {
            return null;
        }
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHH");
        return df.format(this.getTimeLocalDate());
    }

    /** 获取域名 */
    public String getDomain(){
        if(StringUtils.isEmpty(httpReferer) || httpReferer.length()<8){
            return httpReferer;
        }
        String str=this.httpReferer.replace("\"", "").replace("http://", "").replace("https://", "");
        return str.indexOf("/")>0?str.substring(0, str.indexOf("/")):str;
    }

    /** 获取访问URL，不带参数 */
    public String getRequestUrl() {
        if (StringUtils.isEmpty(request) || !request.contains("?")) {
            return request;
        }
        return this.request.substring(0, request.indexOf("?"));
    }

    /**
     * 数据解析
     *
     * @param line 数据
     * @return 解析完的数据
     */
    public static Kpi parser(String line) {
        Kpi kpi = new Kpi();
        String[] arr = line.split(" ");
        if (arr.length > 11) {
            kpi.setRemoteAddr(arr[0]);
            kpi.setRemoteUser(arr[1]);
            kpi.setTimeLocal(arr[3].substring(1));
            if (arr[5].length() < 9) {
                kpi.setRequestMethod(arr[5].substring(1));
            }
            kpi.setRequest(arr[6]);
            kpi.setStatus(arr[8]);
            kpi.setBodyBytesSent(arr[9]);
            kpi.setHttpReferer(arr[10].substring(1, arr[10].lastIndexOf("\"")));
            if (arr.length > 12) {
                if (arr[12].contains(";")) {
                    kpi.setHttpUserAgent(arr[11].substring(1) + " " + arr[12].substring(1, arr[12].indexOf(";")));
                }
            } else {
                kpi.setHttpUserAgent(arr[11]);
            }
            // 大于400，HTTP错误
            if (Integer.parseInt(kpi.getStatus()) >= 400) {
                kpi.setValid(false);
            }
        } else {
            kpi.setValid(false);
        }
        return kpi;
    }

    /**
     * 过滤
     *
     * @param line 数据
     * @return 过滤完的数据
     */
    public static Kpi filterPVs(String line) {
        Kpi kpi = parser(line);
        if (StringUtils.isEmpty(kpi.getRequestUrl()) || StringUtils.isEmpty(kpi.getRequestMethod())) {
            return null;
        }
        Set<String> pages = new HashSet<>();
        pages.add("/yoga/shop/list");
        if (pages.contains(kpi.getRequestUrl())) {
            kpi.setValid(false);
        }
        return kpi;
    }

    public static void main(String[] args) {
        String line = "61.219.11.153 - - [19/Jul/2019:04:57:29 +0800] \"\\x01\\x00\\x00\\x00\" 400 173 \"-\" \"-\" \"-\"";
        Kpi kpi = filterPVs(line);
        System.out.println(kpi);
        System.out.println(kpi.getDomain());
        System.out.println(kpi.getRequestUrl());
    }
}
