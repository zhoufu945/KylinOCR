package menuFunction;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import net.sourceforge.tess4j.util.ImageHelper;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.CardLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class WordCloudWindow extends JFrame {

	/**
	 * by zzf
	 */
	private static final long serialVersionUID = 1L;
	private JButton saveButton;
	private JPanel contentPane;
	private JPanel ChartPanel;
	private JPanel wordCloudPanel ;
	private WordCloudFunction wd;
	private BufferedImage wordCloudImage=null;
	private BarChart chart;
	
	private List<String>results=new ArrayList<String>();
	private String path=null;

	public void resetWin(){
		results.clear();
		path=null;
		wordCloudImage=null;
	}
	public void setPath(String path){
		this.path=path;
		results.clear();
		wordCloudImage=null;
	}
	public void setList(List<String>results){
		this.path=null;
		this.results=results;
		wordCloudImage=null;
	}
	public  WordCloudWindow(List<String>results) {
		this.results=results;
		try {
			init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public  WordCloudWindow() {
		try {
			init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public  WordCloudWindow(String path) {
		this.path=path;
		try {
			init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		}
	public void beginWordCloud(){
		wordCloudPanel.repaint();
		new Thread(new Runnable() {
		    @Override
		    public void run() {
		    	//获取词云
		    	if(results.isEmpty()){
		    		wd=new WordCloudFunction(path);
		    	}
		    	else{
		    		wd=new WordCloudFunction(results);
		    	}
				wd.creatImage();
				wordCloudImage=wd.getImage();
				wordCloudPanel.repaint();
				saveButton.setVisible(true);
				chart.resetData(wd.getWordFrequency());
		    }
		}).start();
	}
	public void init() throws IOException {
		setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		setBackground(Color.WHITE);
		setTitle("词云");
		setResizable(false);
		
		//获取屏幕尺寸
		Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds((d.width-800)/2, (d.height-420)/2,800, 420);
		
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		//wordCloudPanel = new JPanel();
		wordCloudPanel = new drawImagePanel();
		wordCloudPanel.setBounds(12, 12, 380, 344);
		wordCloudPanel.setBackground(Color.WHITE);
		
		contentPane.add(wordCloudPanel);
		wordCloudPanel.setLayout(null);
		
		ChartPanel = new JPanel();
		ChartPanel.setBackground(Color.WHITE);
		ChartPanel.setBounds(404, 12, 380, 344);
		contentPane.add(ChartPanel);
		
		//加入柱状图
		chart=new BarChart();
		ChartPanel.add(chart.getChartPanel());
		ChartPanel.setLayout(new CardLayout(0, 0));
		
		saveButton = new JButton("保存结果");
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				wd.writeToFile();
			}
		});
		saveButton.setForeground(Color.BLUE);
		BufferedImage save= ImageIO.read(new File("./image/保存1.png"));
		BufferedImage save1=ImageHelper.getScaledInstance(save, save.getWidth()/10, save.getHeight()/10);
		Icon save2=new ImageIcon(save1);
		saveButton.setIcon(save2);
		saveButton.setFocusPainted(false);
		saveButton.setBackground(Color.WHITE);
		saveButton.setBounds(330, 360, 140, 26);
		saveButton.setVisible(false);
		contentPane.add(saveButton);
		
		beginWordCloud();
	}
	
	
	class drawImagePanel extends JPanel{
		
		/**
		 * by zzf
		 */
		private static final long serialVersionUID = 1L;
		public drawImagePanel(){
			//super();
			this.setBounds(12, 12, 380, 344);
		}
		public void paintComponent(Graphics g) 
		{
			//super();
			super.paintComponent(g);
			if(wordCloudImage!=null){
				int x=(this.getWidth()-wordCloudImage.getWidth())/2;
				int y=(this.getHeight()-wordCloudImage.getHeight())/2;
				g.drawImage(wordCloudImage,x, y,null);
			}
			else{
				BufferedImage open=null;
				try {
					open= ImageIO.read(new File("./image/word.png"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				BufferedImage open1=ImageHelper.getScaledInstance(open, open.getWidth(), open.getHeight());
				int x=(this.getWidth()-open1.getWidth())/2;
				int y=(this.getHeight()-open1.getHeight())/2;
				g.drawImage(open1,x, y, open1.getWidth(), open1.getHeight(),null);
				
				g.setFont(new Font("宋体", Font.BOLD, 17));
				g.setColor(Color.blue);
				g.drawString("正在拼命分析中>>>", 20, 40);
				
				
			}
			
		}
	}
}












