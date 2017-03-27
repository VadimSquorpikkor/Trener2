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

public class Exercise4 extends Activity {

    //region VARIABLES AND OBJECTS
    //region WIDGETS
    ImageButton plusWeightValueButton;
    ImageButton minusWeightValueButton;
    ImageButton plusQuantityValueButton;
    ImageButton minusQuantityValueButton;
    ImageButton addToResultButton;
    ImageButton addToHistoryButton;
    TextView weightValue;
    TextView quantityValue;
    TextView exerciseResult;
    TextView quantityLeftText;
    TextView dateText;
    TextView newRecordText;
    TextView newWeightText;
    //endregion

    //region VARIABLES
    public float historyBestWeight;
    public int historyBestSumOfQuantity;
    public float dayBestWeight;
    public int dayBestSumOfQuantity;

    boolean historyRecord;
    boolean newWeight;
    String date = "07.09.2016";/////String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    int[] colors = new int[2];
    int quantityLeftToNewRecord;

    //endregion

    //region SHARED PREFERENSES
    SharedPreferences sPref;
    final String SAVED_WEIGHT_VALUE = "saved_weight_value";
    final String SAVED_QUANTITY_VALUE = "saved_quantity_value";
    final String SAVED_BEST_WEIGHT = "saved_best_weight";
    final String SAVED_SUM_OF_QUANTITY = "saved_sum_of_quantity";

    DBHelper dbHelper;

    //endregion
    //endregion

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise4);

        plusWeightValueButton = (ImageButton)findViewById(R.id.plusWeightValueButton);//НОВАЯ ТЕМА!!!
        minusWeightValueButton = (ImageButton) findViewById(R.id.minusWeightValueButton);
        plusQuantityValueButton = (ImageButton) findViewById(R.id.plusQuantityValueButton);
        minusQuantityValueButton = (ImageButton) findViewById(R.id.minusQuantityValueButton);
        addToResultButton = (ImageButton) findViewById(R.id.addToResultButton);
        addToHistoryButton = (ImageButton) findViewById(R.id.addToHistoryButton);
        weightValue = (TextView) findViewById(R.id.weightValue);
        quantityValue = (TextView) findViewById(R.id.quantityValue);
        exerciseResult = (TextView) findViewById(R.id.exerciseResult);
        quantityLeftText = (TextView) findViewById(R.id.quantityLeftText);
        dateText = (TextView) findViewById(R.id.dateText);
        newRecordText = (TextView) findViewById(R.id.newRecordText);
        newWeightText = (TextView) findViewById(R.id.newWeightText);

        boolean historyRecord = false;
        boolean newWeight = false;
        //dateText.setText(date);
        dayBestWeight = 0;
        dayBestSumOfQuantity = 0;

        dbHelper = new DBHelper(this);

        //region CLICK LISTENER/SWITCH CASE

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
                        createLayoutBD_item();
                        //readFromDB();
                        exerciseResult.setText("");
                        break;
                }
            }
        };

        plusWeightValueButton.setOnClickListener(onClickListener);
        minusWeightValueButton.setOnClickListener(onClickListener);
        plusQuantityValueButton.setOnClickListener(onClickListener);
        minusQuantityValueButton.setOnClickListener(onClickListener);
        addToResultButton.setOnClickListener(onClickListener);
        addToHistoryButton.setOnClickListener(onClickListener);

        //endregion

        loadVariables();
        readFromDB();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveVariables();
    }

    //region METHODS FOR PLUS, MINUS, ADD TO RESULT

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
        int quantityValueInteger = Integer.parseInt(quantityValue.getText().toString());

        if(weightValueFloat>dayBestWeight){
            dayBestWeight = weightValueFloat;
            dayBestSumOfQuantity = quantityValueInteger;
        }
        else if(weightValueFloat==dayBestWeight){
            dayBestSumOfQuantity = dayBestSumOfQuantity + quantityValueInteger;
        }

        Log.d("HIS BEST SUMM QUANT", String.valueOf(historyBestSumOfQuantity));
        Log.d("DAY BEST SUMM QUANT", String.valueOf(dayBestSumOfQuantity));

        quantityLeftToNewRecord = historyBestSumOfQuantity + 1 - dayBestSumOfQuantity;
        Log.d("QUAN LEFT TO NEW", String.valueOf(quantityLeftToNewRecord));
