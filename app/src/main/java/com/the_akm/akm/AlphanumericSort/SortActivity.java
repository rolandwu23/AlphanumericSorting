package com.the_akm.akm.AlphanumericSort;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SortActivity extends AppCompatActivity {

//    implements LoaderManager.LoaderCallbacks<List<String>>
//     private static final int loaderID = 1;

    View loadingIndicator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadingIndicator=findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.VISIBLE);

        // Use LoaderManager and AsyncTaskLoader instead of AsyncTask because of screen orientation changes.
        // If activity destroyed and recreated, asyncTask unable to access new activity
        // and they tend to stay around leading to out of memory.

//        final LoaderManager loaderManager = getLoaderManager();
//        loaderManager.initLoader(loaderID,null,this);

        // starting from API 28, Loader was deprecated and enforces developers to use ViewModel and LiveData
        StringViewModel stringViewModel = ViewModelProviders.of(this).get(StringViewModel.class);
        stringViewModel.getAllData().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(@Nullable List<String> strings) {
                generate2DArray(strings);
                loadingIndicator.setVisibility(View.GONE);
            }
        });
    }


//    method for finding the distinct values and their relative positions in sorted array
     public void generate2DArray(List<String> stringList){

        String[] values = stringList.toArray(new String[0]);

        // can use just Arrays.sort(). Custom comparator supports more abstract input data type.
        Arrays.sort(values,new AlphanumericSorting());

        Integer[] sourceArray = new Integer[values.length];
        for(int x=0 ; x<values.length;x++)
        {
            // assuming input data type to be 11A. needs to implement further to support more abstract type.
            sourceArray[x] = Integer.valueOf(values[x].substring(0,values[x].length()-1));
        }

        Set<Integer> targetSet = new HashSet<>();
        Collections.addAll(targetSet, sourceArray);

        Integer[] resultArray = targetSet.toArray(new Integer[0]);
        Arrays.sort(resultArray);

        ArrayList<ArrayList<Integer>> occurrence = new ArrayList<>();

        int max = 0;

        for(Integer result: resultArray){
            ArrayList<Integer> occur = new ArrayList<>();
            for(int x=0; x< sourceArray.length;x++)
            {
                if(result.equals(sourceArray[x])){
                    occur.add(x);
                }
            }
            occurrence.add(occur);
            if(occur.size() >=  max){
                max = occur.size();
            }
        }

        int count = 0;

        ArrayList<ArrayList<Integer>> rows = new ArrayList<>();

        while (count < max){
            ArrayList<Integer> row = new ArrayList<>();
            for(ArrayList<Integer> item: occurrence){
                if(count < item.size()){
                    row.add(item.get(count));
                }else{
                    row.add(-1);
                }
            }
            count += 1;
            rows.add(row);
        }

        InflateView it = new InflateView(resultArray.length,max,rows,values,this);
        it.InflateTableLayout();
    }

//    @Override
//    public Loader<List<String>> onCreateLoader(int id, Bundle args) {
//       return new ReadFileLoader(this);
//    }
//
//    @Override
//    public void onLoadFinished(Loader<List<String>> loader, List<String> positions) {
//        loadingIndicator.setVisibility(View.GONE);
//        generate2DArray(positions);
//    }
//
//    @Override
//    public void onLoaderReset(Loader<List<String>> loader) { }


    // inflating tableLayout dynamically, thus create a stand-alone class to stand apart from logic
    class InflateView {
        int resultArraySize;
        int max;
        ArrayList<ArrayList<Integer>>  rows;
        String[] values;
        Context context;

        private InflateView(int resultArraySize,int max, ArrayList<ArrayList<Integer>>  rows,String[] values,Context context){
            this.resultArraySize = resultArraySize;
            this.max = max;
            this.rows = rows;
            this.values = values;
            this.context = context;
        }

        private void InflateTableLayout(){
            TableLayout tableLayout = (TableLayout) findViewById(R.id.activity_main_tableLayout);

            ArrayList<TextView> textViewArrayList = createTVarrayList(resultArraySize,max);
            int z = 0;

            for(ArrayList<Integer> r : rows){
                TableRow tableRow = new TableRow(context);
                for(Integer item : r){
                    TextView tb = new TextView(context);
                    if(item != -1) {
                        tb.setText(values[item]);
                        tb.setTextSize(18.0f);
                    }else{
                        tb.setText("");
                    }
                    tableRow.addView(tb);
                    tableRow.addView(textViewArrayList.get(z));
                    z += 1;
                }
                tableLayout.addView(tableRow);
            }
        }

        private ArrayList<TextView> createTVarrayList(int resultArraySize,int max){
            ArrayList<TextView> textViewArrayList = new ArrayList<>();

            for(int x = 0 ; x < (resultArraySize * max)+1 ; x++) {
                TextView tb = new TextView(context);
                tb.setText("    ");
                textViewArrayList.add(tb);
            }

            return textViewArrayList;
        }
    }

}
