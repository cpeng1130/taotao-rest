package com.taotao.rest.service;

import org.springframework.stereotype.Service;

import com.taotao.pojo.TbItem;
import com.taotao.pojo.TbItemDesc;
import com.taotao.pojo.TbItemParamItem;
@Service
public interface ItemService {

	TbItem getItemById(Long itemId);
	TbItemDesc getItemDescById(Long itemId);
	TbItemParamItem getItemParamById(long itemId);
}
