package cc.heroy.strategy.htmlAnalyzer;

import java.util.concurrent.BlockingQueue;

import org.apache.log4j.Logger;

import cc.heroy.bean.IP;

public abstract class BaseHtmlAnalyzer {
	
	Logger logger = Logger.getLogger(this.getClass());
	
	public abstract void analyzer(String html , BlockingQueue<IP> IPs);
	
}
