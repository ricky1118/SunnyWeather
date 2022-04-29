package com.sunnyweather.android.logic.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.RuntimeException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
//统一的网络源数据源访问入口
object SunnyWeatherNetwork {
   //查询位置动态代理
    private val placeService = ServiceCreator.create<PlaceService>()
    //地理位置查询
    suspend fun searchPlaces(query:String) = placeService.searchPlaces(query).await()
  //天气预报动态代理
    private val weatherService = ServiceCreator.create(WeatherService::class.java)
    //按天天气查询
    suspend fun getDailyWeather(lng:String,lat:String) = weatherService.getDailyWeather(lng,lat).await()
    //实时天气查询
    suspend fun getRealtimeWeather(lng:String,lat:String) = weatherService.getRealTimeWeather(lng,lat).await()
//定义扩展函数.await()
    private  suspend fun <T> Call<T>.await():T{
        return suspendCoroutine {continuation -> enqueue(object: Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                val body = response.body()
                if (body != null) continuation.resume(body)
                else continuation.resumeWithException(RuntimeException("response is null") )
            }

            override fun onFailure(call: Call<T>, t: Throwable) {
                continuation.resumeWithException(t)
            }
        })   }
    }

}