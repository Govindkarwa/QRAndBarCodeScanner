package com.example.qrandbarcodescanner;

import static android.Manifest.permission.CAMERA;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

public class ScannerActivity extends AppCompatActivity {
    CodeScanner myCodeScanner;
    CodeScannerView myCodeScannerView;
    TextView dataView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);
        if(checkPermission()){
            Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
        }else {
            requestPermission();
        }
        dataView=findViewById(R.id.data_tv);
        myCodeScannerView=findViewById(R.id.scanner_view);
        myScanner();

    }
    public void myScanner(){
        String[] val = new String[1];
        myCodeScanner=new CodeScanner(this,myCodeScannerView);

        myCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {
                dataView.setText(result.toString());
                val[0] =result.toString();
                Intent i = new Intent(android.content.Intent.ACTION_VIEW,Uri.parse(result.toString()));
                startActivity(i);
            }
        });
//        dataView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(android.content.Intent.ACTION_VIEW,Uri.parse(val[0]));
//                startActivity(i);
//            }
//        });
        myCodeScannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCodeScanner.startPreview();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        myCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        myCodeScanner.releaseResources();
        super.onPause();
    }
    public boolean checkPermission(){
        int CameraPermission= ContextCompat.checkSelfPermission(getApplicationContext(),CAMERA);
        return CameraPermission == PackageManager.PERMISSION_GRANTED;
    }
    private  void requestPermission(){
        int PERMISSION_ALL=100;
        ActivityCompat.requestPermissions(this,new String[]{CAMERA},PERMISSION_ALL);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults.length>0){
            boolean cameraPermission= grantResults[0]==PackageManager.PERMISSION_GRANTED;
            if(cameraPermission){
                Toast.makeText(this,"Permission Granted",Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(this,"Permission Denied \n You Can Not Use App Without Permission ",Toast.LENGTH_SHORT).show();
            }
        }
    }
}