package com.springboot.eft.entity;

import com.springboot.eft.utils.BeanUtils;

import java.sql.Timestamp;

/**
 * 分类表
 */
public class Category {

    private int id;

    /**
     * 分类名称
     */
    private String name;

    private Timestamp createTime;

    public Category(String name) {
        this.name = name;
    }

    public Category(int id, String name, Timestamp createTime) {
        this.id = id;
        this.name = name;
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return BeanUtils.toPrettyJson(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
