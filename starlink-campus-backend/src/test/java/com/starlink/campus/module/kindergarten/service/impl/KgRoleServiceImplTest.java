package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.starlink.campus.module.kindergarten.entity.KgRole;
import com.starlink.campus.module.kindergarten.entity.KgStaffRole;
import com.starlink.campus.module.kindergarten.mapper.KgRoleMapper;
import com.starlink.campus.module.kindergarten.mapper.KgStaffRoleMapper;
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

class KgRoleServiceImplTest {

    @Mock
    private KgRoleMapper roleMapper;

    @Mock
    private KgStaffRoleMapper staffRoleMapper;

    @InjectMocks
    private KgRoleServiceImpl roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        org.springframework.test.util.ReflectionTestUtils.setField(roleService, "baseMapper", roleMapper);
    }

    @Test
    void testGetRoleKeysByStaffId() {
        KgStaffRole staffRole = new KgStaffRole();
        staffRole.setRoleId(1L);
        staffRole.setStaffId(100L);
        
        KgRole role = new KgRole();
        role.setId(1L);
        role.setRoleKey("ROLE_TEACHER");

        when(staffRoleMapper.selectList(any(QueryWrapper.class))).thenReturn(Collections.singletonList(staffRole));
        when(roleMapper.selectBatchIds(anyList())).thenReturn(Collections.singletonList(role));

        List<String> roleKeys = roleService.getRoleKeysByStaffId(100L);

        assertNotNull(roleKeys);
        assertEquals(1, roleKeys.size());
        assertEquals("ROLE_TEACHER", roleKeys.get(0));
    }
    
    @Test
    void testGetRoleKeysByStaffId_NullId() {
        List<String> roleKeys = roleService.getRoleKeysByStaffId(null);
        assertTrue(roleKeys.isEmpty());
    }
}
