package com.sunnyweather.android.ui.weather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.sunnyweather.android.R
import com.sunnyweather.android.databinding.ActivityMainBinding
import com.sunnyweather.android.databinding.ActivityWeatherBinding
import com.sunnyweather.android.logic.model.Weather
import com.sunnyweather.android.logic.model.getSky
import java.text.SimpleDateFormat
import java.util.*

class WeatherActivity : AppCompatActivity() {
    val viewModel by lazy { ViewModelProvider(this).get(WeatherViewModel::class.java) }
    private lateinit var binding: ActivityWeatherBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        //now子布局
        setContentView(binding.nowWeatherShow.nowLayOut)
        //forecast.xml子布局
        setContentView(binding.forecastWeatherShow.forecast)
        //lifeIndex
        setContentView(binding.lifeIndexShow.lifeIndex)

        if (viewModel.locationlng.isEmpty()){
            viewModel.locationlng = intent.getStringExtra("location_lng") ?: ""
        }
        if (viewModel.locationlat.isEmpty()){
            viewModel.locationlng = intent.getStringExtra("location_lat") ?: ""
        }
        if (viewModel.placeName.isEmpty()){
            viewModel.placeName = intent.getStringExtra("place_name") ?: ""
        }
        //观察取值
        viewModel.weatherLiveData.observe(this, Observer { result -> val weather = result.getOrNull()
        if (weather != null){
            showWeatherInfo(weather)
        }else{
            Toast.makeText(this,"无法成功获取天气信息",Toast.LENGTH_SHORT).show()
            result.exceptionOrNull()?.printStackTrace()
        }
        })
        viewModel.refreshWeather(viewModel.locationlng,viewModel.locationlat)
    }

    private fun showWeatherInfo(weather: Weather){

    //    val placeName:TextView = findViewById(R.id.placeName)
        binding.nowWeatherShow.placeName.text = viewModel.placeName
    //    placeName.text = viewModel.placeName
        val realtime = weather.realtime
        val daily = weather.daily
      /************************************************************************************/
        //填充now.xml布局中的数据
        //实时温度
        val currentTempText = "${realtime.temperature.toInt()}℃"
    //    val currentTemp = findViewById<TextView>(R.id.currentTemp)
        binding.nowWeatherShow.currentTemp.text = currentTempText
    //    currentTemp.text = currentTemptext
        //Skycon
     //   val currentSky = findViewById<TextView>(R.id.currentSky)
        binding.nowWeatherShow.currentSky.text = getSky(realtime.skycon).info
    //    currentSky.text = getSky(realtime.skycon).info
        //AQI
        val currentPM25Text = "空气指数${realtime.airQuality.aqi.chn.toInt()}"
   //     val currentAQItext = findViewById<TextView>(R.id.currentAQI)
        binding.nowWeatherShow.currentAQI.text = currentPM25Text
    //    currentAQItext.text = currentPM25Text
        //now 布局背景
        binding.nowWeatherShow.nowLayOut.setBackgroundResource(getSky(realtime.skycon).bg)
      /******************************************************************************************/
      //forecast布局中填充数据
      binding.forecastWeatherShow.forecastLayout.removeAllViews()  //移除所有窗口，不显示forecast窗口
        val days = daily.skycon.size
        for (i in 0 until days){
            val skycon = daily.skycon[i]
            val temperature = daily.temperature[i]
         //子布局forecast_item子布局加载到forecast中
            val view = LayoutInflater.from(this).inflate(R.layout.forecast_item,binding.forecastWeatherShow.forecastLayout,false)
            val dateInfo = view.findViewById<TextView>(R.id.dateInfo)
            val skyIcon = view.findViewById(R.id.skyIcon) as ImageView
            val skyInfo = view.findViewById(R.id.skyInfo) as TextView
            val temperatureInfo = view.findViewById<TextView>(R.id.temperatureInfo)
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateInfo.text = simpleDateFormat.format(skycon.date)
            val sky = getSky(skycon.value)
            skyIcon.setImageResource(sky.icon)
            skyInfo.text = sky.info
            val tempText = "${temperature.min.toInt()}~${temperature.max.toInt()}℃"
            temperatureInfo.text = tempText
            binding.forecastWeatherShow.forecastLayout.addView(view)
        }
        //填充life_index.xml布局中的数据
        val lifeIndex = daily.lifeIndex
        binding.lifeIndexShow.dressingText.text = lifeIndex.dressing[0].desc
        binding.lifeIndexShow.coldRiskText.text = lifeIndex.coldRisk[0].desc
        binding.lifeIndexShow.ultravioletText.text = lifeIndex.ultraviolet[0].desc
        binding.lifeIndexShow.carWashingText.text = lifeIndex.carWashing[0].desc
        binding.weatherLayout.visibility = View.VISIBLE

    }
}