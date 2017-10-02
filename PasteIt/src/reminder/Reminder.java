package reminder;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import com.github.lgooddatepicker.components.DatePicker;
import com.github.lgooddatepicker.components.TimePicker;
import com.github.lgooddatepicker.components.TimePickerSettings;
import com.github.lgooddatepicker.components.TimePickerSettings.TimeArea;

import utility.ComponentMover;

public class Reminder extends TimerTask{
	private JDesktopPane desktopPane;
	private JInternalFrame setTimeFrame;
	private JButton checkButton;
	private JButton cancelButton;
	private JButton exitButton;	
	private JLabel setTimeLable;
	private JLabel setTimeTitle;
	private DatePicker datePicker;
	private TimePicker timePicker;
	private Timer timer;
	private Date inputTime;
	private JInternalFrame targetFrame;
	private String timeString;
	
	public JInternalFrame getSetTimeFrame() {
		return setTimeFrame;
	}
	public String getTimeString() {
		return timeString;
	}

	public void setTimeString(String timeString) {
		this.timeString = timeString;
	}
	
	//constructor
	public Reminder(JDesktopPane desktop, JInternalFrame inputFrame, String reminderTime){
		desktopPane = desktop;
		targetFrame = inputFrame;
		setTimeString(reminderTime);
		setTimeWindow(reminderTime);
		if(!"".equals(reminderTime)){		//reminderTime有紀錄
			SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
			try {
				inputTime = dateFormatter.parse(reminderTime);	//String轉Date
				setSchedule(targetFrame ,inputTime);
			} catch (ParseException e) {
				 System.out.println("輸入的日期字串格式有誤");
			}
		}
	}
	
