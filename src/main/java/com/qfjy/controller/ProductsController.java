package com.qfjy.controller;

import com.qfjy.service.ProductsService;
import com.qfjy.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping
public class ProductsController {

    @Autowired
    private ProductsService productsService;

    @RequestMapping("search")
    public ModelAndView search(@RequestParam(value = "keyword",required = false) String keyword,
                               @RequestParam(value = "catalogName",required = false)String catalogName,
                               @RequestParam(value = "priceStr",required = false)String priceStr,
                               @RequestParam(value = "psort",required = false)String psort,
                               @RequestParam(value = "currtPage",defaultValue = "1",required = false)Integer currtPage){
        ModelAndView model = new ModelAndView();

        try {
            PageUtils pageUtils = productsService.search(keyword, catalogName, priceStr, psort, currtPage, 9);
            System.out.println("controllor ------------>"+pageUtils);
            model.addObject("pageList",pageUtils);
            model.addObject("keyword",keyword);
            model.addObject("catalogName",catalogName);
            model.addObject("psort",psort);
            model.addObject("priceStr",priceStr);
            model.addObject("currtPage",currtPage);

        } catch (Exception e) {
            System.out.println(e.getMessage()+"\t ---------------->solr 服务器无法访问");
            e.printStackTrace();
        }

        model.setViewName("/jd.jsp");
        return model;
    }
}
