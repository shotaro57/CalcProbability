package com.example.user.calcprobability;

import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private int tmpNumerator = 0;
    private int tmpDenominator = 0;
    private int sumNumerator = 0;
    private int sumDenominator = 0;

    private TextView sumProbability;
    private TextView tmpProbability;
    private TextView scrollTextView;
    private EditText editView;

    private Button buttonNumeratorAdd;
    private Button buttonDenominatorAdd;
    private Button buttonNumeratorSub;
    private Button buttonDenominatorSub;
    private Button update;
    private Button editing;
    private Button delete;
    private Button today;

    private AlertDialog.Builder dlg_delete;
    private AlertDialog.Builder dlg_editing;

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
        editView = new EditText(MainActivity.this);

        displaySum();
        displayUI();
        updateDataFromFile(fileName);

        dlg_delete = new AlertDialog.Builder(this);
        dlg_delete.setTitle(R.string.dlg_delete_title);
        dlg_delete.setMessage(R.string.dlg_delete_message);
        dlg_delete.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // OK ボタンクリック処理
                        deleteFile(fileName);
                        updateDataFromFile(fileName);
                    }
                });
        dlg_delete.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Cancel ボタンクリック処理
                    }
                });

        dlg_editing = new AlertDialog.Builder(MainActivity.this);
        dlg_editing.setTitle(R.string.dlg_editing_title);
        dlg_editing.setMessage(R.string.dlg_editing_message);
        dlg_editing.setView(editView);
        dlg_editing.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // OK ボタンクリック処理

                    }
                });
        dlg_editing.setNegativeButton(
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
                //sumNumerator += tmpNumerator;
                //sumDenominator += tmpDenominator;
/*
                try{
                    FileInputStream fin = openFileInput(fileName);
                    FileOutputStream fout = openFileOutput(fileName, Context.MODE_PRIVATE|MODE_APPEND);
                    BufferedReader reader= new BufferedReader(new InputStreamReader(fin, "UTF-8"));
                    String lineBuffer;
                    while( (lineBuffer = reader.readLine()) != null ) {

                    }
                    int newNumerator = tmpNumerator + getNumeratorOfFile(lineBuffer);
                    int newDenominator = tmpDenominator + getDenominatorOfFile(lineBuffer);
                    String newText = getDayOfFile(lineBuffer) + newNumerator + "," + newDenominator;
                    fout.write(newText.getBytes());
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                updateDataFromFile(fileName);*/
            }
        });

        editing = findViewById(R.id.editing);
        editing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg_editing.create().show();
            }
        });

        delete = findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg_delete.create().show();
            }
        });

        today = findViewById(R.id.today);
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int month = cal.get(Calendar.MONTH) + 1;
                int day = cal.get(Calendar.DAY_OF_MONTH);
                String text = month + "," + day + "," + 0 + "," + 0 + "\n";
                saveFile(fileName, text);
                updateDataFromFile(fileName);
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
        String sumNumeratorString = "";
        String sumDenominatorString = "";
        String[] template = {"月", "日  ", "/"};

        try{
            FileInputStream fin = openFileInput(file);
            BufferedReader reader= new BufferedReader(new InputStreamReader(fin, "UTF-8"));
            String lineBuffer;
            while( (lineBuffer = reader.readLine()) != null ) {
                text = getResources().getString(R.string.bar) + text;
                // lineBufferから表示用文字列を生成
                for(int i = 0; i < 3; i++){
                    lineBuffer = lineBuffer.replaceFirst(",", template[i]);
                }
                text = lineBuffer + text;
                // lineBufferから分母と分子を計算
                //sumNumerator += getNumeratorOfFile(lineBuffer);
                //sumDenominator += getDenominatorOfFile(lineBuffer);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        scrollTextView.setText(text);
        displaySum();
    }

    // lineBufferから分子の値を返す。
    private int getNumeratorOfFile(String lineBuffer){
        String sumNumeratorString = "";

        for(int i = 0, index = 0; i < lineBuffer.length(); i++){
            if( lineBuffer.charAt(i) == ',' ){
                index++;
                continue;
            }
            if( index == 3 ){
                sumNumeratorString = sumNumeratorString.concat( String.valueOf(lineBuffer.charAt(i)) );
            }
        }
        Log.d(sumNumeratorString,"debug");
        return Integer.parseInt(sumNumeratorString);
    }

    // lineBufferから分母の値を返す。
    private int getDenominatorOfFile(String lineBuffer){
        String sumDenominatorString = "";

        for(int i = 0, index = 0; i < lineBuffer.length(); i++){
            if( lineBuffer.charAt(i) == ',' ) index++;
            if(index == 4){
                if( lineBuffer.charAt(i) == ','|| lineBuffer.charAt(i) == '\n' ) continue;
                sumDenominatorString = sumDenominatorString.concat( String.valueOf(lineBuffer.charAt(i)) );
            }
        }
        return Integer.parseInt(sumDenominatorString);
    }

    // lineBufferから日付を返す。
    private String getDayOfFile(final String lineBuffer){
        String day = "";

        for(int i = 0, index = 0; i < lineBuffer.length(); i++){
            if( lineBuffer.charAt(i) == ',' ) index++;
            if(index < 3){
                day = day.concat( String.valueOf(lineBuffer.charAt(i)) );
            }
        }
        return day;
    }

}
