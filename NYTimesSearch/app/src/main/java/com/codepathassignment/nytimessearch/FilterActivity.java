package com.codepathassignment.nytimessearch;


import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FilterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    EditText etBeginDate;
    Spinner spSortOrder;
    CheckBox cbArts;
    CheckBox cbFashions;
    CheckBox cbSports;
    static SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        prefs=this.getSharedPreferences("store", Context.MODE_PRIVATE);

        etBeginDate = (EditText)findViewById(R.id.etBeginDate);
        spSortOrder = (Spinner)findViewById(R.id.spSortOrder);

        cbArts = ( CheckBox)findViewById(R.id.cbArts);
        cbFashions = ( CheckBox)findViewById(R.id.cbFashion);
        cbSports = ( CheckBox)findViewById(R.id.cbSports);

        etBeginDate.setText(prefs.getString("beginDate","1970-01-01"));

        if (prefs.getString("sortOrder","oldest").equals("oldest")){
            spSortOrder.setSelection(0);
        }else{
            spSortOrder.setSelection(1);
        }


        cbArts.setChecked(prefs.getBoolean("bArts",false));
        cbFashions.setChecked(prefs.getBoolean("bFashion",false));
        cbSports.setChecked(prefs.getBoolean("bSports",false));



        CompoundButton.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                switch (compoundButton.getId()){
                    case R.id.cbArts:

                        prefs.edit().putBoolean("bArts",checked).commit();
                        break;
                    case R.id.cbFashion:

                        prefs.edit().putBoolean("bFashion",checked).commit();
                        break;
                    case R.id.cbSports:

                        prefs.edit().putBoolean("bSports",checked).commit();
                        break;
                }
            }
        };

        cbArts.setOnCheckedChangeListener(checkListener);
        cbFashions.setOnCheckedChangeListener(checkListener);
        cbSports.setOnCheckedChangeListener(checkListener);
    }

    public void showDataPickerDialog(View v){
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(),"datePicker");

    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
        final Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR,year);
        c.set(Calendar.MONTH,monthOfYear);
        c.set(Calendar.DAY_OF_MONTH,dayOfMonth);

        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
        etBeginDate.setText(format1.format(c.getTime()));
    }


    public void save(View view) {
        //if invalid date, using current date.
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        String beginDate = etBeginDate.getText().toString();
        try {
            cal.setTime(sdf.parse(beginDate));
        }catch(java.text.ParseException e){
            e.printStackTrace();
            try {

                cal.setTime(sdf.parse("1970-01-01"));
            } catch (ParseException e1) {
                e1.printStackTrace();
            }


        }

        prefs.edit().putString("beginDate",etBeginDate.getText().toString()).commit();
        prefs.edit().putString("sortOrder",spSortOrder.getSelectedItem().toString().toLowerCase()).commit();
        prefs.edit().putBoolean("bArts",cbArts.isChecked()).commit();
        prefs.edit().putBoolean("bFashion",cbFashions.isChecked()).commit();
        prefs.edit().putBoolean("bSports",cbSports.isChecked()).commit();

        finish();
    }
}
