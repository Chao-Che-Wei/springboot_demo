package com.vicent.demo.parameter;

public class ProductQueryParameter {
    private String keyword;
    private String orderBy;
    private String sortRule;

    public String getKeyword() {
        return keyword;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public String getSortRule() {
        return sortRule;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public void setSortRule(String sortRule) {
        this.sortRule = sortRule;
    }
}
