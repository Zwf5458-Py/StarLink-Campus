package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.starlink.campus.module.kindergarten.entity.KgEnrollment;
import com.starlink.campus.module.kindergarten.mapper.KgEnrollmentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EnrollmentServiceImplTest {

    @Mock
    private KgEnrollmentMapper enrollmentMapper;

    @InjectMocks
    private EnrollmentServiceImpl enrollmentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        org.springframework.test.util.ReflectionTestUtils.setField(enrollmentService, "baseMapper", enrollmentMapper);
    }

    @Test
    void testAddEnrollment() {
        KgEnrollment enrollment = new KgEnrollment();
        when(enrollmentMapper.insert(any(KgEnrollment.class))).thenReturn(1);
        
        boolean result = enrollmentService.add(enrollment);
        
        assertTrue(result);
        assertEquals("意向", enrollment.getStatus());
        verify(enrollmentMapper, times(1)).insert(enrollment);
    }

    @Test
    void testGetFunnelStats() {
        Map<String, Map<String, Object>> mockCounts = new HashMap<>();
        
        Map<String, Object> intentCount = new HashMap<>();
        intentCount.put("count", 100L);
        mockCounts.put("意向", intentCount);
        
        Map<String, Object> admittedCount = new HashMap<>();
        admittedCount.put("count", 50L);
        mockCounts.put("已录取", admittedCount);

        when(enrollmentMapper.countByStatus()).thenReturn(mockCounts);
        
        Map<String, Object> stats = enrollmentService.getFunnelStats();
        
        assertNotNull(stats);
        assertEquals(150L, stats.get("total"));
        assertEquals(100L, stats.get("意向"));
        assertEquals(50L, stats.get("已录取"));
        assertEquals(33.3, (Double) stats.get("conversionRate"), 0.1);
    }
}
