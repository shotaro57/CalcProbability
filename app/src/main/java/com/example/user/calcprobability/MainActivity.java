package com.example.user.calcprobability;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Vibrator;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
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
    private EditText editView_delete;
    private EditText editView_editing;

    private Button buttonNumeratorAdd;
    private Button buttonDenominatorAdd;
    private Button buttonNumeratorSub;
    private Button buttonDenominatorSub;
    private Button update;
    private Button editing;
    private Button delete;
    private Button today;
    private Button graph;

    private AlertDialog.Builder dlg_delete_all;
    private AlertDialog.Builder dlg_delete_piece;
    private AlertDialog.Builder dlg_delete_choice;
    private AlertDialog.Builder dlg_editing;
    private AlertDialog.Builder dlg_graph;

    private  AlertDialog dlg_delete_all_create;
    private  AlertDialog dlg_delete_piece_create;
    private  AlertDialog dlg_delete_choice_create;
    private  AlertDialog dlg_editing_create;
    private  AlertDialog dlg_graph_create;

    private final String fileName = "data.txt";
    private final String[] items_delete = {"一部削除", "全削除"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 縦画面
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //saveNewFile(fileName, null);

        sumProbability = (TextView)findViewById(R.id.sumProbability);
        tmpProbability = (TextView)findViewById(R.id.tmpProbability);
        scrollTextView = (TextView)findViewById(R.id.scrollTextView);
        editView_delete = new EditText(MainActivity.this);
        editView_editing = new EditText(MainActivity.this);

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
        dlg_delete_all_create = dlg_delete_all.create();

        dlg_delete_piece = new AlertDialog.Builder(MainActivity.this);
        dlg_delete_piece.setTitle(R.string.dlg_delete_piece_title);
        dlg_delete_piece.setMessage(R.string.dlg_delete_piece_message);
        dlg_delete_piece.setView(editView_delete);
        dlg_delete_piece.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // OK ボタンクリック処理
                        String str = editView_delete.getText().toString();
                        String[] strSplit = str.split(",");
                        if( strSplit.length != 2 ){
                            editView_delete.getEditableText().clear();
                            return;
                        }
                        if( ! isInteger(strSplit[0]) || ! isInteger(strSplit[1]) ){
                            editView_delete.getEditableText().clear();
                            return;
                        }
                        try{
                            FileInputStream fin = openFileInput(fileName);
                            BufferedReader reader= new BufferedReader(new InputStreamReader(fin, "UTF-8"));
                            String lineBuffer;
                            List<String> listLineBuffer = new ArrayList<String>();
                            while( (lineBuffer = reader.readLine()) != null ) {
                                String[] lineBufferSplit = lineBuffer.split(",");
                                if(     Integer.parseInt(lineBufferSplit[0]) == Integer.parseInt(strSplit[0]) &&
                                        Integer.parseInt(lineBufferSplit[1]) == Integer.parseInt(strSplit[1])   )
                                            continue;
                                listLineBuffer.add(lineBuffer);
                            }
                            saveNewFile(fileName, null);
                            for(int i = 0; i < listLineBuffer.size(); i++){
                                saveAddFile(fileName, listLineBuffer.get(i) + "\n");
                            }

                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        updateDataFromFile(fileName);
                        editView_delete.getEditableText().clear();
                    }
                });
        dlg_delete_piece.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Cancel ボタンクリック処理
                        editView_delete.getEditableText().clear();
                    }
                });
        dlg_delete_piece_create = dlg_delete_piece.create();

        dlg_delete_choice = new AlertDialog.Builder(MainActivity.this);
        dlg_delete_choice.setItems(items_delete, new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0)          dlg_delete_piece_create.show();
                else if(which == 1)     dlg_delete_all_create.show();
                else;
            }
        });
        dlg_delete_choice_create = dlg_delete_choice.create();

        dlg_editing = new AlertDialog.Builder(MainActivity.this);
        dlg_editing.setTitle(R.string.dlg_editing_title);
        dlg_editing.setMessage(R.string.dlg_editing_message);
        dlg_editing.setView(editView_editing);
        dlg_editing.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // OK ボタンクリック処理
                        boolean flag = true;
                        String str = editView_editing.getText().toString();
                        String[] strSplit = str.split(",");
                        if( strSplit.length != 4 ){
                            editView_editing.getEditableText().clear();
                            return;
                        }
                        if(     ! isInteger(strSplit[0]) || ! isInteger(strSplit[1]) ||
                                ! isInteger(strSplit[2]) || ! isInteger(strSplit[3])    ){
                                    editView_editing.getEditableText().clear();
                                    return;
                        }

                        try{
                            FileInputStream fin = openFileInput(fileName);
                            BufferedReader reader= new BufferedReader(new InputStreamReader(fin, "UTF-8"));
                            String lineBuffer;
                            List<String> listLineBuffer = new ArrayList<String>();
                            while( (lineBuffer = reader.readLine()) != null ) {
                                String[] lineBufferSplit = lineBuffer.split(",");
                                if(     Integer.parseInt(lineBufferSplit[0]) == Integer.parseInt(strSplit[0]) &&
                                        Integer.parseInt(lineBufferSplit[1]) == Integer.parseInt(strSplit[1])   ){
                                            listLineBuffer.add(strSplit[0] + "," + strSplit[1] + "," + strSplit[2] + "," + strSplit[3] + ",");
                                            flag = false;
                                }else{
                                            listLineBuffer.add(lineBuffer);
                                }
                            }
                            if( flag ) listLineBuffer.add(strSplit[0] + "," + strSplit[1] + "," + strSplit[2] + "," + strSplit[3] + ",");

                            saveNewFile(fileName, null);
                            for(int i = 0; i < listLineBuffer.size(); i++){
                                saveAddFile(fileName, listLineBuffer.get(i) + "\n");
                            }

                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        updateDataFromFile(fileName);
                        editView_editing.getEditableText().clear();
                    }
                });
        dlg_editing.setNegativeButton(
                "Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Cancel ボタンクリック処理
                        editView_editing.getEditableText().clear();
                    }
                });
        dlg_editing_create = dlg_editing.create();

        dlg_graph = new AlertDialog.Builder(this);
        dlg_graph.setMessage(R.string.dlg_graph_message);
        dlg_graph_create = dlg_graph.create();


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
        update.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                tmpProbability.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.tmp_probability_style2, null));
                tmpProbability.setTextColor(getResources().getColor(R.color.tmpProbabilityColor2));
                return false;
            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tmpProbability.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.tmp_probability_style, null));
                tmpProbability.setTextColor(getResources().getColor(R.color.tmpProbabilityColor));

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
                            saveAddFile(fileName, listLineBuffer.get(i) + "\n");
                        }
                    }

                    tmpNumerator = 0;
                    tmpDenominator = 0;
                    displayUI();

                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                updateDataFromFile(fileName);
                ((Vibrator) getSystemService(Context.VIBRATOR_SERVICE)).vibrate(50);
            }
        });

        editing = findViewById(R.id.editing);
        editing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg_editing_create.show();
            }
        });

        delete = findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dlg_delete_choice_create.show();
            }
        });

        today = findViewById(R.id.today);
        today.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int month = cal.get(Calendar.MONTH) + 1;
                int day = cal.get(Calendar.DAY_OF_MONTH);
                if( checkData(fileName, month + "," + day + ",") ) return;

                String text = month + "," + day + "," + 0 + "," + 0 + ",\n";
                saveAddFile(fileName, text);
                updateDataFromFile(fileName);
            }
        });

        graph = findViewById(R.id.graph);
        graph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( checkOutputGraph(fileName) ){
                    Intent intent = new Intent(getApplication(), GraphActivity.class);
                    intent.putExtra("file_name", fileName);
                    startActivity(intent);
                }else{
                    dlg_graph_create.show();
                }
            }
        });

    }




    private void displayUI(){
        tmpProbability.setText(tmpNumerator + "/" + tmpDenominator);
    }

    private void displaySum(){
        sumProbability.setText("合計:" + sumNumerator + "/" + sumDenominator + "  " + String.format("%.2f", calcProbability(sumNumerator, sumDenominator)) + "%");
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
                // lineBufferから表示用文字列を生成
                String[] split = lineBuffer.split(",", 0);
                text = getResources().getString(R.string.bar) + String.format("%.2f", calcProbability(Integer.parseInt(split[2]), Integer.parseInt(split[3]))) + "%\n" + text;
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

    // データがすでに存在するか確認
    private boolean checkData(String file, String date){
        String[] dateSplit = date.split(",");

        try{
            FileInputStream fin = openFileInput(file);
            BufferedReader reader= new BufferedReader(new InputStreamReader(fin, "UTF-8"));
            String lineBuffer;
            while( (lineBuffer = reader.readLine()) != null ) {
                String[] lineBufferSplit = lineBuffer.split(",");
                if(     Integer.parseInt(lineBufferSplit[0]) == Integer.parseInt(dateSplit[0]) &&
                        Integer.parseInt(lineBufferSplit[1]) == Integer.parseInt(dateSplit[1])  )
                            return true;
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    // 文字列がint型に変換できるか確認
    private boolean isInteger(String str){
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // 確率を計算する
    private double calcProbability(double numerator, double denominator){
        if(denominator == 0.0){
            return 0.0;
        }else{
            return numerator/denominator*100.0;
        }
    }

    // グラフが表示できるかチェック（データが2個以上）
    private boolean checkOutputGraph(String file){
        int dataCount = 0;
        boolean flag = false;

        try{
            FileInputStream fin = openFileInput(file);
            BufferedReader reader= new BufferedReader(new InputStreamReader(fin, "UTF-8"));
            String lineBuffer;
            while( (lineBuffer = reader.readLine()) != null ) {
                dataCount++;
                String[] split = lineBuffer.split(",");
                if( Integer.parseInt(split[3]) != 0 ) flag = true;
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if( dataCount >= 2 && flag )    return true;
        else                            return false;

    }


}
