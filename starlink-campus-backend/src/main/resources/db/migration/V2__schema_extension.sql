-- 补充业务表结构: 访客记录、巡检记录、维修工单、通知公告、校本选课、园务OA审批

CREATE TABLE IF NOT EXISTS `kg_visitor_record` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
  `visitor_name` VARCHAR(50) NOT NULL COMMENT '访客姓名',
  `visitor_phone` VARCHAR(20) NOT NULL COMMENT '联系电话',
  `visit_reason` VARCHAR(255) COMMENT '来访事由',
  `visit_date` DATE COMMENT '预约日期',
  `expected_arrival_time` DATETIME COMMENT '预计到达时间',
  `pass_code` VARCHAR(100) UNIQUE COMMENT '通行码Code',
  `qrcode_url` VARCHAR(255) COMMENT '二维码图片URL',
  `check_in_time` DATETIME COMMENT '入园时间',
  `check_out_time` DATETIME COMMENT '离园时间',
  `status` VARCHAR(20) DEFAULT '在园中' COMMENT '通行状态: 已预约, 在园中, 已离园',
  `overtime_alerted` TINYINT DEFAULT 0 COMMENT '超时滞留告警: 0否, 1是',
  `receiver_staff_id` BIGINT COMMENT '接待教职工ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB COMMENT='访客记录表';

CREATE TABLE IF NOT EXISTS `kg_patrol_record` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '巡检记录ID',
  `patrol_point_name` VARCHAR(100) NOT NULL COMMENT '巡更点位名称',
  `patrol_staff_id` BIGINT NOT NULL COMMENT '巡检安保员ID',
  `patrol_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '打卡时间',
  `photo_url` VARCHAR(255) COMMENT '拍照防伪水印图片URL',
  `watermark_info` VARCHAR(255) COMMENT '水印文本信息(时间/坐标/人员)',
  `is_normal` TINYINT DEFAULT 1 COMMENT '设备状态: 1正常, 0异常',
  `abnormal_desc` VARCHAR(255) COMMENT '异常描述',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB COMMENT='安防巡检记录表';

CREATE TABLE IF NOT EXISTS `kg_repair_order` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '工单ID',
  `patrol_record_id` BIGINT COMMENT '关联巡检记录ID',
  `order_no` VARCHAR(50) UNIQUE NOT NULL COMMENT '工单编号',
  `description` VARCHAR(255) NOT NULL COMMENT '故障描述',
  `status` VARCHAR(20) DEFAULT '待处理' COMMENT '工单状态: 待处理, 处理中, 已完成',
  `assignee_id` BIGINT COMMENT '维修响应人员ID',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
  `finish_time` DATETIME COMMENT '完成时间'
) ENGINE=InnoDB COMMENT='后勤维修工单表';

CREATE TABLE IF NOT EXISTS `kg_notification` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '通知ID',
  `title` VARCHAR(100) NOT NULL COMMENT '通知标题',
  `content` TEXT NOT NULL COMMENT '通知内容',
  `target_type` VARCHAR(20) DEFAULT 'ALL' COMMENT '目标类型: ALL全员, CLASS特定班级, GRADE特定年级',
  `target_id` BIGINT COMMENT '目标ID',
  `sender_staff_id` BIGINT COMMENT '发送人ID',
  `is_read` TINYINT DEFAULT 0 COMMENT '已读状态',
  `read_time` DATETIME COMMENT '阅读时间',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB COMMENT='通知公告表';

CREATE TABLE IF NOT EXISTS `kg_course` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '课程ID',
  `course_name` VARCHAR(100) NOT NULL COMMENT '选修活动/课程名称',
  `campus_name` VARCHAR(50) DEFAULT '校本部' COMMENT '所属校区',
  `target_grades` VARCHAR(100) COMMENT '参加选修的年级',
  `teacher_name` VARCHAR(50) COMMENT '任课教师',
  `teacher_submit_time` DATETIME COMMENT '教师申报截止时间',
  `student_select_time` DATETIME COMMENT '学生选课时间',
  `status` VARCHAR(30) DEFAULT '已结束' COMMENT '选课状态: 未开始, 已开始, 已结束',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB COMMENT='校本选课活动表';

-- 7. 园务管理系统 OA 审批表
CREATE TABLE IF NOT EXISTS `kg_oa_approval` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '审批ID',
  `applicant_name` VARCHAR(50) NOT NULL COMMENT '申请人',
  `applicant_role` VARCHAR(30) COMMENT '申请人角色(教师/保育员)',
  `approval_type` VARCHAR(30) NOT NULL COMMENT '审批类型: 病假申请, 事假申请, 补卡申请, 后勤采购',
  `reason` VARCHAR(255) NOT NULL COMMENT '申请原因',
  `status` VARCHAR(20) DEFAULT '待审批' COMMENT '状态: 待审批, 已通过, 已驳回',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB COMMENT='园务OA审批表';

-- 初始化数据
INSERT INTO `kg_oa_approval` (`id`, `applicant_name`, `applicant_role`, `approval_type`, `reason`, `status`) VALUES
(1, '李老师', '大班班主任', '病假申请', '急性咽喉炎需请假1天', '待审批'),
(2, '王医生', '保健医师', '后勤采购', '采购秋季体温枪与消毒液储备', '待审批'),
(3, '张阿姨', '保育员', '补卡申请', '08-20 入园刷脸考勤未成功补卡', '已通过');
