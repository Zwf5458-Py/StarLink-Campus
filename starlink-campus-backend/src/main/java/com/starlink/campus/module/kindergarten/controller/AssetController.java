package com.starlink.campus.module.kindergarten.controller;

import jakarta.validation.Valid;
import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckRole;
import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.entity.KgAssetItem;
import com.starlink.campus.module.kindergarten.entity.KgAssetRecord;
import com.starlink.campus.module.kindergarten.service.AssetManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@SaCheckLogin
@RestController
@RequestMapping("/api/asset")
@Tag(name = "固定资产与物料台账")
@CrossOrigin
public class AssetController {

    @Autowired
    private AssetManagementService assetService;

    @Operation(summary = "登记资产台账")
    @SaCheckRole("ADMIN")
    @PostMapping("/item")
    public R<KgAssetItem> saveAsset(@Valid @RequestBody KgAssetItem item) {
        return R.ok(assetService.saveAssetItem(item));
    }

    @Operation(summary = "出入库/领用/报废流转登记")
    @SaCheckRole(value = {"ADMIN", "TEACHER"}, mode = cn.dev33.satoken.annotation.SaMode.OR)
    @PostMapping("/transaction")
    public R<KgAssetRecord> logTransaction(@Valid @RequestBody KgAssetRecord record) {
        return R.ok(assetService.logAssetTransaction(record));
    }

    @Operation(summary = "查询资产台账清单")
    @SaCheckRole("ADMIN")
    @GetMapping("/list")
    public R<List<KgAssetItem>> listAssets() {
        return R.ok(assetService.listAllAssets());
    }
}
