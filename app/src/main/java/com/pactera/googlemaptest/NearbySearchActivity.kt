package com.pactera.googlemaptest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.model.LatLng
import com.pactera.googlemaptest.adapter.NearbyPlaceAdapter
import com.pactera.googlemaptest.model.NearbyModel
import com.vise.xsnow.http.ViseHttp
import com.vise.xsnow.http.callback.ACallback
import kotlinx.android.synthetic.main.activity_nearby_search.*

class NearbySearchActivity : AppCompatActivity() {
    private val TAG = "NearbySearchActivity"

    private lateinit var mLatLng: LatLng;
    private lateinit var mNearbyPlaceAdapter: NearbyPlaceAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nearby_search)


        mLatLng = intent.getParcelableExtra<LatLng>("latLng")!!


        mRecyclerView.layoutManager = LinearLayoutManager(this);
        mNearbyPlaceAdapter =
            NearbyPlaceAdapter();
        mRecyclerView.adapter = mNearbyPlaceAdapter;

        getNearbyPlace(mLatLng);


    }


    private fun getNearbyPlace(mLatLng: LatLng) {

        ViseHttp.GET("place/nearbysearch/json")
            .tag(TAG)

            .addParam("location", "${mLatLng.latitude},${mLatLng.longitude}")
            .addParam("radius", "1000") //半径1000 米
            //.addParam("type", "restaurant")  //搜索结果期望的类型，例如：超市，咖啡馆，学校等等 https://developers.google.cn/places/web-service/supported_types
            // .addParam("keyword", "cruise")//搜索结果期望包含的关键字

            .addParam("language", "zh-CN")
            .addParam("key", Config.API_KAY)

            .request(object : ACallback<NearbyModel?>() {
                override fun onSuccess(mBean: NearbyModel?) {
                    //请求成功，AuthorModel为解析服务器返回数据的对象，可以是String，按需定义即可
                    // Log.i(TAG, " mBeanStr=${mBean}")

                    if (mBean!!.status == "OK") {
                        val resultList = mBean.results
                        val name = resultList[0].name
                        Log.i(TAG, "onSuccess: name=> $name")
                        mNearbyPlaceAdapter.setNewData(resultList)
                    } else {
                        Log.i(TAG, " errCode=${mBean!!.status}")
                    }

                }

                override fun onFail(errCode: Int, errMsg: String) {
                    //请求失败，errCode为错误码，errMsg为错误描述
                    Log.i(TAG, " errCode=${errCode} errMsg=$errMsg")
                }
            })


    }
}