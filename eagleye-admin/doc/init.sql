/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2014/5/26 18:15:04                           */
/*==============================================================*/


drop table if exists eagleye_app;

drop table if exists eagleye_group;

drop table if exists eagleye_rule;

drop table if exists eagleye_user_group;

/*==============================================================*/
/* Table: eagleye_app                                           */
/*==============================================================*/
create table eagleye_app
(
   id                   integer not null auto_increment,
   app_name             varchar(200),
   group_ids            text,
   description          text,
   create_time          datetime,
   update_time          datetime,
   data_status          varchar(10) comment '0:delete  1: active 2:inactive',
   primary key (id)
);

/*==============================================================*/
/* Table: eagleye_group                                         */
/*==============================================================*/
create table eagleye_group
(
   id                   int not null auto_increment,
   group_name           varchar(200),
   description          text,
   create_time          datetime,
   update_time          datetime,
   data_status          varchar(10) comment '0:delete  1: active 2:inactive',
   primary key (id)
);

/*==============================================================*/
/* Table: eagleye_rule                                          */
/*==============================================================*/
create table eagleye_rule
(
   id                   integer not null auto_increment,
   rule_keyword         varchar(500),
   app_ids              text,
   description          text,
   create_time          datetime,
   update_time          datetime,
   data_status          varchar(10) comment '0:delete  1: active 2:inactive',
   primary key (id)
);

/*==============================================================*/
/* Table: eagleye_user_group                                    */
/*==============================================================*/
create table eagleye_user_group
(
   id                   integer not null auto_increment,
   group_id             integer,
   user_id              varchar(200),
   user_phone           varchar(200),
   user_account         varchar(200),
   warn_type            varchar(20) comment 'n; m; e; me;',
   description          text,
   create_time          datetime,
   update_time          datetime,
   data_status          varchar(10) comment '0:delete  1: active 2:inactive',
   primary key (id)
);


#创建seed表
CREATE TABLE `TB_DATA_SEED` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `VALUE` bigint(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=82 DEFAULT CHARSET=utf8;

#创建app表
CREATE TABLE `TB_PARA_APP` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=154 DEFAULT CHARSET=utf8;

#创建service表
CREATE TABLE `TB_PARA_SERVICE` (
  `ID` varchar(6) NOT NULL,
  `SERVICENAME` varchar(100) DEFAULT NULL,
  `APP_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_appId` (`APP_ID`),
  CONSTRAINT `fk_appId` FOREIGN KEY (`APP_ID`) REFERENCES `TB_PARA_APP` (`ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#创建serviceId生成策略表
CREATE TABLE `TB_PARA_SERVICE_ID_GEN` (
  `MAX_ID` int(11) NOT NULL,
  `HEAD` int(11) NOT NULL,
  `MAX_HEAD` int(11) NOT NULL,
  `ID_SCOPE` int(11) NOT NULL,
  PRIMARY KEY (`MAX_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

#初始化生成策略数据，serviceId由head+max_id两部分组成max_id和head分别自增。
#head自增至26之后重置为0（为了配合hbase分区策略，hbase分多少个区，则max_head为多少）
#max_id自增值9999后后重置为0
INSERT INTO `TB_PARA_SERVICE_ID_GEN` VALUES (0, 0, 26, 10000);

#annotation
CREATE TABLE `annotation` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `k` varchar(128) DEFAULT NULL,
  `value` varchar(2048) DEFAULT NULL,
  `ip` varchar(45) DEFAULT NULL,
  `port` varchar(11) DEFAULT NULL,
  `timestamp` bigint(20) DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `spanId` bigint(128) DEFAULT NULL,
  `traceId` bigint(128) DEFAULT NULL,
  `service` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=217122 DEFAULT CHARSET=utf8;

#span
CREATE TABLE `span` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL,
  `traceId` bigint(20) DEFAULT NULL,
  `parentId` bigint(20) DEFAULT NULL,
  `spanId` bigint(20) DEFAULT NULL,
  `service` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=53365 DEFAULT CHARSET=utf8;

#trace
CREATE TABLE `trace` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `traceId` bigint(128) DEFAULT NULL,
  `duration` int(11) DEFAULT NULL,
  `service` varchar(1024) CHARACTER SET utf8 DEFAULT NULL,
  `time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16924 DEFAULT CHARSET=utf8;

/*==============================================================*/
/* Table: eagleye_user                                          */
/*==============================================================*/
CREATE TABLE `eagleye_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id，自增',
  `user_account` varchar(64) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '用户账号，用邮箱填写',
  `user_phone` varchar(32) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '用户手机',
  `user_name` varchar(32) COLLATE utf8_unicode_ci NOT NULL DEFAULT '' COMMENT '用户姓名',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '修改时间',
  `data_status` varchar(4) COLLATE utf8_unicode_ci NOT NULL DEFAULT '1' COMMENT '0:delete  1: active 2:inactive',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='用户信息表';
