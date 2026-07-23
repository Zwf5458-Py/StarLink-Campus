package com.starlink.campus.module.kindergarten.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.starlink.campus.module.kindergarten.entity.KgStudentAttendance;
import com.starlink.campus.module.kindergarten.mapper.KgStudentAttendanceMapper;
import com.starlink.campus.module.kindergarten.service.impl.StudentAttendanceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StudentAttendanceServiceImplTest {

    @Mock
    private KgStudentAttendanceMapper attendanceMapper;

    @InjectMocks
    private StudentAttendanceServiceImpl attendanceService;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(attendanceService, "absentThreshold", 5L);
        ReflectionTestUtils.setField(attendanceService, "dailyRefundAmount", 20L);
    }

    @Test
    public void testCheckIn_ExistingRecord_UpdatesRecord() {
        // 测试当日已有记录时更新而非新建
        Long studentId = 1L;
        KgStudentAttendance existing = new KgStudentAttendance();
        existing.setId(100L);
        existing.setStudentId(studentId);

        when(attendanceMapper.selectOne(any(QueryWrapper.class))).thenReturn(existing);

        attendanceService.checkIn(studentId, "FACE", new BigDecimal("36.5"));

        verify(attendanceMapper, times(1)).updateById(existing);
        verify(attendanceMapper, never()).insert(any(KgStudentAttendance.class));
        assertNotNull(existing.getCheckInTime());
        assertEquals(36.5, existing.getCheckInTemperature());
        assertNotEquals("EXCEPTIONAL", existing.getStatus());
    }

    @Test
    public void testCalculateRefund_AboveThreshold_TriggersRefund() {
        // 测试缺勤天数 > 5 时触发退费
        Long studentId = 1L;
        when(attendanceMapper.selectCount(any(QueryWrapper.class))).thenReturn(6L); // 6天缺勤，大于阈值5

        BigDecimal refund = attendanceService.calculateRefund(studentId, "2023-10");

        assertEquals(new BigDecimal("120"), refund, "Refund should be 6 * 20 = 120");
    }
    
    @Test
    public void testCalculateRefund_BelowThreshold_NoRefund() {
        // 测试缺勤天数 <= 5 时不触发退费
        Long studentId = 1L;
        when(attendanceMapper.selectCount(any(QueryWrapper.class))).thenReturn(5L); // 5天缺勤，不大于阈值5

        BigDecimal refund = attendanceService.calculateRefund(studentId, "2023-10");

        assertEquals(BigDecimal.ZERO, refund, "Refund should be 0 when absences <= threshold");
    }

    @Test
    public void testGetClassSummary_ReturnsCorrectData() {
        // 测试返回正确的班级考勤统计数据
        Long classId = 1L;
        LocalDate date = LocalDate.of(2023, 10, 1);
        
        KgStudentAttendance a1 = new KgStudentAttendance();
        KgStudentAttendance a2 = new KgStudentAttendance();
        when(attendanceMapper.selectList(any(QueryWrapper.class))).thenReturn(Arrays.asList(a1, a2));

        Map<String, Object> summary = attendanceService.getClassSummary(classId, date);

        assertEquals(2, summary.get("presentCount"));
        assertEquals("2023-10-01", summary.get("date"));
        assertEquals(classId, summary.get("classId"));
    }
}
