SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'TRADITIONAL,ALLOW_INVALID_DATES';

DROP SCHEMA IF EXISTS `eft`;

CREATE SCHEMA IF NOT EXISTS `eft` DEFAULT CHARACTER SET utf8;
USE `eft`;

DROP TABLE IF EXISTS `eft`.`user`;

CREATE TABLE IF NOT EXISTS `eft`.`user`
(
    `id`              INT          NOT NULL AUTO_INCREMENT COMMENT '用户编号',
    `username`        VARCHAR(16)  NOT NULL DEFAULT '' COMMENT '用户名',
    `real_name`       VARCHAR(45)  NULL     DEFAULT '' COMMENT '真实姓名',
    `email`           VARCHAR(255) NOT NULL DEFAULT '' COMMENT '邮箱地址',
    `password`        VARCHAR(128) NOT NULL DEFAULT '' COMMENT '登录密码',
    `permission`      INT          NOT NULL DEFAULT 1 COMMENT '0（禁止登录），1（正常，普通用户），2（正常，管理员），3（正常，超级管理员）',
    `create_time`     DATETIME     NOT NULL DEFAULT current_timestamp COMMENT '注册时间',
    `last_login_time` DATETIME     NOT NULL DEFAULT current_timestamp COMMENT '最后一次登录时间',
    `is_downloadable` INT          NOT NULL DEFAULT 1 COMMENT '（全局权限）用户是否可以下载，0不可以，1可以',
    `is_uploadable`   INT          NOT NULL DEFAULT 1 COMMENT '（全局权限）用户是否可以上传，0不可以，1可以',
    `is_visible`      INT          NOT NULL DEFAULT 1 COMMENT '（全局权限）用户是否可以查看文件，0不可以，1可以',
    `is_deletable`    INT          NOT NULL DEFAULT 0 COMMENT '（全局权限）用户可以删除文件，0不可以，1可以',
    `is_updatable`    INT          NOT NULL DEFAULT 0 COMMENT '（全局权限）用户是否可以更新文件，0不可以，1可以',
    `avatar`          VARCHAR(255) NULL     DEFAULT '' COMMENT '头像',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `id_UNIQUE` (`id` ASC),
    UNIQUE INDEX `username_UNIQUE` (`username` ASC),
    UNIQUE INDEX `create_time_UNIQUE` (`create_time` ASC),
    UNIQUE INDEX `email_UNIQUE` (`email` ASC)
)
    ENGINE = InnoDB
    COMMENT = '用户表';


DROP TABLE IF EXISTS `eft`.`category`;

CREATE TABLE IF NOT EXISTS `eft`.`category`
(
    `id`          INT         NOT NULL AUTO_INCREMENT,
    `name`        VARCHAR(45) NOT NULL DEFAULT '',
    `create_time` DATETIME    NOT NULL DEFAULT current_timestamp,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `name_UNIQUE` (`name` ASC),
    UNIQUE INDEX `cat_id_UNIQUE` (`id` ASC)
)
    ENGINE = InnoDB
    COMMENT = '文件分类';


DROP TABLE IF EXISTS `eft`.`file`;

