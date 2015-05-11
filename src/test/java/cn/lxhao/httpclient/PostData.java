package cn.lxhao.httpclient;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.*;

/**
 * 封装了采用HttpClient发送HTTP请求的方法
 *  本工具所采用的是HttpComponents-Client-4.2.1
 *  ===================================================================================================
 *  开发HTTPS应用时，时常会遇到两种情况
 *  1、测试服务器没有有效的SSL证书,客户端连接时就会抛异常
 *     javax.net.ssl.SSLPeerUnverifiedException: peer not authenticated
 *  2、测试服务器有SSL证书,但可能由于各种不知名的原因,它还是会抛一堆烂码七糟的异常,诸如下面这两种
 *     javax.net.ssl.SSLException: hostname in certificate didn't match: <123.125.97.66> != <123.125.97.241>
 *     javax.net.ssl.SSLHandshakeException: sun.security.validator.ValidatorException: PKIX path building failed: sun.security.provider.certpath.SunCertPathBuilderException: unable to find valid certification path to requested target
 *  ===================================================================================================
 *  这里使用的是HttpComponents-Client-4.2.1创建的连接,所以就要告诉它使用一个不同的TrustManager
 *  由于SSL使用的模式是X.509,对于该模式,Java有一个特定的TrustManager,称为X509TrustManager
 *  TrustManager是一个用于检查给定的证书是否有效的类,所以我们自己创建一个X509TrustManager实例
 *  而在X509TrustManager实例中,若证书无效,那么TrustManager在它的checkXXX()方法中将抛出CertificateException
 *  既然我们要接受所有的证书,那么X509TrustManager里面的方法体中不抛出异常就行了
 *  然后创建一个SSLContext并使用X509TrustManager实例来初始化之
 *  接着通过SSLContext创建SSLSocketFactory,最后将SSLSocketFactory注册给HttpClient就可以了
 *  ===================================================================================================
 * @version v1.7
 * @history v1.0-->新建<code>sendGetRequest()</code>和<code>sendPostRequest()</code>方法
 * @history v1.1-->新增<code>sendPostSSLRequest()</code>方法,用于发送HTTPS的POST请求
 * @history v1.2-->新增<code>sendPostRequest()</code>方法,用于发送HTTP协议报文体为任意字符串的POST请求
 * @history v1.3-->新增<code>java.net.HttpURLConnection</code>实现的<code>sendPostRequestByJava()</code>
 * @history v1.4-->所有POST方法中增加连接超时限制和读取超时限制
 * @history v1.5-->重组各方法,并补充自动获取HTTP响应文本编码的方式,移除<code>sendPostRequestByJava()</code>
 * @history v1.6-->整理GET和POST请求方法,使之更为适用
 * @history v1.7-->修正<code>sendPostRequest()</code>请求的CONTENT_TYPE头信息,并优化各方法参数及内部处理细节
 * @create Feb 1, 2012 3:02:27 PM
 * @update Jul 23, 2013 1:18:35 PM
 * @author 玄玉<http://blog.csdn.net/jadyer>
 */
