package translate;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import net.sf.json.JSONObject;

import org.apache.http.HttpEntity;  
/*
 * by 郑周甫
 * 2019.5.16
 * 
*/

public class iciboTran { 
	
	public static String tarnslate(String word) throws ClientProtocolException, IOException{

		String form = "auto"; 
		String to = "auto"; 
		String words=word;
		
		String url = "http://fy.iciba.com/ajax.php?a=fy"; 
		Map<String, String> params = new HashMap<>(); 
		params.put("f", form); 
		params.put("t", to); 
		params.put("w", words); 
		CloseableHttpClient httpClient = HttpClients.createDefault();
		HttpPost request = new HttpPost(getUrlWithQueryString(url, params)); 
		CloseableHttpResponse response = httpClient.execute(request); 
		HttpEntity entity = response.getEntity(); 
		String result = EntityUtils.toString(entity, "utf-8"); 
		
		//System.out.println(result); 
		String[]re=result.split("\"content\":");
		
		String tempresult=re[1].replaceAll("}}", "}");;
		
		String translate=dispose(tempresult);
		
		//System.out.println("translate:"+translate );
		
		EntityUtils.consume(entity); 
		response.getEntity().getContent().close(); 
		response.close(); 
		
		return translate;
	}
	 public static String getUrlWithQueryString(String url, Map<String, String> params) { 
		 if (params == null) { 
			 return url; 
			 } 
		 StringBuilder builder = new StringBuilder(url); 
		 if (url.contains("?")) {
			 builder.append("&"); 
			 }
		 else { 
			 builder.append("?"); 
			 } 
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
	//对从网页上获取的信息进行提取
	 public static String dispose(String result){
		 String temp=null;
        JSONObject jsonObject = JSONObject.fromObject(result);
        String words = (String)jsonObject.get("out");
        if(words==null){
        	String[] word=result.split("\"word_mean\":\\[\"");
        	String tempRe=word[1].replaceAll("\"]}", "");
        	String[] wordsRe=tempRe.split("\",\"");
        	words=wordsRe[0]+"\n";
        	for(int i=1;i<wordsRe.length;i++){
        		words+=wordsRe[i]+"\n";
        	}
        	
        }
        if(words!=null){
        	temp = ascii2native(words);
        }
		
       return temp;
	 }
	 public static String ascii2native(String ascii) {

	        List<String> ascii_s = new ArrayList<String>();

	        String zhengz= "\\\\u[0-9,a-f,A-F]{4}";

	        Pattern p = Pattern.compile(zhengz);

	        Matcher m=p.matcher(ascii);

	        while (m.find()){

	            ascii_s.add(m.group());

	        }
	        for (int i = 0, j = 2; i < ascii_s.size(); i++) {

	            String code = ascii_s.get(i).substring(j, j + 4);

	            char ch = (char) Integer.parseInt(code, 16);

	            ascii = ascii.replace(ascii_s.get(i),String.valueOf(ch));

	        }

	        return ascii;

	}
	 
	 
		
	}
