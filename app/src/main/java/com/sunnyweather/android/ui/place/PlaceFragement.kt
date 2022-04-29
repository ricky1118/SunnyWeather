package com.sunnyweather.android.ui.place

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunnyweather.android.databinding.FragmentPlaceBinding
import com.sunnyweather.android.ui.weather.WeatherActivity


class PlaceFragment :Fragment() {
    private var _binding: FragmentPlaceBinding? = null
    //_binding!! 双感叹号表明_binding绝对不为空
    private val binding get() = _binding!!

    //委托得方式，延迟加载
    val viewModel by lazy {ViewModelProvider(this).get(PlaceViewModel::class.java)  }
    private lateinit var adapter: PlaceAdapter
    //加载fragment_place布局,传统写法
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        _binding = FragmentPlaceBinding.inflate(inflater, container, false)
        return binding.root
    }

//该方法在新版中已经被弃用,onActivityCreated，fragment关联的Activity启动后运行，为了降低fragment耦合度，在新版弃用
    //在这里给recyclerview的适配器适配相关数据，并指定linearLayoutmanager
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    //P647页面
    if (viewModel.isPlaceSaved()){

        val place = viewModel.getSavedPlace()
        val intent = Intent(context,WeatherActivity::class.java).apply {
            putExtra("location_lng",place.location.lng)
            putExtra("location_lat",place.location.lat)
            putExtra("place_name",place.name)
        }
        startActivity(intent)
        activity?.finish()
        return
    }

    /***********************/
        val layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.layoutManager = layoutManager

       // val recyclerView01 = view?.findViewById<RecyclerView>(R.id.recyclerView)
       // recyclerView01?.layoutManager = layoutManager
        adapter = PlaceAdapter(this,viewModel.placeList)
        binding.recyclerView.adapter = adapter
        binding.searchPlaceEdit.addTextChangedListener { editable ->
            val content = editable.toString()
            if (content.isNotEmpty()) {
                viewModel.searchPlaces(content)
            } else {
                binding.recyclerView.visibility = View.GONE
                binding.bgImageView.visibility = View.VISIBLE
                viewModel.placeList.clear()
                adapter.notifyDataSetChanged()
            }

        }
        viewModel.placeLiveData.observe(viewLifecycleOwner, Observer{
            result -> val places = result.getOrNull()
            if (places != null){
                binding.recyclerView.visibility = View.VISIBLE
                binding.bgImageView.visibility = View.GONE
                viewModel.placeList.clear()
                viewModel.placeList.addAll(places)
                adapter.notifyDataSetChanged()
            }else{
                Toast.makeText(activity,"未能查询到任何地点",Toast.LENGTH_SHORT).show()
                result.exceptionOrNull()?.printStackTrace()
            }
        })
    }





    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}