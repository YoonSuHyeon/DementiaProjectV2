package com.example.last;

public class Users {
    private String age;
    private String id;
    private String password;
    private String email;
    private String sex;
    private String graduation;
    private String authority;
    private String level;
    private  boolean agreement;

    public boolean isAgreement() {
        return agreement;
    }

    public void setAgreement(boolean agreement) {
        this.agreement = agreement;
    }

    public Users(String age, String id, String password, String email, String sex, String graduation, String  authority, String level, boolean agreement,String hp,String name) {
        this.age = age;
        this.id = id;
        this.password = password;
        this.email = email;
        this.sex = sex;
        this.graduation = graduation;
        this.level = level;
        this.authority=authority;
        this.agreement=agreement;
    }

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getGraduation() {
        return graduation;
    }

    public void setGraduation(String graduation) {
        this.graduation = graduation;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
