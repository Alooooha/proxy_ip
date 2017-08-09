package proxy_ip;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import cc.heroy.bean.IP;
import cc.heroy.thread.IPSpider;
import cc.heroy.util.HttpClientUtil;

public class CoreTest {
	public static void main(String[] args) {
		ConcurrentHashMap<String,Set<String>> entitys =new ConcurrentHashMap<>();
		final CopyOnWriteArrayList<IP> IPs = new CopyOnWriteArrayList<>();
		
		new Thread(new IPSpider(entitys, HttpClientUtil.getHttpClient(), IPs)).start();
	}
}
