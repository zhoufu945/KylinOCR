package menuFunction;

import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import com.kennycason.kumo.WordFrequency;


/*
 * by 郑周甫
 * 2019.4.20
 * 
*/

public class BarChart{
	private ChartPanel chartPanel;
	private  JFreeChart chart;
	
	public  BarChart(){
		CategoryDataset dataset = getDataSet();
        chart = ChartFactory.createBarChart(
       		                "词频分析", // 图表标题
                            "高频词", // 文件夹轴的显示标签
                            "数量", // 数值轴的显示标签
                            dataset, // 数据集
                            PlotOrientation.HORIZONTAL, // 图表方向：水平、垂直VERTICAL
                            true,           // 是否显示图例(对于简单的柱状图必须是false)
                            false,          // 是否生成工具
                            false           // 是否生成URL链接
                            );
        
        CategoryPlot plot=chart.getCategoryPlot();//获取图表区域对象
        plot.setBackgroundPaint(Color.WHITE); 
        CategoryAxis domainAxis=plot.getDomainAxis();         //水平底部列表
         domainAxis.setLabelFont(new Font("黑体",Font.BOLD,14));         //水平底部标题
         domainAxis.setTickLabelFont(new Font("宋体",Font.BOLD,12));  //垂直标题
         ValueAxis rangeAxis=plot.getRangeAxis();//获取柱状
         rangeAxis.setLabelFont(new Font("黑体",Font.BOLD,15));
          chart.getLegend().setItemFont(new Font("黑体", Font.BOLD, 15));
          chart.getTitle().setFont(new Font("宋体",Font.BOLD,20));//设置标题字体
          
          // 自定义柱状图中柱子的样式
          CustomRenderer brender = new CustomRenderer();
          // 设置柱状图的顶端显示数字
          brender.setIncludeBaseInRange(true);
          brender.setBaseItemLabelGenerator(new StandardCategoryItemLabelGenerator());
          // 设置柱子为平面图不是立体的
          brender.setBarPainter(new StandardBarPainter());
          // 设置柱状图之间的距离0.1代表10%；
          brender.setItemMargin(0.2); 
          plot.setRenderer(brender);  
          
         chartPanel=new ChartPanel(chart,true);     
         //chartPanel.setBackground(Color.WHITE);
	}
	private static CategoryDataset getDataSet() {
           DefaultCategoryDataset dataset = new DefaultCategoryDataset();
            
          // dataset.addValue(100,rowKeys, "苹果");
           return dataset;
	   }
	public ChartPanel getChartPanel(){
		return chartPanel;
		
		}
	//更新数据
	public void resetData(List<WordFrequency> wordFrequencyList){
		CategoryPlot plot=chart.getCategoryPlot();//获取图表区域对象
		String rowKey =  "高频统计" ; 
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		
		for(int i=0;i<10;i++){
			dataset.addValue(wordFrequencyList.get(i).getFrequency(), rowKey, wordFrequencyList.get(i).getWord());
		}
		plot.setDataset(dataset); 
	}
}

class CustomRenderer extends org.jfree.chart.renderer.category.BarRenderer {  
	  
    /**
	 * by zzf
	 */
	private static final long serialVersionUID = 1L;
	private Paint[] colors;  
    //初始化柱子颜色  
    private String[] colorValues = { "#FF0000","#D64646", "#FF8E46","#F6BD0F","#AFD8F8", "#8BBA00", "#008E8E",
    		"#00FFFF","#7FFF00","#00FF00"};  
  
    public CustomRenderer() {  
        colors = new Paint[colorValues.length];  
        for (int i = 0; i < colorValues.length; i++) {  
            colors[i] = Color.decode(colorValues[i]);  
        }  
    }  
  
    //每根柱子以初始化的颜色不断轮循  
    public Paint getItemPaint(int i, int j) {  
        return colors[j % colors.length];  
    }  
}  
















