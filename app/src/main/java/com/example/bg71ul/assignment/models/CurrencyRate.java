package com.example.bg71ul.assignment.models;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Anthony Vest on 13/12/2017.
 */
@Getter
@Setter
public class CurrencyRate implements Serializable{

    private String currencyType;
    private float currencyRate;
}
