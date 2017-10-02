package trashCan;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import projectList.ListManager;
import stickyNote.NoteManager;
import utility.ComponentMover;



public class TrashManager extends JInternalFrame {
	private static final long serialVersionUID = 1L;
	private JLabel title;
	private JPanel cardNote = new JPanel();
	private JPanel cardList = new JPanel();
	private JPanel customTitleBar;
	private JLabel sleep;
    private JMenuItem close;
    private JMenuItem reload;
    private ImageIcon noteIcon;
    private ImageIcon listIcon;
    private ImageIcon guardian;
	private ListManager listManager;
	private NoteManager noteManager;
	private JTabbedPane tabbedPane;
	private JDesktopPane desktopPane;
	private Random random = new Random();
	private int randomNumber;
	final static String TEXTNOTE = "便利貼";
	final static String TEXTLIST = "清單";
	
	public TrashManager(JDesktopPane desktopPane) {
			this.desktopPane = desktopPane;
			this.randomNumber = random.nextInt(2);
	}
		
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
    }
	
	public void initCustomTitleBar() {
		this.customTitleBar = new JPanel();
		this.title = new JLabel("垃圾桶");
		this.title.setFont(new Font("微軟正黑體", Font.PLAIN, 20));
		this.title.setPreferredSize(new Dimension(193, 20));
		this.customTitleBar.add(this.title);		
		this.close = new JMenuItem();
		this.reload = new JMenuItem();
		
		URL path = TrashManager.class.getResource("/images/reload.png");
		this.reload.setIcon(new ImageIcon(path));
    	this.reload.setToolTipText("重新整理");
    	this.reload.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	reloading();
            }
        });
    	this.customTitleBar.add(this.reload);
    	
    	path = TrashManager.class.getResource("/images/close.png");
		this.close.setIcon(new ImageIcon(path));
    	this.close.setToolTipText("關閉視窗");
    	this.close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	reloading();
            	setVisible(false);
            }
        });
    	this.customTitleBar.add(this.close);
    	this.add(this.customTitleBar, BorderLayout.NORTH);
	}// end method initCustomTitleBar
	
	public void reloading(){
		getContentPane().remove(tabbedPane);
    	if(randomNumber == 0){
    		remove(sleep);
    	}
		revalidate();
		repaint();
		randomNumber = random.nextInt(2);
    	run();
	}
	
	public void loadListList(){
		listManager = new ListManager(desktopPane);
		listManager.setLoadDirectory("/Desktop/PasteIt/TrashCan/TrashList");
		listManager.setEditList(false);
		listManager.loadList();
		listManager.setOldIndex(listManager.getListSize());
		listManager.initListPanel();
		cardList = listManager.getListPanel();	
	}
	
	public void loadNoteList(){
		noteManager = new NoteManager(desktopPane);
		noteManager.setLoadDirectory("/Desktop/PasteIt/TrashCan/TrashNote");
		noteManager.setEditList(false);
		noteManager.loadNote();
		noteManager.initListPanel();
		cardNote = noteManager.getListPanel();		
	}
	
	public void addComponentToPane(Container pane) {
		tabbedPane = new JTabbedPane(JTabbedPane.TOP, JTabbedPane.SCROLL_TAB_LAYOUT);
    	URL path = TrashManager.class.getResource("/images/notes.png");
    	this.noteIcon = new ImageIcon(path);
    	path = TrashManager.class.getResource("/images/list.png");
    	this.listIcon = new ImageIcon(path);
		
		// Create the "cards".
    	if (randomNumber == 0){
    		if (noteManager.getNoteListSize() >= 5) {
				tabbedPane.addTab(TEXTNOTE, this.noteIcon, new JScrollPane(cardNote));
			}else{
				tabbedPane.addTab(TEXTNOTE, this.noteIcon, cardNote);
			}
			if (listManager.getListSize() >= 5) {
				tabbedPane.addTab(TEXTLIST, this.listIcon, new JScrollPane(cardList));
			}else{
				tabbedPane.addTab(TEXTLIST, this.listIcon, cardList);
			}
    	}else{
			if (noteManager.getNoteListSize() >= 12) {
				tabbedPane.addTab(TEXTNOTE, this.noteIcon, new JScrollPane(cardNote));
			}else{
				tabbedPane.addTab(TEXTNOTE, this.noteIcon, cardNote);
			}
			if (listManager.getListSize() >= 12) {
				tabbedPane.addTab(TEXTLIST, this.listIcon, new JScrollPane(cardList));
			}else{
				tabbedPane.addTab(TEXTLIST, this.listIcon, cardList);
			}
    	}
		
		pane.add(tabbedPane, BorderLayout.CENTER);
	}
	
	public void addGuardian(){
 		URL path = TrashManager.class.getResource("/images/sleepZZ.png");
		this.guardian = new ImageIcon(path);
		this.guardian.setImage(guardian.getImage().getScaledInstance( 230, 180, Image.SCALE_DEFAULT));
		this.sleep = new JLabel(this.guardian);
		this.sleep.setLayout(null);
		this.sleep.setBounds( 130, 130, 100, 130);
		this.sleep.setVisible(true);
		this.sleep.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				addGuardianTalk();
			}
	    });
		//介面加入frame
		this.add(this.sleep, BorderLayout.SOUTH);
	}
	
	public void addGuardianTalk(){
		this.remove(this.sleep);
		this.revalidate();
		this.repaint();
 		URL path = TrashManager.class.getResource("/images/sleepZZtalk.png");
		this.guardian = new ImageIcon(path);
		this.guardian.setImage(guardian.getImage().getScaledInstance( 230, 180, Image.SCALE_DEFAULT));
		this.sleep = new JLabel(this.guardian);
		this.sleep.setLayout(null);
		this.sleep.setBounds( 130, 130, 100, 130);
		this.sleep.setVisible(true);
		this.sleep.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				addGuardianFace();
			}
	    });
		//介面加入frame
		this.add(this.sleep, BorderLayout.SOUTH);
	}
	
	public void addGuardianFace(){
		this.remove(this.sleep);
		this.revalidate();
		this.repaint();
 		URL path;
 		if(random.nextInt(2) == 0){
 			path = TrashManager.class.getResource("/images/waZZ.png");
 		}else{
 			path = TrashManager.class.getResource("/images/ZZ.png");
 		}
		this.guardian = new ImageIcon(path);
		this.guardian.setImage(guardian.getImage().getScaledInstance( 230, 180, Image.SCALE_DEFAULT));
		this.sleep = new JLabel(this.guardian);
		this.sleep.setLayout(null);
		this.sleep.setBounds( 130, 130, 100, 130);
		this.sleep.setVisible(true);
		//介面加入frame
		this.add(this.sleep, BorderLayout.SOUTH);
	}
	
	public void run(){
		java.awt.Dimension scr_size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		this.initFrame();
		this.initCustomTitleBar();
		this.loadListList();
		this.loadNoteList();
		this.addComponentToPane(this.getContentPane());
		if(randomNumber == 0)
			this.addGuardian();
		this.setSize(300, 500);
		setLocation((scr_size.width - this.getWidth()) / 2,(scr_size.height - this.getHeight()) / 2);
		@SuppressWarnings("unused")
		ComponentMover componentMover0 = new ComponentMover(JInternalFrame.class, this.customTitleBar);
		@SuppressWarnings("unused")
		ComponentMover componentMover1 = new ComponentMover(JInternalFrame.class, this.cardList);
		@SuppressWarnings("unused")
		ComponentMover componentMover2 = new ComponentMover(JInternalFrame.class, this.cardNote);
	}
}
	