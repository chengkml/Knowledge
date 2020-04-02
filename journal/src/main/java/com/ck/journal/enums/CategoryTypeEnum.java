package com.ck.journal.enums;

/**
 * 目录类型
 */
public enum CategoryTypeEnum {
    YEAR("year", "年"),
    SEASON("season", "季度"),
    MONTH("month", "月"),
    WEEK("week", "周"),
    DAY("day", "日");
    private String name;

    private String label;

    private CategoryTypeEnum(String name, String label) {
        this.name = name;
        this.label = label;
    }

    public String label() {
        return this.label;
    }}
