#!/bin/bash
BASE_DIR="/Users/oraclez/Desktop/zwf/StarLink Campus/starlink-campus-backend/src/main/java/com/starlink/campus/module/kindergarten"
ENTITY_DIR="$BASE_DIR/entity"
MAPPER_DIR="$BASE_DIR/mapper"
SERVICE_DIR="$BASE_DIR/service"
IMPL_DIR="$SERVICE_DIR/impl"

cat << 'JAVA' > "$ENTITY_DIR/KgStaffAttendance.java"
package com.starlink.campus.module.kindergarten.entity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@Data
@TableName("kg_staff_attendance")
public class KgStaffAttendance {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long staffId;
    private Date attendanceDate;
    private Date checkInTime;
    private Date checkOutTime;
    private String status;
    private Date createTime;
    
    // Explicit getters/setters just in case Lombok is wonky
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getStaffId() { return staffId; }
    public void setStaffId(Long staffId) { this.staffId = staffId; }
    public Date getAttendanceDate() { return attendanceDate; }
    public void setAttendanceDate(Date attendanceDate) { this.attendanceDate = attendanceDate; }
    public Date getCheckInTime() { return checkInTime; }
    public void setCheckInTime(Date checkInTime) { this.checkInTime = checkInTime; }
    public Date getCheckOutTime() { return checkOutTime; }
    public void setCheckOutTime(Date checkOutTime) { this.checkOutTime = checkOutTime; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Date getCreateTime() { return createTime; }
    public void setCreateTime(Date createTime) { this.createTime = createTime; }
}
JAVA

cat << 'JAVA' > "$MAPPER_DIR/KgStaffAttendanceMapper.java"
package com.starlink.campus.module.kindergarten.mapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.starlink.campus.module.kindergarten.entity.KgStaffAttendance;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface KgStaffAttendanceMapper extends BaseMapper<KgStaffAttendance> {}
JAVA

cat << 'JAVA' > "$SERVICE_DIR/KgStaffAttendanceService.java"
package com.starlink.campus.module.kindergarten.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.starlink.campus.module.kindergarten.entity.KgStaffAttendance;

public interface KgStaffAttendanceService extends IService<KgStaffAttendance> {}
JAVA

cat << 'JAVA' > "$IMPL_DIR/KgStaffAttendanceServiceImpl.java"
package com.starlink.campus.module.kindergarten.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.starlink.campus.module.kindergarten.entity.KgStaffAttendance;
import com.starlink.campus.module.kindergarten.mapper.KgStaffAttendanceMapper;
import com.starlink.campus.module.kindergarten.service.KgStaffAttendanceService;
import org.springframework.stereotype.Service;

@Service
public class KgStaffAttendanceServiceImpl extends ServiceImpl<KgStaffAttendanceMapper, KgStaffAttendance> implements KgStaffAttendanceService {}
JAVA

echo "Done"
