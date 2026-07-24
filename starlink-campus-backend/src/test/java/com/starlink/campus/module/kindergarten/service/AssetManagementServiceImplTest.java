package com.starlink.campus.module.kindergarten.service;

import com.starlink.campus.module.kindergarten.entity.KgAssetItem;
import com.starlink.campus.module.kindergarten.entity.KgAssetRecord;
import com.starlink.campus.module.kindergarten.mapper.KgAssetItemMapper;
import com.starlink.campus.module.kindergarten.mapper.KgAssetRecordMapper;
import com.starlink.campus.module.kindergarten.service.impl.AssetManagementServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AssetManagementServiceImplTest {

    @InjectMocks
    private AssetManagementServiceImpl service;

    @Mock
    private KgAssetItemMapper itemMapper;

    @Mock
    private KgAssetRecordMapper recordMapper;

    @Test
    void testLogAssetTransaction_OutboundSuccess() {
        KgAssetRecord record = new KgAssetRecord();
        record.setAssetId(1L);
        record.setQuantity(5);
        record.setRecordType("OUTBOUND");

        KgAssetItem item = new KgAssetItem();
        item.setId(1L);
        item.setAvailableQuantity(10);
        item.setTotalQuantity(10);

        when(itemMapper.selectById(1L)).thenReturn(item);
        when(recordMapper.insert(any(KgAssetRecord.class))).thenReturn(1);
        when(itemMapper.updateById(any(KgAssetItem.class))).thenReturn(1);

        KgAssetRecord result = service.logAssetTransaction(record);
        assertNotNull(result);
        assertEquals(5, item.getAvailableQuantity());
        assertEquals(10, item.getTotalQuantity());
    }

    @Test
    void testLogAssetTransaction_OutboundInsufficient() {
        KgAssetRecord record = new KgAssetRecord();
        record.setAssetId(1L);
        record.setQuantity(15);
        record.setRecordType("OUTBOUND");

        KgAssetItem item = new KgAssetItem();
        item.setId(1L);
        item.setAvailableQuantity(10);
        item.setTotalQuantity(10);

        when(itemMapper.selectById(1L)).thenReturn(item);

        assertThrows(IllegalArgumentException.class, () -> {
            service.logAssetTransaction(record);
        });

        verify(recordMapper).insert(record);
        verify(itemMapper, never()).updateById(any());
    }
}