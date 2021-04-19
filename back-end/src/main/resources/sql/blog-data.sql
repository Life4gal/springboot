USE blog;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Records of blog_article
-- ----------------------------
INSERT INTO `blog_article`
VALUES ('1',
        '1',
        '1',
        '1',
        '2000-01-01 00:00:01',
        '2000-01-01 00:00:02',
        '则是测试文章标题',
        '这是测试文章摘要',
        '0',
        '0',
        '0');

-- ----------------------------
-- Records of blog_article_body
-- ----------------------------
INSERT INTO `blog_article_body`
VALUES ('1',
        'Spring框架是 Java 平台的一个开源的全栈（Full-stack）应用程序框架和控制反转容器实现，一般被直接称为 Spring。该框架的一些核心功能理论上可用于任何 Java 应用，但 Spring 还为基于Java企业版平台构建的 Web 应用提供了大量的拓展支持。Spring 没有直接实现任何的编程模型，但它已经在 Java 社区中广为流行，基本上完全代替了企业级JavaBeans（EJB）模型。',
        null);

-- ----------------------------
-- Records of blog_article_tag
-- ----------------------------
INSERT INTO `blog_article_tag`
VALUES ('1', '1');
INSERT INTO `blog_article_tag`
VALUES ('1', '2');
INSERT INTO `blog_article_tag`
VALUES ('1', '3');
INSERT INTO `blog_article_tag`
VALUES ('2', '1');
INSERT INTO `blog_article_tag`
VALUES ('2', '2');
INSERT INTO `blog_article_tag`
VALUES ('2', '3');
INSERT INTO `blog_article_tag`
VALUES ('3', '1');
INSERT INTO `blog_article_tag`
VALUES ('4', '1');


-- ----------------------------
-- Records of blog_category
-- ----------------------------
INSERT INTO `blog_category`
VALUES ('1', '/categories/front.png', '前端',
        '前端开发是创建WEB页面或APP等前端界面呈现给用户的过程，通过HTML，CSS及JavaScript以及衍生出来的各种技术、框架、解决方案，来实现互联网产品的用户界面交互。');
INSERT INTO `blog_category`
VALUES ('2', '/categories/back.png', '后端', '后端');
INSERT INTO `blog_category`
VALUES ('3', '/categories/life.jpg', '生活', '生活');
INSERT INTO `blog_category`
VALUES ('4', '/categories/database.png', '数据库', '数据库');
INSERT INTO `blog_category`
VALUES ('5', '/categories/language.png', '编程语言', '编程语言');



-- ----------------------------
-- Records of blog_comment
-- ----------------------------
INSERT INTO `blog_comment`
VALUES ('1',
        '你辛苦了',
        '2000-01-01 00:01:00',
        '2000-01-01 00:02:00',
        '1',
        '1',
        null,
        '2',
        null);

INSERT INTO `blog_comment`
VALUES ('2',
        '啦啦啦',
        '2000-01-01 00:01:00',
        '2000-01-01 00:02:00',
        '1',
        '1',
        null,
        '3',
        null);



-- ----------------------------
-- Records of blog_tag
-- ----------------------------
INSERT INTO `blog_tag`
VALUES ('1', '/tags/java.png', 'Java');
INSERT INTO `blog_tag`
VALUES ('2', '/tags/spring.png', 'Spring');
INSERT INTO `blog_tag`
VALUES ('3', '/tags/hibernate.png', 'Hibernate');
INSERT INTO `blog_tag`
VALUES ('4', '/tags/maven.png', 'Maven');
INSERT INTO `blog_tag`
VALUES ('5', '/tags/html.png', 'Html');
INSERT INTO `blog_tag`
VALUES ('6', '/tags/js.png', 'JavaScript');
INSERT INTO `blog_tag`
VALUES ('7', '/tags/vue.png', 'Vue');
INSERT INTO `blog_tag`
VALUES ('8', '/tags/css.png', 'Css');



-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user`
VALUES ('1',
        'admin',
        'super_user',
        'admin@admin',
        '179324865',
        '/users/admin.png',
        'admin@admin.com',
        '123456789',
        'very good!',
        '2000-01-01 00:00:00',
        null,
        1,
        0);

INSERT INTO `sys_user`
VALUES ('2',
        'test',
        'test_user',
        'test@test',
        '179324865',
        '/users/test.png',
        'test@test.com',
        '987654321',
        'very good!',
        '2000-01-01 00:00:01',
        null,
        0,
        0);
