package ua.cn.stu.domain;

import java.math.BigDecimal;

public class Goods {
    private Long goodsId;
    private String goodsName;
    private BigDecimal goodsPrice;
    private Long supplierId;
    private Integer goodsQuantity;
    private String supplierName;

    public Goods() {}

    public Goods(String goodsName, BigDecimal goodsPrice, Long supplierId, Integer goodsQuantity) {
        this.goodsName = goodsName;
        this.goodsPrice = goodsPrice;
        this.supplierId = supplierId;
        this.goodsQuantity = goodsQuantity;
    }

    public Long getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Long goodsId) {
        this.goodsId = goodsId;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public BigDecimal getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(BigDecimal goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getGoodsQuantity() {
        return goodsQuantity;
    }

    public void setGoodsQuantity(Integer goodsQuantity) {
        this.goodsQuantity = goodsQuantity;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    @Override
    public String toString() {
        return "ID: " + goodsId + " | " + goodsName + " | " + goodsPrice + " грн | Кількість: " +
                goodsQuantity + " | Постачальник: " + supplierName;
    }
}
