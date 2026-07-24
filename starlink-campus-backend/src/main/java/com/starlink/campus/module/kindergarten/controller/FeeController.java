package com.starlink.campus.module.kindergarten.controller;

import jakarta.validation.Valid;
import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.starlink.campus.common.R;
import com.starlink.campus.module.kindergarten.entity.KgFeeItem;
import com.starlink.campus.module.kindergarten.entity.KgPayment;
import com.starlink.campus.module.kindergarten.service.FeeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Tag(name = "在线缴费与账单管理")
@SaCheckLogin
@RestController
@RequestMapping("/kindergarten/fee")
@CrossOrigin
public class FeeController {

    @Autowired
    private FeeService feeService;

    @Operation(summary = "获取费用项列表")
    @GetMapping("/item/list")
    public R<Page<KgFeeItem>> listFeeItems(@RequestParam(defaultValue = "1") Integer pageNum,
                                           @RequestParam(defaultValue = "10") Integer pageSize) {
        return R.ok(feeService.listFeeItems(pageNum, pageSize));
    }

    @Operation(summary = "新增费用项")
    @PostMapping("/item/add")
    public R<Boolean> addFeeItem(@Valid @RequestBody KgFeeItem feeItem) {
        return R.ok(feeService.addFeeItem(feeItem));
    }

    @Operation(summary = "更新费用项")
    @PostMapping("/item/update")
    public R<Boolean> updateFeeItem(@Valid @RequestBody KgFeeItem feeItem) {
        return R.ok(feeService.updateFeeItem(feeItem));
    }

    @Operation(summary = "删除费用项")
    @DeleteMapping("/item/delete/{id}")
    public R<Boolean> deleteFeeItem(@PathVariable Long id) {
        return R.ok(feeService.deleteFeeItem(id));
    }

    @Operation(summary = "获取学生缴费记录")
    @GetMapping("/payment/list")
    public R<Page<KgPayment>> listPayments(@RequestParam(required = false) Long studentId,
                                           @RequestParam(defaultValue = "1") Integer pageNum,
                                           @RequestParam(defaultValue = "10") Integer pageSize) {
        return R.ok(feeService.listPayments(studentId, pageNum, pageSize));
    }

    @Operation(summary = "创建缴费记录")
    @PostMapping("/payment/create")
    public R<Boolean> createPayment(@Valid @RequestBody KgPayment payment) {
        return R.ok(feeService.createPayment(payment));
    }

    @Operation(summary = "获取学生待缴账单")
    @GetMapping("/payment/unpaid")
    public R<List<KgPayment>> getUnpaidList(@RequestParam Long studentId) {
        return R.ok(feeService.getUnpaidList(studentId));
    }

    @Operation(summary = "收缴率统计")
    @GetMapping("/stats")
    public R<Map<String, Object>> getCollectionStats(@RequestParam Long feeItemId) {
        return R.ok(feeService.getCollectionStats(feeItemId));
    }
}
