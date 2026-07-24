package com.starlink.campus.module.kindergarten.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("kg_development_assessment")
public class KgDevelopmentAssessment {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long studentId;
    private String term;
    private java.math.BigDecimal healthScore;
    private java.math.BigDecimal languageScore;
    private java.math.BigDecimal socialScore;
    private java.math.BigDecimal scienceScore;
    private java.math.BigDecimal artScore;
    private String teacherEvaluation;
    private Long teacherId;
    private java.time.LocalDateTime createTime;
    private java.time.LocalDateTime updateTime;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getStudentId() {
        return studentId;
    }
    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }
    public String getTerm() {
        return term;
    }
    public void setTerm(String term) {
        this.term = term;
    }
    public java.math.BigDecimal getHealthScore() {
        return healthScore;
    }
    public void setHealthScore(java.math.BigDecimal healthScore) {
        this.healthScore = healthScore;
    }
    public java.math.BigDecimal getLanguageScore() {
        return languageScore;
    }
    public void setLanguageScore(java.math.BigDecimal languageScore) {
        this.languageScore = languageScore;
    }
    public java.math.BigDecimal getSocialScore() {
        return socialScore;
    }
    public void setSocialScore(java.math.BigDecimal socialScore) {
        this.socialScore = socialScore;
    }
    public java.math.BigDecimal getScienceScore() {
        return scienceScore;
    }
    public void setScienceScore(java.math.BigDecimal scienceScore) {
        this.scienceScore = scienceScore;
    }
    public java.math.BigDecimal getArtScore() {
        return artScore;
    }
    public void setArtScore(java.math.BigDecimal artScore) {
        this.artScore = artScore;
    }
    public String getTeacherEvaluation() {
        return teacherEvaluation;
    }
    public void setTeacherEvaluation(String teacherEvaluation) {
        this.teacherEvaluation = teacherEvaluation;
    }
    public Long getTeacherId() {
        return teacherId;
    }
    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }
    public java.time.LocalDateTime getCreateTime() {
        return createTime;
    }
    public void setCreateTime(java.time.LocalDateTime createTime) {
        this.createTime = createTime;
    }
    public java.time.LocalDateTime getUpdateTime() {
        return updateTime;
    }
    public void setUpdateTime(java.time.LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}
