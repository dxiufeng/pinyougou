package com.pinyougou.search.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.pinyougou.search.service.ItemSearchService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/itemSearch")
public class ItemSearchController {


    @Reference
    private ItemSearchService itemSearchService;

    //查询索引库
    @RequestMapping("/search")
    public Map search(@RequestBody Map map){
        Map search = itemSearchService.search(map);
        return search;



    }
}
