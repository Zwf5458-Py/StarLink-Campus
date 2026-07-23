package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.starlink.campus.module.kindergarten.entity.KgFeeItem;
import com.starlink.campus.module.kindergarten.entity.KgPayment;
import com.starlink.campus.module.kindergarten.mapper.KgFeeItemMapper;
import com.starlink.campus.module.kindergarten.mapper.KgPaymentMapper;
import com.starlink.campus.module.kindergarten.service.FeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class FeeServiceImpl implements FeeService {
    
    private static final Logger log = LoggerFactory.getLogger(FeeServiceImpl.class);

    @Autowired
    private KgFeeItemMapper feeItemMapper;

    @Autowired
    private KgPaymentMapper paymentMapper;

    @Override
    public Page<KgFeeItem> listFeeItems(Integer pageNum, Integer pageSize) {
        Page<KgFeeItem> page = new Page<>(pageNum, pageSize);
        QueryWrapper<KgFeeItem> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("create_time");
        return feeItemMapper.selectPage(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addFeeItem(KgFeeItem feeItem) {
        feeItem.setCreateTime(LocalDateTime.now());
        if (feeItem.getStatus() == null) {
            feeItem.setStatus("生效");
        }
        return feeItemMapper.insert(feeItem) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateFeeItem(KgFeeItem feeItem) {
        return feeItemMapper.updateById(feeItem) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteFeeItem(Long id) {
        return feeItemMapper.deleteById(id) > 0;
    }

    @Override
    public Page<KgPayment> listPayments(Long studentId, Integer pageNum, Integer pageSize) {
        Page<KgPayment> page = new Page<>(pageNum, pageSize);
        QueryWrapper<KgPayment> queryWrapper = new QueryWrapper<>();
        if (studentId != null) {
            queryWrapper.eq("student_id", studentId);
        }
        queryWrapper.orderByDesc("create_time");
        return paymentMapper.selectPage(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createPayment(KgPayment payment) {
        payment.setPayTime(LocalDateTime.now());
        payment.setTransactionNo("PAY-" + UUID.randomUUID().toString().substring(0, 8));
        payment.setStatus("已缴");
        payment.setCreateTime(LocalDateTime.now());
        return paymentMapper.insert(payment) > 0;
    }

    @Override
    public List<KgPayment> getUnpaidList(Long studentId) {
        QueryWrapper<KgPayment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_id", studentId)
                    .eq("status", "待缴");
        return paymentMapper.selectList(queryWrapper);
    }

    @Override
    public Map<String, Object> getCollectionStats(Long feeItemId) {
        QueryWrapper<KgPayment> paidQuery = new QueryWrapper<>();
        paidQuery.eq("fee_item_id", feeItemId).eq("status", "已缴");
        long paidCount = paymentMapper.selectCount(paidQuery);

        QueryWrapper<KgPayment> unpaidQuery = new QueryWrapper<>();
        unpaidQuery.eq("fee_item_id", feeItemId).eq("status", "待缴");
        long unpaidCount = paymentMapper.selectCount(unpaidQuery);
        
        QueryWrapper<KgPayment> overdueQuery = new QueryWrapper<>();
        overdueQuery.eq("fee_item_id", feeItemId).eq("status", "逾期");
        long overdueCount = paymentMapper.selectCount(overdueQuery);

        Map<String, Object> stats = new HashMap<>();
        stats.put("paidCount", paidCount);
        stats.put("unpaidCount", unpaidCount);
        stats.put("overdueCount", overdueCount);
        stats.put("totalCount", paidCount + unpaidCount + overdueCount);
        return stats;
    }
}
