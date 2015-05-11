package cn.lxhao.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.dom4j.DocumentException;

import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created on 2015-04-03.
 *
 * @author 吕浩
 * @since 1.0.0
 */

public class HttpPostXml {

    public static String inter1(String type, String beginTime, String endTime) {
        String sb = 
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://ws.aj.com/\">" +
                        "<soapenv:Header>" +
                        "<soapenv:Body>" +
                        "<ws:APP_GetJGDT>" +
                        "<arg0>" + type + "</arg0>" +
                        "<arg1>" + beginTime + "</arg1>" +
                        "<arg2>" + endTime + "</arg2>" +
                        "<arg3>1deddb327d6f46ca83e53295abda7c6e</arg3>" +
                        "</ws:APP_GetJGDT>" +
                        "</soapenv:Body>" +
                        "</soapenv:Header>" +
                        "</soapenv:Envelope>";
        System.out.println(sb);
        return sb;
    }

    public static String inter2(String ip, String type, String number) {
        String sb =
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://ws.aj.com/\">" +
                        "<soapenv:Header>" +
                        "<soapenv:Body>" +
                        "<ws:APP_GetJDC_INFO>" +
                        "<arg0>" +
                        "<![CDATA[" +
                        "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                        "<root>" +
                        "<QueryCondition>" +
                        "<ip>"+ip+"</ip>" +
                        "<hpzl>"+type+"</hpzl>" +
                        "<hphm>"+number+"</hphm>" +
                        "</QueryCondition>" +
                        "</root>" +
                        "]]>" +
                        "</arg0>" +
                        "<arg1>1deddb327d6f46ca83e53295abda7c6e</arg1>" +
                        "</ws:APP_GetJDC_INFO>" +
                        "</soapenv:Body>" +
                        "</soapenv:Header>" +
                        "</soapenv:Envelope>";
        System.out.println(sb);
        return sb;
    }

    public static String inter3(String identity) {
        String sb =
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://ws.aj.com/\">" +
                        "<soapenv:Header>" +
                        "<soapenv:Body>" +
                        "<ws:APP_GetJSZ_INFO>" +
                        "<arg0>" +
                        "<![CDATA[" +
                        "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                        "<root>" +
                        "<QueryCondition>" +
                        "<sfzmhm >" + identity + "</sfzmhm >" +
                        "</QueryCondition>" +
                        "</root> " +
                        "]]>" +
                        "</arg0>" +
                        "<arg1>1deddb327d6f46ca83e53295abda7c6e</arg1>" +
                        "</ws:APP_GetJSZ_INFO>" +
                        "</soapenv:Body>" +
                        "</soapenv:Header>" +
                        "</soapenv:Envelope>";
        System.out.println(sb);
        return sb;
    }

    public static String inter4(String ip, String type, String number) {
        String sb =
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://ws.aj.com/\">" +
                        "<soapenv:Header>" +
                        "</soapenv:Header>" +
                        "<soapenv:Body>" +
                        "<ws:APP_GetJTWF_INFO>" +
                        "<arg0>" +
                        "<![CDATA[" +
                        "<?xml version=\"1.0\" encoding=\"GBK\"?>" +
                        "<root>" +
                        "<QueryCondition>" +
                        "<ip>"+ip+"</ip>" +
                        "<hpzl>"+type+"</hpzl>" +
                        "<hphm>"+number+"</hphm>" +
                        "</QueryCondition>" +
                        "</root>" +
                        "]]>" +
                        "</arg0>" +
                        "<arg1>1deddb327d6f46ca83e53295abda7c6e</arg1>" +
                        "</ws:APP_GetJTWF_INFO>" +
                        "</soapenv:Body>" +
                        "</soapenv:Envelope>";
        System.out.println(sb);
        return sb;
    }

