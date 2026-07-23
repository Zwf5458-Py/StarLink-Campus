package com.starlink.campus.module.kindergarten.controller;

import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.entity.KgVisitorRecord;
import com.starlink.campus.module.kindergarten.service.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.dev33.satoken.annotation.SaCheckLogin;
import jakarta.validation.Valid;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import com.starlink.campus.common.utils.ExcelExportUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
@Tag(name = "访客通行管理")
@SaCheckLogin
@RestController
@RequestMapping("/kindergarten/visitor")
@CrossOrigin
public class KgVisitorController {

    @Autowired
    private VisitorService visitorService;

    @GetMapping("/list")
    public R<List<KgVisitorRecord>> getVisitorList() {
        return R.ok(visitorService.getVisitorList());
    }

    @PostMapping("/create-pass")
    public R<String> createVisitorPass(@Valid @RequestBody KgVisitorRecord visitor) {
        return R.ok(visitorService.createVisitorPass(visitor));
    }

    @PostMapping("/approve/{id}")
    public R<Boolean> approvePass(@PathVariable Long id) {
        return R.ok(visitorService.approvePass(id));
    }

    @PostMapping("/reject/{id}")
    public R<Boolean> rejectPass(@PathVariable Long id) {
        return R.ok(visitorService.rejectPass(id));
    }

    @PostMapping("/verify-pass")
    public R<Boolean> verifyPass(@RequestParam String passCode) {
        return R.ok(visitorService.verifyPass(passCode));
    }

    @GetMapping("/check-overtime")
    public R<List<KgVisitorRecord>> checkOvertime() {
        return R.ok(visitorService.checkOvertime());
    }

    @GetMapping("/export")
    @Operation(summary = "导出访客记录 Excel")
    public void exportVisitor(HttpServletResponse response) throws IOException {
        List<KgVisitorRecord> records = visitorService.getVisitorList();
        List<Map<String, Object>> data = new ArrayList<>();
        for (KgVisitorRecord r : records) {
            Map<String, Object> row = new HashMap<>();
            row.put("visitorName", r.getVisitorName());
            row.put("visitorPhone", r.getVisitorPhone());
            row.put("visitReason", r.getVisitReason());
            row.put("visitDate", r.getVisitDate() != null ? r.getVisitDate().toString() : "");
            row.put("status", r.getStatus());
            row.put("checkInTime", r.getCheckInTime() != null ? r.getCheckInTime().toString() : "");
            row.put("checkOutTime", r.getCheckOutTime() != null ? r.getCheckOutTime().toString() : "");
            data.add(row);
        }
        ExcelExportUtil.export(data,
            List.of("visitorName", "visitorPhone", "visitReason", "visitDate", "status", "checkInTime", "checkOutTime"),
            List.of("访客姓名", "联系电话", "来访事由", "来访日期", "状态", "入园时间", "离园时间"),
            "访客记录", "访客通行记录", response);
    }
}
