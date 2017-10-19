package com.stock.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;

/**
 * http工具对比
 *

 */
public class HttpFetchUtil {

    /**
     * 获取访问的状态码
     *
     * @param request
     * @return
     * @throws Exception
     */
    public static int getResponseCode(String request) throws Exception {
        URL url = new URL(request);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        return conn.getResponseCode();

    }

    /**
     * 1）JDK自带HTTP连接，获取页面或Json
     *
     * @param request
     * @param charset
     * @return
     * @throws Exception
     */
    public static String JDKFetch(String request, String charset) throws Exception {
        URL url = new URL(request);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        //模拟浏览器参数
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36"
                + " (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36");
        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
            InputStream input = conn.getInputStream();
            StringBuffer sb = new StringBuffer();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input, charset));
            String s;
            while ((s = reader.readLine()) != null) {
                sb.append(s + "\n");
            }
            input.close();
            conn.disconnect();
            return sb.toString();
        }
        return "";
    }

    /**
     * 2） JDK自带URL连接，获取页面或Json
     *
     * @param request
     * @param charset
     * @return
     * @throws Exception
     */
    public static String URLFetch(String request, String charset) throws Exception {
        URL url = new URL(request);
        return IOUtils.toString(url.openStream());
    }

    /**
     * 3）HttpClient Get工具，获取页面或Json
     *
     * @param url
     * @param charset
     * @return
     * @throws Exception
     */
    public static String httpClientFetch(String url, String charset) throws Exception {
        // GET
        HttpClient httpClient = new HttpClient();
        httpClient.getParams().setContentCharset(charset);
        HttpMethod method = new GetMethod(url);
        httpClient.executeMethod(method);
        return method.getResponseBodyAsString();
    }

    /**
     * 4）commons-io工具，获取页面或Json
     *
     * @param url
     * @param charset
     * @return
     * @throws Exception
     */
    public static String commonsIOFetch(String url, String charset) throws Exception {
        return IOUtils.toString(new URL(url), charset);
    }

    /**
     * 5） Jsoup工具（通常用于html字段解析），获取页面,非Json返回格式
     *
     * @param url
     * @return
     * @throws Exception
     */
    public static String jsoupFetch(String url) throws Exception {
        return Jsoup.parse(new URL(url), 2 * 1000).html();
    }
}
