package com.clean.lot.util;

import android.os.StrictMode;
import android.util.TimeUtils;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import javax.net.ssl.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * http 工具类
 *
 * @author cloud cloud
 * @create 2017/10/18
 **/
public class HttpUtil {

    private static final String CHARSET = "UTF-8";
    private static final String HTTP_POST = "POST";
    private static final String HTTP_GET = "GET";

    private static final String HTTP_PUT = "PUT";


    public static String okGet(String url){
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(10, TimeUnit.SECONDS).build();
        Request request = new Request.Builder().url(url).get().build();
        Call call = client.newCall(request);
        try{
            Response response = call.execute();
            if(response.isSuccessful()){
                return response.body().string();
            }else {
                return "";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }

    }


    /**
     * Send GET request
     */
    public static String get(String url, Map<String, String> queryParas, Map<String, String> headers) {
        HttpURLConnection conn = null;
        try {
            conn = getHttpConnection(buildUrlWithQueryString(url, queryParas), HTTP_GET, headers);
            conn.connect();
            return readResponseString(conn);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static String get(String url, Map<String, String> queryParas) {
        return get(url, queryParas, null);
    }

    public static String get(String url) {
        return get(url, null, null);
    }

    public static String jsonGet(String url,Map<String,String> params){
        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        return get(url,params,headers);
    }


    /**
     * Send POST request
     */
    public static String post(String url, Map<String, String> queryParas, String data, Map<String, String> headers) {
        HttpURLConnection conn = null;
        try {
            conn = getHttpConnection(buildUrlWithQueryString(url, queryParas), HTTP_POST, headers);
            conn.connect();
            OutputStream out = conn.getOutputStream();
            out.write(data.getBytes(CHARSET));
            out.flush();
            out.close();
            return readResponseString(conn);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static String post(String url, Map<String, String> queryParas, String data) {
        return post(url, queryParas, data, null);
    }

    public static String post(String url, String data, Map<String, String> headers) {
        return post(url, null, data, headers);
    }

    public static String post(String url, String data) {
        return post(url, null, data, null);
    }

    public static String jsonPost(String url,String data){
        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        return post(url,null,data,headers);
    }

    public static String jsonPost(String url,Map<String,String>headers,String data){
        if(headers == null){
            headers = new HashMap<>();
        }
        headers.put("Content-Type","application/json");
        return post(url,null,data,headers);
    }




    /**
     * Send POST request
     */
    public static String put(String url, Map<String, String> queryParas, String data, Map<String, String> headers) {
        HttpURLConnection conn = null;
        try {
            conn = getHttpConnection(buildUrlWithQueryString(url, queryParas), HTTP_PUT, headers);
            conn.connect();
            OutputStream out = conn.getOutputStream();
            out.write(data.getBytes(CHARSET));
            out.flush();
            out.close();
            return readResponseString(conn);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }



    public static String jsonPut(String url,String data){
        Map<String,String> headers = new HashMap<>();
        headers.put("Content-Type","application/json");
        return put(url,null,data,headers);
    }


    /**
     * https 域名校验
     */
    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        @Override
		public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    /**
     * https 证书管理
     */
    private static class TrustAnyTrustManager implements X509TrustManager {
        @Override
		public X509Certificate[] getAcceptedIssuers() {
            return null;
        }
        @Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
        @Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
        }
    }



    private static HttpURLConnection getHttpConnection(String url, String method, Map<String, String> headers) throws Exception {
        URL _url = new URL(url);
        HttpURLConnection conn = (HttpURLConnection)_url.openConnection();
        conn.setRequestMethod(method);
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setConnectTimeout(30000);
        conn.setReadTimeout(30000);
        conn.setUseCaches(false); // Post 请求不能使用缓存
        if(headers != null){
            String contentType = headers.get("Content-Type");
            if(!StringUtil.isEmpty(contentType)){
                conn.setRequestProperty("Content-Type",contentType);
            }else{
                conn.setRequestProperty("Content-Type","application/x-www-form-urlencoded;charset=utf-8");
            }
        }
        conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.146 Safari/537.36");
        if (headers != null && !headers.isEmpty())
            for (Map.Entry<String, String> entry : headers.entrySet())
                conn.setRequestProperty(entry.getKey(), entry.getValue());

        return conn;
    }

    private static String readResponseString(HttpURLConnection conn) {
        StringBuilder sb = new StringBuilder();
        InputStream inputStream = null;
        try {
            inputStream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, CHARSET));
            String line = null;
            while ((line = reader.readLine()) != null){
                sb.append(line).append("\n");
            }
            return sb.toString();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Build queryString of the url
     */
    private static String buildUrlWithQueryString(String url, Map<String, String> queryParas) {
        if (queryParas == null || queryParas.isEmpty())
            return url;

        StringBuilder sb = new StringBuilder(url);
        boolean isFirst;
        if (url.indexOf("?") == -1) {
            isFirst = true;
            sb.append("?");
        }
        else {
            isFirst = false;
        }

        for (Map.Entry<String, String> entry : queryParas.entrySet()) {
            if (isFirst) isFirst = false;
            else sb.append("&");

            String key = entry.getKey();
            String value = entry.getValue();
            if (!StringUtil.isEmpty(value)){
                try {value = URLEncoder.encode(value, CHARSET);} catch (UnsupportedEncodingException e) {throw new RuntimeException(e);}
                sb.append(key).append("=").append(value);
            }
        }
        return sb.toString();
    }




















    public static void main(String[] args) {
        String data = "{\"sign\":\"XH1p9AU6CiP5bpp/F3BZs1lugdz7zLVYdzsu4MO0UXzT3klXu/HPm6cRgd8f5LB8BUJp1Y31RKXXLl7l+cAKx8mSPcTpyj+AJHR8JUJpM67LfMdttyBjDjYZnzYRTc3mGAu30f8L/FS+KOMH+iRUU7q1fwss6Xymowd6hlR6XJA=\",\"signtype\":\"RSA\",\"transdata\":{\\\\\"appid\\\\\":\\\\\"3016010720\\\\\",\\\\\"appuserid\\\\\":\\\\\"954\\\\\",\\\\\"cporderid\\\\\":\\\\\"NO20171110171000179T1GALqZ38DHzI\\\\\",\\\\\"cpprivate\\\\\":\\\\\"RECHARGE\\\\\",\\\\\"currency\\\\\":\\\\\"RMB\\\\\",\\\\\"feetype\\\\\":0,\\\\\"money\\\\\":1000.00,\\\\\"paytype\\\\\":402,\\\\\"result\\\\\":0,\\\\\"transid\\\\\":\\\\\"32411711101710038808\\\\\",\\\\\"transtime\\\\\":\\\\\"2017-11-10 17:11:39\\\\\",\\\\\"transtype\\\\\":0,\\\\\"waresid\\\\\":1}\"}";
        data = data.replace("\\","");
        System.out.println(data);
        JSONObject json = JSON.parseObject(data);
        JSONObject transdata = json.getJSONObject("transdata");
        System.out.println(transdata.getString("cporderid"));
        System.out.println(transdata.getDouble("money"));
    }
}
