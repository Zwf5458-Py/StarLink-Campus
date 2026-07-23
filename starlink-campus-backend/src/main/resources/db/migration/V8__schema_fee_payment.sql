CREATE TABLE IF NOT EXISTS `kg_fee_item` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `fee_name` VARCHAR(100) NOT NULL COMMENT '费用名称',
    `fee_type` VARCHAR(30) COMMENT '类型: 伙食费/保教费/活动费/材料费',
    `amount` DECIMAL(10,2) NOT NULL COMMENT '金额',
    `deadline` DATE COMMENT '截止日期',
    `applicable_class_ids` JSON COMMENT '适用班级ID列表',
    `semester` VARCHAR(20) COMMENT '学期',
    `status` VARCHAR(10) DEFAULT '生效' COMMENT '状态',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_semester` (`semester`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='费用项目';

CREATE TABLE IF NOT EXISTS `kg_payment` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `student_id` BIGINT NOT NULL COMMENT '学生ID',
    `fee_item_id` BIGINT NOT NULL COMMENT '费用项ID',
    `amount` DECIMAL(10,2) NOT NULL COMMENT '缴费金额',
    `pay_time` DATETIME COMMENT '缴费时间',
    `pay_channel` VARCHAR(20) COMMENT '支付渠道',
    `transaction_no` VARCHAR(50) COMMENT '交易号',
    `status` VARCHAR(10) DEFAULT '待缴' COMMENT '状态',
    `remark` VARCHAR(200) COMMENT '备注',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_student` (`student_id`),
    INDEX `idx_fee_item` (`fee_item_id`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='缴费记录';
