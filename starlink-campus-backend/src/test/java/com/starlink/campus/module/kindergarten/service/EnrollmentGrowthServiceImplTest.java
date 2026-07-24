package com.starlink.campus.module.kindergarten.service;

import com.starlink.campus.module.kindergarten.entity.KgOpenDayEvent;
import com.starlink.campus.module.kindergarten.mapper.KgOpenDayEventMapper;
import com.starlink.campus.module.kindergarten.mapper.KgReferralRecordMapper;
import com.starlink.campus.module.kindergarten.service.impl.EnrollmentGrowthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EnrollmentGrowthServiceImplTest {

    @InjectMocks
    private EnrollmentGrowthServiceImpl service;

    @Mock
    private KgOpenDayEventMapper eventMapper;

    @Mock
    private KgReferralRecordMapper referralMapper;

    @Test
    void testEnrollInEvent_Success() {
        KgOpenDayEvent event = new KgOpenDayEvent();
        event.setId(1L);
        event.setStatus("PUBLISHED");
        event.setCapacity(50);
        event.setEnrolledCount(40);

        when(eventMapper.selectById(1L)).thenReturn(event);
        when(eventMapper.updateEnrolledCount(1L)).thenReturn(1);
        
        boolean result = service.enrollInEvent(1L);
        assertTrue(result);
        verify(eventMapper, never()).updateById(any());
    }

    @Test
    void testEnrollInEvent_EventFull_AfterUpdate() {
        KgOpenDayEvent event = new KgOpenDayEvent();
        event.setId(1L);
        event.setStatus("PUBLISHED");
        event.setCapacity(50);
        event.setEnrolledCount(50); // reaches full after update

        when(eventMapper.selectById(1L)).thenReturn(event);
        when(eventMapper.updateEnrolledCount(1L)).thenReturn(1);
        
        boolean result = service.enrollInEvent(1L);
        assertTrue(result);
        assertEquals("FULL", event.getStatus());
        verify(eventMapper).updateById(event);
    }
    
    @Test
    void testEnrollInEvent_AlreadyFull_BeforeUpdate() {
        KgOpenDayEvent event = new KgOpenDayEvent();
        event.setId(1L);
        event.setStatus("FULL");
        event.setCapacity(50);
        event.setEnrolledCount(50); 

        when(eventMapper.selectById(1L)).thenReturn(event);
        
        boolean result = service.enrollInEvent(1L);
        assertFalse(result);
        verify(eventMapper, never()).updateEnrolledCount(anyLong());
    }
}