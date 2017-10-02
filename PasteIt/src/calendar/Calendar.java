package calendar;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.GregorianCalendar;
import java.util.prefs.Preferences;
import utility.ComponentMover;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicInternalFrameUI;


public class Calendar extends JInternalFrame{
	private static final long serialVersionUID = 1L;
	private String months[] = {
    "Jan", "Feb", "Mar", "Apr",
    "May", "Jun", "Jul", "Aug",
    "Sep", "Oct", "Nov", "Dec"};
    private int today[] = {0, 0, 0};	
    private JPanel jPanel1, jPanel2, jPanel3, jPanel4;
    private JPanel []dayJPanel;
    private JLabel label1, previousLabel, nextLabel, weekLabel, paletteLabel, saveLabel, closeLabel;
    private JLabel []dayLabel;
    private JLabel []iconLabel;
    private Font font1, font2, font3;
	private ImageIcon previousIcon, nextIcon, star, palette, save, close;
	private int selectYear, selectMonth, selectDay=1;
 	private int nowcolor, dateCount, dayOne;
 	private Preferences prefs;
 	private String ID = "Color";
 	private JTextArea text;
	@SuppressWarnings("unused")
	private JDesktopPane desktopPane;

	public Calendar(JDesktopPane desktopPane){
		this.desktopPane = desktopPane;	
	}
	
    public void initGUI(){
    	
    	jPanel1 = new JPanel();
    	 
    	getdate();
    	selectYear = today[0];
    	selectMonth = today[1];   	 
    	 
    	jPanel2 = new JPanel();
    	jPanel2.setLayout(new BorderLayout());
    	String temp = today[0]+" "+months[today[1]];
    	label1 = new JLabel(temp, JLabel.CENTER);
    	font1 = new Font("微軟正黑體", Font.BOLD, 30);
    	font2 = new Font("微軟正黑體", Font.BOLD, 20);
    	font3 = new Font("微軟正黑體",Font.PLAIN,25);
    	label1.setFont(font1);
    	label1.addMouseListener(new MouseAdapter()  
     	{  
 		    public void mouseClicked(MouseEvent e)  
 		    {   
 		    	int clickTimes=e.getClickCount();
 		    	if(clickTimes==2){
 		 		    label1.setText(today[0]+" "+months[today[1]]);
 		 		    selectYear = today[0];
 		 		    selectMonth = today[1];
 		 		    createDateButton();
 		    	}
 		    } 
 		 }); 
    	 
 		 URL path = Calendar.class.getResource("/images/c_previous.png");
 		 previousIcon = new ImageIcon(path);

 		 path = Calendar.class.getResource("/images/c_next.png");
 		 nextIcon = new ImageIcon(path);		 		 

 		 previousLabel = new JLabel(previousIcon);
 	 	 previousLabel.addMouseListener(new MouseAdapter()  
 		 {  
 		     public void mouseClicked(MouseEvent e)  
 		     {  
 		    	 if(selectMonth==0){
 		    		selectMonth=11;
 		    		selectYear--;
 		    	 }else selectMonth--;
 		    	 label1.setText(selectYear+" "+months[selectMonth]);
 		    	 createDateButton();
 		    	 text.setText("");
 		       	 jPanel3.setVisible(false);
 		       	 setSize(500, 550);		    		
 		     }  
 		 }); 
 		  
 		 nextLabel = new JLabel(nextIcon);
 		 nextLabel.addMouseListener(new MouseAdapter()  
 		 {  
 		     public void mouseClicked(MouseEvent e)  
 		     {   
 		    	 if(selectMonth==11){
 		    		selectMonth=0;
 		    		selectYear++;
 		    	 }else selectMonth++;
 		    	 label1.setText(selectYear+" "+months[selectMonth]);
 		    	 createDateButton();
 		    	 text.setText("");
 		       	 jPanel3.setVisible(false);
 		       	 setSize(500, 550);	
 		     }  
 		 }); 
 		 
    	 jPanel2.add(previousLabel, BorderLayout.WEST);
    	 jPanel2.add(label1, BorderLayout.CENTER);   	 
    	 jPanel2.add(nextLabel, BorderLayout.EAST);
    	 
         weekLabel = new JLabel("    Sun     Mon      Tue      Wed      Thu        Fri         Sat");
         weekLabel.setFont(font2);
         jPanel2.add(weekLabel, BorderLayout.SOUTH);
         
         @SuppressWarnings("unused")
		ComponentMover componentMover1 = new ComponentMover(JInternalFrame.class, label1);
         
     	 jPanel3 = new JPanel();
     	 jPanel3.setVisible(false);
     	 jPanel4 = new JPanel();
     	 jPanel4.setLayout(new BorderLayout());
     	 text = new JTextArea(); 	
    }   
    
