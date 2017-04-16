package com.taotao.rest.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.utils.JsonUtils;
import com.taotao.common.utils.TaotaoResult;
import com.taotao.mapper.TbContentMapper;
import com.taotao.pojo.TbContent;
import com.taotao.pojo.TbContentExample;
import com.taotao.pojo.TbContentExample.Criteria;
import com.taotao.rest.component.JedisClient;
import com.taotao.rest.service.ContentService;

@Service
public class ContentServiceImpl implements ContentService {

	@Autowired
	private TbContentMapper contentMapper;
	@Autowired
	private JedisClient jedisClient;
		
	@Value("${REDIS_CONTENT_KEY}")
	private String REDIS_CONTENT_KEY;
	public List<TbContent> getContentList(long cid) {
		
		try{
			String json = jedisClient.hget(REDIS_CONTENT_KEY, cid+"");
			if(!StringUtils.isBlank(json)){
				List<TbContent> list = JsonUtils.jsonToList(json, TbContent.class);
				System.out.println("by using Jedis");
				return list;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		TbContentExample example = new TbContentExample();
		Criteria criteria = example.createCriteria();
		criteria.andCategoryIdEqualTo(cid);
		List<TbContent> list = contentMapper.selectByExampleWithBLOBs(example);
		System.out.println("by using db");
		try{
			jedisClient.hset(REDIS_CONTENT_KEY ,cid+"", JsonUtils.objectToJson(list));
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return list;
	}
	@Override
	public TaotaoResult syncContent(long cid) {
		
		jedisClient.hdel(REDIS_CONTENT_KEY, cid+"");
		return TaotaoResult.ok();
	}

}
