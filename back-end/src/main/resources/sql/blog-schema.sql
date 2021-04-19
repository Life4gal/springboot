DROP DATABASE IF EXISTS blog;
CREATE DATABASE blog;
USE blog;
SET NAMES UTF8MB4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
--  Table structure for `blog_article`
-- ----------------------------
DROP TABLE IF EXISTS `blog_article`;
CREATE TABLE `blog_article`
(
    `id`             INT          NOT NULL AUTO_INCREMENT COMMENT '文章id',
    `author_id`      BIGINT       NOT NULL COMMENT '作者id',
    `body_id`        BIGINT       NOT NULL COMMENT '正文id',
    `category_id`    INT          NOT NULL COMMENT '分类id',

    `create_date`    DATETIME     NOT NULL DEFAULT NOW() COMMENT '创建时间',
    `update_date`    DATETIME              DEFAULT NULL COMMENT '上一次修改时间',

    `title`          VARCHAR(64)  NOT NULL COMMENT '文章标题',
    `summary`        VARCHAR(100) NOT NULL COMMENT '文章摘要',
    `weight`         INT          NOT NULL DEFAULT 0 COMMENT '权重',
    `view_counts`    INT          NOT NULL DEFAULT 0 COMMENT '阅读量',
    `comment_counts` INT          NOT NULL DEFAULT 0 COMMENT '评论数量',

    PRIMARY KEY (`id`),

    KEY `FKndx2m69302cso79y66yxiju4h` (`author_id`),
    KEY `FKrd11pjsmueckfrh9gs7bc6374` (`body_id`),
    KEY `FKjrn3ua4xmiulp8raj7m9d2xk6` (`category_id`),

    CONSTRAINT `FKndx2m69302cso79y66yxiju4h` FOREIGN KEY (`author_id`) REFERENCES `sys_user` (`id`),
    CONSTRAINT `FKrd11pjsmueckfrh9gs7bc6374` FOREIGN KEY (`body_id`) REFERENCES `blog_article_body` (`id`),
    CONSTRAINT `FKjrn3ua4xmiulp8raj7m9d2xk6` FOREIGN KEY (`category_id`) REFERENCES `blog_category` (`id`)

) ENGINE = InnoDB
  AUTO_INCREMENT = 25
  DEFAULT CHARSET = UTF8MB4;

-- ----------------------------
--  Table structure for `blog_article_body`
-- ----------------------------
DROP TABLE IF EXISTS `blog_article_body`;
CREATE TABLE `blog_article_body`
(
    `id`           BIGINT   NOT NULL AUTO_INCREMENT COMMENT '正文id',
    `content`      LONGTEXT NOT NULL COMMENT '正文',
    `content_html` LONGTEXT DEFAULT NULL COMMENT '正文链接',

    PRIMARY KEY (`id`)

) ENGINE = InnoDB
  AUTO_INCREMENT = 38
  DEFAULT CHARSET = UTF8MB4;

-- ----------------------------
--  Table structure for `blog_article_tag`
-- ----------------------------
DROP TABLE IF EXISTS `blog_article_tag`;
CREATE TABLE `blog_article_tag`
(
    `article_id` INT NOT NULL COMMENT '文章id',
    `tag_id`     INT NOT NULL COMMENT '标签id',

    KEY `FKsmysra6pt3ehcvts18q2h4409` (`article_id`),
    KEY `FK2s65pu9coxh7w16s8jycih79w` (`tag_id`),

    CONSTRAINT `FKsmysra6pt3ehcvts18q2h4409` FOREIGN KEY (`article_id`) REFERENCES `blog_article` (`id`),
    CONSTRAINT `FK2s65pu9coxh7w16s8jycih79w` FOREIGN KEY (`tag_id`) REFERENCES `blog_tag` (`id`)

) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8MB4;

-- ----------------------------
--  Table structure for `blog_category`
-- ----------------------------
DROP TABLE IF EXISTS `blog_category`;
CREATE TABLE `blog_category`
(
    `id`            INT          NOT NULL AUTO_INCREMENT COMMENT '分类id',
    `avatar`        VARCHAR(255) DEFAULT NULL COMMENT '分类头像',
    `category_name` VARCHAR(255) NOT NULL COMMENT '分类名称',
    `description`   VARCHAR(255) NOT NULL COMMENT '分类描述',

    PRIMARY KEY (`id`)

) ENGINE = InnoDB
  AUTO_INCREMENT = 6
  DEFAULT CHARSET = UTF8MB4;

