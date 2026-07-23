package com.starlink.campus.module.kindergarten.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/notification")
public class NotificationController {

    @GetMapping("/unread")
    public Map<String, Object> getNoticeList() {
        List<Map<String, Object>> notices = new ArrayList<>();
        
        Map<String, Object> n1 = new HashMap<>();
        n1.put("id", 1);
        n1.put("type", "danger");
        n1.put("typeText", "🩺 晨检发热预警");
        n1.put("time", "10 分钟前");
        n1.put("title", "小(1)班 雏菊班 晨检异常");
        n1.put("content", "检测到学生 [张小明] 体温 37.5℃，已由保健医王医生复测并引导至留观室。");
        notices.add(n1);
        
        Map<String, Object> n2 = new HashMap<>();
        n2.put("id", 2);
        n2.put("type", "warning");
        n2.put("typeText", "⏳ OA 审批提醒");
        n2.put("time", "25 分钟前");
        n2.put("title", "待园长终审申请");
        n2.put("content", "大班李老师提交了 [急性咽喉炎请假 1 天] 申请，请及时在线审批。");
        notices.add(n2);
        
        Map<String, Object> n3 = new HashMap<>();
        n3.put("id", 3);
        n3.put("type", "warning");
        n3.put("typeText", "🎫 访客滞留告警");
        n3.put("time", "40 分钟前");
        n3.put("title", "校园安防滞留提醒");
        n3.put("content", "访客 [刘工 (消防检测)] 在园区停留时间已超出 15 分钟，安防系统已发送警报。");
        notices.add(n3);

        Map<String, Object> res = new HashMap<>();
        res.put("code", 200);
        res.put("msg", "success");
        res.put("data", notices);
        return res;
    }
}
