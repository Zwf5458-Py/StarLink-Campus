package com.starlink.campus.module.kindergarten.controller;

import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.entity.KgVisitorRecord;
import com.starlink.campus.module.kindergarten.service.VisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.dev33.satoken.annotation.SaCheckLogin;
import jakarta.validation.Valid;

import java.util.List;

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
}
