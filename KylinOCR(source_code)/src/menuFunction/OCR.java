package menuFunction;

import java.awt.image.BufferedImage;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import net.sourceforge.tess4j.util.ImageHelper;

public class OCR {
	
	private BufferedImage tempImage=null;
	String type="chi_sim";
	
	public void setLang(String tp){
		type=tp;
	}
	public OCR(){
	}
	public OCR(String type){
		if(type!=null){
			this.type=type;
		}
	}
	
    public String photoRec(BufferedImage photoImage){
    	String result=null;
    	ITesseract instance = new Tesseract();
    	instance.setLanguage(type);//chi_sim ：简体中文，根据需求选择语言库
    	
    	//图像预处理
    	//tempImage=ImageHelper.convertImageToGrayscale(photoImage);
    	tempImage=photoImage;
    	//tempImage=ImageHelper.convertImageToBinary(tempImage);
    	BufferedImage tempImage1=ImageHelper.getScaledInstance(tempImage, tempImage.getWidth()*10, tempImage.getHeight()*10);
    	//ResampleOp resampleOp = new ResampleOp(tempImage.getWidth()*6, tempImage.getHeight()*6);// 转换   
    	//BufferedImage tempImage1 = resampleOp.filter(tempImage,    null);  
    	
    	try {
            //Image grayImage =  GrayFilter.createDisabledImage(photoImage);
             result =  instance.doOCR(tempImage1);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
    	return result;
    }
    
    /** 
     * 中值滤波  
     * @param pix 像素矩阵数组 
     * @param w 矩阵的宽 
     * @param h 矩阵的高 
     * @return 处理后的数组 
     */  
}
