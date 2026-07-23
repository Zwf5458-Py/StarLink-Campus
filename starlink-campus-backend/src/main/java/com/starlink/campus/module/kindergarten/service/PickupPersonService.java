package com.starlink.campus.module.kindergarten.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.starlink.campus.module.kindergarten.entity.KgPickupPerson;

import java.util.List;

public interface PickupPersonService extends IService<KgPickupPerson> {
    Page<KgPickupPerson> listByStudent(Long studentId, Integer pageNum, Integer pageSize);
    void addPickupPerson(KgPickupPerson person);  // status=待审核
    void approvePickupPerson(Long id);             // status=有效
    void disablePickupPerson(Long id);             // status=停用
    boolean verifyPickup(Long studentId, String identifier);  // IC卡号或人脸ID校验
    void triggerUnauthorizedAlert(Long studentId, String unknownIdentifier);  // 非绑定人员预警
    List<KgPickupPerson> getActiveByStudent(Long studentId);  // 获取有效接送人
}
