package com.app.entity;

public class CurrencyEntity {

    private Integer id;
    private String name;
    private String code;
    private String sign;

    public CurrencyEntity() {

    }

    public CurrencyEntity(String name, String code, String sign) {
        this.name = name;
        this.code = code;
        this.sign = sign;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
