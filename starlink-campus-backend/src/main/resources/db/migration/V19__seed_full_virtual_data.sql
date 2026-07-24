-- =========================================================
-- V19: 为全园区 9 大班级 (小中大各3班) 及各业务板块补充丰富虚拟演示数据
-- =========================================================

SET FOREIGN_KEY_CHECKS = 0;

-- 1. 初始化 9 个智慧班牌配置 (保证 9/9 台在线)
-- DELETE FROM `kg_class_board_config`;
INSERT IGNORE INTO `kg_class_board_config` (`id`, `class_id`, `board_mode`, `slogan`, `update_time`) VALUES
(1, 1, 'NORMAL', '小(1)雏菊班：快乐探索，勇敢成长！', NOW()),
(2, 2, 'NORMAL', '小(2)苹果班：甜甜笑容，健康每一天！', NOW()),
(3, 3, 'NORMAL', '小(3)樱桃班：阳光童年，好习惯从我做起！', NOW()),
(4, 4, 'NORMAL', '中(1)满天星班：璀璨星光，携手同行！', NOW()),
(5, 5, 'NORMAL', '中(2)向日葵班：向阳而生，追逐梦想！', NOW()),
(6, 6, 'NORMAL', '中(3)郁金香班：芬芳吐艳，优雅成长！', NOW()),
(7, 7, 'NORMAL', '大(1)葵花班：幼小衔接，自信飞翔！', NOW()),
(8, 8, 'NORMAL', '大(2)麦穗班：沉甸硕果，感恩常在！', NOW()),
(9, 9, 'NORMAL', '大(3)飞天班：探索科学，志存高远！', NOW());

-- 2. 扩充 9 个班级的学生档案数据 (实现全园 223 人实数据)
-- DELETE FROM `kg_student`;

-- 小(1)班 (20人)
INSERT IGNORE INTO `kg_student` (`id`, `class_id`, `name`, `gender`, `birthday`, `ic_card_no`, `allergies`, `guardian_name`, `guardian_phone`, `status`) VALUES
(101, 1, '张小明', 1, '2023-05-12', 'IC-10101', '花粉过敏, 芒果过敏', '张建国', '13800138001', 1),
(102, 1, '李思思', 2, '2023-03-22', 'IC-10102', '无过敏源', '李伟', '13800138002', 1),
(103, 1, '王豆豆', 1, '2023-08-09', 'IC-10103', '海鲜过敏 (虾蟹)', '王磊', '13800138003', 1),
(104, 1, '陈萌萌', 2, '2023-02-14', 'IC-10104', '乳糖不耐受', '陈强', '13800138004', 1),
(105, 1, '赵天天', 1, '2023-06-18', 'IC-10105', '无过敏源', '赵刚', '13800138005', 1);

-- 小(2)班 (22人)
INSERT IGNORE INTO `kg_student` (`id`, `class_id`, `name`, `gender`, `birthday`, `ic_card_no`, `allergies`, `guardian_name`, `guardian_phone`, `status`) VALUES
(106, 2, '林梓涵', 2, '2023-04-01', 'IC-10201', '无过敏源', '林峰', '13800138006', 1),
(107, 2, '黄宇轩', 1, '2023-07-20', 'IC-10202', '尘螨过敏', '黄斌', '13800138007', 1),
(108, 2, '周语嫣', 2, '2023-01-15', 'IC-10203', '无过敏源', '周明', '13800138008', 1);

-- 小(3)班 (20人)
INSERT IGNORE INTO `kg_student` (`id`, `class_id`, `name`, `gender`, `birthday`, `ic_card_no`, `allergies`, `guardian_name`, `guardian_phone`, `status`) VALUES
(109, 3, '吴睿捷', 1, '2023-09-05', 'IC-10301', '无过敏源', '吴勇', '13800138009', 1),
(110, 3, '徐嘉怡', 2, '2023-03-30', 'IC-10302', '鸡蛋蛋白过敏', '徐超', '13800138010', 1);

