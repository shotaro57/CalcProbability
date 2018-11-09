package com.example.user.calcprobability;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private int tmpNumerator = 0;
    private int tmpDenominator = 0;
    private int sumNumerator = 0;
    private int sumDenominator = 0;

    private TextView sumProbability;
    private TextView tmpProbability;
    private TextView scrollTextView;

    private Button buttonNumeratorAdd;
    private Button buttonDenominatorAdd;
    private Button buttonNumeratorSub;
    private Button buttonDenominatorSub;
    private Button update;
    private Button delete;

    private AlertDialog.Builder dlg;

    private String fileName = "data.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 縦画面
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sumProbability = (TextView)findViewById(R.id.sumProbability);
        tmpProbability = (TextView)findViewById(R.id.tmpProbability);
        scrollTextView = (TextView)findViewById(R.id.scrollTextView);

        displaySum();
        displayUI();

        saveFile(fileName, "hello world\n");
        updateDataFromFile(fileName);
        saveFile(fileName, "hello world\n");
        updateDataFromFile(fileName);

        dlg = new AlertDialog.Builder(this);
        dlg.setTitle(R.string.dlg_title);
        dlg.setMessage(R.string.dlg_message);
        dlg.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // OK ボタンクリック処理
                        deleteFile(fileName);
                        updateDataFromFile(fileName);
                    }
                });
        dlg.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Cancel ボタンクリック処理
                    }
                });


        // ボタンを設定
        buttonNumeratorAdd = findViewById(R.id.buttonNumeratorAdd);
        buttonNumeratorAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tmpNumerator++;
                displayUI();
            }
        });

        buttonDenominatorAdd = findViewById(R.id.buttonDenominatorAdd);
        buttonDenominatorAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tmpDenominator++;
                displayUI();
            }
        });

        buttonNumeratorSub = findViewById(R.id.buttonNumeratorSub);
        buttonNumeratorSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tmpNumerator--;
                displayUI();
            }
        });

        buttonDenominatorSub = findViewById(R.id.buttonDenominatorSub);
        buttonDenominatorSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tmpDenominator--;
                displayUI();
            }
        });

        update = findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sumNumerator += tmpNumerator;
                sumDenominator += tmpDenominator;
                displaySum();
            }
        });

        delete = findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg.create().show();
            }
        });

    }




    private void displayUI(){
        tmpProbability.setText(tmpNumerator + "/" + tmpDenominator);
    }

    private void displaySum(){
        double probability;

        if(sumDenominator == 0){
            probability = 0.0;
        }else{
            probability = (double)sumNumerator/(double)sumDenominator*100.0;
        }
        sumProbability.setText("合計:" + sumNumerator + "/" + sumDenominator + "  " + String.format("%.2f", probability) + "%");
    }

    // ファイルを保存
    private void saveFile(String file, String str) {

        try{
            FileOutputStream fout = openFileOutput(file, Context.MODE_PRIVATE|MODE_APPEND);
            fout.write(str.getBytes());
            fout.flush();
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ファイルを読み出し
    private void updateDataFromFile(String file) {
        String text = "";

        try{
            FileInputStream fin = openFileInput(file);
            BufferedReader reader= new BufferedReader(new InputStreamReader(fin, "UTF-8"));
            String lineBuffer;
            while( (lineBuffer = reader.readLine()) != null ) {
                text = text.concat(lineBuffer) ;
                text = text.concat(getResources().getString(R.string.bar)) ;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        scrollTextView.setText(text);
    }

}
