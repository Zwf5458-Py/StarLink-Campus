package com.starlink.campus.module.kindergarten.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.starlink.campus.module.kindergarten.entity.KgPatrolPoint;
import com.starlink.campus.module.kindergarten.mapper.KgPatrolPointMapper;
import com.starlink.campus.module.kindergarten.entity.KgPatrolRecord;
import com.starlink.campus.module.kindergarten.entity.KgRepairOrder;
import com.starlink.campus.module.kindergarten.mapper.KgPatrolRecordMapper;
import com.starlink.campus.module.kindergarten.mapper.KgRepairOrderMapper;
import com.starlink.campus.module.kindergarten.service.impl.PatrolInspectionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PatrolInspectionServiceImplTest {

    @Mock
    private KgPatrolRecordMapper patrolRecordMapper;

    @Mock
    private KgRepairOrderMapper repairOrderMapper;

    @InjectMocks
    private PatrolInspectionServiceImpl patrolInspectionService;

    @BeforeEach
    public void setUp() {
        // Mockito annotations are initialized by MockitoExtension
    }

    @Mock
    private KgPatrolPointMapper patrolPointMapper;

    @Test
    public void testGenerateTasks() {
        Long staffId = 101L;

        KgPatrolPoint p1 = new KgPatrolPoint();
        p1.setPointName("大门岗亭");
        p1.setStatus(1);

        when(patrolPointMapper.selectList(any(QueryWrapper.class))).thenReturn(Arrays.asList(p1, p1, p1, p1, p1));
        when(patrolRecordMapper.insert(any(KgPatrolRecord.class))).thenReturn(1);

        boolean result = patrolInspectionService.generateTasks(staffId);

        assertTrue(result, "generateTasks should return true");

        ArgumentCaptor<KgPatrolRecord> captor = ArgumentCaptor.forClass(KgPatrolRecord.class);
        verify(patrolRecordMapper, times(5)).insert(captor.capture());

        List<KgPatrolRecord> savedRecords = captor.getAllValues();
        assertEquals(5, savedRecords.size(), "Should generate exactly 5 default patrol points");

        assertEquals("大门岗亭", savedRecords.get(0).getPatrolPointName());
        assertEquals("待巡检", savedRecords.get(0).getStatus());
        assertEquals(staffId, savedRecords.get(0).getPatrolStaffId());
    }

    @Test
    public void testSubmitPatrol_Normal() {
        KgPatrolRecord record = new KgPatrolRecord();
        record.setId(1L);
        record.setIsNormal(1);

        when(patrolRecordMapper.updateById(any(KgPatrolRecord.class))).thenReturn(1);

        patrolInspectionService.submitPatrol(record);

        verify(patrolRecordMapper, times(1)).updateById(record);
        verify(repairOrderMapper, never()).insert(any(KgRepairOrder.class));
        assertNotNull(record.getPatrolTime(), "Patrol time should be set");
    }

    @Test
    public void testSubmitPatrol_Abnormal_TriggersRepairOrder() {
        KgPatrolRecord record = new KgPatrolRecord();
        record.setId(2L);
        record.setIsNormal(0); // 0 means abnormal
        record.setAbnormalDesc("消防栓玻璃破损");

        when(patrolRecordMapper.updateById(any(KgPatrolRecord.class))).thenReturn(1);

        patrolInspectionService.submitPatrol(record);

        verify(patrolRecordMapper, times(1)).updateById(record);

        ArgumentCaptor<KgRepairOrder> repairCaptor = ArgumentCaptor.forClass(KgRepairOrder.class);
        verify(repairOrderMapper, times(1)).insert(repairCaptor.capture());

        KgRepairOrder generatedOrder = repairCaptor.getValue();
        assertEquals(2L, generatedOrder.getPatrolRecordId(), "Repair order should link to patrol record ID");
        assertTrue(generatedOrder.getOrderNo().startsWith("REP-"), "Order number should start with REP-");
        assertTrue(generatedOrder.getDescription().contains("消防栓玻璃破损"), "Description should contain abnormal desc");
        assertEquals("待处理", generatedOrder.getStatus(), "Status should be initialized to pending");
    }
}
