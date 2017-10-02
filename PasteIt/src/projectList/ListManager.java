package projectList;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import trashCan.TrashList;
import utility.ComponentMover;

public class ListManager extends JInternalFrame {
	private static final long serialVersionUID = 1L;
	private ArrayList<singleList> listList = new ArrayList<singleList>();
    private JLabel title;
    private JMenuItem newAList;
    private JMenuItem close;
    private JPanel customTitleBar;
    private ArrayList<JLabel> item = new ArrayList<JLabel>();
    private JPanel listPanel;
    private JScrollPane ScrollPane;
    private JDesktopPane desktopPane;
    private String loadDirectory = "/Desktop/PasteIt/ListManger";
    private boolean editList = true;
    private int oldIndex = 0;
    
    public ListManager(JDesktopPane desktopPane) {
		this.desktopPane = desktopPane;
	}
    // 初始設定視窗
    public void initFrame() {
    	this.setSize(300, 500);
    	this.setResizable(false);
    	this.setClosable(false);
    	this.setMaximizable(false);
    	this.setIconifiable(false);
    	this.setBorder(null);
        BasicInternalFrameUI basicInternalFrameUI = (BasicInternalFrameUI)this.getUI();
        basicInternalFrameUI.setNorthPane(null);
		this.setVisible(true);
    }// end method initFrame
    
