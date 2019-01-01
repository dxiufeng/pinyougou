package com.pinyougou.manager.controller;

import java.util.List;

import com.pinyougou.sellergoods.service.BrandService;
import entity.PageResult;
import entity.Result;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;

import com.pinyougou.pojo.TbBrand;

@RestController//@Controller 和@ResponseBody的结合,这样配置,相当于返回值直接回返回到请求的页面
@RequestMapping("/brand")
public class BrandController {
	@Reference
	private BrandService brandService;
	
	
	@RequestMapping("/findAll")
	public List<TbBrand> findAll(){
		return brandService.findAll();
	}


	//分页查询
	@RequestMapping("/findPage")
	public PageResult findPage(@RequestParam(name = "page",defaultValue = "1") Integer currentPage, @RequestParam(name = "size",defaultValue = "10") Integer pageSize){

		return brandService.findPage(currentPage, pageSize);
	}


	//添加
	@RequestMapping("/saveBrand")
	public Result saveBrand(@RequestBody TbBrand brand){
		try {
			brandService.saveBround(brand);
			return new Result(true,"添加成功");
		} catch (Exception e) {
			return new Result(false,"添加失败");
		}
	}

	//修改
	@RequestMapping("/updateBrand")
	public Result updateBrand(@RequestBody TbBrand brand){
		try {
			brandService.changeBrand(brand);
			return new Result(true,"修改成功");
		} catch (Exception e) {
			return new Result(false,"修改失败");
		}
	}

}
