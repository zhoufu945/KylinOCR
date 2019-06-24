package ocrjava.utils;
import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/*
 * by 郑周甫
 * 2019.5.21
 * 
*/
/**
 * 解析json数据工具类
 */
public class ParseJsonUtils {
	/**
	 * 解析最后识别到的文字
	 * 
	 * @param result
	 * @return
	 */
	public static Map<String, Object> parseWordsResult(String result) {

		Map<String, Object> map = new HashMap<>();

		JSONObject jsonObject1 = JSONObject.fromObject(result);
		JSONObject jsonObject=null;
		JSONArray wordsResult=null;
		
		if(result !=null && !jsonObject1.get("data").equals("")){
			jsonObject = JSONObject.fromObject(jsonObject1.get("data"));
			wordsResult = jsonObject.getJSONArray("words_result");
		}
		
		if (wordsResult != null && wordsResult.size() > 0) {
			StringBuilder sb = new StringBuilder();

			for (int i = 0; i < wordsResult.size(); i++) {

				JSONObject wordsJsonObject = wordsResult.getJSONObject(i);
				String words = (String) wordsJsonObject.get("words");

				sb.append(words+"\n");
			}

			map.put("isHasWord", true);
			map.put("result", sb.toString());

		} else {
			map.put("isHasWord", false);
		}

		return map;
	}
}
