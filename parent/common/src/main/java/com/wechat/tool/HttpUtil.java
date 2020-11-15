package com.wechat.tool;

import com.wechat.model.HttpClientResult;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * http请求
 * @Author dai
 * @Date 2020/1/12
 */
public class HttpUtil {
    private final static Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    /**
     * 编码格式。发送编码格式统一用UTF-8
     */
    private static final String ENCODING = "UTF-8";

    /**
     * 设置连接超时时间，单位毫秒
     */
    private static final int CONNECT_TIMEOUT = 6000;

    /**
     * 请求获取数据的超时时间(即响应时间)，单位毫秒
     */
    private static final int SOCKET_TIMEOUT = 6000;

    //------------------------------- post ---------------------------------
    /**
     *  post请求
     * @param url 请求地址
     * @param params 请求参数   【Map、JSONObject】
     * @return 响应结果
     */
    public static HttpClientResult post(String url, Map<String, Object> params) {
        return post(url, null, params);
    }

    /**
     *  post请求
     * @param url 请求地址
     * @param body 请求参数   标准参数【a=1&amp;b=2】   REST模式【JSON或者XML字符串】
     * @return 响应结果
     */
    public static HttpClientResult post(String url, String body) {
        return post(url, null, body);
    }

    /**
     * post请求
     * @param url 请求地址
     * @param headers 请求头
     * @param params 请求参数  【Map、JSONObject】
     * @return 响应结果
     * @throws Exception
     */
    public static HttpClientResult post(String url, Map<String, String> headers, Map<String, Object> params) {
        // 创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建http对象
        HttpPost httpPost = new HttpPost(url);
        // 设置连接超时时间、请求获取数据的超时时间
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        httpPost.setConfig(requestConfig);
        // 设置请求头
		packageHeader(headers, httpPost);
        // 创建httpResponse对象
        CloseableHttpResponse httpResponse = null;
        try {
            // 封装请求参数
            packageParam(params, httpPost);
            // 执行请求
            httpResponse = httpClient.execute(httpPost);
            // 获得响应数据
            return getHttpClientResult(httpResponse, httpClient, httpPost);
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
        }finally {
            // 释放资源
            release(httpResponse, httpClient);
        }
        return null;
    }

    /**
     * post请求
     * @param url 请求地址
     * @param headers 请求头  【Map、JSONObject】
     * @param body 请求参数   标准参数【a=1&amp;b=2】   REST模式【JSON或者XML字符串】
     * @return 响应结果
     * @throws Exception
     */
    public static HttpClientResult post(String url, Map<String, String> headers, String body) {
        // 创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建http对象
        HttpPost httpPost = new HttpPost(url);
        // 设置连接超时时间、请求获取数据的超时时间
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        httpPost.setConfig(requestConfig);
        // 设置请求头
        packageHeader(headers, httpPost);
        // 创建httpResponse对象
        CloseableHttpResponse httpResponse = null;
        try {
            // 封装请求参数
            httpPost.setEntity(new StringEntity(body, "UTF-8"));
            // 执行请求
            httpResponse = httpClient.execute(httpPost);
            // 获得响应数据
            return getHttpClientResult(httpResponse, httpClient, httpPost);
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
        }finally {
            // 释放资源
            release(httpResponse, httpClient);
        }
        return null;
    }


    //------------------------------- get ----------------------------------
    /**
     * get请求
     * @param url 请求地址
     * @return 响应结果
     */
    public static HttpClientResult get(String url){
        return get(url, null, null);
    }

    /**
     * get请求
     * @param url 请求地址
     * @param params 请求参数 【Map、JSONObject】
     * @return 响应结果
     */
    public static HttpClientResult get(String url, Map<String, Object> params){
        return get(url, null, params);
    }

