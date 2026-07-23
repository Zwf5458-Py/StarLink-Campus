CREATE TABLE IF NOT EXISTS `kg_staff_attendance` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `staff_id` bigint(20) NOT NULL COMMENT '教职工ID',
  `staff_name` varchar(50) DEFAULT NULL COMMENT '教职工姓名',
  `role_type` varchar(50) DEFAULT NULL COMMENT '角色类型',
  `shift_type` varchar(50) DEFAULT NULL COMMENT '班次',
  `attendance_status` varchar(50) DEFAULT NULL COMMENT '出勤状态描述',
  `overtime_hours` double DEFAULT 0 COMMENT '加班时间',
  `attendance_date` date NOT NULL COMMENT '考勤日期',
  `check_in_time` datetime DEFAULT NULL COMMENT '签到时间',
  `check_out_time` datetime DEFAULT NULL COMMENT '签退时间',
  `status` varchar(20) DEFAULT 'NORMAL' COMMENT '状态: NORMAL, LATE, LEAVE',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='教职工考勤表';