    public static String inter5(String type, String number, String index) {
        String sb =
                "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                        "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:ws=\"http://ws.aj.com/\">" +
                        "<soapenv:Header>" +
                        "</soapenv:Header>" +
                        "<soapenv:Body>" +
                        "<ws:APP_GetKSCJ_INFO>" +
                        "<arg0>" +
                        "<![CDATA[" +
                        "<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                        "<bigdata>" +
                        "<values>lsh,xm,kscj,ksrq</values>" +
                        "<where>" +
                        "<sfzmhm>" + number + "</sfzmhm>" +
                        "<sfzmmc>" + type + "</sfzmmc>" +
                        "<tkskm>" + index + "</tkskm>" +
                        "</where>" +
                        "</bigdata>" +
                        "]]>" +
                        "</arg0>" +
                        "<arg1>1deddb327d6f46ca83e53295abda7c6e</arg1>" +
                        "</ws:APP_GetKSCJ_INFO>" +
                        "</soapenv:Body>" +
                        "</soapenv:Envelope>";
        System.out.println(sb);
        return sb;
    }

    public static void main(String[] args) throws IOException, DocumentException {

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://113.240.245.61:5228/gd.app.webservice/GdAppService");

        //StringEntity myEntity = new StringEntity(inter1("1101", "2015-04-08 00:00:00", "2015-04-09 18:00:00"), "UTF-8");
        //StringEntity myEntity = new StringEntity(inter2("127.0.0.1", "02", "A6479J"), "UTF-8");
        //StringEntity myEntity = new StringEntity(inter3("430103196306290533"), "UTF-8");
        StringEntity myEntity = new StringEntity(inter4("127.0.0.1", "02", "湘A6749J"), "UTF-8");
        //StringEntity myEntity = new StringEntity(inter5("A", "431124199601208236", "01"), "UTF-8");

        httppost.addHeader("Content-Type", "text/xml");
        httppost.setEntity(myEntity);
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        InputStreamReader reader = new InputStreamReader(resEntity.getContent(), "UTF-8");
        char[] buff = new char[1024];
        int length;
        StringBuilder stringBuilder = new StringBuilder();
        while ((length = reader.read(buff)) != -1) {
            String line = new String(buff, 0, length);
            stringBuilder.append(line);
        }
        String line = stringBuilder.toString();
        line = line.replace("&lt;", "<");
        line = line.replace("&gt;", ">");
        line = line.replace("&apos;", "\'");
        line = line.replace("&quot;", "\"");
        line = line.replace("&#xD;", "\r");
        line = line.replace("&amp;", "&");
        line = line.replace("&nbsp;", " ");
        System.out.println(line);



        // DOM4J 解析 XML文件
        //SAXReader saxReader = new SAXReader();
        //Document doc = saxReader.read(reader);
//
        //Element root = doc.getRootElement();
        //Element soapBody = root.element("Body");
        //Element ns1 = soapBody.element("APP_GetJGDTResponse");
        //Element ret = ns1.element("return");
//
        //String line = (String)ret.getData();
        //line = line.replace("UTF-8", "GBK");
        //line = line.replace("&lt;", "<");
        //line = line.replace("&gt;", ">");
        //line = line.replace("&apos;", "\'");
        //line = line.replace("&quot;", "\"");
        //line = line.replace("&#xD;", "\r");
        //line = line.replace("&amp;", "&");
        //line = line.replace("&nbsp;", " ");
        ////System.out.println(line);
//
        //Document childDoc = saxReader.read(new ByteArrayInputStream(line.getBytes()));
        //Element childRoot = childDoc.getRootElement();
        //Element code = childRoot.element("code");
        //Element message = childRoot.element("message");
        //Element result = childRoot.element("result");
//
        //Iterator iterator = result.elementIterator("row");
        //while (iterator.hasNext()) {
        //    Element row = (Element) iterator.next();
        //    Element content = row.element("CONTENT");
        //    Element infoDate = row.element("INFODATE");
        //    Element title = row.element("TITLE");
        //    System.out.println(title.getData());
        //}

        //if (rowList == null || rowList.isEmpty()) {
        //   // return null;
        //    System.out.println("null");
        //} else {
        //    System.out.println(rowList);
        //}
        httpclient.getConnectionManager().shutdown();
    }
}