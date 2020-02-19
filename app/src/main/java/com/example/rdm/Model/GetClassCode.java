package com.example.rdm.Model;

import android.util.Log;

import com.example.rdm.activities.MainActivity;

import java.util.HashMap;

public class GetClassCode {
    static HashMap<String, String> codeHash = new HashMap<String, String>();

    static {
        init();
    }

    public static void init() {

        for (int i = 0; i < MainActivity.neighborhoodList.size(); i++) {
            codeHash.put(MainActivity.neighborhoodList.get(i).getCity_id(), MainActivity.neighborhoodList.get(i).getName_ar());
        }

    }

    public static String getCode(String param) {
        return codeHash.get(param);
    }

}