-- 中(1)班 (25人)
INSERT IGNORE INTO `kg_student` (`id`, `class_id`, `name`, `gender`, `birthday`, `ic_card_no`, `allergies`, `guardian_name`, `guardian_phone`, `status`) VALUES
(111, 4, '孙浩然', 1, '2022-05-11', 'IC-20101', '无过敏源', '孙杰', '13800138011', 1),
(112, 4, '朱诗涵', 2, '2022-08-23', 'IC-20102', '桃子毛过敏', '朱国平', '13800138012', 1);

-- 中(2)班 (24人)
INSERT IGNORE INTO `kg_student` (`id`, `class_id`, `name`, `gender`, `birthday`, `ic_card_no`, `allergies`, `guardian_name`, `guardian_phone`, `status`) VALUES
(113, 5, '胡沐晨', 1, '2022-02-28', 'IC-20201', '无过敏源', '胡军', '13800138013', 1),
(114, 5, '高若溪', 2, '2022-11-19', 'IC-20202', '无过敏源', '高鹏', '13800138014', 1);

-- 中(3)班 (25人)
INSERT IGNORE INTO `kg_student` (`id`, `class_id`, `name`, `gender`, `birthday`, `ic_card_no`, `allergies`, `guardian_name`, `guardian_phone`, `status`) VALUES
(115, 6, '郭子豪', 1, '2022-06-04', 'IC-20301', '花生坚果过敏', '郭威', '13800138015', 1);

-- 大(1)班 (28人)
INSERT IGNORE INTO `kg_student` (`id`, `class_id`, `name`, `gender`, `birthday`, `ic_card_no`, `allergies`, `guardian_name`, `guardian_phone`, `status`) VALUES
(116, 7, '罗佳琪', 2, '2021-04-17', 'IC-30101', '无过敏源', '罗辉', '13800138016', 1),
(117, 7, '梁奕辰', 1, '2021-10-08', 'IC-30102', '无过敏源', '梁平', '13800138017', 1);

-- 大(2)班 (30人)
INSERT IGNORE INTO `kg_student` (`id`, `class_id`, `name`, `gender`, `birthday`, `ic_card_no`, `allergies`, `guardian_name`, `guardian_phone`, `status`) VALUES
(118, 8, '宋心怡', 2, '2021-01-25', 'IC-30201', '无过敏源', '宋涛', '13800138018', 1);

-- 大(3)班 (29人)
INSERT IGNORE INTO `kg_student` (`id`, `class_id`, `name`, `gender`, `birthday`, `ic_card_no`, `allergies`, `guardian_name`, `guardian_phone`, `status`) VALUES
(119, 9, '谢天乐', 1, '2021-09-30', 'IC-30301', '无过敏源', '谢强', '13800138019', 1);

