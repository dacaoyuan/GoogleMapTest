package com.pactera.googlemaptest.model;

import java.util.List;

public class LineModule {


    private String status;

    // 包含一个数组，其中包含有关起点，目的地和航点的地理编码的详细信息。
    private List<GeocodedWaypointsDTO> geocoded_waypoints;
    //包含一组从起点到终点的路线。
    private List<RoutesDTO> routes;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<GeocodedWaypointsDTO> getGeocoded_waypoints() {
        return geocoded_waypoints;
    }

    public void setGeocoded_waypoints(List<GeocodedWaypointsDTO> geocoded_waypoints) {
        this.geocoded_waypoints = geocoded_waypoints;
    }

    public List<RoutesDTO> getRoutes() {
        return routes;
    }

    public void setRoutes(List<RoutesDTO> routes) {
        this.routes = routes;
    }

    public static class GeocodedWaypointsDTO {
        /**
         * geocoder_status : OK
         * place_id : ChIJpTvG15DL1IkRd8S0KlBVNTI
         * types : ["locality","political"]
         */

        //表示由地址解析操作产生的状态代码
        private String geocoder_status;
        private String place_id;

        //表示用于计算路线的地址解析结果的地址类型
        private List<String> types;

        public String getGeocoder_status() {
            return geocoder_status;
        }

        public void setGeocoder_status(String geocoder_status) {
            this.geocoder_status = geocoder_status;
        }

        public String getPlace_id() {
            return place_id;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }
    }

    public static class RoutesDTO {

        //包含的视口边界框 overview_polyline。
        private BoundsDTO bounds;
        private String copyrights;

        //重要：包含一个points 对象，该对象保存路线的编码折线表示。该折线是结果方向的近似（平滑）路径。
        private OverviewPolylineDTO overview_polyline;

        //包含该路线的简短文字说明，适用于从备选方案中命名和消除歧义。
        private String summary;
        //包含一个数组，该数组包含有关给定路线内两个位置之间路线段的信息。
        private List<LegsDTO> legs;

        //包含显示这些方向时要显示的警告数组。您必须自己处理并显示这些警告。
        private List<String> warnings;

        //包含一个数组，该数组指示计算出的路线中任何路标的顺序。
        private List<?> waypoint_order;

        public BoundsDTO getBounds() {
            return bounds;
        }

        public void setBounds(BoundsDTO bounds) {
            this.bounds = bounds;
        }

        public String getCopyrights() {
            return copyrights;
        }

        public void setCopyrights(String copyrights) {
            this.copyrights = copyrights;
        }

        public OverviewPolylineDTO getOverview_polyline() {
            return overview_polyline;
        }

        public void setOverview_polyline(OverviewPolylineDTO overview_polyline) {
            this.overview_polyline = overview_polyline;
        }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public List<LegsDTO> getLegs() {
            return legs;
        }

        public void setLegs(List<LegsDTO> legs) {
            this.legs = legs;
        }

        public List<String> getWarnings() {
            return warnings;
        }

        public void setWarnings(List<String> warnings) {
            this.warnings = warnings;
        }

        public List<?> getWaypoint_order() {
            return waypoint_order;
        }

        public void setWaypoint_order(List<?> waypoint_order) {
            this.waypoint_order = waypoint_order;
        }

        public static class BoundsDTO {
            /**
             * northeast : {"lat":45.5017123,"lng":-73.5672184}
             * southwest : {"lat":43.6532565,"lng":-79.38867700000002}
             */

            private NortheastDTO northeast;
            private SouthwestDTO southwest;

            public NortheastDTO getNortheast() {
                return northeast;
            }

            public void setNortheast(NortheastDTO northeast) {
                this.northeast = northeast;
            }

            public SouthwestDTO getSouthwest() {
                return southwest;
            }

            public void setSouthwest(SouthwestDTO southwest) {
                this.southwest = southwest;
            }

            public static class NortheastDTO {
                /**
                 * lat : 45.5017123
                 * lng : -73.5672184
                 */

                private double lat;
                private double lng;

