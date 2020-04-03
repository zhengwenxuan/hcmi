package com.hjw.interfaces.util;

import org.apache.commons.io.IOUtils;  
import org.apache.http.HttpEntity;  
import org.apache.http.HttpResponse;  
import org.apache.http.HttpStatus;  
import org.apache.http.NameValuePair;  
import org.apache.http.client.HttpClient;  
import org.apache.http.client.config.RequestConfig;  
import org.apache.http.client.entity.UrlEncodedFormEntity;  
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;  
import org.apache.http.client.methods.HttpPost;  
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;  
import org.apache.http.conn.ssl.SSLContextBuilder;  
import org.apache.http.conn.ssl.TrustStrategy;  
import org.apache.http.conn.ssl.X509HostnameVerifier;  
import org.apache.http.entity.StringEntity;  
import org.apache.http.impl.client.CloseableHttpClient;  
import org.apache.http.impl.client.DefaultHttpClient;  
import org.apache.http.impl.client.HttpClients;  
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;  
import org.apache.http.message.BasicNameValuePair;  
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONObject;

import javax.net.ssl.SSLContext;  
import javax.net.ssl.SSLException;  
import javax.net.ssl.SSLSession;  
import javax.net.ssl.SSLSocket;  
import java.io.IOException;  
import java.io.InputStream;  
import java.nio.charset.Charset;  
import java.security.GeneralSecurityException;  
import java.security.cert.CertificateException;  
import java.security.cert.X509Certificate;  
import java.util.ArrayList;  
import java.util.HashMap;  
import java.util.List;  
import java.util.Map;  
  
/** 
 * HTTP 请求工具类 
 * 
 * @author : liii 
 * @version : 1.0.0 
 * @date : 2015/7/21 
 * @see : TODO 
 */  
public class HttpUtil {  
    private static PoolingHttpClientConnectionManager connMgr;  
    private static RequestConfig requestConfig;  
    private static final int MAX_TIMEOUT = 30000;  
  
    static {  
        // 设置连接池  
        connMgr = new PoolingHttpClientConnectionManager();  
        // 设置连接池大小  
        connMgr.setMaxTotal(500);  
        connMgr.setDefaultMaxPerRoute(connMgr.getMaxTotal());  
  
        RequestConfig.Builder configBuilder = RequestConfig.custom();  
        // 设置连接超时  
        configBuilder.setConnectTimeout(MAX_TIMEOUT);  
        // 设置读取超时  
        configBuilder.setSocketTimeout(MAX_TIMEOUT);  
        // 设置从连接池获取连接实例的超时  
        configBuilder.setConnectionRequestTimeout(MAX_TIMEOUT);  
        // 在提交请求之前 测试连接是否可用  
        configBuilder.setStaleConnectionCheckEnabled(true);  
        requestConfig = configBuilder.build();  
    }  
  
    /** 
     * 发送 GET 请求（HTTP），不带输入数据 
     * @param url 
     * @return 
     */  
    public static String doGet(String url,String chatset) {  
        return doGet(url, new HashMap<String, Object>(),chatset);  
    }  
  
