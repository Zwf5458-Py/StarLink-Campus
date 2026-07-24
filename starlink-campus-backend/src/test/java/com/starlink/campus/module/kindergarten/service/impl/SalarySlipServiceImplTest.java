package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.starlink.campus.module.kindergarten.entity.KgSalarySlip;
import com.starlink.campus.module.kindergarten.mapper.KgSalarySlipMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SalarySlipServiceImplTest {

    @Mock
    private KgSalarySlipMapper salarySlipMapper;

    @InjectMocks
    private SalarySlipServiceImpl salarySlipService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        org.springframework.test.util.ReflectionTestUtils.setField(salarySlipService, "baseMapper", salarySlipMapper);
    }

    @Test
    void testListByStaff() {
        Page<KgSalarySlip> page = new Page<>(1, 10);
        when(salarySlipMapper.selectPage(any(Page.class), any(LambdaQueryWrapper.class))).thenReturn(page);
        
        Page<KgSalarySlip> result = salarySlipService.listByStaff(1L, 1, 10);
        
        assertNotNull(result);
        verify(salarySlipMapper, times(1)).selectPage(any(Page.class), any(LambdaQueryWrapper.class));
    }

    @Test
    void testAddSlip() {
        KgSalarySlip slip = new KgSalarySlip();
        when(salarySlipMapper.insert(any(KgSalarySlip.class))).thenReturn(1);
        
        boolean result = salarySlipService.addSlip(slip);
        
        assertTrue(result);
        assertEquals("待发布", slip.getPublishStatus());
        assertNotNull(slip.getCreateTime());
        verify(salarySlipMapper, times(1)).insert(slip);
    }

    @Test
    void testPublishSlip() {
        when(salarySlipMapper.updateById(any(KgSalarySlip.class))).thenReturn(1);
        
        boolean result = salarySlipService.publishSlip(1L);
        
        assertTrue(result);
        verify(salarySlipMapper, times(1)).updateById(any(KgSalarySlip.class));
    }
}
