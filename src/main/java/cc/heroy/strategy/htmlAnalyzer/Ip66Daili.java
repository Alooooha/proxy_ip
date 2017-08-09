package cc.heroy.strategy.htmlAnalyzer;

import java.util.concurrent.CopyOnWriteArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cc.heroy.bean.IP;

public class Ip66Daili implements BaseHtmlAnalyzer{

	@Override
	public void analyzer(String html, CopyOnWriteArrayList<IP> IPs) {
		Document doc = Jsoup.parse(html);
		Elements elements = doc.select("table tr:gt(1)");

        for (Element element : elements){
            String host = element.select("td:eq(0)").first().text();
            String port  = element.select("td:eq(1)").first().text();
            IP ip = new IP(host,port);
			IPs.add(ip);
        }
	}
	
	
}
