package com.example.moneyexchanger;

import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Task extends AsyncTask<String, Void, Double> {

    String clientKey = "ssCVcdVJASuD7W9hdqPelYhpM72tOKC4Tqchs1cH";;
    private String str, receiveMsg;



    // MainActivity에서 execute 의 인자로 string 배열을 넣고 여기서 params로 받는다.
    @Override
    protected Double doInBackground(String... params) {

        String from = params[0];
        String to = params[1];
        URL url = null;

        double currencyRate = 0.0;


        try {
            url = new URL("https://api.freecurrencyapi.com/v1/latest?apikey="
                    +clientKey
                    +"&currencies="
                    +from
                    +"," + to);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            if (conn.getResponseCode() == conn.HTTP_OK) {
                InputStreamReader tmp = new InputStreamReader(conn.getInputStream(), "UTF-8");
                BufferedReader reader = new BufferedReader(tmp);
                StringBuffer buffer = new StringBuffer();
                while ((str = reader.readLine()) != null) {
                    buffer.append(str);
                }
                receiveMsg = buffer.toString();
                Log.e("receiveMsg : ", receiveMsg);

                reader.close();
            } else {
                Log.e("통신 결과", conn.getResponseCode() + "에러");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        try {
            JsonParser jsonParser = new JsonParser();
            Object obj = jsonParser.parse(receiveMsg);

            JsonObject jsonObj = (JsonObject) obj;

            JsonObject curobj = (JsonObject) jsonObj.get("data");

            Log.e("parse", from + " " +  curobj.get(from).toString());
            Log.e("parse", to + " " + curobj.get(to).toString());
            String a = curobj.get(from).toString();
            String b = curobj.get(to).toString();

            currencyRate = Double.parseDouble(b)/Double.parseDouble(a);
            Log.e("parse", "currencyRate " + currencyRate);


        }catch (JsonParseException e){
            e.printStackTrace();
        }
        return currencyRate;
    }
}