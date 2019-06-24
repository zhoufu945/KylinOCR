package menuFunction;
import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import org.apache.http.client.ClientProtocolException;

import com.mortennobel.imagescaling.ResampleOp;

import net.sourceforge.tess4j.util.ImageHelper;
import ocrjava.utils.baiduOCRget;
import translate.iciboTran;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;

import javax.swing.JOptionPane;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.UIManager;
import javax.swing.JButton;
import javax.swing.JFileChooser;

import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class Menu extends JFrame {

	/**
	 * by 郑周甫
	 * 2019.3.25
	 */
	private static final long serialVersionUID = 1L;

	private JPanel contentPane;

	/**
	 * Create the frame.
	 * @throws IOException 
	 */
	
	//获得的图片
	BufferedImage choosePhoto=null;
	
	//PDF转换后word路径
	private String PDFpath=null;
	//标记是否开始识别
	boolean beginOCR=false; 
	//标记是否结束识别
	boolean endOCR=false; 
	
	
	public OCR ocr=new OCR();
	
	//画图面板
	private drawImagePanel drawImagePan=null;

	private ScreenChoose win;
	
	private logoPanel logo = null;
	private JPanel menuPanel;
	private JButton photoChoose;
	private JButton reChoose;
	private JButton larger;
	private JButton smaller;
	private JButton reFresh;
	private JButton copyButton;
	private JButton ocrEngButton;
	private JPanel resultMenuPanel;
	private JTextArea resultTextArea;
	private JPanel imageMenuPanel;
	private JScrollPane scrollPane ;
	
	//翻译时显示
	private JScrollPane tranScrollPane ;
	private JTextArea tranTextArea;
	private JTextField tranTipTextField;
	
	//批量读取时显示
	private JPanel groupPanel;
	private JButton rightButton;
	private JButton leftButton;
	private JTextField grouInfoTextField;
	private JButton groupOCRButton;
	
	//批量文件路径
	private String[] path=null;
	//private String[] results=null;
	private List<String>results=new ArrayList<String>();
	
	private int index=0;
	private JButton wordCloudBut;
	
	//清空
	public void resetView(){
		resetTran();
		//批量识别清空
		groupPanel.setVisible(false);
		path=null;
		//results=null;
		if(!results.isEmpty()){
			results.clear();
		}
		PDFpath=null;
		index=0;
	}
	
	public void setResult(String result)
	{
		resultTextArea.setText(result);
	}
	//批量百度云识别函数
	public void groupBaiduOCR(){

		index=0;
		path=getGroupImage();
		
		if(path!=null){
			
			//results=new String[path.length];
			if(!results.isEmpty()){
				results.clear();
			}
			new Thread(new Runnable() {
			    @Override
			    public void run() {
			    	beginOCR=true;
			    	for(int i =0;i<path.length;i++){
			    		
			    		setResult("正在识别第  "+(i+1)+"  图片");
			    		try {
			    			File file=new File(path[i]);
			    			grouInfoTextField.setText("  "+(i+1)+"/"+path.length+" : "+file.getName());
			    			
							choosePhoto = ImageIO.read(file);
							
					    	drawImagePan.repaint();
					    		
					    	String resultTemp=baiduOCRget.getOCR(choosePhoto);
									
					    	results.add(resultTemp);
							setResult(resultTemp);
							
							if(resultTemp.equals("没有识别到数据，请检查网络。")){
					    		setResult(resultTemp+"\r\n正在切换本地识别中，请耐心等待...");
					    		break;
					    	}
							
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    		
			    		index=i;
			    	}
			    	beginOCR=false;
					endOCR=true;
					drawImagePan.repaint();
					if(resultTextArea.getText().equals("没有识别到数据，请检查网络。"+"\r\n正在切换本地识别中，请耐心等待...")){
			    		groupOCR();
			    	}
					
			    }
			}).start();
			
		}
		
	}
	//批量本地识别函数
	public void groupOCR(){
		
		index=0;
		//path=getGroupImage();
		
		if(path!=null){
			
			//results=new String[path.length];
			if(!results.isEmpty()){
				results.clear();
			}
			new Thread(new Runnable() {
			    @Override
			    public void run() {
			    	beginOCR=true;
			    	for(int i =0;i<path.length;i++){
			    		if(i!=0){
			    			setResult("正在识别第  "+(i+1)+"  图片");
			    		}
			    		
			    		try {
			    			File file=new File(path[i]);
			    			grouInfoTextField.setText("  "+(i+1)+"/"+path.length+" : "+file.getName());
			    			
							choosePhoto = ImageIO.read(file);
							
					    	drawImagePan.repaint();
					    	ocr.setLang("chi_sim");
					    	String result=  ocr.photoRec(choosePhoto);
							
					    	//ssWin.repaint();
					    	results.add(result);
							//results[i]=result;
							setResult(result);
							
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
			    		index=i;
			    	}
			    	beginOCR=false;
					endOCR=true;
					drawImagePan.repaint();
			    }
			}).start();
			
		}
	}
	
	//获取批量图片文件夹目录
	public String[] getGroupImage(){
		//对真实的图像数目计数
		int imagesNum=0;
		String []trueImagesPath;
		
		//UIManager.put("FileChooser.cancelButtonText", "Cancel Button"); //修改取消
		UIManager.put("FileChooser.saveButtonText", "选择该文件夹"); //修改保存
		//UIManager.put("FileChooser.openButtonText", "Open Button");//修改打开
	
    	JFileChooser jfc=new JFileChooser();
    	jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		jfc.setDialogTitle("选择图像文件夹");
		
		//文件过滤器，用户过滤可选择的文件DIRECTORIES_ONLY
		FileNameExtensionFilter filter=new FileNameExtensionFilter("png","PNG");
		jfc.setFileFilter(filter);
	
		String groupImagePath=null;
		int flag=jfc.showSaveDialog(null);
		if(flag==JFileChooser.APPROVE_OPTION){
			
			File filesPath=jfc.getSelectedFile();
			groupImagePath=filesPath.getPath();
			
			File file = new File(groupImagePath);
			//文件夹判断
			if (!file.isDirectory()) {
				JOptionPane.showMessageDialog(null,"请选择文件夹" , "【出错啦】",
						JOptionPane.ERROR_MESSAGE);
			}
			else{
				String[] filelist = file.list();
				Arrays.sort(filelist);
				String []imagesPath=new String[filelist.length];
				for (int i = 0; i < filelist.length; i++) {
					 File readfile = new File(groupImagePath + "/" + filelist[i]);
			         if (!readfile.isDirectory()) {
			        	 
			        	 String pathname=readfile.getPath();
			        	 if(pathname.endsWith(".png")||pathname.endsWith(".PNG")){
			        		 imagesPath[imagesNum]=pathname;
			        		 imagesNum++;
			        	 }
 			            //System.out.println("path=" + readfile.getPath());
			            //System.out.println("name=" + readfile.getName());
			         }
				}
				//必须含有有效个PNG图片
				if(imagesNum>0){
					groupPanel.setVisible(true);
					//返回有效的图片文件
					trueImagesPath=new String[imagesNum];
					for(int i = 0; i < imagesNum; i++){
						trueImagesPath[i]=imagesPath[i];
					}
					return trueImagesPath;
				}
				else{
					JOptionPane.showMessageDialog(null,"文件夹内不含图片文件" , "【出错啦】",
							JOptionPane.ERROR_MESSAGE);
				}
				
			}
		}
		return null;
	}

	//翻译界面重新设置
	public void resetTran(){
		scrollPane.setSize(370, Menu.this.getHeight()-180);
		scrollPane.setViewportView(resultTextArea);
		tranTipTextField.setVisible(false);
		tranScrollPane.setVisible(false);
	}
	
	public void drawPhoto(BufferedImage p){
		if(!beginOCR){
			choosePhoto=p;
			drawImagePan.repaint();
		}
	
	}
	
	//本地识别
	public void beginOCR(String lan){
		
		resetTran();
		
		setResult(null);
		if(choosePhoto==null){
			JOptionPane.showMessageDialog(null,"没有图片喔！" , "【Tips】",
					JOptionPane.INFORMATION_MESSAGE);
		}
		else if(beginOCR){
				JOptionPane.showMessageDialog(null,"正在识别，请不要着急喔..." , "【Tips】",
						JOptionPane.INFORMATION_MESSAGE);
		}
		else{
			new Thread(new Runnable() {
			    @Override
			    public void run() {
			    	
			    	beginOCR=true;
			    	drawImagePan.repaint();
			    	ocr.setLang(lan);
			    	String result=  ocr.photoRec(choosePhoto);
					beginOCR=false;
					endOCR=true;
			    	//ssWin.repaint();
					drawImagePan.repaint();
					setResult(result);
			    }
			}).start();
		}	
		
	}
	
	//百度云识别
	public void baiduOCR(){
		resetTran();
		if(choosePhoto==null){
			JOptionPane.showMessageDialog(null,"没有图片喔！" , "【Tips】",
					JOptionPane.INFORMATION_MESSAGE);
		}
		else if(beginOCR){
			JOptionPane.showMessageDialog(null,"正在识别，请不要着急喔..." , "【Tips】",
					JOptionPane.INFORMATION_MESSAGE);
		}
		else{
			new Thread(new Runnable() {
			    @Override
			    public void run() {
			    	beginOCR=true;
			    	drawImagePan.repaint();
			    	setResult(null);
			    	
			    	setResult(baiduOCRget.getOCR(choosePhoto));
			    		
					beginOCR=false;
					drawImagePan.repaint();
			    	
			    }
			}).start();
		}
	}
	
	public BufferedImage chooseImage()
	{

		//UIManager.put("FileChooser.cancelButtonText", "Cancel Button"); //修改取消
		UIManager.put("FileChooser.saveButtonText", "读取图片"); //修改保存
		//UIManager.put("FileChooser.openButtonText", "Open Button");//修改打开
	
    	JFileChooser jfc=new JFileChooser();
		jfc.setDialogTitle("选择图片");
	
		//文件过滤器，用户过滤可选择的文件
		FileNameExtensionFilter filter=new FileNameExtensionFilter("png","PNG","jpg","jpeg");
		jfc.setFileFilter(filter);
		
		BufferedImage chooseImage=null;
		int flag=jfc.showSaveDialog(null);
		if(flag==JFileChooser.APPROVE_OPTION){
			File imageFile=jfc.getSelectedFile();
			String path=imageFile.getPath();
			//检查文件后缀，防止用户输入不正确的后缀
			//读出文件		
			if((path.endsWith(".png")||path.endsWith(".jpg")||path.endsWith(".JPG")||
					path.endsWith(".PNG")||path.endsWith(".jpeg"))){
				
				try {
					chooseImage = ImageIO.read(imageFile);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				return chooseImage;
			}
					
			else
			{
				JOptionPane.showMessageDialog(null,"请选择图像文件" , "【出错啦】",
						JOptionPane.ERROR_MESSAGE);
			}
		}
		return null;
	}
	
	//选择pdf文件
	public String choosePDF()
	{

		//UIManager.put("FileChooser.cancelButtonText", "Cancel Button"); //修改取消
		UIManager.put("FileChooser.saveButtonText", "读取PDF"); //修改保存
		//UIManager.put("FileChooser.openButtonText", "Open Button");//修改打开
	
    	JFileChooser jfc=new JFileChooser();
		jfc.setDialogTitle("选择PDF文件");
		
		//文件过滤器，用户过滤可选择的文件
		FileNameExtensionFilter filter=new FileNameExtensionFilter("pdf","PDF");
		jfc.setFileFilter(filter);
	
		String choosePDFPath=null;
		int flag=jfc.showSaveDialog(null);
		if(flag==JFileChooser.APPROVE_OPTION){
			File pdfFile=jfc.getSelectedFile();
			String path=pdfFile.getPath();
			//检查文件后缀，防止用户输入不正确的后缀
			if(!(path.endsWith(".pdf")||path.endsWith(".PDF"))){
				JOptionPane.showMessageDialog(null,"请选择PDF文件" , "【出错啦】",
						JOptionPane.ERROR_MESSAGE);
			}
			//读出文件				
			else
			{
				choosePDFPath = path;
				return choosePDFPath;
			}
		}
		return null;
	}
	
			//保存识别结果文件
	public void saveResult(String res) throws IOException{
				String reStr=res;
				
				UIManager.put("FileChooser.saveButtonText", "保存"); //修改保存
				
				JFileChooser jfc=new JFileChooser();
				jfc.setDialogTitle("保存");
				
				//文件过滤器，用户过滤可选择的文件
				FileNameExtensionFilter filter=new FileNameExtensionFilter("DOCX","docx");
				jfc.setFileFilter(filter);
				
				//初始化一个默认文件
				SimpleDateFormat resultFile=new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
				String filename=resultFile.format(new Date());
				
				File filePath=null;
				if(groupPanel.isVisible()){
					filePath=new File(path[0]);
					
				}
				else
					filePath=FileSystemView.getFileSystemView().getHomeDirectory();
				File defaultFile=new File(filePath+File.separator+filename+".docx");
				jfc.setSelectedFile(defaultFile);
				
				int flag=jfc.showSaveDialog(this);
				if(flag==JFileChooser.APPROVE_OPTION){
				
					File file=jfc.getSelectedFile();
					String path=file.getPath();
					//检查文件后缀，防止不正确的后缀
					if(!(path.endsWith(".docx"))){
						path+=".docx";
					}
					//写入文件
					  FileOutputStream out=null;
					   out = new FileOutputStream(new File(path));
				      out.write(reStr.getBytes("GBK"));
				      out.close();
				}
			}
	
	public Menu() throws IOException {
		//setResizable(false);
		this.setTitle("KylinOCR");
		
		setBackground(Color.WHITE);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setBounds(100, 100, 850, 500);
		
		//获取屏幕尺寸
		Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds((d.width-890)/2, (d.height-500)/2,890, 500);
		
		Dimension minimumSize=new Dimension(890, 500);
		this.setMinimumSize(minimumSize);
		
		contentPane = new JPanel();
		contentPane.setBackground(new Color(220, 220, 220));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		menuPanel = new MenuPanel();
		//menuPanel = new JPanel();
		menuPanel.setBackground(Color.DARK_GRAY);
		menuPanel.setBounds(0, 0, 130, 480);
		contentPane.add(menuPanel);//
		
		menuPanel.setLayout(null);
		
		JButton chooseScreen = new JButton("屏幕选区");
		chooseScreen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(beginOCR){
					JOptionPane.showMessageDialog(null,"正在识别，请不要着急喔..." , "【Tips】",
							JOptionPane.INFORMATION_MESSAGE);
				}
				else{
					resetView();
					Menu.this.setVisible(false);
					//等待主窗口消失
					try {
						Thread.sleep(150);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
						
					try {
						//ScreenChoose win;
						if(win==null){
							win = new ScreenChoose( Menu.this, null);
						}
						else
						{
							win.reset(null);
							//System.out.println("111");
						}
						win.setVisible(true);
							
					} catch (AWTException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
						
				}
							
			}
		});
		chooseScreen.setFocusPainted(false);
		chooseScreen.setBackground(Color.WHITE);
		chooseScreen.setOpaque(false);
		chooseScreen.setFont(new Font("Dialog", Font.BOLD, 17));
		chooseScreen.setForeground(Color.WHITE);
		chooseScreen.setBounds(0, 147, 131, 31);
		menuPanel.add(chooseScreen);
		
		photoChoose = new JButton("加载图片");
		photoChoose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(beginOCR){
					JOptionPane.showMessageDialog(null,"正在识别，请不要着急喔..." , "【Tips】",
							JOptionPane.INFORMATION_MESSAGE);
				}
				else{
					resetView();
					choosePhoto=chooseImage();
					setResult(null);
					drawImagePan.repaint();
				}
				
			}
		});
		photoChoose.setOpaque(false);
		photoChoose.setForeground(Color.WHITE);
		photoChoose.setFont(new Font("Dialog", Font.BOLD, 17));
		photoChoose.setFocusPainted(false);
		photoChoose.setBackground(Color.WHITE);
		photoChoose.setBounds(0, 178, 131, 31);
		menuPanel.add(photoChoose);
		
		logo = new logoPanel();
		menuPanel.add(logo);
		
		JButton PDFChang = new JButton("PDF转Word");
		PDFChang.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(beginOCR){
					JOptionPane.showMessageDialog(null,"正在识别，请不要着急喔..." , "【Tips】",
							JOptionPane.INFORMATION_MESSAGE);
				}
				else{
					resetView();
					choosePhoto=null;
					drawImagePan.repaint();
					
					resultTextArea.setText("正在加速转换中......");
					String PDFPath=choosePDF();
					if(PDFPath!=null){
						PDF pdf=new PDF(PDFPath);
						PDFpath=pdf.PDFToDocx();
					}
					resultTextArea.setText(null);
				}
				
			}
		});
		PDFChang.setOpaque(false);
		PDFChang.setForeground(Color.WHITE);
		PDFChang.setFont(new Font("Dialog", Font.BOLD, 15));
		PDFChang.setFocusPainted(false);
		PDFChang.setBackground(Color.WHITE);
		PDFChang.setBounds(0, 240, 131, 31);
		menuPanel.add(PDFChang);
		
		groupOCRButton = new JButton("批量识别");
		groupOCRButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(beginOCR){
					JOptionPane.showMessageDialog(null,"正在识别，请不要着急喔..." , "【Tips】",
							JOptionPane.INFORMATION_MESSAGE);
				}
				else{
					resetView();
					//groupOCR();
					groupBaiduOCR();
				}
				
			}
		});
		groupOCRButton.setOpaque(false);
		groupOCRButton.setForeground(Color.WHITE);
		groupOCRButton.setFont(new Font("Dialog", Font.BOLD, 17));
		groupOCRButton.setFocusPainted(false);
		groupOCRButton.setBackground(Color.WHITE);
		groupOCRButton.setBounds(0, 209, 131, 31);
		menuPanel.add(groupOCRButton);
		
		//词云按键
		BufferedImage wordBut= ImageIO.read(new File("./image/word.png"));
		BufferedImage wordBut1=ImageHelper.getScaledInstance(wordBut, wordBut.getWidth()/8, wordBut.getHeight()/8);
		Icon wordBut2=new ImageIcon(wordBut1);
		wordCloudBut = new JButton("词云分析");
		wordCloudBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(results.isEmpty() && PDFpath==null){
					JOptionPane.showMessageDialog(null,"请先进行批量识别或PDF分析" , "【Tips】",
							JOptionPane.INFORMATION_MESSAGE);
				}
				else if(beginOCR){
					JOptionPane.showMessageDialog(null,"正在识别，请不要着急喔..." , "【Tips】",
							JOptionPane.INFORMATION_MESSAGE);
				}
				else{
					if(!results.isEmpty()){
						WordCloudWindow wcw=new WordCloudWindow(results);
						wcw.setVisible(true);
						wcw.toFront();
					}
					else if(PDFpath!=null){
						WordCloudWindow wcw=new WordCloudWindow(PDFpath);
						wcw.setVisible(true);
						wcw.toFront();
					}
				}
			}
		});
		wordCloudBut.setIcon(wordBut2);
		wordCloudBut.setOpaque(false);
		wordCloudBut.setForeground(Color.WHITE);
		wordCloudBut.setFont(new Font("Dialog", Font.BOLD, 15));
		wordCloudBut.setFocusPainted(false);
		wordCloudBut.setBackground(Color.WHITE);
		wordCloudBut.setBounds(0, 271, 131, 31);
		wordCloudBut.setVisible(true);
		menuPanel.add(wordCloudBut);
		logo.setLayout(null);
		
		//drawImagePan = new JPanel();
		drawImagePan = new drawImagePanel();
		drawImagePan.setBounds(130, 23, 370, 350);
		contentPane.add(drawImagePan);
		BufferedImage reCho= ImageIO.read(new File("./image/裁剪.png"));
		BufferedImage reCho1=ImageHelper.getScaledInstance(reCho, reCho.getWidth()/3, reCho.getHeight()/3);
		Icon reCho2=new ImageIcon(reCho1);
		
		imageMenuPanel = new JPanel();
		imageMenuPanel.setBackground(Color.WHITE);
		imageMenuPanel.setBounds(130, 380, 370, 77);
		contentPane.add(imageMenuPanel);
		imageMenuPanel.setLayout(null);
		
		reChoose = new JButton("");
		reChoose.setBounds(12, 12, 69, 50);
		imageMenuPanel.add(reChoose);
		reChoose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetTran();
				if(choosePhoto!=null){
					//ScreenChoose win;
					try {
						if(win==null){
							win = new ScreenChoose( Menu.this, choosePhoto);
						}
						else{
							win.reset(choosePhoto);
						}
						win.setVisible(true);
						Menu.this.setVisible(false);
						
					} catch (AWTException e1) {
						e1.printStackTrace();
					}
				}
				else{
					JOptionPane.showMessageDialog(null,"没有图片喔！" , "【Tips】",
							JOptionPane.INFORMATION_MESSAGE);
				}
				
				
			}
		});
		reChoose.setBackground(Color.WHITE);
		reChoose.setFocusPainted(false);
		reChoose.setIcon(reCho2);
		
		
		BufferedImage larg= ImageIO.read(new File("./image/放大.png"));
		BufferedImage larg1=ImageHelper.getScaledInstance(larg, larg.getWidth()/14, larg.getHeight()/14);
		Icon larg2=new ImageIcon(larg1);
		BufferedImage smal= ImageIO.read(new File("./image/缩小.png"));
		BufferedImage smal1=ImageHelper.getScaledInstance(smal, smal.getWidth()/14, smal.getHeight()/14);
		Icon smal2=new ImageIcon(smal1);
		BufferedImage reFre= ImageIO.read(new File("./image/垃圾桶.png"));
		BufferedImage reFre1=ImageHelper.getScaledInstance(reFre, reFre.getWidth()/12, reFre.getHeight()/12);
		Icon reFre2=new ImageIcon(reFre1);
		
		larger = new JButton("");
		larger.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
		         Menu.this.drawImagePan.lager();
			}
		});
		larger.setBounds(105, 12, 69, 50);
		imageMenuPanel.add(larger);
		larger.setBackground(Color.WHITE);
		larger.setFocusPainted(false);
		larger.setIcon(larg2);
		
		smaller = new JButton("");
		smaller.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 Menu.this.drawImagePan.smaller();
			}
		});
		smaller.setBounds(196, 12, 69, 50);
		imageMenuPanel.add(smaller);
		smaller.setBackground(Color.WHITE);
		smaller.setFocusPainted(false);
		smaller.setIcon(smal2);
		
		reFresh = new JButton("");
		reFresh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				resetView();
				
				if(beginOCR){
					JOptionPane.showMessageDialog(null,"正在识别，请不要着急喔..." , "【出错啦】",
							JOptionPane.ERROR_MESSAGE);
				}
				else{
					setResult(null);
					drawPhoto(null);
				}
				
			}
		});
		reFresh.setBounds(283, 12, 69, 50);
		imageMenuPanel.add(reFresh);
		reFresh.setBackground(Color.WHITE);
		reFresh.setFocusPainted(false);
		reFresh.setIcon(reFre2);
		
		
		
		resultMenuPanel = new JPanel();
		resultMenuPanel.setBackground(Color.WHITE);
		resultMenuPanel.setBounds(506, 342, 370, 114);
		contentPane.add(resultMenuPanel);
		
		JButton saveButton = new JButton("保存结果");
		saveButton.setBounds(247, 62, 111, 48);
		saveButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//批量导出保存
				if(groupPanel.isVisible()){
					if(beginOCR){
						JOptionPane.showMessageDialog(null,"正在识别，请稍等。" , "【Tips】",
								JOptionPane.INFORMATION_MESSAGE);
					}
					else{
						//更新识别内容
						results.set(index, resultTextArea.getText());
						//results[index]=resultTextArea.getText();
						
						String reStr=path[0]+"\r\n";
						//reStr=reStr.concat(results[0]+"\r\n");
						reStr=reStr.concat(results.get(0)+"\r\n");
						for(int i=1;i<path.length;i++){
							reStr=reStr.concat(path[i]+"\r\n");
							reStr=reStr.concat(results.get(i)+"\r\n");
						}
						try {
							saveResult(reStr) ;
						
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
					
					
				}
				//非批量导出保存
				else{
					String reStr=resultTextArea.getText();
					if(tranScrollPane.isVisible() && !tranTextArea.getText().isEmpty()){
						String str="翻译结果为：\r\n"+tranTextArea.getText();
						reStr=reStr.concat(str);
					}
					if(reStr.isEmpty()){
						 JOptionPane.showMessageDialog(null,"没有信息可保存" , "【Tips】",
									JOptionPane.INFORMATION_MESSAGE);
					}
					else{
						try {
							saveResult(reStr) ;
						
						} catch (IOException e1) {
							e1.printStackTrace();
						}
					}
				}
				
				
			}
		});
		resultMenuPanel.setLayout(null);
		resultMenuPanel.add(saveButton);
		BufferedImage save= ImageIO.read(new File("./image/保存.png"));
		BufferedImage save1=ImageHelper.getScaledInstance(save, save.getWidth()/13, save.getHeight()/10);
		Icon save2=new ImageIcon(save1);
		saveButton.setIcon(save2);
		saveButton.setFocusPainted(false);
		saveButton.setBackground(Color.WHITE);
		
		ocrEngButton = new JButton("英文识别");
		ocrEngButton.setFont(new Font("Dialog", Font.BOLD, 12));
		ocrEngButton.setBounds(129, 12, 116, 48);
		ocrEngButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				beginOCR("eng");
				
			}
		});
		resultMenuPanel.add(ocrEngButton);
		BufferedImage ocreng= ImageIO.read(new File("./image/ocreng.png"));
		BufferedImage ocreng1=ImageHelper.getScaledInstance(ocreng, ocreng.getWidth()/8, ocreng.getHeight()/8);
		Icon ocreng2=new ImageIcon(ocreng1);
		ocrEngButton.setIcon(ocreng2);
		ocrEngButton.setFocusPainted(false);
		ocrEngButton.setBackground(Color.WHITE);
		
		JButton ocrButton = new JButton("普通识别");
		ocrButton.setBounds(12, 12, 116, 48);
		resultMenuPanel.add(ocrButton);
		ocrButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				beginOCR("chi_sim");
			}
		});
		BufferedImage rec= ImageIO.read(new File("./image/rec.png"));
		BufferedImage rec1=ImageHelper.getScaledInstance(rec, rec.getWidth()/24, rec.getHeight()/24);
		Icon rec2=new ImageIcon(rec1);
		ocrButton.setIcon(rec2);
		ocrButton.setFocusPainted(false);
		ocrButton.setBackground(Color.WHITE);
		
		copyButton = new JButton("复制结果");
		copyButton.setBounds(12, 62, 116, 48);
		copyButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String reStr=resultTextArea.getText();
				if(tranScrollPane.isVisible() && !tranTextArea.getText().isEmpty()){
					String str="翻译结果为：\r\n"+tranTextArea.getText();
					reStr=reStr.concat(str);
				}
				 if(reStr.isEmpty()){
					 JOptionPane.showMessageDialog(null,"没有信息可复制" , "【Tips】",
								JOptionPane.INFORMATION_MESSAGE);
				 }
				 else{
					 Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard(); 
					 Transferable tText = new StringSelection(reStr); 
					 clip.setContents(tText, null); 
					 JOptionPane.showMessageDialog(null,"已经复制到粘贴板" , "【Tips】",
								JOptionPane.INFORMATION_MESSAGE);
				 }
					
			}
		});
		resultMenuPanel.add(copyButton);
		BufferedImage copy= ImageIO.read(new File("./image/copy.png"));
		BufferedImage copy1=ImageHelper.getScaledInstance(copy, copy.getWidth()/21, copy.getHeight()/20);
		Icon copy2=new ImageIcon(copy1);
		
		copyButton.setIcon(copy2);
		copyButton.setFocusPainted(false);
		copyButton.setBackground(Color.WHITE);
		
		JButton baiduButton = new JButton("云识别");
		baiduButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				baiduOCR();
			}
		});
		baiduButton.setBounds(247, 12, 111, 48);
		BufferedImage baidu= ImageIO.read(new File("./image/百度.png"));
		BufferedImage baidu1=ImageHelper.getScaledInstance(baidu, baidu.getWidth()/21, baidu.getHeight()/20);
		Icon baidu2=new ImageIcon(baidu1);
		
		baiduButton.setIcon(baidu2);
		baiduButton.setFocusPainted(false);
		baiduButton.setBackground(Color.WHITE);
		resultMenuPanel.add(baiduButton);
		
		JButton transButton = new JButton("翻译");
		transButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!resultTextArea.getText().isEmpty() && !beginOCR){
					
					//翻译结果面板显示
					scrollPane.setSize(370, (Menu.this.getHeight()-180)/2);
					scrollPane.setViewportView(resultTextArea);
					tranTipTextField.setVisible(true);
					tranTextArea.setText(null);
					tranScrollPane.setVisible(true);
					
					new Thread(new Runnable() {
					    public void run() {
					    	String tranTemp=null;
						 	
						 	try {
								tranTemp=iciboTran.tarnslate(resultTextArea.getText());
							} catch (ClientProtocolException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								tranTemp="请检查网络！";
								/*JOptionPane.showMessageDialog(null,e , "【Tips】",
										JOptionPane.INFORMATION_MESSAGE);*/
								e.printStackTrace();
							}
							
						 	//resultTextArea.setText(temp[0]+"\n翻译结果为：\n"+tranTemp);
						 	tranTextArea.setText(tranTemp);
					    }}).start();
				}
				
			}
		});
		BufferedImage tran= ImageIO.read(new File("./image/翻译.png"));
		BufferedImage tran1=ImageHelper.getScaledInstance(tran, tran.getWidth()/23, tran.getHeight()/23);
		Icon tran2=new ImageIcon(tran1);
		
		transButton.setIcon(tran2);
		transButton.setFocusPainted(false);
		transButton.setBackground(Color.WHITE);
		transButton.setBounds(129, 62, 116, 48);
		resultMenuPanel.add(transButton);
		
		scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setBackground(Color.WHITE);
		scrollPane.setBounds(506, 23, 370, 320);
		contentPane.add(scrollPane);
		
		resultTextArea = new JTextArea();
		resultTextArea.setForeground(Color.DARK_GRAY);
		resultTextArea.setWrapStyleWord(true);
		resultTextArea.setLineWrap(true);
		resultTextArea.setDisabledTextColor(Color.WHITE);
		resultTextArea.setCaretColor(new Color(51, 51, 51));
		resultTextArea.setFont(new Font("华文中宋", Font.PLAIN, 15));
		resultTextArea.setBackground(Color.WHITE);
		scrollPane.setViewportView(resultTextArea);
		
		tranTipTextField = new JTextField();
		tranTipTextField.setBackground(Color.WHITE);
		tranTipTextField.setEditable(false);
		tranTipTextField.setForeground(Color.BLUE);
		tranTipTextField.setFont(new Font("Dialog", Font.PLAIN, 15));
		tranTipTextField.setText("翻译结果为：");
		tranTipTextField.setBounds(506, 182, 370, 19);
		contentPane.add(tranTipTextField);
		tranTipTextField.setColumns(10);
		tranTipTextField.setVisible(false);
		
		tranScrollPane = new JScrollPane();
		tranScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		tranScrollPane.setBackground(Color.WHITE);
		tranScrollPane.setBounds(506, 200, 370, 140);
		contentPane.add(tranScrollPane);
		
		tranTextArea = new JTextArea();
		tranTextArea.setWrapStyleWord(true);
		tranTextArea.setLineWrap(true);
		tranTextArea.setForeground(Color.DARK_GRAY);
		tranTextArea.setFont(new Font("华文中宋", Font.PLAIN, 15));
		tranTextArea.setDisabledTextColor(Color.WHITE);
		tranTextArea.setCaretColor(UIManager.getColor("Button.foreground"));
		tranTextArea.setBackground(Color.WHITE);
		tranScrollPane.setViewportView(tranTextArea);
		
		groupPanel = new JPanel();
		groupPanel.setBackground(Color.WHITE);
		groupPanel.setBounds(130, 0, 746, 22);
		contentPane.add(groupPanel);
		groupPanel.setLayout(null);
		groupPanel.setVisible(false);
		
		//左按键
		BufferedImage leftBut= ImageIO.read(new File("./image/left.png"));
		BufferedImage leftBut1=ImageHelper.getScaledInstance(leftBut, leftBut.getWidth()/6, leftBut.getHeight()/7);
		Icon leftBut2=new ImageIcon(leftBut1);
		
		leftButton = new JButton("");
		leftButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!beginOCR){
					
					//更新识别内容
					results.set(index, resultTextArea.getText());
					
					resetTran();
					if(index<=0){
						index=path.length;
					}
					index-=1;
					File file=new File(path[index]);
	    			grouInfoTextField.setText("  "+(index+1)+"/"+path.length+" : "+file.getName());
	    			
					try {
						choosePhoto = ImageIO.read(file);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
			    	drawImagePan.repaint();
					setResult(results.get(index));
					
				}
				
			}
		});
		leftButton.setIcon(leftBut2);
		leftButton.setFocusPainted(false);
		leftButton.setBackground(Color.WHITE);
		leftButton.setBounds(143, 0, 80, 22);
		groupPanel.add(leftButton);
		
		//右按键
		BufferedImage rightBut= ImageIO.read(new File("./image/right.png"));
		BufferedImage rightBut1=ImageHelper.getScaledInstance(rightBut, rightBut.getWidth()/6, rightBut.getHeight()/7);
		Icon rightBut2=new ImageIcon(rightBut1);
		rightButton = new JButton("");
		rightButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!beginOCR){
					
					//更新识别内容
					results.set(index, resultTextArea.getText());
					
					resetTran();
					
					if(index>=path.length-1){
						index=-1;
					}
					index+=1;
					File file=new File(path[index]);
	    			grouInfoTextField.setText("  "+(index+1)+"/"+path.length+" : "+file.getName());
	    			
					try {
						choosePhoto = ImageIO.read(file);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
			    	drawImagePan.repaint();
					setResult(results.get(index));
					
				}
				
			}
		});
		rightButton.setIcon(rightBut2);
		rightButton.setFocusPainted(false);
		rightButton.setBackground(Color.WHITE);
		rightButton.setBounds(523, 0, 80, 22);
		groupPanel.add(rightButton);
		
		grouInfoTextField = new JTextField();
		grouInfoTextField.setBackground(Color.WHITE);
		grouInfoTextField.setFont(new Font("Dialog", Font.PLAIN, 15));
		grouInfoTextField.setForeground(Color.BLUE);
		grouInfoTextField.setEditable(false);
		grouInfoTextField.setBounds(223, 0, 300, 23);
		groupPanel.add(grouInfoTextField);
		grouInfoTextField.setColumns(10);
		
		tranScrollPane.setVisible(false);
		
		// 注册快捷键方法
		//如过同时按下CTRL\shift\s按键实现屏幕选区功能
		ShortcutManager.getInstance().addShortcutListener(new ShortcutManager.ShortcutListener() {
			public void handle() {
				if(Menu.this.isVisible()==true){
					resetTran();
					Menu.this.setVisible(false);
					//等待主窗口消失
					try {
						Thread.sleep(150);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
						
						try {
							//ScreenChoose win;
							if(win==null){
								win = new ScreenChoose( Menu.this, null);
							}
							else
							{
								win.reset(null);
								//System.out.println("111");
							}
							win.setVisible(true);
							
						} catch (AWTException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					
				}
			}
		}, KeyEvent.VK_CONTROL,KeyEvent.VK_SHIFT,KeyEvent.VK_S);
		//忽略文本输入里面的快捷键事件, 当按钮得到焦点时, 快捷键事件能正常发生
		//ShortcutManager.getInstance().addIgnoredComponent(resultTextArea);
		
		//如过同时按下CTRL\shift\c按键实现图片选择功能
		ShortcutManager.getInstance().addShortcutListener(new ShortcutManager.ShortcutListener() {
			public void handle() {
				if(choosePhoto==null){
					choosePhoto=chooseImage();
					setResult(null);
					drawImagePan.repaint();
				}
			}
		}, KeyEvent.VK_CONTROL, KeyEvent.VK_SHIFT ,KeyEvent.VK_C);
		
		//监听窗口尺寸改变事件
		this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
            	//(890, 500);
            	menuPanel.setSize(130, Menu.this.getHeight());
            	
            	imageMenuPanel.setBounds(130, Menu.this.getHeight()-120, (Menu.this.getWidth()-520), 77);

            	reChoose.setBounds(12+(imageMenuPanel.getWidth()-370)/2, 12, 69, 50);
            	larger.setBounds(105+(imageMenuPanel.getWidth()-370)/2, 12, 69, 50);
            	smaller.setBounds(196+(imageMenuPanel.getWidth()-370)/2, 12, 69, 50);
            	reFresh.setBounds(283+(imageMenuPanel.getWidth()-370)/2, 12, 69, 50);
            	
            	//组按键面板
            	groupPanel.setBounds(130, 0, Menu.this.getWidth()-144, 22);
            	leftButton.setBounds(143+(groupPanel.getWidth()-746)/2, 0, 80, 22);
            	rightButton.setBounds(523+(groupPanel.getWidth()-746)/2, 0, 80, 22);
            	grouInfoTextField.setBounds(223+(groupPanel.getWidth()-746)/2, 0, 300, 23);
            	//wordAnalyseBut.setBounds(groupPanel.getWidth()-60, 0, 60, 22);
            	
            	drawImagePan.setBounds(130, 23, (Menu.this.getWidth()-520), Menu.this.getHeight()-150);
            	drawImagePan.resetWH();
            	
            	resultMenuPanel.setBounds(Menu.this.getWidth()-384, Menu.this.getHeight()-158, 370, 114);
            	
            	if(tranTipTextField.isVisible()){
            		//370, 320
                	scrollPane.setBounds(Menu.this.getWidth()-384, 23, 370, Menu.this.getHeight()/2-90);
                	scrollPane.setViewportView(resultTextArea);
                
            	}
            	else{
            		//370, 320
                	scrollPane.setBounds(Menu.this.getWidth()-384, 23, 370, Menu.this.getHeight()-180);
                	scrollPane.setViewportView(resultTextArea);
            	}
              	tranTipTextField.setBounds(Menu.this.getWidth()-384, Menu.this.getHeight()/2-67, 370, 19);
            	tranScrollPane.setBounds(Menu.this.getWidth()-384, Menu.this.getHeight()/2-49, 370, Menu.this.getHeight()/2-110);
            	tranScrollPane.setViewportView(tranTextArea);
            	
            	
            }
        });
        
	}
	
	class MenuPanel extends JPanel{
		
		/**
		 * by 郑周甫
		 */
		private static final long serialVersionUID = 1L;

		public MenuPanel(){
			super();
		}
		
		//重写绘画的方法
		public void paintComponent(Graphics g) 
		{
			super.paintComponent(g);
			BufferedImage menuPhoto=null;
			try {
				menuPhoto= ImageIO.read(new File("./image/menu.jpg"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			BufferedImage tempImage1=ImageHelper.getScaledInstance(menuPhoto, menuPhoto.getWidth()/6, menuPhoto.getHeight()/5);
			//g.drawImage(tempImage1, -5, 320, tempImage1.getWidth(), tempImage1.getHeight(),null);
			g.drawImage(tempImage1, -5, this.getHeight()-210, tempImage1.getWidth(), tempImage1.getHeight(),null);
			g.setColor(Color.WHITE);
			g.setFont(new Font("宋体", Font.PLAIN, 14));
			g.drawString("作者：", 0, this.getHeight()-60);
			
			g.drawString("郑周甫", 0, this.getHeight()-40);
			g.drawString("李宝成", 50, this.getHeight()-40);
			g.drawString("张爽", 100, this.getHeight()-40);
		}
	}
	
	class logoPanel extends JPanel{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public logoPanel(){
			//repaint();
			this.setBounds(0, 0, 150, 150);
		}
		
		//重写绘画的方法
		public void paintComponent(Graphics g) 
		{
			//super.paintComponent(g);
			BufferedImage logoPho=null;
			try {
				logoPho= ImageIO.read(new File("./image/logo.png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			BufferedImage tempImage1=ImageHelper.getScaledInstance(logoPho, logoPho.getWidth()/2, logoPho.getHeight()/2);
			g.drawImage(tempImage1, -35, 0, tempImage1.getWidth(), tempImage1.getHeight(),null);
			
		}
	}
	
	class drawImagePanel extends JPanel{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		int width=370;
		int height=350;
		
		//铺满图片显示面板的长宽
		int orWidth,orHeight;
		//缩放的长宽
		int drawimgWidth,drawimgHeight;
		//画图位置
		int x,y;
		
		//拖动需要的参数
		int fir_x,fir_y,sec_x,sec_y;
		int tx,ty;
		
		//缩放功能的缓存图片
		BufferedImage tempPhoto=null;
		
		//更新高宽
		public void resetWH(){
			width=this.getWidth();
			height=this.getHeight();
		}
		
		public drawImagePanel(){
			super();
			//this.width=w;
			//this.height=h;
			this.setBackground(Color.WHITE);
			this.setSize(width, height);
			
			//对于鼠标移动的监听
			this.addMouseListener(new MouseAdapter(){
				
				Cursor curHand=new Cursor(Cursor.HAND_CURSOR);
				Cursor curDef=new Cursor(Cursor.DEFAULT_CURSOR);
				
				public void mouseEntered(MouseEvent e) {
					drawImagePanel.this.setCursor(curHand);
				}
				public void mouseExited(MouseEvent e)	{
					drawImagePanel.this.setCursor(curDef);
				}
				public void mousePressed(MouseEvent e) {
					if(choosePhoto!=null && !beginOCR){
						fir_x=e.getX();
						fir_y=e.getY();
					}
				}
				public void mouseReleased(MouseEvent e) {
					x=tx;
					y=ty;
				}
				
			});
			//鼠标点击时选择图片
			this.addMouseListener(new MouseListener(){

				@Override
				public void mouseClicked(MouseEvent e) {
					// TODO Auto-generated method stub
					if(choosePhoto==null){
						choosePhoto=chooseImage();
						setResult(null);
						drawImagePan.repaint();
					}
				}

				@Override
				public void mousePressed(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseReleased(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void mouseExited(MouseEvent e) {
					// TODO Auto-generated method stub
					
				}
				
			});
			//对于鼠标移动的监听
			this.addMouseMotionListener(new MouseMotionAdapter() {
				public void mouseDragged(MouseEvent e) {
					if(choosePhoto!=null && !beginOCR){
						drawImagePanel.this.width=drawImagePanel.this.getWidth();
						drawImagePanel.this.height=drawImagePanel.this.getHeight();
						
						sec_x=e.getX();
						sec_y=e.getY();
						
						tx=x+sec_x-fir_x;
						ty=y+sec_y-fir_y;
						
						Menu.this.drawImagePan.getGraphics().clearRect(0, 0, width, height);
				        Menu.this.drawImagePan.getGraphics().drawImage(tempPhoto, tx,ty, null);
					}
					
				}
				
				
			});
			//便捷缩放
			this.addMouseWheelListener(new MouseWheelListener(){
				
				   public void mouseWheelMoved(MouseWheelEvent e){
					   //大于零表示向下滚动
						if(e.getWheelRotation()>0){
							drawImagePanel.this.smaller();
						}
						else
							drawImagePanel.this.lager();
				   }
			});
		}
		
		public void lager(){
			
			if(choosePhoto!=null && !beginOCR){
				if(drawimgWidth<1000||drawimgHeight<1000){
					drawimgWidth=(int)(drawimgWidth*1.1);
					drawimgHeight=(int)(drawimgHeight*1.1);
					ResampleOp resampleOp = new ResampleOp(drawimgWidth,drawimgHeight);// 转换   
			          tempPhoto = resampleOp.filter(choosePhoto,    null);   
			          x=(width-drawimgWidth)/2;
			          y=(height-drawimgHeight)/2;
			          Menu.this.drawImagePan.getGraphics().clearRect(0, 0, width, height);
			         Menu.this.drawImagePan.getGraphics().drawImage(tempPhoto, x, y, null);
				}
			
			}

		}
		
		public void smaller(){
			
			if(choosePhoto!=null && !beginOCR){
				if(drawimgWidth>orWidth){
					drawimgWidth=(int)(drawimgWidth*0.9);
					drawimgHeight=(int)(drawimgHeight*0.9);
					ResampleOp resampleOp = new ResampleOp(drawimgWidth,drawimgHeight);// 转换   
			         tempPhoto = resampleOp.filter(choosePhoto,    null);   
			         x=(width-drawimgWidth)/2;
			         y=(height-drawimgHeight)/2;
			         Menu.this.drawImagePan.getGraphics().clearRect(0, 0, width, height);
			         Menu.this.drawImagePan.getGraphics().drawImage(tempPhoto, x, y, null);
				}
				
			}
		}
		
		public void paintComponent(Graphics g) 
		{
			super.paintComponent(g);
			
			if(choosePhoto==null){
				tempPhoto=null;
				BufferedImage open=null;
				try {
					open= ImageIO.read(new File("./image/打开文件.png"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				BufferedImage open1=ImageHelper.getScaledInstance(open, open.getWidth()/2, open.getHeight()/2);
				x=(width-open1.getWidth())/2;
				y=(height-open1.getHeight())/2;
				g.drawImage(open1,x, y, open1.getWidth(), open1.getHeight(),null);
				
				g.setFont(new Font("宋体", Font.BOLD, 17));
				g.setColor(Color.blue);
				g.drawString("点击加载图片", x-20, y+80);
			}
			else{
				
				  //没有识别时图片正常显示
				orWidth=drawimgWidth=choosePhoto.getWidth();
				orHeight=drawimgHeight=choosePhoto.getHeight();
					
				//依据图像大小进行显示处理
					//图片小于显示框时
					if(orWidth<=this.width && orHeight<=this.height){
						int x=(width-orWidth)/2;
						int y=(height-orHeight)/2;
						g.drawImage(choosePhoto,x, y,orWidth,orHeight,null);

						tempPhoto=choosePhoto;
						
						}
					//图片大于显示框时
					else {
						//注意 int / int 还是整数，必须 ×1.0变为小数
						double scale=1.0*orWidth/width;
						double s1=1.0*orHeight/height;
						if(s1>scale){
							scale=s1;
							}
						orWidth=drawimgWidth=(int)(orWidth/scale);
						orHeight=drawimgHeight=(int)(orHeight/scale);
					    x=(width-drawimgWidth)/2;
					    y=(height-drawimgHeight)/2;
					    g.drawImage(choosePhoto,x, y,drawimgWidth,drawimgHeight,null);
						
						ResampleOp resampleOp = new ResampleOp(drawimgWidth,drawimgHeight);// 转换   
				          tempPhoto = resampleOp.filter(choosePhoto,    null);   
						
					}
					//开始识别人机交互 显示
					if(beginOCR){
						
						double imagSacel=this.height/350.0*1.2;
						double imagSacel1=this.height/350.0*0.6;
						BufferedImage open=null;
						BufferedImage open1=null;
						try {
							open= ImageIO.read(new File("./image/OCR.png"));
							open1= ImageIO.read(new File("./image/OCR1.png"));
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						BufferedImage ocrImage=ImageHelper.getScaledInstance(open, (int)(open.getWidth()*imagSacel), (int)( open.getHeight()*imagSacel));
						x=(width-ocrImage.getWidth())/2;
						y=(height-ocrImage.getHeight())/2;
						g.drawImage(ocrImage,x, y, ocrImage.getWidth(), ocrImage.getHeight(),null);
						
						BufferedImage ocrImage1=ImageHelper.getScaledInstance(open1, (int)(open1.getWidth()*imagSacel1), (int)( open1.getHeight()*imagSacel1));
						x=(width-ocrImage1.getWidth())/2;
						y=(height-ocrImage1.getHeight())/2;
						g.drawImage(ocrImage1,x, y, ocrImage1.getWidth(), ocrImage1.getHeight(),null);

						g.setColor(Color.BLUE);
						g.setFont(new Font("宋体", Font.BOLD, 16));
						//g.setColor(Color.ORANGE);
						g.drawString("正在识别中...", 20, 28);
					}
			}
			
		}
		
	}
}





