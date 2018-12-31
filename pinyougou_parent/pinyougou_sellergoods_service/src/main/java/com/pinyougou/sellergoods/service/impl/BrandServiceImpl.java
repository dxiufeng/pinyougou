package com.pinyougou.sellergoods.service.impl;

import java.util.List;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.dubbo.config.annotation.Service;

import com.pinyougou.mapper.TbBrandMapper;
import com.pinyougou.pojo.TbBrand;

@Service
public class BrandServiceImpl implements BrandService {
    @Autowired
    private TbBrandMapper brandMapper;

    @Override
    public List<TbBrand> findAll() {
        return brandMapper.selectByExample(null);
    }

    /**
     * 分页查询
     *
     * @param currentPage
     * @param pageSize
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize) {
        PageResult pageResult = new PageResult();
        PageHelper.startPage(currentPage, pageSize);//pageHelper的使用
        Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(null);//获取page对象,里面封装了分页的基本数据
        //把page中的对象放入到pageResult中送到前端去
        pageResult.setTotal(page.getTotal());
        pageResult.setRows(page.getResult());
        return pageResult;
    }


    //添加品牌
    @Override
    public void saveBround(TbBrand brand) {
        brandMapper.insert(brand);
    }

}
