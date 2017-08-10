package cc.heroy.thread;

import java.io.IOException;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;

import cc.heroy.bean.IP;
import cc.heroy.util.HttpClientUtil;

/**
 * 
* @ClassName: IPTester
* @Description: 测试IP是否可用
* @author BeiwEi
* @date 2017年8月10日 下午6:02:29
*
 */
public class IPTester implements Runnable{
	static volatile int  i = 0;

	private BlockingQueue<IP> IPs;
	private Vector<IP> vector;
	private HttpClient httpClient;
private CountDownLatch end;	
	
	public IPTester(BlockingQueue<IP> IPs, Vector<IP> vector,CountDownLatch end) {
		super();
		this.IPs = IPs;
		this.vector = vector;
		httpClient = HttpClientUtil.getHttpClient();
		this.end =end ;
	}

	@Override
	public void run() {
		while(!IPs.isEmpty()){
			try {
				IP ip = IPs.take();
				if(isFine(ip)){
					vector.add(ip);
				}
				
			} catch (InterruptedException e) {
				end.countDown();
				e.printStackTrace();
			}
			
		}
		System.out.println("IPTester结束");
		end.countDown();
	}
	
	private boolean isFine(IP ip){
long b = System.currentTimeMillis();
		String url = "https://seofangfa.com/checkproxy/";		
		HttpGet httpGet = new HttpGet(url); 
		HttpHost proxy = new HttpHost(ip.getHost(), Integer.parseInt(ip.getPort()));
		//设置IP代理和连接超时时间
		RequestConfig requestConfig = RequestConfig.custom().setProxy(proxy).setConnectTimeout(1000).setConnectionRequestTimeout(2000).setSocketTimeout(2000).build();
		httpGet.setConfig(requestConfig);
		//模拟浏览器请求
		httpGet.setHeader("User-Agent","Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
		HttpResponse response ;
		try {
			response = httpClient.execute(httpGet);
			if(response.getStatusLine().getStatusCode()==200){
System.out.println(ip.getHost()+":"+ip.getPort()+"可用"+"  时间 "+(System.currentTimeMillis()-b));
				return true;
			}
		} catch (IOException e) {
System.out.println(ip.getHost()+":"+ip.getPort()+"请求失败"+"  时间 "+(System.currentTimeMillis()-b));
		}
		return false;
	}
	

}
