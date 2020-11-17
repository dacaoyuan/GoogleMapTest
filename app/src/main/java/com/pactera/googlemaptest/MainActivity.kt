package com.pactera.googlemaptest

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.*
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Location
import android.os.*
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.model.LocationBias
import com.google.android.libraries.places.api.model.LocationRestriction
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.Autocomplete
import com.google.android.libraries.places.widget.AutocompleteActivity
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode
import com.google.android.material.snackbar.Snackbar
import com.google.maps.android.PolyUtil
import com.google.maps.android.ui.IconGenerator
import com.pactera.googlemaptest.model.LineModule
import com.pactera.googlemaptest.utils.StringUtil
import com.vise.xsnow.http.ViseHttp
import com.vise.xsnow.http.callback.ACallback
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(), OnMapReadyCallback, SensorEventListener {

    companion object {
        private const val TAG = "MainActivitypk"
        private const val AUTOCOMPLETE_REQUEST_CODE = 23487
    }

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mSensorManager: SensorManager // 传感器管理器对象


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getPermission()


        mSensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager;
        // mAccelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)


        initListener();

        //val readJSonFile = FileUtil.readJSonFile(this)
        //Log.i(TAG, "onCreate: readJSonFile=$readJSonFile")

    }


    private fun initListener() {


        tvLocation.setOnClickListener {
            getDeviceLocation()
        }
        tvGetSelectPlace.setOnClickListener {
            startActivity(Intent(this, PlaceActivity::class.java).apply {
                putExtra(
                    "latLng",
                    appointLoc
                )
            })
        }
        tvRealTimeLocation.setOnClickListener {
            if (tvRealTimeLocation.text == "实时定位") {
                tvRealTimeLocation.text = "取消实时定位"
                monitorLocationChange()
            } else {
                tvRealTimeLocation.text = "实时定位"
                if (mLocationCallback != null) {
                    fusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
                }
            }

        }

        tvGet.setOnClickListener {
            getLineInfo();
        }

        tvClearMarker.setOnClickListener {
            checkReadyThen { mGoogleMap.clear() }
        }

        tvCameraZoomIn.setOnClickListener {
            cameraZoomIn()

        }

        imgSearch.setOnClickListener {
            placeSearch();
        }
        tvInstance.setOnClickListener { v: View? ->

            startActivity(Intent(MainActivity@ this, ExampleActivity::class.java))

        }
    }

    //Place 相关 start

    // 使用字段定义要返回的数据类型.
    var placeFields = listOf(
        Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG,
        Place.Field.ADDRESS, Place.Field.ADDRESS_COMPONENTS,
        Place.Field.RATING, Place.Field.USER_RATINGS_TOTAL, Place.Field.VIEWPORT

        //Place.Field.TYPES,Place.Field.PLUS_CODE,  Place.Field.OPENING_HOURS,Place.Field.PHONE_NUMBER,
        //Place.Field.PRICE_LEVEL,Place.Field.UTC_OFFSET,Place.Field.WEBSITE_URI,

        // Place.Field.PHOTO_METADATAS, Place.Field.BUSINESS_STATUS
    )


    val query = "潍坊";
    val countries = listOf("CN", "JP", "US");//国家/地区（例如CH，US，RO）
    val locationBias: LocationBias? = null //设置位置偏差
    lateinit var locationRestriction: LocationRestriction  //设置位置限制
    private fun placeSearch() {
        //val bounds = StringUtil.convertToLatLngBounds("", "")!!
        //locationRestriction = RectangularBounds.newInstance(bounds)

        //获取所有信息
        // placeFields = listOf(*Place.Field.values())


        //AutocompleteActivityMode.OVERLAY
        val autocompleteIntent =
            Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, placeFields)
                .setInitialQuery(query)
                .setHint("请输入您要搜索的内容")
                .setCountries(countries)
                //.setLocationBias(locationBias)
                //.setLocationRestriction(locationRestriction)
                //.setTypeFilter(TypeFilter.ADDRESS) //设置类型过滤器
                .build(this)
        startActivityForResult(autocompleteIntent, AUTOCOMPLETE_REQUEST_CODE)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE && data != null) {
            when (resultCode) {
                AutocompleteActivity.RESULT_OK -> {
                    val place = Autocomplete.getPlaceFromIntent(data!!)
                    val result = StringUtil.stringifyAutocompleteWidget(place, true)
                    Log.i(TAG, "onActivityResult: result=${result}")
                    //Toast.makeText(this, "${result}", Toast.LENGTH_LONG).show()

                    mGoogleMap!!.addMarker(
                        MarkerOptions()
                            .position(place.latLng!!)
                            .title(place.name)
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                            .snippet(place.address)
                    )
                    val viewport = place.viewport
                    mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(viewport, 50))

                    // Log.i(TAG, "onActivityResult: data=${data!!.extras.toString()}")
                }
                AutocompleteActivity.RESULT_ERROR -> {
                    val status = Autocomplete.getStatusFromIntent(data)
                    Log.i(TAG, "onActivityResult: status.statusMessage=${status.statusMessage}")
                }
                AutocompleteActivity.RESULT_CANCELED -> {
                    // The user canceled the operation.
                }
            }
        }


        super.onActivityResult(requestCode, resultCode, data)
    }


    //Place 相关 end
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


    private fun checkReadyThen(stuffToDo: () -> Unit) {
        if (!::mGoogleMap.isInitialized) {
            Toast.makeText(this, "Map is not ready", Toast.LENGTH_SHORT).show()
        } else {
            stuffToDo()
        }
    }

    private lateinit var mGoogleMap: GoogleMap

    //潍坊软件园 LatLng(纬度,经度)
    val strOrigin = "36.70458915541349,119.1854485767212"

    // 航路点
    val strWaypoints1 = "36.699094372900944,119.18357335031034"
    val strWaypoints2 = "36.69567948422026,119.18077178299427"

    //潍坊软件园附近的点
    //val strDestination = "36.70661050620801,119.18400018385316"
    //val strDestination = "36.70658492929346,119.17723931372166"

    //上城国际
    val strDestination = "36.73854236243336,119.07044112682344"


    val appointLoc = LatLng(
        strOrigin.split(",").toTypedArray()[0].toDouble(),
        strOrigin.split(",").toTypedArray()[1].toDouble()
    );


    val destinationLoc =
        LatLng(
            strDestination.split(",").toTypedArray()[0].toDouble(),
            strDestination.split(",").toTypedArray()[1].toDouble()
        )


    override fun onMapReady(googleMap: GoogleMap?) {
        mGoogleMap = googleMap ?: return


        with(googleMap) {
            // 移动地图到指定经度的位置
            moveCamera(CameraUpdateFactory.newLatLngZoom(appointLoc, 15f))


            //添加标记到指定经纬度
            addMarker(
                MarkerOptions()
                    .position(appointLoc)
                    .title("起点")
                    .snippet("$appointLoc")
                //.icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_change_history_red_a700_24dp))
            )

            //添加标记到指定经纬度
            addMarker(
                MarkerOptions()
                    .position(destinationLoc)
                    .title("终点")
                    .snippet("$destinationLoc")
                // .icon(BitmapDescriptorFactory.fromResource(R.mipmap.timg2))
            )

            uiSettings.isZoomControlsEnabled = true
        }

        mGoogleMap.setOnMapClickListener { latLng: LatLng? ->
            Log.i(TAG, "onMapReady: 地图点击事件")


        }

        mGoogleMap.setOnMapLongClickListener { latLng ->
            Log.i(TAG, "onMapReady: 地图长按点击事件")
            //currentSelectMarkerLatLng=latLng;
            mGoogleMap!!.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title("获取附近地点")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                    .snippet("$latLng")


                // .icon(BitmapDescriptorFactory.fromResource(R.mipmap.timg2))
            )

        }


        mGoogleMap.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
            override fun onMarkerClick(mMarker: Marker?): Boolean {
                val position = mMarker!!.position
                Log.i(TAG, "onMarkerClick: position=$position")

                return false
            }

        })

        mGoogleMap.setOnInfoWindowClickListener { mMarker ->
            val position = mMarker!!.position
            Log.i(TAG, "setOnInfoWindowClickListener: position=$position")
            startActivity(Intent(this, NearbySearchActivity::class.java).apply {
                putExtra(
                    "latLng",
                    position
                )
            })

        }




        mGoogleMap.setOnPoiClickListener(object : GoogleMap.OnPoiClickListener {
            override fun onPoiClick(mPointOfInterest: PointOfInterest?) {
                val latLng = mPointOfInterest!!.latLng
                val name = mPointOfInterest.name
                Log.i(TAG, "setOnInfoWindowClickListener: setOnPoiClickListener latLng=$latLng")

                mGoogleMap!!.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title("$name")
                        .snippet("$latLng")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))
                )

                Snackbar.make(tvRealTimeLocation, "$name", Snackbar.LENGTH_LONG).show()


            }

        })


        // getDeviceLocation()

    }

    //private lateinit var currentSelectMarkerLatLng: LatLng


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
     * API支持以下行驶模式的航点：驾驶，步行和骑车；不中转。
     *
     * alternatives：如果设置为 true，则指定“方向”服务可以在响应中提供多个路线选择。
     * 请注意，提供路由选择可能会增加服务器的响应时间。这仅适用于没有中间航路点的请求。
     *
     * avoid： 表示计算出的路线应避开所指示的特征。highways 表示计算出的路线应避开高速公路。
     *
     * units：指定显示结果时要使用的单位制。有效值在下面的“单位系统”中指定 。
     *
     */
    private fun getLineInfo() {

        ViseHttp.GET("directions/json")
            .tag("tag1")
            .addParam("origin", strOrigin)
            .addParam("destination", strDestination)
            //.addParam("mode", "driving")

            .addParam(
                "waypoints",
                "$strWaypoints2"
            ) //航路点,最大允许数量 waypoints为25，加上起点和终点  $strWaypoints1|$strWaypoints2
            //.addParam("alternatives", "true") //如果设置为 true,这仅适用于没有中间航路点的请求。
            //.addParam("avoid", "tolls|highways|ferries")

            .addParam("destination", strDestination)
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
                    calculateTotalTime(legs)


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


    private fun cameraZoomIn() {

        val centerLoc = LatLng(
            36.71243230293682,
            119.12847775965929
        );

        val sydneyLocation: CameraPosition =
            CameraPosition.Builder().target(centerLoc)
                .zoom(11.5f)
                .bearing(0f)//方位
                .tilt(25f)//倾斜
                .build()

        mGoogleMap.animateCamera(
            CameraUpdateFactory.newCameraPosition(sydneyLocation),
            // CameraUpdateFactory.newLatLngZoom(appointLoc,14f),
            3000,
            object : GoogleMap.CancelableCallback {
                override fun onFinish() {
                    Log.i(TAG, "animateCamera onFinish: ")
                }

                override fun onCancel() {
                    Log.i(TAG, "animateCamera onCancel: ")
                }

            })

    }


    private fun getDeviceLocation() {
        //这行代码，就能发起定位请求
        val selfPermission4 =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (selfPermission4 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ),
                12
            )
            return
        } else {
            Log.i(TAG, "getDeviceLocation: 已有权限")
        }

        mGoogleMap.isMyLocationEnabled = false
        mGoogleMap.uiSettings?.isMyLocationButtonEnabled = true

        val locationResult = fusedLocationProviderClient.lastLocation



        locationResult.addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {


                // Set the map's camera position to the current location of the device.
                var lastKnownLocation = task.result
                if (lastKnownLocation != null) {
                    Log.i(TAG, "getDeviceLocation:  locationResult if" + lastKnownLocation.speed)

                    val currentLocation =
                        LatLng(lastKnownLocation!!.latitude, lastKnownLocation!!.longitude)

                    //calculationDirection(currentLocation,lastKnownLocation)


                    mGoogleMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            currentLocation,
                            17.3f
                        )
                    )

                    if (isFirstLocation) {
                        mPositionMarker = mGoogleMap!!.addMarker(
                            MarkerOptions()
                                .position(currentLocation)
                                .title("我的位置")
                                .rotation(azimuth)
                                .anchor(0f, 0.5f)
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_change_history_red_a700_24dp))
                        )
                    }
                    mPositionMarker!!.position = currentLocation;

                    val circle: Circle = mGoogleMap!!.addCircle(
                        CircleOptions().apply {
                            center(currentLocation)
                            if (isFirstLocation) {
                                radius(100.00)
                                strokeWidth(5f)
                                strokeColor(
                                    ContextCompat.getColor(
                                        this@MainActivity,
                                        R.color.purple
                                    )
                                )
                                fillColor(
                                    ContextCompat.getColor(
                                        this@MainActivity,
                                        R.color.blue_100
                                    )
                                )
                                clickable(true)
                                strokePattern(getSelectedPattern(0))
                                isFirstLocation = false
                            }

                        })


                } else {
                    Log.i(TAG, "getDeviceLocation:  locationResult else")
                }
            } else {
                Log.e(TAG, "Exception: %s", task.exception)
            }
        }

        mGoogleMap!!.setOnMyLocationButtonClickListener {
            //返回 false，这样我们就不会使用该事件，而默认行为仍然会发生
            println("MainActivity.getDeviceLocation  ddd")
            false

        }
        mGoogleMap!!.setOnMyLocationClickListener {
            println("MainActivity.getDeviceLocation setOnMyLocationClickListener")
        }
    }

    override fun onResume() {
        super.onResume()
        val orientationSensor: Sensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION)
        mSensorManager.registerListener(this, orientationSensor, SensorManager.SENSOR_DELAY_NORMAL);

    }

    override fun onPause() {
        super.onPause()
        mSensorManager.unregisterListener(this);
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    var mPositionMarker: Marker? = null
    var azimuth = 0f
    override fun onSensorChanged(event: SensorEvent?) {

        if (event!!.sensor.type == Sensor.TYPE_ORIENTATION) {

        }

        //（需要手机屏幕向上，向下的话南北会反掉）设备绕Z轴旋转，Y轴正方向与地磁北极方向的夹角，顺时针方向为正，范围【0，180】
        azimuth = event!!.values[0] //Sensor.TYPE_GYROSCOPE
        //设备绕X轴旋转的角度，当Z轴向Y轴正方向旋转时为正，反之为负，范围【-180,180】
        val pitch = event!!.values[1]
        //设备绕Y轴旋转的角度，当Z轴向X轴正方向旋转时为负，反之为正，范围【-90,90】
        val roll = event!!.values[2]

        // Log.i(TAG, "onSensorChanged: azimuth=$azimuth pitch=$pitch  roll=$roll")
        Log.i(TAG, "onSensorChanged: azimuth=$azimuth")

        if (mPositionMarker != null) {
            mPositionMarker!!.rotation = azimuth
        }

    }


    private fun calculationDirection(
        currentLocation: LatLng,
        rotate: Float,
        lastKnownLocation: Location
    ) {
        if (mGoogleMap == null) {
            return
        }
        /* val rotateBitmap = BitmapUtil.rotateBitmap(
             BitmapFactory.decodeResource(
                 resources,
                 R.drawable.baseline_change_history_red_a700_24dp
             ),
             rotate
         )
         //添加标记到指定经纬度
         mGoogleMap.addMarker(
             MarkerOptions()
                 .position(appointLoc)
                 .title("起点")
                 .snippet("$appointLoc")
                 .rotation(90f)
                 .flat(true)
                 .icon(BitmapDescriptorFactory.fromBitmap(rotateBitmap))
         )*/


        val arrowBitmap = BitmapFactory.decodeResource(
            resources,
            R.drawable.baseline_change_history_red_a700_24dp
        )

        // 将GeoPoint转换为屏幕像素
        val toScreenLocation: Point = mGoogleMap.projection.toScreenLocation(currentLocation)
        //val screenPts: Point = mGoogleMap.projection.toPixels(currentLocation, null)

        val x = toScreenLocation.x
        val y = toScreenLocation.y
        println("MainActivity.calculationDirection x=$x y=$y")

        val matrix: Matrix = Matrix();
        matrix.postRotate(0f)
        val rotatedBmp: Bitmap = Bitmap.createBitmap(
            arrowBitmap,
            0,
            0,
            arrowBitmap.width,
            arrowBitmap.height,
            matrix,
            true
        )


    }

    var isFirstLocation: Boolean = true

    lateinit var mLocationCallback: LocationCallback

    /**
     *  监听位置变化
     */
    private fun monitorLocationChange() {
        //这行代码，就能发起定位请求
        val selfPermission4 =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if (selfPermission4 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION
                ),
                12
            )
            return
        } else {
            Log.i(TAG, "monitorLocationChange: 已有权限")
        }


        val request = LocationRequest()
        request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        request.setInterval(3000)
        //request.fastestInterval = 3000
        request.fastestInterval = 3000
        request.numUpdates

        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {

                val lastLocation = locationResult?.lastLocation
                if (lastLocation != null) {
                    Log.i(TAG, "onLocationResult: 监听位置变化结果")
                    val curPosition = LatLng(
                        lastLocation.latitude,
                        lastLocation.longitude
                    )
                    mGoogleMap!!.animateCamera(CameraUpdateFactory.newLatLng(curPosition))
                } else {
                    Log.i(TAG, "onLocationResult: 监听位置变化结果 lastLocation is null")
                }

            }
        }

        fusedLocationProviderClient.requestLocationUpdates(
            request,
            mLocationCallback,
            Looper.myLooper()
        )


    }

    override fun onDestroy() {
        super.onDestroy()
        //停止获取位置更新
        if (mLocationCallback != null) {
            fusedLocationProviderClient.removeLocationUpdates(mLocationCallback)
        }
    }


    private fun getSelectedPattern(pos: Int): List<PatternItem>? = patterns[pos].second

    private val PATTERN_DASH_LENGTH = 100
    private val PATTERN_GAP_LENGTH = 200
    private val dot = Dot()
    private val dash = Dash(PATTERN_DASH_LENGTH.toFloat())
    private val gap = Gap(PATTERN_GAP_LENGTH.toFloat())
    private val patternDotted = Arrays.asList(dot, gap)
    private val patternDashed = Arrays.asList(dash, gap)
    private val patternMixed = Arrays.asList(dot, gap, dot, dash, gap)

    // These are the options for stroke patterns
    private val patterns: List<Pair<Int, List<PatternItem>?>> = listOf(
        Pair(R.string.pattern_solid, null),
        Pair(R.string.pattern_dashed, patternDashed),
        Pair(R.string.pattern_dotted, patternDotted),
        Pair(R.string.pattern_mixed, patternMixed)
    )

    private fun decodePoly(encoded: String): List<LatLng>? {
        val poly: MutableList<LatLng> = ArrayList()
        var index = 0
        val len = encoded.length
        var lat = 0
        var lng = 0
        while (index < len) {
            var b: Int
            var shift = 0
            var result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlat = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lat += dlat
            shift = 0
            result = 0
            do {
                b = encoded[index++].toInt() - 63
                result = result or (b and 0x1f shl shift)
                shift += 5
            } while (b >= 0x20)
            val dlng = if (result and 1 != 0) (result shr 1).inv() else result shr 1
            lng += dlng
            val p = LatLng(
                lat.toDouble() / 1E5,
                lng.toDouble() / 1E5
            )
            poly.add(p)
        }
        return poly
    }

    private fun getPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val selfPermission4 =
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            //如果请求此权限，则还必须请求 ACCESS_FINE_LOCATION 和 ACCESS_COARSE_LOCATION权限。只请求此权限无效果。
            if (selfPermission4 != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                    ),
                    12
                )
            }
        }
    }


}


/* val okHttpClient = OkHttpClient()
          val request = Request.Builder()
              .url("https://maps.googleapis.com/maps/api/directions/json?\n" +
                      "origin=Toronto&destination=Montreal\n" +
                      "&avoid=highways &mode=bicycling\n" +
                      "&key=AIzaSyDchbbcoDdhqXF0FDvMbLGf7XG6_2aYvks")
              .get()
              .build()
          val call = okHttpClient.newCall(request)
          call.enqueue(object : Callback {
              override fun onFailure(call: Call, e: IOException) {
                  Log.d("okhttp_error", e.message!!)
              }

              @Throws(IOException::class)
              override fun onResponse(call: Call, response: Response) {
                  println("MainActivity.onResponseddd")

                 // XLog.get().d("okhttp_onRespons==> "+response.body()!!.string())
              }
          })*/