//        quantityLeftText.setText(String.valueOf(quantityLeftToNewRecord));
        //Log.d("QUANTITY LEFT RECORD" , "quantityLeftText");


        if(dayBestWeight>historyBestWeight){
            historyBestWeight = dayBestWeight;
            saveVariables();
            newWeight = true;
            newWeightText.setText("Новый вес!");
            historyRecord = true;
            Log.d("NEW RECORD!!!!", newRecordText.getText().toString());
            newRecordText.setText("Новый рекорд!");
        }
        if(dayBestSumOfQuantity>historyBestSumOfQuantity){
            historyBestSumOfQuantity = dayBestSumOfQuantity;
            saveVariables();
            historyRecord = true;
            newRecordText.setText("Новый рекорд!");
        }

        exerciseResult.setText(exerciseResult.getText().toString()
                +weightValue.getText().toString()+"-"
                +quantityValue.getText().toString()+", ");

        Log.d("QUANTITY LFT TXT BEFORE", quantityLeftText.getText().toString());

        quantityLeftText.setText(String.valueOf(quantityLeftToNewRecord));
        dateText.setText(String.valueOf(date));
        //newWeightText.setText("NewWeight");
        //newRecordText.setText("NewRecord");
    }

    //endregion

    public void  addToDB(){
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelper.KEY_RESULT, exerciseResult.getText().toString());//добавить результат
        contentValues.put(DBHelper.KEY_HISTORY_RECORD_COUNT, historyRecord);
        contentValues.put(DBHelper.KEY_NEW_WEIGHT_COUNT, newWeight);
        contentValues.put(DBHelper.KEY_DATE, date);

        Log.d("LLLLLLOOOOG_ADD", exerciseResult.getText().toString());
        database.insert(DBHelper.TABLE_EXERCISE1, null, contentValues);

        Toast.makeText(Exercise4.this, "Запись добавлена в БД", Toast.LENGTH_SHORT).show();
    }

    public void readFromDB(){
        colors[0] = Color.parseColor("#559966CC");
        colors[1] = Color.parseColor("#55336699");

        int i = 0;
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        Cursor cursor = database.query(DBHelper.TABLE_EXERCISE1, null, null, null, null, null, null);
        if(cursor.moveToFirst()){
            //int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            //int exResultIndex = cursor.getColumnIndex(DBHelper.KEY_RESULT);
            do{
                Log.d("LLLLOOOGGGG_READ", cursor.getString(cursor.getColumnIndex(DBHelper.KEY_RESULT)));
                Log.d("myLogs", "i = " + i);

                LinearLayout linLayout = (LinearLayout) findViewById(R.id.linLayout);
                LayoutInflater ltInflater = getLayoutInflater();

                View bd_item = ltInflater.inflate(R.layout.bd_item, linLayout, false);

                TextView listResult = (TextView) bd_item.findViewById(R.id.bd_listResult);
                //TextView listHistoryRecord = (TextView) bd_item.findViewById(R.id.listHistoryRecord);
                //TextView listNewWeight = (TextView) bd_item.findViewById(R.id.listNewWeight);
                TextView listDate = (TextView) bd_item.findViewById(R.id.bd_listDate);

                listResult.setText(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_RESULT)));
                //listHistoryRecord.setText(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_HISTORY_RECORD_COUNT)));
                //listNewWeight.setText(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_NEW_WEIGHT_COUNT)));
                listDate.setText(cursor.getString(cursor.getColumnIndex(DBHelper.KEY_DATE)));

                bd_item.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
                bd_item.setBackgroundColor(colors[i % 2]);
                linLayout.addView(bd_item);

                i = i+1;
            }
            while (cursor.moveToNext());
        }
        else
            Log.d("mLog", "0rows");
        cursor.close();
    }

    public void createLayoutBD_item(){
        LinearLayout linLayout = (LinearLayout) findViewById(R.id.linLayout);
        LayoutInflater ltInflater = getLayoutInflater();

        View bd_item = ltInflater.inflate(R.layout.bd_item, linLayout, false);

        TextView listResult = (TextView) bd_item.findViewById(R.id.listResult);
        listResult.setText(exerciseResult.getText().toString());

        //TextView listHistoryRecord = (TextView) bd_item.findViewById(R.id.listHistoryRecord);
        //listHistoryRecord.setText(exerciseResult.getText().toString());

        TextView listDate = (TextView) bd_item.findViewById(R.id.listDate);
        listDate.setText(date);

        bd_item.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        bd_item.setBackgroundColor(colors[1]);
        linLayout.addView(bd_item);
    }

    //region METHODS FOR SAVE/LOAD VARIABLES

    public void saveVariables(){
        sPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sPref.edit();
        editor.putString(SAVED_WEIGHT_VALUE, weightValue.getText().toString());
        editor.putString(SAVED_QUANTITY_VALUE, quantityValue.getText().toString());
        editor.putFloat(SAVED_BEST_WEIGHT, historyBestWeight);  //(SAVED_BEST_WEIGHT, String.valueOf(bestWeight));
        editor.putInt(SAVED_SUM_OF_QUANTITY, historyBestSumOfQuantity);
        editor.commit();
        Toast.makeText(Exercise4.this, "Сохранено", Toast.LENGTH_SHORT).show();
    }

    public void loadVariables(){
        sPref = getPreferences(MODE_PRIVATE);
        weightValue.setText(sPref.getString(SAVED_WEIGHT_VALUE, "0"));
        quantityValue.setText(sPref.getString(SAVED_QUANTITY_VALUE, "0"));
        historyBestWeight = sPref.getFloat(SAVED_BEST_WEIGHT, historyBestWeight);
        historyBestSumOfQuantity = sPref.getInt(SAVED_SUM_OF_QUANTITY, historyBestSumOfQuantity);
    }

    //endregion
}