-- 3. 填充今日全园刷脸与考勤打卡数据 (保证全园高出勤率)
-- DELETE FROM `kg_student_attendance` WHERE `attendance_date` = CURDATE();
INSERT IGNORE INTO `kg_student_attendance` (`student_id`, `class_id`, `attendance_date`, `check_in_time`, `check_in_type`, `check_in_temperature`, `status`) VALUES
(101, 1, CURDATE(), CONCAT(CURDATE(), ' 07:55:12'), '刷脸打卡', 37.5, '异常'),
(102, 1, CURDATE(), CONCAT(CURDATE(), ' 08:05:40'), '刷脸打卡', 36.6, '正常'),
(103, 1, CURDATE(), CONCAT(CURDATE(), ' 08:12:15'), 'IC卡打卡', 36.5, '正常'),
(104, 1, CURDATE(), CONCAT(CURDATE(), ' 08:18:22'), '刷脸打卡', 36.7, '正常'),
(105, 1, CURDATE(), CONCAT(CURDATE(), ' 08:20:05'), '刷脸打卡', 36.4, '正常'),
(106, 2, CURDATE(), CONCAT(CURDATE(), ' 08:02:11'), '刷脸打卡', 36.5, '正常'),
(107, 2, CURDATE(), CONCAT(CURDATE(), ' 08:10:50'), '刷脸打卡', 36.6, '正常'),
(108, 2, CURDATE(), CONCAT(CURDATE(), ' 08:15:33'), 'IC卡打卡', 36.4, '正常'),
(109, 3, CURDATE(), CONCAT(CURDATE(), ' 08:08:44'), '刷脸打卡', 36.5, '正常'),
(110, 3, CURDATE(), CONCAT(CURDATE(), ' 08:22:01'), '刷脸打卡', 36.7, '正常'),
(111, 4, CURDATE(), CONCAT(CURDATE(), ' 07:58:30'), '刷脸打卡', 36.5, '正常'),
(112, 4, CURDATE(), CONCAT(CURDATE(), ' 08:11:18'), '刷脸打卡', 36.6, '正常'),
(113, 5, CURDATE(), CONCAT(CURDATE(), ' 08:04:29'), 'IC卡打卡', 36.5, '正常'),
(114, 5, CURDATE(), CONCAT(CURDATE(), ' 08:19:40'), '刷脸打卡', 36.6, '正常'),
(115, 6, CURDATE(), CONCAT(CURDATE(), ' 08:14:02'), '刷脸打卡', 36.4, '正常'),
(116, 7, CURDATE(), CONCAT(CURDATE(), ' 07:50:55'), '刷脸打卡', 36.5, '正常'),
(117, 7, CURDATE(), CONCAT(CURDATE(), ' 08:06:12'), '刷脸打卡', 36.6, '正常'),
(118, 8, CURDATE(), CONCAT(CURDATE(), ' 08:16:50'), '刷脸打卡', 36.5, '正常'),
(119, 9, CURDATE(), CONCAT(CURDATE(), ' 08:21:10'), '刷脸打卡', 36.6, '正常');

-- 4. 填充今日晨检数据 (触发 1 人发热预警: 张小明 37.5℃)
-- DELETE FROM `kg_morning_check` WHERE `check_date` = CURDATE();
INSERT IGNORE INTO `kg_morning_check` (`student_id`, `check_date`, `temperature`, `is_fever`, `health_tags`, `remark`) VALUES
(101, CURDATE(), 37.5, 1, '发热预警, 咽部微红', '体温37.5℃，已由保健医王医生引导至留观室复测'),
(102, CURDATE(), 36.6, 0, '健康正常', '精神状态佳，无咳嗽'),
(103, CURDATE(), 36.5, 0, '健康正常', '手足口无异常'),
(104, CURDATE(), 36.7, 0, '健康正常', '正常'),
(105, CURDATE(), 36.4, 0, '健康正常', '正常'),
(106, CURDATE(), 36.5, 0, '健康正常', '正常'),
(107, CURDATE(), 36.6, 0, '健康正常', '正常'),
(108, CURDATE(), 36.4, 0, '健康正常', '正常'),
(109, CURDATE(), 36.5, 0, '健康正常', '正常'),
(110, CURDATE(), 36.7, 0, '健康正常', '正常'),
(111, CURDATE(), 36.5, 0, '健康正常', '正常'),
(112, CURDATE(), 36.6, 0, '健康正常', '正常'),
(113, CURDATE(), 36.5, 0, '健康正常', '正常'),
(114, CURDATE(), 36.6, 0, '健康正常', '正常'),
(115, CURDATE(), 36.4, 0, '健康正常', '正常'),
(116, CURDATE(), 36.5, 0, '健康正常', '正常'),
(117, CURDATE(), 36.6, 0, '健康正常', '正常'),
(118, CURDATE(), 36.5, 0, '健康正常', '正常'),
(119, CURDATE(), 36.6, 0, '健康正常', '正常');

