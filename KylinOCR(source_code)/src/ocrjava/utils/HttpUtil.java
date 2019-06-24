package ocrjava.utils;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class HttpUtil {
	/*
	 * by 郑周甫
	 * 2019.5.21
	 * 
	*/
	public static String post(String requestUrl, Map<String, String>  params) {
        try {
            String generalUrl = requestUrl ;
            URL url = new URL(generalUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            // 设置通用的请求属性
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Cookie", setCookie());
            connection.setUseCaches(false);
            connection.setDoOutput(true);
            connection.setDoInput(true);

            // 得到请求的输出流对象
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes(getUrlWithQueryString(params));
            out.flush();
            out.close();

            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> headers = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : headers.keySet()) {
                System.out.println(key + "--->" + headers.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = null;
            if (requestUrl.contains("nlp"))
                in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "GBK"));
            else
                in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String result = "";
            String getLine;
            while ((getLine = in.readLine()) != null) {
                result += getLine;
            }
            in.close();
           // System.out.println("result:" + result);
            return result;
        } catch (Exception e) {
        	return null;
           // throw new RuntimeException(e);
        	
        }
    }
	public static String getUrlWithQueryString( Map<String, String> params) { 
		 if (params == null) { 
			 return null; 
			 } 
		 StringBuilder builder = new StringBuilder(""); 
		 
		 int i = 0;
		 for (String key : params.keySet()) { 
			 String value = params.get(key); 
			 if (value == null) { 
				 // 过滤空的key 
				 continue; 
				 } 
			 if (i != 0) { 
				 builder.append('&'); 
				 }
			 builder.append(key);
			 builder.append('=');
			 builder.append(encode(value));
			 i++; 
			 } 
		 return builder.toString();
		 }
	 public static String encode(String input) { 
		 if (input == null) { 
			 return ""; 
			 }
		 try { 
			 return URLEncoder.encode(input, "utf-8"); 
			 } 
		 catch (UnsupportedEncodingException e) {
			 e.printStackTrace(); 
			 } return input; 
		}
	 
	//HttpClient获取Cookies
	 public static String setCookie() { 
		 
		 CloseableHttpClient httpClient = null; 
		 HttpPost httpPost = null;
		 String result = ""; 
		 try {
			 CookieStore cookieStore = new BasicCookieStore();
			 httpClient = HttpClients.custom().setDefaultCookieStore(cookieStore).build(); 
			 httpPost = new HttpPost("http://ai.baidu.com/tech/ocr/general"); 
			 
			 httpClient.execute(httpPost); 
			 List<org.apache.http.cookie.Cookie> cookies = cookieStore.getCookies();
			 
			 for (int i = 0; i < cookies.size(); i++) {
				 if(i==0){
					 result+=cookies.get(i).getName()+"="+cookies.get(i).getValue();
				 }
				 else
					 result+=";"+cookies.get(i).getName()+"="+cookies.get(i).getValue();
				 } 
		 } catch (Exception ex) { 
			 ex.printStackTrace();
			 /*JOptionPane.showMessageDialog(null, "请检查网络>>>>>>\r\n"+ex.toString(), "【Tips】",
						JOptionPane.INFORMATION_MESSAGE);
			 System.exit(0);*/
			 return result; 
			 } 
		 System.err.println(result);
		 return result; 
		 }
	
}