    /** 
     * 发送 GET 请求（HTTP），K-V形式 
     * @param url 
     * @param params 
     * @return 
     */  
    public static String doGet(String url, Map<String, Object> params,String chatset) {  
        String apiUrl = url;  
        StringBuffer param = new StringBuffer();  
        int i = 0;  
        for (String key : params.keySet()) {  
            if (i == 0)  
                param.append("?");  
            else  
                param.append("&");  
            param.append(key).append("=").append(params.get(key));  
            i++;  
        }  
        apiUrl += param;  
        String result = null;  
        HttpClient httpclient = new DefaultHttpClient();  
        try {  
            HttpGet httpPost = new HttpGet(apiUrl);  
            HttpResponse response = httpclient.execute(httpPost);  
            int statusCode = response.getStatusLine().getStatusCode();  
  
//            System.out.println("执行状态码 : " + statusCode);  
  
            HttpEntity entity = response.getEntity();  
            if (entity != null) {  
                InputStream instream = entity.getContent();  
                result = IOUtils.toString(instream, chatset);  
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return result;  
    }  
  
    /** 
     * 发送 POST 请求（HTTP），不带输入数据 
     * @param apiUrl 
     * @return 
     */  
    public static String doPost(String apiUrl,String chatset) {  
        return doPost(apiUrl, new HashMap<String, Object>(),chatset);  
    }  
  
    /** 
     * 发送 POST 请求（HTTP），K-V形式 
     * @param apiUrl API接口URL 
     * @param params 参数map 
     * @return 
     */  
    public static String doPostParam(String apiUrl, Map<String, Object> params,String chatset) {  
        CloseableHttpClient httpClient = HttpClients.createDefault();  
        String httpStr = null;  
        HttpPost httpPost = new HttpPost(apiUrl);  
        CloseableHttpResponse response = null;  
  
        try {  
            httpPost.setConfig(requestConfig);  
            List<NameValuePair> pairList = new ArrayList<>(params.size());  
            for (Map.Entry<String, Object> entry : params.entrySet()) {  
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry  
                        .getValue().toString());  
                pairList.add(pair);  
            }  
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName(chatset)));  
            response = httpClient.execute(httpPost);  
//            System.out.println(responscom.hjw.interfaces.util.StringUtil.formatException(e));  
            HttpEntity entity = response.getEntity();  
            httpStr = EntityUtils.toString(entity, chatset);  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (response != null) {  
                try {  
                    EntityUtils.consume(response.getEntity());  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        return httpStr;  
    }  
  
    /** 
     * 发送 POST 请求（HTTP），JSON形式 
     * @param apiUrl 
     * @param json json对象 
     * @return 
     */  
    public static String doPost(String apiUrl, Object json,String chatset) {  
        CloseableHttpClient httpClient = HttpClients.createDefault();  
        String httpStr = null;  
        HttpPost httpPost = new HttpPost(apiUrl);  
        CloseableHttpResponse response = null;  
  
        try {  
            httpPost.setConfig(requestConfig);  
            JSONObject ojson = JSONObject.fromObject(json);// 将java对象转换为json对象
			String jstr = ojson.toString();// 将json对象转换为字符串
            StringEntity stringEntity = new StringEntity(jstr,chatset);//解决中文乱码问题  
            stringEntity.setContentEncoding(chatset);  
            stringEntity.setContentType("application/json");  
            httpPost.setEntity(stringEntity);  
            response = httpClient.execute(httpPost);  
            HttpEntity entity = response.getEntity();  
//            System.out.println(response.getStatusLine().getStatusCode());  
            httpStr = EntityUtils.toString(entity, chatset);  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (response != null) {  
                try {  
                    EntityUtils.consume(response.getEntity());  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        return httpStr;  
    }  
    
    /** 
     * 发送 POST 请求（HTTP），JSON形式 
     * @param apiUrl 
     * @param json json对象 
     * @return 
     */  
    public static String doPost_Str(String apiUrl, String jstr,String chatset) {  
        CloseableHttpClient httpClient = HttpClients.createDefault();  
        String httpStr = null;  
        HttpPost httpPost = new HttpPost(apiUrl);  
        CloseableHttpResponse response = null;  
  
        try {  
            httpPost.setConfig(requestConfig);  
            StringEntity stringEntity = new StringEntity(jstr,chatset);//解决中文乱码问题  
            stringEntity.setContentEncoding(chatset);  
            stringEntity.setContentType("application/json");  
            httpPost.setEntity(stringEntity);  
            response = httpClient.execute(httpPost);  
            HttpEntity entity = response.getEntity();  
//            System.out.println(response.getStatusLine().getStatusCode());  
            httpStr = EntityUtils.toString(entity, chatset);  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (response != null) {  
                try {  
                    EntityUtils.consume(response.getEntity());  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        return httpStr;  
    }  
    
    /** 
     * 发送 POST 请求（HTTP），JSON形式 
     * @param apiUrl 
     * @param json json对象 
     * @return 
     */  
    public static String doPost_Xml(String apiUrl, String jstr,String chatset) {  
    	CloseableHttpClient httpClient = HttpClients.createDefault();  
    	String httpStr = null;  
    	HttpPost httpPost = new HttpPost(apiUrl);  
    	CloseableHttpResponse response = null;  
    	
    	try {  
    		httpPost.setConfig(requestConfig);  
    		StringEntity stringEntity = new StringEntity(jstr,chatset);//解决中文乱码问题  
    		stringEntity.setContentEncoding(chatset);  
    		stringEntity.setContentType("application/xml");  
    		httpPost.setEntity(stringEntity);  
    		response = httpClient.execute(httpPost);  
    		HttpEntity entity = response.getEntity();  
//            System.out.println(response.getStatusLine().getStatusCode());  
    		httpStr = EntityUtils.toString(entity, chatset);  
    	} catch (IOException e) {  
    		e.printStackTrace();  
    	} finally {  
    		if (response != null) {  
    			try {  
    				EntityUtils.consume(response.getEntity());  
    			} catch (IOException e) {  
    				e.printStackTrace();  
    			}  
    		}  
    	}  
    	return httpStr;  
    }  
  
    /** 
     * 发送 SSL POST 请求（HTTPS），K-V形式 
     * @param apiUrl API接口URL 
     * @param params 参数map 
     * @return 
     */  
    public static String doPostSSL(String apiUrl, Map<String, Object> params,String chatset) {  
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();  
        HttpPost httpPost = new HttpPost(apiUrl);  
        CloseableHttpResponse response = null;  
        String httpStr = null;  
  
        try {  
            httpPost.setConfig(requestConfig);  
            List<NameValuePair> pairList = new ArrayList<NameValuePair>(params.size());  
            for (Map.Entry<String, Object> entry : params.entrySet()) {  
                NameValuePair pair = new BasicNameValuePair(entry.getKey(), entry  
                        .getValue().toString());  
                pairList.add(pair);  
            }  
            httpPost.setEntity(new UrlEncodedFormEntity(pairList, Charset.forName(chatset)));  
            response = httpClient.execute(httpPost);  
            int statusCode = response.getStatusLine().getStatusCode();  
            if (statusCode != HttpStatus.SC_OK) {  
                return null;  
            }  
            HttpEntity entity = response.getEntity();  
            if (entity == null) {  
                return null;  
            }  
            httpStr = EntityUtils.toString(entity, chatset);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if (response != null) {  
                try {  
                    EntityUtils.consume(response.getEntity());  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        return httpStr;  
    }  
  
    /** 
     * 发送 SSL POST 请求（HTTPS），JSON形式 
     * @param apiUrl API接口URL 
     * @param json JSON对象 
     * @return 
     */  
    public static String doPostSSL(String apiUrl, Object json,String chatset) {  
        CloseableHttpClient httpClient = HttpClients.custom().setSSLSocketFactory(createSSLConnSocketFactory()).setConnectionManager(connMgr).setDefaultRequestConfig(requestConfig).build();  
        HttpPost httpPost = new HttpPost(apiUrl);  
        CloseableHttpResponse response = null;  
        String httpStr = null;  
  
        try {  
            httpPost.setConfig(requestConfig);  
            StringEntity stringEntity = new StringEntity(json.toString(),chatset);//解决中文乱码问题  
            stringEntity.setContentEncoding(chatset);  
            stringEntity.setContentType("application/json");  
            httpPost.setEntity(stringEntity);  
            response = httpClient.execute(httpPost);  
            int statusCode = response.getStatusLine().getStatusCode();  
            if (statusCode != HttpStatus.SC_OK) {  
                return null;  
            }  
            HttpEntity entity = response.getEntity();  
            if (entity == null) {  
                return null;  
            }  
            httpStr = EntityUtils.toString(entity, chatset);  
        } catch (Exception e) {  
            e.printStackTrace();  
        } finally {  
            if (response != null) {  
                try {  
                    EntityUtils.consume(response.getEntity());  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        return httpStr;  
    }  
  
    /** 
     * 创建SSL安全连接 
     * 
     * @return 
     */  
    private static SSLConnectionSocketFactory createSSLConnSocketFactory() {  
        SSLConnectionSocketFactory sslsf = null;  
        try {  
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {  
  
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {  
                    return true;  
                }  
            }).build();  
            sslsf = new SSLConnectionSocketFactory(sslContext, new X509HostnameVerifier() {  
  
                @Override  
                public boolean verify(String arg0, SSLSession arg1) {  
                    return true;  
                }  
  
                @Override  
                public void verify(String host, SSLSocket ssl) throws IOException {  
                }  
  
                @Override  
                public void verify(String host, X509Certificate cert) throws SSLException {  
                }  
  
                @Override  
                public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {  
                }  
            });  
        } catch (GeneralSecurityException e) {  
            e.printStackTrace();  
        }  
        return sslsf;  
    }  
  
    public static String doDelete(String apiUrl ,String chatset) {  
        CloseableHttpClient httpClient = HttpClients.createDefault();  
        String httpStr = null;  
        HttpDelete httpDelete = new HttpDelete(apiUrl);
        CloseableHttpResponse response = null;  
  
        try {  
        	httpDelete.setConfig(requestConfig);  
            response = httpClient.execute(httpDelete);  
            HttpEntity entity = response.getEntity();  
//            System.out.println(response.getStatusLine().getStatusCode());  
            httpStr = EntityUtils.toString(entity, chatset);  
        } catch (IOException e) {  
            e.printStackTrace();  
        } finally {  
            if (response != null) {  
                try {  
                    EntityUtils.consume(response.getEntity());  
                } catch (IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
        return httpStr;  
    }
    
    /** 
     * 测试方法 
     * @param args 
     */  
    public static void main(String[] args) throws Exception {  
       HttpUtil.doPost_Str("http://localhost:8080/setCustomInfo.action", "{\"birthday\":\"\",\"user_name\":\"吴雅云\",\"sex\":\"\",\"phone\":\"13960282090\",\"address\":\"\",\"id_num\":\"350500198111191043\",\"nation\":\"\",\"arch_num\":\"1581825\"}", "utf-8");
    }  
}