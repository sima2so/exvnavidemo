package com.example.navi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract.CommonDataKinds.Website.URL
import java.util.Base64
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import com.github.mikephil.charting.charts.LineChart
import kotlinx.coroutines.Deferred
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URLEncoder

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.http.*
import java.net.URL
import retrofit2.Call
import retrofit2.Retrofit
//import retrofit2.converter.moshi.MoshiConverterFactory
//import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import kotlin.concurrent.thread

class SearchGoalActivity : AppCompatActivity() {
    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_view)

        listView = findViewById(R.id.custom_list_view)

        // MainActivityから渡された変数を受け取る
        val searchWord = intent.getStringExtra("searchWord")

        // itumonavi_api_search_goal関数でitemListを生成（inputVariableを渡す）
        val itemList = itumonavi_api_search_goal(searchWord)

        // リストをListViewにセット
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, itemList)
        listView.adapter = adapter
    }

    private fun itumonavi_api_search_goal(searchWord: String?): List<String> {
        var nameAPI: String
        var paramAPI: Map<String, String>
        var response: Any

        nameAPI = "poi/word"
        paramAPI = mapOf(
//            "word" to searchWord.orEmpty(),
            "word" to "東刈谷".orEmpty(),
            "datum" to "WGS84"
        )

        response = itumonavi_api(nameAPI, paramAPI)

        val list_search_result: List<String>
        val itemArray = response["api_response"]

//        for (i in 0 until itemArray.size) {
//            val itemObject = itemArray.getJSONObject(i)
//            val text = itemObject.getString("text")
//            itemList.add(text)
//        }
        return listOf("$searchWord", "Result 1", "Result 2", "Result 3", "Result 4", "Result 5")
    }

//    fun itumonavi_api_search_goal(searchWord: String) {
//
//        // データを用意
//        val data = listOf(
//            "とり", "しか", "ぞう", "きつね",
//            "かば", "ライオン", "パンダ", "ひつじ"
//        )
//
//        // データを更新して表示
//        adapter.clear()
//        adapter.addAll(data)
//        adapter.notifyDataSetChanged()
//    }
//        setContentView(R.layout.activity_navi)


//        val lineChart = findViewById<LineChart>(R.id.line_chart);
//        val energyGraph = EnergyGraph(lineChart)
//        val dataSet = energyGraph.updateDataSet()
//        energyGraph.updateEnergyGraph(lineChart, dataSet)

//        // リストデータの作成
//        val dataList = arrayListOf<Data>()
//        for (i in 0..100){
//            dataList.add(Data().apply {
//                title = "${i}番目のタイトル"
//                text =  "${i}番目のテキスト"
//            })
//        }
//        // アダプターをセット
//        val adapter = CustomAdapter(this, dataList)
//        val custom_list_view = findViewById<ListView>(R.id.custom_list_view);
//        custom_list_view.adapter = adapter
//        val nameAPI = "poi/word"

//        setContentView(R.layout.activity_navi)
//
//        val lineChart = findViewById<LineChart>(R.id.line_chart);
//        val energyGraph = EnergyGraph(lineChart)
//        val dataSet = energyGraph.updateDataSet()
//        energyGraph.updateEnergyGraph(lineChart, dataSet)




