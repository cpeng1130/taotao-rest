package com.taotao.rest.service;

import org.springframework.stereotype.Service;

import com.taotao.rest.pojo.ItemCatResult;

@Service
public interface ItemCatService {
	public ItemCatResult getItemCatList();
	
}