                public double getLat() {
                    return lat;
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }

                public double getLng() {
                    return lng;
                }

                public void setLng(double lng) {
                    this.lng = lng;
                }
            }

            public static class SouthwestDTO {
                /**
                 * lat : 43.6532565
                 * lng : -79.38867700000002
                 */

                private double lat;
                private double lng;

                public double getLat() {
                    return lat;
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }

                public double getLng() {
                    return lng;
                }

                public void setLng(double lng) {
                    this.lng = lng;
                }
            }
        }

        public static class OverviewPolylineDTO {
            private String points;

            public String getPoints() {
                return points;
            }

            public void setPoints(String points) {
                this.points = points;
            }
        }

        public static class LegsDTO {


            //指示此腿段所覆盖的总距离
            private DistanceDTO distance;
            //表示这条腿的总时长
            private DistanceDTO duration;

            private String end_address;
            //包含这条腿给定目的地的纬度/经度坐标。
            private EndLocationDTO end_location;

            private String start_address;
            //包含该腿原点的纬度/经度坐标。
            private StartLocationDTO start_location;

            //包含这条腿的预计到达时间。仅针对公交路线返回此属性。
            private String arrival_time;
            // 包含指定为Time对象的这条腿的预计出发时间。该 departure_time只适用于公交路线。
            private String departure_time;


            //包含一系列步骤，这些步骤表示有关行程的每个单独步骤的信息。
            private List<StepsDTO> steps;

            private List<?> traffic_speed_entry;
            private List<?> via_waypoint;

            public String getArrival_time() {
                return arrival_time;
            }

            public void setArrival_time(String arrival_time) {
                this.arrival_time = arrival_time;
            }

            public String getDeparture_time() {
                return departure_time;
            }

            public void setDeparture_time(String departure_time) {
                this.departure_time = departure_time;
            }

            public DistanceDTO getDistance() {
                return distance;
            }

            public void setDistance(DistanceDTO distance) {
                this.distance = distance;
            }

            public DistanceDTO getDuration() {
                return duration;
            }

            public void setDuration(DistanceDTO duration) {
                this.duration = duration;
            }

            public String getEnd_address() {
                return end_address;
            }

            public void setEnd_address(String end_address) {
                this.end_address = end_address;
            }

            public EndLocationDTO getEnd_location() {
                return end_location;
            }

            public void setEnd_location(EndLocationDTO end_location) {
                this.end_location = end_location;
            }

            public String getStart_address() {
                return start_address;
            }

            public void setStart_address(String start_address) {
                this.start_address = start_address;
            }

            public StartLocationDTO getStart_location() {
                return start_location;
            }

            public void setStart_location(StartLocationDTO start_location) {
                this.start_location = start_location;
            }

            public List<StepsDTO> getSteps() {
                return steps;
            }

            public void setSteps(List<StepsDTO> steps) {
                this.steps = steps;
            }

            public List<?> getTraffic_speed_entry() {
                return traffic_speed_entry;
            }

            public void setTraffic_speed_entry(List<?> traffic_speed_entry) {
                this.traffic_speed_entry = traffic_speed_entry;
            }

            public List<?> getVia_waypoint() {
                return via_waypoint;
            }

            public void setVia_waypoint(List<?> via_waypoint) {
                this.via_waypoint = via_waypoint;
            }

            public static class DistanceDTO {
                /**
                 * text : 598 公里
                 * value : 598021
                 */

                private String text;
                private int value;

                public String getText() {
                    return text;
                }

                public void setText(String text) {
                    this.text = text;
                }

                public int getValue() {
                    return value;
                }

                public void setValue(int value) {
                    this.value = value;
                }
            }

            public static class EndLocationDTO {
                /**
                 * lat : 45.5017123
                 * lng : -73.5672184
                 */

                private double lat;
                private double lng;

                public double getLat() {
                    return lat;
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }

                public double getLng() {
                    return lng;
                }

                public void setLng(double lng) {
                    this.lng = lng;
                }
            }

