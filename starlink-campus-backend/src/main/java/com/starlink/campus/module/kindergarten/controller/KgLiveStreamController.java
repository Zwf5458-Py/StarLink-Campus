package com.starlink.campus.module.kindergarten.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.entity.KgCamera;
import com.starlink.campus.module.kindergarten.mapper.KgCameraMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@SaCheckLogin
@RestController
@RequestMapping("/live")
@Tag(name = "校园安防监控与直播服务")
public class KgLiveStreamController {

    @Autowired
    private KgCameraMapper cameraMapper;

    @GetMapping("/cameras")
    @Operation(summary = "获取园区监控摄像头与 HLS/WebRTC 直播流列表")
    public R<List<KgCamera>> getCameraStreams() {
        return R.ok(cameraMapper.selectList(new QueryWrapper<>()));
    }
}
