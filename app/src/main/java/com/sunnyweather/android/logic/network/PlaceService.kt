package com.sunnyweather.android.logic.network

import com.sunnyweather.android.logic.model.PlaceResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
/*****************
 * 地址查询接口
 * 访问请求的路径，
 * 定义函数，调用该方法时会自动发起网络请求，并将返回的json数据解析成PlaceResponse格式数据
 * **********************************************************************************/
interface PlaceService {
    @GET("")
    fun searchPlaces(@Query("query") query: String): Call<PlaceResponse>
}