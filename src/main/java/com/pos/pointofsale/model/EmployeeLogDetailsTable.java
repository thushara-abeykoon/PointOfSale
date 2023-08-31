package com.pos.pointofsale.model;

public class EmployeeLogDetailsTable {
   private String cmpId;
   private String login;
   private String logout;

    public EmployeeLogDetailsTable(String cmpId, String login, String logout) {
        this.cmpId = cmpId;
        this.login = login;
        this.logout = logout;
    }


    public String getCmpId() {
        return cmpId;
    }

    public void setCmpId(String cmpId) {
        this.cmpId = cmpId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getLogout() {
        return logout;
    }

    public void setLogout(String logout) {
        this.logout = logout;
    }
}
