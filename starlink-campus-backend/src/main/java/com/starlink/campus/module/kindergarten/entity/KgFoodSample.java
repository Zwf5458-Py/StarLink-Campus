package com.starlink.campus.module.kindergarten.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("kg_food_sample")
public class KgFoodSample {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String mealType;
    private String dishName;
    private java.math.BigDecimal sampleWeight;
    private Long samplerId;
    private String fridgeNo;
    private java.time.LocalDateTime sampleTime;
    private java.time.LocalDateTime destroyTime;
    private Long destroyerId;
    private Long supplierId;
    private java.time.LocalDateTime createTime;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getMealType() {
        return mealType;
    }
    public void setMealType(String mealType) {
        this.mealType = mealType;
    }
    public String getDishName() {
        return dishName;
    }
    public void setDishName(String dishName) {
        this.dishName = dishName;
    }
    public java.math.BigDecimal getSampleWeight() {
        return sampleWeight;
    }
    public void setSampleWeight(java.math.BigDecimal sampleWeight) {
        this.sampleWeight = sampleWeight;
    }
    public Long getSamplerId() {
        return samplerId;
    }
    public void setSamplerId(Long samplerId) {
        this.samplerId = samplerId;
    }
    public String getFridgeNo() {
        return fridgeNo;
    }
    public void setFridgeNo(String fridgeNo) {
        this.fridgeNo = fridgeNo;
    }
    public java.time.LocalDateTime getSampleTime() {
        return sampleTime;
    }
    public void setSampleTime(java.time.LocalDateTime sampleTime) {
        this.sampleTime = sampleTime;
    }
    public java.time.LocalDateTime getDestroyTime() {
        return destroyTime;
    }
    public void setDestroyTime(java.time.LocalDateTime destroyTime) {
        this.destroyTime = destroyTime;
    }
    public Long getDestroyerId() {
        return destroyerId;
    }
    public void setDestroyerId(Long destroyerId) {
        this.destroyerId = destroyerId;
    }
    public Long getSupplierId() {
        return supplierId;
    }
    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }
    public java.time.LocalDateTime getCreateTime() {
        return createTime;
    }
    public void setCreateTime(java.time.LocalDateTime createTime) {
        this.createTime = createTime;
    }
}
