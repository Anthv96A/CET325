package com.example.bg71ul.assignment.models;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Anthony Vest on 07/12/2017.
 */
@Getter
@Setter
public class Gallery {

    private int id;
    private String artist;
    private String title;
    private String room;
    private String description;
    private String rank;
    private String year;


}
