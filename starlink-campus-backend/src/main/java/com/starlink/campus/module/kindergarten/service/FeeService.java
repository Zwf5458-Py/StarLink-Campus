package com.starlink.campus.module.kindergarten.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.starlink.campus.module.kindergarten.entity.KgFeeItem;
import com.starlink.campus.module.kindergarten.entity.KgPayment;

import java.util.List;
import java.util.Map;

public interface FeeService {
    // 费用项 CRUD
    Page<KgFeeItem> listFeeItems(Integer pageNum, Integer pageSize);
    boolean addFeeItem(KgFeeItem feeItem);
    boolean updateFeeItem(KgFeeItem feeItem);
    boolean deleteFeeItem(Long id);

    // 缴费记录
    Page<KgPayment> listPayments(Long studentId, Integer pageNum, Integer pageSize);
    boolean createPayment(KgPayment payment);
    List<KgPayment> getUnpaidList(Long studentId);

    // 统计
    Map<String, Object> getCollectionStats(Long feeItemId);
}
