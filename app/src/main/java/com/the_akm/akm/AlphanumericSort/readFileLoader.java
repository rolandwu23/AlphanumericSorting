package com.the_akm.akm.AlphanumericSort;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.support.annotation.Nullable;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class readFileLoader extends AsyncTaskLoader<List<String>> {
    Context context;


    public readFileLoader(Context context){
        super(context);
        this.context = context;

    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Nullable
    @Override
    public List<String> loadInBackground() {
        StringBuilder sb = new StringBuilder();

        List<String> positions = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(context.getAssets().open("assign.json")));
            String temp;
            while ((temp = br.readLine()) != null)
                sb.append(temp);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String myjsonstring = sb.toString();
        try {
            JSONObject jsonObjMain = new JSONObject(myjsonstring);

            JSONArray jsonArray = jsonObjMain.getJSONArray("Items");

            for (int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject jsonObj = jsonArray.getJSONObject(i);
                String question = jsonObj.getString("Position");
                positions.add(question);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return positions;

    }

}
