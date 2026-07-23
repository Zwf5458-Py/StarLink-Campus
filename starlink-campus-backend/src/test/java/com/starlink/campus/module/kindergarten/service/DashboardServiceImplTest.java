package com.starlink.campus.module.kindergarten.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.starlink.campus.module.kindergarten.entity.KgClass;
import com.starlink.campus.module.kindergarten.mapper.*;
import com.starlink.campus.module.kindergarten.service.impl.DashboardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DashboardServiceImplTest {

    @Mock
    private KgStudentMapper studentMapper;

    @Mock
    private KgStudentAttendanceMapper attendanceMapper;

    @Mock
    private KgMorningCheckMapper morningCheckMapper;

    @Mock
    private KgClassMapper classMapper;

    @Mock
    private KgClassBoardConfigMapper boardConfigMapper;

    @Mock
    private KgPatrolRecordMapper patrolRecordMapper;

    @InjectMocks
    private DashboardServiceImpl dashboardService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testGetDashboardStats_NormalData() {
        // 模拟各 Mapper 的返回值
        when(studentMapper.selectCount(any(QueryWrapper.class))).thenReturn(100L);
        when(attendanceMapper.selectCount(any(QueryWrapper.class))).thenReturn(90L);
        when(morningCheckMapper.selectCount(any(QueryWrapper.class))).thenReturn(2L);
        when(boardConfigMapper.selectCount(null)).thenReturn(10L);
        when(boardConfigMapper.selectCount(any(QueryWrapper.class))).thenReturn(8L);
        when(patrolRecordMapper.selectCount(null)).thenReturn(5L);
        when(patrolRecordMapper.selectCount(any(QueryWrapper.class))).thenReturn(4L);

        KgClass cls1 = new KgClass();
        cls1.setId(1L);
        cls1.setClassName("大一班");
        cls1.setGradeLevel("大班");
        when(classMapper.selectList(null)).thenReturn(Arrays.asList(cls1));

        // 调用目标方法
        Map<String, Object> stats = dashboardService.getDashboardStats();

        // 验证结果
        assertNotNull(stats);
        assertEquals(100L, stats.get("totalStudents"), "Total students should be 100");
        assertEquals(90L, stats.get("presentStudents"), "Present students should be 90");
        assertEquals(90.0, stats.get("attendanceRate"), "Attendance rate should be 90.0%");
        assertEquals(2L, stats.get("feverCount"), "Fever count should be 2");
        assertEquals(10L, stats.get("totalBoardCount"), "Total board count should be 10");
        assertEquals(8L, stats.get("onlineBoardCount"), "Online board count should be 8");
        assertEquals(80.0, stats.get("patrolRate"), "Patrol rate should be 80.0%");
        
        List<Map<String, Object>> classList = (List<Map<String, Object>>) stats.get("classList");
        assertEquals(1, classList.size(), "Should have 1 class detail");
        assertNotNull(stats.get("lastUpdatedTime"));
    }

    @Test
    public void testGetDashboardStats_NoStudents_AttendanceRateZero() {
        // 当无学生时出勤率应为 0
        when(studentMapper.selectCount(any(QueryWrapper.class))).thenReturn(0L);
        
        Map<String, Object> stats = dashboardService.getDashboardStats();
        
        assertEquals(0L, stats.get("totalStudents"));
        assertEquals(0.0, stats.get("attendanceRate"), "Attendance rate should be 0.0 when there are no students");
    }
}
