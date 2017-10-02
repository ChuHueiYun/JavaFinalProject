package index;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import calendar.Calendar;
import stickyNote.NoteManager;
import utility.ComponentMover;
import weatherForecast.WeatherView;
import projectList.ListManager;
import trashCan.TrashManager;
import digitalClock.DigitalClockFrame;


public class Index extends JInternalFrame{
	private static final long serialVersionUID = 1L;
	private JPanel upBar;
	private JPanel upBar1;
	private JPanel upBar2;//便條
	private JPanel upBar3;//清單
	private JPanel upBar4;//行事曆
	private JPanel upBar5;//時間
	private JPanel upBar6;//天氣
	private JPanel upBar7;//標籤
	private JPanel twoBar;
	private ImageIcon image;//背景圖
	private ImageIcon moving;//ㄗㄗ冒出來
	private JLabel jLab1;
	private JLabel jLab2;
	private JLabel labelNote;
	private JLabel labelList;
	private JLabel labelCal;
	private JLabel labelTime;
	private JLabel labelWea;
	private JLabel labelTag;
	private JDesktopPane desktopPane;
	private NoteManager noteManager;
	private ListManager listManager;
	private TrashManager trashManager;
	private WeatherView weatherView;
	private Calendar calendar;
	private DigitalClockFrame digitalClockFrame;
	
	
	public Index(JDesktopPane desktopPane){
		this.desktopPane = desktopPane;
		
	}
	//外框架
	public void initFrame(boolean temp) {
		this.setBorder(null);
		BasicInternalFrameUI basicInternalFrameUI = (BasicInternalFrameUI)this.getUI();
		basicInternalFrameUI.setNorthPane(null);
    	this.setResizable(false);
    	this.setClosable(false);
    	this.setMaximizable(false);
    	this.setIconifiable(false);
    	this.setSize(400,580);
		this.setVisible(temp);
    }
	//底圖
	public void initPic() {
		//整個框架
		this.upBar = new JPanel();//最上面那一條 包含標題以及圖片
		this.upBar.setLayout(null);
		this.upBar.setSize(new Dimension(400,700));
		this.upBar.setBackground(Color.LIGHT_GRAY);
		this.upBar.setVisible(true);
		@SuppressWarnings("unused")
		ComponentMover componentMover1 = new ComponentMover(JInternalFrame.class, upBar);
		//背景圖1
 		URL path = Index.class.getResource("/images/intex2.png");
		this.image=new ImageIcon(path);
        this.image.setImage(image.getImage().getScaledInstance(390, 530,Image.SCALE_DEFAULT));
        this.jLab1=new JLabel(image);
        this.jLab1.setLayout(null);
        this.jLab1.setBounds(0, 0, 390, 530); 
		this.upBar.add(this.jLab1);
		//介面加入frame
		this.add(this.upBar);
	}
	//便利貼
	public void initLabel() {
		//整個框架
		this.upBar2 = new JPanel();//最上面那一條 包含標題以及圖片
		this.upBar2.setLayout(null);
		this.upBar2.setBounds( 127,332, 30,25);
		this.upBar2.setOpaque(false);
		this.upBar2.setVisible(true);
		//便利貼
		this.labelNote = new JLabel();//最上面那一條 包含標題以及圖片
		this.labelNote.setLayout(null);
		this.labelNote.setBounds(0,0,30,25);
		this.labelNote.setOpaque(false);//透明化
		this.labelNote.setVisible(true);
		this.labelNote.setToolTipText("便利貼");
        noteManager = new NoteManager(desktopPane);
    	noteManager.run();
    	desktopPane.add(noteManager);
    	noteManager.setVisible(false);
		this.labelNote.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				noteManager.refresh();
				//產生便利貼視窗
				noteManager.setVisible(true);
            }
        });
		this.upBar2.add(this.labelNote);
		//介面加入frame
		this.add(this.upBar2);
	}
	//清單
	public void initList() {
		//整個框架
		this.upBar3 = new JPanel();//最上面那一條 包含標題以及圖片
		this.upBar3.setLayout(null);
		this.upBar3.setBounds( 47,320, 57,65);
		this.upBar3.setOpaque(false);
		this.upBar3.setVisible(true);
		//清單
		this.labelList = new JLabel();//最上面那一條 包含標題以及圖片
		this.labelList.setLayout(null);
		this.labelList.setBounds(0,0,57,65);
		this.labelList.setOpaque(false);//透明化
		this.labelList.setVisible(true);
		this.labelList.setToolTipText("清單");
		listManager = new ListManager(desktopPane);
		listManager.run();
		desktopPane.add(listManager);
		listManager.setVisible(false);
		this.labelList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				//跳到清單視窗
				listManager.setVisible(true);
            }
        });
		this.upBar3.add(this.labelList);
		//介面加入frame
		this.add(this.upBar3);
	}
	//行事曆
	public void initCal() {
		//整個框架
		this.upBar4 = new JPanel();//最上面那一條 包含標題以及圖片
		this.upBar4.setLayout(null);
		this.upBar4.setBounds( 27,248, 54,58);
		this.upBar4.setOpaque(false);
		this.upBar4.setVisible(true);
		//行事曆
		this.labelCal = new JLabel();//最上面那一條 包含標題以及圖片
		this.labelCal.setLayout(null);
		this.labelCal.setBounds(0,0,57,59);
		this.labelCal.setOpaque(false);//透明化
		this.labelCal.setVisible(true);
		this.labelCal.setToolTipText("行事曆");
    	calendar = new Calendar(desktopPane);
    	calendar.run();
    	desktopPane.add(calendar);
    	calendar.setVisible(false);
		this.labelCal.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				//跳到行事曆視窗
		    	calendar.setVisible(true);
		    	
            }
        });
		this.upBar4.add(this.labelCal);
		//介面加入frame
		this.add(this.upBar4);
	}
	//天氣
	public void initWea() {
		//整個框架
		this.upBar6 = new JPanel();//最上面那一條 包含標題以及圖片
		this.upBar6.setLayout(null);
		this.upBar6.setBounds( 150, 100, 170, 150);
		this.upBar6.setOpaque(false);
		this.upBar6.setVisible(true);
		//天氣
		this.labelWea = new JLabel();//最上面那一條 包含標題以及圖片
		this.labelWea.setLayout(null);
		this.labelWea.setBounds( 0, 0, 170, 150);
		this.labelWea.setOpaque(false);//透明化
		this.labelWea.setVisible(true);
		this.labelWea.setToolTipText("天氣");
        weatherView = new WeatherView();
    	weatherView.run();
    	desktopPane.add(weatherView);
    	weatherView.setVisible(false);
		this.labelWea.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				//跳到天氣視窗
		    	weatherView.setVisible(true);
            }
        });
		this.upBar6.add(this.labelWea);
		//介面加入frame
		this.add(this.upBar6);
	}
	//時間
	public void initTime() {
		//整個框架
		this.upBar5 = new JPanel();//最上面那一條 包含標題以及圖片
		this.upBar5.setLayout(null);
		this.upBar5.setBounds( 43,73, 80,80);
		this.upBar5.setOpaque(false);
		this.upBar5.setVisible(true);
		//時間
		this.labelTime = new JLabel();//最上面那一條 包含標題以及圖片
		this.labelTime.setLayout(null);
		this.labelTime.setBounds(0,0,80,80);
		this.labelTime.setOpaque(false);//透明化
		this.labelTime.setVisible(true);
		this.labelTime.setToolTipText("時間");
		digitalClockFrame = new DigitalClockFrame(desktopPane);
		this.labelTime.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				//跳到時間視窗
		        digitalClockFrame.getInternalClockFrame().setVisible(true);
            }
        });
		this.upBar5.add(this.labelTime);
		//介面加入frame
		this.add(this.upBar5);
	}
	//標籤
	public void initTag() {
		//整個框架
		this.upBar7 = new JPanel();//最上面那一條 包含標題以及圖片
		this.upBar7.setLayout(null);
		this.upBar7.setBounds( 310,424,13,30);
		this.upBar7.setOpaque(false);
		this.upBar7.setVisible(true);
		//標籤
		this.labelTag = new JLabel();//最上面那一條 包含標題以及圖片
		this.labelTag.setLayout(null);
		this.labelTag.setBounds( 0, 0, 13,30);
		this.labelTag.setOpaque(false);//透明化
		this.labelTag.setVisible(true);
		this.labelTag.setToolTipText("標籤");
		this.labelTag.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				//跳到標籤視窗
            }
        });
		this.upBar7.add(this.labelTag);
		//介面加入frame
		this.add(this.upBar7);
	}
	//垃圾桶
	public void initChiayu(boolean a){
		if(a==false){
			this.upBar1.removeAll();
			this.upBar1.repaint();
			this.upBar1.setOpaque(false);
			}
		else{
			//整個框架
			this.upBar1 = new JPanel();//最上面那一條 包含標題以及圖片
			this.upBar1.setLayout(null);
			this.upBar1.setSize(new Dimension( 60, 130));
			this.upBar1.setBounds( 113,400, 60, 130);
			this.upBar1.setVisible(true);
			//女神圖
	 		URL path = Index.class.getResource("/images/123.png");
			this.moving=new ImageIcon(path);
			this.moving.setImage(moving.getImage().getScaledInstance( 60, 130,Image.SCALE_DEFAULT));
			this.jLab2=new JLabel(moving);
			this.jLab2.setLayout(null);
			this.jLab2.setBounds( 0,0, 60, 130);
			this.jLab2.setVisible(true);
			this.upBar1.add(this.jLab2);
			//介面加入frame
			this.add(this.upBar1);
			this.upBar1.repaint();
		}
	}
	//垃圾桶位置
	public void initLachi(){
		//整個框架
		this.twoBar = new JPanel();//最上面那一條 包含標題以及圖片
		this.twoBar.setOpaque(false);
		this.twoBar.setLayout(null);
		this.twoBar.setBounds(115,445,55,85);
		this.twoBar.setVisible(true);
		trashManager = new TrashManager(desktopPane);
		trashManager.run();
		desktopPane.add(trashManager);
		trashManager.setVisible(false);
		this.twoBar.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
		    		//產生垃圾桶
				trashManager.reloading();
				trashManager.setVisible(true);
		    }
			public void mouseEntered(MouseEvent event) {
				initChiayu(true);
			}
			public void mouseExited(MouseEvent event) {
				initChiayu(false);
		    }
		});
		this.add(this.twoBar);
	}
	public void run() {
		java.awt.Dimension scr_size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		this.initTag();
		this.initWea();
		this.initTime();
		this.initCal();
		this.initList();
		this.initLabel();
		this.initLachi();
		this.initPic();
		this.initFrame(true);
		setLocation((scr_size.width - this.getWidth()),(scr_size.height - this.getHeight()-160));
	}

}
