package com.starlink.campus.module.kindergarten.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("kg_referral_record")
public class KgReferralRecord {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long referrerParentId;
    private String newFamilyName;
    private String contactPhone;
    private String referralStatus;
    private String rewardStatus;
    private java.time.LocalDateTime createTime;
    private java.time.LocalDateTime updateTime;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getReferrerParentId() {
        return referrerParentId;
    }
    public void setReferrerParentId(Long referrerParentId) {
        this.referrerParentId = referrerParentId;
    }
    public String getNewFamilyName() {
        return newFamilyName;
    }
    public void setNewFamilyName(String newFamilyName) {
        this.newFamilyName = newFamilyName;
    }
    public String getContactPhone() {
        return contactPhone;
    }
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }
    public String getReferralStatus() {
        return referralStatus;
    }
    public void setReferralStatus(String referralStatus) {
        this.referralStatus = referralStatus;
    }
    public String getRewardStatus() {
        return rewardStatus;
    }
    public void setRewardStatus(String rewardStatus) {
        this.rewardStatus = rewardStatus;
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
