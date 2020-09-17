package com.vicent.demo.parameter;

public class ProductQueryParameter {
    private String keyword;
    private Integer priceFrom;
    private Integer priceTo;
    private String orderBy;
    private String sortRule;

    public Integer getPriceFrom() {
        return priceFrom;
    }

    public Integer getPriceTo() {
        return priceTo;
    }

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

    public void setPriceFrom(Integer priceFrom) {
        this.priceFrom = priceFrom;
    }

    public void setPriceTo(Integer priceTo) {
        this.priceTo = priceTo;
    }
}
