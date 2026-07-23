-- 文章分类表
CREATE TABLE IF NOT EXISTS `kg_article_category` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `category_name` varchar(100) NOT NULL COMMENT '分类名称',
  `sort_order` int(11) DEFAULT '0' COMMENT '排序',
  `status` tinyint(4) DEFAULT '1' COMMENT '1启用 0停用',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章分类表';

-- 文章评论表
CREATE TABLE IF NOT EXISTS `kg_article_comment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `article_id` bigint(20) NOT NULL COMMENT '文章ID',
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父评论ID',
  `author_name` varchar(100) NOT NULL COMMENT '评论者姓名',
  `content` text NOT NULL COMMENT '评论内容',
  `status` varchar(20) DEFAULT '待审核' COMMENT '状态: 待审核/已审核/已拒绝',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章评论表';

-- 预置分类种子数据
INSERT INTO `kg_article_category` (`category_name`, `sort_order`, `status`) VALUES
('园务公告', 1, 1),
('教学动态', 2, 1),
('食育健康', 3, 1),
('活动风采', 4, 1),
('政策通知', 5, 1);
