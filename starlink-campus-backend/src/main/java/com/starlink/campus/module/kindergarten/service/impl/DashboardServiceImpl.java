package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.starlink.campus.module.kindergarten.entity.KgClass;
import com.starlink.campus.module.kindergarten.entity.KgMorningCheck;
import com.starlink.campus.module.kindergarten.entity.KgStudent;
import com.starlink.campus.module.kindergarten.entity.KgStudentAttendance;
import com.starlink.campus.module.kindergarten.entity.KgClassBoardConfig;
import com.starlink.campus.module.kindergarten.entity.KgPatrolRecord;
import com.starlink.campus.module.kindergarten.mapper.KgClassMapper;
import com.starlink.campus.module.kindergarten.mapper.KgMorningCheckMapper;
import com.starlink.campus.module.kindergarten.mapper.KgStudentAttendanceMapper;
import com.starlink.campus.module.kindergarten.mapper.KgStudentMapper;
import com.starlink.campus.module.kindergarten.mapper.KgClassBoardConfigMapper;
import com.starlink.campus.module.kindergarten.mapper.KgPatrolRecordMapper;
import com.starlink.campus.module.kindergarten.service.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class DashboardServiceImpl implements DashboardService {

    @Autowired(required = false)
    private KgStudentMapper studentMapper;

    @Autowired(required = false)
    private KgStudentAttendanceMapper attendanceMapper;

    @Autowired(required = false)
    private KgMorningCheckMapper morningCheckMapper;

    @Autowired(required = false)
    private KgClassMapper classMapper;

    @Autowired(required = false)
    private KgClassBoardConfigMapper boardConfigMapper;

    @Autowired(required = false)
    private KgPatrolRecordMapper patrolRecordMapper;

    @Override
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> data = new HashMap<>();
        java.util.Date today = java.util.Date.from(LocalDate.now().atStartOfDay(java.time.ZoneId.systemDefault()).toInstant());

        // 1. 全园学生总数
        Long totalStudents = studentMapper != null ? studentMapper.selectCount(new QueryWrapper<KgStudent>().eq("status", 1)) : 0L;

        // 2. 今日实到人数
        Long presentStudents = attendanceMapper != null ? 
                attendanceMapper.selectCount(new QueryWrapper<KgStudentAttendance>().eq("attendance_date", today)) : 0L;

        // 3. 实时出勤率
        double attendanceRate = totalStudents == 0 ? 0.0 : Math.round((double) presentStudents / totalStudents * 1000.0) / 10.0;

        // 4. 晨检发热人数
        Long feverCount = morningCheckMapper != null ?
                morningCheckMapper.selectCount(new QueryWrapper<KgMorningCheck>().eq("check_date", today).eq("is_fever", 1)) : 0L;

        // 5. 班牌在线统计
        Long totalBoardCount = boardConfigMapper != null ? boardConfigMapper.selectCount(null) : 0L;
        Long onlineBoardCount = boardConfigMapper != null ? boardConfigMapper.selectCount(new QueryWrapper<KgClassBoardConfig>().eq("status", 1)) : 0L;

        // 6. 巡检完成率
        Long totalPatrols = patrolRecordMapper != null ? patrolRecordMapper.selectCount(null) : 0L;
        Long completedPatrols = patrolRecordMapper != null ? patrolRecordMapper.selectCount(new QueryWrapper<KgPatrolRecord>().eq("status", "COMPLETED")) : 0L;
        double patrolRate = totalPatrols == 0 ? 0.0 : Math.round((double) completedPatrols / totalPatrols * 1000.0) / 10.0;

        // 7. 班级考勤明细列表 SQL 聚合
        List<Map<String, Object>> classDetails = new ArrayList<>();
        List<KgClass> classList = classMapper != null ? classMapper.selectList(null) : new ArrayList<>();
        
        for (KgClass cls : classList) {
            Long clsTotal = studentMapper != null ? studentMapper.selectCount(new QueryWrapper<KgStudent>().eq("class_id", cls.getId())) : 0L;
            Long clsPresent = attendanceMapper != null ? attendanceMapper.selectCount(new QueryWrapper<KgStudentAttendance>().eq("class_id", cls.getId()).eq("attendance_date", today)) : 0L;
            long leaveCount = Math.max(0, clsTotal - clsPresent);
            classDetails.add(createClassMap(cls.getClassName(), cls.getGradeLevel(), clsTotal.intValue(), clsPresent.intValue(), (int) leaveCount, "全部正常"));
        }

        // 8. 8 大看板数据注入
        data.put("totalStudents", totalStudents);
        data.put("presentStudents", presentStudents);
        data.put("attendanceRate", attendanceRate);
        data.put("feverCount", feverCount);
        data.put("onlineBoardCount", onlineBoardCount);
        data.put("totalBoardCount", totalBoardCount);
        data.put("patrolRate", patrolRate);
        data.put("classList", classDetails);
        data.put("lastUpdatedTime", java.time.LocalDateTime.now().toString());

        return data;
    }

    private Map<String, Object> createClassMap(String name, String grade, int total, int present, int leave, String healthStatus) {
        Map<String, Object> m = new HashMap<>();
        m.put("className", name);
        m.put("gradeLevel", grade);
        m.put("totalCount", total);
        m.put("presentCount", present);
        m.put("leaveCount", leave);
        m.put("healthStatus", healthStatus);
        return m;
    }
}