-- ----------------------------
--  Table structure for `blog_comment`
-- ----------------------------
DROP TABLE IF EXISTS `blog_comment`;
CREATE TABLE `blog_comment`
(
    `id`          INT          NOT NULL AUTO_INCREMENT COMMENT '评论id',
    `content`     VARCHAR(255) NOT NULL COMMENT '评论内容',
    `create_date` TIMESTAMP    NOT NULL DEFAULT NOW() COMMENT '创建时间',
    `update_date` TIMESTAMP             DEFAULT NULL COMMENT '上一次修改时间',

    `article_id`  INT          NOT NULL COMMENT '文章id',
    `author_id`   BIGINT       NOT NULL COMMENT '作者id',
    `parent_id`   INT                   DEFAULT NULL COMMENT '父级id',
    `uid`         BIGINT       NOT NULL COMMENT '用户id',

    `level`       VARCHAR(1)            DEFAULT NULL COMMENT '等级',

    PRIMARY KEY (`id`),

    KEY `FKecq0fuo9k0lnmea6r01vfhiok` (`article_id`),
    KEY `FKkvuyh6ih7dt1rfqhwsjomsa6i` (`author_id`),
    KEY `FKaecafrcorkhyyp1luffinsfqs` (`parent_id`),
    KEY `FK73dgr23lbs3ebex5qvqyku308` (`uid`),

    CONSTRAINT `FKecq0fuo9k0lnmea6r01vfhiok` FOREIGN KEY (`article_id`) REFERENCES `blog_article` (`id`),
    CONSTRAINT `FKkvuyh6ih7dt1rfqhwsjomsa6i` FOREIGN KEY (`author_id`) REFERENCES `sys_user` (`id`),
    CONSTRAINT `FKaecafrcorkhyyp1luffinsfqs` FOREIGN KEY (`parent_id`) REFERENCES `blog_comment` (`id`),
    CONSTRAINT `FK73dgr23lbs3ebex5qvqyku308` FOREIGN KEY (`uid`) REFERENCES `sys_user` (`id`)

) ENGINE = InnoDB
  AUTO_INCREMENT = 53
  DEFAULT CHARSET = UTF8MB4;

-- ----------------------------
--  Table structure for `blog_tag`
-- ----------------------------
DROP TABLE IF EXISTS `blog_tag`;
CREATE TABLE `blog_tag`
(
    `id`       INT          NOT NULL AUTO_INCREMENT COMMENT '标签id',
    `avatar`   VARCHAR(255) DEFAULT NULL COMMENT '标签头像',
    `tag_name` VARCHAR(255) NOT NULL COMMENT '标签名称',

    PRIMARY KEY (`id`)

) ENGINE = InnoDB
  AUTO_INCREMENT = 9
  DEFAULT CHARSET = UTF8MB4;

-- ----------------------------
--  Table structure for `sys_log`
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`
(
    `id`          INT NOT NULL AUTO_INCREMENT COMMENT '日志id',
    `create_date` TIMESTAMP                        DEFAULT NOW() COMMENT '创建时间',
    `ip`          VARCHAR(15) COLLATE UTF8MB4_bin  DEFAULT '未知' COMMENT 'IP',
    `method`      VARCHAR(100) COLLATE UTF8MB4_bin DEFAULT '未知' COMMENT '请求方法',
    `module`      VARCHAR(10) COLLATE UTF8MB4_bin  DEFAULT '未知' COMMENT '请求模块',
    `nickname`    VARCHAR(10) COLLATE UTF8MB4_bin  DEFAULT '未知' COMMENT '昵称',
    `operation`   VARCHAR(25) COLLATE UTF8MB4_bin  DEFAULT '未知' COMMENT '行为',
    `params`      VARCHAR(255) COLLATE UTF8MB4_bin DEFAULT '未知' COMMENT '参数',
    `time`        BIGINT                           DEFAULT 0 COMMENT '时间',
    `userid`      BIGINT                           DEFAULT 0 COMMENT '用户id',

    PRIMARY KEY (`id`)

) ENGINE = InnoDB
  AUTO_INCREMENT = 2994
  DEFAULT CHARSET = UTF8MB4
  COLLATE = UTF8MB4_bin;

-- ----------------------------
--  Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`
(
    `id`                  BIGINT       NOT NULL AUTO_INCREMENT COMMENT '用户id',
    `account`             VARCHAR(64)  NOT NULL COMMENT '帐号',
    `nickname`            VARCHAR(255) NOT NULL COMMENT '昵称',
    `password`            VARCHAR(64)  NOT NULL COMMENT '密码',
    `salt`                VARCHAR(255) NOT NULL COMMENT '密码盐',
    `avatar`              VARCHAR(255) DEFAULT NULL COMMENT '头像',
    `email`               VARCHAR(128) DEFAULT NULL COMMENT '邮箱地址',
    `mobile_phone_number` VARCHAR(20)  DEFAULT NULL COMMENT '手机号码',
    `status`              VARCHAR(255) DEFAULT NULL COMMENT '当前状态',

    `create_date`         TIMESTAMP    DEFAULT NOW() COMMENT '创建日期',
    `last_login`          TIMESTAMP    DEFAULT NULL COMMENT '上一次登陆',
    `admin`               BIT(1)       DEFAULT 0 COMMENT '是否为管理员',
    `deleted`             BIT(1)       DEFAULT 0 COMMENT '已注销',

    PRIMARY KEY (`id`),

    UNIQUE KEY `UK_awpog86ljqwb89aqa1c5gvdrd` (`account`),
    UNIQUE KEY `UK_ahtq5ew3v0kt1n7hf1sgp7p8l` (`email`)

) ENGINE = InnoDB
  AUTO_INCREMENT = 16
  DEFAULT CHARSET = UTF8MB4;

SET FOREIGN_KEY_CHECKS = 1;
