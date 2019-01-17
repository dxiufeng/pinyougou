package com.pinyougou.page.service;

/**
 * 商品详细页接口
 */
public interface ItemPageService {

    /**
     * 生成静态页面
     *
     */
    public boolean getItemHtml(Long goodsId);
}
