package projectList;

import java.awt.GridLayout;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;

import java.net.URL;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.plaf.basic.BasicInternalFrameUI;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon; // loads images
import javax.swing.JCheckBox;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import reminder.Reminder;
import utility.ComponentMover;

public class singleList extends JInternalFrame{
	private static final long serialVersionUID = 1L;
	private JDesktopPane desktopPane;
	private JTextField title;
	private JTextField text[] = new JTextField[100];
	private JTextField deleteText[] = new JTextField[100];
	private JPanel add;
	private JPanel allCheckBoxText;
	private JPanel checkBoxText[] = new JPanel[100];
	private JPanel deleteCheckBoxText[] = new JPanel[100];
	private JCheckBox checkBox[] = new JCheckBox[100];
	private JCheckBox deleteCheckBox[] = new JCheckBox[100];
	private JMenuBar menuBar = new JMenuBar();
	private JMenuItem colorItem[] = new JMenuItem[6];
	private JMenuItem callItem, tagItem, deleteItem, closeItem;
	private JMenu setMenu, colorMenu;
	private JScrollPane jsp;
	private Color titleColor = new Color(225, 225, 225);
	private Color textColor = new Color(248, 248, 248);
	private int countText = 0, countSize = 0, deleteSize = 0, size = 13, checked = 0;
	private int[] allIndex = new int[100];
	private String reminderTime = "";
	private Reminder reminder;	//新增一個Reminder做為data member
	protected ListManager manager;
	
	
	public singleList(JDesktopPane desktopPane, ListManager manager){
		//super("", false, false, false, false);
		this.manager = manager;
		this.desktopPane = desktopPane;
		this.initFrame();
		this.initcustomTitleBar();
		this.initContentArea();
		this.initContent();
		this.initAddbutton();
		java.awt.Dimension scr_size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((scr_size.width - this.getWidth()) / 2-200,(scr_size.height - this.getHeight()) / 2+200);
	}
	
	public singleList(JDesktopPane desktopPane, ListManager manager,String body){
		this.manager = manager;
		this.desktopPane = desktopPane;
		this.initFrame();
		this.initcustomTitleBar();
		this.load(body);
		this.initAddbutton();
		java.awt.Dimension scr_size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		setLocation((scr_size.width - this.getWidth()) / 2-200,(scr_size.height - this.getHeight()) / 2+200);
	}
	
	private void initFrame() {
		this.reminder = new Reminder(this.desktopPane, this, this.reminderTime);
		this.setBorder(null);
		BasicInternalFrameUI basicInternalFrameUI = (BasicInternalFrameUI)this.getUI();
		basicInternalFrameUI.setNorthPane(null);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		this.setBounds(100, 100, 300, 500); // set frame size
	}
	
