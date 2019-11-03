package com.example.last;

public class Users {
    private int age;
    private String id;
    private String password;
    private String email;
    private String sex;
    private String graduation;
    private String authority;
    private String level;
    private String hp;
    private String name;
    private boolean agreement;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
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

    public String getAuthority() {
        return authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getHp() {
        return hp;
    }

    public void setHp(String hp) {
        this.hp = hp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isAgreement() {
        return agreement;
    }

    public void setAgreement(boolean agreement) {
        this.agreement = agreement;
    }

     //나이,아이디,패스워드,이메일,성,졸업,권한,레벨,핸드폰,이름,동의
    public Users(int age, String id, String password, String email, String sex, String graduation, String authority, String level, String hp, String name, boolean agreement) {
        this.age = age;
        this.id = id;
        this.password = password;
        this.email = email;
        this.sex = sex;
        this.graduation = graduation;
        this.authority = authority;
        this.level = level;
        this.hp = hp;
        this.name = name;
        this.agreement = agreement;
    }
}

