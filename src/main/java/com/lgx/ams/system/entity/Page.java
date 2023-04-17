package com.lgx.ams.system.entity;

import lombok.Data;

@Data
public class Page {

    public int total;
    public int current;
    public int size;
    public int pages;
    public int records;


//    System.out.println("总条数 ------> " + page.getTotal());
//    System.out.println("当前页数 ------> " + page.getCurrent());
//    System.out.println("当前每页显示数 ------> " + page.getSize());
//    System.out.println("总页数 ------> " + page.getPages());
//    System.out.println("当前页面的文档数据 ------> " + page.getRecords());
}
