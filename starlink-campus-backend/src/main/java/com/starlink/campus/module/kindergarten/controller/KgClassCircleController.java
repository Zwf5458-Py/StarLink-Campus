package com.starlink.campus.module.kindergarten.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.entity.KgClassCircle;
import com.starlink.campus.module.kindergarten.service.KgClassCircleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.dev33.satoken.annotation.SaCheckLogin;
import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 班级圈与家园互动 API
 * 遵循代码分析与优化报告 2.8 节规范
 */
@SaCheckLogin
@RestController
@RequestMapping("/kindergarten/circle")
@CrossOrigin
public class KgClassCircleController {

    @Autowired
    private KgClassCircleService classCircleService;

    @Autowired
    private com.starlink.campus.common.utils.MockWechatSecurityUtil securityUtil;

    @GetMapping("/list")
    public R<List<KgClassCircle>> list() {
        QueryWrapper<KgClassCircle> query = new QueryWrapper<>();
        query.orderByDesc("publish_time");
        return R.ok(classCircleService.list(query));
    }

    @PostMapping("/post")
    public R<Boolean> post(@Valid @RequestBody KgClassCircle circle) {
        if (!securityUtil.checkTextSecurity(circle.getContent())) {
            return R.fail("发布失败，内容包含违规敏感词！");
        }
        if (!securityUtil.checkMediaSecurity(circle.getMediaUrls())) {
            return R.fail("发布失败，媒体文件涉嫌违规！");
        }
        
        circle.setLikes(0);
        circle.setComments(0);
        circle.setPublishTime(LocalDateTime.now());
        return R.ok(classCircleService.save(circle));
    }

    @PostMapping("/like/{id}")
    public R<Boolean> like(@PathVariable Long id) {
        KgClassCircle circle = classCircleService.getById(id);
        if (circle != null) {
            circle.setLikes(circle.getLikes() + 1);
            return R.ok(classCircleService.updateById(circle));
        }
        return R.fail("记录不存在");
    }
}
