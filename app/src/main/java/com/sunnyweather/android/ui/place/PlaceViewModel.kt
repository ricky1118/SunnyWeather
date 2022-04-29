package com.sunnyweather.android.ui.place

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.sunnyweather.android.logic.Repository
import com.sunnyweather.android.logic.model.Place

class PlaceViewModel: ViewModel() {
    private val searchLiveData = MutableLiveData<String>()
    val placeList = ArrayList<Place>()
    val placeLiveData = Transformations.switchMap(searchLiveData){query -> Repository.searchPlaces(query)}

    fun  searchPlaces(query:String){
        searchLiveData.value = query
    }
//地址存储涉是placeModel相关需要在这里再封装一次，前面的是Repository中封装了一次
    fun savePlace(place: Place) = Repository.savePlace(place)
    fun getSavedPlace() = Repository.getSavePlace()
    fun isPlaceSaved() = Repository.isPlaceSaved()
}