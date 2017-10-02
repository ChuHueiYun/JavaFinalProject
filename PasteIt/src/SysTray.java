import java.awt.AWTException; 
import java.awt.Image; 
import java.awt.MenuItem; 
import java.awt.PopupMenu; 
import java.awt.SystemTray; 
import java.awt.Toolkit; 
import java.awt.TrayIcon; 
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.JOptionPane;

public class SysTray { 
	//icon object 
	private TrayIcon trayIcon; 
	//SystemTray object 
	private SystemTray tray = null; 
	//取得圖示,如果找不到圖,在系統列上會是空白的 
	private URL path = SysTray.class.getResource("/images/paste.png");
	private final Image image = Toolkit.getDefaultToolkit().getImage(path);

	//跳出式選單 
	private PopupMenu popup = new PopupMenu(); 
	private MenuItem exitItem = null;
	private static PasteIt pasteIt;
	
	public SysTray(){ 
		//檢查OS是否支援SystemTray 
		if(SystemTray.isSupported()){

			//每個Java程式只能有一個SystemTray實體 
			tray = SystemTray.getSystemTray();

			//設定選單 
			setMenu();

			//設定trayIcon(圖片,滑鼠指上去的Tip訊息,選單) 
			trayIcon = new TrayIcon(image, "PasteIt", popup);

			//設定圖示自動變更尺寸 
			trayIcon.setImageAutoSize(true);

			try { 
				//把trayIcon加入tray中 
				tray.add(trayIcon); 
			}catch (AWTException e) { 
				System.err.println("TrayIcon could not be added."); 
			}
			
			//加入事件 
			exitItem.addActionListener(new ActionListener() { 
				public void actionPerformed(ActionEvent e) { 
					System.out.println("離開"); 
					System.exit(0); 
				} 
			});
			trayIcon.addMouseListener(new MouseAdapter()  
	     	{  
	 		    public void mouseClicked(MouseEvent e)  
	 		    {   
	 		    	pasteIt.setVisible(true);
	 		    } 
	 		 });  
						
		}else{ 
			JOptionPane.showMessageDialog(null, "SystemTray not support!"); 
		} 
	}

	private void setMenu(){ 
		//加入選單 
		exitItem = new MenuItem("END");
		popup.add(exitItem); 
	}


	public static void main(String[] args) { 
		pasteIt = new PasteIt();
		pasteIt.run();
	    pasteIt.setVisible(false);
		new SysTray();
	    
	}

}