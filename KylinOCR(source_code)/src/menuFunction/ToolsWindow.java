package menuFunction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.JWindow;
import javax.swing.UIManager;

import java.awt.Color;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;

import net.sourceforge.tess4j.util.ImageHelper;
 
public class ToolsWindow extends JWindow{
 
	/**
	 *  by zhengzhoufu
	 */
	private static final long serialVersionUID = 1L;
	private ScreenChoose ssWin;
	
	private JButton changView =null;
	private JButton copy =null;
	private JButton ocr =null;
	
	//选定
	public void choose(){
		//工具栏恢复
		changView.setVisible(false);
		copy.setVisible(false);
		ocr.setVisible(true);
		ToolsWindow.this.pack();
		if(ssWin.resultTextArea.getText().isEmpty()){
			ssWin.ocrMenu.setResult(null);
		}
		else
			ssWin.ocrMenu.setResult(ssWin.resultTextArea.getText());
		ssWin.ocrMenu.drawPhoto(ssWin.saveImage);
		dispose();
		ssWin.ocrMenu.setVisible(true);
		ssWin.dispose();
	}
	
	//退出
	public void esc(){
		//工具栏恢复
		changView.setVisible(false);
		copy.setVisible(false);
		ocr.setVisible(true);
		ToolsWindow.this.pack();
		
		ToolsWindow.this.dispose();
		ssWin.ocrMenu.setVisible(true);
		ssWin.scrollPane.setVisible(false);
		ssWin.dispose();
		
	}
 
	public ToolsWindow(ScreenChoose ssWin,int x,int y){
		//this.setAlwaysOnTop(true);
		this.ssWin=ssWin ;
		try {
			this.init();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//将组件移到(x,y)的位置
		this.setLocation(x, y);
		//调整窗口的大小来适应控件
		this.pack();
		this.setVisible(true);
	}
 
	private void init() throws IOException {
		getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.X_AXIS));
		JToolBar toolBar=new JToolBar();
		toolBar.setBackground(Color.WHITE);
		toolBar.setFloatable(false);
		toolBar.setEnabled(false);
		
		//重新绘画选区
		BufferedImage reCho= ImageIO.read(new File("./image/垃圾桶.png"));
		BufferedImage reCho1=ImageHelper.getScaledInstance(reCho, reCho.getWidth()/18, reCho.getHeight()/21);
		Icon reCho2=new ImageIcon(reCho1);
		
		//确认选区
		BufferedImage right= ImageIO.read(new File("./image/18确定.png"));
		BufferedImage right1=ImageHelper.getScaledInstance(right, right.getWidth()/10, (int) (right.getHeight()/9.5));
		Icon right2=new ImageIcon(right1);
		JButton choose = new JButton("");
		choose.setIcon(right2);
		choose.setToolTipText("确认选区结果");
		choose.setBackground(Color.WHITE);
		toolBar.add(choose);
		
		
		//保存截图
		BufferedImage saveIma= ImageIO.read(new File("./image/截图.png"));
		BufferedImage save1=ImageHelper.getScaledInstance(saveIma, saveIma.getWidth()/10, (int) (saveIma.getHeight()/9.5));
		Icon save2=new ImageIcon(save1);
		JButton saveImage=new JButton("");
		saveImage.setToolTipText("保存截图结果");
		saveImage.setIcon(save2);
		saveImage.setBackground(Color.WHITE);
		toolBar.add(saveImage);
		
		
		//选区识别
		BufferedImage ocrIma= ImageIO.read(new File("./image/iconOCR.png"));
		BufferedImage ocr1=ImageHelper.getScaledInstance(ocrIma, ocrIma.getWidth()/10, (int) (ocrIma.getHeight()/9.5));
		Icon ocr2=new ImageIcon(ocr1);
		ocr = new JButton("");
		ocr.setToolTipText(" 文字识别 ");
		ocr.setIcon(ocr2);
		ocr.setBackground(Color.WHITE);
		toolBar.add(ocr);
		
		getContentPane().add(toolBar);
		
