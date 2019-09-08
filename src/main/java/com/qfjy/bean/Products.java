package com.qfjy.bean;

import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;
import org.apache.zookeeper.data.Id;

import java.util.Date;

@Data
public class Products {

    @Field("id")
    private String pid;

    @Field("prod_pname")
    private String pname;

    private Integer catalog;

    @Field("prod_catalog_name")
    private String catalogName;

    @Field("prod_price")
    private Double price;

    private Integer number;

    @Field("prod_picture")
    private String picture;

    private Date releaseTime;

    @Field("prod_description")
    private String description;


}