package com.example.navi

import android.util.DisplayMetrics
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import android.content.res.Resources
import android.graphics.Color
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.utils.ColorTemplate

class EnergyGraph(lineChart : LineChart) {
    private val energyPredictor = EnergyPredictor()
    private var energyGraphDataSet: EnergyGraphDataSet

    //        距離km（仮）
    private val goalDist = 300F
    private var curDist: Float

    init {
        // グラフサイズの比率を設定
        val widthRatio = 1.0 // 幅の比率
        val heightRatio = 0.5 // 高さの比率

        // DisplayMetricsを使用してデバイスのスクリーンサイズを取得
        val displayMetrics: DisplayMetrics = lineChart.context.resources.displayMetrics
        val screenWidth = displayMetrics.widthPixels
        val screenHeight = displayMetrics.heightPixels

        // グラフのサイズを設定
        val chartWidth = (screenWidth * widthRatio - 100).toInt()
        val chartHeight = (screenHeight * heightRatio - 100).toInt()

        // レイアウトパラメータにサイズを設定
        val layoutParams = lineChart.layoutParams
        layoutParams.width = chartWidth
        layoutParams.height = chartHeight
        lineChart.layoutParams = layoutParams

        // 凡例を非表示
        lineChart.legend.isEnabled = false

        // 説明ラベルを非表示
        lineChart.description.isEnabled = false

        // 背景色を設定
        lineChart.setBackgroundColor(Color.rgb(30, 30, 30))

        // タッチ操作無効
        lineChart.setTouchEnabled(false)

        // タッチ操作無効
        lineChart.xAxis.textColor = Color.WHITE
        lineChart.xAxis.position = XAxis.XAxisPosition.BOTTOM
        lineChart.axisLeft.textColor = Color.WHITE
        lineChart.axisLeft.axisMinimum = -20F
        lineChart.axisLeft.axisMaximum = 120F
        lineChart.xAxis.axisMaximum = goalDist * 1.1F
        lineChart.axisRight.isEnabled = false
//        lineChart.axisLeft. = -20F

        curDist = 0F
        energyGraphDataSet = initDataSet()
    }

    data class EnergyGraphDataSet(
        val initSocPred: ArrayList<Entry>,
        val curSocPred: ArrayList<Entry>
    )
    fun initDataSet(): EnergyGraphDataSet {

        val curSoC = energyPredictor.getCurSoC()
        val initSocPred = energyPredictor.predictSOC(curSoC, 0F, goalDist)
        val curSocPred = initSocPred

        return EnergyGraphDataSet(initSocPred, curSocPred)
    }
    fun updateDataSet(): EnergyGraphDataSet {
//        curDist = curDist + 40F / 60F / 60F
        curDist = curDist + 10F

        var curSoC = energyPredictor.getCurSoC()

        val x0 = energyGraphDataSet.initSocPred[0].x
        val y0 = energyGraphDataSet.initSocPred[0].y
        val x1 = energyGraphDataSet.initSocPred[1].x
        val y1 = energyGraphDataSet.initSocPred[1].y
        curSoC = 100 - curDist*(y0-y1)/(x1-x0)


        val curSocPred = energyPredictor.predictSOC(curSoC, curDist, goalDist)

        return EnergyGraphDataSet(energyGraphDataSet.initSocPred, curSocPred)
    }

    fun updateEnergyGraph(lineChart: LineChart) {
        energyGraphDataSet = updateDataSet()

        // データセットを作成
        val dataSet1 = LineDataSet(energyGraphDataSet.initSocPred, "Initial SOC Prediction")
        val dataSet2 = LineDataSet(energyGraphDataSet.curSocPred, "Current SOC Prediction")
        val dataSet3 = LineDataSet(arrayListOf(energyGraphDataSet.curSocPred[0]), "Current SOC")

        // データセットの設定
        dataSet1.setDrawValues(false) // データポイントの値の表示を無効化
        dataSet2.setDrawValues(false)
        dataSet3.setDrawValues(false)

        // データポイントのスタイルを設定
        dataSet1.setDrawCircles(false)
        dataSet2.setDrawCircles(false)
        dataSet1.lineWidth = 3f // 線の幅を設定
        dataSet2.lineWidth = 3f // 線の幅を設定
        dataSet1.color = Color.GRAY
        dataSet2.color = ColorTemplate.rgb("#ADFF2F")
        dataSet3.setDrawCircles(true)
        dataSet3.setCircleColor(Color.RED)
        dataSet3.setCircleRadius(6f)

        // グラフにデータセットを設定
        val data = LineData(dataSet1, dataSet2, dataSet3)
        lineChart.data = data

        // グラフの更新
        lineChart.invalidate()
    }
}