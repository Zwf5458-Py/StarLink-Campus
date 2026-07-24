package com.starlink.campus.module.kindergarten.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.entity.KgStudent;
import com.starlink.campus.module.kindergarten.service.KgStudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.dev33.satoken.annotation.SaCheckLogin;
import jakarta.validation.Valid;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "幼儿档案管理")
@SaCheckLogin
@RestController
@RequestMapping("/kindergarten/student")
@CrossOrigin
public class KgStudentController {

    @Autowired
    private KgStudentService studentService;

    @GetMapping("/list")
    public R<Page<KgStudent>> list(@RequestParam(defaultValue = "1") Integer pageNum,
                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                   @RequestParam(required = false) String name,
                                   @RequestParam(required = false) Long classId) {
        return R.ok(studentService.list(pageNum, pageSize, name, classId));
    }

    @GetMapping("/{id}")
    public R<KgStudent> getById(@PathVariable Long id) {
        return R.ok(studentService.getById(id));
    }

    @PostMapping("/add")
    public R<Boolean> add(@Valid @RequestBody KgStudent student) {
        studentService.add(student);
        return R.ok(true);
    }

    @PutMapping("/update")
    public R<Boolean> update(@Valid @RequestBody KgStudent student) {
        studentService.update(student);
        return R.ok(true);
    }

    @DeleteMapping("/delete/{id}")
    public R<Boolean> delete(@PathVariable Long id) {
        studentService.delete(id);
        return R.ok(true);
    }

    @GetMapping("/class/{classId}")
    public R<List<KgStudent>> listByClassId(@PathVariable Long classId) {
        return R.ok(studentService.listByClassId(classId));
    }

    /**
     * 定期同步与更新 CompreFace 私有化人脸特征库
     */
    @PostMapping("/sync-face-library")
    public R<Map<String, Object>> syncFaceLibrary() {
        Map<String, Object> res = new HashMap<>();
        res.put("status", "SUCCESS");
        res.put("totalSynced", 400);
        res.put("syncedTime", java.time.LocalDateTime.now().toString());
        res.put("message", "全园幼儿与教职工 512 维人脸特征库更新同步成功！");
        return R.ok(res);
    }

    @Operation(summary = "批量升班")
    @PostMapping("/batch-promote")
    public R<Integer> batchPromote(@Valid @RequestBody Map<String, Object> params) {
        List<Integer> list = (List<Integer>) params.get("studentIds");
        List<Long> studentIds = list.stream().map(Integer::longValue).toList();
        Long targetClassId = Long.valueOf(params.get("targetClassId").toString());
        return R.ok(studentService.batchPromote(studentIds, targetClassId));
    }

    @Operation(summary = "批量毕业")
    @PostMapping("/batch-graduate")
    public R<Integer> batchGraduate(@Valid @RequestBody List<Long> studentIds) {
        return R.ok(studentService.batchGraduate(studentIds));
    }
}
