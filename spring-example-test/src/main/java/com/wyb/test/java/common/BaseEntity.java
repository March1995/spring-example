package com.wyb.test.java.common;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Kunzite
 */
public class BaseEntity implements Serializable {

    private static final long serialVersionUID = -1618357536992740779L;

    protected Long id;

    protected Integer isDelete;

    protected Date createTime;

    protected Date updateTime;

    public BaseEntity(Long id) {
        this.id = id;
    }

    public BaseEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public static BaseEntity parseConfig(String str) {
        return JSONObject.parseObject(str, BaseEntity.class);
    }
}
