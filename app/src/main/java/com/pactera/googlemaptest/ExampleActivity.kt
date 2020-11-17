package com.pactera.googlemaptest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.LocationSource
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class ExampleActivity : AppCompatActivity(), OnMapReadyCallback {

    private val TAG = "ExampleActivity"
    private lateinit var mGoogleMap: GoogleMap
    lateinit var mMyLocationSource: MyLocationSource

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)

       // mMyLocationSource = MyLocationSource();

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)


    }

    override fun onMapReady(mGoogleMap: GoogleMap?) {
        this.mGoogleMap = mGoogleMap ?: return;
       // mGoogleMap.setLocationSource(mMyLocationSource)


    }

    public inner class MyLocationSource : LocationSource {
        var mListener: LocationSource.OnLocationChangedListener? = null
        override fun activate(mListener: LocationSource.OnLocationChangedListener?) {
            this.mListener = mListener;
           // mListener!!.onLocationChanged()
        }
        override fun deactivate() {
            mListener = null;
        }
    }


}