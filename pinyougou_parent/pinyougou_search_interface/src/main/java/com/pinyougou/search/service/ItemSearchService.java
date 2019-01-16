package com.pinyougou.search.service;

import com.pinyougou.pojo.TbItem;

import java.util.List;
import java.util.Map;


public interface ItemSearchService {
    /**
     * solr查询接口
     */
    public Map<String,Object> search(Map<String,Object> searchMap);

    public void importItemData(List<TbItem> itemList);
}