CREATE TABLE IF NOT EXISTS `eft`.`file`
(
    `id`               BIGINT        NOT NULL AUTO_INCREMENT COMMENT '编号',
    `name`             VARCHAR(256)  NULL     DEFAULT '' COMMENT '文件名',
    `suffix`           VARCHAR(16)   NOT NULL DEFAULT '' COMMENT '文件后缀',
    `local_url`        VARCHAR(1024) NOT NULL DEFAULT '' COMMENT '本地路径',
    `visit_url`        VARCHAR(1024) NOT NULL DEFAULT '' COMMENT '客户端访问路径',
    `size`             BIGINT        NOT NULL DEFAULT 0 COMMENT '文件大小，单位bit',
    `create_time`      DATETIME      NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `description`      VARCHAR(1024) NULL     DEFAULT '' COMMENT '文件描述',
    `check_times`      INT           NOT NULL DEFAULT 0 COMMENT '查看次数',
    `download_times`   INT           NOT NULL DEFAULT 0 COMMENT '下载次数',
    `tag`              VARCHAR(45)   NULL     DEFAULT '' COMMENT '文件标签',
    `user_id`          INT           NOT NULL,
    `category_id`      INT           NOT NULL,
    `is_downloadable`  INT           NOT NULL DEFAULT 1 COMMENT '（全局权限）文件是否可以下载，0不可以，1可以',
    `is_uploadable`    INT           NOT NULL DEFAULT 1 COMMENT '（全局权限）文件夹是否允许上传（需要判断文件是否是文件夹），0不可以，1可以',
    `is_visible`       INT           NOT NULL DEFAULT 1 COMMENT '（全局权限）文件是否可见，0不可以，1可以',
    `is_deletable`     INT           NOT NULL DEFAULT 1 COMMENT '（全局权限）文件是否可以删除，0不可以，1可以',
    `is_updatable`     INT           NOT NULL DEFAULT 1 COMMENT '（全局权限）文件是否可以更新，0不可以，1可以',
    `last_modify_time` DATETIME      NOT NULL DEFAULT current_timestamp COMMENT '最近一次修改时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `file_id_UNIQUE` (`id` ASC),
    INDEX `fk_file_user_idx` (`user_id` ASC),
    INDEX `fk_file_category1_idx` (`category_id` ASC),
    UNIQUE INDEX `local_url_UNIQUE` (`local_url` ASC),
    UNIQUE INDEX `visit_url_UNIQUE` (`visit_url` ASC),
    CONSTRAINT `fk_file_user`
        FOREIGN KEY (`user_id`)
            REFERENCES `eft`.`user` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_file_category1`
        FOREIGN KEY (`category_id`)
            REFERENCES `eft`.`category` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB
    COMMENT = '文件列表';


DROP TABLE IF EXISTS `eft`.`download`;

CREATE TABLE IF NOT EXISTS `eft`.`download`
(
    `id`          BIGINT   NOT NULL AUTO_INCREMENT COMMENT '编号',
    `create_time` DATETIME NOT NULL DEFAULT current_timestamp COMMENT '下载时间',
    `user_id`     INT      NOT NULL,
    `file_id`     BIGINT   NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE INDEX `id_UNIQUE` (`id` ASC),
    INDEX `fk_download_user1_idx` (`user_id` ASC),
    INDEX `fk_download_file1_idx` (`file_id` ASC),
    CONSTRAINT `fk_download_user1`
        FOREIGN KEY (`user_id`)
            REFERENCES `eft`.`user` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_download_file1`
        FOREIGN KEY (`file_id`)
            REFERENCES `eft`.`file` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB
    COMMENT = '下载历史表';


DROP TABLE IF EXISTS `eft`.`auth`;

CREATE TABLE IF NOT EXISTS `eft`.`auth`
(
    `id`              BIGINT   NOT NULL AUTO_INCREMENT,
    `is_uploadable`   INT      NOT NULL DEFAULT 1 COMMENT '是否可以上传（需要判断对应的文件是否是文件夹），0不可以，1可以',
    `is_deletable`    INT      NOT NULL DEFAULT 1 COMMENT '是否可以删除，0不可以，1可以',
    `is_updatable`    INT      NOT NULL DEFAULT 1 COMMENT '是否可以更新，0不可以，1可以',
    `user_id`         INT      NOT NULL,
    `file_id`         BIGINT   NOT NULL,
    `is_visible`      INT      NOT NULL DEFAULT 1 COMMENT '是否可以查看，0不可以，1可以',
    `is_downloadable` INT      NOT NULL DEFAULT 1 COMMENT '用户是否可以下载，0不可以，1可以',
    `create_time`     DATETIME NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE INDEX `id_UNIQUE` (`id` ASC),
    INDEX `fk_auth_user1_idx` (`user_id` ASC),
    INDEX `fk_auth_file1_idx` (`file_id` ASC),
    CONSTRAINT `fk_auth_user1`
        FOREIGN KEY (`user_id`)
            REFERENCES `eft`.`user` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION,
    CONSTRAINT `fk_auth_file1`
        FOREIGN KEY (`file_id`)
            REFERENCES `eft`.`file` (`id`)
            ON DELETE NO ACTION
            ON UPDATE NO ACTION
)
    ENGINE = InnoDB
    COMMENT = '用户对应指定文件的权限表，覆盖用户表的权限';


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;

INSERT INTO `eft`.`user`(username, real_name, email, password, permission, is_deletable, is_updatable)
VALUES ("system", "系统", "system@local.host", sha2("123456", 256), 3, 1, 1);

#请确保数据库中始终有“未分类”这个分类，否则系统运行时有可能出错
INSERT INTO `eft`.`category`(name)
VALUES ("未分类");
