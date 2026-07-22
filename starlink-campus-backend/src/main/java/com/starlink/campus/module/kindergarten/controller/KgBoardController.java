package com.starlink.campus.module.kindergarten.controller;

import com.starlink.campus.common.R;
import com.starlink.campus.config.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.dev33.satoken.annotation.SaCheckLogin;
import jakarta.validation.Valid;

@SaCheckLogin
@RestController
@RequestMapping("/kg/board")
@CrossOrigin
public class KgBoardController {

    @Autowired
    private WebSocketServer webSocketServer;

    @PostMapping("/mode/switch")
    public R<Boolean> switchMode(@RequestParam String roomNumber, @RequestParam String mode) {
        webSocketServer.broadcastToRoom(roomNumber, mode);
        return R.ok(true);
    }
}
