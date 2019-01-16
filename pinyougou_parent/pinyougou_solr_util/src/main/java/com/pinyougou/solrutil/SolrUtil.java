package com.pinyougou.solrutil;

import com.alibaba.fastjson.JSON;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SolrUtil {
    @Autowired
    private TbItemMapper itemMapper;
    @Autowired
    private SolrTemplate solrTemplate;


    //查询数据库Tb_Item表中所有数据,把字段上传到solrhome
    public void importItemData(){
        TbItemExample example=new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andStatusEqualTo("1");
        List<TbItem> items = itemMapper.selectByExample(example);


        //把查询出来的所有记录进行遍历
        for (TbItem item : items) {

            //对spec字段上的字符串转换为Map集合
            String spec = item.getSpec();
            Map map = JSON.parseObject(spec, Map.class);
            System.out.println(map);

            //把map集合封装进item对象中
            item.setSpecMap(map);


        }


        //把字段上传到solrhome
        solrTemplate.saveBeans(items);

        //提交事务
        solrTemplate.commit();




    }


    public static void main(String[] args) {
        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath*:spring/applicationContext*.xml");
        SolrUtil sorlUtil = (SolrUtil) ac.getBean("solrUtil");
        sorlUtil.importItemData();
    }
}
