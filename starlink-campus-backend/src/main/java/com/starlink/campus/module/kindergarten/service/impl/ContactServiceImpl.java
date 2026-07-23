package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.starlink.campus.module.kindergarten.dto.ContactDTO;
import com.starlink.campus.module.kindergarten.entity.KgClass;
import com.starlink.campus.module.kindergarten.entity.KgStaff;
import com.starlink.campus.module.kindergarten.entity.KgStudent;
import com.starlink.campus.module.kindergarten.mapper.KgClassMapper;
import com.starlink.campus.module.kindergarten.mapper.KgStaffMapper;
import com.starlink.campus.module.kindergarten.mapper.KgStudentMapper;
import com.starlink.campus.module.kindergarten.service.ContactService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class ContactServiceImpl implements ContactService {

    @Autowired(required = false)
    private KgStaffMapper staffMapper;

    @Autowired(required = false)
    private KgStudentMapper studentMapper;

    @Autowired(required = false)
    private KgClassMapper classMapper;

    @Override
    public List<ContactDTO> getContactList() {
        return searchContacts(null);
    }

    @Override
    @org.springframework.cache.annotation.Cacheable(value = "contactList", key = "#keyword == null ? 'ALL' : #keyword")
    public List<ContactDTO> searchContacts(String keyword) {
        List<ContactDTO> contacts = new ArrayList<>();

        // 1. Query Staff
        if (staffMapper != null) {
            LambdaQueryWrapper<KgStaff> staffWrapper = new LambdaQueryWrapper<>();
            staffWrapper.select(KgStaff::getId, KgStaff::getName, KgStaff::getRoleType, KgStaff::getPhone);
            if (StringUtils.hasText(keyword)) {
                staffWrapper.like(KgStaff::getName, keyword);
            }
            List<KgStaff> staffs = staffMapper.selectList(staffWrapper);
            if (staffs != null) {
                for (KgStaff staff : staffs) {
                    ContactDTO dto = new ContactDTO();
                    dto.setName(staff.getName());
                    dto.setRole(staff.getRoleType());
                    dto.setClassName("");
                    dto.setMaskedPhone(maskPhone(staff.getPhone()));
                    dto.setOriginalId(staff.getId());
                    dto.setType("STAFF");
                    contacts.add(dto);
                }
            }
        }

        // 2. Query Students (Parents)
        if (studentMapper != null) {
            Map<Long, String> classMap = null;
            if (classMapper != null) {
                LambdaQueryWrapper<KgClass> classWrapper = new LambdaQueryWrapper<>();
                classWrapper.select(KgClass::getId, KgClass::getClassName);
                List<KgClass> classes = classMapper.selectList(classWrapper);
                if (classes != null) {
                    classMap = classes.stream().collect(Collectors.toMap(KgClass::getId, KgClass::getClassName));
                }
            }

            LambdaQueryWrapper<KgStudent> studentWrapper = new LambdaQueryWrapper<>();
            studentWrapper.select(KgStudent::getId, KgStudent::getName, KgStudent::getGuardianName, KgStudent::getGuardianPhone, KgStudent::getClassId);
            if (StringUtils.hasText(keyword)) {
                studentWrapper.and(w -> w.like(KgStudent::getGuardianName, keyword).or().like(KgStudent::getName, keyword));
            }
            List<KgStudent> students = studentMapper.selectList(studentWrapper);
            if (students != null) {
                for (KgStudent student : students) {
                    ContactDTO dto = new ContactDTO();
                    String relationName = StringUtils.hasText(student.getGuardianName()) ? student.getGuardianName() : student.getName() + "家长";
                    dto.setName(relationName);
                    dto.setRole("家长");
                    String cName = (classMap != null && classMap.containsKey(student.getClassId())) ? classMap.get(student.getClassId()) : "";
                    
                    if (StringUtils.hasText(keyword)) {
                        if (!relationName.contains(keyword) && !cName.contains(keyword) && !student.getName().contains(keyword)) {
                           // continue; // if we want exact search match, but already handled in DB wrapper mostly.
                        }
                    }
                    
                    dto.setClassName(cName);
                    dto.setMaskedPhone(maskPhone(student.getGuardianPhone()));
                    dto.setOriginalId(student.getId());
                    dto.setType("PARENT");
                    contacts.add(dto);
                }
            }
        }

        return contacts;
    }

    private String maskPhone(String phone) {
        if (!StringUtils.hasText(phone) || phone.length() < 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }
}
