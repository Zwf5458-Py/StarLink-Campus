package com.starlink.campus.module.kindergarten.service;

import java.util.List;
import java.util.Map;

public interface SearchService {
    /**
     * 全局搜索
     * @param keyword 搜索关键字
     * @return 搜索结果列表
     */
    List<Map<String, Object>> globalSearch(String keyword);
}
