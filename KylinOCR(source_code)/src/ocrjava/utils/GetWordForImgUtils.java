package ocrjava.utils;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GetWordForImgUtils {
	private static final String requestWordsUrl = "http://ai.baidu.com/aidemo";
	/*
	 * by 郑周甫
	 * 2019.5.21
	 * 
	*/
	
	/**
	 * 获取本地的图片上的文字
	 * @return
	 * 
	 * https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic
	 */
	public static Map<String, Object> getWordForDesk(BufferedImage chooseImage) throws IOException {

		// 将文件读取为字节数组
		byte[] readFileByBytes = FileUtil.readFileByBytes(chooseImage);

		String image_url = ""; 
		//String type="https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic";
		//String type="commontext";
		String type="https://aip.baidubce.com/rest/2.0/ocr/v1/accurate_basic";
		
		String imgStr = Base64Util.encode(readFileByBytes);
		Map<String, String> params = new HashMap<>(); 
		params.put("image", "data:image/png;base64," + imgStr); 
		params.put("image_url", image_url); 
		params.put("type", type); 
		params.put("detect_direction", "false"); 
		
		//String params = URLEncoder.encode("image", "UTF-8") + "=" + URLEncoder.encode(imgStr, "UTF-8");
		String result = HttpUtil.post(requestWordsUrl,  params);

		System.err.println(result);
		
		return ParseJsonUtils.parseWordsResult(result);
	}
}
