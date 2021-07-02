package com.example.firstspringboot.utils;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;

import javax.net.ssl.*;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Xnn on 2020/1/16 14:25
 */
public class HttpUtil {

    public final static String METHOD_GET = "GET";
    public final static String METHOD_PUT = "PUT";
    public final static String METHOD_DELETE = "DELETE";
    public final static String METHOD_POST = "POST";
    public final static String IS_TEST = "1";
    public final static String ENCODE_TYPE = "UTF-8";

    // 返回结果中的body参数
    public final static String RESPONSE_BODY = "response-body";
    public final static String RESPONSE_CODE = "response-code";

    private static class TrustAnyTrustManager implements X509TrustManager {

        public void checkClientTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
        }

        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[]{};
        }
    }

    private static class TrustAnyHostnameVerifier implements HostnameVerifier {
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }

    public static String sendPostHttps(String url, String param) {
        OutputStreamWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpsURLConnection conn = (HttpsURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            SSLContext sslContext = SSLContext.getInstance("SSL");
            TrustManager[] tm = {new TrustAnyTrustManager()};
            // 初始化
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 获取SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            conn.setRequestProperty("Accept-Charset", "UTF-8");
            conn.setRequestProperty("contentType", "UTF-8");
            conn.setRequestProperty("Content-type", "application/json;charset=UTF-8");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setSSLSocketFactory(ssf);
            // 获取URLConnection对象对应的输出流
            //out = new PrintWriter(conn.getOutputStream());

            out = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            // 发送请求参数
            out.write(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            int code = conn.getResponseCode();
            InputStream is = null;
            if (code == 200) {
                is = conn.getInputStream();
            } else {
                is = conn.getErrorStream();
            }
            in = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
            return null;
        }
        //使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;

    }

    public static Map<String, List<String>> rest(String serviceUrl, String parameter, String restMethod, Map<String, String> header) {

        InputStream is = null;
        InputStream in = null;
        OutputStream os = null;
        ByteArrayOutputStream baos = null;
        HttpsURLConnection con = null;
        Map<String, List<String>> map = new HashMap<>();
        try {
            URL url = new URL(serviceUrl);
            con = (HttpsURLConnection) url.openConnection();
            SSLContext ctx = null;
            SSLSocketFactory ssf = null;

            //	测试环境验证证书时使用
            if (IS_TEST.equals("1")) {
                try {
                    ctx = SSLContext.getInstance("SSL");
                    ctx.init(null, new TrustManager[]{new TrustAnyTrustManager()},
                            new java.security.SecureRandom());
                } catch (KeyManagementException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                ssf = ctx.getSocketFactory();
                con.setHostnameVerifier(new TrustAnyHostnameVerifier());
                con.setSSLSocketFactory(ssf);
            }
            con.setRequestMethod(restMethod);
            // 设置头部信息
            if (header != null && !header.isEmpty()) {
                for (Map.Entry<String, String> vo : header.entrySet()) {
                    con.setRequestProperty(vo.getKey(), vo.getValue());
                }
            } else {
                con.setRequestProperty("content-type", "application/json;charset=UTF-8");
            }

            // 如果请求方法为PUT,POST和DELETE设置DoOutput为真
            if (!METHOD_GET.equals(restMethod)) {
                con.setDoOutput(true);
                if (!METHOD_DELETE.equals(restMethod)) { // 请求方法为PUT或POST时执行
                    os = con.getOutputStream();
                    os.write(parameter.getBytes("UTF-8"));
                    os.close();
                }
            } else {
                // 请求方法为GET时执行
                in = con.getInputStream();
                byte[] b = new byte[1024];
                int result = in.read(b);
                while (result != -1) {
                    System.out.write(b, 0, result);
                    result = in.read(b);
                }
            }
            // 获取返回结果
            String encoding = con.getContentEncoding();
            int code = con.getResponseCode();

            // 保存返回结果代码
            List<String> codeList = new ArrayList<>(1);
            codeList.add(con + "");
            map.put(RESPONSE_CODE, codeList);

            if (code == 200 || code == 201) {
                is = con.getInputStream();
                // 返回成功，取返回的头部信息
                map.putAll(con.getHeaderFields());
            } else {
                is = con.getErrorStream();
            }
            int read = -1;
            baos = new ByteArrayOutputStream();
            while ((read = is.read()) != -1) {
                baos.write(read);
            }
            byte[] data = baos.toByteArray();
            String content = null;
            if (encoding != null) {
                content = new String(data, encoding);
            } else {
                content = new String(data, ENCODE_TYPE);
            }

            List<String> body = new ArrayList<>(1);
            body.add(content);
            map.put(RESPONSE_BODY, body);
            return map;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (is != null) {
                    is.close();

                }
                if (baos != null) {
                    baos.close();
                }
                if (in != null) {
                    in.close();
                }
                if (os != null) {
                    os.close();
                }
                if (con != null) {
                    con.disconnect();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static class TestObject{

        private String name;
        private String sex;
        private int age;
        private int value;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }
    }

    public static String getString(Object object) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Field[] fields = object.getClass().getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        for (Field field : fields) {
            String fieldName = field.getName();
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = object.getClass().getMethod(getter, new Class[] {});
            Object value = method.invoke(object, new Object[] {});
            if (value instanceof String){
                String s = (String)value;
                if (!s.trim().isEmpty()){
                    sb.append("&").append(fieldName).append("=").append(s);
                }
            } else if (value instanceof Integer){
                int i = (Integer) value;
                if (i!=0){
                    sb.append("&").append(fieldName).append("=").append(i);
                }
            }
        }
        return sb.toString().substring(1,sb.length());
    }

    public static void main(String[] args) {
//        System.out.println(sendPostHttps("https://rtcpns.cn-north-1.myhuaweicloud.com:443/rest/caas/datavoice/v1.0?userId=c911c47f8f6b22a24964a491e2687c19&privateNum=%2B8613866666666"
//                , "{\"privateNum\":\"+8613866666666\",\"userId\":\"c911c47f8f6b22a24964a491e2687c19\"}"));
//        String reqBody = "{\"auth\":{\"identity\":{\"methods\":[\"password\"],\"password\":{\"user\":{\"domain\":{\"name\":\"huaxiang\"},\"name\":\"yuyin\",\"password\":\"HXtest@123123\"}}},\"scope\":{\"project\":{\"name\":\"cn-north-1\"}}}}";
//        String reqUrl = "https://rtc.cn-north-1.myhuaweicloud.com/v3/auth/tokens";
//        Map restInfo = rest(reqUrl, reqBody, METHOD_POST, null);
//        System.out.println(JSONObject.toJSONString(restInfo));


//        TestObject t = new TestObject();
//        t.setAge(20);
//        t.setName("张三");
//        t.setSex(" ");
//        try {
//            getString(t);
//        } catch (NoSuchMethodException e) {
//            e.printStackTrace();
//        } catch (InvocationTargetException e) {
//            e.printStackTrace();
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }

//        String str = "{\"results\":[],\"total\":0}";

        /*for (int i=0;i<10000;i++){
            getHWRequestHeader();
        }*/

        /*String aa = "0d9034aaf40e4a63b4309b36f52bec162020-12-14T08:05:02Zt01GZ67JrL94mwJYQVb4j1jU75OkGYnG";

        String PasswordDigest = null;
        try {
            PasswordDigest = Base64.encodeBase64String((encrypt(aa, "SHA-256").getBytes()));
            System.out.println(PasswordDigest);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        getString();

    }

    private static void getString(){
        String cycleMonth = "202012";
        String serviceNum = "16510100069";
        String acctId = "1186438";
        String beginDay = "2020-12-01";
        String date = "2020-12-16";
        StringBuffer sb = new StringBuffer();
        sb.append(
                " select OTHER_FEE,BASE_FEE,CALL_TYPE,START_TIME,SERVICE_TYPE,MOBILE_NUMBER,LONG_TYPE,DURATION,ROAM_AREA,OPP_NUMBER,PRODUCT_NAME from   ");
        sb.append(" (");
        sb.append(" select '语音电话' AS service_type,    ");
        sb.append("        a . start_time AS start_time,  ");
        sb.append("        a . call_duration AS DURATION, ");
        sb.append("        (case a . call_type            ");
        sb.append("          when 1 then                  ");
        sb.append("           '语音主叫'                  ");
        sb.append("          when 2 then                  ");
        sb.append("           '语音被叫'                  ");
        sb.append("          when 3 then                  ");
        sb.append("           '视频语音主叫'              ");
        sb.append("          when 4 then                  ");
        sb.append("           '视频语音被叫'              ");
        sb.append("          when 7 then                  ");
        sb.append("           '国际短信'                  ");
        sb.append("          else                         ");
        sb.append("           '呼转'                      ");
        sb.append("        end) AS call_type,             ");
        sb.append("        a . other_party AS opp_number, ");
        sb.append("        (case self_dealer_code when 0 then a.visit_area when 2 then 0 end) AS roam_area,     ");
        sb.append("        (a . cfee / 1000) AS base_fee, ");
        sb.append("        (a . lfee / 1000) AS other_fee,");
        sb.append("        (case a . toll_type            ");
        sb.append("          when 0 then                  ");
        sb.append("           '本地 '                     ");
        sb.append("          when 1 then                  ");
        sb.append("           '本地区间'                  ");
        sb.append("          when 2 then                  ");
        sb.append("           '省内长途'                  ");
        sb.append("          when 3 then                  ");
        sb.append("           '省际长途'                  ");
        sb.append("          when 5 then                  ");
        sb.append("           '国际长途'                  ");
        sb.append("          when 6 then                  ");
        sb.append("           '国际长途'                  ");
        sb.append("          when 7 then                  ");
        sb.append("           '国际长途'                  ");
        sb.append("          when 4 then                  ");
        sb.append("           '港澳台长途'                ");
        sb.append("          else                         ");
        sb.append("           '本地 '                     ");
        sb.append("        end) AS long_type,             ");
        sb.append("        a . mdn AS mobile_number,      ");
        sb.append("        '" + cycleMonth + "' AS DIV_FIELD,         ");
        sb.append("        ' ' AS product_name ");
        sb.append("   from dr_g_" + cycleMonth + "_a a     where mdn='" + serviceNum + "' and account_id='" + acctId
                + "'      ");
        sb.append(" and START_TIME>=str_to_date('" + beginDay + "','%Y-%m-%d')");
        sb.append(" and START_TIME<=str_to_date('" + date + "','%Y-%m-%d') ");
        sb.append(" ) b ");
            /*sb.append(" where START_TIME>=str_to_date('" + beginDay + "','%Y-%m-%d')");
            sb.append(" and START_TIME<=str_to_date('" + date + "','%Y-%m-%d') ");*/
        sb.append(" order by START_TIME desc");
        System.out.println(sb.toString());
    }

    private static Map<String, String> getHWRequestHeader() {
        Map<String, String> header = new HashMap<>();
        // created UTC 时间
        String created = getUTCTimeStr();
        // 随机数
        String nonce = UUID.randomUUID().toString().replace("-","");
        // 加密文本
        String text = nonce + created + "t01GZ67JrL94mwJYQVb4j1jU75OkGYnG";

        String PasswordDigest = null;
        String PasswordDigest2 = null;
        try {
            PasswordDigest = encrypt(text, "SHA-256");
            PasswordDigest2 = encrypt2(text, "SHA-256");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!PasswordDigest.equals(PasswordDigest2)){
            System.out.println("javaUtil加密结果：" + PasswordDigest2);
            System.out.println("本地加密结果：" + PasswordDigest);
            System.out.println("加密结果是否相等：" + PasswordDigest.equals(PasswordDigest2));
            System.out.println("!!!!!!!!!!!!!!!");
        }
        String xWSSE = "UsernameToken Username=\"" + "QutIQj4wuhLzihNu9C7G8m0hEVcn" + "\",PasswordDigest=\"" + PasswordDigest + "\",Nonce=\"" + nonce + "\",Created=\"" + created + "\"";
        header.put("content-type", "application/json;charset=UTF-8");
        header.put("authorization", "WSSE realm=\"SDP\",profile=\"UsernameToken\",type=\"Appkey\"");
        header.put("x-wsse", xWSSE);

        return header;
    }

    public static String encrypt(String inputText, String algorithmName) throws Exception {
        if (StringUtils.isBlank(inputText)) {
            throw new IllegalArgumentException("请输入要加密的内容");
        }
        if (StringUtils.isBlank(algorithmName)) {
            algorithmName = "SHA-1";
        }

        MessageDigest m = MessageDigest.getInstance(algorithmName);
        m.update(inputText.getBytes("UTF-8"));
        return Base64.encodeBase64String(m.digest());
    }

    public static String encrypt2(String inputText, String algorithmName) throws Exception {
        if (StringUtils.isBlank(inputText)) {
            throw new IllegalArgumentException("请输入要加密的内容");
        }
        if (StringUtils.isBlank(algorithmName)) {
            algorithmName = "SHA-1";
        }

        MessageDigest m = MessageDigest.getInstance(algorithmName);
        m.update(inputText.getBytes("UTF-8"));
        return java.util.Base64.getEncoder().encodeToString(m.digest());
    }

    public static String getUTCTimeStr() {

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'") ;
        
        // 1、取得本地时间：
        Calendar cal = Calendar.getInstance() ;
        // 2、取得时间偏移量：
        int zoneOffset = cal.get(java.util.Calendar.ZONE_OFFSET);
        // 3、取得夏令时差：
        int dstOffset = cal.get(java.util.Calendar.DST_OFFSET);
        // 4、从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(java.util.Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        return format.format(new Date(cal.getTimeInMillis()));
    }
}
