package com.dmallcott.dismissibleimageview.example;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dmallcott.dismissibleimageview.DismissibleImageView;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DismissibleImageView dismissibleImageView = (DismissibleImageView) findViewById(R.id.activity_main_dismissibleImageView);

        Picasso.with(this).load("http://www.readersdigest.ca/wp-content/uploads/2011/01/4-ways-cheer-up-depressed-cat.jpg").into(dismissibleImageView);
    }
}
