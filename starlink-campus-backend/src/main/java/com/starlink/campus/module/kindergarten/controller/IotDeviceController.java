package com.starlink.campus.module.kindergarten.controller;

import jakarta.validation.Valid;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.entity.KgEnvironmentMonitor;
import com.starlink.campus.module.kindergarten.entity.KgSmartGateRecord;
import com.starlink.campus.module.kindergarten.service.IotDeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/iot")
@Tag(name = "IoT 设备物联与联动采集")
@CrossOrigin
public class IotDeviceController {

    @Autowired
    private IotDeviceService iotService;

    @Operation(summary = "硬件上报教室环境数据 (可由班牌直接POST)")
    @PostMapping("/env/report")
    public R<KgEnvironmentMonitor> reportEnv(@RequestHeader(value = "X-Device-Token", required = false) String deviceToken, @Valid @RequestBody KgEnvironmentMonitor data) {
        if (!"starlink-iot-secret-2026".equals(deviceToken)) {
            return R.fail(401, "Invalid Device Token");
        }
        return R.ok(iotService.recordEnvironmentData(data));
    }

    @SaCheckLogin
    @Operation(summary = "前端展示本班环境数据流")
    @GetMapping("/env/class/{classId}")
    public R<List<KgEnvironmentMonitor>> getClassEnv(@PathVariable Long classId) {
        return R.ok(iotService.listClassEnvironment(classId));
    }

    @Operation(summary = "门禁闸机触发人脸或IC卡通行回传记录")
    @PostMapping("/gate/pass")
    public R<KgSmartGateRecord> reportGatePass(@RequestHeader(value = "X-Device-Token", required = false) String deviceToken, @Valid @RequestBody KgSmartGateRecord record) {
        if (!"starlink-iot-secret-2026".equals(deviceToken)) {
            return R.fail(401, "Invalid Device Token");
        }
        return R.ok(iotService.logGatePass(record));
    }

    @SaCheckLogin
    @Operation(summary = "家长查询幼儿入园离园刷卡历史")
    @GetMapping("/gate/person/{personId}")
    public R<List<KgSmartGateRecord>> getPersonGateRecords(@PathVariable Long personId, @RequestParam String personType) {
        return R.ok(iotService.listPersonPassRecords(personId, personType));
    }
}
