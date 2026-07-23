-- AI 助手相关数据库表结构

CREATE TABLE IF NOT EXISTS `kg_ai_prompt_template` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `template_code` VARCHAR(50) NOT NULL COMMENT '模版编码',
    `template_name` VARCHAR(100) NOT NULL COMMENT '模版名称',
    `scene_type` VARCHAR(30) NOT NULL COMMENT '适用场景: GROWTH_COMMENT/WEEKLY_PLAN/MENU_NUTRITION/NOTICE',
    `prompt_pattern` TEXT NOT NULL COMMENT 'Prompt 模版内容',
    `status` VARCHAR(10) DEFAULT '启用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY `uk_code` (`template_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI Prompt 模版表';

CREATE TABLE IF NOT EXISTS `kg_ai_knowledge_base` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `title` VARCHAR(200) NOT NULL COMMENT '知识标题',
    `category` VARCHAR(50) NOT NULL COMMENT '分类: 作息规则/缴费说明/请假退费/入园须知/安全防护',
    `content` TEXT NOT NULL COMMENT '知识正文',
    `tags` VARCHAR(200) COMMENT '标签列表 (逗号分隔)',
    `status` VARCHAR(10) DEFAULT '启用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_category` (`category`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='园务 AI 知识库表';

CREATE TABLE IF NOT EXISTS `kg_ai_log` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` BIGINT COMMENT '调用者 ID',
    `user_type` VARCHAR(20) COMMENT 'STAFF/PARENT',
    `scene_type` VARCHAR(30) COMMENT '调用的场景',
    `prompt` TEXT COMMENT '输入 Prompt',
    `result` TEXT COMMENT 'AI 返回结果',
    `tokens_used` INT DEFAULT 0 COMMENT '消耗 Token 数',
    `model_name` VARCHAR(50) DEFAULT 'DeepSeek-V3' COMMENT '使用的 AI 模型',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX `idx_user` (`user_id`),
    INDEX `idx_scene` (`scene_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI 调用日志表';

-- 预置标准 Prompt 模版
INSERT INTO `kg_ai_prompt_template` (`template_code`, `template_name`, `scene_type`, `prompt_pattern`) VALUES
('GROWTH_COMMENT_V1', '幼儿成长评语生成', 'GROWTH_COMMENT', '请作为一名资深学前教育专家，针对幼儿在校表现关键词：“{keywords}”，学期：“{semester}”，撰写一段约 150 字温馨、专业、鼓励性的教师评语。结构包括：优点关怀、发展表现、寄语期望。'),
('WEEKLY_PLAN_V1', '教学周计划建议生成', 'WEEKLY_PLAN', '请作为一名幼儿园教研组长，围绕本周主题：“{theme}”，为{targetAge}班级设计一份周教学计划，包含健康、语言、社会、科学、艺术五大领域的简要活动目标与建议。'),
('MENU_NUTRITION_V1', '每周食谱营养分析', 'MENU_NUTRITION', '请作为一名专业幼儿营养师，分析以下每周食谱：“{dishes}”的营养搭配合理性（蛋白质、碳水、维生素占比），并提出 2 条优化或替代建议。')
ON DUPLICATE KEY UPDATE `template_name` = VALUES(`template_name`);

-- 预置常见园务知识
INSERT INTO `kg_ai_knowledge_base` (`title`, `category`, `content`, `tags`) VALUES
('幼儿园作息时间安排', '作息规则', '早上 7:50-8:30 入园晨检；8:30-9:00 早餐；9:00-11:30 领域教学与户外活动；11:30-12:15 午餐；12:30-14:30 午睡；14:30-15:00 午点；15:00-16:30 游戏与离园准备；16:30 离园。', '作息,晨检,离园'),
('缺勤退费与伙食费计算标准', '请假退费', '根据园所管理规定，幼儿连续缺勤超过 5 个工作日（含）以上并办理请假手续的，伙食费按 20 元/天 退还，在次月缴费账单中自动扣减；保教费按学期结算，中途退园按剩余天数折算。', '退费,请假,伙食费'),
('入园接送卡与接送人安全规定', '入园须知', '每位幼儿最多可绑定 3 名经常接送的家属（需提交身份证及人脸信息）。临时委托他人接送必须提前在微信小程序中提交授权申请并由班主任审核确认。', '接送,安全,接送人')
ON DUPLICATE KEY UPDATE `title` = VALUES(`title`);
