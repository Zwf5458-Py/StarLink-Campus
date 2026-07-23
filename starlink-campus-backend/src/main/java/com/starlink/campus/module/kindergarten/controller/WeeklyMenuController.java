package com.starlink.campus.module.kindergarten.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.entity.KgWeeklyMenu;
import com.starlink.campus.module.kindergarten.service.WeeklyMenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;

@SaCheckLogin
@RestController
@RequestMapping("/kindergarten/menu")
@CrossOrigin
@Tag(name = "每周食谱管理")
public class WeeklyMenuController {

    private static final Logger logger = LoggerFactory.getLogger(WeeklyMenuController.class);

    @Autowired
    private WeeklyMenuService weeklyMenuService;

    @GetMapping("/list")
    @Operation(summary = "查询食谱列表")
    public R<Page<KgWeeklyMenu>> list(
            @RequestParam(required = false) LocalDate weekStart,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        logger.info("List menus for week: {}", weekStart);
        return R.ok(weeklyMenuService.listByWeek(weekStart, pageNum, pageSize));
    }

    @PostMapping("/add")
    @Operation(summary = "新增食谱")
    public R<Boolean> addMenu(@RequestBody KgWeeklyMenu menu) {
        return R.ok(weeklyMenuService.addMenu(menu));
    }

    @PutMapping("/update")
    @Operation(summary = "更新食谱")
    public R<Boolean> updateMenu(@RequestBody KgWeeklyMenu menu) {
        return R.ok(weeklyMenuService.updateMenu(menu));
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除食谱")
    public R<Boolean> deleteMenu(@PathVariable Long id) {
        return R.ok(weeklyMenuService.deleteMenu(id));
    }

    @PutMapping("/publish/{id}")
    @Operation(summary = "发布食谱")
    public R<Boolean> publishMenu(@PathVariable Long id) {
        return R.ok(weeklyMenuService.publishMenu(id));
    }

    @GetMapping("/check-allergen")
    @Operation(summary = "检查过敏原")
    public R<String> checkAllergen(@RequestParam Long menuId, @RequestParam Long classId) {
        return R.ok(weeklyMenuService.checkAllergen(menuId, classId));
    }
}
