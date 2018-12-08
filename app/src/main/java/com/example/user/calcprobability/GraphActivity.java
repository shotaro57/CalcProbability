package com.example.user.calcprobability;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class GraphActivity extends AppCompatActivity {

    private LineChart lineChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        setTitle(R.string.graph_title);

        // グラフ初期化
        initChart();

        // インテントを取得
        Intent intent = getIntent();
        // インテントに保存されたデータを取得
        String fileName = intent.getStringExtra("file_name");

        try{
            FileInputStream fin = openFileInput(fileName);
            BufferedReader reader= new BufferedReader(new InputStreamReader(fin, "UTF-8"));
            String lineBuffer;
            List<String> listLineBuffer = new ArrayList<String>();
            while( (lineBuffer = reader.readLine()) != null ) {
                listLineBuffer.add(lineBuffer);
            }

            ArrayList<Entry> myDatas = new ArrayList<>();
            float sumNumerator = 0.0f;
            float sumDenominator = 0.0f;
            for(int i = 0; i < listLineBuffer.size(); i++) {
                String str = listLineBuffer.get(i);
                String[] strSplit = str.split(",");
                if( Float.parseFloat(strSplit[3]) != 0.0f){
                    // 数値を生成
                    float value = Float.parseFloat(strSplit[2]) / Float.parseFloat(strSplit[3]) * 100f;
                    // (x, y) = (i, value)として座標データをセット
                    myDatas.add(new Entry(i, value));
                    // 平均値計算用変数
                    sumNumerator += Float.parseFloat(strSplit[2]);
                    sumDenominator += Float.parseFloat(strSplit[3]);
                }
            }
            // 平均値計算
            float aveValue = sumNumerator / sumDenominator * 100;

            ArrayList<Entry> myAveDatas = new ArrayList<>();
            myAveDatas.add(new Entry(0, aveValue));
            myAveDatas.add(new Entry(listLineBuffer.size()-1, aveValue));

            ArrayList<Entry> datas102 = new ArrayList<>();
            datas102.add(new Entry(0, 80f));
            datas102.add(new Entry(listLineBuffer.size()-1, 80f));

            ArrayList<Entry> datas100 = new ArrayList<>();
            datas100.add(new Entry(0, 50f));
            datas100.add(new Entry(listLineBuffer.size()-1, 50f));

            // 第一引数にデータ、第二引数にラベル名を設定する
            LineDataSet myLineDataSet = new LineDataSet(myDatas, "自己データ");
            LineDataSet myAveLineDataSet = new LineDataSet(myAveDatas, "(点線)自己平均値");
            LineDataSet lineDataSet102 = new LineDataSet(datas102, "機械割102%");
            LineDataSet lineDataSet100 = new LineDataSet(datas100, "機械割100%");

            // 値のプロット点
            myLineDataSet.setDrawCircles(true);
            myAveLineDataSet.setDrawCircles(false);
            lineDataSet102.setDrawCircles(false);
            lineDataSet100.setDrawCircles(false);
            // 線の色設定
            myLineDataSet.setColor(Color.RED);
            myAveLineDataSet.setColor(Color.RED);
            lineDataSet102.setColor(Color.GREEN);
            lineDataSet100.setColor(Color.BLUE);
            // 点線設定
            myAveLineDataSet.enableDashedLine(10,10,10);

            ArrayList<ILineDataSet> dataSets = new ArrayList<>();
            dataSets.add(myLineDataSet);
            dataSets.add(myAveLineDataSet);
            dataSets.add(lineDataSet102);
            dataSets.add(lineDataSet100);
            LineData lineData = new LineData(dataSets);
            lineChart.setData(lineData);

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // アニメーション
        lineChart.animateX(1200, Easing.EasingOption.Linear);

    }

    // グラフviewを初期化
    private void initChart() {
        // 線グラフView
        lineChart = (LineChart) findViewById(R.id.line_chart);

        /*
        // グラフ説明テキストを表示するか
        lineChart.getDescription().setEnabled(true);
        // グラフ説明テキストのテキスト設定
        lineChart.getDescription().setText("確率推移");
        // グラフ説明テキストの文字色設定
        lineChart.getDescription().setTextColor(Color.BLACK);
        // グラフ説明テキストの文字サイズ設定
        //lineChart.getDescription().setTextSize(10f);
        */

        // グラフへのタッチジェスチャーを有効にするか
        lineChart.setTouchEnabled(true);

        // グラフのスケーリングを有効にするか
        lineChart.setScaleEnabled(true);
        //lineChart.setScaleXEnabled(true);     // X軸のみに対しての設定
        //lineChart.setScaleYEnabled(true);     // Y軸のみに対しての設定

        // グラフのドラッギングを有効にするか
        lineChart.setDragEnabled(true);

        // グラフのピンチ/ズームを有効にするか
        lineChart.setPinchZoom(true);

        // グラフの背景色設定
        lineChart.setBackgroundColor(Color.WHITE);

        // Y軸(左)の設定
        // Y軸(左)の取得
        YAxis leftYAxis = lineChart.getAxisLeft();
        // Y軸(左)の最大値設定
        leftYAxis.setAxisMaximum(100);
        // Y軸(左)の最小値設定
        leftYAxis.setAxisMinimum(0);

        // Y軸(右)の設定
        // Y軸(右)は表示しない
        lineChart.getAxisRight().setEnabled(false);

        // X軸の設定
        // X軸は表示しない
        lineChart.getXAxis().setEnabled(false);
        //XAxis xAxis = lineChart.getXAxis();
        // X軸の最大値設定
        //xAxis.setAxisMaximum(100f);
        // X軸の最小値設定
        //xAxis.setAxisMinimum(0f);

    }



}
