package com.starlink.campus.module.kindergarten.entity;

import com.baomidou.mybatisplus.annotation.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@TableName("kg_weekly_menu")
public class KgWeeklyMenu implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @TableId(type = IdType.AUTO)
    private Long id;
    private LocalDate weekStartDate;
    private Integer dayOfWeek;      // 1-7
    private String mealType;        // 早餐/午餐/午点/晚餐
    private String dishes;          // JSON数组
    private String nutritionNote;
    private String allergenWarning;
    private String publishStatus;   // 草稿/已发布
    private Long createdBy;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getWeekStartDate() { return weekStartDate; }
    public void setWeekStartDate(LocalDate weekStartDate) { this.weekStartDate = weekStartDate; }

    public Integer getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(Integer dayOfWeek) { this.dayOfWeek = dayOfWeek; }

    public String getMealType() { return mealType; }
    public void setMealType(String mealType) { this.mealType = mealType; }

    public String getDishes() { return dishes; }
    public void setDishes(String dishes) { this.dishes = dishes; }

    public String getNutritionNote() { return nutritionNote; }
    public void setNutritionNote(String nutritionNote) { this.nutritionNote = nutritionNote; }

    public String getAllergenWarning() { return allergenWarning; }
    public void setAllergenWarning(String allergenWarning) { this.allergenWarning = allergenWarning; }

    public String getPublishStatus() { return publishStatus; }
    public void setPublishStatus(String publishStatus) { this.publishStatus = publishStatus; }

    public Long getCreatedBy() { return createdBy; }
    public void setCreatedBy(Long createdBy) { this.createdBy = createdBy; }

    public LocalDateTime getCreateTime() { return createTime; }
    public void setCreateTime(LocalDateTime createTime) { this.createTime = createTime; }
}
