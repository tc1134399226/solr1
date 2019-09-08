package com.qfjy.service.impl;


import com.qfjy.service.ProductsService;
import com.qfjy.bean.Products;
import com.qfjy.utils.PageUtils;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class ProductsServiceImpl implements ProductsService {
    
    @Autowired
    private HttpSolrClient httpSolrClient;



    public PageUtils search(String keyword, String catalogName, String priceStr, String psort, Integer currtPage, Integer pageSize) throws IOException, SolrServerException {

        PageUtils pageUtils=new PageUtils(0,pageSize,currtPage);

        SolrQuery query=new SolrQuery();
        //1.主页关键字判断
        if (StringUtils.isEmpty(keyword)){
            query.set("q","*:*");
        }else {
            query.set("q","prod_pname:"+keyword);
        }
        //2.类别筛选
        if (!StringUtils.isEmpty(catalogName)){
            query.addFilterQuery("prod_catalog_name:"+catalogName);
        }
        //3.价格过滤
        if (!StringUtils.isEmpty(priceStr)){
            String[] strs=priceStr.split("-");
            if (strs.length==2){
                if (StringUtils.isEmpty(strs[0])){
                    query.addFilterQuery("prod_price:[0 TO "+strs[1]+"]");
                }else{
                    query.addFilterQuery("prod_price:["+strs[0]+" TO "+strs[1]+"]");
                }
            }else {
                query.addFilterQuery("prod_price:["+strs[0]+" TO *]");
            }
        }

        //4.排序过程 1升序 2降序
        if (!StringUtils.isEmpty(psort)){
            if ("1".equals(psort)){
                query.addSort("prod_price",SolrQuery.ORDER.asc);
            }else if ("2".equals(psort)){
                query.addSort("prod_price",SolrQuery.ORDER.desc);
            }
        }

        //5.分页功能
        query.setStart(pageUtils.getOffset());
        query.setRows(pageUtils.getPageSize());

        //设置高亮功能
        query.setHighlight(true);//启动高亮
        query.addHighlightField("prod_pname");
        query.setHighlightSimplePre("<font color='red'>");
        query.setHighlightSimplePost("</font>");



        QueryResponse queryResponse=httpSolrClient.query(query);
        //高亮数据 替换 原文标题的内容
        Map<String, Map<String, List<String>>> map=queryResponse.getHighlighting();

        //总记录数
        long count=queryResponse.getResults().getNumFound();


        //得到查询结果
        List<Products> list=queryResponse.getBeans(Products.class);

        for (Products p : list) {

            Map<String,List<String>> map1=map.get(p.getPid());
            List<String> list1=map1.get("prod_pname");
            if (list1!=null){
                String resultPname=list1.get(0);
                p.setPname(resultPname);
            }
        }
        pageUtils=new PageUtils((int)count,pageSize,currtPage);
        pageUtils.setList(list);

        return pageUtils;
    }

}
