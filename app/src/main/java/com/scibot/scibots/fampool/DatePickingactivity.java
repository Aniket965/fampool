package com.scibot.scibots.fampool;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

public class DatePickingactivity extends AppCompatActivity {
    DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_pickingactivity);
        datePicker = (DatePicker) findViewById(R.id.datePicker2);


    }

    public void next(View view) {
        Intent intent = new Intent(DatePickingactivity.this,MainActivity.class);
        Bundle b = new Bundle();
        b.putInt("day",datePicker.getDayOfMonth());
        b.putInt("month" ,datePicker.getMonth());
        intent.putExtras(b);
        startActivity(intent);
    }
}
