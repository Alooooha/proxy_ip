package cc.heroy.thread;

import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import cc.heroy.bean.IP;
import cc.heroy.strategy.htmlAnalyzer.BaseHtmlAnalyzer;

public class HTMLAnalyzer implements Runnable{

	private final ConcurrentHashMap<String,Set<String>> entitys ;
	private final CopyOnWriteArrayList<IP> IPs;
	private final Properties prop;
	private List<String> list;
	
	public HTMLAnalyzer(ConcurrentHashMap<String,Set<String>> entitys,CopyOnWriteArrayList<IP> IPs,Properties prop,List<String> list){
		this.entitys = entitys;
		this.IPs = IPs ;
		this.prop = prop ;
		this.list = list ;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		Iterator<String> it = list.iterator();
		while(it.hasNext()){
			String str= it.next();
			Class<BaseHtmlAnalyzer> clazz;
			try {
				clazz = (Class<BaseHtmlAnalyzer>) Class.forName((String) prop.get(str));
				Set<String> htmls = entitys.get(str);
				for(String html : htmls){
					analyzer(html, IPs, clazz);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
	//反射
	void analyzer(String html,CopyOnWriteArrayList<IP> IPs,Class<BaseHtmlAnalyzer> clazz){
		try{
		BaseHtmlAnalyzer analyzer = clazz.newInstance();
		analyzer.analyzer(html, IPs);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
