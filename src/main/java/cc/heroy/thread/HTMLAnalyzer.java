package cc.heroy.thread;

import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import cc.heroy.bean.IP;
import cc.heroy.strategy.htmlAnalyzer.BaseHtmlAnalyzer;

public class HTMLAnalyzer implements Runnable{

	private BlockingQueue<IP> IPs;
	private Set<String> htmls;
	private Class<BaseHtmlAnalyzer> clazz;
private CountDownLatch end;
	
	public HTMLAnalyzer(Set<String> htmls,BlockingQueue<IP> IPs,Class<BaseHtmlAnalyzer> clazz,CountDownLatch end){
		this.htmls = htmls;
		this.IPs = IPs;
		this.clazz = clazz;
		this.end = end ;
	}
	
	@Override
	public void run() {
		for(String html : htmls){
			analyzer(html, IPs, clazz);
		}
		end.countDown();
	}
	
	//反射
	void analyzer(String html,BlockingQueue<IP> IPs,Class<BaseHtmlAnalyzer> clazz){
		try{
		BaseHtmlAnalyzer analyzer = clazz.newInstance();
		analyzer.analyzer(html, IPs);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