    // 初始設定標題Bar
	public void initCustomTitleBar() {
		this.customTitleBar = new JPanel();
		this.title = new JLabel("清單列表");
		this.title.setFont(new Font("微軟正黑體", Font.PLAIN, 20));
		this.title.setPreferredSize(new Dimension(193, 20));
		this.customTitleBar.add(this.title);
		this.newAList = new JMenuItem();		
		
		URL path = ListManager.class.getResource("/images/add.png");
		this.newAList.setIcon(new ImageIcon(path));
    	this.newAList.setToolTipText("新增清單");
    	this.newAList.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	addNewList();
            }
        });
    	this.customTitleBar.add(this.newAList);
		
    	this.close = new JMenuItem();		
		path = ListManager.class.getResource("/images/close.png");
		this.close.setIcon(new ImageIcon(path));
    	this.close.setToolTipText("關閉視窗");
    	this.close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	setVisible(false);
				saveList();
            }
        });
    	this.customTitleBar.add(this.close);
    	this.add(this.customTitleBar, BorderLayout.NORTH);
	}// end method initCustomTitleBar
	
	// 初始設定主要內容框架
	public void initListPanel() {
		this.listPanel = new JPanel();
		this.initAllItem();
		this.ScrollPane = new JScrollPane();
    	this.ScrollPane.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
    	this.ScrollPane.setAutoscrolls(true);
    	this.ScrollPane.setViewportView(this.listPanel);
    	this.add(this.ScrollPane);
	}// end method initListPanel
	
	// 初始內容物件
	public void initAllItem() {
    	//init listPanel
		if(this.listList.size() < 9)
			this.listPanel.setLayout(new GridLayout(9, 1, 3, 3));
    	else 
    		this.listPanel.setLayout(new GridLayout(this.listList.size(), 1, 3, 3));
		for(int i=0; i<this.listList.size(); i++) {
    		this.item.add(new JLabel(this.listList.get(i).getListTitle()));
    		this.item.get(i).setPreferredSize(new Dimension(250, 40));
    		this.item.get(i).setFont(new Font("微軟正黑體", Font.PLAIN, 20));
    		this.item.get(i).setOpaque(true);
    		this.setItemColor(this.listList.get(i).getColor(), i);
    		final singleList temp = this.listList.get(i);
    		this.item.get(i).addMouseListener(new MouseAdapter() {
    			public void mouseClicked(MouseEvent event) {
    				openList(listList.indexOf(temp));
    		    }
    			public void mouseEntered(MouseEvent event) {
    				setItemColorBrighter(listList.get(listList.indexOf(temp)).getColor(), listList.indexOf(temp));
    		    }
    			public void mouseExited(MouseEvent event) {
    				setItemColor(listList.get(listList.indexOf(temp)).getColor(), listList.indexOf(temp));
    		    }
    		});
    		this.listPanel.add(this.item.get(i), 0);
		}
	}// end method initAllItem
	
	// 載入所有檔案清單
	public void loadList() {
		File folder = new File(System.getProperty("user.home") + loadDirectory );
		folder.mkdirs();
		File [] file = folder.listFiles();
		if(loadDirectory == "/Desktop/PasteIt/TrashCan/TrashList"){
			checkAfterSevenDayFile(folder, file);
		}
		for(File list : file) {
			if(list.isFile()) {
				try {
					createList(readFile(list.getPath(), Charset.forName("MS950")));
					//createList(readFile(list.getPath(), Charset.forName("UTF-8")));
				}
				catch(IOException e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "Load Note Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}// end method loadList
	
	static private String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return encoding.decode(ByteBuffer.wrap(encoded)).toString();
	}
	
	public void createList(String read) {
		if(read == "") {
			this.listList.add(new singleList(this.desktopPane, this));
		}
		else {
			String[] temp = read.split("\n", 1);
			if(editList == false){
				singleList addList = new TrashList(this.desktopPane, this, temp[0]);
				this.listList.add(addList);
			}else{
				singleList addList = new singleList(this.desktopPane, this, temp[0]);
				this.listList.add(addList);
			}
			
		}
		this.desktopPane.add(this.listList.get(this.listList.size() - 1));
	}
	
	public void addNewList() {
		//建立清單
		this.createList("");
		this.listList.get(this.listList.size() - 1).setVisible(true);
		//更新列表
		this.listPanel.removeAll();
		this.item.clear();
		this.initAllItem();
		this.listPanel.revalidate();
		this.listPanel.repaint();
	}
	
	public void openList(int index) {
		if(index != this.oldIndex){
			this.listList.get(index).showScrollPane(this.listList.get(index).getCountSize());
		}
		this.listList.get(index).setVisible(true);
	}
	
	public void changeColors(singleList list, int colorRBG) {
		for(int i=0; i<this.listList.size(); i++) {
			if(this.listList.get(i) == list) {
				this.listList.get(i).selectColor(colorRBG);
				this.setItemColor(this.listList.get(i).getColor(), i);
				return;
			}
		}
	}
	
	public void checkAfterSevenDayFile(File folder, File [] file){
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String nowTime = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
		String[] fileList = folder.list();
		int index = 0;
		for(File f : file){
			if(f.isFile()) 
				index++;
		}
		for(int i = 0; i < index; i++){
			String fileTime = "";
			String[] fileData = fileList[i].split("_");
			fileTime += fileData[0];
			fileTime += "_";
			fileTime += fileData[1];
			try {
				Date now = df.parse(nowTime);
				Date date = df.parse(fileTime);
				long l = now.getTime() - date.getTime();
				long day = l / (24 * 60 * 60 * 1000);
				if(day >= 7){
					File deleteFile = new File(System.getProperty("user.home") + "/Desktop/PasteIt/TrashCan/TrashList/" + fileList[i]);
					try {
						if (deleteFile.delete()) {
							System.out.println(deleteFile.getName() + " is deleted!");
						} else {
							System.out.println("Delete operation is failed.");
						}
					} catch (Exception e) {

						e.printStackTrace();
					}
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	public void deleteList(singleList list) {
		//把檔案移到垃圾桶檔案夾
		File folder = new File(System.getProperty("user.home") + "/Desktop/PasteIt/TrashCan/TrashList");
		folder.mkdirs();
		File [] file = folder.listFiles();
		int index = 0;
		for(File f : file) {
			if(f.isFile()) index++;
		}
		try {
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			PrintWriter printWriter = new PrintWriter(folder.getPath() + '/' + timeStamp + "_list_trash_" + index + ".txt");
			printWriter.write(list.save() + "\n");
			printWriter.close();
		}
		catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Save Note Error", JOptionPane.ERROR_MESSAGE);
		}
		//更新列表
		for(int i=0; i<this.listList.size(); i++) {
			if(this.listList.get(i) == list) {
				this.listList.remove(i);
				this.listPanel.remove(this.item.get(i));
				this.item.remove(i);
			}
		}
		this.listPanel.revalidate();
		this.listPanel.repaint();
		list.dispose();
		this.saveList();
	}
	
	//手動刪除垃圾桶中檔案
	public void RealyDeleteList(singleList list) {
		int index = 0;
		for(int i=0; i<this.listList.size(); i++) {
			if(this.listList.get(i) == list) {
				this.listList.remove(i);
				this.listPanel.remove(this.item.get(i));
				this.item.remove(i);
				index = i;
			}
		}
		File folder = new File(System.getProperty("user.home") + "/Desktop/PasteIt/TrashCan/TrashList");
		String[] fileList = folder.list();
		File deleteFile = new File(System.getProperty("user.home") + "/Desktop/PasteIt/TrashCan/TrashList/" + fileList[index]);
		try {
			if (deleteFile.delete()) {
				//System.out.println(deleteFile.getName() + " is deleted!");
			} else {
				//System.out.println("Delete operation is failed.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.listPanel.revalidate();
		this.listPanel.repaint();
		list.dispose();
	}
	
	public void recoverList(singleList list){
		// 把檔案移回存清單的檔案夾
		File folder = new File(System.getProperty("user.home") + "/Desktop/PasteIt/ListManger");
		folder.mkdirs();
		File[] file = folder.listFiles();
		int index = 0;
		for (File f : file) {
			if (f.isFile())
				index++;
		}
		try {
			String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
			PrintWriter printWriter = new PrintWriter(folder.getPath() + '/' + timeStamp + "_" + index + ".txt");
			printWriter.write(list.save() + "\n");
			printWriter.close();
		} catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Save Note Error", JOptionPane.ERROR_MESSAGE);
		}
		RealyDeleteList(list);
	}
	
	public void closeList(singleList list) {
		for(int i=0; i<this.listList.size(); i++) {
			if(this.listList.get(i) == list) {
				this.item.get(i).setText(list.getListTitle());
			}
		}
		this.saveList();
		list.setVisible(false);
    }
	
	public void saveList() {
		File folder = new File(System.getProperty("user.home") + loadDirectory);
		folder.mkdirs();
		File [] file = folder.listFiles();
		//刪除原本所有的note
		for(File list : file) {
			if(list.isFile()) list.delete();
		}
		//儲存所有的note
		int i = 0;
		for(singleList list : this.listList) {
			try {
				String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
				PrintWriter printWriter = new PrintWriter(folder.getPath() + '/' + timeStamp + "_" + i++ + ".txt");
				printWriter.write(list.save() + "\n");
				printWriter.close();
			}
			catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Save Note Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	
	public void setItemColor(String colorRGB, int i) {
		switch(colorRGB){
	    	case "-1973791" :
	    		this.item.get(i).setBackground(new Color(225, 225, 225));
	    	break;
	    	
	    	case "-26215" :
	    		this.item.get(i).setBackground(new Color(255, 153, 153));
	        break;
	        	
	    	case "-108" :
	    		this.item.get(i).setBackground(new Color(255, 255, 148));
	        break;
	        	
	    	case "-3342464" :
	    		this.item.get(i).setBackground(new Color(204, 255, 128));
	        break;
	        	
	    	case "-6697729" :
	    		this.item.get(i).setBackground(new Color(153, 204, 255));
	        break;
	        	
	    	case "-3436545" :
	    		this.item.get(i).setBackground(new Color(203, 143, 255));
	        break;
	        
	    	default:
	        	this.item.get(i).setBackground(new Color(225, 225, 225).brighter());
	    	break;	
    	}
	}
	
	public void setItemColorBrighter(String colorRGB, int i) {
		switch(colorRGB) {
    	case "-1973791" :
        	this.item.get(i).setBackground(new Color(225, 225, 225).brighter());
    		break;
    	case "-26215" :
        	this.item.get(i).setBackground(new Color(255, 153, 153).brighter());
    		break;
    	case "-108":
        	this.item.get(i).setBackground(new Color(255, 255, 148).brighter());
    		break;
    	case "-3342464":
        	this.item.get(i).setBackground(new Color(204, 255, 128).brighter());
    		break;
    	case "-6697729":
        	this.item.get(i).setBackground(new Color(153, 204, 255).brighter());
    		break;
    	case "-3436545":
        	this.item.get(i).setBackground(new Color(203, 143, 255).brighter());
    		break;
    	default:
        	this.item.get(i).setBackground(new Color(225, 225, 225).brighter());
    		break;
    	}
	}
	
	public void setLoadDirectory(String newDirectory){
		loadDirectory = newDirectory;
	}
	
	public void setEditList(boolean b){
		editList = b;
	}
	
	public void setOldIndex(int index){
		this.oldIndex = index;
	}
	
	public JPanel getListPanel(){
		return this.listPanel;
	}
	
	public int getListSize(){
		return this.listList.size();
	}
	
	public void run() {
		java.awt.Dimension scr_size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		this.loadList();
		this.oldIndex = listList.size();
		this.initCustomTitleBar();
		this.initListPanel();
		this.initFrame();
		setLocation((scr_size.width - this.getWidth()) / 2,(scr_size.height - this.getHeight()) / 2);
		@SuppressWarnings("unused")
		ComponentMover componentMover1 = new ComponentMover(JInternalFrame.class, this.customTitleBar);
		@SuppressWarnings("unused")
		ComponentMover componentMover2 = new ComponentMover(JInternalFrame.class, this.listPanel);

	}
}

