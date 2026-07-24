package com.starlink.campus.module.kindergarten.service;

import com.starlink.campus.module.kindergarten.entity.KgEnvironmentMonitor;
import com.starlink.campus.module.kindergarten.entity.KgSmartGateRecord;
import com.starlink.campus.module.kindergarten.mapper.KgEnvironmentMonitorMapper;
import com.starlink.campus.module.kindergarten.mapper.KgSmartGateRecordMapper;
import com.starlink.campus.module.kindergarten.service.impl.IotDeviceServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class IotDeviceServiceImplTest {

    @InjectMocks
    private IotDeviceServiceImpl service;

    @Mock
    private KgEnvironmentMonitorMapper envMapper;

    @Mock
    private KgSmartGateRecordMapper gateMapper;

    @Test
    void testRecordEnvironmentData_Normal() {
        KgEnvironmentMonitor data = new KgEnvironmentMonitor();
        data.setPm25Level(50);
        data.setCo2Level(800);
        
        when(envMapper.insert(any(KgEnvironmentMonitor.class))).thenReturn(1);
        
        KgEnvironmentMonitor result = service.recordEnvironmentData(data);
        assertNotNull(result);
        assertEquals(0, result.getWarningTriggered());
    }

    @Test
    void testRecordEnvironmentData_Pm25Warning() {
        KgEnvironmentMonitor data = new KgEnvironmentMonitor();
        data.setPm25Level(150); // > 100
        data.setCo2Level(800);
        
        when(envMapper.insert(any(KgEnvironmentMonitor.class))).thenReturn(1);
        
        KgEnvironmentMonitor result = service.recordEnvironmentData(data);
        assertEquals(1, result.getWarningTriggered());
    }
    
    @Test
    void testRecordEnvironmentData_Co2Warning() {
        KgEnvironmentMonitor data = new KgEnvironmentMonitor();
        data.setPm25Level(50);
        data.setCo2Level(1200); // > 1000
        
        when(envMapper.insert(any(KgEnvironmentMonitor.class))).thenReturn(1);
        
        KgEnvironmentMonitor result = service.recordEnvironmentData(data);
        assertEquals(1, result.getWarningTriggered());
    }

    @Test
    void testRecordEnvironmentData_BoundaryConditions() {
        // PM2.5 Boundary: 100
        KgEnvironmentMonitor dataLow = new KgEnvironmentMonitor();
        dataLow.setPm25Level(100);
        dataLow.setCo2Level(500);
        when(envMapper.insert(any(KgEnvironmentMonitor.class))).thenReturn(1);
        assertEquals(0, service.recordEnvironmentData(dataLow).getWarningTriggered());

        KgEnvironmentMonitor dataHigh = new KgEnvironmentMonitor();
        dataHigh.setPm25Level(101);
        dataHigh.setCo2Level(500);
        assertEquals(1, service.recordEnvironmentData(dataHigh).getWarningTriggered());

        // CO2 Boundary: 1000
        KgEnvironmentMonitor co2Low = new KgEnvironmentMonitor();
        co2Low.setPm25Level(50);
        co2Low.setCo2Level(1000);
        assertEquals(0, service.recordEnvironmentData(co2Low).getWarningTriggered());

        KgEnvironmentMonitor co2High = new KgEnvironmentMonitor();
        co2High.setPm25Level(50);
        co2High.setCo2Level(1001);
        assertEquals(1, service.recordEnvironmentData(co2High).getWarningTriggered());
    }

    @Test
    void testListClassEnvironment() {
        List<KgEnvironmentMonitor> result = service.listClassEnvironment(1L);
    }

    @Test
    void testLogGatePass() {
        KgSmartGateRecord record = new KgSmartGateRecord();
        record.setPersonId(1L);
        when(gateMapper.insert(any(KgSmartGateRecord.class))).thenReturn(1);
        KgSmartGateRecord result = service.logGatePass(record);
        assertNotNull(result);
        assertNotNull(result.getPassTime());
    }

    @Test
    void testListPersonPassRecords() {
        List<KgSmartGateRecord> result = service.listPersonPassRecords(1L, "");
    }

}