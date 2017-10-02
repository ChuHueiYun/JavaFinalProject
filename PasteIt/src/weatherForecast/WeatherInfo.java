package weatherForecast;

public class WeatherInfo {
	private String[][] currentWeatherInfo;		//今日白天, 今晚至明晨, 明日白天or今晚至明晨, 明日白天, 明晚後天
	//time, weatherStatus, lowerTemperature, higherTemperature, rainfallProbability, imageNumber
	private String[][] weekWeatherInfo;
	//date, time, dayLowerTemperature, dayHigherTemperature, weatherStatus, imageNumber
	//date, time, nightLowerTemperature, nightHigherTemperature, weatherStatus, imageNumber
	private int cityIndex;
	private boolean isConnect;
	
	public WeatherInfo() {
		this.cityIndex = 0;		//預設:基隆市
		this.isConnect = false;
	}
	public WeatherInfo(int cityIndex) {
		this.cityIndex = cityIndex;
		this.isConnect = false;
	}
	public void weatherFetcher() {
		RSSReader rss = new RSSReader();
		String[][] weatherInfo = rss.getWeatherInfo(this.cityIndex);
		if(weatherInfo == null) {
			this.isConnect = false;
			return;
		}
		else this.isConnect = true;
		this.currentWeatherInfo = new String[3][6];
		this.weekWeatherInfo = new String[weatherInfo.length-3][6];
		for(int i=0; i<3; i++) {
			for(int j=0; j<6; j++) {
				if(j == 5) this.currentWeatherInfo[i][j] = Integer.toString(statusToImage(weatherInfo[i][1], weatherInfo[i][0]));
				else if(j == 5) this.currentWeatherInfo[i][j] = Integer.toString(statusToImage(weatherInfo[i][1], weatherInfo[i][0]));
				else this.currentWeatherInfo[i][j] = weatherInfo[i][j];
			}
		}
		for(int i=3; i<weatherInfo.length; i++) {
			for(int j=0; j<6; j++) {
				if(j == 5 && i % 2 == 0) this.weekWeatherInfo[i-3][j] = Integer.toString(statusToImage(weatherInfo[i][4], weatherInfo[i][1]));
				else if(j == 5 && i % 2 == 1) this.weekWeatherInfo[i-3][j] = Integer.toString(statusToImage(weatherInfo[i][4], weatherInfo[i][1]));
				else this.weekWeatherInfo[i-3][j] = weatherInfo[i][j];
			}
		}
		/*for(String s[] : this.currentWeatherInfo) {
			System.out.println("");
			for(String t : s) System.out.print(t + " ");
		}
		for(String s[] : this.weekWeatherInfo) {
			System.out.println("");
			for(String t : s) System.out.print(t + " ");
		}*/
	}
	public int statusToImage(String status, String time) {
		//0: sun, 1: cloudWithSun, 2: moon, 3: cloudWithMoon, 4: cloud, 5: rain, 6: null
    	if(status.contains("雨")) return 5;
    	else if(status.contains("晴") && status.contains("雲") && time.contains("白天")) return 1;
    	else if(status.contains("晴") && status.contains("雲") && time.contains("晚")) return 3;
    	else if(status.contains("雲") || status.contains("陰")) return 4;
    	else if(status.contains("晴") && time.contains("白天")) return 0;
    	else if(status.contains("晴") && time.contains("晚")) return 2;
    	else return 6;
    }
	public void cityChange(int cityIndex) {
		this.cityIndex = cityIndex;
		this.weatherFetcher();
	}
	public int getCityIndex() {
		return this.cityIndex;
	}
	public String[] getCurrentWeatherInfo() {		//current
		if(!this.isConnect) return null;
		else return this.currentWeatherInfo[0];
	}
	public String[] getSecondWeatherInfo() {		//recent
		if(!this.isConnect) return null;
		else return this.currentWeatherInfo[1];
	}
	public String[] getThirdWeatherInfo() {		//recent
		if(!this.isConnect) return null;
		else return this.currentWeatherInfo[2];
	}
	public String[][] getWeeklyWeatherInfo() {
		if(!this.isConnect) return null;
		else return this.weekWeatherInfo;
	}
	public boolean isConnect() {
		return this.isConnect;
	}
	/*public static void main(String[] args) {
		WeatherInfo w = new WeatherInfo();
		if(!w.isConnect) System.out.println("fail to connect");
	}*/
}