            public static class StartLocationDTO {
                /**
                 * lat : 43.6532565
                 * lng : -79.38303979999999
                 */

                private double lat;
                private double lng;

                public double getLat() {
                    return lat;
                }

                public void setLat(double lat) {
                    this.lat = lat;
                }

                public double getLng() {
                    return lng;
                }

                public void setLng(double lng) {
                    this.lng = lng;
                }
            }

            public static class StepsDTO {
                /**
                 * distance : {"text":"0.2 公里","value":154}
                 * duration : {"text":"2分钟","value":140}
                 * end_location : {"lat":43.6520699,"lng":-79.3829136}
                 * html_instructions : 向<b>南</b>前行<div style="font-size:0.9em">推车步行</div>
                 * polyline : {"points":"{_miG~nocNv@U`@MdA[`@MNEBA@?@A@@@??@@B@B?@Hj@@B"}
                 * start_location : {"lat":43.6532565,"lng":-79.38303979999999}
                 * travel_mode : BICYCLING
                 * maneuver : turn-sharp-right
                 */

                //包含此步骤到下一步为止的距离。
                private DistanceDTO distance;

                private DistanceDTO duration;


                //包含此步骤的格式化指令，以HTML文本字符串形式显示。
                private String html_instructions;

                private OverviewPolylineDTO polyline;

                //包含此步骤起点的位置，以lat和的形式表示 为一组lng。
                private StartLocationDTO start_location;

                //包含此步骤的最后一个点的位置，作为一个单一的组lat和 lng领域。
                private EndLocationDTO end_location;

                private String travel_mode;

                //包含当前步骤要执行的操作（向左转，合并，笔直等）。
                private String maneuver;

                //包含公交特定信息。
                private String transit_details;

                public String getTransit_details() {
                    return transit_details;
                }

                public void setTransit_details(String transit_details) {
                    this.transit_details = transit_details;
                }

                public DistanceDTO getDistance() {
                    return distance;
                }

                public void setDistance(DistanceDTO distance) {
                    this.distance = distance;
                }

                public DistanceDTO getDuration() {
                    return duration;
                }

                public void setDuration(DistanceDTO duration) {
                    this.duration = duration;
                }

                public EndLocationDTO getEnd_location() {
                    return end_location;
                }

                public void setEnd_location(EndLocationDTO end_location) {
                    this.end_location = end_location;
                }

                public String getHtml_instructions() {
                    return html_instructions;
                }

                public void setHtml_instructions(String html_instructions) {
                    this.html_instructions = html_instructions;
                }

                public OverviewPolylineDTO getPolyline() {
                    return polyline;
                }

                public void setPolyline(OverviewPolylineDTO polyline) {
                    this.polyline = polyline;
                }

                public StartLocationDTO getStart_location() {
                    return start_location;
                }

                public void setStart_location(StartLocationDTO start_location) {
                    this.start_location = start_location;
                }

                public String getTravel_mode() {
                    return travel_mode;
                }

                public void setTravel_mode(String travel_mode) {
                    this.travel_mode = travel_mode;
                }

                public String getManeuver() {
                    return maneuver;
                }

                public void setManeuver(String maneuver) {
                    this.maneuver = maneuver;
                }

                public static class EndLocationDTO {
                    /**
                     * lat : 43.6520699
                     * lng : -79.3829136
                     */

                    private double lat;
                    private double lng;

                    public double getLat() {
                        return lat;
                    }

                    public void setLat(double lat) {
                        this.lat = lat;
                    }

                    public double getLng() {
                        return lng;
                    }

                    public void setLng(double lng) {
                        this.lng = lng;
                    }
                }

                public static class StartLocationDTO {
                    /**
                     * lat : 43.6532565
                     * lng : -79.38303979999999
                     */

                    private double lat;
                    private double lng;

                    public double getLat() {
                        return lat;
                    }

                    public void setLat(double lat) {
                        this.lat = lat;
                    }

                    public double getLng() {
                        return lng;
                    }

                    public void setLng(double lng) {
                        this.lng = lng;
                    }
                }
            }
        }
    }
}
