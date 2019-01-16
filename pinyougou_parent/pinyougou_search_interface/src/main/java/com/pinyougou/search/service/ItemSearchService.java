package com.pinyougou.search.service;

import com.pinyougou.pojo.TbItem;

import java.util.List;
import java.util.Map;


public interface ItemSearchService {
    /**
     * solr查询接口,重索引库中查询数据
     */
    public Map<String,Object> search(Map<String,Object> searchMap);

    /**
     * 把数据重写更新到solr索引库
     * @param itemList
     */
    public void importItemData(List<TbItem> itemList);


    /**
     * 把数据从索引库中删除
     */
    public void deleteByGoodsIds(Long[] ids);
}
