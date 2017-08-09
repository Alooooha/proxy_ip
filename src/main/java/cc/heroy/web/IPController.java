package cc.heroy.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;

import cc.heroy.util.JSONUtil;


@Controller
public class IPController {
	
	@ResponseBody
	@RequestMapping(value="/getIPProxy",method=RequestMethod.GET)
	public JSONObject getIPProxy(){
		//
		
		String host = "172.1.1.0";
		String port = "80";
		
		return JSONUtil.constructJSON(host, port);
	}
	
	
}
