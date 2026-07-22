package com.starlink.campus.module.kindergarten.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.dto.LoginDTO;
import com.starlink.campus.module.kindergarten.entity.KgStaff;
import com.starlink.campus.module.kindergarten.mapper.KgStaffMapper;
import org.springframework.beans.factory.annotation.Autowired;
import com.starlink.campus.common.PasswordUtil;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private KgStaffMapper staffMapper;

    @PostMapping("/login")
    public R<String> login(@RequestBody LoginDTO loginDTO) {
        if (loginDTO.getUsername() == null || loginDTO.getPassword() == null) {
            return R.fail("用户名或密码不能为空");
        }

        KgStaff staff = staffMapper.selectOne(
                new QueryWrapper<KgStaff>().eq("username", loginDTO.getUsername())
        );

        if (staff == null) {
            return R.fail("用户不存在");
        }

        if (!PasswordUtil.matches(loginDTO.getPassword(), staff.getPassword())) {
            return R.fail("密码错误");
        }

        StpUtil.login(staff.getId());
        return R.ok(StpUtil.getTokenValue());
    }

    @PostMapping("/logout")
    public R<String> logout() {
        StpUtil.logout();
        return R.ok("登出成功");
    }
}
