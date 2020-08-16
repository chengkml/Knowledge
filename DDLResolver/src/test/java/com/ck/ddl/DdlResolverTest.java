package com.ck.ddl;

import org.junit.Test;

import static org.junit.Assert.*;

public class DdlResolverTest {

    @Test
    public void resolveDdl() {
        String ddl = "/** Sql Generate **/\n" +
                "\n" +
                "create table  dacp_meta_tab_src\n" +
                "(\n" +
                "       src_id            VARCHAR(32) not null \t/*唯一标识*/,\n" +
                "       tab_id            VARCHAR(32) not null \t/*模型标识 模型ID*/,\n" +
                "       src_tab_id        VARCHAR(32) not null \t/*来源表标识 来源模型ID*/,\n" +
                "       src_tab_sql       VARCHAR(1024) \t/*来源表SQL 查询来源表的SQL*/,\n" +
                "       src_tab_alias_name VARCHAR(32) \t/*来源表别名*/,\n" +
                "       src_tab_sort      INTEGER \t/*来源表顺序*/,\n" +
                "       extend_items      TEXT \t/*扩展属性*/,\n" +
                "       create_user       VARCHAR(32) \t/*创建人*/,\n" +
                "       create_date       DATETIME \t/*创建时间*/,\n" +
                "       last_update_user  VARCHAR(4000) \t/*最后更新人*/,\n" +
                "       last_update_date  DATETIME \t/*最后更新时间*/\n" +
                ");\n" +
                "alter  table dacp_meta_tab_src\n" +
                "       add constraint PK_dacp_mesrc_src_idF336 primary key (src_id);\n" +
                "alter  table dacp_meta_tab_src\n" +
                "       add constraint FK_dacp_mesrc_tab_id76BE foreign key (tab_id)\n" +
                "       references dacp_meta_tab(tab_id);\n" +
                "alter  table dacp_meta_tab_src\n" +
                "       add constraint FK_dacp_mesrc_src_tab_id1CD6 foreign key (src_tab_id)\n" +
                "       references dacp_meta_tab(tab_id);\n" +
                "\n" +
                "\n" +
                "select\n" +
                "  src_id              as 唯一标识,\n" +
                "  tab_id              as 模型标识,\n" +
                "  src_tab_id          as 来源表标识,\n" +
                "  src_tab_sql         as 来源表SQL,\n" +
                "  src_tab_alias_name  as 来源表别名,\n" +
                "  src_tab_sort        as 来源表顺序,\n" +
                "  extend_items        as 扩展属性,\n" +
                "  create_user         as 创建人,\n" +
                "  create_date         as 创建时间,\n" +
                "  last_update_user    as 最后更新人,\n" +
                "  last_update_date    as 最后更新时间\n" +
                "from dacp_meta_tab_src t;\n" +
                "\n" +
                "insert into dacp_meta_tab_src\n" +
                "(\n" +
                "  src_id,\n" +
                "  tab_id,\n" +
                "  src_tab_id,\n" +
                "  src_tab_sql,\n" +
                "  src_tab_alias_name,\n" +
                "  src_tab_sort,\n" +
                "  extend_items,\n" +
                "  create_user,\n" +
                "  create_date,\n" +
                "  last_update_user,\n" +
                "  last_update_date\n" +
                ")\n" +
                "values(\n" +
                "  :v_src_id,             /*src_id*/\n" +
                "  :v_tab_id,             /*tab_id*/\n" +
                "  :v_src_tab_id,         /*src_tab_id*/\n" +
                "  :v_src_tab_sql,        /*src_tab_sql*/\n" +
                "  :v_src_tab_alias_name, /*src_tab_alias_name*/\n" +
                "  :v_src_tab_sort,       /*src_tab_sort*/\n" +
                "  :v_extend_items,       /*extend_items*/\n" +
                "  :v_create_user,        /*create_user*/\n" +
                "  :v_create_date,        /*create_date*/\n" +
                "  :v_last_update_user,   /*last_update_user*/\n" +
                "  :v_last_update_date    /*last_update_date*/\n" +
                ");\n" +
                "\n" +
                "insert into dacp_meta_tab_src\n" +
                "(\n" +
                "  src_id,\n" +
                "  tab_id,\n" +
                "  src_tab_id,\n" +
                "  src_tab_sql,\n" +
                "  src_tab_alias_name,\n" +
                "  src_tab_sort,\n" +
                "  extend_items,\n" +
                "  create_user,\n" +
                "  create_date,\n" +
                "  last_update_user,\n" +
                "  last_update_date\n" +
                ")\n" +
                "values(\n" +
                "  '唯一标识4',        /*src_id*/\n" +
                "  '模型标识7',        /*tab_id*/\n" +
                "  '来源表标识8',      /*src_tab_id*/\n" +
                "  '来源表SQL5',       /*src_tab_sql*/\n" +
                "  '来源表别名4',      /*src_tab_alias_name*/\n" +
                "  3,                  /*src_tab_sort*/\n" +
                "  '扩展属性9',        /*extend_items*/\n" +
                "  '创建人8',          /*create_user*/\n" +
                "  '2020-04-07 11:18:44', /*create_date*/\n" +
                "  '最后更新人6',      /*last_update_user*/\n" +
                "  '2020-04-07 22:59:11' /*last_update_date*/\n" +
                ");\n" +
                "\n" +
                "update dacp_meta_tab_src\n" +
                "set\n" +
                "  src_id              = :v_src_id,\n" +
                "  tab_id              = :v_tab_id,\n" +
                "  src_tab_id          = :v_src_tab_id,\n" +
                "  src_tab_sql         = :v_src_tab_sql,\n" +
                "  src_tab_alias_name  = :v_src_tab_alias_name,\n" +
                "  src_tab_sort        = :v_src_tab_sort,\n" +
                "  extend_items        = :v_extend_items,\n" +
                "  create_user         = :v_create_user,\n" +
                "  create_date         = :v_create_date,\n" +
                "  last_update_user    = :v_last_update_user,\n" +
                "  last_update_date    = :v_last_update_date\n" +
                "where src_id = :v_src_id;\n" +
                "\n" +
                "delete from dacp_meta_tab_src\n" +
                "where src_id = :v_src_id;\n";
        Table table = DdlResolver.resolveDdl(ddl);
        assertEquals("dacp_meta_tab_src",table.getName());
        assertEquals(11,table.getColumns().size());
        assertEquals("src_id",table.getColumns().get(0).getName());
        assertEquals(32,table.getColumns().get(0).getLength());
        assertEquals(32,table.getColumns().get(0).getLength());
        assertEquals(true,table.getColumns().get(3).isNullable());
        assertEquals(true,table.getColumns().get(0).isPk());
        assertEquals("唯一标识",table.getColumns().get(0).getComment());
    }
}