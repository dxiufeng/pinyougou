package com.pinyougou.sellergoods.service.impl;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pinyougou.pojo.TbBrandExample;
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



    //修改品牌
    @Override
    public void changeBrand(TbBrand brand) {
       brandMapper.updateByPrimaryKey(brand);

    }


    //删除品牌
    @Override
    public void deleteBrand(Long[] ids) {
        for (Long id : ids) {
            brandMapper.deleteByPrimaryKey(id);
        }

    }


    /**
     * 条件查询,当没有条件的时候,就是分页查询
     * @param currentPage
     * @param pageSize
     * @param brand
     * @return
     */
    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, TbBrand brand) {

        PageHelper.startPage(currentPage,pageSize);//分页
        TbBrandExample example = new TbBrandExample();
        TbBrandExample.Criteria criteria = example.createCriteria();//创建分页的条件

        //进行模糊查询
        if (brand!=null){
            //模糊查询,判断输入的数据中是否有name和firstchar,如果有进行字符串的拼接
            if (brand.getName()!=null && brand.getName().length()!=0){
                criteria.andNameLike("%"+brand.getName()+"%");
            }

            if (brand.getFirstChar()!=null && brand.getFirstChar().length()!=0){
                criteria.andFirstCharLike("%"+brand.getFirstChar()+"%");
            }
        }



        Page<TbBrand> page = (Page<TbBrand>) brandMapper.selectByExample(example);

        //把查询的数据封装到pageResult中
        PageResult pageResult = new PageResult();
        pageResult.setRows(page.getResult());
        pageResult.setTotal(page.getTotal());

        return pageResult;
    }

    @Override
    public List<Map> selectOptionList() {
        List<Map> maps = brandMapper.selectOptionList();
        return maps;
    }


}
