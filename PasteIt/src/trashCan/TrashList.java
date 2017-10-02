package trashCan;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTextField;

import projectList.ListManager;

public class TrashList extends projectList.singleList{
	private static final long serialVersionUID = 1L;
	private JTextField listTitle;
	private JMenuItem deleteItem;
	private JMenuItem recoverItem;
	private JMenuItem closeItem;
	private int countText;
	private int deleteSize;
	protected ListManager manager;
	@SuppressWarnings("unused")
	private JDesktopPane desktopPane;
	
	public TrashList(JDesktopPane desktopPane, ListManager manager,String body){
		super(desktopPane, manager, body);
		this.manager = manager;
		this.desktopPane = desktopPane;
		this.listTitle = super.getTitleText();
		this.countText = super.getcountText();
		this.deleteSize = super.getDeleteSize();
		this.setNotEdit();
		this.addItem();
	}
	
	private void setNotEdit() {
		super.getTitleText().setEnabled(false);
		super.getAllCheckBoxText().setEnabled(false);
		for(int i = 0; i <= this.countText; i++){
			super.getText()[i].setEnabled(false);
			super.getCheckBox()[i].setEnabled(false);
		}
		for(int i = 0; i < this.deleteSize; i++){
			super.getDeleteText()[i].setEnabled(false);
			super.getDeleteCheckBox()[i].setEnabled(false);
		}
		super.getAdd().setVisible(false);
	}
	
	private void addItem() {
		JMenu setMenu = new JMenu(""); 
		URL setPicture = TrashList.class.getResource("/images/settings.png");
		setMenu.setIcon(new ImageIcon(setPicture));
		setMenu.setToolTipText("設定");
        
		deleteItem = new JMenuItem("刪除");
        deleteItem.setPreferredSize(new Dimension(70, 20));
        deleteItem.addMouseListener(new MouseHandler());
        setMenu.add(deleteItem);
        
        recoverItem = new JMenuItem("還原");
        recoverItem.addMouseListener(new MouseHandler());
        setMenu.add(recoverItem);
       
        closeItem = new JMenuItem("關閉");
        closeItem.addMouseListener(new MouseHandler());
        setMenu.add(closeItem);
        
        setMenu.setPopupMenuVisible(true);
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(listTitle);
        menuBar.add(setMenu);
		menuBar.setBackground(new Color(225, 225, 225));
		this.setJMenuBar(menuBar);
	}
	
	private class MouseHandler extends MouseAdapter{ 
    	
    	public void mousePressed(MouseEvent event){
    		if(event.getSource() == closeItem){ // 關閉
    			setVisible(false);
			}else if(event.getSource() == deleteItem){ // 刪除
				realyDelete();
			}else{ // 還原
				recover();
			}
    	}
	}
	
	public void realyDelete() {
        this.manager.RealyDeleteList(this);
    }
	
	public void recover(){
		this.manager.recoverList(this);
	}
	
}
