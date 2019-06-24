package menuFunction;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

import net.sourceforge.tess4j.util.ImageHelper;
import ocrjava.utils.baiduOCRget;

import javax.swing.JTextArea;
 
public class ScreenChoose extends JFrame {
 
	/**
	 *  by zhengzhoufu
	 */
	private static final long serialVersionUID = 1L;

	public Menu ocrMenu=null;

	//选区时定义参数
	private int fir_x,fir_y,sec_x,sec_y;
	
	//获取屏幕尺寸
	Dimension d=Toolkit.getDefaultToolkit().getScreenSize();
	
	//拖动图片时定位参数   width,height表示选区长和宽
	public int drag_fir_x,drag_fir_y,drag_sec_x,drag_sec_y,image_x,image_y,width,height=0;
	
	//图像的边界  例如imageW=offx+image>getWidth();
	private int imageW,imageH;
	
	//定义图片区域标示
	private boolean inPhoto=true;
	
	
	 // 获取整个屏幕的截图
	public BufferedImage image=null;
	private BufferedImage tempImage=null;
	public BufferedImage saveImage=null;
	public BufferedImage scaleImage=null;
	
	//图像保质
	BufferedImage proImage=null;
	
	//选择图片情况下的居中偏移量
	int offx=0;
	int offy=0;
	
	//识别值结果
	public String result=null;
	private ToolsWindow tools=null;
	public JTextArea resultTextArea=null;
	public JScrollPane scrollPane=null;
	
	//标记面板是否能编辑--用于识别结果锁定
	public boolean canEdit=true;
	//标记是否开始ocr
	public boolean beginOCR=false;
	//标记是否结束ocr
	public boolean endOCR=false;
	
	
	//标记鼠标的拖动动作
	private boolean mouseDrag=false;
	//拖动区域标记
	private boolean inArea=false;
	//标记是否选择了图片
	private boolean photoChoose=false;
	
