package menuFunction;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.bg.CircleBackground;
import com.kennycason.kumo.font.KumoFont;
import com.kennycason.kumo.font.scale.SqrtFontScalar;
import com.kennycason.kumo.nlp.FrequencyAnalyzer;
import com.kennycason.kumo.nlp.tokenizers.ChineseWordTokenizer;
import com.kennycason.kumo.palette.LinearGradientColorPalette;
/*
 * by 郑周甫
 * 2019.5.21
 * 
*/
public class WordCloudFunction {
	private BufferedImage wordCloudImage=null;
	private List<WordFrequency> wordFrequencyList=new ArrayList<WordFrequency>();
	private WordCloud wordCloud ;
	public WordCloudFunction(List<String>results){
		
		//建立词频分析器，设置词频，以及词语最短长度，此处的参数配置视情况而定即可
        FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
        frequencyAnalyzer.setWordFrequenciesToReturn(50);
        frequencyAnalyzer.setMinWordLength(2);
        frequencyAnalyzer.setMaxWordLength(5);
        
        //引入中文解析器
       frequencyAnalyzer.setWordTokenizer(new ChineseWordTokenizer());
       //指定文本文件路径，生成词频集合
       if(!wordFrequencyList.isEmpty()){
    	   wordFrequencyList.clear();
       }
       wordFrequencyList = frequencyAnalyzer.load(results);
       
        
	}
	public WordCloudFunction(String path){
			
			//建立词频分析器，设置词频，以及词语最短长度，此处的参数配置视情况而定即可
	        FrequencyAnalyzer frequencyAnalyzer = new FrequencyAnalyzer();
	        frequencyAnalyzer.setWordFrequenciesToReturn(70);
	        frequencyAnalyzer.setMinWordLength(2);
	        //frequencyAnalyzer.setMaxWordLength(5);
	        
	        //引入中文解析器
	       frequencyAnalyzer.setWordTokenizer(new ChineseWordTokenizer());
	       //指定文本文件路径，生成词频集合
	       if(!wordFrequencyList.isEmpty()){
	    	   wordFrequencyList.clear();
	       }
	       try {
			wordFrequencyList = frequencyAnalyzer.load(path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	       
		}
	public void creatImage(){
		//设置图片分辨率
        Dimension dimension = new Dimension(360,360);
        //此处的设置采用内置常量即可，生成词云对象
        wordCloud = new WordCloud(dimension,CollisionMode.PIXEL_PERFECT);
        //设置边界及字体
        wordCloud.setPadding(2);
        java.awt.Font font = new java.awt.Font("STSong-Light", 2, 40);
        //设置词云显示的三种颜色，越靠前设置表示词频越高的词语的颜色
        wordCloud.setColorPalette(new LinearGradientColorPalette(Color.RED, Color.BLUE, Color.GREEN, 10, 20));
        wordCloud.setKumoFont(new KumoFont(font));
        //设置背景色
        wordCloud.setBackgroundColor(new Color(255,255,255));
        //设置背景图层为圆形
        wordCloud.setBackground(new CircleBackground(180));
        
        wordCloud.setFontScalar(new SqrtFontScalar(10, 50));
        //生成词云
        wordCloud.build(wordFrequencyList);
        //保存词云
        wordCloudImage= wordCloud.getBufferedImage();
	}
	public BufferedImage getImage(){
		return wordCloudImage;
	}
	public List<WordFrequency> getWordFrequency(){
		return wordFrequencyList;
	}
	public void writeToFile() {
		// TODO Auto-generated method stub
		UIManager.put("FileChooser.saveButtonText", "保存"); //修改保存
		JFileChooser jfc=new JFileChooser();
		jfc.setDialogTitle("保存");
		
		//文件过滤器，用户过滤可选择的文件
		FileNameExtensionFilter filter=new FileNameExtensionFilter("png","PNG", "jpg","JPG");
		jfc.setFileFilter(filter);
		
		//初始化一个默认文件
		SimpleDateFormat sdf=new SimpleDateFormat("词云yyyy-mm-dd-HH:mm:ss");
		String filename=sdf.format(new Date());
		
		File filePath=FileSystemView.getFileSystemView().getHomeDirectory();
		File defaultFile=new File(filePath+File.separator+filename+".png");
		jfc.setSelectedFile(defaultFile);
		
		int flag=jfc.showSaveDialog(jfc);
		if(flag==JFileChooser.APPROVE_OPTION){
			File file=jfc.getSelectedFile();
			String path=file.getPath();
			//检查文件后缀，防止不正确的后缀
			if(!(path.endsWith(".png")||path.endsWith("png"))){
					path+=".png";
				}
			wordCloud.writeToFile(path);
		}		
		
		
	}
	
	
}