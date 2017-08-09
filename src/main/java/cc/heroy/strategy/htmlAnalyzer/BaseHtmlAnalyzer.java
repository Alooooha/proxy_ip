package cc.heroy.strategy.htmlAnalyzer;

import java.util.concurrent.CopyOnWriteArrayList;

import cc.heroy.bean.IP;

public interface BaseHtmlAnalyzer {
	
	public void analyzer(String html , CopyOnWriteArrayList<IP> IPs);
	
	
}
