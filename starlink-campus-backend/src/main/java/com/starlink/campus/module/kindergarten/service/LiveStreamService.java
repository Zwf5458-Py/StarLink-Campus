package com.starlink.campus.module.kindergarten.service;

import java.util.List;
import java.util.Map;

/**
 * 视频监控/实时直播服务接口
 * 二期规划，依赖硬件（海康/大华摄像头 + RTMP推流）
 * 当前仅为接口占位
 */
public interface LiveStreamService {
    String getStreamUrl(String roomNumber);
    List<Map<String, Object>> listCameras();
}
