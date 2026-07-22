package com.starlink.campus.module.kindergarten.controller;

import com.starlink.campus.common.R;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import cn.dev33.satoken.annotation.SaCheckLogin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@CrossOrigin
public class FileUploadController {

    @PostMapping("/upload")
    @SaCheckLogin
    public R<String> upload(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return R.fail("上传文件不能为空");
        }

        try {
            // 获取文件原名
            String originalFilename = file.getOriginalFilename();
            // 生成新文件名
            String suffix = originalFilename != null && originalFilename.contains(".") ? 
                    originalFilename.substring(originalFilename.lastIndexOf(".")) : "";
            String newFilename = UUID.randomUUID().toString().replaceAll("-", "") + suffix;

            // 使用系统临时目录保存文件
            String tmpDir = System.getProperty("java.io.tmpdir");
            File dest = new File(tmpDir + File.separator + newFilename);
            file.transferTo(dest);

            // 返回模拟访问URL
            String url = "/files/" + newFilename;
            return R.ok(url);
        } catch (IOException e) {
            e.printStackTrace();
            return R.fail("文件上传失败: " + e.getMessage());
        }
    }
}
