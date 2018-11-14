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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

    private AlertDialog.Builder dlg_delete_all;
    private AlertDialog.Builder dlg_delete_piece;
    private AlertDialog.Builder dlg_delete_choice;
    private AlertDialog.Builder dlg_editing;

    private final String fileName = "data.txt";
    private final String[] items_delete = {"一部削除", "全削除"};

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

        // ダイアログ設定
        dlg_delete_all = new AlertDialog.Builder(this);
        dlg_delete_all.setTitle(R.string.dlg_delete_all_title);
        dlg_delete_all.setMessage(R.string.dlg_delete_all_message);
        dlg_delete_all.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // OK ボタンクリック処理
                        saveNewFile(fileName, null);
                        updateDataFromFile(fileName);
                    }
                });
        dlg_delete_all.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Cancel ボタンクリック処理
                    }
                });

        dlg_delete_piece = new AlertDialog.Builder(MainActivity.this);
        dlg_delete_piece.setTitle(R.string.dlg_delete_piece_title);
        dlg_delete_piece.setMessage(R.string.dlg_delete_piece_message);
        dlg_delete_piece.setView(editView);
        dlg_delete_piece.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // OK ボタンクリック処理

                    }
                });
        dlg_delete_piece.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Cancel ボタンクリック処理
                    }
                });

        dlg_delete_choice = new AlertDialog.Builder(MainActivity.this);
        dlg_delete_choice.setItems(items_delete, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0)          dlg_delete_piece.create().show();
                else if(which == 1)     dlg_delete_all.create().show();
                else;
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

                try{
                    FileInputStream fin = openFileInput(fileName);
                    BufferedReader reader= new BufferedReader(new InputStreamReader(fin, "UTF-8"));
                    String lineBuffer;
                    List<String> listLineBuffer = new ArrayList<String>();
                    while( (lineBuffer = reader.readLine()) != null ) {
                        listLineBuffer.add(lineBuffer);
                    }
                    if(listLineBuffer.size() != 0) {
                        String str = listLineBuffer.get(listLineBuffer.size() - 1);
                        String[] strSplit = str.split(",");
                        String text = strSplit[0] + "," + strSplit[1] + "," + (tmpNumerator + Integer.parseInt(strSplit[2])) + "," + (tmpDenominator + Integer.parseInt(strSplit[3])) + ",";
                        listLineBuffer.set(listLineBuffer.size() - 1, text);
                        saveNewFile(fileName, null);
                        for(int i = 0; i < listLineBuffer.size(); i++){
                            //Log.d(listLineBuffer.get(i),"debug");
                            saveAddFile(fileName, listLineBuffer.get(i) + "\n");
                        }
                    }

                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                updateDataFromFile(fileName);

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
                dlg_delete_choice.create().show();
            }
        });

        today = findViewById(R.id.today);
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int month = cal.get(Calendar.MONTH) + 1;
                int day = cal.get(Calendar.DAY_OF_MONTH);
                String text = month + "," + day + "," + 1 + "," + 2 + ",\n";
                saveAddFile(fileName, text);
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

    // ファイルを追加保存
    private void saveAddFile(String file, String str) {
        try{
            FileOutputStream fout = openFileOutput(file, Context.MODE_PRIVATE|MODE_APPEND);
            if(str != null) fout.write(str.getBytes());
            fout.flush();
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ファイルを新規保存
    private void saveNewFile(String file, String str) {
        try{
            FileOutputStream fout = openFileOutput(file, Context.MODE_PRIVATE);
            if(str != null) fout.write(str.getBytes());
            fout.flush();
            fout.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // ファイルを読み込んで出力
    private void updateDataFromFile(String file) {
        String text = "";
        int calcNumerator = 0;
        int calcDenominator = 0;

        try{
            FileInputStream fin = openFileInput(file);
            BufferedReader reader= new BufferedReader(new InputStreamReader(fin, "UTF-8"));
            String lineBuffer;
            while( (lineBuffer = reader.readLine()) != null ) {
                text = getResources().getString(R.string.bar) + text;
                // lineBufferから表示用文字列を生成
                String[] split = lineBuffer.split(",", 0);
                text = split[0] + "月" + split[1] + "日：" + split[2] + "/" + split[3] + text;

                // 分子と分母を計算
                calcNumerator += Integer.parseInt(split[2]);
                calcDenominator += Integer.parseInt(split[3]);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        sumNumerator = calcNumerator;
        sumDenominator = calcDenominator;
        scrollTextView.setText(text);
        displaySum();
    }



}
