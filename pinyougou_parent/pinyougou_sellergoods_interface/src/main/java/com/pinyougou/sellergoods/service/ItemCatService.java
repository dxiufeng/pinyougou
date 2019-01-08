package com.pinyougou.sellergoods.service;
import java.util.List;


import com.pinyougou.pojo.TbItem;
import com.pinyougou.pojo.TbItemCat;
import entity.PageResult;
/**
 * 服务层接口
 * @author Administrator
 *
 */
public interface ItemCatService {

	/**
	 * 返回全部列表
	 * @return
	 */
	public List<TbItemCat> findAll();
	
	
	/**
	 * 返回分页列表
	 * @return
	 */
	public PageResult findPage(int pageNum, int pageSize);
	
	
	/**
	 * 增加
	*/
	public void add(TbItemCat itemCat);
	
	
	/**
	 * 修改
	 */
	public void update(TbItemCat itemCat);
	

	/**
	 * 根据ID获取实体
	 * @param id
	 * @return
	 */
	public TbItemCat findOne(Long id);
	
	
	/**
	 * 批量删除
	 * @param ids
	 */
	public void delete(Long[] ids);

	/**
	 * 分页
	 * @param pageNum 当前页 码
	 * @param pageSize 每页记录数
	 * @return
	 */
	public PageResult findPage(TbItemCat itemCat, int pageNum, int pageSize);

	/**
	 * 查询分类管理中的各个类
	 */

	public PageResult findByParentId(Long parentId, int pageNum, int pageSize);


	/**
	 * goods_edit页面中进行分类查询表ItemCat,不需要进行分页查询
	 * parentId 传入父类的id,进行分类查询
	 */

	public List<TbItemCat> findByParentIdOne(Long parentId);

}
