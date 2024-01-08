package com.example.navi

import com.github.mikephil.charting.data.Entry
import java.lang.Boolean.TRUE

class EnergyPredictor {

    val DEBUG_MODE = TRUE
    fun predictSOC(curSoC:Float, startDist:Float, goalDist:Float): ArrayList<Entry> {
        val predSOCArrayList: ArrayList<Entry> = ArrayList()
        predSOCArrayList.add(Entry(startDist, curSoC))

//      バッテリ容量kWh（71.4kWh）
        val capBatt = 71.4F
//      電費Wh/km（126Wh/km）
        val distPerWatt = 126F

//      残距離km
        val remainDist = goalDist - startDist
//      予測消費電力Wh
        val consWatt = distPerWatt * remainDist
//      予測消費SOC（％）
        val predSOCdelta = consWatt / capBatt / 1000 * 100
//      予測SOC（％）
        val predSOC = curSoC - predSOCdelta

        predSOCArrayList.add(Entry(goalDist, predSOC))
        return predSOCArrayList
    }

    fun getCurSoC(): Float{
        val curSoC :Float
        if(!DEBUG_MODE){
            curSoC = 100F
        }else{
            curSoC = 100F
        }
        return curSoC
    }
}