package com.pos.pointofsale.model;

public class AdminEmployeesTable {
    private String empId;
    private String empName;
    private String empEmail;
    private String phoneNo;
    public AdminEmployeesTable(String empId, String empName, String empEmail, String phoneNo) {
        this.empId = empId;
        this.empName = empName;
        this.empEmail = empEmail;
        this.phoneNo = phoneNo;
    }

    public String getEmpId() {
        return empId;
    }

    public void setEmpId(String empId) {
        this.empId = empId;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getEmpEmail() {
        return empEmail;
    }

    public void setEmpEmail(String empEmail) {
        this.empEmail = empEmail;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }


}
