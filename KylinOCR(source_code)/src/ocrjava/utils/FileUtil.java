package ocrjava.utils;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

public class FileUtil {
    

    /**
     * 根据文件读取byte[] 数组
     */
    public static byte[] readFileByBytes(BufferedImage chooseImage) throws IOException {
        if (chooseImage==null) {
        	return null;
        } else {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            
            try {
                ImageIO.write(chooseImage,"png", out);
                out.flush();
               
            } catch (IOException e) {
                e.printStackTrace();
                
            } finally {
                try {
                    out.close();
                    
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            byte[] outToByteArray = out.toByteArray();
            return outToByteArray;
        }
	
    }
}
