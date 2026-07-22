-- 04_schema_rbac.sql
-- 智慧校园 RBAC 权限管理扩展

-- 1. 角色表
CREATE TABLE IF NOT EXISTS `kg_role` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称(如: 园长, 班主任)',
  `role_key` VARCHAR(50) UNIQUE NOT NULL COMMENT '角色标识(如: admin, teacher)',
  `status` TINYINT DEFAULT 1 COMMENT '状态: 1正常, 0禁用',
  `remark` VARCHAR(255) COMMENT '备注',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 2. 菜单/权限表
CREATE TABLE IF NOT EXISTS `kg_menu` (
  `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
  `parent_id` BIGINT DEFAULT 0 COMMENT '父菜单ID',
  `menu_name` VARCHAR(50) NOT NULL COMMENT '菜单名称',
  `path` VARCHAR(200) COMMENT '路由地址',
  `component` VARCHAR(255) COMMENT '组件路径',
  `perms` VARCHAR(100) COMMENT '权限标识',
  `icon` VARCHAR(100) COMMENT '图标',
  `menu_type` CHAR(1) COMMENT 'M:目录, C:菜单, F:按钮',
  `order_num` INT DEFAULT 0 COMMENT '排序',
  `status` TINYINT DEFAULT 1 COMMENT '状态: 1正常, 0禁用',
  `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='菜单权限表';

-- 3. 角色-菜单关联表
CREATE TABLE IF NOT EXISTS `kg_role_menu` (
  `role_id` BIGINT NOT NULL,
  `menu_id` BIGINT NOT NULL,
  PRIMARY KEY (`role_id`, `menu_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色和菜单关联表';

-- 4. 员工-角色关联表
CREATE TABLE IF NOT EXISTS `kg_staff_role` (
  `staff_id` BIGINT NOT NULL,
  `role_id` BIGINT NOT NULL,
  PRIMARY KEY (`staff_id`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户和角色关联表';

-- 5. 扩展员工表增加认证字段
ALTER TABLE `kg_staff`
ADD COLUMN `username` VARCHAR(50) UNIQUE COMMENT '登录账号(为空则使用手机号)' AFTER `name`,
ADD COLUMN `password` VARCHAR(255) COMMENT '登录密码(BCrypt加密)' AFTER `username`;

-- 6. 初始化基础数据
-- BCrypt "123456" = $2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2
UPDATE `kg_staff` SET `password` = '$2a$10$7JB720yubVSZvUI0rEqK/.VqGOZTH.ulu33dHOiBE8ByOhJIrdAu2' WHERE `password` IS NULL;
UPDATE `kg_staff` SET `username` = `phone` WHERE `username` IS NULL;

-- 插入默认管理员角色
INSERT IGNORE INTO `kg_role` (`id`, `role_name`, `role_key`, `remark`) VALUES (1, '超级管理员', 'admin', '拥有系统最高权限');
INSERT IGNORE INTO `kg_role` (`id`, `role_name`, `role_key`, `remark`) VALUES (2, '班主任', 'teacher', '班级管理权限');
INSERT IGNORE INTO `kg_role` (`id`, `role_name`, `role_key`, `remark`) VALUES (3, '后勤主管', 'logistics', '物资与巡检管理权限');

-- 为已有的员工分配超级管理员 (假设ID 201 为管理员)
INSERT IGNORE INTO `kg_staff_role` (`staff_id`, `role_id`) VALUES (201, 1);
INSERT IGNORE INTO `kg_staff_role` (`staff_id`, `role_id`) VALUES (202, 2);

-- 插入一些基础菜单树
INSERT IGNORE INTO `kg_menu` (`id`, `parent_id`, `menu_name`, `path`, `component`, `perms`, `menu_type`, `order_num`) VALUES
(1, 0, '仪表盘', '/dashboard', 'DashboardView', 'sys:dashboard', 'C', 1),
(2, 0, '教学与幼儿管理', '', '', '', 'M', 2),
(3, 2, '幼儿档案管理', '/student', 'StudentManageView', 'sys:student', 'C', 1),
(4, 2, '选课中心', '/course', 'CourseManageView', 'sys:course', 'C', 2),
(5, 0, '系统管理', '', '', '', 'M', 10),
(6, 5, '权限管理', '/permission', 'PermissionManageView', 'sys:permission', 'C', 1),
(7, 5, '组织架构', '/system', 'SystemManageView', 'sys:dept', 'C', 2);

-- 管理员赋予所有权限
INSERT IGNORE INTO `kg_role_menu` (`role_id`, `menu_id`) SELECT 1, id FROM `kg_menu`;
