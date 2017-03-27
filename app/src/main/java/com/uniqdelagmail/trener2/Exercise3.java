package com.uniqdelagmail.trener2;

import android.app.Activity;
import android.content.ContentValues;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Exercise3 extends Activity {

    //region OBYAVLENIYA PEREMENNIH
    ImageButton plusWeightValueButton;
    ImageButton minusWeightValueButton;
    ImageButton plusQuantityValueButton;
    ImageButton minusQuantityValueButton;
    ImageButton addToResultButton;
    ImageButton addToHistoryButton;

    TextView weightValue;
    TextView quantityValue;

    TextView exerciseResult;

    public float bestWeight = 0;
    public float sumOfWeight = 0;
    public float bestSumOfWeight = 0;

    SharedPreferences sPref;
    final String SAVED_WEIGHT_VALUE = "saved_weight_value";
    final String SAVED_QUANTITY_VALUE = "saved_quantity_value";
    final String SAVED_BEST_WEIGHT = "saved_best_weight";
    final String SAVED_SUM_OF_WEIGHT = "saved_sum_of_weight";
    final String SAVED_BEST_SUM_OF_WEIGHT = "saved_best_sum_of_weight";

    DBHelper dbHelper;

    Set<String> exerciseHistory = new HashSet<String>();
    ArrayAdapter<String> adapter;
    List<String> exerciseHistoryArray = new ArrayList<String>(exerciseHistory);

    //SQLiteDatabase database = dbHelper.getWritableDatabase();
    //ContentValues contentValues = new ContentValues();

    int dayRecord = 0;
    int historyRecord = 0;
    //String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    String date = "07.09.2016";

//////////////////////////////////////////////////////////////////////////////////////////
    String[] name = { "Иван", "Марья", "Петр", "Антон", "Даша", "Борис",                //
            "Костя", "Игорь" };                                                         //
    String[] position = { "Программер", "Бухгалтер", "Программер",                      //
            "Программер", "Бухгалтер", "Директор", "Программер", "Охранник" };          //
    int salary[] = { 13000, 10000, 13000, 13000, 10000, 15000, 13000, 8000 };           //
                                                                                        //
    int[] colors = new int[2];                                                          //
                                                                                        //
//////////////////////////////////////////////////////////////////////////////////////////
    //endregion

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise3);

        plusWeightValueButton = (ImageButton) findViewById(R.id.plusWeightValueButton);
        minusWeightValueButton = (ImageButton) findViewById(R.id.minusWeightValueButton);
        plusQuantityValueButton = (ImageButton) findViewById(R.id.plusQuantityValueButton);
        minusQuantityValueButton = (ImageButton) findViewById(R.id.minusQuantityValueButton);
        addToResultButton = (ImageButton) findViewById(R.id.addToResultButton);
        addToHistoryButton = (ImageButton) findViewById(R.id.addToHistoryButton);
        weightValue = (TextView) findViewById(R.id.weightValue);
        quantityValue = (TextView) findViewById(R.id.quantityValue);
        exerciseResult = (TextView) findViewById(R.id.exerciseResult);

        //ListView listView = (ListView)findViewById(R.id.listView);

        //adapter = new ArrayAdapter<String>(this,
        //        R.layout.list_item, exerciseHistoryArray);

        //listView.setAdapter(adapter);

        dbHelper = new DBHelper(this);

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                switch (view.getId()){
                    case R.id.plusWeightValueButton:
                        plusWeightValue();
                        break;
                    case R.id.minusWeightValueButton:
                        minusWeightValue();
                        break;
                    case R.id.plusQuantityValueButton:
                        plusQuantityValue();
                        break;
                    case R.id.minusQuantityValueButton:
                        minusQuantityValue();
                        break;
                    case R.id.addToResultButton:
                        addToResult();
                        break;
                    case R.id.addToHistoryButton:
                        addToDB();
                        readFromDB();
                        exerciseResult.setText("");
                        break;
                    //case R.id.addToHistoryButton:
                    //    addToDB();      //НЕ МЕНЯТЬ МЕСТАМИ
                    //    addToHistory(); //ADD TO HISTORY ОБНУЛЯЕТ ПОЛЕ
                    //    break;
                }
            }
        };

        plusWeightValueButton.setOnClickListener(onClickListener);
        minusWeightValueButton.setOnClickListener(onClickListener);
        plusQuantityValueButton.setOnClickListener(onClickListener);
        minusQuantityValueButton.setOnClickListener(onClickListener);
        addToResultButton.setOnClickListener(onClickListener);
        addToHistoryButton.setOnClickListener(onClickListener);


        Log.d("LLOOGG_BEFORE", "ONCREATE");
        Log.d("LLOOGG_BEFORE", weightValue.getText().toString());

        //weightValue.setText("0");
        //quantityValue.setText("0");

        //if(weightValue.getText().length()==0) {
        //    loadVariables();
        //}
        //else{
        //    weightValue.setText("1");
        //}

        loadVariables();
        readFromDB();

        //Log.d("LLOOGG_AFTER", "ONCREATE");
        //Log.d("LLOOGG_AFTER", weightValue.getText().toString());

    }

    public void addToScrollView(){
        LinearLayout linLayout = (LinearLayout) findViewById(R.id.linLayout);
        LayoutInflater ltInflater = getLayoutInflater();

        for (int i = 0; i < name.length; i++) {
            Log.d("myLogs", "i = " + i);
            View item = ltInflater.inflate(R.layout.item, linLayout, false);
            TextView tvName = (TextView) item.findViewById(R.id.listResult);
            tvName.setText(name[i]);
            TextView tvPosition = (TextView) item.findViewById(R.id.listDate);
            tvPosition.setText("Должность: " + position[i]);
            TextView tvSalary = (TextView) item.findViewById(R.id.listNewWeight);
            tvSalary.setText("Оклад: " + String.valueOf(salary[i]));
            item.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
            item.setBackgroundColor(colors[i % 2]);
            linLayout.addView(item);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveVariables();
    }

    public void plusWeightValue(){
        weightValue.setText(String.valueOf(
                Float.parseFloat(weightValue.getText().toString())+2.5
        ));
    }

    public void minusWeightValue(){
        weightValue.setText(String.valueOf(
                Float.parseFloat(weightValue.getText().toString())-2.5
        ));
    }

    public void plusQuantityValue(){
        quantityValue.setText(String.valueOf(
                Integer.parseInt(quantityValue.getText().toString())+1
        ));}

    public void minusQuantityValue(){
        quantityValue.setText(String.valueOf(
                Integer.parseInt(quantityValue.getText().toString())-1
        ));}

    public void  addToResult(){
        float weightValueFloat = Float.parseFloat(weightValue.getText().toString());
        float quantityValueFloat = Float.parseFloat(quantityValue.getText().toString());

        if(weightValueFloat>bestWeight){
            bestWeight = weightValueFloat;
            sumOfWeight = weightValueFloat * quantityValueFloat;
        }
        else if(weightValueFloat==bestWeight){
            sumOfWeight = sumOfWeight + (weightValueFloat * quantityValueFloat);
        }

        exerciseResult.setText(exerciseResult.getText().toString()
                +weightValue.getText().toString()+"-"
                +quantityValue.getText().toString()+", ");
    }

    public void addToHistory() {
        exerciseHistoryArray.add(exerciseResult.getText().toString());
        adapter.notifyDataSetChanged();
        exerciseResult.setText("");
    }

    public void  addToDB(){
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelper.KEY_RESULT, exerciseResult.getText().toString());
        Log.d("LLLLLLOOOOG_ADD", exerciseResult.getText().toString());
        contentValues.put(DBHelper.KEY_NEW_WEIGHT_COUNT, dayRecord);
        contentValues.put(DBHelper.KEY_HISTORY_RECORD_COUNT, historyRecord);
        contentValues.put(DBHelper.KEY_DATE, date);

        database.insert(DBHelper.TABLE_EXERCISE1, null, contentValues);
    }

    public void readFromDB(){
        colors[0] = Color.parseColor("#559966CC");
        colors[1] = Color.parseColor("#55336699");

        int i = 0;
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_EXERCISE1, null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int exResultIndex = cursor.getColumnIndex(DBHelper.KEY_RESULT);
            do{
                exerciseHistoryArray.add(cursor.getString(exResultIndex));
                Log.d("LLLLOOOGGGG_READ", cursor.getString(exResultIndex));


                LinearLayout linLayout = (LinearLayout) findViewById(R.id.linLayout);
                LayoutInflater ltInflater = getLayoutInflater();

                Log.d("myLogs", "i = " + i);
                View item = ltInflater.inflate(R.layout.item, linLayout, false);
                TextView tvName = (TextView) item.findViewById(R.id.listResult);
                tvName.setText(cursor.getString(exResultIndex));
                TextView tvPosition = (TextView) item.findViewById(R.id.listDate);
                tvPosition.setText("Должность: " + cursor.getString(exResultIndex));
                TextView tvSalary = (TextView) item.findViewById(R.id.listNewWeight);
                tvSalary.setText("Оклад: " + cursor.getString(exResultIndex));
                item.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                item.setBackgroundColor(colors[i % 2]);
                linLayout.addView(item);

                i = i+1;
            }
            while (cursor.moveToNext());
        }
        else
            Log.d("mLog", "0rows");
        cursor.close();
    }

    public void saveVariables(){
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(SAVED_QUANTITY_VALUE, quantityValue.getText().toString());
        editor.putString(SAVED_WEIGHT_VALUE, weightValue.getText().toString());
        editor.putFloat(SAVED_BEST_WEIGHT, bestWeight);  //(SAVED_BEST_WEIGHT, String.valueOf(bestWeight));
        editor.putFloat(SAVED_SUM_OF_WEIGHT, sumOfWeight);
        editor.putFloat(SAVED_BEST_SUM_OF_WEIGHT, bestSumOfWeight);
        editor.commit();
        Toast.makeText(Exercise3.this, "save_text", Toast.LENGTH_SHORT).show();
    }

    public void loadVariables(){
        sPref = getPreferences(MODE_PRIVATE);
        weightValue.setText(sPref.getString(SAVED_WEIGHT_VALUE, ""));
        quantityValue.setText(sPref.getString(SAVED_QUANTITY_VALUE, ""));
        bestWeight = sPref.getFloat(SAVED_BEST_WEIGHT, bestWeight);
        bestSumOfWeight = sPref.getFloat(SAVED_BEST_SUM_OF_WEIGHT, bestSumOfWeight);
        sumOfWeight = sPref.getFloat(SAVED_SUM_OF_WEIGHT, sumOfWeight);
        //}
    }

}
