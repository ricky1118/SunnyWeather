package com.sunnyweather.android.logic.model

import com.google.gson.annotations.SerializedName
import java.util.*

//这里将所有数据模型都定义在了RealtimeResponse的内部，防止出现其它数据模型类有相同名称的冲突
data class RealtimeResponse(val status:String,val result: Result){

    data class Result(val realtime :Realtime)
    data class Realtime(val skycon:String,val temperature:Float,@SerializedName("air_quality")val airQuality:AirQuality)
    data class AirQuality(val aqi:AQI)
    data class AQI(val chn: Float)

}
//实时数据和每天数据都有Result类但是因为分别写在不同的类内部，所以不会冲突
data class DailyResponse(val status: String,val result: Result){
    data class Result(val daily: Daily)
    data class Daily(val temperature: List<Temperature>,val skycon:List<Skycon>,@SerializedName("life_index") val lifeIndex:LifeIndex)
    data class Temperature(val max:Float,val min:Float)
    data class Skycon(val value: String,val date:Date)
    data class LifeIndex(val coldRisk:List<LifeDescription>,val carWashing:List<LifeDescription>,val ultraviolet:List<LifeDescription>,val dressing:List<LifeDescription>)
    data class LifeDescription(val desc:String)
}