	public void getdate(){	//取得系統日期
		GregorianCalendar calendar = new GregorianCalendar();  
		today[0] = calendar.get(GregorianCalendar.YEAR);//年
	  	today[1] = calendar.get(GregorianCalendar.MONTH);//月
	  	today[2] = calendar.get(GregorianCalendar.DAY_OF_MONTH);//日
	}
	
	public void createDateButton(){		//日期按鈕
		
		@SuppressWarnings("unused")
		int y=0, x=0;
   	 	font3 = new Font("微軟正黑體", Font.BOLD, 15);
	    
	    remove(jPanel1);
	    jPanel1 = new JPanel();
	    jPanel1.setLayout(new GridLayout(6, 7));
	    switch(selectMonth+1){	//設定月份天數
			case 1:
	        case 3:
	        case 5:
	        case 7:
	        case 8:
	        case 10:
	        case 12:
	            dateCount = 31;		//一個月31天
	        break;
	            
	        case 4:	//小月30天
	        case 6:
	        case 9:
	        case 11:
	        	dateCount = 30;		//一個月30天
	        break;
	            
	        case 2:
	            if (leap_year(selectYear))
	            	dateCount = 29;		//閏年，一個月29天
	            else
	            	dateCount = 28;		//平年，一個月28天
	    }
	        
	    dayOne= firstDay(selectYear,selectMonth+1,1);	//取得當月第一天是星期幾

	    
	    dayJPanel = new JPanel[42];	    //建立日期Panel
	    dayLabel = new JLabel[42];		//建立日期Label
	    iconLabel = new JLabel[42]; 

		URL path = Calendar.class.getResource("/images/c_star.png");
		star = new ImageIcon(path);
		path = Calendar.class.getResource("/images/c_exit.png");
		close = new ImageIcon(path);
		path = Calendar.class.getResource("/images/c_palette.png");
		palette = new ImageIcon(path);
		
		jPanel1.setOpaque(true);
		for(int j=0;j<dayOne;j++){
	    	dayJPanel[j] = new JPanel();
			jPanel1.add(dayJPanel[j]);
		}
		int i;
	    for(int j=dayOne;j<dateCount+dayOne;j++){
	    	i = j-dayOne;
	    	dayLabel[i] = new JLabel();
	    	dayJPanel[j] = new JPanel();
	    	iconLabel[i] = new JLabel();
	    	dayJPanel[j].setLayout(new BorderLayout());
		    dayLabel[i] = new JLabel(String.valueOf(i+1), JLabel.CENTER);
		    dayLabel[i].setOpaque(true);
		    iconLabel[i].setOpaque(true);
            dayLabel[i].setForeground(new Color(255, 255, 255));
	        
	        dayLabel[i].setFont(font3);	//設定字體大小及樣式
	            
	        loadEventIcon(i+1);
	        
	        final int temp=i;
	        iconLabel[i].addMouseListener(new MouseAdapter()  
			 {  
			     public void mouseClicked(MouseEvent e)  
			     {	 
			    	 addEvent(temp+1);
	        	 }
	        });
	        
	        x++;//下一個按鈕X座標加權
	        dayJPanel[j].add(iconLabel[i], BorderLayout.CENTER);
	        dayJPanel[j].add(dayLabel[i], BorderLayout.NORTH);
            if (selectYear == today[0] && selectMonth == today[1] && i+1 == today[2])
            	dayJPanel[j].setBorder(BorderFactory.createLineBorder(new Color(105, 105, 105), 3));		//當天
	        iconLabel[i].setBorder(BorderFactory.createLineBorder(new Color(211, 211, 211), 1));
	        jPanel1.add(dayJPanel[j]); 
	    }
		for(int j=dayOne+dateCount;j<40;j++){
	    	dayJPanel[j] = new JPanel();
			jPanel1.add(dayJPanel[j]); 
		}
		closeLabel = new JLabel();
		dayJPanel[41] = new JPanel();
		closeLabel.setIcon(close);
		dayJPanel[41].add(closeLabel);
		closeLabel.setOpaque(true);
		
		paletteLabel = new JLabel();
    	dayJPanel[40] = new JPanel();
		paletteLabel.setIcon(palette);	
        dayJPanel[40].add(paletteLabel);       
        paletteLabel.setOpaque(true);
        
		jPanel1.add(dayJPanel[40]); 
		jPanel1.add(dayJPanel[41]); 
		add(jPanel1, BorderLayout.CENTER);
		
		closeLabel.addMouseListener(new MouseAdapter()  
		 {  
		     public void mouseClicked(MouseEvent e)  
		     {   
		    	 int clickTimes=e.getClickCount();
		    	 if(clickTimes==2)
		    	 {
		    		 setVisible(false);
		    	 }
		     }  
		 });		

		paletteLabel.addMouseListener(new MouseAdapter()  
		 {  
		     public void mouseClicked(MouseEvent e)  
		     {   
		    	 int clickTimes=e.getClickCount();
		    	 if(clickTimes==2)
		    	 {
		    		 changeColor();
		    	 }
		     }  
		 });
    	setColor();
	}

