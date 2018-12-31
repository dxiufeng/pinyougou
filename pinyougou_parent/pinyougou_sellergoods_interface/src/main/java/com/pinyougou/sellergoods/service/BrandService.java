package com.pinyougou.sellergoods.service;
/**
 * 品牌接口
 * @author dxf
 *
 */

import java.util.List;

import com.pinyougou.pojo.TbBrand;
import entity.PageResult;

public interface BrandService {

	//查询所有
	public List<TbBrand> findAll();

	//分页查询
	public PageResult findPage(Integer currentPage, Integer pageSize);

	//保存brand
	public void saveBround(TbBrand brand);
}
