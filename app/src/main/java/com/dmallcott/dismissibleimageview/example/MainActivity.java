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
        dismissibleImageView.setFinalUrl("https://images.pexels.com/photos/96938/pexels-photo-96938.jpeg?w=1260&h=750&dpr=2&auto=compress&cs=tinysrgb");

        Picasso.with(this).load("https://images.pexels.com/photos/96938/pexels-photo-96938.jpeg?w=640&h=393&dpr=2&auto=compress&cs=tinysrgb").into(dismissibleImageView);
    }
}