public class PostData {
    /**
     * 使用Java原生API编写发送HTTP_POST请求的工具类
     *
     * @create Mar 4, 2013 2:47:57 PM
     * @author 玄玉<;
     * @see 本工具类仅用于学习了解，实际中推荐使用HttpClientUtil工具类
     * @see 地址为
     */
    private PostData() {
    }/**
     * 发送HTTP_GET请求
     * @see 1)该方法会自动关闭连接,释放资源
     * @see 2)方法内设置了连接和读取超时时间,单位为毫秒,超时或发生其它异常时方法会自动返回"通信失败"字符串
     * @see 3)请求参数含中文时,经测试可直接传入中文,HttpClient会自动编码发给Server,应用时应根据实际效果决定传入前是否转码
     * @see 4)该方法会自动获取到响应消息头中[Content-Type:text/html; charset=GBK]的charset值作为响应报文的解码字符集
     * @   若响应消息头中无Content-Type属性,则会使用HttpClient内部默认的ISO-8859-1作为响应报文的解码字符集
     * @param reqURL 请求地址(含参数)
     * @return 远程主机响应正文
     */
    public static String sendGetRequest(String reqURL){
        String respContent = "通信失败"; //响应内容
        HttpClient httpClient = new DefaultHttpClient(); //创建默认的httpClient实例
        //设置代理服务器
        //httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost("10.0.0.4", 8080));
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000); //连接超时10s
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);         //读取超时20s
        HttpGet httpGet = new HttpGet(reqURL); //创建org.apache.http.client.methods.HttpGet
        try{
            HttpResponse response = httpClient.execute(httpGet); //执行GET请求
            HttpEntity entity = response.getEntity();            //获取响应实体
            if(null != entity){
                //respCharset=EntityUtils.getContentCharSet(entity)也可以获取响应编码,但从4.1.3开始不建议使用这种方式
                Charset respCharset = ContentType.getOrDefault(entity).getCharset();
                respContent = EntityUtils.toString(entity, respCharset);
                //Consume response content
                EntityUtils.consume(entity);
            }
            System.out.println("-------------------------------------------------------------------------------------------");
            StringBuilder respHeaderDatas = new StringBuilder();
            for(Header header : response.getAllHeaders()){
                respHeaderDatas.append(header.toString()).append("\r\n");
            }
            String respStatusLine = response.getStatusLine().toString(); //HTTP应答状态行信息
            String respHeaderMsg = respHeaderDatas.toString().trim();    //HTTP应答报文头信息
            String respBodyMsg = respContent;                            //HTTP应答报文体信息
            System.out.println("HTTP应答完整报文=[" + respStatusLine + "\r\n" + respHeaderMsg + "\r\n\r\n" + respBodyMsg + "]");
            System.out.println("-------------------------------------------------------------------------------------------");
        } catch (ConnectTimeoutException cte){
            //Should catch ConnectTimeoutException, and don`t catch org.apache.http.conn.HttpHostConnectException
            System.out.println("请求通信[" + reqURL + "]时连接超时,堆栈轨迹如下");
        } catch (SocketTimeoutException ste){
            System.out.println("请求通信[" + reqURL + "]时读取超时,堆栈轨迹如下");
        }catch(ClientProtocolException cpe){
            //该异常通常是协议错误导致:比如构造HttpGet对象时传入协议不对(将'http'写成'htp')or响应内容不符合HTTP协议要求等
            System.out.println("请求通信[" + reqURL + "]时协议异常,堆栈轨迹如下");
        }catch(ParseException pe){
            System.out.println("请求通信[" + reqURL + "]时解析异常,堆栈轨迹如下");
        }catch(IOException ioe){
            //该异常通常是网络原因引起的,如HTTP服务器未启动等
            System.out.println("请求通信[" + reqURL + "]时网络异常,堆栈轨迹如下");
        }catch (Exception e){
            System.out.println("请求通信[" + reqURL + "]时偶遇异常,堆栈轨迹如下");
        }finally{
            //关闭连接,释放资源
            httpClient.getConnectionManager().shutdown();
        }
        return respContent;
    }


    /**
     * 发送HTTP_POST请求
     * @see 1)该方法允许自定义任何格式和内容的HTTP请求报文体
     * @see 2)该方法会自动关闭连接,释放资源
     * @see 3)方法内设置了连接和读取超时时间,单位为毫秒,超时或发生其它异常时方法会自动返回"通信失败"字符串
     * @see 4)请求参数含中文等特殊字符时,可直接传入本方法,并指明其编码字符集encodeCharset参数,方法内部会自动对其转码
     * @see 5)该方法在解码响应报文时所采用的编码,取自响应消息头中的[Content-Type:text/html; charset=GBK]的charset值
     * @   若响应消息头中未指定Content-Type属性,则会使用HttpClient内部默认的ISO-8859-1
     * @param reqURL        请求地址
     * @param reqData       请求参数,若有多个参数则应拼接为param11=value11&22=value22&33=value33的形式
     * @param encodeCharset 编码字符集,编码请求数据时用之,此参数为必填项(不能为""或null)
     * @return 远程主机响应正文
     */
    public static String sendPostRequest(String reqURL, String reqData, String encodeCharset){
        String reseContent = "通信失败";
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
        HttpPost httpPost = new HttpPost(reqURL);
        //由于下面使用的是new StringEntity(....),所以默认发出去的请求报文头中CONTENT_TYPE值为text/plain; charset=ISO-8859-1
        //这就有可能会导致服务端接收不到POST过去的参数,比如运行在Tomcat6.0.36中的Servlet,所以我们手工指定CONTENT_TYPE头消息
        httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=" + encodeCharset);
        try{
            httpPost.setEntity(new StringEntity(reqData==null?"":reqData, encodeCharset));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                reseContent = EntityUtils.toString(entity, ContentType.getOrDefault(entity).getCharset());
                EntityUtils.consume(entity);
            }
        } catch (ConnectTimeoutException cte){
            System.out.println("请求通信[" + reqURL + "]时连接超时,堆栈轨迹如下");
        } catch (SocketTimeoutException ste) {
            System.out.println("请求通信[" + reqURL + "]时读取超时,堆栈轨迹如下");
        }catch(Exception e){
            System.out.println("请求通信[" + reqURL + "]时偶遇异常,堆栈轨迹如下");
        }finally{
            httpClient.getConnectionManager().shutdown();
        }
        return reseContent;
    }


    /**
     * 发送HTTP_POST_SSL请求
     * @see 1)该方法会自动关闭连接,释放资源
     * @see 2)该方法亦可处理普通的HTTP_POST请求
     * @see 3)当处理HTTP_POST_SSL请求时,默认请求的是对方443端口,除非reqURL参数中指明了SSL端口
     * @see 4)方法内设置了连接和读取超时时间,单位为毫秒,超时或发生其它异常时方法会自动返回"通信失败"字符串
     * @see 5)请求参数含中文等特殊字符时,可直接传入本方法,并指明其编码字符集encodeCharset参数,方法内部会自动对其转码
     * @see 6)方法内部会自动注册443作为SSL端口,若实际使用中reqURL指定的SSL端口非443,可自行尝试更改方法内部注册的SSL端口
     * @see 7)该方法在解码响应报文时所采用的编码,取自响应消息头中的[Content-Type:text/html; charset=GBK]的charset值
     * @   若响应消息头中未指定Content-Type属性,则会使用HttpClient内部默认的ISO-8859-1
     * @param reqURL        请求地址
     * @param params        请求参数
     * @param encodeCharset 编码字符集,编码请求数据时用之,当其为null时,则取HttpClient内部默认的ISO-8859-1编码请求参数
     * @return 远程主机响应正文
     */
    public static String sendPostSSLRequest(String reqURL, Map<String, String> params, String encodeCharset){
        String responseContent = "通信失败";
        HttpClient httpClient = new DefaultHttpClient();
        httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
        httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
        //创建TrustManager()
        //用于解决javax.net.ssl.SSLPeerUnverifiedException: peer not authenticated
        X509TrustManager trustManager = new X509TrustManager(){
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
            @Override
            public X509Certificate[] getAcceptedIssuers() {return null;}
        };
        //创建HostnameVerifier
        //用于解决javax.net.ssl.SSLException: hostname in certificate didn't match: <123.125.97.66> != <123.125.97.241>
        X509HostnameVerifier hostnameVerifier = new X509HostnameVerifier(){
            @Override
            public void verify(String host, SSLSocket ssl) throws IOException {}
            @Override
            public void verify(String host, X509Certificate cert) throws SSLException {}
            @Override
            public void verify(String host, String[] cns, String[] subjectAlts) throws SSLException {}
            @Override
            public boolean verify(String arg0, SSLSession arg1) {return true;}
        };
        try {
            //TLS1.0与SSL3.0基本上没有太大的差别,可粗略理解为TLS是SSL的继承者，但它们使用的是相同的SSLContext
            SSLContext sslContext = SSLContext.getInstance(SSLSocketFactory.TLS);
            //使用TrustManager来初始化该上下文,TrustManager只是被SSL的Socket所使用
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            //创建SSLSocketFactory
            SSLSocketFactory socketFactory = new SSLSocketFactory(sslContext, hostnameVerifier);
            //通过SchemeRegistry将SSLSocketFactory注册到HttpClient上
            httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", 443, socketFactory));
            //创建HttpPost
            HttpPost httpPost = new HttpPost(reqURL);
            //由于下面使用的是new UrlEncodedFormEntity(....),所以这里不需要手工指定CONTENT_TYPE为application/x-www-form-urlencoded
            //因为在查看了HttpClient的源码后发现,UrlEncodedFormEntity所采用的默认CONTENT_TYPE就是application/x-www-form-urlencoded
            //httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=" + encodeCharset);
            //构建POST请求的表单参数
            if(null != params){
                List<NameValuePair> formParams = new ArrayList<NameValuePair>();
                for(Map.Entry<String,String> entry : params.entrySet()){
                    formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
                }
                httpPost.setEntity(new UrlEncodedFormEntity(formParams, encodeCharset));
            }
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if (null != entity) {
                responseContent = EntityUtils.toString(entity, ContentType.getOrDefault(entity).getCharset());
                EntityUtils.consume(entity);
            }
        } catch (ConnectTimeoutException cte){
            System.out.println("请求通信[" + reqURL + "]时连接超时,堆栈轨迹如下");
        } catch (SocketTimeoutException ste){
            System.out.println("请求通信[" + reqURL + "]时读取超时,堆栈轨迹如下");
        } catch (Exception e) {
            System.out.println("请求通信[" + reqURL + "]时偶遇异常,堆栈轨迹如下");
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return responseContent;
    }

    public static void main2(String[] args) {
        //String reqURL = "http://113.240.245.61:5228/gd.app.webservice/GdAppService/APP_GetJGDT";
        //String reqURL = "http://www.baidu.com";

        //StringBuilder sb = new StringBuilder();
        //sb.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        //sb.append("<root>");
        //sb.append("<QueryCondition>");
        //sb.append("<ip>127.0.0.1</ip>");
        //sb.append("<hpzl>02</hpzl>");
        //sb.append("<hphm>湘A00000</hphm>");
        //sb.append("</QueryCondition>");
        //sb.append("</root>");

        Map<String, String> params = new HashMap<>();
        params.put("page", "3");
        //params.put("key", "1deddb327d6f46ca83e53295abda7c6e");
        String utfString = PostData.sendPostRequest("http://113.240.245.70:8080/CSCXYAPIServer/index.php?/message/fetchRealTraff", params.toString(), "UTF-8");

        StringBuilder sb = new StringBuilder();
        int i = -1;
        int pos = 0;

        while((i=utfString.indexOf("\\u", pos)) != -1){
            sb.append(utfString.substring(pos, i));
            if(i+5 < utfString.length()){
                pos = i+6;
                sb.append((char)Integer.parseInt(utfString.substring(i+2, i+6), 16));
            }
        }

        System.out.println(sb);
    }
    public static void main(String[] args) throws Exception{
        //JSONObject data = PostData.test("/message/fetchEvents", "");
        //System.out.println(data);
        Date date = new Date(111, 9, 17);
        System.out.println(date.getTime());
    }

    public static JSONObject test(String url, String param) {

        url = "http://113.240.245.70:8080/CSCXYAPIServer/index.php?" + url;

        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }

        return JSON.parseObject(result);
    }

    /**
     * 向指定 URL 发送POST方法的请求
     *
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }

}
