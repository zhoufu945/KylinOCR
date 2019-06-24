package ocrjava.utils;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Map;
/*
 * by 郑周甫
 * 2019.5.21
 * 
*/
public class baiduOCRget {
	
	public static String getOCR(BufferedImage choosePhoto){
		
		String result=null;
		Map<String, Object> wordForDeskResult;
		try {
			wordForDeskResult = GetWordForImgUtils.getWordForDesk(choosePhoto);
			Boolean isHasWord = (Boolean) wordForDeskResult.get("isHasWord");
			
			if (isHasWord) {
				
				
				result=(String) wordForDeskResult.get("result");
				/*	
			    System.err.println("识别到的数据：\r\n"  );
				System.err.println(result );
				System.err.println(">>>>>>>>>>>>>识别到的数据>>>>>>>>>>>>>>>"  );
				*/
			} 
			else{
				result="没有识别到数据，请检查网络。";
			}
			
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return result;
	}

}
