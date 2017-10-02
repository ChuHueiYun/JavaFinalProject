package weatherForecast;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class RSSReader {
	final static private String[] RSSURL = {"http://www.cwb.gov.tw/rss/forecast/36_03.xml",		//基隆市
											"http://www.cwb.gov.tw/rss/forecast/36_01.xml",		//臺北市
											"http://www.cwb.gov.tw/rss/forecast/36_04.xml",		//新北市
											"http://www.cwb.gov.tw/rss/forecast/36_05.xml",		//桃園市
											"http://www.cwb.gov.tw/rss/forecast/36_14.xml",		//新竹市
											"http://www.cwb.gov.tw/rss/forecast/36_06.xml",		//新竹縣
											"http://www.cwb.gov.tw/rss/forecast/36_07.xml",		//苗栗縣
											"http://www.cwb.gov.tw/rss/forecast/36_08.xml",		//臺中市
											"http://www.cwb.gov.tw/rss/forecast/36_09.xml",		//彰化縣
											"http://www.cwb.gov.tw/rss/forecast/36_10.xml",		//南投縣
											"http://www.cwb.gov.tw/rss/forecast/36_11.xml",		//雲林縣
											"http://www.cwb.gov.tw/rss/forecast/36_16.xml",		//嘉義市
											"http://www.cwb.gov.tw/rss/forecast/36_12.xml",		//嘉義縣
											"http://www.cwb.gov.tw/rss/forecast/36_13.xml",		//臺南市
											"http://www.cwb.gov.tw/rss/forecast/36_02.xml",		//高雄市
											"http://www.cwb.gov.tw/rss/forecast/36_15.xml",		//屏東縣
											"http://www.cwb.gov.tw/rss/forecast/36_17.xml",		//宜蘭縣
											"http://www.cwb.gov.tw/rss/forecast/36_18.xml",		//花蓮縣
											"http://www.cwb.gov.tw/rss/forecast/36_19.xml",		//臺東縣
											"http://www.cwb.gov.tw/rss/forecast/36_22.xml",		//連江縣
											"http://www.cwb.gov.tw/rss/forecast/36_21.xml",		//金門縣
											"http://www.cwb.gov.tw/rss/forecast/36_20.xml"};	//澎湖縣
	public String[][] getWeatherInfo(int index) {
		Document doc = null;
		try {
			doc = Jsoup.connect(RSSURL[index]).get();
		}
		catch(IOException e) {
			System.out.println(e);
			System.out.println("RSSReader Exception!");
			return null;
		}
		Elements items = doc.select("item");
		
		String[][] weatherInfo = new String[17][5];
		String[] parseTitle = items.select("title").text().split(" ");
		String[] parseFirstDescription = items.select("description").first().text().split(" ");
		String[] parseSecondDescription = items.select("description").get(1).text().split("<BR>");
		String[][] parseSecondDescriptionTwice = new String[14][];
		for(int i=0; i<parseSecondDescription.length; i++) {
			parseSecondDescriptionTwice[i] = parseSecondDescription[i].split(" ");
		}
		
		weatherInfo[0][0] = parseTitle[1];					//今日白天or今晚至明晨
		weatherInfo[0][1] = parseTitle[2];					//天氣描述(EX: 陰短暫雨)
		weatherInfo[0][2] = parseTitle[4];					//溫度(低)
		weatherInfo[0][3] = parseTitle[6];					//溫度(高)
		weatherInfo[0][4] = parseTitle[8];					//降雨機率(%)
		weatherInfo[1][0] = parseFirstDescription[0];		//今晚至明晨or明日白天
		weatherInfo[1][1] = parseFirstDescription[1];		//天氣描述(EX: 陰短暫雨)
		weatherInfo[1][2] = parseFirstDescription[3];		//溫度(低)
		weatherInfo[1][3] = parseFirstDescription[5];		//溫度(高)
		weatherInfo[1][4] = parseFirstDescription[7];		//降雨機率(%)
		weatherInfo[2][0] = parseFirstDescription[9];		//明日白天or明晚後天
		weatherInfo[2][1] = parseFirstDescription[10];		//天氣描述(EX: 陰短暫雨)
		weatherInfo[2][2] = parseFirstDescription[12];		//溫度(低)
		weatherInfo[2][3] = parseFirstDescription[14];		//溫度(高)
		weatherInfo[2][4] = parseFirstDescription[16];		//降雨機率(%)
		//一週
		weatherInfo[3][0] = parseSecondDescriptionTwice[0][0];						//日期
		weatherInfo[3][1] = parseSecondDescriptionTwice[0][1];						//白天or晚上
		weatherInfo[3][2] = parseSecondDescriptionTwice[0][2].substring(3);			//溫度(低)
		weatherInfo[3][3] = parseSecondDescriptionTwice[0][4];						//溫度(高)
		weatherInfo[3][4] = parseSecondDescriptionTwice[0][5];						//天氣描述(EX: 陰短暫雨)
		for(int i=4; i<weatherInfo.length; i++) {
			weatherInfo[i][0] = parseSecondDescriptionTwice[i-3][1];				//日期
			weatherInfo[i][1] = parseSecondDescriptionTwice[i-3][2];				//白天or晚上
			weatherInfo[i][2] = parseSecondDescriptionTwice[i-3][3].substring(3);	//溫度(低)
			weatherInfo[i][3] = parseSecondDescriptionTwice[i-3][5];				//溫度(高)
			weatherInfo[i][4] = parseSecondDescriptionTwice[i-3][6];				//天氣描述(EX: 陰短暫雨)
		}
		/*for(String s[] : weatherInfo) {
			System.out.println("");
			for(String t : s) System.out.print(t + " ");
		}*/
		return weatherInfo;
	}
	/*public static void main(String[] args) {
		RSSReader rss = new RSSReader();
		rss.getWeatherInfo(0);
	}*/
}