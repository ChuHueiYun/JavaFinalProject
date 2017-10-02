package trashCan;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JTextField;

import stickyNote.NoteManager;

public class TrashNote extends stickyNote.Note {
	private static final long serialVersionUID = 1L;
	private JTextField noteTitle;
	private JPanel noteTitleBar;
	private JMenuItem deleteItem;
	private JMenuItem recoverItem;
	private JMenuItem closeItem;
	protected NoteManager manager;
	@SuppressWarnings("unused")
	private JDesktopPane desktopPane;

	public TrashNote(JDesktopPane desktopPane, NoteManager noteManager, String[] tags, String reminderTime, String color, String size, String title, String content) {
		super(desktopPane, noteManager, tags, reminderTime, color, size, title, content);
		this.manager = noteManager;
		this.desktopPane = desktopPane;
		this.noteTitleBar = super.getCustomTitleBar();
		this.noteTitleBar.remove(super.getTitleComponent());
		this.noteTitleBar.remove(super.getAMenuBar());
		this.noteTitle = super.getTitleComponent();
		this.setNotEdit();
		this.addItem();
	}
	private void setNotEdit() {
		super.getTitleComponent().setEnabled(false);
		super.getContentComponent().setEnabled(false);
	}
	
	private void addItem() {
		JMenu setMenu = new JMenu(""); 

		URL setPicture = TrashNote.class.getResource("/images/settings.png");
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
        menuBar.add(setMenu);
        this.noteTitleBar.add(noteTitle);
        this.noteTitleBar.add(menuBar);
        this.add(this.noteTitleBar, BorderLayout.NORTH);
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
        this.manager.RealyDeleteNote(this);
    }
	
	public void recover(){
		this.manager.recoverNote(this);
	}
	
}
