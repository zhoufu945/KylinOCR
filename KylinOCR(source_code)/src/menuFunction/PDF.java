package menuFunction;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import java.io.*;

import javax.swing.JOptionPane;

public class PDF {
    String filename;

    public  PDF(String filename){
        this.filename = filename;
    }

    public String PDFToDocx(){
        PDDocument document;

        try {
        	
            File file = new File(this.filename);  
            
            String destinationPath=this.filename.substring(0, filename .indexOf("."))+".docx";

            File outputFile = new File(destinationPath);
            if(outputFile.exists()){
                outputFile.delete();
            }
            outputFile.createNewFile();

            document = PDDocument.load(file);
            int pagenumber = document.getNumberOfPages();

            PDFTextStripper stripper = new PDFTextStripper();
            
            stripper.setSortByPosition(true);	// 排序
            stripper.setStartPage(1);			// 设置转换的开始页
            stripper.setEndPage(pagenumber);	// 设置转换的结束页

            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));
           // Writer writer=new OutputStreamWriter(new  FileOutputStream(outputFile),"GB2312");
            
            stripper.writeText(document, writer);
            stripper.restoreGraphicsState();
            document.close();
            writer.close();
            
            JOptionPane.showMessageDialog(null,"         转换完成\r\n文件保存在原文件地址中。" , "INFORMATION",
					JOptionPane.INFORMATION_MESSAGE);
            
            return destinationPath;
            
        } catch (Exception e){
            e.printStackTrace();
            
            JOptionPane.showMessageDialog(null,"         转换失败" , "INFORMATION",
					JOptionPane.ERROR_MESSAGE);
            
            return null;
        }
    }

}