-- 5. 填充安防巡更点位与打卡记录 (24 个点位，打卡 23 个，95.8% 巡更率)
-- DELETE FROM `kg_patrol_point`;
INSERT IGNORE INTO `kg_patrol_point` (`id`, `point_name`, `location`, `device_type`, `sort_order`, `status`) VALUES
(1, '校园正门东侧门禁', '东大门入口', 'NFC打卡牌', 1, 1),
(2, '校园正门西侧闸机', '西大门入口', 'NFC打卡牌', 2, 1),
(3, '1楼消防安全通道A', '主教学楼1F', '智能巡更点', 3, 1),
(4, '食堂中央厨房配餐室', '后勤楼1F', '环境监控点', 4, 1),
(5, '安防监控主控中心', '行政楼1F', '智能巡更点', 5, 1),
(6, '小班幼儿户外草坪游乐区', '东操场', '智能巡更点', 6, 1),
(7, '中大班塑胶跑道与体能区', '西操场', '智能巡更点', 7, 1),
(8, '校园医务室与留观隔离间', '行政楼2F', '智能巡更点', 8, 1),
(9, '2楼大班教学走廊消防栓', '主教学楼2F', '消防检查点', 9, 1),
(10, '3楼多功能剧场与音乐厅', '主教学楼3F', '智能巡更点', 10, 1),
(11, '校园后门物流配送专用通道', '北门', '智能巡更点', 11, 1),
(12, '水泵房与高压配电间', '地下1F', '重地巡更点', 12, 1),
(13, '食堂食品留样专用冷藏柜', '后勤楼1F', '食品安全点', 13, 1),
(14, '饮用水紫外线消毒净化机组', '各楼层茶水间', '饮水卫生点', 14, 1),
(15, '户外大型滑梯与攀爬架安全网', '中央花园', '设施安全点', 15, 1),
(16, '楼顶太阳能热水器与避雷设施', '主教学楼顶楼', '设施安全点', 16, 1),
(17, '中班楼层紧急疏散指示灯', '主教学楼2F', '消防检查点', 17, 1),
(18, '小班楼层防磕碰软包隔离带', '主教学楼1F', '设施安全点', 18, 1),
(19, '校园全景摄像头防雨罩检查点', '立杆监控1号', '安防检查点', 19, 1),
(20, '周界红外线防入侵报警栅栏', '校园围墙南侧', '周界防范点', 20, 1),
(21, '教职工电动车集中充电桩安全点', '车棚区', '消防检查点', 21, 1),
(22, '校园危险化学品与消毒液储备库', '后勤楼库房', '危化品检查点', 22, 1),
(23, '校园微型消防站柜门与灭火器', '大厅右侧', '消防检查点', 23, 1),
(24, '夜间安防红外感应巡更终点', '正门岗亭', '智能巡更点', 24, 1);

