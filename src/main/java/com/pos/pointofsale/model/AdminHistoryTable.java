package com.pos.pointofsale.model;

public class AdminHistoryTable {
    private String orderId;
    private String empId;
    private String totalPrice;
    private String orderDate;
    private String orderTime;

    public AdminHistoryTable(String orderId, String empId, String totalPrice, String orderDate, String orderTime) {
        this.orderId = orderId;
        this.empId = empId;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
        this.orderTime = orderTime;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }
}
