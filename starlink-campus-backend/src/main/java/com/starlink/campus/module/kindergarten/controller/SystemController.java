package com.starlink.campus.module.kindergarten.controller;

import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.entity.KgMenu;
import com.starlink.campus.module.kindergarten.entity.KgRole;
import com.starlink.campus.module.kindergarten.service.KgMenuService;
import com.starlink.campus.module.kindergarten.service.KgRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/system")
public class SystemController {

    @Autowired
    private KgRoleService roleService;

    @Autowired
    private KgMenuService menuService;

    @GetMapping("/role/list")
    public R<List<KgRole>> roleList() {
        return R.ok(roleService.list());
    }

    @GetMapping("/menu/tree")
    public R<List<KgMenu>> menuTree() {
        return R.ok(menuService.list());
    }
}
