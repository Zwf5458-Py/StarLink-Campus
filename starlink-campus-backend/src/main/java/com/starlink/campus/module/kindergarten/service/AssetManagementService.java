package com.starlink.campus.module.kindergarten.service;

import com.starlink.campus.module.kindergarten.entity.KgAssetItem;
import com.starlink.campus.module.kindergarten.entity.KgAssetRecord;
import java.util.List;

public interface AssetManagementService {
    KgAssetItem saveAssetItem(KgAssetItem assetItem);
    KgAssetRecord logAssetTransaction(KgAssetRecord record);
    List<KgAssetItem> listAllAssets();
}
