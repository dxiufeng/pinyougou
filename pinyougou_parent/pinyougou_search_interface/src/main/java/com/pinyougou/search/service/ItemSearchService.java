package com.pinyougou.search.service;

import java.util.Map;


public interface ItemSearchService {
    /**
     * solr查询接口
     */
    public Map<String,Object> search(Map<String,Object> searchMap);
}
