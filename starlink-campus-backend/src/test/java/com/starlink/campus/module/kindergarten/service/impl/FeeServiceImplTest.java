package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.starlink.campus.module.kindergarten.entity.KgFeeItem;
import com.starlink.campus.module.kindergarten.entity.KgPayment;
import com.starlink.campus.module.kindergarten.mapper.KgFeeItemMapper;
import com.starlink.campus.module.kindergarten.mapper.KgPaymentMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FeeServiceImplTest {

    @Mock
    private KgFeeItemMapper feeItemMapper;

    @Mock
    private KgPaymentMapper paymentMapper;

    @InjectMocks
    private FeeServiceImpl feeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListFeeItems() {
        Page<KgFeeItem> page = new Page<>(1, 10);
        when(feeItemMapper.selectPage(any(Page.class), any(QueryWrapper.class))).thenReturn(page);
        
        Page<KgFeeItem> result = feeService.listFeeItems(1, 10);
        
        assertNotNull(result);
        verify(feeItemMapper, times(1)).selectPage(any(Page.class), any(QueryWrapper.class));
    }

    @Test
    void testAddFeeItem() {
        KgFeeItem feeItem = new KgFeeItem();
        when(feeItemMapper.insert(any(KgFeeItem.class))).thenReturn(1);
        
        boolean result = feeService.addFeeItem(feeItem);
        
        assertTrue(result);
        assertEquals("生效", feeItem.getStatus());
        assertNotNull(feeItem.getCreateTime());
        verify(feeItemMapper, times(1)).insert(feeItem);
    }

    @Test
    void testGetCollectionStats() {
        when(paymentMapper.selectCount(any(QueryWrapper.class))).thenReturn(10L, 5L, 2L);
        
        Map<String, Object> stats = feeService.getCollectionStats(1L);
        
        assertEquals(10L, stats.get("paidCount"));
        assertEquals(5L, stats.get("unpaidCount"));
        assertEquals(2L, stats.get("overdueCount"));
        assertEquals(17L, stats.get("totalCount"));
        verify(paymentMapper, times(3)).selectCount(any(QueryWrapper.class));
    }
}