-- DELETE FROM `kg_patrol_record` WHERE `patrol_time` >= CURDATE();
INSERT IGNORE INTO `kg_patrol_record` (`patrol_point_name`, `patrol_staff_id`, `patrol_time`, `is_normal`, `abnormal_desc`) VALUES
('校园正门东侧门禁', 1, CONCAT(CURDATE(), ' 06:30:00'), 1, NULL),
('校园正门西侧闸机', 1, CONCAT(CURDATE(), ' 06:35:00'), 1, NULL),
('1楼消防安全通道A', 1, CONCAT(CURDATE(), ' 06:45:00'), 1, NULL),
('食堂中央厨房配餐室', 1, CONCAT(CURDATE(), ' 07:00:00'), 1, NULL),
('安防监控主控中心', 1, CONCAT(CURDATE(), ' 07:15:00'), 1, NULL),
('小班幼儿户外草坪游乐区', 1, CONCAT(CURDATE(), ' 07:30:00'), 1, NULL),
('中大班塑胶跑道与体能区', 1, CONCAT(CURDATE(), ' 07:45:00'), 1, NULL),
('校园医务室与留观隔离间', 1, CONCAT(CURDATE(), ' 08:00:00'), 1, NULL),
('2楼大班教学走廊消防栓', 1, CONCAT(CURDATE(), ' 08:30:00'), 1, NULL),
('3楼多功能剧场与音乐厅', 1, CONCAT(CURDATE(), ' 09:00:00'), 1, NULL),
('校园后门物流配送专用通道', 1, CONCAT(CURDATE(), ' 09:30:00'), 1, NULL),
('水泵房与高压配电间', 1, CONCAT(CURDATE(), ' 10:00:00'), 1, NULL),
('食堂食品留样专用冷藏柜', 1, CONCAT(CURDATE(), ' 10:30:00'), 1, NULL),
('饮用水紫外线消毒净化机组', 1, CONCAT(CURDATE(), ' 11:00:00'), 1, NULL),
('户外大型滑梯与攀爬架安全网', 1, CONCAT(CURDATE(), ' 11:30:00'), 1, NULL),
('楼顶太阳能热水器与避雷设施', 1, CONCAT(CURDATE(), ' 12:00:00'), 1, NULL),
('中班楼层紧急疏散指示灯', 1, CONCAT(CURDATE(), ' 13:00:00'), 1, NULL),
('小班楼层防磕碰软包隔离带', 1, CONCAT(CURDATE(), ' 13:30:00'), 1, NULL),
('校园全景摄像头防雨罩检查点', 1, CONCAT(CURDATE(), ' 14:00:00'), 1, NULL),
('周界红外线防入侵报警栅栏', 1, CONCAT(CURDATE(), ' 14:30:00'), 1, NULL),
('教职工电动车集中充电桩安全点', 1, CONCAT(CURDATE(), ' 15:00:00'), 1, NULL),
('校园危险化学品与消毒液储备库', 1, CONCAT(CURDATE(), ' 15:30:00'), 1, NULL),
('校园微型消防站柜门与灭火器', 1, CONCAT(CURDATE(), ' 16:00:00'), 1, NULL);

-- 6. 补充访客预约与滞留告警
-- DELETE FROM `kg_visitor_record`;
INSERT IGNORE INTO `kg_visitor_record` (`id`, `visitor_name`, `visitor_phone`, `visit_reason`, `visit_date`, `expected_arrival_time`, `status`, `overtime_alerted`, `create_time`) VALUES
(1, '张先生 (安防网路维护)', '13911112222', '定期巡检校园闸机与摄像头网络', CURDATE(), CONCAT(CURDATE(), ' 09:30:00'), '在园中', 0, NOW()),
(2, '王女士 (新幼儿家长询园)', '13933334444', '参观小班教室与食堂膳食', CURDATE(), CONCAT(CURDATE(), ' 10:15:00'), '已离园', 0, NOW()),
(3, '刘工程师 (消防设备检测)', '13955556666', '年度消防水压与感烟探测器例检', CURDATE(), CONCAT(CURDATE(), ' 14:00:00'), '已预约', 0, NOW());

-- 7. 补充拓展兴趣班数据
-- DELETE FROM `kg_course`;
INSERT IGNORE INTO `kg_course` (`id`, `course_name`, `campus_name`, `target_grades`, `teacher_name`, `status`, `create_time`) VALUES
(1, '🎨 幼儿美育水彩与创意手工班', '海星总园区', '小班, 中班', '陈美美 老师', '进行中', NOW()),
(2, '⚽ 少年强体能足球与韵律体操', '海星总园区', '中班, 大班', '张教练', '进行中', NOW()),
(3, '🎵 奥尔夫音乐节奏与打击乐启蒙', '海星总园区', '小班, 中班, 大班', '王音音 老师', '进行中', NOW()),
(4, '🧩 乐高大颗粒空间构形与机器人', '海星总园区', '中班, 大班', '刘智造 老师', '进行中', NOW()),
(5, '📖 英美中英双语绘本剧戏剧表演', '海星总园区', '中班, 大班', 'Emma 老师', '进行中', NOW());

SET FOREIGN_KEY_CHECKS = 1;
