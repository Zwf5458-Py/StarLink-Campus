package com.starlink.campus.module.kindergarten.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.starlink.campus.module.kindergarten.entity.KgAssetItem;
import com.starlink.campus.module.kindergarten.entity.KgAssetRecord;
import com.starlink.campus.module.kindergarten.mapper.KgAssetItemMapper;
import com.starlink.campus.module.kindergarten.mapper.KgAssetRecordMapper;
import com.starlink.campus.module.kindergarten.service.AssetManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class AssetManagementServiceImpl implements AssetManagementService {

    @Autowired
    private KgAssetItemMapper itemMapper;

    @Autowired
    private KgAssetRecordMapper recordMapper;

    @Override
    public KgAssetItem saveAssetItem(KgAssetItem assetItem) {
        if (assetItem.getId() != null) {
            itemMapper.updateById(assetItem);
        } else {
            itemMapper.insert(assetItem);
        }
        return assetItem;
    }

    @Override
    @Transactional
    public KgAssetRecord logAssetTransaction(KgAssetRecord record) {
        recordMapper.insert(record);
        KgAssetItem item = itemMapper.selectById(record.getAssetId());
        if (item != null) {
            int qty = record.getQuantity();
            if ("INBOUND".equals(record.getRecordType()) || "RETURN".equals(record.getRecordType())) {
                item.setAvailableQuantity(item.getAvailableQuantity() + qty);
                if ("INBOUND".equals(record.getRecordType())) {
                    item.setTotalQuantity(item.getTotalQuantity() + qty);
                }
            } else if ("OUTBOUND".equals(record.getRecordType()) || "SCRAP".equals(record.getRecordType())) {
                if (item.getAvailableQuantity() - qty < 0) {
                    throw new IllegalArgumentException("库存不足，无法出库/报废");
                }
                item.setAvailableQuantity(item.getAvailableQuantity() - qty);
                if ("SCRAP".equals(record.getRecordType())) {
                    item.setTotalQuantity(item.getTotalQuantity() - qty);
                }
            }
            itemMapper.updateById(item);
        }
        return record;
    }

    @Override
    public List<KgAssetItem> listAllAssets() {
        return itemMapper.selectList(new QueryWrapper<>());
    }
}
