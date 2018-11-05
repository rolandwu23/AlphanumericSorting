package com.the_akm.akm.AlphanumericSort;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class StringViewModel extends AndroidViewModel {

    private MutableLiveData<List<String>> positions;


    StringViewModel(@NonNull Application application){
        super(application);
    }

    public LiveData<List<String>> getAllData(){

        if(positions == null){
            positions = new MutableLiveData<>();
            new ReadJsonFile().execute();
        }

        return positions;
    }

    private class ReadJsonFile extends AsyncTask<Void, Void, List<String>>{

            @Override
            protected List<String> doInBackground (Void...voids){
                StringBuilder sb = new StringBuilder();

                List<String> positions = new ArrayList<>();

                try {
                    BufferedReader br = new BufferedReader(new InputStreamReader(getApplication().getAssets().open("assign.json")));
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

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObj = jsonArray.getJSONObject(i);
                        String question = jsonObj.getString("Position");
                        positions.add(question);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return positions;
            }

            @Override
            protected void onPostExecute (List < String > strings) {
                super.onPostExecute(strings);

                positions.setValue(strings);
            }
        }

}