    /**
     * get请求
     * @param url 请求地址
     * @param headers 请求头
     * @param params 请求参数 【Map、JSONObject】
     * @return 响应结果
     */
    public static HttpClientResult get(String url, Map<String, String> headers, Map<String, Object> params) {
        // 创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建httpResponse对象
        CloseableHttpResponse httpResponse = null;
        try {
            // 创建访问的地址
            URIBuilder uriBuilder = new URIBuilder(url);
            if (params != null) {
                Set<Entry<String, Object>> entrySet = params.entrySet();
                for (Entry<String, Object> entry : entrySet) {
                    uriBuilder.setParameter(entry.getKey(),(String) entry.getValue());
                }
            }
            // 创建http对象
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            // 设置连接超时时间、请求获取数据的超时时间
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
            httpGet.setConfig(requestConfig);
            // 设置请求头
            packageHeader(headers, httpGet);
            // 执行请求
            httpResponse = httpClient.execute(httpGet);
            // 执行请求并获得响应结果
            return getHttpClientResult(httpResponse, httpClient, httpGet);
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
        }finally {
            // 释放资源
            release(httpResponse, httpClient);
        }
        return null;
    }


    //------------------------------- put ----------------------------------
    /**
     * put 请求
     * @param url 请求地址
     * @param params 请求求参数 【Map、JSONObject】
     * @return
     */
    public static HttpClientResult put(String url, Map<String, Object> params) {
        // 创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建http对象
        HttpPut httpPut = new HttpPut(url);
        // 设置连接超时时间、请求获取数据的超时时间
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        httpPut.setConfig(requestConfig);
        // 创建httpResponse对象
        CloseableHttpResponse httpResponse = null;
        try {
            // 封装请求参数
            packageParam(params, httpPut);
            // 执行请求
            httpResponse = httpClient.execute(httpPut);
            // 获得响应数据
            return getHttpClientResult(httpResponse, httpClient, httpPut);
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
        }finally {
            release(httpResponse, httpClient);
        }
        return null;
    }

    /**
     * put 请求
     * @param url 请求地址
     * @param body 请求参数   标准参数【a=1&amp;b=2】   REST模式【JSON或者XML字符串】
     * @return
     */
    public static HttpClientResult put(String url, String body) {
        // 创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建http对象
        HttpPut httpPut = new HttpPut(url);
        // 设置连接超时时间、请求获取数据的超时时间
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        httpPut.setConfig(requestConfig);
        // 创建httpResponse对象
        CloseableHttpResponse httpResponse = null;
        try {
            // 封装请求参数
            httpPut.setEntity(new StringEntity(body, "UTF-8"));
            // 执行请求
            httpResponse = httpClient.execute(httpPut);
            // 获得响应数据
            return getHttpClientResult(httpResponse, httpClient, httpPut);
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
        }finally {
            release(httpResponse, httpClient);
        }
        return null;
    }


    //----------------------------- patch -----------------------------------
    /**
     * patch 请求
     * @param url 请求地址
     * @param params 请求求参数 【Map、JSONObject】
     * @return
     */
    public static HttpClientResult patch(String url, Map<String, Object> params) {
        // 创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建http对象
        HttpPatch httpPatch = new HttpPatch(url);
        // 设置连接超时时间、请求获取数据的超时时间
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        httpPatch.setConfig(requestConfig);
        // 创建httpResponse对象
        CloseableHttpResponse httpResponse = null;
        try {
            // 封装请求参数
            packageParam(params, httpPatch);
            // 执行请求
            httpResponse = httpClient.execute(httpPatch);
            // 获得响应数据
            return getHttpClientResult(httpResponse, httpClient, httpPatch);
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
        }finally {
            release(httpResponse, httpClient);
        }
        return null;
    }

