package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.starlink.campus.module.kindergarten.entity.KgPickupPerson;
import com.starlink.campus.module.kindergarten.mapper.KgPickupPersonMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PickupPersonServiceImplTest {

    @Mock
    private KgPickupPersonMapper pickupPersonMapper;

    @InjectMocks
    private PickupPersonServiceImpl pickupPersonService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        org.springframework.test.util.ReflectionTestUtils.setField(pickupPersonService, "baseMapper", pickupPersonMapper);
    }

    @Test
    void testVerifyPickup() {
        when(pickupPersonMapper.selectCount(any(LambdaQueryWrapper.class))).thenReturn(1L);
        
        boolean result = pickupPersonService.verifyPickup(1L, "TEST_IDENTIFIER");
        
        assertTrue(result);
        verify(pickupPersonMapper, times(1)).selectCount(any(LambdaQueryWrapper.class));
    }

    @Test
    void testGetActiveByStudent() {
        KgPickupPerson person = new KgPickupPerson();
        person.setId(1L);
        person.setStatus("有效");
        
        when(pickupPersonMapper.selectList(any(LambdaQueryWrapper.class))).thenReturn(Collections.singletonList(person));
        
        List<KgPickupPerson> list = pickupPersonService.getActiveByStudent(1L);
        
        assertNotNull(list);
        assertEquals(1, list.size());
        assertEquals("有效", list.get(0).getStatus());
        verify(pickupPersonMapper, times(1)).selectList(any(LambdaQueryWrapper.class));
    }
}
