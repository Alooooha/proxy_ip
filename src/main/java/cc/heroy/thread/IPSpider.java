package cc.heroy.thread;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import cc.heroy.bean.IP;
import cc.heroy.strategy.htmlAnalyzer.BaseHtmlAnalyzer;
import cc.heroy.util.ThreadPoolUtil;

/**
 * 
 * @ClassName: IPSpider
 * @Description: 向IP代理网站爬取网站信息
 * @author BeiwEi
 * @date 2017年8月8日 下午3:46:57
 *
 */
public class IPSpider implements Runnable {

	Logger logger = Logger.getLogger(this.getClass());
	//IPTester数量
	int testerCount = 3;

	private CloseableHttpClient httpClient;
	private BlockingQueue<IP> IPs;
	private Set<String> urls ;
	private Class<BaseHtmlAnalyzer> clazz ;
	private Vector<IP> vector;
	private CountDownLatch over;
	
	public IPSpider(Set<String> urls, CloseableHttpClient httpClient,
			BlockingQueue<IP> IPs,Class<BaseHtmlAnalyzer> clazz,Vector<IP> vector,CountDownLatch over) {
		super();
		this.urls = urls ;
		this.httpClient = httpClient;
		this.IPs = IPs;
		this.clazz = clazz;
		this.vector = vector;
		this.over = over;
	}

	@Override
	public void run() {
		
		ThreadPoolExecutor threadPool = ThreadPoolUtil.getThreadPool();
		Set<String> htmls = new HashSet<String>();
		//闭锁
CountDownLatch end = new CountDownLatch(4);
		
		//请求urls地址并解析出html(较慢，偶尔出现请求错误)
		for(String url : urls){
			String content = spider(url);
			htmls.add(content);
		}
		//解析html(速度较快)
		threadPool.execute(new HTMLAnalyzer(htmls,IPs,clazz,end));
try {
	Thread.sleep(2000);
} catch (InterruptedException e1) {
	e1.printStackTrace();
}
		System.out.println(IPs.size());
		//启动IPTester
		for(int i=0;i<testerCount;i++){
			IPTester tester = new IPTester(IPs,vector,end);
			threadPool.execute(tester);
		}
		
		try {
			end.await();
			over.countDown();
		} catch (InterruptedException e) {
			e.printStackTrace();
			over.countDown();
		}
		System.out.println("IP搜集完成！");
	}

	
	private String spider(String url) {
		HttpGet get = new HttpGet(url);
		// 模拟服务器
		get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
		// 设置请求超时时间 5s
		RequestConfig config = RequestConfig.custom().setConnectTimeout(5000).build();
		get.setConfig(config);
		CloseableHttpResponse response = null;
		String content = "";
		try {
			response = httpClient.execute(get);
			content = EntityUtils.toString(response.getEntity());
			// entitys.put(content);
			System.out.println("已访问：" + url);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return content;
	}
}