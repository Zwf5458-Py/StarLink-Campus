package com.starlink.campus.module.kindergarten.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.entity.KgFeedbackTicket;
import com.starlink.campus.module.kindergarten.service.FeedbackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@SaCheckLogin
@RestController
@RequestMapping("/api/feedback")
@Tag(name = "家长客诉与建议", description = "客诉闭环处理流转")
@CrossOrigin
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @Operation(summary = "家长提交工单")
    @PostMapping("/submit")
    public R<KgFeedbackTicket> submitFeedback(@RequestBody KgFeedbackTicket ticket) {
        return R.ok(feedbackService.submitFeedback(ticket));
    }

    @Operation(summary = "园方受理并跟进工单")
    @SaCheckRole(value = {"ADMIN", "TEACHER", "DOCTOR"}, mode = cn.dev33.satoken.annotation.SaMode.OR)
    @PutMapping("/handle/{ticketId}")
    public R<Boolean> handleFeedback(@PathVariable Long ticketId, @RequestParam Long handlerId, @RequestParam String process) {
        return R.ok(feedbackService.handleFeedback(ticketId, handlerId, process));
    }

    @Operation(summary = "家长结单并打分")
    @PutMapping("/close/{ticketId}")
    public R<Boolean> closeFeedback(@PathVariable Long ticketId, @RequestParam Integer score) {
        return R.ok(feedbackService.closeFeedback(ticketId, score));
    }

    @Operation(summary = "后台查询工单列表")
    @SaCheckRole("ADMIN")
    @GetMapping("/list")
    public R<List<KgFeedbackTicket>> listFeedback(@RequestParam(required = false) String status) {
        return R.ok(feedbackService.listAllFeedback(status));
    }
}
