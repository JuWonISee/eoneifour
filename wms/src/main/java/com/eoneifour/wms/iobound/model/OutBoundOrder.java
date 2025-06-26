package com.eoneifour.wms.iobound.model;

import java.sql.Date;

import com.eoneifour.shopadmin.product.model.Product;

public class OutBoundOrder {

    private int outbound_order_id;     // 출고 요청 ID
    private int quantity;              // 출고 수량
    private int status;                // 출고 상태 (4~6)
    private Date request_date;         // 출고 요청일
    private Date complete_date;        // 출고 완료일
    private int requested_by;          // 출고 요청자 ID
    private int product_id;            // 상품 ID
    private Product product;           // 상품 객체

    public int getOutbound_order_id() {
        return outbound_order_id;
    }

    public void setOutbound_order_id(int outbound_order_id) {
        this.outbound_order_id = outbound_order_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getRequest_date() {
        return request_date;
    }

    public void setRequest_date(Date request_date) {
        this.request_date = request_date;
    }

    public Date getComplete_date() {
        return complete_date;
    }

    public void setComplete_date(Date complete_date) {
        this.complete_date = complete_date;
    }

    public int getRequested_by() {
        return requested_by;
    }

    public void setRequested_by(int requested_by) {
        this.requested_by = requested_by;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}