package com.pactera.googlemaptest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PointOfInterest
import com.pactera.googlemaptest.adapter.ExampleAdapter
import com.pactera.googlemaptest.model.ExampleBean
import kotlinx.android.synthetic.main.activity_example.*

class ExampleActivity : AppCompatActivity(), OnMapReadyCallback {

    private val TAG = "ExampleActivity"
    private lateinit var mGoogleMap: GoogleMap

    //潍坊软件园 LatLng(纬度,经度)
    // val strOrigin = "36.70458915541349,119.1854485767212"

    //富华游乐园
    val strOrigin = "36.71525382744859,119.16037559509276"

    private lateinit var mExampleAdapter: ExampleAdapter

    val appointLoc = LatLng(
        strOrigin.split(",").toTypedArray()[0].toDouble(),
        strOrigin.split(",").toTypedArray()[1].toDouble()
    );

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)


        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        mExampleAdapter = ExampleAdapter(getInitData());
        placeRV.layoutManager = LinearLayoutManager(this)
        placeRV.adapter = mExampleAdapter;


        mExampleAdapter.addFooterView(
            LayoutInflater.from(this).inflate(R.layout.footer_view, null).apply {
                setOnClickListener {
                    placeRV.visibility = View.GONE

                    //
                }
            })

        initListener();


    }

    private fun initListener() {


    }

    private fun getInitData(): MutableList<ExampleBean> {
        val mList = ArrayList<ExampleBean>();
        val mExampleBean = ExampleBean()
        mExampleBean.name = "选择起点"
        mList.add(mExampleBean)

        val mExampleBean2 = ExampleBean()
        mExampleBean2.name = "选择目的地"
        mList.add(mExampleBean2)

        return mList;

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
                    //设置谷歌地图自带的图标样式
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                //设置本地资源图标
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_change_history_red_a700_24dp))
            ).showInfoWindow() //显示信息窗口


            setOnPoiClickListener { pointOfInterest: PointOfInterest? ->

                val latLng = pointOfInterest!!.latLng
                val nameResult = pointOfInterest!!.name
                val placeId = pointOfInterest!!.placeId


                var isSelectFirstAndEnd = true
                for (bean in mExampleAdapter.data) {
                    if (bean.name == "选择起点") {
                        isSelectFirstAndEnd = false;
                        bean.name = nameResult;
                        break
                    } else if (bean.name == "选择目的地") {
                        isSelectFirstAndEnd = false;
                        bean.name = nameResult;
                        break
                    }
                }

                if (isSelectFirstAndEnd) {
                    //底部弹窗


                    // mExampleAdapter.addData(ExampleBean().apply { name = nameResult })
                    //更新adapter数据后，需要调接口，获取路线信息
                } else {

                    mExampleAdapter.notifyDataSetChanged()
                }





            }


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