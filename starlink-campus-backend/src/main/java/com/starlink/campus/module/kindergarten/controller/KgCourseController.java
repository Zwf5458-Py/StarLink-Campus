package com.starlink.campus.module.kindergarten.controller;

import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.entity.KgCourse;
import com.starlink.campus.module.kindergarten.service.KgCourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.dev33.satoken.annotation.SaCheckLogin;
import jakarta.validation.Valid;

import java.util.List;

@SaCheckLogin
@RestController
@RequestMapping("/kindergarten/course")
@CrossOrigin
public class KgCourseController {

    @Autowired
    private KgCourseService courseService;

    @GetMapping("/list")
    public R<List<KgCourse>> list() {
        return R.ok(courseService.list());
    }

    @PostMapping("/add")
    public R<Boolean> add(@Valid @RequestBody KgCourse course) {
        return R.ok(courseService.save(course));
    }

    @DeleteMapping("/delete/{id}")
    public R<Boolean> delete(@PathVariable Long id) {
        return R.ok(courseService.removeById(id));
    }
}
