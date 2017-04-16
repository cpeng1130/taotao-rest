package com.taotao.rest.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.taotao.common.utils.TaotaoResult;
import com.taotao.pojo.TbContent;
@Service
public interface ContentService {
	
	List<TbContent> getContentList(long cid);
	TaotaoResult syncContent(long cid);
}
