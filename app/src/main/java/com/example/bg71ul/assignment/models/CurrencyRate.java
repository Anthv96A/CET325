package com.example.bg71ul.assignment.models;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Anthony Vest on 13/12/2017.
 */
public class CurrencyRate implements Serializable{

    private String currencyType;
    private float currencyRate;

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public float getCurrencyRate() {
        return currencyRate;
    }

    public void setCurrencyRate(float currencyRate) {
        this.currencyRate = currencyRate;
    }
}
