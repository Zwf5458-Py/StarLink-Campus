package com.starlink.campus.module.kindergarten.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.starlink.campus.common.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@SaCheckLogin
@RestController
@RequestMapping("/live")
@Tag(name = "校园安防监控与直播服务")
public class KgLiveStreamController {

    @GetMapping("/cameras")
    @Operation(summary = "获取园区监控摄像头与 HLS/WebRTC 直播流列表")
    public R<List<Map<String, Object>>> getCameraStreams() {
        List<Map<String, Object>> list = new ArrayList<>();

        Map<String, Object> c1 = new HashMap<>();
        c1.put("id", 101);
        c1.put("cameraName", "📷 园区正门入口 - 晨检卡口双目相机");
        c1.put("location", "一楼正门广场");
        c1.put("status", "ONLINE");
        c1.put("resolution", "1080P / 60fps");
        c1.put("streamType", "HLS / RTSP");
        c1.put("hlsUrl", "https://sample-videos.com/video321/mp4/720/big_buck_bunny_720p_1mb.mp4");
        c1.put("previewImage", "https://picsum.photos/400/225?random=1");
        c1.put("supportPtz", true);
        list.add(c1);

        Map<String, Object> c2 = new HashMap<>();
        c2.put("id", 102);
        c2.put("cameraName", "📹 小(1)班 雏菊班 活动教室内景");
        c2.put("location", "教学楼 201 教室");
        c2.put("status", "ONLINE");
        c2.put("resolution", "1080P / 30fps");
        c2.put("streamType", "HLS");
        c2.put("hlsUrl", "https://sample-videos.com/video321/mp4/720/big_buck_bunny_720p_1mb.mp4");
        c2.put("previewImage", "https://picsum.photos/400/225?random=2");
        c2.put("supportPtz", false);
        list.add(c2);

        Map<String, Object> c3 = new HashMap<>();
        c3.put("id", 103);
        c3.put("cameraName", "🏟️ 户外大操场 - 体育活动广角视角");
        c3.put("location", "中央大操场");
        c3.put("status", "ONLINE");
        c3.put("resolution", "4K 超清");
        c3.put("streamType", "WebRTC");
        c3.put("hlsUrl", "https://sample-videos.com/video321/mp4/720/big_buck_bunny_720p_1mb.mp4");
        c3.put("previewImage", "https://picsum.photos/400/225?random=3");
        c3.put("supportPtz", true);
        list.add(c3);

        Map<String, Object> c4 = new HashMap<>();
        c4.put("id", 104);
        c4.put("cameraName", "🥗 阳光后厨与食谱烹饪监控");
        c4.put("location", "食堂后厨区");
        c4.put("status", "ONLINE");
        c4.put("resolution", "1080P");
        c4.put("streamType", "HLS");
        c4.put("hlsUrl", "https://sample-videos.com/video321/mp4/720/big_buck_bunny_720p_1mb.mp4");
        c4.put("previewImage", "https://picsum.photos/400/225?random=4");
        c4.put("supportPtz", false);
        list.add(c4);

        return R.ok(list);
    }
}