		//切换
		BufferedImage chang= ImageIO.read(new File("./image/refresh.png"));
		BufferedImage chang1=ImageHelper.getScaledInstance(chang, chang.getWidth()/7, (int) (chang.getHeight()/7));
		Icon chang2=new ImageIcon(chang1);
		
		changView = new JButton("");
		changView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				ssWin.changView();
			}
		});
		changView.setIcon(chang2);
		changView.setToolTipText("识别结果与原图切换");
		changView.setBackground(Color.WHITE);
		changView.setVisible(false);
		toolBar.add(changView);
		
		//复制识别结果
		BufferedImage copyIma= ImageIO.read(new File("./image/复制.png"));
		BufferedImage copy1=ImageHelper.getScaledInstance(copyIma, copyIma.getWidth()/10, (int) (copyIma.getHeight()/9.5));
		Icon copy2=new ImageIcon(copy1);
		
		copy = new JButton("");
		copy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String reStr=ssWin.resultTextArea.getText();
				
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
				 ToolsWindow.this.toFront();
			}
		});
		copy.setIcon(copy2);
		copy.setToolTipText("复制识别结果");
		copy.setBackground(Color.WHITE);
		copy.setVisible(false);
		toolBar.add(copy);
		
		
		//取消
		BufferedImage wrong= ImageIO.read(new File("./image/删除.png"));
		BufferedImage wrong1=ImageHelper.getScaledInstance(wrong, wrong.getWidth()/10, (int) (wrong.getHeight()/9.5));
		Icon wrong2=new ImageIcon(wrong1);
		
		JButton cancle = new JButton("");
		cancle.setIcon(wrong2);
		cancle.setToolTipText("取消选区操作");
		cancle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ToolsWindow.this.esc();
			}
		});
		JButton reChoose=new JButton("");
		reChoose.setToolTipText("重新绘画选区");
		reChoose.setIcon(reCho2);
		reChoose.setBackground(Color.WHITE);
		
		toolBar.add(reChoose);	
		
			reChoose.addActionListener(new ActionListener() {
				
				public void actionPerformed(ActionEvent e) {
					//工具栏恢复
					changView.setVisible(false);
					copy.setVisible(false);
					ocr.setVisible(true);
					ToolsWindow.this.pack();
					
					ssWin.resultTextArea.setText(null);
					ssWin.canEdit=true;
					ToolsWindow.this.setVisible(false);
					ssWin.reChoose();
				}
			});
		cancle.setBackground(Color.WHITE);
		toolBar.add(cancle);
		
		
		ocr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(ssWin.saveImage!=null){
					//识别时不可编辑选区
					ssWin.canEdit=false;
					ssWin.baiduOCR();
					changView.setVisible(true);
					copy.setVisible(true);
					ocr.setVisible(false);
					ToolsWindow.this.pack();
				}
				
			}
		});
		//保存按钮含图片类
		saveImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try{
					saveImage(ssWin.saveImage);
				}catch(IOException ex1){
					ex1.printStackTrace();
					
				}
			}
		});
		//确认选区
		choose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ToolsWindow.this.choose();
			}
		});
		
	}
	
		//保存图像到文件
		public void saveImage(BufferedImage saveImage) throws IOException{
			//写入文件
			if(saveImage!=null)
			{
				UIManager.put("FileChooser.saveButtonText", "保存图片"); //修改保存
				JFileChooser jfc=new JFileChooser();
				jfc.setDialogTitle("保存");
				
				//文件过滤器，用户过滤可选择的文件
				FileNameExtensionFilter filter=new FileNameExtensionFilter("png","PNG", "jpg","JPG");
				jfc.setFileFilter(filter);
				
				//初始化一个默认文件
				SimpleDateFormat sdf=new SimpleDateFormat("截图yyyy-mm-dd-HH:mm:ss");
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
					ImageIO.write(saveImage, "png", new File(path));
						}		
			}
			else
				 JOptionPane.showMessageDialog(null,"没有截图区域" , "【Tips】",
							JOptionPane.INFORMATION_MESSAGE);
		}
	
}