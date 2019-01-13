package com.pinyougou.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.*;
import org.springframework.data.solr.core.query.result.*;
import org.springframework.data.solr.core.query.result.HighlightEntry.Highlight;


import java.util.ArrayList;
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


        //进行普通的查询:
       /* HashMap<String, Object> map = new HashMap<>();

        //获取query查询对象
        Query query=new SimpleQuery("*:*");

        //添加条件,即数据在item_keywords 域下面的keywords关键词
        Criteria criteria = new Criteria("item_keywords");
        criteria.is(searchMap.get("keywords"));

        query.addCriteria(criteria);

        ScoredPage<TbItem> page = solrTemplate.queryForPage(query, TbItem.class);

        List<TbItem> items = page.getContent();//获取数据的封装集合

        map.put("rows", items);//封装到map集合中,rows表示每一行的数据,有多行*/

        //------------------------------------------------------------------
        //高亮查询
        HashMap<String, Object> map = new HashMap<>();
        map.putAll(searchList(searchMap));//putAll(),把一个map集合追加到另一个map集合里,便于扩展


        //分组查询商品分类列表
        List list = searchCategoryList(searchMap);
        map.put("categoryList",list );

        //把查询好的数据放入map集合中返回
        return map;
    }

    /**
     * 查询数据库中符合条件的信息,以Map返回
     * @param searchMap
     * @return
     */
    private Map searchList(Map<String, Object> searchMap) {
        //高亮结果显示
        HashMap<String, Object> map = new HashMap<>();

        //获取获取query查询对象
        HighlightQuery query = new SimpleHighlightQuery();

        //构建高亮对象
        HighlightOptions highlightOptions = new HighlightOptions();
        highlightOptions.addField("item_title");//添加高亮显示域
        // highlightOptions.addField("item_price");//添加高亮显示域

        //添加高亮效果
        highlightOptions.setSimplePrefix("<en style='color:red'>");//前缀
        highlightOptions.setSimplePostfix("</en>");//后缀

        //query中设置高亮对象
        query.setHighlightOptions(highlightOptions);

        //添加查询条件
        Criteria criteria = new Criteria("item_keywords");
        criteria.is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        //通过solrTemplate进行查询
        HighlightPage<TbItem> page = solrTemplate.queryForHighlightPage(query, TbItem.class);

        //高亮入口集合就是一些符合规则的高亮数据的集合[{id:1,item_title:"三星",},{},{}]
        List<HighlightEntry<TbItem>> list = page.getHighlighted();

        //循环遍历集合,设置title为高亮显示
        //entry里面包含了highlightList和item
        for (HighlightEntry<TbItem> entry : list) {

            List<Highlight> highlightList = entry.getHighlights();//高亮显示结合

            //判断是否存在高亮页
            if (highlightList.size() > 0 && highlightList.get(0).getSnipplets().size() > 0) {

                TbItem item = entry.getEntity();//获取高亮后的选项,一个entry就对应数据库里面一条数据
                item.setTitle(highlightList.get(0).getSnipplets().get(0));

            }
        }

        map.put("rows", page.getContent());

        return map;

    }

    /**
     * 分组查询查询商品分类列表
     * @param searchMap
     * @return
     */
    private List searchCategoryList(Map<String, Object> searchMap) {

        List<String> list =new ArrayList<>();

        //创建查询对象,并添加查询条件 ,类似where ..
        Query query = new SimpleQuery("*:*");
        Criteria criteria = new Criteria("item_keywords").is(searchMap.get("keywords"));
        query.addCriteria(criteria);

        //创建分组对象,添加到查询对象中, 类似group by item_category
        GroupOptions groupOptions = new GroupOptions().addGroupByField("item_category");
        query.setGroupOptions(groupOptions);

        //进行solr查询
        GroupPage<TbItem> page = solrTemplate.queryForGroupPage(query, TbItem.class);

        //获取分组结果对象
        GroupResult<TbItem> category = page.getGroupResult("item_category");//获取item_category域中的分结果


        //获取分组入口页category是一个map集合中包含多个数据,根据key(item_category),得到value(SimpleGroupResult)对象
        //SimpleGroupResult对象中封装了一个GroupEntries(PageImpl)对象,在GroupEntries对象中有content(List集合)集合
        //content集合中存储SimpleGroupEntry对象,在该对象中有一个属性groupValue;
        Page<GroupEntry<TbItem>> groupEntries = category.getGroupEntries();
        //获取分组入口集合
        List<GroupEntry<TbItem>> entryList = groupEntries.getContent();

        //遍历把数据放入到list集合中
        for (GroupEntry<TbItem> entry : entryList) {
            list.add(entry.getGroupValue());
        }


        return list;



    }
}
