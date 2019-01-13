package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Service(timeout = 5000)
public class ItemSearchServiceImpl implements ItemSearchService {

    @Autowired
    private SolrTemplate solrTemplate;


    //已经把数据库中的数据都添加到索引库中,并查询索引库
    @Override
    public Map<String, Object> search(Map<String, Object> searchMap) {
        HashMap<String, Object> map = new HashMap<>();

        //获取query查询对象
        Query query=new SimpleQuery("*:*");

        //添加条件,即数据在item_keywords 域下面的keywords关键词
        Criteria criteria = new Criteria("item_keywords");
        criteria.is(searchMap.get("keywords"));

        query.addCriteria(criteria);

        ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);

        List<TbItem> items = page.getContent();//获取数据的封装集合

        map.put("rows", items);//封装到map集合中,rows表示每一行的数据,有多行


        return map;
    }
}