    /**
     * put 请求
     * @param url 请求地址
     * @param body 请求参数   标准参数【a=1&amp;b=2】   REST模式【JSON或者XML字符串】
     * @return
     */
    public static HttpClientResult patch(String url, String body) {
        // 创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建http对象
        HttpPatch httpPatch = new HttpPatch(url);
        // 设置连接超时时间、请求获取数据的超时时间
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        httpPatch.setConfig(requestConfig);
        // 创建httpResponse对象
        CloseableHttpResponse httpResponse = null;
        try {
            // 封装请求参数
            httpPatch.setEntity(new StringEntity(body, "UTF-8"));
            // 执行请求
            httpResponse = httpClient.execute(httpPatch);
            // 获得响应数据
            return getHttpClientResult(httpResponse, httpClient, httpPatch);
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
        }finally {
            release(httpResponse, httpClient);
        }
        return null;
    }

    //----------------------------- delete ----------------------------------
    /**
     * delete请求
     * @param url 请求地址
     * @return
     */
    public static HttpClientResult delete(String url) {
        // 创建httpClient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 创建http对象
        HttpDelete httpDelete = new HttpDelete(url);
        // 设置连接超时时间、请求获取数据的超时时间
        RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(CONNECT_TIMEOUT).setSocketTimeout(SOCKET_TIMEOUT).build();
        httpDelete.setConfig(requestConfig);
        // 创建httpResponse对象
        CloseableHttpResponse httpResponse = null;
        try {
            // 执行请求
            httpResponse = httpClient.execute(httpDelete);
            return getHttpClientResult(httpResponse, httpClient, httpDelete);
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
        } finally {
            release(httpResponse, httpClient);
        }
        return null;
    }

    //------------------------------- 封装 ----------------------------------

    /**
     * 封装请求头
     * @param params 请求头参数
     * @param httpMethod http对象
     */
    private static void packageHeader(Map<String, String> params, HttpRequestBase httpMethod) {
        if (params != null) {
            Set<Entry<String, String>> entrySet = params.entrySet();
            for (Entry<String, String> entry : entrySet) {
                // 设置到请求头到HttpRequestBase对象中
                httpMethod.setHeader(entry.getKey(), entry.getValue());
            }
        }
    }
    /**
     * 封装Map格式请求参数
     * @param params Map参数
     * @param httpMethod http对象
     * @throws Exception
     */
    private static void packageParam(Map<String, Object> params, HttpEntityEnclosingRequestBase httpMethod)
            throws Exception {
        if (params != null) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            Set<Entry<String, Object>> entrySet = params.entrySet();
            for (Entry<String, Object> entry : entrySet) {
                nvps.add(new BasicNameValuePair(entry.getKey(),(String) entry.getValue()));
            }
            // 设置到请求的http对象中
            httpMethod.setEntity(new UrlEncodedFormEntity(nvps, ENCODING));
        }
    }

    /**
     * 获取响应结果
     * @param httpResponse httpResponse对象
     * @param httpClient httpClient对象
     * @param httpMethod http对象
     * @return 响应结果
     * @throws Exception
     */
    private static HttpClientResult getHttpClientResult(CloseableHttpResponse httpResponse,
                                                        CloseableHttpClient httpClient, HttpRequestBase httpMethod) throws Exception {
        // 获取返回结果
        if (httpResponse != null && httpResponse.getStatusLine() != null) {
            String content = httpResponse.getEntity() != null ? EntityUtils.toString(httpResponse.getEntity(), ENCODING) : "";
            return new HttpClientResult(httpResponse.getStatusLine().getStatusCode(), content);
        }
        return null;
    }

    /**
     * 释放资源
     * @param httpResponse httpResponse对象
     * @param httpClient httpClient对象
     * @throws IOException
     */
    private static void release(CloseableHttpResponse httpResponse, CloseableHttpClient httpClient){
        try {
            // 释放资源
            if (httpResponse != null) {
                httpResponse.close();
            }
            if (httpClient != null) {
                httpClient.close();
            }
        }catch (Exception e){
            logger.error(ExceptionUtils.getStackTrace(e));
        }
    }
}