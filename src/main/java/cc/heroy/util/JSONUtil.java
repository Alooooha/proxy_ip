package cc.heroy.util;

import com.alibaba.fastjson.JSONObject;

public class JSONUtil {
	
	public static JSONObject constructJSON(int code,String msg,Object data){
		JSONObject jo = new JSONObject();
		jo.put("code", code);
		jo.put("msg", msg);
		jo.put("data", data);
		
		return jo;
	}
	public static JSONObject constructJSON(String host,String port){
		JSONObject jo = new JSONObject();
		jo.put("host", host);
		jo.put("port", port);
		
		return jo;
	}
	
}
