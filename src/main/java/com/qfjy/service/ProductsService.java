package com.qfjy.service;

import com.qfjy.utils.PageUtils;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;

public interface ProductsService {

    /**
     * 电商主页 京东
     * @param keyword
     * @param catalogName
     * @param priceStr
     * @param psort
     * @param currtPage
     * @param pageSize 每页大小
     * @return
     */
    public PageUtils search(String keyword, String catalogName, String priceStr, String psort,
                            Integer currtPage, Integer pageSize) throws IOException, SolrServerException ;
}
