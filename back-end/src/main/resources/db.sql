drop database springboot_vue;
CREATE DATABASE springboot_vue;
USE springboot_vue;

/*!40101 SET NAMES UTF8MB4 */;

#
# Structure for table "article"
#

DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`
(
    `id`            int(11)      NOT NULL AUTO_INCREMENT,
    `content`       varchar(255) NOT NULL COMMENT '文章内容',
    `create_time`   timestamp    NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   timestamp    NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `delete_status` varchar(1)        DEFAULT TRUE COMMENT '是否有效',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 20
  DEFAULT CHARSET = UTF8MB4 COMMENT ='发布号作者表';

#
# Data for table "article"
#

INSERT INTO `article` VALUE (1, '这是测试内容1', '2000-01-01 00:00:00', '2000-01-01 00:00:01', '1');
INSERT INTO `article` VALUE (2, '这是测试内容2', '2000-01-01 00:00:01', '2000-01-01 00:00:02', '1');
INSERT INTO `article` VALUE (3, '这是测试内容3', '2000-01-01 00:00:02', '2000-01-01 00:00:03', '1');
INSERT INTO `article` VALUE (4, '这是测试内容4', '2000-01-01 00:00:03', '2000-01-01 00:00:04', '1');
INSERT INTO `article` VALUE (5, '这是测试内容5', '2000-01-01 00:00:04', '2000-01-01 00:00:05', '1');

#
# Structure for table "sys_permission"
#

DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission`
(
    `id`                  int(11) NOT NULL DEFAULT '0' COMMENT '自定id,主要供前端展示权限列表分类排序使用.',
    `menu_code`           varchar(255)     DEFAULT '' COMMENT '归属菜单,前端判断并展示菜单使用,',
    `menu_name`           varchar(255)     DEFAULT '' COMMENT '菜单的中文释义',
    `permission_code`     varchar(255)     DEFAULT '' COMMENT '权限的代码/通配符,对应代码中@RequiresPermissions 的value',
    `permission_name`     varchar(255)     DEFAULT '' COMMENT '本权限的中文释义',
    `required_permission` varchar(1)       DEFAULT FALSE COMMENT '是否本菜单必选权限',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4
  ROW_FORMAT = COMPACT COMMENT ='后台权限表';

#
# Data for table "sys_permission"
#

INSERT INTO `sys_permission` VALUE (101, 'article', '文章管理', 'article:list', '列表', '1');
INSERT INTO `sys_permission` VALUE (102, 'article', '文章管理', 'article:add', '新增', '0');
INSERT INTO `sys_permission` VALUE (103, 'article', '文章管理', 'article:update', '修改', '0');
INSERT INTO `sys_permission` VALUE (601, 'user', '用户', 'user:list', '列表', '1');
INSERT INTO `sys_permission` VALUE (602, 'user', '用户', 'user:add', '新增', '0');
INSERT INTO `sys_permission` VALUE (603, 'user', '用户', 'user:update', '修改', '0');
INSERT INTO `sys_permission` VALUE (701, 'role', '角色权限', 'role:list', '列表', '1');
INSERT INTO `sys_permission` VALUE (702, 'role', '角色权限', 'role:add', '新增', '0');
INSERT INTO `sys_permission` VALUE (703, 'role', '角色权限', 'role:update', '修改', '0');
INSERT INTO `sys_permission` VALUE (704, 'role', '角色权限', 'role:delete', '删除', '0');

#
# Structure for table "sys_role"
#

DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`
(
    `id`            int(11)   NOT NULL AUTO_INCREMENT,
    `role_name`     varchar(20)    DEFAULT NULL COMMENT '角色名',
    `create_time`   timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`   timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `delete_status` varchar(1)     DEFAULT TRUE COMMENT '是否有效',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = UTF8MB4
  ROW_FORMAT = COMPACT COMMENT ='后台角色表';

#
# Data for table "sys_role"
#

INSERT INTO `sys_role` VALUE (1, '管理员', '2000-01-01 00:00:01', '2000-01-01 00:00:02', '1');
INSERT INTO `sys_role` VALUE (2, '不愿透露姓名的路人1', '2000-01-01 00:00:02', '2000-01-01 00:00:03', '1');
INSERT INTO `sys_role` VALUE (3, '不愿透露姓名的路人2', '2000-01-01 00:00:03', '2000-01-01 00:00:04', '1');

#
# Structure for table "sys_role_permission"
#

DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission`
(
    `id`            int(11)   NOT NULL AUTO_INCREMENT,
    `role_id`       int(11)        DEFAULT NULL COMMENT '角色id',
    `permission_id` int(11)        DEFAULT NULL COMMENT '权限id',
    `create_time`   timestamp NULL DEFAULT CURRENT_TIMESTAMP,
    `update_time`   timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `delete_status` varchar(1)     DEFAULT TRUE COMMENT '是否有效',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = UTF8MB4 COMMENT ='角色-权限关联表';

#
# Data for table "sys_role_permission"
#

INSERT INTO `sys_role_permission` VALUE (1, 2, 101, '2000-01-01 00:00:01', '2000-01-01 00:00:02', '1');
INSERT INTO `sys_role_permission` VALUE (2, 2, 102, '2000-01-01 00:00:01', '2000-01-01 00:00:02', '1');
INSERT INTO `sys_role_permission` VALUE (5, 2, 602, '2000-01-01 00:00:01', '2000-01-01 00:00:02', '1');
INSERT INTO `sys_role_permission` VALUE (6, 2, 601, '2000-01-01 00:00:01', '2000-01-01 00:00:02', '1');
INSERT INTO `sys_role_permission` VALUE (7, 2, 603, '2000-01-01 00:00:01', '2000-01-01 00:00:02', '1');
INSERT INTO `sys_role_permission` VALUE (8, 2, 703, '2000-01-01 00:00:01', '2000-01-01 00:00:02', '1');
INSERT INTO `sys_role_permission` VALUE (9, 2, 701, '2000-01-01 00:00:01', '2000-01-01 00:00:02', '1');
INSERT INTO `sys_role_permission` VALUE (10, 2, 702, '2000-01-01 00:00:01', '2000-01-01 00:00:02', '1');
INSERT INTO `sys_role_permission` VALUE (11, 2, 704, '2000-01-01 00:00:01', '2000-01-01 00:00:02', '1');
INSERT INTO `sys_role_permission` VALUE (12, 2, 103, '2000-01-01 00:00:01', '2000-01-01 00:00:02', '1');
INSERT INTO `sys_role_permission` VALUE (13, 3, 601, '2000-01-01 00:00:01', '2000-01-01 00:00:02', '1');
INSERT INTO `sys_role_permission` VALUE (14, 3, 701, '2000-01-01 00:00:01', '2000-01-01 00:00:02', '1');
INSERT INTO `sys_role_permission` VALUE (15, 3, 702, '2000-01-01 00:00:01', '2000-01-01 00:00:02', '1');
INSERT INTO `sys_role_permission` VALUE (16, 3, 704, '2000-01-01 00:00:01', '2000-01-01 00:00:02', '1');
INSERT INTO `sys_role_permission` VALUE (17, 3, 102, '2000-01-01 00:00:01', '2000-01-01 00:00:02', '1');

#
# Structure for table "sys_user"
#

DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `id`            int(11)   NOT NULL AUTO_INCREMENT,
    `username`      varchar(255)   DEFAULT NULL COMMENT '用户名',
    `password`      varchar(255)   DEFAULT NULL COMMENT '密码',
    `nickname`      varchar(255)   DEFAULT NULL COMMENT '昵称',
    `role_id`       int(11)        DEFAULT '0' COMMENT '角色ID',
    `create_time`   timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `delete_status` varchar(1)     DEFAULT TRUE COMMENT '是否有效',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 10008
  DEFAULT CHARSET = UTF8MB4 COMMENT ='运营后台用户表';

#
# Data for table "sys_user"
#

INSERT INTO `sys_user` VALUE (10003, 'admin', 'admin', '超级用户', 1, '2000-01-01 00:00:01', '2000-01-01 00:00:01', '1');
