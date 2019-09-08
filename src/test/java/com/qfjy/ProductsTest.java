package com.qfjy;

import com.qfjy.bean.Products;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class ProductsTest {

    @Test
    public void addOrUpdate() throws IOException, SolrServerException {
        //1 得到Solr 服务器地址
        String solrUrl = "http://localhost:9080/solr/tc_core";

        //2 创建客户端连接
        HttpSolrClient solrClient = new HttpSolrClient.Builder(solrUrl).build();

        //3 增加或修改
        Products p = new Products();
        p.setPid("9999999");
        p.setCatalogName("测试数据1");
        p.setDescription("测试内容描述1");
        p.setPname("添加一个商品测试1");
        p.setPicture("1.jpg");
        p.setPrice(12.5);

        //4 将该对象添加到Solr索引库中
        UpdateResponse updateResponse = solrClient.addBean(p);
        System.out.println("solr J 执行成功");

        //5 提交事务
        solrClient.commit();
        //6 客户端关闭
        solrClient.close();
    }





    @Test
    public void deleteById() throws IOException, SolrServerException {

        //1 得到Solr服务器
        String solrUrl = "http://localhost:9080/solr/tc_core";
        //2 创建客户端链接
        HttpSolrClient solrClient = new HttpSolrClient.Builder(solrUrl).build();

        //3 执行删除操作
//        UpdateResponse response = solrClient.deleteById("9999999");
        solrClient.deleteByQuery("*:*");
        System.out.println("solr J 执行成功");

        //4 提交事务
        solrClient.commit();
        //5 客户端关闭
        solrClient.close();
    }

    /**
     * 普通查询
     * @throws IOException
     * @throws SolrServerException
     */

    @Test
    public void search() throws IOException, SolrServerException {
        //1 得到Solr服务器
        String solrUrl = "http://localhost:9080/solr/tc_core";
        //2 创建客户端链接
        HttpSolrClient solrClient = new HttpSolrClient.Builder(solrUrl).build();

        //3 执行查询操作
        SolrQuery solrQuery = new SolrQuery("*:*");
        QueryResponse query = solrClient.query(solrQuery);
        List<Products> list = query.getBeans(Products.class);
        for (Products products : list) {
            System.out.println(products.getPid()+":"+products.getPname());
        }

    }


    /**
     * 复杂查询
     * @throws IOException
     * @throws SolrServerException
     */

    @Test
    public void complexSearch() throws IOException, SolrServerException {
        //1 得到Solr服务器
        String solrUrl = "http://localhost:9080/solr/tc_core";
        //2 创建客户端链接
        HttpSolrClient solrClient = new HttpSolrClient.Builder(solrUrl).build();

        //3 执行查询操作
        // q
//        SolrQuery solrQuery = new SolrQuery("*:*");
        SolrQuery solrQuery = new SolrQuery();
        solrQuery.set("q","手机");

        // fq
        solrQuery.setFilterQueries("prod_catalog_name:手机饰品");
        solrQuery.addFilterQuery("prod_price:[5 TO *]");

        //sort 排序
        solrQuery.addSort("prod_price", SolrQuery.ORDER.desc);

        //分页
        // start偏移量 rows 页数大小
        solrQuery.setStart(0);
        solrQuery.setRows(9);

        //fl 回显
//        solrQuery.setFields("prod_pname");
//        solrQuery.set("fl","prod_pname");

        //df 默认域设置
        solrQuery.set("df","prod_pname");

        //hl 高亮设置
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("prod_pname");
        solrQuery.setHighlightSimplePre("<font color='red'>");
        solrQuery.setHighlightSimplePost("</font>");

        //查询
        QueryResponse query = solrClient.query(solrQuery);
        Map<String, Map<String, List<String>>> highlighting = query.getHighlighting();
        //数据长度获取
        long numFound = query.getResults().getNumFound();
        System.out.println(numFound);
        List<Products> list = query.getBeans(Products.class);
        for (Products products : list) {
            Map<String, List<String>> stringListMap = highlighting.get(products.getPid());
            List<String> strings = stringListMap.get("prod_pname");
            String result = strings.get(0);
            products.setPname(result);
            System.out.println(products.getPid()+":"+products.getPname()+"\t"+products.getPrice());
        }

    }
}
