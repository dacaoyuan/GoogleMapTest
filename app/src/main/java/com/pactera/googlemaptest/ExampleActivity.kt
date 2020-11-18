package com.pactera.googlemaptest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class ExampleActivity : AppCompatActivity(), OnMapReadyCallback {

    private val TAG = "ExampleActivity"
    private lateinit var mGoogleMap: GoogleMap

    //潍坊软件园 LatLng(纬度,经度)
   // val strOrigin = "36.70458915541349,119.1854485767212"

    //富华游乐园
    val strOrigin = "36.71525382744859,119.16037559509276"

    val appointLoc = LatLng(
        strOrigin.split(",").toTypedArray()[0].toDouble(),
        strOrigin.split(",").toTypedArray()[1].toDouble()
    );

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)


        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)


    }



    override fun onMapReady(mGoogleMap: GoogleMap?) {
        this.mGoogleMap = mGoogleMap ?: return;
        with(mGoogleMap) {
            // 移动地图到指定经度的位置
            moveCamera(CameraUpdateFactory.newLatLngZoom(appointLoc, 15f))


            //添加标记到指定经纬度
            addMarker(
                MarkerOptions()
                    .position(appointLoc)
                    .title("富华游乐园")
                    .snippet("$appointLoc")
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_change_history_red_a700_24dp))
            ).showInfoWindow()


        }

    }

    public inner class MyLocationSource : LocationSource {
        var mListener: LocationSource.OnLocationChangedListener? = null
        override fun activate(mListener: LocationSource.OnLocationChangedListener?) {
            this.mListener = mListener;
        }

        override fun deactivate() {
            mListener = null;
        }
    }


}