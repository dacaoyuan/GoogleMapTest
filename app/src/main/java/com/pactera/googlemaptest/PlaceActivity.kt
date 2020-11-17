package com.pactera.googlemaptest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.pactera.googlemaptest.model.PlaceModel
import com.pactera.googlemaptest.utils.StringUtil
import com.vise.xsnow.http.ViseHttp
import com.vise.xsnow.http.callback.ACallback

class PlaceActivity : AppCompatActivity(), OnMapReadyCallback {

    private val TAG = "PlaceActivity"
    private lateinit var mGoogleMap: GoogleMap
    private lateinit var mLatLng: LatLng;
    private lateinit var placesClient: PlacesClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place)

        mLatLng = intent.getParcelableExtra<LatLng>("latLng")!!

        placesClient = Places.createClient(this)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)


    }


    override fun onMapReady(mGoogleMap: GoogleMap?) {
        this.mGoogleMap = mGoogleMap ?: return;

        mGoogleMap.apply {

            // 移动地图到指定经度的位置
            moveCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 15f))

            addMarker(
                MarkerOptions()
                    .position(mLatLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                //.title("GetPlace")
                //.snippet("$latLng")
            )
            setOnMapLongClickListener { latLng ->
                Log.i(TAG, "onMapReady: 地图长按点击事件")
                //currentSelectMarkerLatLng=latLng;
                mGoogleMap!!.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title("GetPlace")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                        .snippet("$latLng")
                )

                setOnInfoWindowClickListener { mMarker ->
                    val position = mMarker!!.position
                    Log.i(TAG, "setOnInfoWindowClickListener: position=$position")
                    getPlaceId(mLatLng)

                }
            }
        }


    }

    private fun getPlaceId(mLatLng: LatLng) {

        //首先需要根据经纬度获取 placeId 和一些 地点相关信息  （反向地理编码（地址查找））

        Log.i(TAG, "getPlaceInfo: value=> ${mLatLng.latitude},${mLatLng.longitude}")

        ViseHttp.GET("geocode/json")
            .tag("tag1")
            .addParam("latlng", "${mLatLng.latitude},${mLatLng.longitude}")
            .addParam("language", "zh-CN")

            .addParam("key", Config.API_KAY)

            .request(object : ACallback<PlaceModel?>() {
                override fun onSuccess(mBean: PlaceModel?) {
                    //请求成功，AuthorModel为解析服务器返回数据的对象，可以是String，按需定义即可
                    // Log.i(TAG, "onSuccess: mBean=> $mBean")
                    val results = mBean!!.results

                    results.forEach {
                        val placeId = it.place_id
                        Log.i(TAG, "onSuccess: placeId=> $placeId")

                        val formattedAddress = it.formatted_address
                        println("PlaceActivity.onSuccess formattedAddress=$formattedAddress")

                    }
                    if (results != null && results.size > 0) {
                        val placeId = results[0].place_id
                        getPlaceInfo(placeId)
                    }
                }

                override fun onFail(errCode: Int, errMsg: String) {
                    //请求失败，errCode为错误码，errMsg为错误描述
                    Log.i(TAG, " errCode=${errCode} errMsg=$errMsg")
                }
            })


    }

    // 使用字段定义要返回的数据类型.
    var placeFields = listOf(
        Place.Field.ID,
        Place.Field.NAME,
        Place.Field.LAT_LNG,
        Place.Field.ADDRESS,
        Place.Field.ADDRESS_COMPONENTS,
        Place.Field.RATING,
        Place.Field.USER_RATINGS_TOTAL,
        Place.Field.VIEWPORT,

        Place.Field.TYPES,
        Place.Field.PLUS_CODE,
        Place.Field.OPENING_HOURS,
        Place.Field.PHONE_NUMBER,
        Place.Field.PRICE_LEVEL,
        Place.Field.UTC_OFFSET,
        Place.Field.WEBSITE_URI,

        Place.Field.PHOTO_METADATAS,
        Place.Field.BUSINESS_STATUS
    )

    //根据placeId，可以获取 地点 和 照片 等相关信息
    private fun getPlaceInfo(placeId: String) {

        //获取所有信息
        //placeFields = listOf(*Place.Field.values())


        val request = FetchPlaceRequest.newInstance(placeId, placeFields)
        val placeTask = placesClient.fetchPlace(request)
        placeTask.addOnSuccessListener { response: FetchPlaceResponse ->
            Log.i(TAG, "getPlaceInfo: addOnSuccessListener response=$response")
            val address = response.place.address
            Log.i(TAG, "getPlaceInfo: addOnSuccessListener address=$address")
            Toast.makeText(this, "${response}", Toast.LENGTH_LONG).show()

            // responseView.text = StringUtil.stringify(response, isDisplayRawResultsChecked)
        }
        placeTask.addOnFailureListener { exception: Exception ->
            exception.printStackTrace()
            Log.i(TAG, "getPlaceInfo: exception.message=${exception.message}")
        }
        placeTask.addOnCompleteListener {
            Log.i(TAG, "getPlaceInfo: addOnCompleteListener")
        }


    }
}