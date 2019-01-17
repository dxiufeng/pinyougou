package com.pinyougou.page.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.pinyougou.mapper.TbGoodsDescMapper;
import com.pinyougou.mapper.TbGoodsMapper;
import com.pinyougou.mapper.TbItemCatMapper;
import com.pinyougou.mapper.TbItemMapper;
import com.pinyougou.page.service.ItemPageService;
import com.pinyougou.pojo.TbGoods;
import com.pinyougou.pojo.TbGoodsDesc;
import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemExample;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.FileWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ItemPageServiceImpl implements ItemPageService {
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    @Value("${pagedir}")
    private String pagedir;//配置文件中生成静态html页面的目录

    @Autowired
    private TbGoodsMapper goodsMapper;
    @Autowired
    private TbGoodsDescMapper goodsDescMapper;
    @Autowired
    private TbItemCatMapper itemCatMapper;
    @Autowired
    private TbItemMapper itemMapper;


    //生成静态页面
    @Override
    public boolean getItemHtml(Long goodsId) {
        try {
            //1获取配置对象,在freeMarkerConfigurer中已经配置好了模板所在的目录和字符编码
            Configuration configuration = freeMarkerConfigurer.getConfiguration();
            //2获取模板对象
            Template template = configuration.getTemplate("item.ftl");
            //3创建数据模型dataMode
            Map dataMap = new HashMap();

            //4通过goodsId查询goods表和tb_goodsDesc表,把数据放入到数据模型中
            TbGoods goods = goodsMapper.selectByPrimaryKey(goodsId);
            TbGoodsDesc goodsDesc = goodsDescMapper.selectByPrimaryKey(goodsId);
            dataMap.put("goods", goods);
            dataMap.put("goodsDesc",goodsDesc);
            //4.1读取商品分类表中数据TbItemCat
            String itemName1 = itemCatMapper.selectByPrimaryKey(goods.getCategory1Id()).getName();//商品分类名称
            String itemName2 = itemCatMapper.selectByPrimaryKey(goods.getCategory2Id()).getName();//商品分类名称
            String itemName3 = itemCatMapper.selectByPrimaryKey(goods.getCategory3Id()).getName();//商品分类名称
            dataMap.put("category1",itemName1);
            dataMap.put("category2",itemName2);
            dataMap.put("category3",itemName3);

            //4.2读取sku表中的数据,并把数据生成在页面上
            TbItemExample example = new TbItemExample();
            TbItemExample.Criteria criteria = example.createCriteria();
            criteria.andStatusEqualTo("1");
            criteria.andGoodsIdEqualTo(goodsId);
            example.setOrderByClause("is_default desc");
            List<TbItem> items = itemMapper.selectByExample(example);
            dataMap.put("itemList", items);

            //5 创建输出流,并确定输出的文件路径和名字
            Writer writer=new FileWriter(pagedir+goodsId+".html");
            //6 生成模板
            template.process(dataMap,writer);
            //7关闭输出流
            writer.close();
            System.out.println("生成成功,successful");
            return true;

        } catch (Exception e) {
            System.out.println("生成失败,failed");
            return false;
        }
    }
}
