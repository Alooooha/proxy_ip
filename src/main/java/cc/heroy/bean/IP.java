package cc.heroy.bean;

public class IP {
	private String host;
	private String port;
	
	public IP(String host, String port) {
		super();
		this.host = host;
		this.port = port;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	}
	@Override
	public String toString() {
		return "IP [host=" + host + ", port=" + port + "]";
	}
	
}
