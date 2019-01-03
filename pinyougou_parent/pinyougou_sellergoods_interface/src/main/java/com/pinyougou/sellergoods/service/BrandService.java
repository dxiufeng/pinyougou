package com.pinyougou.sellergoods.service;
/**
 * 品牌接口
 *
 * @author dxf
 */

import java.util.List;
import java.util.Map;

import com.pinyougou.pojo.TbBrand;
import entity.PageResult;

public interface BrandService {

    //查询所有
    public List<TbBrand> findAll();

    //分页查询
    public PageResult findPage(Integer currentPage, Integer pageSize);

    //保存brand
    public void saveBround(TbBrand brand);


    //修改brand
    public void changeBrand(TbBrand brand);

    //删除brand
    public void deleteBrand(Long[] ids);

    //模糊查询
    public PageResult findPage(Integer currentPage, Integer pageSize, TbBrand brand);

    //查询所有的品牌信息封装到map集合中并整个变为list集合
    public List<Map> selectOptionList();
}
