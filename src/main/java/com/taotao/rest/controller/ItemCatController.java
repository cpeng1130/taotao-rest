package com.taotao.rest.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.taotao.rest.pojo.ItemCatResult;
import com.taotao.rest.service.ItemCatService;

@Controller
@RequestMapping("/item/cat")
public class ItemCatController {

	@Autowired
	private ItemCatService itemCatService;
	// 方法1
	/*@RequestMapping(value="/list",produces=MediaType.APPLICATION_JSON_VALUE+";charset=utf-8")
	@ResponseBody
	public String getItemCatList(String callback){
		ItemCatResult result= itemCatService.getItemCatList();
		if(StringUtils.isBlank(callback)){
			String json = JsonUtils.objectToJson(result);
			return json;			
		}
		String json =JsonUtils.objectToJson(callback);
		return callback+"("+json+")";
	}*/
	
	//方法2
	@RequestMapping("/list")
	@ResponseBody
	public Object getItemCatList(String callback){
		ItemCatResult result= itemCatService.getItemCatList();
		if(StringUtils.isBlank(callback)){
			return result;			
		}
		MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
		mappingJacksonValue.setJsonpFunction(callback);
		return mappingJacksonValue;
	}
	
}
