package stickyNote;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import reminder.Reminder;
import utility.ComponentMover;

public class Note extends JInternalFrame {
	private static final long serialVersionUID = 1L;
	private JDesktopPane desktopPane;
	private JTextField title;
	private JMenuBar menuBar;
    private JMenu setting;
    private JMenu color;
    private JMenuItem white;
    private JMenuItem pink;
    private JMenuItem yellow;
    private JMenuItem green;
    private JMenuItem blue;
    private JMenuItem purple;
    private JMenu shape;
    private JMenuItem large;
    private JMenuItem medium;
    private JMenuItem alarm;
    private JMenuItem tag;
    @SuppressWarnings("unused")
	private JMenuItem displayOnDesktop;
    private JMenuItem delete;
    private JMenuItem close;
    private JPanel customTitleBar;
	private JTextArea content;
    private JScrollPane ScrollPane;
    private JMenuItem currentColor;
    private JMenuItem currentSize;
    private NoteManager noteManager;
    private String reminderTime;
    private String[] tags;
    private Reminder reminder;
    
    public Note(JDesktopPane desktopPane, NoteManager noteManager, String[] tags, String reminderTime, String color, String size, String title, String content){
    	super("", false, false, false, false);
        		//resizable, closable, maximizable, iconifiable
        this.setBorder(null);
        BasicInternalFrameUI basicInternalFrameUI = (BasicInternalFrameUI)this.getUI();
        basicInternalFrameUI.setNorthPane(null);
        this.desktopPane = desktopPane;
    	this.noteManager = noteManager;
    	this.initFrame();
    	this.initcustomTitleBar();
    	this.initContent();
    	this.tags = tags;
    	this.reminderTime = reminderTime;
    	this.initReminder();
    	this.setNoteTitle(title);
    	this.setContent(content);
    	this.setColor(color);
    	this.setSize(size);
		java.awt.Dimension scr_size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((scr_size.width - this.getWidth()) / 2-200,(scr_size.height - this.getHeight()) / 2+200);
		@SuppressWarnings("unused")
		ComponentMover componentMover = new ComponentMover(JInternalFrame.class, customTitleBar);
    }
    private void initReminder() {
    	this.reminder = new Reminder(this.desktopPane, this, this.reminderTime);
    	
    }
    private void initFrame() {
    	this.setBounds(new Rectangle(0, 0, 0, 0));
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }
    private void initcustomTitleBar() {
    	//title
    	this.title = new JTextField(22);
    	this.title.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
    	//setting
    	this.menuBar = new JMenuBar();

    	this.setting = new JMenu("");

		URL path = Note.class.getResource("/images/settings.png");
		this.setting.setIcon(new ImageIcon(path));
    	
		this.color = new JMenu("顏色");
    	this.color.setFont(new Font("微軟正黑體", Font.BOLD, 14));
    	this.currentColor = this.white;
    	this.white = new JMenuItem("白色");
    	this.white.setFont(new Font("微軟正黑體", Font.BOLD, 14));
    	this.white.setEnabled(false);
    	this.white.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	setColor(getColor(white));
            	colorChange("白色");
            }
        });
    	this.pink = new JMenuItem("粉紅色");
    	this.pink.setFont(new Font("微軟正黑體", Font.BOLD, 14));
    	this.pink.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	setColor(getColor(pink));
            	colorChange("粉紅色");
            }
        });
    	this.yellow = new JMenuItem("黃色");
    	this.yellow.setFont(new Font("微軟正黑體", Font.BOLD, 14));
    	this.yellow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	setColor(getColor(yellow));
            	colorChange("黃色");
            }
        });
    	this.green = new JMenuItem("綠色");
    	this.green.setFont(new Font("微軟正黑體", Font.BOLD, 14));
    	this.green.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	setColor(getColor(green));
            	colorChange("綠色");
            }
        });
    	this.blue = new JMenuItem("藍色");
    	this.blue.setFont(new Font("微軟正黑體", Font.BOLD, 14));
    	this.blue.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	setColor(getColor(blue));
            	colorChange("藍色");
            }
        });
    	this.purple = new JMenuItem("紫色");
    	this.purple.setFont(new Font("微軟正黑體", Font.BOLD, 14));
    	this.purple.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	setColor(getColor(purple));
            	colorChange("紫色");
            }
        });
    	this.color.add(this.white);
    	this.color.add(this.pink);
    	this.color.add(this.yellow);
    	this.color.add(this.green);
    	this.color.add(this.blue);
    	this.color.add(this.purple);
    	
    	this.shape = new JMenu("形狀");
    	this.shape.setFont(new Font("微軟正黑體", Font.BOLD, 14));
    	this.large = new JMenuItem("大");
    	this.large.setFont(new Font("微軟正黑體", Font.BOLD, 14));
    	this.large.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	setSize(getSize(large));
            }
        });
    	this.medium = new JMenuItem("中");
    	this.medium.setFont(new Font("微軟正黑體", Font.BOLD, 14));
    	this.currentSize = this.medium;
    	this.medium.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	setSize(getSize(medium));
            }
        });
    	this.shape.add(this.large);
    	this.shape.add(this.medium);
    	
    	this.alarm = new JMenuItem("提醒");
    	this.alarm.setFont(new Font("微軟正黑體", Font.BOLD, 14));
    	this.alarm.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	reminder.getSetTimeFrame().setVisible(true);
            }
        });
    	this.tag = new JMenuItem("標籤");
    	this.tag.setFont(new Font("微軟正黑體", Font.BOLD, 14));
    	this.tag.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	//未完成
            }
        });
    	this.delete = new JMenuItem("刪除");
    	this.delete.setFont(new Font("微軟正黑體", Font.BOLD, 14));
    	this.delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	delete();
            }
        });
    	this.close = new JMenuItem("關閉");
    	this.close.setFont(new Font("微軟正黑體", Font.BOLD, 14));
    	this.close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	close();
            }
        });

    	this.setting.add(this.color);
    	this.setting.add(this.shape);
    	this.setting.add(this.alarm);
    	this.setting.add(this.tag);
    	this.setting.add(this.delete);
    	this.setting.add(this.close);
    	this.menuBar.add(this.setting);
    	
    	this.customTitleBar = new JPanel();
    	this.customTitleBar.add(this.title);
    	this.customTitleBar.add(this.menuBar);
    	this.add(this.customTitleBar, BorderLayout.NORTH);
    }
    private void initContent() {
    	this.ScrollPane = new JScrollPane();
    	this.ScrollPane.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
    	this.ScrollPane.setAutoscrolls(true);
    	
    	this.content = new JTextArea("");
    	this.content.setFont(new Font("微軟正黑體", Font.PLAIN, 12));
    	this.content.setColumns(20);
    	this.content.setLineWrap(true);
    	this.content.setRows(5);
    	this.content.setBorder(null);
    	this.content.setCursor(new Cursor(Cursor.TEXT_CURSOR));
    	this.content.setDisabledTextColor(new java.awt.Color(0, 0, 0));
    	this.content.setDragEnabled(true);
        
    	this.ScrollPane.setViewportView(this.content);
    	this.add(this.ScrollPane);
    }
    private void setNoteTitle(String title) {
    	this.title.setText(title);
    }
    public String getNoteTitle() {
    	return this.title.getText();
    }
    private void setContent(String content) {
    	this.content.setText(content);
    }
    private String getContent() {
    	return this.content.getText();
    }
    public void setColor(String color) {
    	switch(color) {
    	case "白色":
    		this.currentColor = this.white;
    		this.setColorEnable();
        	this.white.setEnabled(false);
        	this.customTitleBar.setBackground(new Color(225, 225, 225));
        	this.content.setBackground(new Color(248, 248, 248));
    		break;
    	case "粉紅色":
    		this.currentColor = this.pink;
    		this.setColorEnable();
        	this.pink.setEnabled(false);
        	this.customTitleBar.setBackground(new Color(255, 153, 153));
        	this.content.setBackground(new Color(255, 235, 235));
    		break;
    	case "黃色":
    		this.currentColor = this.yellow;
    		this.setColorEnable();
        	this.yellow.setEnabled(false);
        	this.customTitleBar.setBackground(new Color(255, 255, 148));
        	this.content.setBackground(new Color(255, 255, 224));
    		break;
    	case "綠色":
    		this.currentColor = this.green;
    		this.setColorEnable();
        	this.green.setEnabled(false);
        	this.customTitleBar.setBackground(new Color(204, 255, 128));
        	this.content.setBackground(new Color(239, 255, 214));
    		break;
    	case "藍色":
    		this.currentColor = this.blue;
    		this.setColorEnable();
        	this.blue.setEnabled(false);
        	this.customTitleBar.setBackground(new Color(153, 204, 255));
        	this.content.setBackground(new Color(235, 244, 255));
    		break;
    	case "紫色":
    		this.currentColor = this.purple;
    		this.setColorEnable();
        	this.purple.setEnabled(false);
        	this.customTitleBar.setBackground(new Color(203, 143, 255));
        	this.content.setBackground(new Color(235, 235, 255));
    		break;
    	default:
    		this.currentColor = this.white;
    		this.setColorEnable();
        	this.white.setEnabled(false);
        	this.customTitleBar.setBackground(new Color(225, 225, 225));
        	this.content.setBackground(new Color(248, 248, 248));
    		break;
    	}
    }
    public String getColor() {
    	return getColor(currentColor);
    }
    private String getColor(JMenuItem menuItem) {
    	return menuItem.getText();
    }
    private void setSize(String size) {
    	switch(size) {
    	case "大":
    		this.currentSize = large;
        	this.setSizeEnable();
        	this.large.setEnabled(false);
        	this.setSize(300, 500);
    		break;
    	case "中":
    		this.currentSize = medium;
    		this.setSizeEnable();
    		this.medium.setEnabled(false);
    		this.setSize(300, 300);
    		break;
    	default:
    		this.currentSize = medium;
    		this.setSizeEnable();
    		this.medium.setEnabled(false);
    		this.setSize(300, 300);
    		break;
    	}
    }
    private String getSize(JMenuItem menuItem) {
    	return menuItem.getText();
    }
    /*private void setTags(String[] tags) {
    	this.tags = new String[tags.length];
    	this.tags = tags;
    }*/
    public String[] getTags() {
    	return this.tags;
    }
    public String getReminder() {
    	this.reminderTime = reminder.getTimeString();
    	return this.reminderTime;
    }
    public JPanel getCustomTitleBar(){
    	return this.customTitleBar;
    }
    public JTextField getTitleComponent() {
    	return this.title;
    }
    public JMenuBar getAMenuBar(){
    	return this.menuBar;
    }
    public JTextArea getContentComponent() {
    	return this.content;
    }
    private void setColorEnable() {
    	this.white.setEnabled(true);
    	this.pink.setEnabled(true);
    	this.yellow.setEnabled(true);
    	this.green.setEnabled(true);
    	this.blue.setEnabled(true);
    	this.purple.setEnabled(true);
    }
    private void setSizeEnable() {
    	this.large.setEnabled(true);
    	this.medium.setEnabled(true);
    }
    public String save() {
    	this.reminderTime = reminder.getTimeString();
    	String temp = "";
    	if(this.tags != null) {
    		temp = "[";
    		for(int i=0; i<this.tags.length; i++) {
    			temp += this.tags[i];
    			if(i != this.tags.length - 1) temp += ", ";
    		}
    		temp += "]";
    	}
    	return temp + "\n" + this.reminderTime + "\n" + this.getColor(this.currentColor) + "\n" + this.getSize(this.currentSize) + "\n" + this.getNoteTitle() + "\n" + this.getContent() + "\n";
    }
    private void delete() {
    	noteManager.deleteNote(this);
    }
    private void close() {
    	noteManager.closeNote(this);
    }
    public void colorChange(String color) {
    	noteManager.changeColor(this, color);
    }
}