	public boolean leap_year(int year){		//判斷閏年平年
		boolean leapYear;
		if ((year % 400 == 0)||(year % 4 == 0 && year % 100 !=0)){
			leapYear = true;
		}else{
			leapYear = false;
		}
	    return leapYear;
	}
	  
	public int firstDay(int y,int m,int d){		//判斷星期
		int[] temp1 = {6, 2, 2, 5, 0, 3, 5, 1, 4, 6, 2, 4};//天文星體運行值
	    int temp2;
	    temp2 = temp1[m-1] + y + (y/4) - (y/100) + (y/400);		//閏年
	    if(((y%4) == 0) && (m<3)){
	    	temp2--;
	        if((y%100) == 0) temp2++;
	        if((y%400) == 0) temp2--;
	    }
	    return (temp2+d) % 7;		//0 = 星期日
	    
	}	
	
    public void initFrame(){
    	setSize(500, 550);
    	setResizable(false);
    	setClosable(false);
    	setMaximizable(false);
    	setIconifiable(false);
    	setBorder(null);
    	BasicInternalFrameUI basicInternalFrameUI = (BasicInternalFrameUI)this.getUI();
    	basicInternalFrameUI.setNorthPane(null);
		setVisible(true);
    }
    
    public void loadEventIcon(int d){
	    String syear,smonth,sday,filename;	//記事
	    syear = String.valueOf(selectYear);
	    if(selectMonth<9)
	    	smonth = "0"+String.valueOf(selectMonth+1);
	    else
	    	smonth = String.valueOf(selectMonth+1);
	    
	    if(d<10)
	    	sday = "0"+String.valueOf(d);
	    else
	    	sday = String.valueOf(d);
        
        filename = syear+smonth+sday;//記事檔案名稱(年+月+日.txt)
        File file=new File(System.getProperty("user.home") + "/Desktop/PasteIt/calendar/"+filename+".txt");//檔案物件
        if(file.exists()){
        	iconLabel[d-1].setHorizontalAlignment(SwingUtilities.CENTER);
            iconLabel[d-1].setIcon(star);
        }else{
            file.getParentFile().mkdirs();
        }
    }

    public void addEvent(int d){
    	dayLabel[selectDay-1].setForeground(new Color( 255, 255, 255));
    	selectDay = d;
    	dayLabel[d-1].setForeground(new Color( 0, 0, 0));
    	enlargeFream();
    	loadFile();
    	jPanel3.setVisible(true);
    	add(jPanel3, BorderLayout.SOUTH);
    	setSize(500, 700);
    	

    }

    public void enlargeFream(){
    	saveLabel = new JLabel(); 
		URL path = Calendar.class.getResource("/images/c_save.png");
		save = new ImageIcon(path);
		saveLabel.setIcon(save);
    	text.setLineWrap(true);
    	text.setPreferredSize(new Dimension(300, 140));
    	text.setFont(new Font("微軟正黑體",Font.PLAIN,20));
    	text.setForeground(new Color(128, 128, 128));
    	text.setEditable(false);

    	text.addMouseListener(new MouseAdapter()  
		 {  
		     public void mouseClicked(MouseEvent e)  
		     {   
		    	 int clickTimes=e.getClickCount();
		    	 if(clickTimes==2)
		    	 {
		    		text.setEditable(true);
		    	 }
		     }  
		 });
		saveLabel.addMouseListener(new MouseAdapter()  
		{  
		    public void mouseClicked(MouseEvent e)  
		    {    
		    	writeFile();
		    }  
		});

    	jPanel4.add(saveLabel, BorderLayout.CENTER);
    }

