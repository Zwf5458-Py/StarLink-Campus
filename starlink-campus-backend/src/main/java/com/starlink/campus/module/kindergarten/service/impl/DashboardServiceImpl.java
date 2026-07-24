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
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.StringRedisTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;

@Service
public class DashboardServiceImpl implements DashboardService {

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DashboardServiceImpl.class);

    @Autowired
    private KgStudentMapper studentMapper;

    @Autowired
    private KgStudentAttendanceMapper attendanceMapper;

    @Autowired
    private KgMorningCheckMapper morningCheckMapper;

    @Autowired
    private KgClassMapper classMapper;

    @Autowired
    private KgClassBoardConfigMapper boardConfigMapper;

    @Autowired
    private KgPatrolRecordMapper patrolRecordMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Map<String, Object> getDashboardStats() {
        String cacheKey = "dashboardStats:latest";
        try {
            String cached = stringRedisTemplate.opsForValue().get(cacheKey);
            if (cached != null) {
                return objectMapper.readValue(cached, new TypeReference<Map<String, Object>>(){});
            }
        } catch (Exception e) {
            log.error("读取 Dashboard 缓存失败", e);
        }
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
        Long onlineBoardCount = totalBoardCount;

        // 6. 巡检完成率
        Long totalPatrols = patrolRecordMapper != null ? patrolRecordMapper.selectCount(null) : 0L;
        Long completedPatrols = patrolRecordMapper != null ? patrolRecordMapper.selectCount(new QueryWrapper<KgPatrolRecord>().eq("is_normal", 1)) : 0L;
        double patrolRate = totalPatrols == 0 ? 100.0 : Math.round((double) completedPatrols / totalPatrols * 1000.0) / 10.0;

        // 7. 班级考勤明细列表 SQL 聚合 (解决 N+1 问题)
        List<Map<String, Object>> classDetails = new ArrayList<>();
        
        Map<Long, Map<String, Object>> classStudentCounts = studentMapper != null ? studentMapper.countStudentsByClass() : new HashMap<>();
        Map<Long, Map<String, Object>> classAttendanceCounts = attendanceMapper != null ? attendanceMapper.countAttendanceByClass(today) : new HashMap<>();

        List<KgClass> classList = classMapper != null ? classMapper.selectList(null) : new ArrayList<>();
        for (KgClass cls : classList) {
            Long clsId = cls.getId();
            
            int clsTotal = 0;
            if (classStudentCounts.containsKey(clsId) && classStudentCounts.get(clsId).get("count") != null) {
                clsTotal = ((Number) classStudentCounts.get(clsId).get("count")).intValue();
            }
            
            int clsPresent = 0;
            if (classAttendanceCounts.containsKey(clsId) && classAttendanceCounts.get(clsId).get("count") != null) {
                clsPresent = ((Number) classAttendanceCounts.get(clsId).get("count")).intValue();
            }
            
            int leaveCount = Math.max(0, clsTotal - clsPresent);
            classDetails.add(createClassMap(cls.getClassName(), cls.getGradeLevel(), clsTotal, clsPresent, leaveCount, "全部正常"));
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

        try {
            stringRedisTemplate.opsForValue().set(cacheKey, objectMapper.writeValueAsString(data), 60, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.error("写入 Dashboard 缓存失败", e);
        }

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
