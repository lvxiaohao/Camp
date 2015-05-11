package cn.lxhao.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2015-04-03.
 *
 * @author 吕浩
 * @since 1.0.0
 */
public class Post {

    public static String sendPostRequest(String reqURL, Map<String,String> params, String encodeCharset){
        String resContent = "通信失败";
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
        HttpPost httpPost = new HttpPost(reqURL);
        httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=" + encodeCharset);

        try{
            List<NameValuePair> formParams = new ArrayList<NameValuePair>();
            for(Map.Entry<String,String> entry : params.entrySet()){
                formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(formParams, encodeCharset));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                resContent = EntityUtils.toString(entity, ContentType.getOrDefault(entity).getCharset());
                EntityUtils.consume(entity);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            httpClient.getConnectionManager().shutdown();
        }
        return resContent;
    }

    public static void main(String[] args){
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        sb.append("<root>");
        sb.append("<QueryCondition>");
        sb.append("<ip>127.0.0.1</ip>");
        sb.append("<hpzl>02</hpzl>");
        sb.append("<hphm>A00000</hphm>");
        sb.append("</QueryCondition>");
        sb.append("</root>");

        Map<String, String> params = new HashMap<>();
        params.put("QueryXmlDoc", sb.toString());
        params.put("key", "1deddb327d6f46ca83e53295abda7c6e");


        System.out.println(Post.sendPostRequest("http://113.240.245.61:5228/gd.app.webservice/GdAppService/APP_GetJGDT", params, "UTF-8"));

    }
}
