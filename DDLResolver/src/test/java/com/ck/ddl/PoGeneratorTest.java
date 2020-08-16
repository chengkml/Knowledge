package com.ck.ddl;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class PoGeneratorTest {

    private static final String ddl = "create table dacp_meta_member_tab\n" +
            "(\n" +
            "   id                   varchar(64) not null comment '记录ID',\n" +
            "   team_name            varchar(64) comment '团队编码',\n" +
            "   tab_id               varchar(64) not null comment '模型ID',\n" +
            "   member_name          varchar(128) comment '责任人编码',\n" +
            "   arch_name            varchar(64) not null comment '架构结点ID',\n" +
            "   arch_full_path       varchar(512) comment '架构结点全路径',\n" +
            "   primary key (id)\n" +
            ")\n" +
            "\n" +
            "\n" +
            "create table dacp_meta_tab\n" +
            "(\n" +
            "   tab_id               varchar(32) not null comment '模型ID',\n" +
            "   tab_name             varchar(64) not null comment '模型英文名',\n" +
            "   ds_name              varchar(32) not null comment '数据源编码',\n" +
            "   tab_schema           varchar(64) comment '生产库模式名',\n" +
            "   tab_schema_dev       varchar(64) comment '开发库模式名',\n" +
            "   tab_label            varchar(512) comment '模型中文名',\n" +
            "   tab_descr            varchar(1024) comment '模型描述',\n" +
            "   logic_tab_name       varchar(64) comment '逻辑模型名',\n" +
            "   phy_tab_name         varchar(64) comment '物理表名',\n" +
            "   tab_sens_lvl         varchar(64) comment '敏感级别',\n" +
            "   mask_type            varchar(16) comment '脱敏类型',\n" +
            "   lvl                  varchar(32) comment '层次编码',\n" +
            "   topic                varchar(32) comment '主题编码',\n" +
            "   cycle_type           varchar(8) comment '周期类型',\n" +
            "   tab_space            varchar(16) comment '表空间',\n" +
            "   tab_sn               varchar(16) comment '模型编码',\n" +
            "   version              varchar(16) comment '模型版本号',\n" +
            "   team_name            varchar(64) comment '团队编码',\n" +
            "   member_name          varchar(128) comment '责任人',\n" +
            "   open_state           varchar(8) comment '开放状态',\n" +
            "   asset_sn             varchar(32) comment '资产编码',\n" +
            "   extend_items         varchar(1024) comment '扩展属性',\n" +
            "   busi_caliber         varchar(512) comment '业务口径',\n" +
            "   state                varchar(16) not null comment '状态',\n" +
            "   state_date           datetime comment '状态时间',\n" +
            "   is_share             varchar(16) comment '是否分享模型',\n" +
            "   is_tran              varchar(16) comment '是否移植模型',\n" +
            "   model_type           varchar(16) comment '模型类型',\n" +
            "   share_type           varchar(16) comment '模型分享类型',\n" +
            "   recommend_cnt        int comment '推荐数量',\n" +
            "   storage_time         varchar(128) comment '保留周期',\n" +
            "   is_public            varchar(1) comment '是否公用表',\n" +
            "   ownership            varchar(32) comment '模型归属',\n" +
            "   create_date          datetime comment '创建时间',\n" +
            "   lastupd              datetime comment '最后更新时间',\n" +
            "   create_user          varchar(16) comment '创建人',\n" +
            "   lastupd_user         varchar(16) comment '最后更新人',\n" +
            "   primary key (tab_id)\n" +
            ")\n" +
            "collate = utf8_bin;\n" +
            "\n" +
            "create table dacp_meta_tab_alter\n" +
            "(\n" +
            "   tab_id               varchar(32) not null comment '模型ID',\n" +
            "   tab_name             varchar(64) not null comment '模型英文名',\n" +
            "   ds_name              varchar(32) not null comment '数据源编码',\n" +
            "   tab_schema           varchar(64) comment '生产库模式名',\n" +
            "   tab_schema_dev       varchar(64) comment '开发库模式名',\n" +
            "   tab_label            varchar(512) comment '模型中文名',\n" +
            "   tab_descr            varchar(1024) comment '模型描述',\n" +
            "   logic_tab_name       varchar(64) comment '逻辑模型名',\n" +
            "   phy_tab_name         varchar(64) comment '物理表名',\n" +
            "   tab_sens_lvl         varchar(64) comment '敏感级别',\n" +
            "   mask_type            varchar(16) comment '脱敏类型',\n" +
            "   lvl                  varchar(32) comment '层次编码',\n" +
            "   topic                varchar(32) comment '主题编码',\n" +
            "   cycle_type           varchar(8) comment '周期类型',\n" +
            "   tab_space            varchar(16) comment '表空间',\n" +
            "   tab_sn               varchar(16) comment '模型编码',\n" +
            "   version              varchar(16) comment '模型版本号',\n" +
            "   team_name            varchar(64) comment '团队编码',\n" +
            "   member_name          varchar(128) comment '责任人',\n" +
            "   open_state           varchar(8) comment '开放状态',\n" +
            "   asset_sn             varchar(32) comment '资产编码',\n" +
            "   extend_items         varchar(1024) comment '扩展属性',\n" +
            "   busi_caliber         varchar(512) comment '业务口径',\n" +
            "   state                varchar(16) not null comment '状态',\n" +
            "   state_date           datetime comment '状态时间',\n" +
            "   is_share             varchar(16) comment '是否分享模型',\n" +
            "   is_tran              varchar(16) comment '是否移植模型',\n" +
            "   model_type           varchar(16) comment '模型类型',\n" +
            "   share_type           varchar(16) comment '模型分享类型',\n" +
            "   recommend_cnt        int comment '推荐数量',\n" +
            "   storage_time         varchar(128) comment '保留周期',\n" +
            "   is_public            varchar(1) comment '是否公用表',\n" +
            "   ownership            varchar(32) comment '模型归属',\n" +
            "   create_date          datetime comment '创建时间',\n" +
            "   lastupd              datetime comment '最后更新时间',\n" +
            "   create_user          varchar(16) comment '创建人',\n" +
            "   lastupd_user         varchar(16) comment '最后更新人',\n" +
            "   primary key (tab_id)\n" +
            ")\n" +
            "collate = utf8_bin;\n" +
            "\n" +
            "create table dacp_meta_tab_constraint\n" +
            "(\n" +
            "   tab_constraint_id    varchar(32) not null comment '约束记录ID',\n" +
            "   tab_constraint_target varchar(32) not null comment '约束对象',\n" +
            "   tab_constraint_type  varchar(32) not null comment '约束类型',\n" +
            "   tab_constraint_name  varchar(64) not null comment '约束名',\n" +
            "   tab_id               varchar(32) not null comment '模型ID',\n" +
            "   primary key (tab_constraint_id)\n" +
            ");\n" +
            "\n" +
            "\n" +
            "create table dacp_meta_tab_constraint_alter\n" +
            "(\n" +
            "   tab_constraint_id    varchar(32) not null comment '约束记录ID',\n" +
            "   tab_constraint_target varchar(32) not null comment '约束对象',\n" +
            "   tab_constraint_type  varchar(32) not null comment '约束类型',\n" +
            "   tab_constraint_name  varchar(64) not null comment '约束名',\n" +
            "   tab_id               varchar(32) not null comment '模型ID',\n" +
            "   primary key (tab_constraint_id)\n" +
            ");\n" +
            "\n" +
            "create table dacp_meta_tab_constraint_detail\n" +
            "(\n" +
            "   constraint_detail_id varchar(32) not null comment '约束详情记录ID',\n" +
            "   tab_constraint_id    varchar(32) not null comment '约束记录ID',\n" +
            "   tab_id               varchar(32) not null comment '模型ID',\n" +
            "   field_id             varchar(32) not null comment '字段ID',\n" +
            "   field_sort           int comment '字段序号',\n" +
            "   primary key (constraint_detail_id)\n" +
            ");\n" +
            "\n" +
            "create table dacp_meta_tab_constraint_detail_alter\n" +
            "(\n" +
            "   constraint_detail_id varchar(32) not null comment '约束详情记录ID',\n" +
            "   tab_constraint_id    varchar(32) not null comment '约束记录ID',\n" +
            "   tab_id               varchar(32) not null comment '模型ID',\n" +
            "   field_id             varchar(32) not null comment '字段ID',\n" +
            "   field_sort           int comment '字段序号',\n" +
            "   primary key (constraint_detail_id)\n" +
            ");\n" +
            "\n" +
            "\n" +
            "create table dacp_meta_tab_constraint_detail_his\n" +
            "(\n" +
            "   his_id               varchar(32) not null comment '历史记录ID',\n" +
            "   tab_constraint_his_id varchar(32) comment '约束历史记录ID',\n" +
            "   constraint_detail_id varchar(32) not null comment '约束详情记录ID',\n" +
            "   tab_constraint_id    varchar(32) not null comment '约束记录ID',\n" +
            "   tab_id               varchar(32) not null comment '模型ID',\n" +
            "   field_id             varchar(32) not null comment '字段ID',\n" +
            "   field_sort           int comment '字段序号',\n" +
            "   primary key (his_id)\n" +
            ");\n" +
            "\n" +
            "\n" +
            "create table dacp_meta_tab_constraint_his\n" +
            "(\n" +
            "   his_id               varchar(32) not null comment '历史记录ID',\n" +
            "   table_his_id         varchar(32) comment '模型历史记录ID',\n" +
            "   tab_constraint_id    varchar(32) not null comment '约束记录ID',\n" +
            "   tab_constraint_target varchar(32) not null comment '约束对象',\n" +
            "   tab_constraint_type  varchar(32) not null comment '约束类型',\n" +
            "   tab_constraint_name  varchar(64) not null comment '约束名',\n" +
            "   tab_id               varchar(32) not null comment '模型ID',\n" +
            "   primary key (his_id)\n" +
            ");\n" +
            "\n" +
            "create table dacp_meta_tab_field\n" +
            "(\n" +
            "   field_id             varchar(32) not null comment '字段ID',\n" +
            "   tab_id               varchar(32) not null comment '模型ID',\n" +
            "   field_name           varchar(64) not null comment '字段英文名',\n" +
            "   tab_name             varchar(64) not null comment '模型英文名',\n" +
            "   tab_sn               varchar(64) comment '模型编码',\n" +
            "   field_label          varchar(256) comment '字段中文名',\n" +
            "   field_descr          varchar(512) comment '字段描述',\n" +
            "   data_type            varchar(30) not null comment '字段类型',\n" +
            "   length               int comment '字段长度',\n" +
            "   precision_val        int comment '字段精度',\n" +
            "   scale                int comment '字段刻度',\n" +
            "   isnullable           varchar(1) comment '是否可空',\n" +
            "   field_posi           int comment '字段顺序',\n" +
            "   std_name             varchar(32) comment '标准名',\n" +
            "   def_value            varchar(64) comment '默认值',\n" +
            "   primary_key          varchar(1) comment '是否主键',\n" +
            "   partition_key        varchar(1) comment '是否分区键',\n" +
            "   distributed_key      varchar(1) comment '是否分布键',\n" +
            "   distribute_num       int comment '分布数',\n" +
            "   distribute_sort_key  varchar(1) comment '是否分布排序键',\n" +
            "   field_sens_lvl       varchar(16) comment '字段敏感级别',\n" +
            "   mask_type            varchar(16) comment '字段脱敏类型',\n" +
            "   mask_rule            varchar(16) comment '字段脱敏规则',\n" +
            "   sampling             varchar(128) comment '抽样数据',\n" +
            "   segment_seq          varchar(1) comment '分段键',\n" +
            "   asset_sn             varchar(16) comment '字段资产编码',\n" +
            "   busi_caliber         varchar(512) comment '业务口径',\n" +
            "   summary              text comment 'summary',\n" +
            "   extend_items         varchar(512) comment '扩展属性',\n" +
            "   is_datatime_field    varchar(1) comment '是否时间标识',\n" +
            "   primary key (field_id)\n" +
            ")\n" +
            "collate = utf8_bin;\n" +
            "\n" +
            "\n" +
            "create table dacp_meta_tab_field_alter\n" +
            "(\n" +
            "   field_id             varchar(32) not null comment '字段ID',\n" +
            "   tab_id               varchar(32) not null comment '模型ID',\n" +
            "   field_name           varchar(64) not null comment '字段英文名',\n" +
            "   tab_name             varchar(64) not null comment '模型英文名',\n" +
            "   tab_sn               varchar(64) comment '模型编码',\n" +
            "   field_label          varchar(256) comment '字段中文名',\n" +
            "   field_descr          varchar(512) comment '字段描述',\n" +
            "   data_type            varchar(30) not null comment '字段类型',\n" +
            "   length               int comment '字段长度',\n" +
            "   precision_val        int comment '字段精度',\n" +
            "   scale                int comment '字段刻度',\n" +
            "   isnullable           varchar(1) comment '是否可空',\n" +
            "   field_posi           int comment '字段顺序',\n" +
            "   std_name             varchar(32) comment '标准名',\n" +
            "   def_value            varchar(64) comment '默认值',\n" +
            "   primary_key          varchar(1) comment '是否主键',\n" +
            "   partition_key        varchar(1) comment '是否分区键',\n" +
            "   distributed_key      varchar(1) comment '是否分布键',\n" +
            "   distribute_num       int comment '分布数',\n" +
            "   distribute_sort_key  varchar(1) comment '是否分布排序键',\n" +
            "   field_sens_lvl       varchar(16) comment '字段敏感级别',\n" +
            "   mask_type            varchar(16) comment '字段脱敏类型',\n" +
            "   mask_rule            varchar(16) comment '字段脱敏规则',\n" +
            "   sampling             varchar(128) comment '抽样数据',\n" +
            "   segment_seq          varchar(1) comment '分段键',\n" +
            "   asset_sn             varchar(16) comment '字段资产编码',\n" +
            "   busi_caliber         varchar(512) comment '业务口径',\n" +
            "   summary              text comment 'summary',\n" +
            "   extend_items         varchar(512) comment '扩展属性',\n" +
            "   is_datatime_field    varchar(1) comment '是否时间标识',\n" +
            "   primary key (field_id)\n" +
            ")\n" +
            "collate = utf8_bin;\n" +
            "\n" +
            "\n" +
            "create table dacp_meta_tab_field_his\n" +
            "(\n" +
            "   his_id               varchar(32) not null comment '历史记录ID',\n" +
            "   table_his_id         varchar(32) comment '模型历史记录ID',\n" +
            "   field_id             varchar(32) not null comment '字段ID',\n" +
            "   tab_id               varchar(32) not null comment '模型ID',\n" +
            "   field_name           varchar(64) not null comment '字段英文名',\n" +
            "   tab_name             varchar(64) not null comment '模型英文名',\n" +
            "   tab_sn               varchar(64) comment '模型编码',\n" +
            "   field_label          varchar(256) comment '字段中文名',\n" +
            "   field_descr          varchar(512) comment '字段描述',\n" +
            "   data_type            varchar(30) not null comment '字段类型',\n" +
            "   length               int comment '字段长度',\n" +
            "   precision_val        int comment '字段精度',\n" +
            "   scale                int comment '字段刻度',\n" +
            "   isnullable           varchar(1) comment '是否可空',\n" +
            "   field_posi           int comment '字段顺序',\n" +
            "   std_name             varchar(32) comment '标准名',\n" +
            "   def_value            varchar(64) comment '默认值',\n" +
            "   primary_key          varchar(1) comment '是否主键',\n" +
            "   partition_key        varchar(1) comment '是否分区键',\n" +
            "   distributed_key      varchar(1) comment '是否分布键',\n" +
            "   distribute_num       int comment '分布数',\n" +
            "   distribute_sort_key  varchar(1) comment '是否分布排序键',\n" +
            "   field_sens_lvl       varchar(16) comment '字段敏感级别',\n" +
            "   mask_type            varchar(16) comment '字段脱敏类型',\n" +
            "   mask_rule            varchar(16) comment '字段脱敏规则',\n" +
            "   sampling             varchar(128) comment '抽样数据',\n" +
            "   segment_seq          varchar(1) comment '分段键',\n" +
            "   asset_sn             varchar(16) comment '字段资产编码',\n" +
            "   busi_caliber         varchar(512) comment '业务口径',\n" +
            "   summary              text comment 'summary',\n" +
            "   extend_items         varchar(512) comment '扩展属性',\n" +
            "   is_datatime_field    varchar(1) comment '是否时间标识',\n" +
            "   primary key (his_id)\n" +
            ")\n" +
            "collate = utf8_bin;\n" +
            "\n" +
            "create table dacp_meta_tab_field_src\n" +
            "(\n" +
            "   field_src_id         varchar(32) not null comment '字段来源记录ID',\n" +
            "   src_id               varchar(32) comment '来源记录ID',\n" +
            "   tab_id               varchar(32) not null comment '模型ID',\n" +
            "   field_id             varchar(32) not null comment '字段ID',\n" +
            "   pseudo_code          varchar(512) comment '伪代码',\n" +
            "   real_code            varchar(512) comment '技术代码',\n" +
            "   extend_items         varchar(512) comment '扩展属性',\n" +
            "   primary key (field_src_id)\n" +
            ");\n" +
            "\n" +
            "\n" +
            "create table dacp_meta_tab_field_src_alter\n" +
            "(\n" +
            "   field_src_id         varchar(32) not null comment '字段来源记录ID',\n" +
            "   src_id               varchar(32) comment '来源记录ID',\n" +
            "   tab_id               varchar(32) not null comment '模型ID',\n" +
            "   field_id             varchar(32) not null comment '字段ID',\n" +
            "   pseudo_code          varchar(512) comment '伪代码',\n" +
            "   real_code            varchar(512) comment '技术代码',\n" +
            "   extend_items         varchar(512) comment '扩展属性',\n" +
            "   primary key (field_src_id)\n" +
            ");\n" +
            "\n" +
            "\n" +
            "create table dacp_meta_tab_field_src_his\n" +
            "(\n" +
            "   his_id               varchar(32) not null comment '历史记录ID',\n" +
            "   tab_src_his_id       varchar(32) comment '表来源历史记录ID',\n" +
            "   field_src_id         varchar(32) not null comment '字段来源记录ID',\n" +
            "   src_id               varchar(32) comment '来源记录ID',\n" +
            "   tab_id               varchar(32) not null comment '模型ID',\n" +
            "   field_id             varchar(32) not null comment '字段ID',\n" +
            "   pseudo_code          varchar(512) comment '伪代码',\n" +
            "   real_code            varchar(512) comment '技术代码',\n" +
            "   extend_items         varchar(512) comment '扩展属性',\n" +
            "   primary key (his_id)\n" +
            ");\n" +
            "\n" +
            "create table dacp_meta_tab_field_src_rela\n" +
            "(\n" +
            "   field_src_rela_id    varchar(32) not null comment '字段来源关系记录ID',\n" +
            "   field_src_id         varchar(32) comment '字段来源记录ID',\n" +
            "   tab_id               varchar(32) not null comment '模型ID',\n" +
            "   field_id             varchar(32) not null comment '字段ID',\n" +
            "   src_tab_id           varchar(32) not null comment '来源模型ID',\n" +
            "   src_field_id         varchar(32) not null comment '来源字段ID',\n" +
            "   src_field_name       varchar(32) not null comment '来源字段名',\n" +
            "   extend_items         varchar(512) comment '扩展属性',\n" +
            "   primary key (field_src_rela_id)\n" +
            ");\n" +
            "\n" +
            "\n" +
            "/*==============================================================*/\n" +
            "create table dacp_meta_tab_field_src_rela_alter\n" +
            "(\n" +
            "   field_src_rela_id    varchar(32) not null comment '字段来源关系记录ID',\n" +
            "   field_src_id         varchar(32) not null comment '字段来源记录ID',\n" +
            "   tab_id               varchar(32) not null comment '模型ID',\n" +
            "   field_id             varchar(32) not null comment '字段ID',\n" +
            "   src_tab_id           varchar(32) not null comment '来源模型ID',\n" +
            "   src_field_id         varchar(32) not null comment '来源字段ID',\n" +
            "   src_field_name       varchar(32) not null comment '来源字段名',\n" +
            "   extend_items         varchar(512) comment '扩展属性',\n" +
            "   primary key (field_src_rela_id)\n" +
            ");\n" +
            "\n" +
            "create table dacp_meta_tab_field_src_rela_his\n" +
            "(\n" +
            "   his_id               varchar(32) not null comment '历史记录ID',\n" +
            "   field_src_his_id     varchar(32) comment '字段来源历史记录ID',\n" +
            "   field_src_rela_id    varchar(32) not null comment '字段来源关系记录ID',\n" +
            "   field_src_id         varchar(32) not null comment '字段来源记录ID',\n" +
            "   tab_id               varchar(32) not null comment '模型ID',\n" +
            "   field_id             varchar(32) not null comment '字段ID',\n" +
            "   src_tab_id           varchar(32) not null comment '来源模型ID',\n" +
            "   src_field_id         varchar(32) not null comment '来源字段ID',\n" +
            "   src_field_name       varchar(32) not null comment '来源字段名',\n" +
            "   extend_items         varchar(512) comment '扩展属性',\n" +
            "   primary key (his_id)\n" +
            ");\n" +
            "\n" +
            "create table dacp_meta_tab_his\n" +
            "(\n" +
            "   his_id               varchar(32) not null comment '历史记录ID',\n" +
            "   tab_id               varchar(32) not null comment '模型ID',\n" +
            "   tab_name             varchar(64) not null comment '模型英文名',\n" +
            "   ds_name              varchar(32) not null comment '数据源编码',\n" +
            "   tab_schema           varchar(64) comment '生产库模式名',\n" +
            "   tab_schema_dev       varchar(64) comment '开发库模式名',\n" +
            "   tab_label            varchar(512) comment '模型中文名',\n" +
            "   tab_descr            varchar(1024) comment '模型描述',\n" +
            "   logic_tab_name       varchar(64) comment '逻辑模型名',\n" +
            "   phy_tab_name         varchar(64) comment '物理表名',\n" +
            "   tab_sens_lvl         varchar(64) comment '敏感级别',\n" +
            "   mask_type            varchar(16) comment '脱敏类型',\n" +
            "   lvl                  varchar(32) comment '层次编码',\n" +
            "   topic                varchar(32) comment '主题编码',\n" +
            "   cycle_type           varchar(8) comment '周期类型',\n" +
            "   tab_space            varchar(16) comment '表空间',\n" +
            "   tab_sn               varchar(16) comment '模型编码',\n" +
            "   version              varchar(16) comment '模型版本号',\n" +
            "   team_name            varchar(64) comment '团队编码',\n" +
            "   member_name          varchar(128) comment '责任人',\n" +
            "   open_state           varchar(8) comment '开放状态',\n" +
            "   asset_sn             varchar(32) comment '资产编码',\n" +
            "   extend_items         varchar(1024) comment '扩展属性',\n" +
            "   busi_caliber         varchar(512) comment '业务口径',\n" +
            "   state                varchar(16) not null comment '状态',\n" +
            "   state_date           datetime comment '状态时间',\n" +
            "   is_share             varchar(16) comment '是否分享模型',\n" +
            "   is_tran              varchar(16) comment '是否移植模型',\n" +
            "   model_type           varchar(16) comment '模型类型',\n" +
            "   share_type           varchar(16) comment '模型分享类型',\n" +
            "   recommend_cnt        int comment '推荐数量',\n" +
            "   storage_time         varchar(128) comment '保留周期',\n" +
            "   is_public            varchar(1) comment '是否公用表',\n" +
            "   ownership            varchar(32) comment '模型归属',\n" +
            "   create_date          datetime comment '创建时间',\n" +
            "   lastupd              datetime comment '最后更新时间',\n" +
            "   create_user          varchar(16) comment '创建人',\n" +
            "   lastupd_user         varchar(16) comment '最后更新人',\n" +
            "   flow_inst_id         varchar(32) comment '关联流程ID',\n" +
            "   primary key (his_id)\n" +
            ")\n" +
            "collate = utf8_bin;\n" +
            "\n" +
            "\n" +
            "create table dacp_meta_tab_open\n" +
            "(\n" +
            "   open_id              varchar(32) comment '开放ID',\n" +
            "   tab_id               varchar(32) comment '开放模型ID',\n" +
            "   open_target_type     varchar(16) comment '开放对象类型',\n" +
            "   open_target_id       varchar(64) comment '开放对象ID',\n" +
            "   open_state           varchar(8) comment '开放状态',\n" +
            "   extend_items         varchar(128) comment '扩展属性'\n" +
            ");\n" +
            "\n" +
            "create table dacp_meta_tab_open_log\n" +
            "(\n" +
            "   open_log_id          varchar(32) comment '开放日志ID',\n" +
            "   open_id              varchar(32) comment '开放记录ID',\n" +
            "   operation            varchar(16) comment '开放操作',\n" +
            "   operator             varchar(64 comment '操作人',\n" +
            "   operate_result       varchar(16) comment '操作结果',\n" +
            "   operate_time         datetime comment '操作时间',\n" +
            "   operate_msg          varchar(255) comment '操作信息'\n" +
            ");\n" +
            "\n" +
            "create table dacp_meta_tab_src\n" +
            "(\n" +
            "   src_id               varchar(32) not null comment '来源记录ID',\n" +
            "   tab_id               varchar(32) comment '模型ID',\n" +
            "   src_tab_id           varchar(32) not null comment '来源模型ID',\n" +
            "   src_tab_sort         int comment '来源表序号',\n" +
            "   src_tab_sql          varchar(1024) comment '来源表SQL',\n" +
            "   src_tab_alias_name   varchar(32) comment '来源表别名',\n" +
            "   extend_items         varchar(512) comment '扩展属性',\n" +
            "   primary key (src_id)\n" +
            ");\n" +
            "\n" +
            "\n" +
            "create table dacp_meta_tab_src_alter\n" +
            "(\n" +
            "   src_id               varchar(32) not null comment '来源记录ID',\n" +
            "   tab_id               varchar(32) comment '模型ID',\n" +
            "   src_tab_id           varchar(32) not null comment '来源模型ID',\n" +
            "   src_tab_sort         int comment '来源表序号',\n" +
            "   src_tab_sql          varchar(1024) comment '来源表SQL',\n" +
            "   src_tab_alias_name   varchar(32) comment '来源表别名',\n" +
            "   extend_items         varchar(512) comment '扩展属性',\n" +
            "   primary key (src_id)\n" +
            ");\n" +
            "\n" +
            "\n" +
            "\n" +
            "create table dacp_meta_tab_src_his\n" +
            "(\n" +
            "   his_id               varchar(32) not null comment '历史记录ID',\n" +
            "   table_his_id         varchar(32) comment '模型历史记录ID',\n" +
            "   src_id               varchar(32) not null comment '来源记录ID',\n" +
            "   tab_id               varchar(32) comment '模型ID',\n" +
            "   src_tab_id           varchar(32) not null comment '来源模型ID',\n" +
            "   src_tab_sort         int comment '来源表序号',\n" +
            "   src_tab_sql          varchar(1024) comment '来源表SQL',\n" +
            "   src_tab_alias_name   varchar(32) comment '来源表别名',\n" +
            "   extend_items         varchar(512) comment '扩展属性',\n" +
            "   primary key (his_id)\n" +
            ");";

    @Test
    public void generatePoStr() {
        PoGenerator.batchGeneratePoStr(DdlResolver.batchResolveDdls(ddl)).forEach(s->{
            System.out.println(s);
        });
    }

    @Test
    public void batchGeneratePoFiles() throws IOException {
        PoGenerator.batchGeneratePoFiles("C:\\Users\\Administrator\\Desktop",DdlResolver.batchResolveDdls(ddl));
    }
}