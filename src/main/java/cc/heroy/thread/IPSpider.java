package cc.heroy.thread;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import cc.heroy.bean.IP;

/**
 * 
* @ClassName: IPSpider
* @Description: 向IP代理网站爬取网站信息
* @author BeiwEi
* @date 2017年8月8日 下午3:46:57
*
 */
public class IPSpider implements Runnable{
	
	private final ConcurrentHashMap<String,Set<String>> entitys ;
	private final CloseableHttpClient httpClient ;
	private final CopyOnWriteArrayList<IP> IPs;
	
	public IPSpider(ConcurrentHashMap<String,Set<String>> entitys, CloseableHttpClient httpClient , CopyOnWriteArrayList<IP> IPs) {
		super();
		this.entitys = entitys;
		this.httpClient = httpClient;
		this.IPs = IPs;
	}

	@Override
	public void run() {
		//加载url.properties文件
		Properties prop = new Properties();
		try{
//		prop.load(getClass().getResourceAsStream("../url.properties"));
//System.out.println(getClass().getResourceAsStream("../url.properties"));
			prop.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("url.properties"));
System.out.println("正在加载url配置文件...");
		}catch(IOException e){
			e.printStackTrace();
		}
		//将KEY-VALUE依次取出来
		Iterator<String> it = prop.stringPropertyNames().iterator();
		List<String> list = new ArrayList<String>();
		while(it.hasNext()){
			String key = it.next();
			if(key.length()==1){
				if(!entitys.containsKey(key)){
				entitys.put(key, new HashSet<String>());
				}
				list.add(key);
			}else{
				if(entitys.get(key.substring(0,1))==null){
				entitys.put(key.substring(0,1), new HashSet<String>());
				}
				entitys.get(key.substring(0, 1)).add(prop.getProperty(key));
				System.out.println("添加 --》"+key+prop.getProperty(key));
			}
		}

		//从网页中获取content
		for(int i = 0;i<list.size();i++){
			Iterator<String> u = entitys.get(list.get(i)).iterator();
			Set<String> s = new HashSet<String>();
			while(u.hasNext()){ 
				String url = u.next();
				String content = spider(url);
				s.add(content);
			}
			entitys.put(list.get(i), s);
		}
		//使用策略解析html
		try {
			Thread t = new Thread(new HTMLAnalyzer(entitys, IPs, prop, list));
			t.start();
			t.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		for(IP ip :IPs){
			String str = ip.getHost()+":"+ip.getPort();
			System.out.println(ip.getHost()+":"+ip.getPort());
			//测试代理ip
			HttpPost post = new HttpPost("http://www.66ip.cn/yz/post.php");
			List<NameValuePair> params = new ArrayList<NameValuePair>();  
	        params.add(new BasicNameValuePair("ipadd", str)); 
			post.setEntity(new UrlEncodedFormEntity(params,Consts.UTF_8));
			try {
				System.out.println(EntityUtils.toString(httpClient.execute(post).getEntity()));
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		
		
System.out.println("IP搜集完成！");
	}

	private String spider(String url){
		HttpGet get = new HttpGet(url);
		
		//模拟服务器
		get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:50.0) Gecko/20100101 Firefox/50.0");
		
		//设置请求超时时间 5s
		RequestConfig config = RequestConfig.custom().setConnectTimeout(5000).build();
		get.setConfig(config);
		CloseableHttpResponse response = null ;
		String content = "";
		try{
			response = httpClient.execute(get);
			content = EntityUtils.toString(response.getEntity());
//			entitys.put(content);
			
System.out.println("已访问："+url);
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			try {
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return content ;
	}
}