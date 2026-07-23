package com.starlink.campus.module.kindergarten.controller;

import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.entity.KgNotice;
import com.starlink.campus.module.kindergarten.service.KgNoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/notification")
@Tag(name = "系统通知中心")
public class NotificationController {

    @Autowired
    private KgNoticeService noticeService;

    @GetMapping("/unread")
    @Operation(summary = "获取未读通知列表 (查库)")
    public R<List<KgNotice>> getNoticeList() {
        return R.ok(noticeService.getUnreadNoticeList());
    }

    @PutMapping("/read/{id}")
    @Operation(summary = "标记通知为已读")
    public R<Boolean> markAsRead(@PathVariable Long id) {
        return R.ok(noticeService.markAsRead(id));
    }
}
