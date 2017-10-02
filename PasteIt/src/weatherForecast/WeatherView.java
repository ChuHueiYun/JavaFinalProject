package weatherForecast;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import utility.ComponentMover;

public class WeatherView extends JInternalFrame {
	private static final long serialVersionUID = 1L;
	//data
	final static public String[] city = {"基隆市", "臺北市", "新北市", "桃園市", "新竹市", "新竹縣", "苗栗縣", "臺中市", "彰化縣", "南投縣", "雲林縣", "嘉義市", "嘉義縣", "臺南市", "高雄市", "屏東縣", "宜蘭縣", "花蓮縣", "臺東縣", "連江縣", "金門縣", "澎湖縣"};
	private WeatherInfo weatherInfo;
    private String currentCity;
    private int currentCityIndex;
    private boolean displayWeeklyWeatherInfo;
    private boolean isConnect;
	//GUI components
    private GridBagConstraints gridBagConstraints;
    private ArrayList<ImageIcon> imageIcon;		//0: 背景, 1: 設定, 2~7: 天氣圖(大), 8~13: 天氣圖(小), 14: 溫度計, 15: 雨傘
	private JPanel panel;
    private JButton watchWeeklyWeatherButton;
    private JButton hideWeeklyWeatherButton;
    private JPanel weeklyWeather;
    //temp
    private boolean isFirstInitWatchWeeklyWeatherButton = true;
    