	//設定時間視窗
    public void setTimeWindow(String reminderTime){
    	Color backgroundColor = new Color(36, 121, 158);
    	Color textColor = new Color(255, 224, 102);
    	
    	setTimeFrame = new JInternalFrame("設定提醒時間:",false,false,false,false);
    	setTimeFrame.setBorder(null);
    	setTimeFrame.getContentPane().setBackground(backgroundColor);
    	setTimeFrame.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));	
    	setTimeFrame.setSize(250, 250);
    	//setTimeFrame.setLocationRelativeTo(null);
    	
    	BasicInternalFrameUI basicInternalFrameUI = (BasicInternalFrameUI)setTimeFrame.getUI();
        basicInternalFrameUI.setNorthPane(null);
    	
    	//選擇日期 :
    	JPanel selectDatePanel = new JPanel(new FlowLayout(FlowLayout.LEFT ,10, 10));
    	selectDatePanel.setPreferredSize(new Dimension(250, 50)); 
    	selectDatePanel.setBackground(backgroundColor);
    	
    	JLabel dateLable = new JLabel(new ImageIcon(Reminder.class.getResource("/images/calendar.png")));
    	dateLable.setToolTipText( "選擇日期" );
    	selectDatePanel.add(dateLable);
    	
    	datePicker = new DatePicker();
    	selectDatePanel.add(datePicker);
    	
    	setTimeFrame.add(selectDatePanel);
    	
        //選擇時間 :
    	JPanel selectTimePanel = new JPanel(new FlowLayout(FlowLayout.LEFT ,10, 10));
    	selectTimePanel.setPreferredSize(new Dimension(250, 50)); 
    	selectTimePanel.setBackground(backgroundColor);
    	
    	JLabel timeLable = new JLabel(new ImageIcon(Reminder.class.getResource("/images/clock.png")));
    	timeLable.setToolTipText( "選擇時間" );
    	selectTimePanel.add(timeLable);
    	
    	
    	TimePickerSettings timeSettings = new TimePickerSettings();
        timeSettings.setColor(TimeArea.TimePickerTextValidTime, Color.black);
        timeSettings.initialTime = LocalTime.now();
        timePicker = new TimePicker(timeSettings);
        selectTimePanel.add(timePicker);
        
    	setTimeFrame.add(selectTimePanel);
    	
        //中間按鈕
        JPanel centerButtonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 50, 10));
        centerButtonPanel.setPreferredSize(new Dimension(250, 50)); 
        centerButtonPanel.setBackground(backgroundColor);
        		
        //取消按鈕
		cancelButton = new JButton(new ImageIcon(Reminder.class.getResource("/images/cancel.png")));
        cancelButton.setRolloverIcon(new ImageIcon(Reminder.class.getResource("/images/cancel_Rollover.png")) );
        cancelButton.setContentAreaFilled(false);
        cancelButton.setFocusPainted(false);
        cancelButton.setBorderPainted(false);
        cancelButton.setBorder(null);
        cancelButton.setToolTipText( "清除已設定的提醒" );
        centerButtonPanel.add(cancelButton);
		//確認按鈕
		checkButton = new JButton(new ImageIcon(Reminder.class.getResource("/images/check.png")));
		checkButton.setRolloverIcon(new ImageIcon(Reminder.class.getResource("/images/check_Rollover.png")));
		checkButton.setContentAreaFilled(false);
		checkButton.setFocusPainted(false);
		checkButton.setBorderPainted(false);
		checkButton.setBorder(null);
		checkButton.setToolTipText( "新增提醒時間" );
		centerButtonPanel.add(checkButton);
          
        setTimeFrame.add(centerButtonPanel);

        //底部文字區塊
        JPanel buttomTextPanel = new JPanel(new GridLayout(2, 1, 20, 20));
        buttomTextPanel.setPreferredSize(new Dimension(250, 50));
        buttomTextPanel.setBackground(backgroundColor);
        
        setTimeTitle = new JLabel("  已設定的提醒時間:");
        setTimeTitle.setFont(new Font("微軟正黑體", Font.BOLD, 15));
        setTimeTitle.setForeground(Color.WHITE);
        buttomTextPanel.add(setTimeTitle);
        
        if("".equals(reminderTime)){		//reminderTime有紀錄
        	setTimeLable = new JLabel("  無");
        }
        else{
        	setTimeLable = new JLabel(reminderTime);
        }
        
        setTimeLable.setFont(new Font("微軟正黑體", Font.BOLD, 18));
        setTimeLable.setForeground(textColor);
        buttomTextPanel.add(setTimeLable);
        
        setTimeFrame.add(buttomTextPanel);
        
        //離開按鈕
        JPanel exitButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        exitButtonPanel.setPreferredSize(new Dimension(250, 50));
        exitButtonPanel.setBackground(backgroundColor);
        
        exitButton = new JButton(new ImageIcon(Reminder.class.getResource("/images/exit.png")));
        exitButton.setRolloverIcon( new ImageIcon(Reminder.class.getResource("/images/exit_Rollover.png")) );
        exitButton.setContentAreaFilled(false);
        exitButton.setFocusPainted(false);
        exitButton.setBorderPainted(false);
        exitButton.setBorder(null);
        exitButton.setToolTipText( "離開" );
        
        exitButtonPanel.add(exitButton);
        setTimeFrame.add(exitButtonPanel);
    	setTimeFrame.setVisible(false);		//隱藏設定時間視窗
    	
    	@SuppressWarnings("unused")
		ComponentMover componentMover = new ComponentMover(setTimeFrame, setTimeFrame);
    	
    	desktopPane.add(setTimeFrame);	//加入Reminder到desktopPane中
    	
        //註冊事件監聽器
    	ButtonHandler handler = new ButtonHandler();
    	checkButton.addActionListener( handler );
    	cancelButton.addActionListener( handler );
    	exitButton.addActionListener( handler );
    }
    
    private class ButtonHandler implements ActionListener {
	      // handle button event
		@Override
		public void actionPerformed( ActionEvent event ){
			if(event.getSource() == checkButton){
		        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");  
		        
		    	try {
		    		inputTime = dateFormatter.parse(datePicker.getDateStringOrEmptyString() + " " + timePicker.getTimeStringOrEmptyString());
		    		setSchedule(targetFrame ,inputTime);	//排程	    		
		    		setTimeString(datePicker.getDateStringOrEmptyString() + " " + timePicker.getTimeStringOrEmptyString());
		    		setTimeLable.setText("  " + getTimeString());
				} catch (ParseException e) {
					//e.printStackTrace();
					setTimeLable.setText("  輸入日期格式有誤");
				}
			}
			else if(event.getSource() == cancelButton){
				setTimeLable.setText("無");
				cancelSchedule();
			}
			else if(event.getSource() == exitButton){
				setTimeFrame.setVisible(false);
			}
		}
    }
    
    public void setSchedule(JInternalFrame frame, Date inputTime){
        System.out.println("Reminder的設定時間: " + inputTime);

        if(timer == null){
	        timer = new Timer();
	        timer.schedule(new TimerTask(){
				public void run() { 
					frame.setVisible(true);
				} 
	        }
	        , inputTime);
        }
        else{
        	System.out.println("修改Reminder的設定時間:");
        	timer.cancel();
        	timer.purge();
        	timer = new Timer();
        	timer.schedule(new TimerTask(){
				public void run() { 
					frame.setVisible(true);
				} 
			}
		, inputTime);
        }
        
    }
    
    public void cancelSchedule(){
    	if(timer != null){
    		timer.cancel();
    		timer.purge();
    	}
    }
    
    @Override   
    public void run() {
        System.out.println("Reminder於指定時間執行");
    }

}