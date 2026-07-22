package com.starlink.campus.module.kindergarten.controller;

import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.entity.KgClassBoardConfig;
import com.starlink.campus.module.kindergarten.mapper.KgClassBoardConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.Arrays;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

@RestController
@RequestMapping("/kindergarten/board/config")
@CrossOrigin
public class KgClassBoardConfigController {

    @Autowired(required = false)
    private KgClassBoardConfigMapper boardConfigMapper;

    @GetMapping("/{roomNumber}")
    public R<Map<String, Object>> getBoardConfig(@PathVariable String roomNumber) {
        KgClassBoardConfig config = boardConfigMapper != null ? 
            boardConfigMapper.selectOne(new QueryWrapper<KgClassBoardConfig>().last("LIMIT 1")) : null;
        Map<String, Object> result = new HashMap<>();
        
        if (config != null) {
            result.put("className", "大(1)班 - 葵花班");
            result.put("roomNumber", roomNumber);
            result.put("slogan", config.getSlogan());
        } else {
            result.put("className", "大(1)班 - 葵花班");
            result.put("roomNumber", roomNumber);
            result.put("slogan", "快乐成长，健康每一步！探索世界，梦想起航！");
        }
        
        // Mocking schedule list for now
        List<Map<String, Object>> scheduleList = Arrays.asList(
            createSchedule("08:00 - 08:30", "晨检入园与体温检测", false),
            createSchedule("08:30 - 09:00", "营养早餐时间", false),
            createSchedule("09:00 - 09:40", "海星科学实验探索课", true),
            createSchedule("10:00 - 11:00", "户外体能锻炼活动", false),
            createSchedule("12:00 - 14:30", "午餐与午休恢复", false),
            createSchedule("15:00 - 15:40", "绘本阅读与兴趣拓育", false)
        );
        result.put("scheduleList", scheduleList);

        // Mocking teachers
        List<Map<String, Object>> teachers = Arrays.asList(
            createTeacher("👩‍🏫", "李老师", "班主任"),
            createTeacher("👩‍⚕️", "王医生", "保健医师"),
            createTeacher("👩‍🍳", "张阿姨", "保育员")
        );
        result.put("teachers", teachers);

        return R.ok(result);
    }

    private Map<String, Object> createSchedule(String time, String name, boolean current) {
        Map<String, Object> map = new HashMap<>();
        map.put("time", time);
        map.put("name", name);
        map.put("current", current);
        return map;
    }

    private Map<String, Object> createTeacher(String avatar, String name, String role) {
        Map<String, Object> map = new HashMap<>();
        map.put("avatar", avatar);
        map.put("name", name);
        map.put("role", role);
        return map;
    }
}
