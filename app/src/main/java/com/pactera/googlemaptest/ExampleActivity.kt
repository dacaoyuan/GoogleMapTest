package com.pactera.googlemaptest

import android.app.ProgressDialog
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FetchPlaceResponse
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.android.PolyUtil
import com.google.maps.android.ui.IconGenerator
import com.pactera.googlemaptest.adapter.ExampleAdapter
import com.pactera.googlemaptest.model.ExampleBean
import com.pactera.googlemaptest.model.LineModule
import com.shehuan.nicedialog.BaseNiceDialog
import com.shehuan.nicedialog.NiceDialog
import com.shehuan.nicedialog.ViewConvertListener
import com.shehuan.nicedialog.ViewHolder
import com.vise.xsnow.http.ViseHttp
import com.vise.xsnow.http.callback.ACallback
import kotlinx.android.synthetic.main.activity_example.*

class ExampleActivity : AppCompatActivity(), OnMapReadyCallback {

    private val TAG = "ExampleActivity"
    private lateinit var mGoogleMap: GoogleMap

    //潍坊软件园 LatLng(纬度,经度)
    // val strOrigin = "36.70458915541349,119.1854485767212"

    //富华游乐园
    val strOrigin = "36.71525382744859,119.16037559509276"

    private lateinit var mExampleAdapter: ExampleAdapter
    private lateinit var placesClient: PlacesClient
    val appointLoc = LatLng(
        strOrigin.split(",").toTypedArray()[0].toDouble(),
        strOrigin.split(",").toTypedArray()[1].toDouble()
    );

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_example)

        placesClient = Places.createClient(this)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)

        mExampleAdapter = ExampleAdapter(getInitData());
        placeRV.layoutManager = LinearLayoutManager(this)
        placeRV.adapter = mExampleAdapter;


        /* mExampleAdapter.addFooterView(
             LayoutInflater.from(this).inflate(R.layout.footer_view, null).apply {
                 setOnClickListener {
                     var isSelectFirstAndEnd = true
                     for (bean in mExampleAdapter.data) {
                         if (bean.name == "选择起点") {
                             isSelectFirstAndEnd = false;
                             break
                         } else if (bean.name == "选择目的地") {
                             isSelectFirstAndEnd = false;
                             break
                         }
                     }

                     if (isSelectFirstAndEnd) {
                         placeRV.visibility = View.GONE
                     } else {
                         Toast.makeText(this@ExampleActivity, "请选选择起点和目的地", Toast.LENGTH_LONG).show()
                     }


                 }
             })
 */
        initListener();

        initProgressDialog()
    }

    private fun initListener() {

        mExampleAdapter.setOnItemChildClickListener { adapter, view, position ->
            mExampleAdapter.remove(position)
            if (mExampleAdapter.data.size > 2) {
                getLineInfo(true)
            } else {
                getLineInfo(false)
            }

        }


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


                //1：这里我们根据 placeId 获取 地点信息
                getPlaceInfo(placeId)


            }


        }

    }

    private var mProgressDialog: ProgressDialog? = null

    public fun initProgressDialog() {
        mProgressDialog = ProgressDialog(this)
        mProgressDialog!!.setMessage("加载中...")
    }

    public fun showProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog!!.show()
        }
    }

    public fun dismissProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog!!.dismiss()
        }
    }

    //根据placeId，可以获取 地点 和 照片 等相关信息
    private fun getPlaceInfo(placeId: String) {
        showProgressDialog();

        //获取所有信息
        val placeFields = listOf(*Place.Field.values())


        val request = FetchPlaceRequest.newInstance(placeId, placeFields)
        val placeTask = placesClient.fetchPlace(request)
        placeTask.addOnSuccessListener { response: FetchPlaceResponse ->
            /*Log.i(TAG, "getPlaceInfo: addOnSuccessListener response=$response")
            val address = response.place.address
            val name = response.place.name
            val latLng = response.place.latLng*/

            dialogBottom(response);

        }
        placeTask.addOnFailureListener { exception: Exception ->
            exception.printStackTrace()
            Log.i(TAG, "getPlaceInfo: exception.message=${exception.message}")
            dismissProgressDialog()
        }
        placeTask.addOnCompleteListener {
            Log.i(TAG, "getPlaceInfo: addOnCompleteListener")
            dismissProgressDialog()
        }


    }


    private fun dialogBottom(response: FetchPlaceResponse) {
        NiceDialog.init()
            .setLayoutId(R.layout.popup_select_place)
            .setConvertListener(object : ViewConvertListener() {
                public override fun convertView(
                    holder: ViewHolder,
                    dialog: BaseNiceDialog
                ) {
                    val place = response.place

                    val nameResult = place.name!!
                    val latLng = place.latLng!!
                    holder.setText(R.id.tvName, place.name)
                    holder.setText(R.id.tvAddress, "地址：" + place.address)
                    holder.setText(R.id.tvscore, "评分：" + place.rating)

                    var isSelectFirstAndEnd = 0
                    for (bean in mExampleAdapter.data) {
                        if (bean.name == "选择起点") {
                            isSelectFirstAndEnd = 1;
                            bean.name = nameResult;
                            bean.mLatLng = latLng
                            holder.setText(R.id.btnAdd, "添加起点")
                            break
                        } else if (bean.name == "选择目的地") {
                            isSelectFirstAndEnd = 2;
                            bean.name = nameResult;
                            bean.mLatLng = latLng
                            holder.setText(R.id.btnAdd, "添加目的地")
                            break
                        }
                    }


                    holder.setOnClickListener(R.id.btnAdd) {
                        //  placeRV.visibility = View.VISIBLE

                        if (isSelectFirstAndEnd == 1) {
                            mExampleAdapter.notifyDataSetChanged()
                        } else if (isSelectFirstAndEnd == 2) {
                            mExampleAdapter.notifyDataSetChanged()
                            //更新adapter数据后，需要调接口，然后在获取路线信息
                            getLineInfo(hasWaypoints = false);
                        } else {
                            mGoogleMap.addMarker(
                                MarkerOptions()
                                    .position(latLng)
                                    .title(nameResult)
                                    .icon(
                                        BitmapDescriptorFactory.defaultMarker(
                                            BitmapDescriptorFactory.HUE_VIOLET
                                        )
                                    )
                            )

                            val addPosition = mExampleAdapter.data.size - 1
                            mExampleAdapter.addData(addPosition, ExampleBean().apply {
                                name = nameResult
                                mLatLng = latLng
                            })
                            mExampleAdapter.notifyDataSetChanged()
                            //更新adapter数据后，需要调接口，然后在获取路线信息
                            getLineInfo(hasWaypoints = true);

                        }


                        dialog.dismiss()
                    }


                }
            })
            .setGravity(Gravity.BOTTOM)
            .show(supportFragmentManager)

    }

    //去掉首尾数据，中间的数据，就是途经点数据
    private fun processwaypointsData(mlist: MutableList<ExampleBean>): String {
        val mlist = mlist.subList(1, mlist.size - 1)

        var strwaypointsData = "";
        for (bean in mlist) {
            val mLatLng = bean.mLatLng
            strwaypointsData += "${mLatLng!!.latitude},${mLatLng!!.longitude}|"
        }
        strwaypointsData = strwaypointsData.substring(0, strwaypointsData.length - 1)

        Log.i(TAG, "processwaypointsData: strwaypointsData=$strwaypointsData")
        return strwaypointsData
    }


    /**
     * 获取线路信息
     *
     * origin，destination :可是以下几种：位置ID，地址，字符串经纬度
     *
     * mode: driving  (默认）表示使用道路网络的标准行车路线。
     *       walking
     *       bicycling
     *       transit  通过公共交通路线（如果有）请求路线。如果将模式设置为transit，则可以选择指定departure_time或 arrival_time。
     *       如果未指定任何时间，则 departure_time默认为现在（即，出发时间默认为当前时间）。
     *       您还可以选择包含 transit_mode和和/或 transit_routing_preference。
     *
     * arrival_time：指定公交路线的理想到达时间
     * departure_time：指定所需的出发时间。
     *
     * waypoints：指定要在起点和终点之间的路径上包括的直通或中途停留位置的中间位置数组。航点通过将路线引导通过指定位置来更改路线
     * API支持以下行驶模式的航点：驾驶，步行和骑车；不中转。 例如：strWaypoints1|strWaypoints2|strWaypoints3
     *
     * alternatives：如果设置为 true，则指定“方向”服务可以在响应中提供多个路线选择。
     * 请注意，提供路由选择可能会增加服务器的响应时间。这仅适用于没有中间航路点的请求。
     *
     * avoid： 表示计算出的路线应避开所指示的特征。highways 表示计算出的路线应避开高速公路。
     *
     * units：指定显示结果时要使用的单位制。有效值在下面的“单位系统”中指定 。
     *
     */
    private fun getLineInfo(hasWaypoints: Boolean) {
        mGoogleMap.clear()

        var origin: LatLng? = null;
        var destination: LatLng? = null;

        for (i in mExampleAdapter.data.indices) {
            if (i == 0) {
                origin = mExampleAdapter.data[i].mLatLng
                mGoogleMap.addMarker(
                    MarkerOptions()
                        .position(origin!!)
                        .title(mExampleAdapter.data[i].name)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                )
            } else if (i == mExampleAdapter.data.size - 1) {
                destination = mExampleAdapter.data[i].mLatLng
                mGoogleMap.addMarker(
                    MarkerOptions()
                        .position(destination!!)
                        .title(mExampleAdapter.data[i].name)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                )
            } else {
                mGoogleMap.addMarker(
                    MarkerOptions()
                        .position(mExampleAdapter.data[i].mLatLng!!)
                        .title(mExampleAdapter.data[i].name)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                )
            }

        }

        var waypointsData: String? = null
        if (hasWaypoints) {
            waypointsData = processwaypointsData(mExampleAdapter.data);
        }


        val strOrigin = "${origin!!.latitude},${origin.longitude}"
        val strDestination = "${destination!!.latitude},${destination.longitude}"


        ViseHttp.GET("directions/json")
            .tag("tag1")
            .addParam("origin", strOrigin)
            .addParam("destination", strDestination)
            //.addParam("mode", "driving")

            .addParam(
                "waypoints",
                waypointsData
            ) //航路点,最大允许数量 waypoints为25，加上起点和终点  $strWaypoints1|$strWaypoints2
            //.addParam("alternatives", "true") //如果设置为 true,这仅适用于没有中间航路点的请求。
            //.addParam("avoid", "tolls|highways|ferries")

            .addParam("key", Config.API_KAY)


            .request(object : ACallback<LineModule?>() {
                override fun onSuccess(mBean: LineModule?) {
                    //请求成功，AuthorModel为解析服务器返回数据的对象，可以是String，按需定义即可
                    dealWithData(mBean)
                }

                override fun onFail(errCode: Int, errMsg: String) {
                    //请求失败，errCode为错误码，errMsg为错误描述
                    Log.i(TAG, " errCode=${errCode} errMsg=$errMsg")
                }
            })
    }

    private fun dealWithData(mBean: LineModule?) {
        val status = mBean!!.status
        if (status == "OK") {

            //包含一个数组，其中包含有关起点，目的地和航点的地理编码的详细信息。
            val geocodedWaypoints = mBean!!.geocoded_waypoints
            Log.i(TAG, "geocodedWaypoints.size=${geocodedWaypoints.size}")


            val routes = mBean!!.routes //路线 测试发现：第一条数据就是最优的线路
            if (routes != null && routes.size > 0) {
                Log.i(TAG, "routes.size=${routes.size}")

                var routesNumber = 0;

                routes.forEach {
                    routesNumber++;

                    //包含的视口边界框 overview_polyline。
                    val bounds = it.bounds

                    routesGlobalPreview(bounds);


                    //包含一个points 对象，该对象保存路线的编码折线表示。该折线是结果方向的近似（平滑）路径。
                    val overviewPolyline = it.overview_polyline

                    val points = overviewPolyline.points
                    Log.i(TAG, "points=$points")


                    // val mLatLngList: MutableList<LatLng> = decodePoly(points) as MutableList<LatLng>

                    val mLatLngList: MutableList<LatLng> = PolyUtil.decode(points)
                    Log.i(TAG, " mLatLngList.size=${mLatLngList.size}")

                    showLine(mLatLngList, routesNumber);


                    //包含一个数组，该数组包含有关给定路线内两个位置之间路线段的信息。对于指定的每个航点或目的地，将显示一条单独的航段。
                    // （没有航路点的路线将在legs阵列中仅包含一条腿。）每条腿由一系列组成steps。（请参见下面的“ 指导腿”。）
                    val legs = it.legs
                    Log.i(TAG, "legs.size=${legs.size}")
                    //总时间显示处理
                    // calculateTotalTime(legs)


                    // 包含该路线的简短文字说明，适用于对路线进行命名和消除歧义。
                    val summary = it.summary


                    //包含显示这些方向时要显示的警告数组。您必须自己处理并显示这些警告。
                    val warnings = it.warnings


                    //包含要为此路线显示的版权文本。您必须自己处理和显示此信息。
                    val copyrights = it.copyrights


                    //包含一个数组，该数组指示计算出的路线中任何路标的顺序。如果请求是optimize:true在其waypoints参数内 传递的，则此路点可能会重新排序。
                    val waypointOrder = it.waypoint_order


                }
            } else {
                Log.i(TAG, " routes is null")
            }


        } else {
            if (status == "ZERO_RESULTS") {
                Log.i(TAG, " status 在起点和终点之间找不到路由")
            } else {
                Log.i(TAG, " status=${status}")
            }


        }
    }

    private fun showLine(mLatLngList: MutableList<LatLng>, routesNumber: Int) {

        val mPolylineOptions = PolylineOptions();
        if (routesNumber == 1) {
            mPolylineOptions
                .addAll(mLatLngList)
                .color(Color.GREEN)
                .width(30f)
        } else {
            mPolylineOptions
                .addAll(mLatLngList)
                .color(Color.GRAY)
                .width(30f)
        }




        mGoogleMap!!.addPolyline(mPolylineOptions)


    }

    //总时间显示处理
    private fun calculateTotalTime(legs: List<LineModule.RoutesDTO.LegsDTO>) {


        for (index in legs.indices) {
            val legsDTO = legs[index]

            val value = legsDTO.duration.value
            Log.i(TAG, "calculateTotalTime value=${value}")


            val text = legsDTO.duration.text
            Log.i(TAG, "calculateTotalTime text=${text}")

            val steps = legsDTO.steps
            Log.i(TAG, "calculateTotalTime steps.size=${steps.size}")
            if (steps != null && steps.size > 0) {

                val j = Math.max(steps.size / 2, 1)

                val endLocation = steps[j].end_location
                addInfoWindowsToRoutes(LatLng(endLocation.lat, endLocation.lng), text);
            }

            if (index != legs.size - 1) {
                val endAddressLegs = legsDTO.end_address
                Log.i(TAG, "calculateTotalTime endAddressLegs=${endAddressLegs}")
                val endLocationLegs = legsDTO.end_location
                mGoogleMap.addMarker(
                    MarkerOptions()
                        .position(LatLng(endLocationLegs.lat, endLocationLegs.lng))
                        .title("途径地点")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_commute_red_400_24dp))

                )
            }

        }

    }


    /**
     * 线路全局预览
     */
    private fun routesGlobalPreview(mBoundsDTO: LineModule.RoutesDTO.BoundsDTO) {
        val northeast = mBoundsDTO.northeast
        val southwest = mBoundsDTO.southwest

        val boundsBuilder = LatLngBounds.Builder()
        boundsBuilder.include(LatLng(northeast.lat, northeast.lng));
        boundsBuilder.include(LatLng(southwest.lat, southwest.lng));

        val bounds = boundsBuilder.build()

        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50))


    }


    /**
     * 将信息窗口添加到路线
     */
    private fun addInfoWindowsToRoutes(mLatLng: LatLng, text: String) {
        val iconFactory = IconGenerator(this)
        //iconFactory.setStyle(IconGenerator.STYLE_RED)
        val makeIcon: Bitmap = iconFactory.makeIcon(text)
        val mBitmapDescriptor: BitmapDescriptor = BitmapDescriptorFactory.fromBitmap(makeIcon)

        mGoogleMap.addMarker(
            MarkerOptions()
                .position(mLatLng)
                .icon(mBitmapDescriptor)
                .anchor(iconFactory.anchorU, iconFactory.anchorV)
        )
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