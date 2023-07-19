package com.example.ledrgb_ocr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;
import java.util.List;
import java.util.Objects;
public class manualActivity extends AppCompatActivity{
    Button RED,BLUE,GREEN,CYAN,YELLOW,PURPLE,OFF,OCR,BLINK;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myref = database.getReference("LED");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);
        RED = findViewById(R.id.red);
        BLUE = findViewById(R.id.blue);
        GREEN = findViewById(R.id.green);
        CYAN = findViewById(R.id.cyan);
        YELLOW = findViewById(R.id.yellow);
        PURPLE = findViewById(R.id.purple);
        OFF = findViewById(R.id.off);
        BLINK = findViewById(R.id.blink);
        OCR = findViewById(R.id.ocr);
        OCR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(manualActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        RED.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myref.setValue("000");
            }
        });
        BLUE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myref.setValue("001");
            }
        });
        GREEN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myref.setValue("010");
            }
        });
        YELLOW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myref.setValue("011");
            }
        });
        CYAN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myref.setValue("100");
            }
        });
        PURPLE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myref.setValue("101");
            }
        });
        BLINK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myref.setValue("0000");
            }
        });
        OFF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myref.setValue("111");
            }
        });

    }
}