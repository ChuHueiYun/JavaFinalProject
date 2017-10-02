import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;

import index.Index;

public class PasteIt extends JFrame {
	private static final long serialVersionUID = 1L;
	private JDesktopPane desktopPane;
	private Index index;
	private JLabel homeLabel, exitLabel;
	private ImageIcon home, exit;
	private boolean temp;
	
    public PasteIt() {
        super("Paste It!");
        this.desktopPane = new JDesktopPane();
        
        this.index = new Index(this.desktopPane);
        this.index.run();
        this.desktopPane.add(this.index);
		java.awt.Dimension scr_size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		temp = false;
		URL path = PasteIt.class.getResource("/images/home.png");
		home = new ImageIcon(path);
		path = PasteIt.class.getResource("/images/main_exit.png");
		exit = new ImageIcon(path);
		homeLabel = new JLabel();
		exitLabel = new JLabel();
		homeLabel.setPreferredSize(new Dimension(80, 80));
		exitLabel.setPreferredSize(new Dimension(80, 80));
        homeLabel.setBounds(new Rectangle(new Point(scr_size.width-160,scr_size.height-160), homeLabel.getPreferredSize()));
        exitLabel.setBounds(new Rectangle(new Point(scr_size.width-80,scr_size.height-160), homeLabel.getPreferredSize()));
		homeLabel.setIcon(home);
		exitLabel.setIcon(exit);
        this.desktopPane.add(homeLabel);
        this.desktopPane.add(exitLabel);
    	
    	
        
        homeLabel.addMouseListener(new MouseAdapter()  
		{  
		     public void mouseClicked(MouseEvent e)  
		     {	 
		    	 index.initFrame(temp);
		    	 temp=!temp;
		    	 
		     }
        });
        
        exitLabel.addMouseListener(new MouseAdapter()  
		{  
		     public void mouseClicked(MouseEvent e)  
		     {	 
		    	 setVisible(false);    	 
		     }
         });
        
        this.setContentPane(desktopPane);
        this.desktopPane.setDragMode(JDesktopPane.OUTLINE_DRAG_MODE);
    }
    public void run() {
        this.setUndecorated(true);
        this.setBackground(new Color(0, 0, 0, 0));
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setVisible(true);
    }
}