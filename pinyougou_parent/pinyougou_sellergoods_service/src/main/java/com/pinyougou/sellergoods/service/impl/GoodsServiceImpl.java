package com.pinyougou.sellergoods.service.impl;
import java.util.*;

import com.alibaba.dubbo.common.json.JSON;
import com.alibaba.dubbo.common.json.ParseException;
import com.alibaba.fastjson.JSONObject;
import com.pinyougou.mapper.*;
import com.pinyougou.pojo.*;
import com.pinyougou.pojogroup.Goods;
import com.pinyougou.sellergoods.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

import com.pinyougou.pojo.TbGoodsExample.Criteria;


import entity.PageResult;
import org.springframework.transaction.annotation.Transactional;

/**
 * 服务实现层
 * @author Administrator
 *
 */
@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

	@Autowired
	private TbGoodsMapper goodsMapper;
	@Autowired
	private TbGoodsDescMapper goodsDescMapper;
	@Autowired
	private TbItemMapper itemMapper;
	@Autowired
	private TbBrandMapper brandMapper;
	@Autowired
	private TbItemCatMapper itemCatMapper;
	@Autowired
	private TbSellerMapper sellerMapper;
	
	/**
	 * 查询全部
	 */
	@Override
	public List<TbGoods> findAll() {
		return goodsMapper.selectByExample(null);
	}

	/**
	 * 按分页查询
	 */
	@Override
	public PageResult findPage(int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);		
		Page<TbGoods> page=   (Page<TbGoods>) goodsMapper.selectByExample(null);
		return new PageResult(page.getTotal(), page.getResult());
	}

	/**
	 * 增加
	 */
	@Override
	public void add(Goods goods) {


		TbGoods goods1 = goods.getGoods();
		goods1.setAuditStatus("0");
		goodsMapper.insert(goods1);	//在插入的时候要插入两张表分别是tbgoods和tbgoodsDesc


		//id同步
		TbGoodsDesc goodsDesc = goods.getGoodsDesc();
		goodsDesc.setGoodsId(goods1.getId());

		//把数据插入第二种表tbgoodsDesc中
		goodsDescMapper.insert(goodsDesc);


        //在item表中插入数据
        saveItemList(goods);//插入sku列表数据

	}

	
	/**
	 * 修改
	 */
	@Override
	public void update(Goods goods){

		goods.getGoods().setAuditStatus("0");//修改后要重写审核,设置状态为0
	   //更改goods表数据
        goodsMapper.updateByPrimaryKey(goods.getGoods());


        //更改goodsDesc
        goodsDescMapper.updateByPrimaryKey(goods.getGoodsDesc());

        //更改表item  先删除在插入
        TbItemExample example=new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsIdEqualTo(goods.getGoods().getId());
        itemMapper.deleteByExample(example);//删除当前goodsId下的所有数据


        //在item表中插入相关数据
        saveItemList(goods);//插入sku列表数据

	}



    //插入sku列表数据
	private void saveItemList(Goods goods){
        //启用规格后
        if ("1".equals(goods.getGoods().getIsEnableSpec())){
            //增加需要插入的表items数据
            List<TbItem> items = goods.getItemList();//获得item集合数据
            for (TbItem item : items) {
                //title
                String title = goods.getGoods().getGoodsName();


                String sp = item.getSpec();
                Map<String,Object> map = JSONObject.parseObject(sp);//{"网络":"移动3G","机身内存":"32G"}
                Set<String> set = map.keySet();
                for (String s : set) {
                    title+=" "+map.get(s);
                }

                item.setTitle(title);//商品标题


                item.setGoodsId(goods.getGoods().getId());//商品编号
                item.setCategoryid(goods.getGoods().getCategory3Id());//商品分类编号
                item.setCreateTime(new Date());//创建日期
                item.setUpdateTime(new Date());//修改日期

                //品牌
                TbBrand tbBrand = brandMapper.selectByPrimaryKey(goods.getGoods().getBrandId());
                item.setBrand(tbBrand.getName());
                //分类名称
                TbItemCat tbItemCat = itemCatMapper.selectByPrimaryKey(goods.getGoods().getCategory3Id());
                item.setCategory(tbItemCat.getName());
                //商家名称
                TbSeller seller = sellerMapper.selectByPrimaryKey(goods.getGoods().getSellerId());
                item.setSeller(seller.getNickName());
                //图片地址
                String itemImages = goods.getGoodsDesc().getItemImages();



                List<Map> list = com.alibaba.fastjson.JSON.parseArray(itemImages, Map.class);




                if (list.size()>0){
                    String url = (String) list.get(0).get("url");
                    item.setImage(url);
                }


                itemMapper.insert(item);


            }
        }
    }


	
	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	@Override
	public Goods findOne(Long id){

        Goods goods = new Goods();
	    //查询tbgoods 商品表
        TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
        goods.setGoods(tbGoods);

        //查询商品扩展表goodsDesc
        TbGoodsDesc tbGoodsDesc = goodsDescMapper.selectByPrimaryKey(id);
        goods.setGoodsDesc(tbGoodsDesc);

        //查询sku表 (tbItem表)
        TbItemExample example=new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andGoodsIdEqualTo(id);
        List<TbItem> tbItems = itemMapper.selectByExample(example);
        goods.setItemList(tbItems);
        return goods;
	}

	/**
	 * 批量删除
	 */
	@Override
	public void delete(Long[] ids) {
		for(Long id:ids){
			//goodsMapper.deleteByPrimaryKey(id);
			//改为逻辑删除
			TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
			tbGoods.setIsDelete("1");
			goodsMapper.updateByPrimaryKey(tbGoods);
		}		
	}
	
	
		@Override
	public PageResult findPage(TbGoods goods, int pageNum, int pageSize) {
		PageHelper.startPage(pageNum, pageSize);
		
		TbGoodsExample example=new TbGoodsExample();
		Criteria criteria = example.createCriteria();
		criteria.andIsDeleteIsNull();//设置是否删除为空,则表示逻辑上没删除
		
		if(goods!=null){			
			if(goods.getSellerId()!=null && goods.getSellerId().length()>0){
			criteria.andSellerIdEqualTo(goods.getSellerId());//进行确定判断,不在用以前的模糊查询
			}
			if(goods.getGoodsName()!=null && goods.getGoodsName().length()>0){
				criteria.andGoodsNameLike("%"+goods.getGoodsName()+"%");
			}
			if(goods.getAuditStatus()!=null && goods.getAuditStatus().length()>0){
				criteria.andAuditStatusLike("%"+goods.getAuditStatus()+"%");
			}
			if(goods.getIsMarketable()!=null && goods.getIsMarketable().length()>0){
				criteria.andIsMarketableLike("%"+goods.getIsMarketable()+"%");
			}
			if(goods.getCaption()!=null && goods.getCaption().length()>0){
				criteria.andCaptionLike("%"+goods.getCaption()+"%");
			}
			if(goods.getSmallPic()!=null && goods.getSmallPic().length()>0){
				criteria.andSmallPicLike("%"+goods.getSmallPic()+"%");
			}
			if(goods.getIsEnableSpec()!=null && goods.getIsEnableSpec().length()>0){
				criteria.andIsEnableSpecLike("%"+goods.getIsEnableSpec()+"%");
			}
			if(goods.getIsDelete()!=null && goods.getIsDelete().length()>0){
				criteria.andIsDeleteLike("%"+goods.getIsDelete()+"%");
			}
	
		}
		
		Page<TbGoods> page= (Page<TbGoods>)goodsMapper.selectByExample(example);		
		return new PageResult(page.getTotal(), page.getResult());
	}


	//更改审核状态
	@Override
	public void updateStatus(Long[] ids, String status) {
		for (Long id : ids) {
			TbGoods tbGoods = goodsMapper.selectByPrimaryKey(id);
			tbGoods.setAuditStatus(status);
			goodsMapper.updateByPrimaryKey(tbGoods);
		}
	}


	//通过spu的id,查询sku的数据
	@Override
	public List<TbItem> findItemListByGoodsIdAndStatus(Long[] ids, String status) {

		TbItemExample example = new TbItemExample();

		TbItemExample.Criteria criteria = example.createCriteria();
		 criteria.andGoodsIdIn(Arrays.asList(ids));
		 criteria.andStatusEqualTo(status);
		List<TbItem> itemList = itemMapper.selectByExample(example);

		return itemList;
	}
}