	private void initcustomTitleBar() {
		title = new JTextField("標題", 18);
		title.setBackground(titleColor);
		title.setFont(new Font("微軟正黑體", Font.PLAIN, 18));
		title.addActionListener(new Handler());
		
		setMenu = new JMenu(""); 
		URL setPicture = singleList.class.getResource("/images/settings.png");
		setMenu.setIcon(new ImageIcon(setPicture));
		setMenu.setToolTipText("設定");
        
        colorMenu = new JMenu("顏色");
        colorMenu.setFont(new Font("微軟正黑體", Font.BOLD, 14));
        colorMenu.setPreferredSize(new Dimension(80, 20));
        colorItem[0] = new JMenuItem("白色(預設)");
        colorItem[0].setFont(new Font("微軟正黑體", Font.BOLD, 14));
        colorItem[0].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	titleColor = new Color(225, 225, 225);
				textColor = new Color(248, 248, 248);
				changeColor();
            }
        });
        colorItem[1] = new JMenuItem("粉紅色");
        colorItem[1].setFont(new Font("微軟正黑體", Font.BOLD, 14));
        colorItem[1].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
				titleColor = new Color(255, 153, 153);
				textColor = new Color(255, 235, 235);
				changeColor();
            }
        });
        colorItem[2] = new JMenuItem("黃色");
        colorItem[2].setFont(new Font("微軟正黑體", Font.BOLD, 14));
        colorItem[2].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
    				titleColor = new Color(255, 255, 148);
    				textColor = new Color(255, 255, 224);
    				changeColor();
            }
        });
        colorItem[3] = new JMenuItem("綠色");
        colorItem[3].setFont(new Font("微軟正黑體", Font.BOLD, 14));
        colorItem[3].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
    				titleColor = new Color(204, 255, 128);
    				textColor = new Color(239, 255, 214);
    				changeColor();
            }
        });
        colorItem[4] = new JMenuItem("藍色");
        colorItem[4].setFont(new Font("微軟正黑體", Font.BOLD, 14));
        colorItem[4].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
    				titleColor = new Color(153, 204, 255);
    				textColor = new Color(235, 244, 255);
    				changeColor();
            }
        });
        colorItem[5] = new JMenuItem("紫色");
        colorItem[5].setFont(new Font("微軟正黑體", Font.BOLD, 14));
        colorItem[5].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
    				titleColor = new Color(203, 143, 255);
    				textColor = new Color(235, 235, 255);
    				changeColor();
            }
        });
        for(int i = 0; i < colorItem.length; i++){
        	colorItem[i].setPreferredSize(new Dimension(80, 20));
        	colorMenu.add(colorItem[i]);
        }
        setMenu.add(colorMenu);
        
        callItem = new JMenuItem("提醒");
        callItem.setFont(new Font("微軟正黑體", Font.BOLD, 14));
        callItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	remind();
            }
        });
        setMenu.add(callItem);
        
        tagItem = new JMenuItem("標籤");
        tagItem.setFont(new Font("微軟正黑體", Font.BOLD, 14));
        tagItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	//標籤
            }
        });
        setMenu.add(tagItem);
        
        deleteItem = new JMenuItem("刪除");
        deleteItem.setFont(new Font("微軟正黑體", Font.BOLD, 14));
        deleteItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	delete();
            }
        });
        setMenu.add(deleteItem);
       
        closeItem = new JMenuItem("關閉");
        closeItem.setFont(new Font("微軟正黑體", Font.BOLD, 14));
        closeItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	close();
            }
        });
        setMenu.add(closeItem);
        
        setMenu.setPopupMenuVisible(true);
        menuBar.add(title);
        menuBar.add(setMenu);
		menuBar.setBackground(titleColor);
		this.setJMenuBar(menuBar);
	}
	
	private void initContentArea(){
		allCheckBoxText = new JPanel();
		allCheckBoxText.setBackground(textColor);
		allCheckBoxText.setLayout(new GridLayout(size, 1));
	}

	private void initContent(){
		checkBoxText[countText] = new JPanel();
		checkBoxText[countText].setBackground(textColor);
		
		checkBox[countText] = new JCheckBox();
		checkBox[countText].setBackground(textColor);
		checkBox[countText].addItemListener(new checkBoxHandler(countSize));
		checkBoxText[countText].add(checkBox[countText]);
		
		text[countText] = new JTextField("事項", 18);
		text[countText].setBackground(textColor);
		text[countText].setFont(new Font("微軟正黑體", Font.PLAIN, 14));
		text[countText].addActionListener(new Handler(countText));
		checkBoxText[countText].add(text[countText]);
		allCheckBoxText.add(checkBoxText[countText]);
	}
	
	
	
	private void initAddbutton(){
		add = new JPanel();
		JLabel addtext1 = new JLabel("+");
		addtext1.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
		JLabel addtext2 = new JLabel("清單項目");
		addtext2.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
		add.add(addtext1);
		add.add(addtext2);
		add.setBackground(textColor);
		add.addMouseListener(new MouseClickHandler());// add handler
		allCheckBoxText.add(add);
		add(allCheckBoxText, BorderLayout.CENTER);
		@SuppressWarnings("unused")
		ComponentMover cm = new ComponentMover(JInternalFrame.class, allCheckBoxText);
	}
	
	private class Handler implements ActionListener{ 
		private int index;
		private String updateText;
		
		public Handler() {
			index = 0;
			updateText = "";
		}
		
		public Handler(int countText) {
			index = countText;
			updateText = "";
		}

		@Override
		public void actionPerformed(ActionEvent event) {
			if(event.getSource() == title){
				updateText = event.getActionCommand();
				title.setText(updateText);
			}else{
				updateText = event.getActionCommand();
				text[index].setText(updateText);
			}
		}
	}
	
	private class checkBoxHandler implements ItemListener{
		private int index;
		
		@SuppressWarnings("unused")
		public checkBoxHandler() {
			index = 0;
		}
		
		public checkBoxHandler(int count) {	
			index = count;
			allIndex[index] = count - deleteSize;
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void itemStateChanged(ItemEvent event) {
			int tempIndex = index;
			index = allIndex[index];
			
			if(checkBox[index].isSelected()){
				allCheckBoxText.removeAll();
				allCheckBoxText.revalidate();
				allCheckBoxText.repaint();
				checkBoxText[index].removeAll();
				checkBoxText[index].revalidate();
				checkBoxText[index].repaint();
				Font font = new Font("微軟正黑體", Font.PLAIN, 14);
				@SuppressWarnings("rawtypes")
				Map attributes = font.getAttributes();
				attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
				Font newFont = new Font(attributes);
				text[index].setFont(newFont);
				text[index].setEditable(false);//設定為不可編輯
				checkBox[index].setEnabled(false);
				checkBoxText[index].add(checkBox[index]);
				checkBoxText[index].add(text[index]);
				
				deleteCheckBoxText[deleteSize] = new JPanel();
				deleteCheckBoxText[deleteSize].setBackground(textColor);
				deleteCheckBox[deleteSize] = checkBox[index];
				deleteCheckBox[deleteSize].setBackground(textColor);
				deleteText[deleteSize] = text[index];
				deleteText[deleteSize].setBackground(textColor);
				deleteCheckBoxText[deleteSize].add(deleteCheckBox[deleteSize]);
				deleteCheckBoxText[deleteSize].add(deleteText[deleteSize]);
				deleteSize++;
				
				for (int i = index; i <= countSize; i++) {
					JCheckBox tempBox = new JCheckBox();
					tempBox = checkBox[i];
					checkBox[i] = checkBox[i + 1];
					checkBox[i + 1] = tempBox;
					
					JTextField tempText = new JTextField();
					tempText = text[i];
					text[i] = text[i + 1];
					text[i + 1] = tempText;
					
					JPanel temp = new JPanel();
					temp = checkBoxText[i];
					checkBoxText[i] = checkBoxText[i + 1];
					checkBoxText[i + 1] = temp;
				}
				
				for(int i = tempIndex + 1; i <= countSize; i++){
					if(allIndex[i] - 1 < 0)
						allIndex[i] = 0;
					else
						allIndex[i]--;
				}
				allIndex[tempIndex] = 0;
				countText--;
				for (int i = 0; i <= countText; i++) {
					allCheckBoxText.add(checkBoxText[i]);
				}
				for (int i = 0; i < deleteSize; i++) {
					allCheckBoxText.add(deleteCheckBoxText[i]);
				}
				add = new JPanel();
				JLabel addtext1 = new JLabel("+");
				addtext1.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
				JLabel addtext2 = new JLabel("清單項目");
				addtext2.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
				add.add(addtext1);
				add.add(addtext2);
				add.setBackground(textColor);
				add.addMouseListener(new MouseClickHandler());// add handler
				allCheckBoxText.add(add);
				setVisible(true); // display frame
			}
			
		}
		
	}
	
	private class MouseClickHandler extends MouseAdapter{ 

		public void mouseClicked(MouseEvent event){
			allCheckBoxText.remove(add);
			allCheckBoxText.revalidate();
			allCheckBoxText.repaint();
			countText++;
			countSize++;
			checkBoxText[countText] = new JPanel();
			checkBox[countText] = new JCheckBox();
			checkBox[countText].setBackground(textColor);
			checkBox[countText].addItemListener(new checkBoxHandler(countSize));
			text[countText] = new JTextField("事項", 18);
			text[countText].setFont(new Font("微軟正黑體", Font.PLAIN, 14));
			text[countText].setBackground(textColor);
			text[countText].addActionListener(new Handler(countText));
			checkBoxText[countText].add(checkBox[countText]);
			checkBoxText[countText].add(text[countText]);
			checkBoxText[countText].setBackground(textColor);
			for (int i = 0; i <= countText; i++) {
				allCheckBoxText.add(checkBoxText[i]);
			}
			for (int i = 0; i < deleteSize; i++) {
				allCheckBoxText.add(deleteCheckBoxText[i]);
			}
			add = new JPanel();
			JLabel addtext1 = new JLabel("+");
			addtext1.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
			JLabel addtext2 = new JLabel("清單項目");
			addtext2.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
			add.add(addtext1);
			add.add(addtext2);
			add.setBackground(textColor);
			add.addMouseListener(new MouseClickHandler());// add handler
			allCheckBoxText.add(add);
			allCheckBoxText.setBackground(textColor);
			addScrollPane(countSize);
			//setVisible(true); // display frame
		}
	}
    
    public void changeColor() {
    	menuBar.setBackground(titleColor);
		title.setBackground(titleColor);
		for(int i = 0; i <= countText; i++){
			checkBox[i].setBackground(textColor);
			text[i].setBackground(textColor);
			checkBoxText[i].setBackground(textColor);
		}
		for (int i = 0; i < deleteSize; i++) {
			deleteCheckBox[i].setBackground(textColor);
			deleteText[i].setBackground(textColor);
			deleteCheckBoxText[i].setBackground(textColor);
		}
		add.setBackground(textColor);
		allCheckBoxText.setBackground(textColor);
		colorChange();
    }
    
    public void selectColor(int colorRBG) {
    	switch(colorRBG){
    	case -1973791 :
    		titleColor = new Color(225, 225, 225);
			textColor = new Color(248, 248, 248);
    	break;
    	
    	case -26215 :
    		titleColor = new Color(255, 153, 153);
			textColor = new Color(255, 235, 235);
        break;
        	
    	case -108 :
    		titleColor = new Color(255, 255, 148);
			textColor = new Color(255, 255, 224);
        break;
        	
    	case -3342464 :
    		titleColor = new Color(204, 255, 128);
			textColor = new Color(239, 255, 214);
        break;
        	
    	case -6697729 :
    		titleColor = new Color(153, 204, 255);
			textColor = new Color(235, 244, 255);
        break;
        	
    	case -3436545 :
    		titleColor = new Color(203, 143, 255);
			textColor = new Color(235, 235, 255);
        break;
    		
    	}
    }
    
    public String getColor() {
    	String color = Integer.toString(titleColor.getRGB());
    	return color;
    }
    
    public String save(){
    	reminderTime = reminder.getTimeString();
    	String saveString = "";
    	saveString += reminderTime;
    	saveString += "\n";
    	saveString += titleColor.getRGB();
    	saveString += "\n";
    	saveString += title.getText();
    	saveString += "\n";
    	saveString += countText;
    	saveString += "\n";
    	saveString += deleteSize;
    	saveString += "\n";
    	saveString += countSize;
    	saveString += "\n";
    	saveString += size;
    	saveString += "\n";
    	for(int i = 0; i <= countText; i++){
    		saveString  = saveString + text[i].getText() + "\n";
    	}
    	if(deleteSize != 0){
    	for(int i = 0; i < deleteSize - 1; i++){
    		saveString  = saveString + deleteText[i].getText() + "\n";
    	}
    	
    	saveString  = saveString + deleteText[deleteSize - 1].getText() + "\n";
    	}
    	return saveString;
    }
    
    
	@SuppressWarnings("unchecked")
	public void load(String body) {
    	String data[] = body.split("\n");
    	reminderTime = data[0];
    	selectColor(Integer.parseInt(data[1])); // data[1] is title color
    	title.setText(data[2]); // data[2] is title
    	title.setBackground(titleColor);
    	menuBar.setBackground(titleColor);
    	countText = Integer.parseInt(data[3]);
    	deleteSize = Integer.parseInt(data[4]);
    	countSize = Integer.parseInt(data[5]);
    	//size = Integer.parseInt(data[6]);
    	this.initContentArea();
    	
    	for(int i = 0; i <= countSize; i++){
    		checkBoxText[i] = new JPanel();
    		checkBox[i] = new JCheckBox();
    		text[i] = new JTextField("事項", 18);
    		text[i].setFont(new Font("微軟正黑體", Font.PLAIN, 14));
    		deleteCheckBoxText[i] = new JPanel();  		
    	}
    	
    	for(int i = 7, k = 0, j = deleteSize; i <= 7 + countText; i++, k++, j++){
    		checkBoxText[k].setBackground(textColor);
    		checkBox[k].setBackground(textColor);
    		checkBox[k].addItemListener(new checkBoxHandler(j));
    		checkBoxText[k].add(checkBox[k]);
    		text[k].setBackground(textColor);
    		text[k].addActionListener(new Handler(countText));
    		text[k].setText(data[i]);
    		checkBoxText[k].add(text[k]);
    		allCheckBoxText.add(checkBoxText[k]);
    		addScrollPane(k);
    	}
    	for(int i = 8 + countText, k = countText + 1, j = 0; i < 8 + countText + deleteSize; i++, k++, j++){
    		Font font = new Font("微軟正黑體", Font.PLAIN, 14);
			@SuppressWarnings("rawtypes")
			Map attributes = font.getAttributes();
			attributes.put(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON);
			Font newFont = new Font(attributes);
			text[k].setText(data[i]);
			text[k].setBackground(textColor);
			text[k].setFont(newFont);
			text[k].setEditable(false);//設定為不可編輯
			checkBox[k].setBackground(textColor);
			checkBox[k].setSelected(true);
			checkBox[k].setEnabled(false);
			checkBoxText[k].add(checkBox[k]);
			checkBoxText[k].add(text[k]);
			
			deleteCheckBoxText[j].setBackground(textColor);
			deleteCheckBox[j] = checkBox[k];
			deleteCheckBox[j].setBackground(textColor);
			deleteText[j] = text[k];
			deleteText[j].setBackground(textColor);
			deleteCheckBoxText[j].add(deleteCheckBox[j]);
			deleteCheckBoxText[j].add(deleteText[j]);
			allCheckBoxText.add(deleteCheckBoxText[j]);
			addScrollPane(k);
    	}
    }
    
    public void showScrollPane(int countNumber){
    	if(countNumber >= 12 && checked == 0){
	    	jsp = new JScrollPane();
	    	jsp.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
	    	jsp.setAutoscrolls(true);
	    	jsp.setViewportView(allCheckBoxText);
	    	add(jsp);
	    	checked = 1;
    	}
    }
    
    public void addScrollPane(int countNumber){
	    if (countNumber == 12) {
			size += 1;
			//System.out.println(size);
			allCheckBoxText.setLayout(new GridLayout(size, 1));
			jsp = new JScrollPane();
	    	jsp.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
	    	jsp.setAutoscrolls(true);
	    	jsp.setViewportView(allCheckBoxText);
	    	add(jsp);
		}
		if(countNumber > 12){
			size += 1;
			//System.out.println(size);
			allCheckBoxText.setLayout(new GridLayout(size, 1));
		}
    }
    
    public JMenuBar getAMenuBar(){
    	return menuBar;
    }
    
    public JTextField getTitleText(){
    	return title;
    }
    
    public JPanel getAllCheckBoxText() {
    	return allCheckBoxText;
    }
    
    public JPanel getAdd() {
    	return add;
    }
    
    public JTextField[] getDeleteText(){
		return deleteText;	
    }
    
    public JCheckBox[] getDeleteCheckBox(){
    	return deleteCheckBox;
    }
    
    public JTextField[] getText(){
		return text;	
    }
    
    public JCheckBox[] getCheckBox(){
    	return checkBox;
    }
    
    public int getcountText() {
    	return countText;
    }
    
    public int getDeleteSize() {
    	return deleteSize;
    }
    
    public int getCountSize() {
    	return countSize;
    }
    
    public String getListTitle() {
    	return title.getText();
    }
    
    protected void delete() {
        manager.deleteList(this);
    }
    
    public void close(){
    	manager.closeList(this);
    }
    
    public void colorChange() {
		manager.changeColors(this, titleColor.getRGB());
    }
    
    public void remind(){
    	reminder.getSetTimeFrame().setVisible(true);
	} // end method remind

}