    public void loadFile(){
    	text.setText("");
	    String syear, smonth, sday, filename, read_str;	//記事
	    syear = String.valueOf(selectYear);
	    if(selectMonth<9)
	    	smonth = "0"+String.valueOf(selectMonth+1);
	    else
	    	smonth = String.valueOf(selectMonth+1);
	    if(selectDay<10)
	    	sday = "0"+String.valueOf(selectDay);
	    else
	    	sday = String.valueOf(selectDay);
	    filename = syear+smonth+sday;
	    try
	    {	
	    	File tempFile = new File(System.getProperty("user.home") + "/Desktop/PasteIt/calendar/"+filename+".txt");
	        FileReader fr = new FileReader(tempFile);	//讀取選擇日期記事檔案
	        BufferedReader bfr = new BufferedReader(fr);		//將檔案讀到緩衝區
	        boolean flag = false;								//旗標
	        while((read_str = bfr.readLine())!=null) // 每次讀取一行，直到檔案結束
	        {
	            if (flag)//從第二行開始每一行第一個位置加入斷行
	                text.append("\n");
	              text.append(read_str);//加入該行訊息
	              flag=true;
	        }
	        fr.close();
	    }catch(FileNotFoundException e)		//無檔案
	    {
	    	
	    }catch(IOException e)//例外處理
	    {
	      e.printStackTrace();
	    }
    	jPanel3.add(text);
    	jPanel3.add(jPanel4);
    }

    public void writeFile(){
        String syear, smonth, sday, filename, insert_str;
	    syear = String.valueOf(selectYear);
	    if(selectMonth<9)
	    	smonth = "0"+String.valueOf(selectMonth+1);
	    else
	    	smonth = String.valueOf(selectMonth+1);
	    if(selectDay<10)
	    	sday = "0"+String.valueOf(selectDay);
	    else
	    	sday = String.valueOf(selectDay);
	    filename = syear+smonth+sday;
	    System.out.print(filename);
        insert_str = text.getText();//記事內容
        System.out.print("\n"+insert_str);
        if (insert_str.length() != 0)//若記事框內有文字且有選擇日期則儲存記事檔案
        {
            try
            {
            	File tempFile = new File(System.getProperty("user.home") + "/Desktop/PasteIt/calendar/"+filename+".txt");
            	if (!tempFile.exists()) tempFile.createNewFile();
                FileWriter fw=new FileWriter(tempFile);//啟用檔案寫入
                BufferedWriter bfw=new BufferedWriter(fw);//啟用緩衝區寫入
                bfw.write(insert_str); //將Textarea內容寫入緩衝區裡
                bfw.flush();			//將緩衝區資料寫到檔案
                fw.close();				//關閉檔案
            	loadEventIcon(selectDay);
            }catch(IOException e)
            {
            	e.printStackTrace();
            }
        }else{
	        File file=new File(System.getProperty("user.home") + "/Desktop/PasteIt/calendar/"+filename+".txt");//檔案物件
	        if(file.exists()){
	        	iconLabel[selectDay-1].setIcon(null);
	        	file.delete();
	        }  	
        }
        
        
    }
    
