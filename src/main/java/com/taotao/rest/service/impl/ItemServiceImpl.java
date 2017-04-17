package com.taotao.rest.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.taotao.common.utils.JsonUtils;
import com.taotao.mapper.TbItemDescMapper;
import com.taotao.mapper.TbItemMapper;
import com.taotao.mapper.TbItemParamItemMapper;
import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;
import com.taotao.pojo.TbItemParamItemExample;
import com.taotao.pojo.TbItemParamItemExample.Criteria;
import com.taotao.rest.component.JedisClient;
import com.taotao.rest.service.ItemService;

@Service
public class ItemServiceImpl implements ItemService {
	@Autowired
	private TbItemMapper tbItemMapper;
	@Autowired
	private TbItemDescMapper tbItemDescMapper;
	@Autowired
	private JedisClient jedisClient;
	@Autowired
	private TbItemParamItemMapper itemParamItemMapper;
	@Value("${REDIS_ITEM_KEY}")
	private String REDIS_ITEM_KEY;
	@Value("${ITEM_BASE_INFO_KEY}")
	private String ITEM_BASE_INFO_KEY;
	@Value("${ITEM_EXPIRE_SECOND}")
	private Integer ITEM_EXPIRE_SECOND;
	@Value("${ITEM_DESC_KEY}")
	private String ITEM_DESC_KEY;
	@Value("${ITEM_PARAM_KEY}")
	private String ITEM_PARAM_KEY;

	@Override
	public TbItem getItemById(Long itemId) {

		try {
			String json = jedisClient.get(REDIS_ITEM_KEY + ":" + ITEM_BASE_INFO_KEY + ":" + itemId);
			if (!StringUtils.isBlank(json)) {
				TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
				return item;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		TbItem item = tbItemMapper.selectByPrimaryKey(itemId);

		try {
			jedisClient.set(REDIS_ITEM_KEY + ":" + itemId + ":" + ITEM_BASE_INFO_KEY, JsonUtils.objectToJson(item));
			jedisClient.expire(REDIS_ITEM_KEY + ":" + itemId + ":" + ITEM_BASE_INFO_KEY, ITEM_EXPIRE_SECOND);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return item;
	}

	@Override
	public TbItemDesc getItemDescById(Long itemId) {

		try {
			String json = jedisClient.get(REDIS_ITEM_KEY + ":" + itemId + ":" + ITEM_DESC_KEY);
			if (!StringUtils.isBlank(json)) {
				TbItemDesc item = JsonUtils.jsonToPojo(json, TbItemDesc.class);
				return item;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItemDesc itemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);

		try {
			jedisClient.set(REDIS_ITEM_KEY + ":" + itemId + ":" + ITEM_DESC_KEY, JsonUtils.objectToJson(itemDesc));
			jedisClient.expire(REDIS_ITEM_KEY + ":" + itemId + ":" + ITEM_DESC_KEY, ITEM_EXPIRE_SECOND);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return itemDesc;
	}

	@Override
	public TbItemParamItem getItemParamById(long itemId) {

		try {
			String json = jedisClient.get(REDIS_ITEM_KEY + ":" + itemId + ":" + ITEM_PARAM_KEY);
			if (!StringUtils.isBlank(json)) {
				TbItemParamItem  itemParamItem = JsonUtils.jsonToPojo(json, TbItemParamItem.class);
				return itemParamItem;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		TbItemParamItemExample example = new TbItemParamItemExample();
		Criteria criteria = example.createCriteria();
		criteria.andItemIdEqualTo(itemId);
		List<TbItemParamItem> list = itemParamItemMapper.selectByExampleWithBLOBs(example);
		if(list!=null && list.size()>0){
			TbItemParamItem tbItemParamItem = list.get(0);
			
			try {
				jedisClient.set(REDIS_ITEM_KEY + ":" + itemId + ":" + ITEM_PARAM_KEY, JsonUtils.objectToJson(tbItemParamItem));
				jedisClient.expire(REDIS_ITEM_KEY + ":" + itemId + ":" + ITEM_PARAM_KEY, ITEM_EXPIRE_SECOND);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return tbItemParamItem;
		}
		return null;
	}

}
