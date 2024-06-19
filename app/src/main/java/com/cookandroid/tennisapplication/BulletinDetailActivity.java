package com.cookandroid.tennisapplication;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class BulletinDetailActivity extends AppCompatActivity {

    private TextView titleTextView;
    private TextView dateTextView;
    private TextView contentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulletin_detail);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        titleTextView = findViewById(R.id.title_text);
        dateTextView = findViewById(R.id.date_text);
        contentTextView = findViewById(R.id.content_text);

        String title = getIntent().getStringExtra("title");
        String date = getIntent().getStringExtra("date");
        String content = getIntent().getStringExtra("content");

        titleTextView.setText(title);
        dateTextView.setText(date);
        contentTextView.setText(content);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
