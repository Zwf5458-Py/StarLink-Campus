CREATE TABLE kg_camera (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    camera_name VARCHAR(100) NOT NULL COMMENT '摄像头名称',
    location VARCHAR(100) NOT NULL COMMENT '位置',
    status VARCHAR(20) NOT NULL DEFAULT 'ONLINE' COMMENT '状态',
    resolution VARCHAR(50) COMMENT '分辨率',
    stream_type VARCHAR(20) COMMENT '流类型',
    hls_url VARCHAR(255) COMMENT '流地址',
    preview_image VARCHAR(255) COMMENT '预览图',
    support_ptz BOOLEAN DEFAULT FALSE COMMENT '是否支持云台',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='监控摄像头表';

INSERT INTO kg_camera (id, camera_name, location, status, resolution, stream_type, hls_url, preview_image, support_ptz) VALUES
(101, '📷 园区正门入口 - 晨检卡口双目相机', '一楼正门广场', 'ONLINE', '1080P / 60fps', 'HLS / RTSP', 'https://sample-videos.com/video321/mp4/720/big_buck_bunny_720p_1mb.mp4', 'https://picsum.photos/400/225?random=1', TRUE),
(102, '📹 小(1)班 雏菊班 活动教室内景', '教学楼 201 教室', 'ONLINE', '1080P / 30fps', 'HLS', 'https://sample-videos.com/video321/mp4/720/big_buck_bunny_720p_1mb.mp4', 'https://picsum.photos/400/225?random=2', FALSE),
(103, '🏟️ 户外大操场 - 体育活动广角视角', '中央大操场', 'ONLINE', '4K 超清', 'WebRTC', 'https://sample-videos.com/video321/mp4/720/big_buck_bunny_720p_1mb.mp4', 'https://picsum.photos/400/225?random=3', TRUE),
(104, '🥗 阳光后厨与食谱烹饪监控', '食堂后厨区', 'ONLINE', '1080P', 'HLS', 'https://sample-videos.com/video321/mp4/720/big_buck_bunny_720p_1mb.mp4', 'https://picsum.photos/400/225?random=4', FALSE);
