package com.example.campingspot;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Calendar;

public class SearchPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_page);
        EditText initial_date = findViewById(R.id.input_initial_date);
        EditText final_date = findViewById(R.id.input_final_date);

        final_date.addTextChangedListener(new TextWatcher() {
            private String current = "";
            final String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void onTextChanged(CharSequence seq, int start, int before, int count) {
                if (!seq.toString().equals(current)) {
                    String clean = seq.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        if(mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon-1);


                        if (day == 00) day = 01;
                        if (mon == 00) mon = 01;

                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    final_date.setText(current);
                    final_date.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}

        });
        initial_date.addTextChangedListener(new TextWatcher() {
            private String current = "";
            final String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void onTextChanged(CharSequence seq, int start, int before, int count) {
                if (!seq.toString().equals(current)) {
                    String clean = seq.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        if(mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon-1);

                        if (day == 00) day = 01;
                        if (mon == 00) mon = 01;

                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    initial_date.setText(current);
                    initial_date.setSelection(sel < current.length() ? sel : current.length());
                }
            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void afterTextChanged(Editable s) {}

        });
    }

    public void clickToSearch(View view) {
        CheckBox checkBox_date = (CheckBox) findViewById(R.id.date_checkbox);
        EditText initial_date = findViewById(R.id.input_initial_date);
        EditText final_date = findViewById(R.id.input_final_date);
        Intent intent = new Intent(this, ResultsPage.class);

        if (checkBox_date.isChecked()) {
            String id = String.valueOf(initial_date.getText());
            String fd = String.valueOf(final_date.getText());

            try {
                int day_id = Integer.parseInt(id.substring(0, 2));
                int mon_id = Integer.parseInt(id.substring(3, 5));
                int year_id = Integer.parseInt(id.substring(6, 10));
                int day_fd = Integer.parseInt(fd.substring(0, 2));
                int mon_fd = Integer.parseInt(fd.substring(3, 5));
                int year_fd = Integer.parseInt(fd.substring(6, 10));

                if (year_id > year_fd) {
                    Toast.makeText(this, "Invalid date!", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    if (mon_id > mon_fd && year_id >= year_fd) {
                        Toast.makeText(this, "Invalid date!", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        if (day_id > day_fd && mon_id >= mon_fd && year_id >= year_fd) {
                            Toast.makeText(this, "Invalid date!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Invalid date format!", Toast.LENGTH_SHORT).show();
                return;
            }
            intent.putExtra("START", id);
            intent.putExtra("FINAL", fd);
        } else {
            intent.putExtra("START", "");
            intent.putExtra("FINAL", "");
        }

        CheckBox checkBox_capacity = (CheckBox) findViewById(R.id.capacity_checkbox);
        if (checkBox_capacity.isChecked()) {
            EditText capacity = findViewById(R.id.input_capacity);
            if (String.valueOf(capacity.getText()).isEmpty()) {
                Toast.makeText(this, "Capacity label is empty!", Toast.LENGTH_SHORT).show();
                return;
            }

            int capacity_value = Integer.parseInt(String.valueOf(capacity.getText()));
            if (capacity_value < 0) {
                Toast.makeText(this, "Invalid capacity!", Toast.LENGTH_SHORT).show();
                return;
            }
            intent.putExtra("CAPACITY", String.valueOf(capacity.getText()));
        } else {
            intent.putExtra("CAPACITY", "");
        }

        CheckBox checkBox_location = (CheckBox) findViewById(R.id.location_checkbox);
        if (checkBox_location.isChecked()) {
            EditText location = findViewById(R.id.input_location);
            if (String.valueOf(location.getText()).replaceAll("\\s+", "").isEmpty()) {
                Toast.makeText(this, "Location label is empty!", Toast.LENGTH_SHORT).show();
                return;
            }
            intent.putExtra("LOCATION", String.valueOf(location.getText()));
        } else {
            intent.putExtra("LOCATION", "");
        }

        CheckBox checkBox_name = (CheckBox) findViewById(R.id.name_checkbox);
        if (checkBox_name.isChecked()) {
            EditText name = findViewById(R.id.input_name);
            if (String.valueOf(name.getText()).replaceAll("\\s+", "").isEmpty()) {
                Toast.makeText(this, "Name label is empty!", Toast.LENGTH_SHORT).show();
                return;
            }
            intent.putExtra("NAME", String.valueOf(name.getText()));
        } else {
            intent.putExtra("NAME", "");
        }

        if (!checkBox_name.isChecked() && !checkBox_location.isChecked() && !checkBox_date.isChecked() && !checkBox_capacity.isChecked()) {
            Toast.makeText(this, "Invalid filter!", Toast.LENGTH_SHORT).show();
            return;
        }
        startActivity(intent);
    }

    public void clickToApplyFilters(View view) {
        CheckBox checkBox_location = (CheckBox) findViewById(R.id.location_checkbox);
        LinearLayout location_layout = (LinearLayout) findViewById(R.id.location_layout);
        if(checkBox_location.isChecked()){
            location_layout.setVisibility(View.VISIBLE);
        } else {
            location_layout.setVisibility(View.GONE);
        }

        CheckBox checkBox_name = (CheckBox) findViewById(R.id.name_checkbox);
        LinearLayout name_layout = (LinearLayout) findViewById(R.id.name_layout);
        if(checkBox_name.isChecked()){
            name_layout.setVisibility(View.VISIBLE);
        } else {
            name_layout.setVisibility(View.GONE);
        }

        CheckBox checkBox_date = (CheckBox) findViewById(R.id.date_checkbox);
        LinearLayout date_layout = (LinearLayout) findViewById(R.id.date_layout);
        if(checkBox_date.isChecked()){
            date_layout.setVisibility(View.VISIBLE);
        } else {
            date_layout.setVisibility(View.GONE);
        }

        CheckBox checkBox_capacity = (CheckBox) findViewById(R.id.capacity_checkbox);
        LinearLayout capacity_layout = (LinearLayout) findViewById(R.id.capacity_layout);
        if(checkBox_capacity.isChecked()){
            capacity_layout.setVisibility(View.VISIBLE);
        } else {
            capacity_layout.setVisibility(View.GONE);
        }
    }

    public void clickToHome(View view) {
        Intent intent = new Intent(getApplicationContext(), FeedPage.class);
        startActivity(intent);
    }
    public void clickToHistoric(View view) {
        Intent intent = new Intent(getApplicationContext(), HistoricPage.class);
        startActivity(intent);
    }

    public void clickToProfile(View view) {
        Intent intent = new Intent(getApplicationContext(), ProfilePage.class);
        startActivity(intent);
    }
}