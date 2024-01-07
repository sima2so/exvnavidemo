package com.example.navi

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

import com.squareup.moshi.Json
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Url

import java.io.Serializable

// ベースアドレス
private const val BASE_URL = "https://test.core.its-mo.com/zmaps/api/apicore/core/v1_0/"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

// Retrofitビルダー(Stringを返すScalarsConverterFactoryでcreate)
//private val retrofit = Retrofit.Builder()
//    .addConverterFactory(ScalarsConverterFactory.create())
//    .baseUrl(BASE_URL)
//    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

// シングルトンオブジェクトとして１つのみインスタンス化される
// retrofitServiceを遅延初期化(実際に利用される時に初期化)
object ItsumoNnaviApi {
    val retrofitService : ItsumoApiService by lazy {
        // RetrofitビルダーのcreateメソッドにMarsApiServiceインターフェイスを渡す
        retrofit.create(ItsumoApiService::class.java)
    }
}

interface ItsumoApiService {
    @GET
    suspend fun getElevation(@Url url: String): List<Elevation>
//    suspend fun getElevation(@Url url: String): String

    @GET
    suspend fun getPoiword(@Url url: String): PoiWord
//    suspend fun getPoiword(@Url url: String): String
}

data class Elevation(
    val elevation: Float, val meshType: String
)

data class PoiWord(
    val info: Info?,
    val item: List<Item>?,
    val status: Status?
)

data class Info(
    val facet: Facet?,
    val hit: Int?
)

data class Item(
    val addressText: String?,
    val arrivalInfo: List<ArrivalInfo>?,
    val code: String?,
    val detail: Any?,
    val genre: GenreX?,
    val kana: String?,
    val language: Any?,
    val phoneNumber: String?,
    val point: Point?,
    val text: String?,
    val zipcode: String?
) : Serializable

data class Status(
    val code: String?,
    val text: String?
)

data class Facet(
    val areaInfo: Any?,
    val detailInfo: Any?,
    val genre: Any?,
    val genreMlang: Any?,
    val shk: Any?,
    val tod: Any?
)

data class ArrivalInfo(
    val arrivalpoint: Arrivalpoint?,
    val exitpoint: Exitpoint?,
    val exittype: Exittype?,
    val kana: String?,
    val text: String?
)

data class GenreX(
    val code: String?,
    val text: String?
)

data class Point(
    val lat: Double?,
    val lon: Double?
)

data class Arrivalpoint(
    val lat: Double?,
    val lon: Double?
)

data class Exitpoint(
    val lat: Double?,
    val lon: Double?
)

data class Exittype(
    val code: String?,
    val text: String?
)

//data class MarsPhoto(
//    val id: String, @Json(name = "img_src") val imgSrcUrl: String
////    val id: String, @Json(name = "img_src") val imgSrcUrl: String
//)