    public void setColor(){
    	int i;
    	
    	switch(nowcolor){
    	case 0:{
    		jPanel1.setBackground(new Color(243, 241, 243));		//日期背景
       	 	jPanel2.setBackground(new Color(240, 121, 149));		//標題背景
            weekLabel.setForeground(new Color(190, 59, 69));		//月份標題
       	 	label1.setForeground(new Color(243, 241, 243));			//星期顏色
       	 	for(int j=0;j<42;j++){
       	 		if(j>=dayOne && j<dateCount+dayOne){
       	 			i=j-dayOne;
       	 			dayLabel[i].setBackground(new Color(165, 142, 135));	//單天日期背景
       	 			iconLabel[i].setBackground(new Color(243, 241, 243));	//單天記事背景
       	 			}
       	 		else
       	 		dayJPanel[j].setBackground(new Color(243, 241, 243));
       	 	}
       	 	paletteLabel.setBackground(new Color(243, 241, 243));
       	 	closeLabel.setBackground(new Color(243, 241, 243));
       	 	jPanel3.setBackground(new Color(240, 121, 149));
       	 	jPanel4.setBackground(new Color(240, 121, 149));
    	}break;
    	
    	case 1:{
    		jPanel1.setBackground(new Color(255, 255, 255));		//日期背景
       	 	jPanel2.setBackground(new Color(45, 48, 72));			//標題背景
            weekLabel.setForeground(new Color(198, 227, 240));		//星期顏色
       	 	label1.setForeground(new Color(0, 174, 239));			//月份顏色
       	 	for(int j=0;j<42;j++){
       	 		if(j>=dayOne && j<dateCount+dayOne){
       	 			i=j-dayOne;
       	 			dayLabel[i].setBackground(new Color(0, 174, 239));	//單天日期背景
       	 			iconLabel[i].setBackground(new Color(255, 255, 255));	//單天記事背景
       	 			}
       	 		else
       	 		dayJPanel[j].setBackground(new Color(255, 255, 255));
       	 	}
       	 	paletteLabel.setBackground(new Color(255, 255, 255));
       	 	closeLabel.setBackground(new Color(255, 255, 255));
       	 	jPanel3.setBackground(new Color(45, 48, 72));
       	 	jPanel4.setBackground(new Color(45, 48, 72));
    	}break;
       	 	
    	case 2:{
    		jPanel1.setBackground(new Color(248, 253, 250));		//日期背景
       	 	jPanel2.setBackground(new Color(45, 164, 96));			//標題背景
            weekLabel.setForeground(new Color(125, 221, 163));		//星期顏色
       	 	label1.setForeground(new Color(248, 253, 250));			//月份顏色
       	 	for(int j=0;j<42;j++){
       	 		if(j>=dayOne && j<dateCount+dayOne){
       	 			i=j-dayOne;
       	 			dayLabel[i].setBackground(new Color(42, 103, 69));	//單天日期背景
       	 			iconLabel[i].setBackground(new Color(248, 253, 250));	//單天記事背景
       	 			}
       	 		else
       	 		dayJPanel[j].setBackground(new Color(248, 253, 250));
       	 	}
       	 	paletteLabel.setBackground(new Color(248, 253, 250));
       	 	closeLabel.setBackground(new Color(248, 253, 250));
      	 	jPanel3.setBackground(new Color(45, 164, 96));
      	 	jPanel4.setBackground(new Color(45, 164, 96));
    	}break;
    	
    	case 3:{
    		jPanel1.setBackground(new Color(255, 255, 255));		//日期背景
       	 	jPanel2.setBackground(new Color(28, 60, 82));			//標題背景
            weekLabel.setForeground(new Color(59, 196, 199));		//星期顏色
       	 	label1.setForeground(new Color(251, 170, 153));			//月份顏色
       	 	for(int j=0;j<42;j++){
       	 		if(j>=dayOne && j<dateCount+dayOne){
       	 			i=j-dayOne;
       	 			dayLabel[i].setBackground(new Color(115, 149, 154));	//單天日期背景
       	 			iconLabel[i].setBackground(new Color(255, 255, 255));	//單天記事背景
       	 			}
       	 		else
       	 		dayJPanel[j].setBackground(new Color(255, 255, 255));
       	 	}
       	 	paletteLabel.setBackground(new Color(255, 255, 255));
       	 	closeLabel.setBackground(new Color(255, 255, 255));
       	 	jPanel3.setBackground(new Color(28, 60, 82));
       	 	jPanel4.setBackground(new Color(28, 60, 82));
    	}break;
    	
    	case 4:{
    		jPanel1.setBackground(new Color(220, 220, 220));		//日期背景
       	 	jPanel2.setBackground(new Color(0, 0, 0));				//標題背景
            weekLabel.setForeground(new Color(251, 251, 251));		//星期顏色
       	 	label1.setForeground(new Color(165, 165, 165));			//月份顏色
       	 	for(int j=0;j<42;j++){
       	 		if(j>=dayOne && j<dateCount+dayOne){
       	 			i=j-dayOne;
       	 			dayLabel[i].setBackground(new Color(118, 118, 118));	//單天日期背景
       	 			iconLabel[i].setBackground(new Color(220, 220, 220));	//單天記事背景
       	 			}
       	 		else
       	 		dayJPanel[j].setBackground(new Color(220, 220, 220));
       	 	}
       	 	paletteLabel.setBackground(new Color(220, 220, 220));
       	 	closeLabel.setBackground(new Color(220, 220, 220));
      	 	jPanel3.setBackground(new Color(0, 0, 0));
      	 	jPanel4.setBackground(new Color(0, 0, 0));
    	}break;
    	
    	}
    	
    }     
    
    public void changeColor(){
    	if(nowcolor==4)
    		nowcolor=0;
    	else
    		nowcolor++;
    		
    	prefs.putInt(ID, nowcolor);
    	setColor();
    }     
    
	public void run() {
    	prefs = Preferences.userRoot().node(this.getClass().getName());
    	nowcolor = prefs.getInt(ID, 0);
		initGUI();
		createDateButton();
   	 	add(jPanel2, BorderLayout.NORTH);
		initFrame();
		setLocation(20, 20);
	}
}
