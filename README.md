
[toc]
# 谷歌地图–集成准备

由于众所周知的的原因，集成谷歌地图sdk前首先你的pc端和移动端都是要翻墙的，不然后续的一些功能你都无法操作。

## pc端准备：
翻墙后，你才能正常访问谷歌地图控制台。由于谷歌文档和控制台全是英文，大家不要担心，给你自己的浏览器下载个翻译插件，直接翻译网页就可以啦。翻译后的效果：
![](https://img-blog.csdnimg.cn/20201116202453581.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2RhX2Nhb3l1YW4=,size_16,color_FFFFFF,t_70#pic_center)

如上图一样，我们首先需要获取API的秘钥，进入控制台按照文档说明，去一步步申请就可以喽。**下面重点来啦：**
申请好后，你可以选择对apikey进行限制，这里建议可先不做任何限制，然后记得进入到这里：
![](https://img-blog.csdnimg.cn/20201116202919903.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2RhX2Nhb3l1YW4=,size_16,color_FFFFFF,t_70#pic_center)

启用api相关的服务：
![](https://img-blog.csdnimg.cn/20201116203001394.png#pic_center)

这里建议先把这些全部都给启用吧，正式使用后在根据实际情况有选择的启用。
![](https://img-blog.csdnimg.cn/20201116203039223.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2RhX2Nhb3l1YW4=,size_16,color_FFFFFF,t_70#pic_center)



## 手机端准备：
翻墙后谷歌服务相关软件，都要安装一下。如图：
<img src="https://img-blog.csdnimg.cn/20201116201320205.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2RhX2Nhb3l1YW4=,size_16,color_FFFFFF,t_70" width = "400" height = "500" alt="" align="center" />

也可以在豌豆荚应用商店搜索“谷歌安装器”进行一键安装，如图：
<img src="https://img-blog.csdnimg.cn/2020111620104081.jpg?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2RhX2Nhb3l1YW4=,size_16,color_FFFFFF,t_70" width = "550" height = "450" alt="" align="center" />


# 谷歌地图–MapsSDK集成
相信经过 `谷歌地图–集成准备`，一些准备工作你基本差不多了，api_key也申请好了。

## 依赖添加
```` kotlin
 implementation 'com.google.android.gms:play-services-maps:17.0.0'
````
## 基本地图展示
布局文件：
```` kotlin
 <fragment
        android:id="@+id/map"
        android:name="com.google.android.gms.maps.SupportMapFragment"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

````
关键代码：
```` kotlin
class ExampleActivity : AppCompatActivity(), OnMapReadyCallback {

    private val TAG = "ExampleActivity"
    private lateinit var mGoogleMap: GoogleMap

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
                     //设置谷歌地图自带的图标样式
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
                     //设置本地资源图标
                    //.icon(BitmapDescriptorFactory.fromResource(R.drawable.baseline_change_history_red_a700_24dp))
            ).showInfoWindow() //显示信息窗口


        }

    }



}

````

## 定位
如果想添加定位功能的话，还需要添加这行依赖：
```` kotlin
implementation 'com.google.android.libraries.places:places:2.4.0'
````
关键代码：
```` kotlin
private fun getDeviceLocation() {
      
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
       
       
        mGoogleMap.isMyLocationEnabled = true
        mGoogleMap.uiSettings?.isMyLocationButtonEnabled = true

        //这行代码，就能发起定位请求
        val locationResult = fusedLocationProviderClient.lastLocation

        //监听定位结果
        locationResult.addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {


                // Set the map's camera position to the current location of the device.
                var lastKnownLocation = task.result
                if (lastKnownLocation != null) {
                    Log.i(TAG, "getDeviceLocation:  locationResult if")

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
     
    }

````

## 地图点击事件
```` kotlin
       mGoogleMap.setOnMapClickListener { latLng: LatLng? ->
            Log.i(TAG, "onMapReady: 地图点击事件")


        }

        mGoogleMap.setOnMapLongClickListener { latLng ->
            Log.i(TAG, "onMapReady: 地图长按点击事件")
        
        }


        mGoogleMap.setOnMarkerClickListener(object : GoogleMap.OnMarkerClickListener {
            override fun onMarkerClick(mMarker: Marker?): Boolean {
                Log.i(TAG, "onMapReady: Marker点击事件")
               
                return false
            }

        })

        mGoogleMap.setOnInfoWindowClickListener { mMarker ->
            Log.i(TAG, "onMapReady: Marker 弹窗点击事件")
         
        }


        mGoogleMap.setOnPoiClickListener(object : GoogleMap.OnPoiClickListener {
            override fun onPoiClick(mPointOfInterest: PointOfInterest?) {
                Log.i(TAG, "onMapReady: Poi 点击事件")
             
            }
        })

````

# 谷歌地图–DirectionsSDK集成

集成 Directions 功能时，需要你的google地图开发者账号为你的项目创建结算账户，来启用计费功能。而启用计费功能，就需要绑定一个国际的信用卡。
截图：
![](https://img-blog.csdnimg.cn/20201125102211224.png?x-oss-process=image/watermark,type_ZmFuZ3poZW5naGVpdGk,shadow_10,text_aHR0cHM6Ly9ibG9nLmNzZG4ubmV0L2RhX2Nhb3l1YW4=,size_16,color_FFFFFF,t_70#pic_center)

## 线路规划

效果图：
![](https://img-blog.csdnimg.cn/20201119145529619.gif#pic_center)


首先明确一点，获取这些路线信息，不像集成国内的地图。谷歌地图而是一个http get请求。链接如下：
````
https://maps.googleapis.com/maps/api/directions/json?origin=Toronto&destination=Montreal&avoid=highways&mode=bicycling&key=your_api_kay
````
具体传递参数的意义大家可仔细阅读[官方文档](https://developers.google.com/maps/documentation/directions/overview)，这里也贴出关键代码：
```` kotlin
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
````
解析并处理的关键代码：
```` kotlin
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

````


# 谷歌地图--PlacesSDK集成

## 依赖添加
```` kotlin
implementation 'com.google.android.libraries.places:places:2.4.0'
````
初始化：
```` kotlin
public class InitApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ViseHttp.init(this);
   
        if (!Places.isInitialized()) {
            Places.initialize(applicationContext, Config.API_KAY)
        }

    }
}

````
## 当前位置获取附近地点
```` kotlin
   // 使用字段定义要返回的数据类型.
        val placeFields = listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)

            // Use the builder to create a FindCurrentPlaceRequest.
            val request = FindCurrentPlaceRequest.newInstance(placeFields)

            // 获取可能的位置-即与设备当前位置最匹配的商家和其他兴趣点。
            val placeResult = placesClient.findCurrentPlace(request)
            placeResult.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    val likelyPlaces = task.result

                  
                } else {
                    Log.e(TAG, "Exception: %s", task.exception)
                }
            }

````

## 地点搜索

### 官方搜索组件
关键代码：
```` kotlin
 val query = "潍坊";
    val countries = listOf("CN", "JP", "US");//国家/地区（例如CH，US，RO）
    val locationBias: LocationBias? = null //设置位置偏差
    lateinit var locationRestriction: LocationRestriction  //设置位置限制
    private fun placeSearch() {
        //val bounds = StringUtil.convertToLatLngBounds("", "")!!
        //locationRestriction = RectangularBounds.newInstance(bounds)

        //获取所有信息
         placeFields = listOf(*Place.Field.values())

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

````

很显然我们这里启动了一个活动，然后我们在onActivityResult中获取结果值：
```` kotlin
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

````

## 获取地点和照片相关信息

关键代码：
```` kotlin
  //根据placeId，可以获取 地点 和 照片 等相关信息
    private fun getPlaceInfo(placeId: String) {
        val argsd = arrayOf("1", "2", "3")
        val argsd1 = arrayOf(1, 2, 3)


        //获取所有信息
        //placeFields = listOf(*Place.Field.values())


        val request = FetchPlaceRequest.newInstance(placeId, placeFields)
        val placeTask = placesClient.fetchPlace(request)
        placeTask.addOnSuccessListener { response: FetchPlaceResponse ->
            Log.i(TAG, "getPlaceInfo: addOnSuccessListener response=$response")
            val address = response.place.address
            val name = response.place.name
            val latLng = response.place.latLng
            Log.i(TAG, "getPlaceInfo: addOnSuccessListener address=$address")
            //Toast.makeText(this, "${response}", Toast.LENGTH_LONG).show()

            selectMarker = mGoogleMap!!.addMarker(
                MarkerOptions()
                    .position(latLng!!)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW))
            )
            selectMarker!!.title = "$name"
            selectMarker!!.snippet = "评分：${response.place.rating}    总评分：${response.place.userRatingsTotal}"
            selectMarker!!.showInfoWindow()

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

````


# [源码](https://github.com/dacaoyuan/GoogleMapTest)

# 参考博客
- [android-samples](https://github.com/googlemaps/android-samples)
- [android-places-demos](https://github.com/googlemaps/android-places-demos)
- [android-maps-utils](https://github.com/googlemaps/android-maps-utils)
- [拾取坐标系统](https://api.map.baidu.com/lbsapi/getpoint/index.html)



​                                                                                                                                                             撰写人：袁培凯