//        var nameAPI: String
//        var paramAPI: Map<String, String>
//        var response: Any
//
//        nameAPI = "poi/word"
//        paramAPI = mapOf(
//            "word" to searchWord,
//            "datum" to "WGS84"
//        )
//
//        response = itumonavi_api(nameAPI, paramAPI)
//    }
//
//        setContentView(R.layout.list_view_singleline)
//        setContentView(R.layout.list_view)
//        // リストデータの作成
//        val dataList = arrayListOf<Data>()
//        for (i in 0..100){
//            dataList.add(Data().apply {
//                title = "${i}番目のタイトル"
//                text =  "${i}番目のテキスト"
//            })
//        }
//        // アダプターをセット
//        val adapter = CustomAdapter(this, dataList)
//        val custom_list_view = findViewById<ListView>(R.id.custom_list_view);
//        custom_list_view.adapter = adapter
//        nameAPI = "poi/word"
//    }

    fun itumonavi_api_use(searchWord: String) {

        var nameAPI: String
        var paramAPI: Map<String, String>
        var response: Any

        nameAPI = "elevation"
        paramAPI = mapOf(
            "latlon" to "34.990855,137.0086819",
            "meshtype" to "N05m")
        response = itumonavi_api(nameAPI, paramAPI)

        response = itumonavi_api(nameAPI, paramAPI)
        Log.d("MSG", ((response["api_response"] as JSONObject?)?.get("item") as JSONObject?)?.get("text") as String)

        val df = (response["api_response"] as JSONObject?)?.get("item") as List<Map<String, Any>>
        val latitudes = df.map { (it["point"] as JSONObject?)?.get("lat") }
        val longitudes = df.map { (it["point"] as JSONObject?)?.get("lon") }
        val targetLocations = latitudes.zip(longitudes).map { "${it.first},${it.second}" }

        val currentLocation = "34.990855,137.0086819"
        var currentTargetLocation = ""
        for (singleTargetLocation in targetLocations) {
            currentTargetLocation += "$currentLocation,$singleTargetLocation,"
        }
        currentTargetLocation = currentTargetLocation.dropLast(1)
        Log.d("MSG", currentTargetLocation)
//        import pandas as pd
//        val df = pd.DataFrame(response["api_response"]["item"])
//        df["lat"] = df["point"].map { it["lat"] }
//        df["lon"] = df["point"].map { it["lon"] }
//        df["targetLocation"] = df["lat"].astype(String) + "," + df["lon"].astype(String)
//
//        val currentLocation = "34.990855,137.0086819"
//        var current_targetLocation = ""
//        for (singleTargetLocation in df["targetLocation"]) {
//            current_targetLocation += currentLocation + "," + singleTargetLocation + ","
//        }
//        current_targetLocation = current_targetLocation.dropLast(1)
//
//        nameAPI = "distance"
//        paramAPI = mapOf("latlon" to current_targetLocation)
//        response = itumonavi_api(nameAPI, paramAPI)
//
//        df["distance"] = response["api_response"]
//        df["distance_km"] = df["distance"] / 1000
//        df = df.sort_values("distance_km")
    }

    fun itumonavi_api(nameAPI: String, paramAPI: Map<String, String>): Map<String, Any> {

        val time = System.currentTimeMillis() / 1000
        val randomname = (1..10).map { ('a'..'z').random() }.joinToString("")

        val http_method = "GET"
        val parameter = mutableMapOf(
            "if_clientid" to "JSZ22f5fd2f67d9|2UcW0",
            "if_auth_type" to "oauth",
            "oauth_consumer_key" to "JSZ22f5fd2f67d9|2UcW0",
            "oauth_signature_method" to "HMAC-SHA1",
            "oauth_timestamp" to time.toString(),
            "oauth_nonce" to randomname,
            "oauth_version" to "1.0",
//            "oauth_timestamp" to "1703500741",
//            "oauth_nonce" to "nppfjiougv",
        )
        parameter += paramAPI

        val secret = "5th-fmKSlNWGvB_cMT2nASDnZHU" + "&"

        val baseURI = "https://test.core.its-mo.com/zmaps/api/apicore/core/v1_0/"
        val uri = baseURI + nameAPI

        fun make_signature_hmac(http_method: String, parameter: MutableMap<String, String>, uri: String, secret: String) {
            val encodedParams = parameter.toList().sortedBy { it.first }
                .joinToString("&") { "${it.first}=${URLEncoder.encode(it.second, "UTF-8")}" }
            val base_string = "$http_method&${URLEncoder.encode(uri, "UTF-8")}&${URLEncoder.encode(encodedParams, "UTF-8")}"
            val mac = Mac.getInstance("HmacSHA1")
            val secretKeySpec = SecretKeySpec(secret.toByteArray(), "HmacSHA1")

            mac.init(secretKeySpec)
            val signature = mac.doFinal(base_string.toByteArray())
            val signature_decoded = Base64.getEncoder().encodeToString(signature)
            parameter["oauth_signature"] = signature_decoded
        }

        make_signature_hmac(http_method, parameter, uri, secret)

        val url = "$uri?${parameter.toList().sortedBy { it.first }
            .joinToString("&") { "${it.first}=${URLEncoder.encode(it.second, "UTF-8")}" }}"

//        lateinit var listResult: Any
        lateinit var listResult: Any
        var oauth_status_code: Int
        runBlocking {
            try {
                if (nameAPI == "elevation") {
                    listResult = ItsumoNnaviApi.retrofitService.getElevation(url)
                } else if (nameAPI == "poi/word") {
                    listResult = ItsumoNnaviApi.retrofitService.getPoiword(url)
                }

                oauth_status_code = 0
//            listResult = MarsApi.retrofitService.getPhotos("https://android-kotlin-fun-mars-server.appspot.com/photos")
            } catch (e: Exception) {
                listResult = mutableListOf("アクセスに失敗しました。")
                oauth_status_code = -1
                Log.d("mopi", "debug $e")
            }
        }

        var response = mutableMapOf(
            "oauth_status_code" to oauth_status_code,
            "api_response" to listResult
        )

        return response
    }

}