package com.ra.model.entity;

import com.ra.util.Annotation.*;

@Table(name = "accounts")
public class Account {
    @Id
    @Column(name = "Acc_id")
    private Integer accId;
    @Auth
    @Index(name = "User_Name")
    @Column(name = "User_name")
    private String userName;
    @Auth
    @Column(name = "Password")
    private String password;
    @Column(name = "Permission")
    private Boolean permission;
    @Index(name = "Emp_id")
    @Column(name = "Emp_id")
    private String employeeId;
    @Column(name = "Acc_status")
    private Boolean accountStatus;

    public Account() {
    }

    public Account(Integer accId, String userName, String password, Boolean permission, String employeeId, Boolean accountStatus) {
        this.accId = accId;
        this.userName = userName;
        this.password = password;
        this.permission = permission;
        this.employeeId = employeeId;
        this.accountStatus = accountStatus;
    }

    public Integer getAccId() {
        return accId;
    }

    public void setAccId(Integer accId) {
        this.accId = accId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public Boolean getPermission() {
        return permission;
    }

    public void setPermission(Boolean permission) {
        this.permission = permission;
    }

    public Boolean getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(Boolean accountStatus) {
        this.accountStatus = accountStatus;
    }
}