	public void readImage() {
		this.imageIcon = new ArrayList<ImageIcon>();
		@SuppressWarnings("unused")
		File file = null;
		URL imagePath = null;
		try {
			file = new File(this.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
		} catch (URISyntaxException e) {
			e.printStackTrace();
			//未完成
		}
		//0: 背景
		imagePath = WeatherView.class.getResource("/images/background.jpg");
		this.imageIcon.add(new ImageIcon(imagePath));
		//1: 設定
		imagePath = WeatherView.class.getResource("/images/gear.png");
		this.imageIcon.add(new ImageIcon(imagePath));
		//2~7: 天氣圖(大)
		imagePath = WeatherView.class.getResource("/images/sun.png");
		this.imageIcon.add(new ImageIcon(imagePath));
		imagePath = WeatherView.class.getResource("/images/cloudWithSun.png");
		this.imageIcon.add(new ImageIcon(imagePath));
		imagePath = WeatherView.class.getResource("/images/moon.png");
		this.imageIcon.add(new ImageIcon(imagePath));
		imagePath = WeatherView.class.getResource("/images/cloudWithMoon.png");
		this.imageIcon.add(new ImageIcon(imagePath));
		imagePath = WeatherView.class.getResource("/images/cloud.png");
		this.imageIcon.add(new ImageIcon(imagePath));
		imagePath = WeatherView.class.getResource("/images/rain.png");
		this.imageIcon.add(new ImageIcon(imagePath));
		//8~13: 天氣圖(小)
		imagePath = WeatherView.class.getResource("/images/sunSmall.png");
		this.imageIcon.add(new ImageIcon(imagePath));
		imagePath = WeatherView.class.getResource("/images/cloudWithSunSmall.png");
		this.imageIcon.add(new ImageIcon(imagePath));
		imagePath = WeatherView.class.getResource("/images/moonSmall.png");
		this.imageIcon.add(new ImageIcon(imagePath));
		imagePath = WeatherView.class.getResource("/images/cloudWithMoonSmall.png");
		this.imageIcon.add(new ImageIcon(imagePath));
		imagePath = WeatherView.class.getResource("/images/cloudSmall.png");
		this.imageIcon.add(new ImageIcon(imagePath));
		imagePath = WeatherView.class.getResource("/images/rainSmall.png");
		this.imageIcon.add(new ImageIcon(imagePath));
		//14: 溫度
		imagePath = WeatherView.class.getResource("/images/thermometer.png");
		this.imageIcon.add(new ImageIcon(imagePath));
		//15: 雨傘
		imagePath = WeatherView.class.getResource("/images/umbrella.png");
		this.imageIcon.add(new ImageIcon(imagePath));
	}
    public void initFrame() {
		this.setSize(550, 400);
    	this.setResizable(false);
    	this.setClosable(false);
    	this.setMaximizable(false);
    	this.setIconifiable(false);
    	this.setBorder(null);
    	BasicInternalFrameUI basicInternalFrameUI = (BasicInternalFrameUI)this.getUI();
    	basicInternalFrameUI.setNorthPane(null);
		this.setVisible(true);
    }
    public void initData() {
    	this.currentCity = city[0];		//預設: 基隆市
		this.currentCityIndex = 0;
    	this.loadFile();
		this.displayWeeklyWeatherInfo = false;
		this.weatherInfo = new WeatherInfo(this.currentCityIndex);
		this.weatherInfo.weatherFetcher();
		this.isConnect = this.weatherInfo.isConnect();
    }
    public void initBackground() {
		((JPanel)this.getContentPane()).setOpaque(false);
		JLabel background = new JLabel(this.imageIcon.get(0));
		this.getLayeredPane().add(background, new Integer(Integer.MIN_VALUE));
		background.setBounds(0, 0, this.imageIcon.get(0).getIconWidth(), this.imageIcon.get(0).getIconHeight());
    }
    public void initPanel() {
		Container container = this.getContentPane();
		this.panel = new JPanel();
		this.panel.setOpaque(false);
		this.panel.setLayout(new GridBagLayout());
		this.gridBagConstraints = new GridBagConstraints();
		if(!this.isConnect) {
			container.add(this.panel);
			this.noConnect();
			return;
		}
		this.initMenu();
		container.add(this.panel);
    }
    public void initMenu() {
    	JMenuBar menuBar = new JMenuBar();
    	menuBar.setOpaque(false);
    	JMenu menu = new JMenu();
    	menu.setOpaque(false);
    	menu.setIcon(this.imageIcon.get(1));
    	JMenu cityMenu = new JMenu("位置");
    	cityMenu.setFont(new Font("微軟正黑體", Font.BOLD, 14));
    	ArrayList<JMenuItem> cityMenuItem = new ArrayList<JMenuItem>();
    	for(int i=0; i<city.length; i++) {
    		cityMenuItem.add(new JMenuItem(city[i]));
    		cityMenuItem.get(i).setFont(new Font("微軟正黑體", Font.BOLD, 14));
	    	final int cityIndex = i;
	    	cityMenuItem.get(i).addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	        		currentCity = city[cityIndex];
	        		currentCityIndex = cityIndex;
	        		weatherInfo.cityChange(cityIndex);
	        		refresh();
	        		saveFile();
	            }
	        });
	    	cityMenu.add(cityMenuItem.get(i));
    	}
    	JMenuItem refresh = new JMenuItem("重新整理");
    	refresh.setFont(new Font("微軟正黑體", Font.BOLD, 14));
    	refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        		refresh();
            }
        });
    	JMenuItem close = new JMenuItem("關閉");
    	close.setFont(new Font("微軟正黑體", Font.BOLD, 14));
    	close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	setVisible(false);
            	saveFile();
            }
        });
    	menu.add(cityMenu);
    	menu.add(refresh);
    	menu.add(close);
		menuBar.add(menu);
		this.gridBagConstraints.gridx = 0;
		this.gridBagConstraints.gridy = 0;
		this.gridBagConstraints.gridwidth = 19;
		this.gridBagConstraints.gridheight = 1;
		this.gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		this.gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
		this.panel.add(menuBar, this.gridBagConstraints);
    }
    public void initWeeklyWeatherButton() {
    	if(!this.displayWeeklyWeatherInfo) {
    		this.watchWeeklyWeatherButton = new JButton("查看一週天氣預報");
    		this.watchWeeklyWeatherButton.setFont(new Font("微軟正黑體", Font.BOLD, 14));
    		this.watchWeeklyWeatherButton.setOpaque(false);
    		this.watchWeeklyWeatherButton.setForeground(Color.WHITE);
    		this.watchWeeklyWeatherButton.setContentAreaFilled(false);
    		this.watchWeeklyWeatherButton.setBorderPainted(false);
    		this.watchWeeklyWeatherButton.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	setSize(550, 600);
	            	displayWeeklyWeatherInfo = true;
	            	initWeeklyWeatherButton();
	            	initWeeklyWeatherInfo();
	            }
	        });
    		if(this.isFirstInitWatchWeeklyWeatherButton) this.isFirstInitWatchWeeklyWeatherButton = false;
    		else {		//似乎不會進來
    			this.panel.remove(this.hideWeeklyWeatherButton);
    			this.panel.revalidate();
    			this.panel.repaint();
    			this.weeklyWeather.removeAll();
    			this.weeklyWeather.revalidate();
    			this.weeklyWeather.repaint();
    			this.panel.remove(this.weeklyWeather);
    			this.panel.revalidate();
    			this.panel.repaint();
    		}
    		this.gridBagConstraints.gridx = 0;
    		this.gridBagConstraints.gridy = 12;
    		this.gridBagConstraints.gridwidth = 20;
    		this.gridBagConstraints.gridheight = 2;
    		this.gridBagConstraints.anchor = GridBagConstraints.PAGE_END;
    		this.panel.add(this.watchWeeklyWeatherButton, this.gridBagConstraints);
			this.panel.revalidate();
			this.panel.repaint();
    	}
    	else if(this.displayWeeklyWeatherInfo) {
			this.hideWeeklyWeatherButton = new JButton("隱藏一週天氣預報");
	    	this.hideWeeklyWeatherButton.setFont(new Font("微軟正黑體", Font.BOLD, 14));
			this.hideWeeklyWeatherButton.setOpaque(false);
			this.hideWeeklyWeatherButton.setForeground(Color.WHITE);
			this.hideWeeklyWeatherButton.setContentAreaFilled(false);
			this.hideWeeklyWeatherButton.setBorderPainted(false);
	    	this.hideWeeklyWeatherButton.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	            	setSize(550, 400);
	            	displayWeeklyWeatherInfo = false;
	    			panel.remove(hideWeeklyWeatherButton);
	    			panel.revalidate();
	    			panel.repaint();
	    			weeklyWeather.removeAll();
	    			weeklyWeather.revalidate();
	    			weeklyWeather.repaint();
	    			panel.remove(weeklyWeather);
	    			panel.revalidate();
	    			panel.repaint();
	            	initWeeklyWeatherButton();
	            }
	        });
			this.panel.remove(this.watchWeeklyWeatherButton);
			this.panel.revalidate();
			this.panel.repaint();
    		this.gridBagConstraints.gridx = 0;
    		this.gridBagConstraints.gridy = 36;
    		this.gridBagConstraints.gridwidth = 20;
    		this.gridBagConstraints.gridheight = 2;
    		this.gridBagConstraints.anchor = GridBagConstraints.PAGE_END;
			this.panel.add(this.hideWeeklyWeatherButton, this.gridBagConstraints);
			this.panel.revalidate();
			this.panel.repaint();
    	}
    }
    public void initCurrentWeatherInfo() {
    	String[] currentWeatherInfo = this.weatherInfo.getCurrentWeatherInfo();
    	
    	JLabel currentWeatherImage = new JLabel();
    	currentWeatherImage.setIcon(this.stringToImage(currentWeatherInfo[5], true));
		this.gridBagConstraints.gridx = 0;
		this.gridBagConstraints.gridy = 1;
		this.gridBagConstraints.gridwidth = 4;
		this.gridBagConstraints.gridheight = 4;
		this.gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		this.panel.add(currentWeatherImage, this.gridBagConstraints);
		
		JLabel currentWeatherStatus = new JLabel(currentWeatherInfo[1]);
		currentWeatherStatus.setFont(new Font("微軟正黑體", Font.BOLD, 30));
		currentWeatherStatus.setForeground(Color.WHITE);
		this.gridBagConstraints.gridx = 4;
		this.gridBagConstraints.gridy = 1;
		this.gridBagConstraints.gridwidth = 5;
		this.gridBagConstraints.gridheight = 2;
		this.gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		this.panel.add(currentWeatherStatus, this.gridBagConstraints);
    	
		JLabel currentWeatherTemperature = new JLabel(currentWeatherInfo[2] + "~" + currentWeatherInfo[3]);
		currentWeatherTemperature.setFont(new Font("微軟正黑體", Font.BOLD, 70));
		currentWeatherTemperature.setForeground(Color.WHITE);
		currentWeatherTemperature.setIcon(this.imageIcon.get(14));
		this.gridBagConstraints.gridx = 6;
		this.gridBagConstraints.gridy = 1;
		this.gridBagConstraints.gridwidth = 6;
		this.gridBagConstraints.gridheight = 4;
		this.gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		this.panel.add(currentWeatherTemperature, this.gridBagConstraints);
		
		JLabel currentWeatherCity = new JLabel(this.currentCity);
		currentWeatherCity.setFont(new Font("微軟正黑體", Font.BOLD, 30));
		currentWeatherCity.setForeground(Color.WHITE);
		this.gridBagConstraints.gridx = 2;
		this.gridBagConstraints.gridy = 4;
		this.gridBagConstraints.gridwidth = 4;
		this.gridBagConstraints.gridheight = 3;
		this.gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		this.panel.add(currentWeatherCity, this.gridBagConstraints);
    	
		JLabel currentWeatherRainfallProbability = new JLabel(currentWeatherInfo[4]);
		currentWeatherRainfallProbability.setFont(new Font("微軟正黑體", Font.BOLD, 40));
		currentWeatherRainfallProbability.setForeground(Color.WHITE);
		currentWeatherRainfallProbability.setIcon(this.imageIcon.get(15));
		this.gridBagConstraints.gridx = 4;
		this.gridBagConstraints.gridy = 5;
		this.gridBagConstraints.gridwidth = 5;
		this.gridBagConstraints.gridheight = 3;
		this.gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		this.panel.add(currentWeatherRainfallProbability, this.gridBagConstraints);
    }
    public void initRecentWeatherInfo() {		//second, third
    	//second
		String[] secondWeatherInfo = this.weatherInfo.getSecondWeatherInfo();
		
		JLabel secondWeatherTime = new JLabel(secondWeatherInfo[0]);
		secondWeatherTime.setFont(new Font("微軟正黑體", Font.BOLD, 25));
		secondWeatherTime.setForeground(Color.WHITE);
		this.gridBagConstraints.gridx = 0;
		this.gridBagConstraints.gridy = 8;
		this.gridBagConstraints.gridwidth = 5;
		this.gridBagConstraints.gridheight = 2;
		this.gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		this.panel.add(secondWeatherTime, this.gridBagConstraints);
    	
		JLabel secondWeatherStatus = new JLabel(secondWeatherInfo[1]);
		secondWeatherStatus.setFont(new Font("微軟正黑體", Font.BOLD, 25));
		secondWeatherStatus.setForeground(Color.WHITE);
		this.gridBagConstraints.gridx = 5;
		this.gridBagConstraints.gridy = 9;
		this.gridBagConstraints.gridwidth = 3;
		this.gridBagConstraints.gridheight = 1;
		this.gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		this.panel.add(secondWeatherStatus, this.gridBagConstraints);
		
		JLabel secondWeatherTemperature = new JLabel(secondWeatherInfo[2] + "~" + secondWeatherInfo[3]);
		secondWeatherTemperature.setFont(new Font("微軟正黑體", Font.BOLD, 25));
		secondWeatherTemperature.setForeground(Color.WHITE);
		secondWeatherTemperature.setIcon(this.stringToImage(secondWeatherInfo[5], false));		//secondWeatherImage
		this.gridBagConstraints.gridx = 8;
		this.gridBagConstraints.gridy = 8;
		this.gridBagConstraints.gridwidth = 3;
		this.gridBagConstraints.gridheight = 2;
		this.gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		this.panel.add(secondWeatherTemperature, this.gridBagConstraints);
		
		JLabel secondWeatherRainfallProbability = new JLabel(secondWeatherInfo[4]);
		secondWeatherRainfallProbability.setFont(new Font("微軟正黑體", Font.BOLD, 25));
		secondWeatherRainfallProbability.setForeground(Color.WHITE);
		this.gridBagConstraints.gridx = 11;
		this.gridBagConstraints.gridy = 8;
		this.gridBagConstraints.gridwidth = 3;
		this.gridBagConstraints.gridheight = 2;
		this.gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		this.panel.add(secondWeatherRainfallProbability, this.gridBagConstraints);
		
		//third
		String[] thirdWeatherInfo = this.weatherInfo.getThirdWeatherInfo();
		
		JLabel thirdWeatherTime = new JLabel(thirdWeatherInfo[0]);
		thirdWeatherTime.setFont(new Font("微軟正黑體", Font.BOLD, 25));
		thirdWeatherTime.setForeground(Color.WHITE);
		this.gridBagConstraints.gridx = 0;
		this.gridBagConstraints.gridy = 10;
		this.gridBagConstraints.gridwidth = 5;
		this.gridBagConstraints.gridheight = 2;
		this.gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		this.panel.add(thirdWeatherTime, this.gridBagConstraints);
    	
		JLabel thirdWeatherStatus = new JLabel(thirdWeatherInfo[1]);
		thirdWeatherStatus.setFont(new Font("微軟正黑體", Font.BOLD, 25));
		thirdWeatherStatus.setForeground(Color.WHITE);
		this.gridBagConstraints.gridx = 5;
		this.gridBagConstraints.gridy = 11;
		this.gridBagConstraints.gridwidth = 3;
		this.gridBagConstraints.gridheight = 1;
		this.gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		this.panel.add(thirdWeatherStatus, this.gridBagConstraints);
    	
		JLabel thirdWeatherTemperature = new JLabel(thirdWeatherInfo[2] + "~" + thirdWeatherInfo[3]);
		thirdWeatherTemperature.setFont(new Font("微軟正黑體", Font.BOLD, 25));
		thirdWeatherTemperature.setForeground(Color.WHITE);
		thirdWeatherTemperature.setIcon(this.stringToImage(thirdWeatherInfo[5], false));		//thirdWeatherImage
		this.gridBagConstraints.gridx = 8;
		this.gridBagConstraints.gridy = 10;
		this.gridBagConstraints.gridwidth = 3;
		this.gridBagConstraints.gridheight = 2;
		this.gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		this.panel.add(thirdWeatherTemperature, this.gridBagConstraints);
    	
		JLabel thirdWeatherRainfallProbability = new JLabel(thirdWeatherInfo[4]);
		thirdWeatherRainfallProbability.setFont(new Font("微軟正黑體", Font.BOLD, 25));
		thirdWeatherRainfallProbability.setForeground(Color.WHITE);
		this.gridBagConstraints.gridx = 11;
		this.gridBagConstraints.gridy = 10;
		this.gridBagConstraints.gridwidth = 3;
		this.gridBagConstraints.gridheight = 2;
		this.gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		this.panel.add(thirdWeatherRainfallProbability, this.gridBagConstraints);
    }
    public void initWeeklyWeatherInfo() {
    	String[][] weeklyWeatherInfo = this.weatherInfo.getWeeklyWeatherInfo();
    	
    	this.weeklyWeather = new JPanel();
    	this.weeklyWeather.setLayout(new GridLayout(5, 8, 3, 3));
		this.weeklyWeather.setOpaque(false);
    	
    	JLabel date = new JLabel("日期");
    	date.setFont(new Font("微軟正黑體", Font.BOLD, 15));
    	date.setForeground(Color.WHITE);
		this.weeklyWeather.add(date);
    	for(int i=0; i<7; i++) {
    		date = new JLabel(weeklyWeatherInfo[i * 2][0]);
        	date.setFont(new Font("微軟正黑體", Font.BOLD, 20));
        	date.setForeground(Color.WHITE);
    		this.weeklyWeather.add(date);
    	}
    	JLabel day = new JLabel("白天");
		day.setFont(new Font("微軟正黑體", Font.BOLD, 15));
		day.setForeground(Color.WHITE);
		this.weeklyWeather.add(day);
    	for(int i=0; i<7; i++) {
    		day = new JLabel(this.stringToImage(weeklyWeatherInfo[i * 2][5], false));
    		this.weeklyWeather.add(day);
    	}
    	day = new JLabel("");
		day.setFont(new Font("微軟正黑體", Font.BOLD, 20));
		day.setForeground(Color.WHITE);
		this.weeklyWeather.add(day);
    	for(int i=0; i<7; i++) {
    		day = new JLabel(weeklyWeatherInfo[i * 2][2] + "~" + weeklyWeatherInfo[i * 2][3]);
    		day.setFont(new Font("微軟正黑體", Font.BOLD, 20));
    		day.setForeground(Color.WHITE);
    		this.weeklyWeather.add(day);
    	}
    	JLabel night = new JLabel("晚上");
		night.setFont(new Font("微軟正黑體", Font.BOLD, 15));
		night.setForeground(Color.WHITE);
		this.weeklyWeather.add(night);
		for(int i=0; i<7; i++) {
			night = new JLabel(this.stringToImage(weeklyWeatherInfo[i * 2 + 1][5], false));
    		this.weeklyWeather.add(night);
    	}
		night = new JLabel("");
		night.setFont(new Font("微軟正黑體", Font.BOLD, 20));
		night.setForeground(Color.WHITE);
		this.weeklyWeather.add(night);
		for(int i=0; i<7; i++) {
			night = new JLabel(weeklyWeatherInfo[i * 2 + 1][2] + "~" + weeklyWeatherInfo[i * 2 + 1][3]);
			night.setFont(new Font("微軟正黑體", Font.BOLD, 20));
			night.setForeground(Color.WHITE);
    		this.weeklyWeather.add(night);
    	}
		this.gridBagConstraints.gridx = 0;
		this.gridBagConstraints.gridy = 14;
		this.gridBagConstraints.gridwidth = 20;
		this.gridBagConstraints.gridheight = 20;
		this.gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		this.panel.add(this.weeklyWeather, this.gridBagConstraints);
    }
    public void refresh() {
    	this.weatherInfo.cityChange(this.currentCityIndex);
    	this.panel.removeAll();
		this.weatherInfo.weatherFetcher();
		this.isConnect = this.weatherInfo.isConnect();
		if(!this.isConnect) {
			this.noConnect();
			return;
		}
    	this.initMenu();
    	this.isFirstInitWatchWeeklyWeatherButton = true;
    	this.initWeeklyWeatherButton();
		this.initCurrentWeatherInfo();
		this.initRecentWeatherInfo();
		if(this.displayWeeklyWeatherInfo) this.initWeeklyWeatherInfo();
		this.panel.revalidate();
		this.panel.repaint();
    }
    public ImageIcon stringToImage(String number, boolean isLarge) {
	    if(isLarge) return this.imageIcon.get(Integer.valueOf(number) + 2);
	    else return this.imageIcon.get(Integer.valueOf(number) + 8);
    }
    public void noConnect() {
    	this.panel.removeAll();
		JLabel noConnect = new JLabel("連線失敗");
		noConnect.setFont(new Font("微軟正黑體", Font.BOLD, 50));
		noConnect.setForeground(Color.WHITE);
		this.gridBagConstraints.gridx = 0;
		this.gridBagConstraints.gridy = 0;
		this.gridBagConstraints.gridwidth = 19;
		this.gridBagConstraints.gridheight = 1;
		this.gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		this.gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
		this.panel.add(noConnect, this.gridBagConstraints);
		
    	JMenuBar menuBar = new JMenuBar();
    	menuBar.setOpaque(false);
    	JMenu menu = new JMenu();
    	menu.setOpaque(false);
    	menu.setIcon(this.imageIcon.get(1));
    	JMenuItem refresh = new JMenuItem("重新整理");
    	refresh.setFont(new Font("微軟正黑體", Font.BOLD, 14));
    	refresh.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
        		refresh();
            }
        });
    	JMenuItem displayOnDesktop = new JMenuItem("顯示桌面");
    	displayOnDesktop.setFont(new Font("微軟正黑體", Font.BOLD, 14));
    	displayOnDesktop.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	//do not finish
            }
        });
    	JMenuItem close = new JMenuItem("關閉");
    	close.setFont(new Font("微軟正黑體", Font.BOLD, 14));
    	close.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	setVisible(false);
            }
        });
    	menu.add(refresh);
    	menu.add(displayOnDesktop);
    	menu.add(close);
		menuBar.add(menu);
		this.gridBagConstraints.gridx = 19;
		this.gridBagConstraints.gridy = 0;
		this.gridBagConstraints.gridwidth = 19;
		this.gridBagConstraints.gridheight = 1;
		this.gridBagConstraints.insets = new Insets(5, 5, 5, 5);
		this.gridBagConstraints.anchor = GridBagConstraints.NORTHEAST;
		this.panel.add(menuBar, this.gridBagConstraints);
		this.panel.revalidate();
		this.panel.repaint();
    }
    static private String readFile(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return encoding.decode(ByteBuffer.wrap(encoded)).toString();
	}
    public void loadFile() {
    	File folder = new File(System.getProperty("user.home") + "/Desktop/PasteIt/WeatherForecast");
		folder.mkdirs();
		File[] file = folder.listFiles();

		for(File f : file) {
			if(f.isFile()) {
				try {
					//parseFile(readFile(f.getPath(), Charset.forName("MS950")));
					parseFile(readFile(f.getPath(), Charset.forName("UTF-8")));
				}
				catch(IOException e) {
					JOptionPane.showMessageDialog(null, e.getMessage(), "Load File Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}
    public void parseFile(String read) {
    	try {
    		String[] temp = read.split("\n", 2);
	    	this.currentCity = temp[0];
			this.currentCityIndex = Integer.valueOf(temp[1]);
		}
		catch(Exception e) {
			this.currentCity = city[0];		//預設: 基隆市
			this.currentCityIndex = 0;
		}
    }
	public void saveFile() {
		File folder = new File(System.getProperty("user.home") + "/Desktop/PasteIt/WeatherForecast");
		folder.mkdirs();
		File[] file = folder.listFiles();
		//刪除原本的檔案
		for(File f : file) {
			if(f.isFile()) f.delete();
		}
		//儲存
		try {
			PrintWriter printWriter = new PrintWriter(folder.getPath() + '/' + "WeatherForecastCity"+ ".txt");
			printWriter.write(this.currentCity + "\n" + this.currentCityIndex);
			printWriter.close();
		}
		catch(FileNotFoundException e) {
			JOptionPane.showMessageDialog(null, e.getMessage(), "Save File Error", JOptionPane.ERROR_MESSAGE);
		}
	}
	public void run() {
		java.awt.Dimension scr_size = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		this.readImage();
		this.initData();
		this.initBackground();
		this.initPanel();
		if(this.isConnect) {
			this.initWeeklyWeatherButton();
			this.initCurrentWeatherInfo();
			this.initRecentWeatherInfo();
		}
		this.initFrame();
		setLocation(20,(scr_size.height - this.getHeight())/2+250);
		@SuppressWarnings("unused")
		ComponentMover componentMover = new ComponentMover(JInternalFrame.class, this.panel);
		Timer timer = new Timer();
		timer.schedule(new SubTimerTask(), 1000, 1000 * 60 * 60);
	}
	private class SubTimerTask extends TimerTask {
		@Override
		public void run() {
			refresh();
		}
	}
}