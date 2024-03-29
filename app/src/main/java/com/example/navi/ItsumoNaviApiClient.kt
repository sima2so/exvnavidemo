package com.example.navi

import android.util.Log
import android.webkit.JavascriptInterface
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import java.net.URLEncoder
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.reflect.jvm.internal.impl.builtins.StandardNames.FqNames.map


class ItsumoNaviApiClient() {
    private val consumer_key = "JSZ22f5fd2f67d9|2UcW0"
    private val secret = "5th-fmKSlNWGvB_cMT2nASDnZHU" + "&"

    private val webapiBaseUri = "https://test.core.its-mo.com/zmaps/api/apicore/core/v1_0/"
    private val jsapiBaseUri = "https://test.api.its-mo.com/v3/"

    fun search_goal(searchWord: String?): PoiWord {
        val nameAPI = "poi/word"
        val paramAPI = mapOf(
            "word" to searchWord.orEmpty(),
            "datum" to "WGS84"
        )
        val response = itumonavi_api_gateway(nameAPI, paramAPI)

        return response["api_response"] as PoiWord
    }

    fun make_signature_hmac(http_method: String, parameter: MutableMap<String, String>, uri: String, secret: String) : MutableMap<String, String>{
        val encodedParams = parameter.toList().sortedBy { it.first }
            .joinToString("&") { "${it.first}=${URLEncoder.encode(it.second, "UTF-8")}" }
        val base_string = "$http_method&${URLEncoder.encode(uri, "UTF-8")}&${URLEncoder.encode(encodedParams, "UTF-8")}"
        val mac = Mac.getInstance("HmacSHA1")
        val secretKeySpec = SecretKeySpec(secret.toByteArray(), "HmacSHA1")

        mac.init(secretKeySpec)
        val signature = mac.doFinal(base_string.toByteArray())
        val signature_decoded = Base64.getEncoder().encodeToString(signature)
        parameter["oauth_signature"] = signature_decoded

        return parameter
    }

    fun itumonavi_api_gateway(nameAPI: String, paramAPI: Map<String, String>): Map<String, Any> {

        val time = System.currentTimeMillis() / 1000
        val randomname = (1..10).map { ('a'..'z').random() }.joinToString("")

        val http_method = "GET"
        var parameter = mutableMapOf(
            "if_clientid" to consumer_key,
            "if_auth_type" to "oauth",
            "oauth_consumer_key" to consumer_key,
            "oauth_signature_method" to "HMAC-SHA1",
            "oauth_timestamp" to time.toString(),
            "oauth_nonce" to randomname,
            "oauth_version" to "1.0",
        )
        parameter += paramAPI

        val uri = webapiBaseUri + nameAPI

        parameter = make_signature_hmac(http_method, parameter, uri, secret)

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

    @JavascriptInterface
    fun gen_itumonavi_JSAPI_loader_url(): String {

        val time = System.currentTimeMillis() / 1000
        val randomname = (1..10).map { ('a'..'z').random() }.joinToString("")

        val http_method = "GET"
        var parameter = mutableMapOf(
            "key" to consumer_key,
            "auth_type" to "oauth",
            "oauth_consumer_key" to consumer_key,
            "oauth_signature_method" to "HMAC-SHA1",
            "oauth_timestamp" to time.toString(),
            "oauth_nonce" to randomname,
            "oauth_version" to "1.0",
            "api" to "zdcmap.js,control.js,search.js,shape.js",
            "enc" to "UTF8"
        )

        val apiName = "loader"
        val uri = jsapiBaseUri + apiName

        parameter = make_signature_hmac(http_method, parameter, uri, secret)

        val url = "$uri?${parameter.toList().sortedBy { it.first }
            .joinToString("&") { "${it.first}=${URLEncoder.encode(it.second, "UTF-8")}" }}"

        return url
    }
    inner class ItsumoNaviApiClientJS(val pointFrom:Point, val pointTo:Point) {
        @JavascriptInterface
        fun gen_itumonavi_JSAPI_route3_params(): String {

            val time = System.currentTimeMillis() / 1000
            val randomname = (1..10).map { ('a'..'z').random() }.joinToString("")

            val http_method = "GET"
            var parameter = mutableMapOf(
                "if_clientid" to consumer_key,
                "if_auth_type" to "oauth",
                "oauth_consumer_key" to consumer_key,
                "oauth_signature_method" to "HMAC-SHA1",
                "oauth_timestamp" to time.toString(),
                "oauth_nonce" to randomname,
                "oauth_version" to "1.0",
                "from" to pointFrom.lat.toString() + "," + pointFrom.lon.toString(),
                "to" to pointTo.lat.toString() + "," + pointTo.lon.toString(),
                "searchType" to "0",
            )

            val apiName = "route3/drive"
            val uri = webapiBaseUri + apiName

            parameter = make_signature_hmac(http_method, parameter, uri, secret)

            val jsonString: String = Gson().toJson(parameter)
            val params = parameter.toList().joinToString(",") { "${it.first}:'${it.second}'" }

            return jsonString
        }
    }
//    private fun itumonavi_api(searchWord: String) {
//
//        var nameAPI: String
//        var paramAPI: Map<String, String>
//        var response: Any
//
//        nameAPI = "elevation"
//        paramAPI = mapOf(
//            "latlon" to "34.990855,137.0086819",
//            "meshtype" to "N05m")
//        response = itumonavi_api(nameAPI, paramAPI)
//
//        response = itumonavi_api(nameAPI, paramAPI)
//        Log.d("MSG", ((response["api_response"] as JSONObject?)?.get("item") as JSONObject?)?.get("text") as String)
//
//        val df = (response["api_response"] as JSONObject?)?.get("item") as List<Map<String, Any>>
//        val latitudes = df.map { (it["point"] as JSONObject?)?.get("lat") }
//        val longitudes = df.map { (it["point"] as JSONObject?)?.get("lon") }
//        val targetLocations = latitudes.zip(longitudes).map { "${it.first},${it.second}" }
//
//        val currentLocation = "34.990855,137.0086819"
//        var currentTargetLocation = ""
//        for (singleTargetLocation in targetLocations) {
//            currentTargetLocation += "$currentLocation,$singleTargetLocation,"
//        }
//        currentTargetLocation = currentTargetLocation.dropLast(1)
//        Log.d("MSG", currentTargetLocation)
//    }
}