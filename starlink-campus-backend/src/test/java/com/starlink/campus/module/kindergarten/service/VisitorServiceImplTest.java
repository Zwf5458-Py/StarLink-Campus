package com.starlink.campus.module.kindergarten.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.starlink.campus.module.kindergarten.entity.KgVisitorRecord;
import com.starlink.campus.module.kindergarten.mapper.KgVisitorRecordMapper;
import com.starlink.campus.module.kindergarten.service.impl.VisitorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class VisitorServiceImplTest {

    @Mock
    private KgVisitorRecordMapper visitorRecordMapper;

    @InjectMocks
    private VisitorServiceImpl visitorService;

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(visitorService, "baseMapper", visitorRecordMapper);
    }

    @Test
    public void testCreateVisitorPass_GeneratesUniquePassCode() {
        // 测试生成唯一通行码
        KgVisitorRecord visitor = new KgVisitorRecord();
        when(visitorRecordMapper.insert(any(KgVisitorRecord.class))).thenReturn(1);

        String passCode = visitorService.createVisitorPass(visitor);

        assertNotNull(passCode);
        assertTrue(passCode.startsWith("PASS-"));
        assertEquals("待审核", visitor.getStatus());
        assertEquals(0, visitor.getOvertimeAlerted());
        assertNotNull(visitor.getCreateTime());
        verify(visitorRecordMapper, times(1)).insert(visitor);
    }

    @Test
    public void testApprovePass_StateTransition() {
        // 测试通行证审批状态转换 (待审核 -> 已通过)
        KgVisitorRecord record = new KgVisitorRecord();
        record.setId(1L);
        record.setStatus("待审核");
        
        when(visitorRecordMapper.selectById(1L)).thenReturn(record);
        when(visitorRecordMapper.updateById(record)).thenReturn(1);

        boolean result = visitorService.approvePass(1L);

        assertTrue(result);
        assertEquals("已通过", record.getStatus());
        verify(visitorRecordMapper, times(1)).updateById(record);
    }

    @Test
    public void testRejectPass_StateTransition() {
        // 测试通行证拒绝状态转换 (待审核 -> 已拒绝)
        KgVisitorRecord record = new KgVisitorRecord();
        record.setId(1L);
        record.setStatus("待审核");
        
        when(visitorRecordMapper.selectById(1L)).thenReturn(record);
        when(visitorRecordMapper.updateById(record)).thenReturn(1);

        boolean result = visitorService.rejectPass(1L);

        assertTrue(result);
        assertEquals("已拒绝", record.getStatus());
        verify(visitorRecordMapper, times(1)).updateById(record);
    }

    @Test
    public void testCheckOvertime_DetectsOvertime() {
        // 测试 15分钟超时检测逻辑
        KgVisitorRecord record1 = new KgVisitorRecord();
        record1.setId(1L);
        record1.setStatus("在园中");
        record1.setCheckInTime(LocalDateTime.now().minusMinutes(20));

        // Service层调用 this.list(wrapper) 时，内部会调用 baseMapper.selectList()
        when(visitorRecordMapper.selectList(any())).thenReturn(Arrays.asList(record1));
        when(visitorRecordMapper.updateById(record1)).thenReturn(1);

        List<KgVisitorRecord> overtimeList = visitorService.checkOvertime();

        assertEquals(1, overtimeList.size());
        assertEquals(1, record1.getOvertimeAlerted());
        verify(visitorRecordMapper, times(1)).updateById(record1);
    }

    @Test
    public void testVerifyPass_ValidAndApproved() {
        // 测试验证逻辑
        KgVisitorRecord record = new KgVisitorRecord();
        record.setPassCode("PASS-123");
        record.setStatus("已通过");

        lenient().when(visitorRecordMapper.selectOne(any())).thenReturn(record);
        lenient().when(visitorRecordMapper.selectOne(any(), anyBoolean())).thenReturn(record);
        lenient().when(visitorRecordMapper.selectList(any())).thenReturn(Arrays.asList(record));
        when(visitorRecordMapper.updateById(record)).thenReturn(1);

        boolean result = visitorService.verifyPass("PASS-123");

        assertTrue(result);
        assertEquals("在园中", record.getStatus());
        assertNotNull(record.getCheckInTime());
        verify(visitorRecordMapper, times(1)).updateById(record);
    }
}
