-- 巡检点位表
CREATE TABLE IF NOT EXISTS `kg_patrol_point` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `point_name` varchar(100) NOT NULL COMMENT '点位名称',
  `location` varchar(255) DEFAULT NULL COMMENT '位置描述',
  `device_type` varchar(50) DEFAULT NULL COMMENT '设备类型',
  `sort_order` int(11) DEFAULT '0' COMMENT '排序',
  `status` tinyint(4) DEFAULT '1' COMMENT '1启用 0停用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='巡检点位表';

-- 巡检路线表
CREATE TABLE IF NOT EXISTS `kg_patrol_route` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `route_name` varchar(100) NOT NULL COMMENT '路线名称',
  `point_ids` varchar(500) NOT NULL COMMENT '点位ID列表,逗号分隔',
  `period` varchar(50) DEFAULT 'DAILY' COMMENT '周期: DAILY/WEEKLY',
  `status` tinyint(4) DEFAULT '1' COMMENT '1启用 0停用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='巡检路线表';

-- 预置 24 个巡更点位种子数据
INSERT INTO `kg_patrol_point` (`point_name`, `location`, `device_type`, `sort_order`, `status`) VALUES
('大门岗亭', '主入口大门处', '摄像头', 1, 1),
('教学楼A', '教学楼A区走廊', '消防栓', 2, 1),
('教学楼B', '教学楼B区走廊', '门禁', 3, 1),
('教学楼C', '教学楼C区走廊', '监控', 4, 1),
('食堂操作间', '一层食堂内部', '烟感', 5, 1),
('食堂储物间', '一层食堂后厨', '监控', 6, 1),
('操场游乐区', '室外操场东侧', '无', 7, 1),
('操场沙地', '室外操场南侧', '无', 8, 1),
('消防通道A', '东侧消防通道', '消防栓', 9, 1),
('消防通道B', '西侧消防通道', '消防栓', 10, 1),
('保健室', '一楼保健室', '门禁', 11, 1),
('图书角', '二楼公共活动区', '监控', 12, 1),
('音乐教室', '三楼音乐室', '门禁', 13, 1),
('美术教室', '三楼美术室', '门禁', 14, 1),
('园长办公室', '二楼行政区', '门禁', 15, 1),
('监控中心', '一楼安保室旁', '监控', 16, 1),
('配电房', '地下室东侧', '门禁', 17, 1),
('水泵房', '地下室西侧', '门禁', 18, 1),
('洗手间A', '一楼东侧洗手间', '无', 19, 1),
('洗手间B', '二楼西侧洗手间', '无', 20, 1),
('楼梯间A', '东侧主楼梯', '应急灯', 21, 1),
('楼梯间B', '西侧副楼梯', '应急灯', 22, 1),
('后花园', '校园北部绿化区', '监控', 23, 1),
('停车场', '地下停车场', '监控', 24, 1),
('垃圾房', '校园西北角', '无', 25, 1);

-- 预置 3 条默认巡检路线
INSERT INTO `kg_patrol_route` (`route_name`, `point_ids`, `period`, `status`) VALUES
('日常全园巡检路线', '1,2,3,4,5,7,9,11,16,17', 'DAILY', 1),
('重点消防巡检路线', '2,5,9,10,17,18', 'WEEKLY', 1),
('夜间安全巡视路线', '1,6,12,15,16,17,23,24', 'DAILY', 1);