	//百度云识别
	public void baiduOCR(){
		if(saveImage==null){
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
			    	setResult("正在识别...");
			    	
			    	String resultTemp=baiduOCRget.getOCR(saveImage);
			    	
			    	if(resultTemp.equals("没有识别到数据，请检查网络。")){
			    		setResult(resultTemp+"\r\n正在切换本地识别中，请耐心等待...");
			    		ScreenChoose.this.ocrMenu.ocr.setLang("chi_sim");
			    		resultTemp=  ScreenChoose.this.ocrMenu.ocr.photoRec(saveImage);
			    		
			    	}
			    	setResult(resultTemp);
			    		
		    		beginOCR=false;
	
					beginOCR=false;
			    }
			}).start();
			
		}
	}
	//平面内识别设置结果
	public void setResult(String result)
	{
		/*
		int w= Math.max(80, saveImage.getWidth());
		int h=Math.max(40, saveImage.getHeight());
		int x=(int) Math.min(image_x, d.getWidth()-w);
		int y=(int) Math.min(image_y, d.getWidth()-h);
		*/
		int w=saveImage.getWidth();
		int h=saveImage.getHeight();
		int x=image_x;
		int y=image_y;
		
		//scrollPane.setVisible(true);
		
		scrollPane.setBounds(x, y, w, h);
		
		resultTextArea.setText(result);
		
		scrollPane.setViewportView(resultTextArea);
		
		//resultTextArea.setVisible(true);
		scrollPane.setVisible(true);
	}
	
	//识别结果和原图交替显示
	public void changView(){
		
		if(!scrollPane.isVisible()){
			scrollPane.setVisible(true);
		}
		else{
			scrollPane.setVisible(false);
			this.repaint();
	    	
		}
		
	}
	
	//恢复初始
	public void reset(BufferedImage tp)
	{	
		fir_x=fir_y=sec_x=sec_y=0;
		drag_fir_x=drag_fir_y=drag_sec_x=drag_sec_y=image_x=image_y=width=height=0;
		
		if(tp==null){
			//获取默认屏幕设备
			GraphicsEnvironment environment=GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice screen=environment.getDefaultScreenDevice();
			Robot robot;
			try {
				robot = new Robot(screen);
				image=robot.createScreenCapture(new Rectangle(0, 0, d.width, d.height));
			} catch (AWTException e) {
				e.printStackTrace();
			}
			photoChoose=false;
			offx=offy=0;
			saveImage=null;
		}
		else{
			photoChoose=true;
			image=tp;
			proImage=tp;
			saveImage=tp;
			
			offx=(d.width-image.getWidth())/2;
			offy=(d.height-image.getHeight())/2;
			
			image_x=offx;
			image_y=offy;
			
			int y=(d.height+image.getHeight())/2;
			tools=new ToolsWindow(this,offx,y);
			//this.getContentPane().add(tools);
			tools.setVisible(true);
			
		}
		mouseDrag=inArea=false;
		tempImage=null;
		
		scaleImage=null;
		canEdit=true;
		this.scrollPane.setVisible(false);
		this.resultTextArea.setText(null);
		
		imageW=image.getWidth()+offx;
		imageH=image.getHeight()+offy;
		repaint();
	}

	//用户重绘选区
	public void reChoose()
	{	
		fir_x=fir_y=sec_x=sec_y=0;
		saveImage=null;
		if(ocrMenu!=null)
			this.ocrMenu.setVisible(false);
		this.scrollPane.setVisible(false);
		repaint();
	}
	//图片区域检测
	public boolean inPhoto(int x,int y)
	{	
		if(x<=offx || y<=offy || x>=imageW || y>=imageH)
			inPhoto=false;
		else 
			inPhoto=true;
		return inPhoto;
	}
	
	//判断是否在已选区域内
	private void isInArea(int x1,int x2,int y1,int y2,int point_x,int point_y)
	{
		if(inArea && mouseDrag && canEdit){}
		
		else if(point_x<Math.max(x1, x2) && point_y< Math.max(y1, y2) && 
				point_x>Math.min(x1, x2) && point_y>Math.min(y1, y2))
		{				
			inArea=true;			
		}
		else
			inArea=false;
	}
	
	public ScreenChoose(Menu ocrMenu,BufferedImage chooseImage) throws AWTException {
		
		//getContentPane().setBackground(Color.WHITE);
		getContentPane().setLayout(null);
		
		this.setUndecorated(true);
		
		this.ocrMenu=ocrMenu;
		
		Cursor curDef=new Cursor(Cursor.CROSSHAIR_CURSOR);
		ScreenChoose.this.setCursor(curDef);
		//识别结果
		scrollPane = new JScrollPane();
		scrollPane.setBackground(Color.WHITE);
	
		resultTextArea = new JTextArea();
		scrollPane.setViewportView(resultTextArea);
		resultTextArea.setFont(new Font("Dialog", Font.PLAIN, 15));
		resultTextArea.setBackground(Color.WHITE);
		resultTextArea.setEditable(true);

		getContentPane().add(scrollPane);
		resultTextArea.setOpaque(true);
		scrollPane.setOpaque(false);
		scrollPane.getViewport().setOpaque(false);
		
		scrollPane.setVisible(false);
		
		
		//判断是否上传图片
		if(chooseImage!=null)
			this.photoChoose=true;
				
		this.setBounds(0, 0, d.width, d.height);
		
		//获取屏幕截图
		if(!photoChoose)
		{
			//获取默认屏幕设备
			GraphicsEnvironment environment=GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice screen=environment.getDefaultScreenDevice();
			Robot robot=new Robot(screen);
			image=robot.createScreenCapture(new Rectangle(0, 0, d.width, d.height));	
		}
		
		//获取已上传定义图片
		else
		{
			image=chooseImage;
			proImage=chooseImage;
			saveImage=chooseImage;
			offx=(d.width-image.getWidth())/2;
			offy=(d.height-image.getHeight())/2;
			
			image_x=offx;
			image_y=offy;
			
			//int x=(d.width+image.getWidth())/2;
			int y=(d.height+image.getHeight())/2;
			tools=new ToolsWindow(this,offx,y);
			tools.setVisible(true);
		}
		//初始化图片参数
		imageW=image.getWidth()+offx;
		imageH=image.getHeight()+offy;
		
		//设置鼠标敲击的时间监听
		this.addMouseListener(new MouseAdapter() {
			int tmepX,tempY;
			@Override
			public void mouseClicked(MouseEvent e) {
				
				if(canEdit){
					//如果是单纯的点击事件，不更新fir_x，sec_x，因为之后定了每次鼠标按压都会更新
					fir_x=tmepX;
					fir_y=tempY;
					
					if(saveImage == null){
						//坐标清空
						fir_x=sec_x=fir_y=sec_y=0;
						if(tools == null){
							tools=new ToolsWindow(ScreenChoose.this,e.getX(),e.getY());
						}
						else{
							tools.setLocation(e.getX(),e.getY());
							tools.setVisible(true);
						}
						
					}
				}
			
			}
			//鼠标按下的事件监听
			@Override
			public void mousePressed(MouseEvent e) {
				if( canEdit){
					if(!inArea ){
						//用于之后判断为点击事件时恢复坐标
						tmepX=fir_x;
						tempY=fir_y;
						fir_x=e.getX();
						fir_y=e.getY();
					}
					else if(inArea )
					{
						drag_fir_x=e.getX();
						drag_fir_y=e.getY();
					}
					
					//重绘
					if(tools!=null ){
						tools.toFront();
						if(mouseDrag )
						{						
							tools.setVisible(false);
							mouseDrag=false;
							if(!inArea){
								saveImage=null;
							}
						}
					}
				}
				
			}
			
			//鼠标抬起的事件监听
			@Override
			public void mouseReleased(MouseEvent e) {
				if(canEdit){
					// 更新此时拖动后图片的位置,并做越界判断
					if(mouseDrag && inArea){
						int off_x=image_x+drag_sec_x-drag_fir_x;						
						int off_y=image_y+drag_sec_y-drag_fir_y;
						
						//越界判断					
						image_x=fir_x=off_x;
						image_y=fir_y=off_y;
						if(off_x<1+offx)
							image_x=fir_x=1+offx;
						if(off_y<1+offy)
							image_y=fir_y=1+offy;

						if(off_x+width >imageW-1){
							image_x=fir_x=imageW-width-1;
							sec_x=imageW-1;
						}
						else
							sec_x=off_x+width-1;
						if(off_y+height >imageH-1){
							image_y=fir_y=imageH-height-1;
							sec_y=imageH-1;
						}
						else
							sec_y=off_y+height-1;	
						}				
						
						int x=Math.min(fir_x, sec_x);
						int y=Math.max(fir_y, sec_y)+3;
						
						if(tools==null && saveImage!=null)
						{
							tools=new ToolsWindow(ScreenChoose.this,x,y);
							//ScreenChoose.this.getContentPane().add(tools);
						}
						//只用重画才重新定义工具位置
						else if(mouseDrag && !inArea)
							{						
								tools.setLocation(x,y);
								
							}				
						if(tools!=null){
							tools.setVisible(true);
							tools.toFront();
						}
						
						mouseDrag=false;
					}
			}
			
		});
		
		this.addMouseWheelListener(new MouseWheelListener(){
			
			
			   public void mouseWheelMoved(MouseWheelEvent e){
				   if(canEdit){
					   //执行图片的缩放功能
						if(photoChoose){
						
							fir_x=0;
							sec_x=0;
							fir_y=0;
							sec_y=0;
							inArea=false;
						
						//大于零表示向下滚动
						if(e.getWheelRotation()>0){
							if(image.getWidth()>300 || image.getHeight()>300){
								image=ImageHelper.getScaledInstance(proImage, (int)(image.getWidth()*0.9),(int)(image.getHeight()*0.9));
								 //ResampleOp resampleOp = new ResampleOp((int)(image.getWidth()*e.getScrollAmount()*scale),(int)(image.getHeight()*e.getScrollAmount()*scale));// 转换   
						         // image = resampleOp.filter(proImage,    null);   
								repaint();
								}
						    }
							//小于零表示是向上滚动
					    else{
					    	if(image.getWidth()<1400 && image.getHeight()<1400){
					    		image=ImageHelper.getScaledInstance(proImage, (int)(image.getWidth()*1.1),(int)(image.getHeight()*1.1));
					    		//ResampleOp resampleOp = new ResampleOp((int)(image.getWidth()/(e.getScrollAmount()*scale)),(int)(image.getHeight()/(e.getScrollAmount()*scale)));// 转换   
						        //image = resampleOp.filter(proImage,    null); 
					    		repaint();
					    		}
						    }
						//更新操作
						offx=(d.width-image.getWidth())/2+1;
						offy=(d.height-image.getHeight())/2+1;
						
						image_x=offx;
						image_y=offy;
						imageW=image.getWidth()+offx;
						imageH=image.getHeight()+offy;
						
						tools.setLocation(offx,imageH+3);
						
						saveImage=image;
						tempImage=null;
						//saveImage=null;
						}
				   }
				
			}
			   
		});
		
		//对于鼠标移动的监听
		this.addMouseMotionListener(new MouseMotionAdapter() {
			
			Cursor curHand=new Cursor(Cursor.HAND_CURSOR);
			Cursor curDef=new Cursor(Cursor.CROSSHAIR_CURSOR);
			
			//鼠标滑动的监听
			//鼠标进入已经选中区域时，变为移动对象
			public void mouseMoved(MouseEvent e) {
				
					if(tools!=null)
					{
							tools.toFront();
					}
											
					isInArea(fir_x,sec_x,fir_y,sec_y,e.getX(),e.getY());
					
					if(inArea){
						
						ScreenChoose.this.setCursor(curHand);
					}
					else
					{
						ScreenChoose.this.setCursor(curDef);
					}
					
			}
			
			public void mouseDragged(MouseEvent e) {
				if(canEdit){
					//临时图像，用于缓冲屏幕区域防止屏幕闪烁
					Image tempImage2=createImage(imageW-offx,imageH-offy);
					//用于绘图
					Graphics g=tempImage2.getGraphics();
					
				    RescaleOp ro=new RescaleOp(0.8f,0, null);
					tempImage=ro.filter(image, null);
					
					g.drawImage(tempImage, 0,0,null);
					
					//标记拖动事件
					mouseDrag=true;
					if(tools!=null)
						tools.setVisible(false);
					
					if(!inArea){
						sec_x=e.getX();
						sec_y=e.getY();
						
						//左上角
						if(sec_x<=offx)
							sec_x=offx+3;
						if(sec_y<=offy)
							sec_y= offy+2;
						//右下角
						if(sec_x>=imageW)
							sec_x=imageW-3;
						if(sec_y>=imageH)
							sec_y=imageH-3;
						
						image_x=Math.min(fir_x, sec_x);
						image_y=Math.min(fir_y, sec_y);
						
						width=Math.abs(sec_x-fir_x)+1;
						height=Math.abs(sec_y-fir_y)+1;
						
						//是否在图片区域检查
						if(inPhoto(fir_x, fir_y)){

							g.setColor(Color.RED);
							//保证图片矩形不被边框覆盖
							g.drawRect(image_x-1-offx, image_y-1-offy, width+1, height+1);
							//获得的Imge对象
							//选择图像 的情况下，注意图像有偏移
							saveImage=image.getSubimage(image_x-offx, image_y-offy, width, height);
							//用于画当前图像中的可用图像
							g.drawImage(saveImage, image_x-offx,image_y-offy, null);
						}
						else{
							
							saveImage=null;
							JOptionPane.showMessageDialog(null,"超出图像边界" , "【出错啦】",
									JOptionPane.ERROR_MESSAGE);
							
							
							fir_x=0;
							sec_x=0;
							fir_y=0;
							sec_y=0;
							
							inArea=false;
							
						}
						
						
					}
					
					
					//拖动动作
					else if(inArea)
					{	
						
							drag_sec_x=e.getX();
							drag_sec_y=e.getY();
							
							int off_x=drag_sec_x-drag_fir_x;
							int off_y=drag_sec_y-drag_fir_y;
							
							int x=image_x+off_x;						
							int y=image_y+off_y;
							
							//越界判断
							if(x<1+offx)
								x=1+offx;
							if(y<1+offy)
								y=1+offy;
							if(x+width >imageW-1){
								x=imageW-width-1;
							}
							if(y+height >imageH-1){
								y=imageH-height-1;
							}
							
							g.setColor(Color.RED);
							//保证图片矩形不被边框覆盖
							g.drawRect(x-1-offx,y-1-offy,width+1,height+1);
							
							//获得的Image对象
							saveImage=image.getSubimage(x-offx, y-offy, width, height);
							
							//用于画当前图像中的可用图像
							g.drawImage(saveImage, x-offx,y-offy, null);
						
							tools.setVisible(false);
							tools.setLocation(x,y+height+3);
											
						}	
					
					ScreenChoose.this.getGraphics().drawImage(tempImage2,offx,offy,ScreenChoose.this);
				
				}
				
			}
												
		});
		
		
		//快捷键
		this.addKeyListener(new KeyListener(){

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
					if(tools==null){
						tools=new ToolsWindow(ScreenChoose.this,0,0);
					}
					tools.esc();
				}
				if(e.getKeyCode()==KeyEvent.VK_ENTER){
					if(tools==null){
						tools=new ToolsWindow(ScreenChoose.this,0,0);
					}
					tools.choose();
				}
			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
	}
	
	//重写绘画的方法
	public void paint(Graphics g) {
		     //super.paint(g);
			if(canEdit ){
				if(photoChoose){
			    	g.clearRect(0, 0, d.width, d.height);
					tempImage=image;
					g.drawImage(tempImage, offx, offy,null);
			     }
			     else
			     {
			    	RescaleOp ro=new RescaleOp(0.8f,0, null);
					tempImage=ro.filter(image, null);
					g.drawImage(tempImage, offx, offy,null);
			     }
			}
		     
			else if(!this.scrollPane.isVisible()){
		    	//临时图像，用于缓冲屏幕区域防止屏幕闪烁
					Image tempImage2=createImage(imageW-offx,imageH-offy);
					//用于绘图
					Graphics g1=tempImage2.getGraphics();
					
				    RescaleOp ro=new RescaleOp(0.8f,0, null);
					tempImage=ro.filter(image, null);
					g1.drawImage(tempImage, 0,0,null);
					
					g1.setColor(Color.RED);
					//保证图片矩形不被边框覆盖
					g1.drawRect(image_x-1-offx,image_y-1-offy,saveImage.getWidth()+1,saveImage.getHeight()+1);
					
					//用于画当前图像中的可用图像
					g1.drawImage(saveImage, image_x-offx,image_y-offy, null);
					
					ScreenChoose.this.getGraphics().drawImage(tempImage2,offx,offy,ScreenChoose.this);
		     }
	}

}