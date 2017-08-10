package cc.heroy.strategy.htmlAnalyzer;

import java.util.concurrent.BlockingQueue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cc.heroy.bean.IP;

/**
 * 
 * @ClassName: NianshaoDaili
 * @Description: 年少代理免费页扫描
 * @author BeiwEi
 * @date 2017年8月9日 上午11:12:10
 *
 */
public class NianshaoDaili extends BaseHtmlAnalyzer {

	@Override
	public void analyzer(String html, BlockingQueue<IP> IPs) {
		Document doc = Jsoup.parse(html);
		Elements elements = doc.select("table tr:gt(1)");

		for (Element element : elements) {
			String host = element.select("td:eq(0)").first().text();
			String port = element.select("td:eq(1)").first().text();
			IP ip = new IP(host, port);
			try {
				IPs.put(ip);
			} catch (InterruptedException e) {
				logger.error(this.getClass().getSimpleName() + "  error");
				e.printStackTrace();
			}
		}
	}

}
