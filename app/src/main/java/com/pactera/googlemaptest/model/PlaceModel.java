package com.pactera.googlemaptest.model;

import java.util.List;

public class PlaceModel {

    /**
     * plus_code : {"compound_code":"P54G+PJ 中国山东省潍坊市奎文区","global_code":"8P8XP54G+PJ"}
     * results : [{"address_components":[{"long_name":"2370","short_name":"2370","types":["street_number"]},{"long_name":"胜利东街","short_name":"胜利东街","types":["route"]},{"long_name":"奎文区","short_name":"奎文区","types":["political","sublocality","sublocality_level_1"]},{"long_name":"潍坊市","short_name":"潍坊市","types":["locality","political"]},{"long_name":"山东省","short_name":"山东省","types":["administrative_area_level_1","political"]},{"long_name":"中国","short_name":"CN","types":["country","political"]},{"long_name":"261041","short_name":"261041","types":["postal_code"]}],"formatted_address":"中国山东省潍坊市奎文区胜利东街2370号 邮政编码: 261041","geometry":{"location":{"lat":36.706533,"lng":119.1766979},"location_type":"ROOFTOP","viewport":{"northeast":{"lat":36.7078819802915,"lng":119.1780468802915},"southwest":{"lat":36.7051840197085,"lng":119.1753489197085}}},"place_id":"ChIJFwRdCV0alTUR11hIbrPq688","plus_code":{"compound_code":"P54G+JM 中国山东省潍坊市奎文区","global_code":"8P8XP54G+JM"},"types":["street_address"]},{"address_components":[{"long_name":"胜利东街","short_name":"胜利东街","types":["route"]},{"long_name":"奎文区","short_name":"奎文区","types":["political","sublocality","sublocality_level_1"]},{"long_name":"潍坊市","short_name":"潍坊市","types":["locality","political"]},{"long_name":"山东省","short_name":"山东省","types":["administrative_area_level_1","political"]},{"long_name":"中国","short_name":"CN","types":["country","political"]}],"formatted_address":"中国山东省潍坊市奎文区胜利东街","geometry":{"bounds":{"northeast":{"lat":36.706741,"lng":119.1780646},"southwest":{"lat":36.70669,"lng":119.1764407}},"location":{"lat":36.7067155,"lng":119.1772527},"location_type":"GEOMETRIC_CENTER","viewport":{"northeast":{"lat":36.7080644802915,"lng":119.1786016302915},"southwest":{"lat":36.70536651970851,"lng":119.1759036697085}}},"place_id":"ChIJSeT5tV0alTURnCQj8jpEcnQ","types":["route"]},{"address_components":[{"long_name":"奎文区","short_name":"奎文区","types":["political","sublocality","sublocality_level_1"]},{"long_name":"潍坊市","short_name":"潍坊市","types":["locality","political"]},{"long_name":"山东省","short_name":"山东省","types":["administrative_area_level_1","political"]},{"long_name":"中国","short_name":"CN","types":["country","political"]}],"formatted_address":"中国山东省潍坊市奎文区","geometry":{"bounds":{"northeast":{"lat":36.769463,"lng":119.3059041},"southwest":{"lat":36.6081366,"lng":119.0960672}},"location":{"lat":36.70759,"lng":119.132482},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":36.769463,"lng":119.3059041},"southwest":{"lat":36.6081366,"lng":119.0960672}}},"place_id":"ChIJb5KDQQwalTURugMXw6aS7mc","types":["political","sublocality","sublocality_level_1"]},{"address_components":[{"long_name":"潍坊市","short_name":"潍坊市","types":["locality","political"]},{"long_name":"山东省","short_name":"山东省","types":["administrative_area_level_1","political"]},{"long_name":"中国","short_name":"CN","types":["country","political"]}],"formatted_address":"中国山东省潍坊市","geometry":{"bounds":{"northeast":{"lat":37.3044663,"lng":120.0152097},"southwest":{"lat":35.709407,"lng":118.1762844}},"location":{"lat":36.706962,"lng":119.161748},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":37.3044663,"lng":120.0152097},"southwest":{"lat":35.709407,"lng":118.1762844}}},"place_id":"ChIJucTy2H8XlTURjUUy5ADfki0","types":["locality","political"]},{"address_components":[{"long_name":"山东省","short_name":"山东省","types":["administrative_area_level_1","political"]},{"long_name":"中国","short_name":"CN","types":["country","political"]}],"formatted_address":"中国山东省","geometry":{"bounds":{"northeast":{"lat":38.4022475,"lng":122.7159596},"southwest":{"lat":34.377352,"lng":114.8192544}},"location":{"lat":35.8939566,"lng":117.9249002},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":38.4022475,"lng":122.7159596},"southwest":{"lat":34.377352,"lng":114.8192544}}},"place_id":"ChIJf0-rNUfNjzURZLPhcCMuOtk","types":["administrative_area_level_1","political"]},{"address_components":[{"long_name":"中国","short_name":"CN","types":["country","political"]}],"formatted_address":"中国","geometry":{"bounds":{"northeast":{"lat":53.5609739,"lng":134.7754563},"southwest":{"lat":17.9996,"lng":73.4994136}},"location":{"lat":35.86166,"lng":104.195397},"location_type":"APPROXIMATE","viewport":{"northeast":{"lat":53.5609739,"lng":134.7754563},"southwest":{"lat":17.9996,"lng":73.4994136}}},"place_id":"ChIJwULG5WSOUDERbzafNHyqHZU","types":["country","political"]}]
     * status : OK
     */

    private PlusCodeDTO plus_code;
    private String status;
    private List<ResultsDTO> results;

    public PlusCodeDTO getPlus_code() {
        return plus_code;
    }

    public void setPlus_code(PlusCodeDTO plus_code) {
        this.plus_code = plus_code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<ResultsDTO> getResults() {
        return results;
    }

    public void setResults(List<ResultsDTO> results) {
        this.results = results;
    }

    public static class PlusCodeDTO {
        /**
         * compound_code : P54G+PJ 中国山东省潍坊市奎文区
         * global_code : 8P8XP54G+PJ
         */

        private String compound_code;
        private String global_code;

        public String getCompound_code() {
            return compound_code;
        }

        public void setCompound_code(String compound_code) {
            this.compound_code = compound_code;
        }

        public String getGlobal_code() {
            return global_code;
        }

        public void setGlobal_code(String global_code) {
            this.global_code = global_code;
        }
    }

    public static class ResultsDTO {
        /**
         * address_components : [{"long_name":"2370","short_name":"2370","types":["street_number"]},{"long_name":"胜利东街","short_name":"胜利东街","types":["route"]},{"long_name":"奎文区","short_name":"奎文区","types":["political","sublocality","sublocality_level_1"]},{"long_name":"潍坊市","short_name":"潍坊市","types":["locality","political"]},{"long_name":"山东省","short_name":"山东省","types":["administrative_area_level_1","political"]},{"long_name":"中国","short_name":"CN","types":["country","political"]},{"long_name":"261041","short_name":"261041","types":["postal_code"]}]
         * formatted_address : 中国山东省潍坊市奎文区胜利东街2370号 邮政编码: 261041
         * geometry : {"location":{"lat":36.706533,"lng":119.1766979},"location_type":"ROOFTOP","viewport":{"northeast":{"lat":36.7078819802915,"lng":119.1780468802915},"southwest":{"lat":36.7051840197085,"lng":119.1753489197085}}}
         * place_id : ChIJFwRdCV0alTUR11hIbrPq688
         * plus_code : {"compound_code":"P54G+JM 中国山东省潍坊市奎文区","global_code":"8P8XP54G+JM"}
         * types : ["street_address"]
         */

        private String formatted_address;
        private GeometryDTO geometry;
        private String place_id;
        private PlusCodeDTO plus_code;
        private List<AddressComponentsDTO> address_components;
        private List<String> types;

        public String getFormatted_address() {
            return formatted_address;
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }

        public GeometryDTO getGeometry() {
            return geometry;
        }

        public void setGeometry(GeometryDTO geometry) {
            this.geometry = geometry;
        }

        public String getPlace_id() {
            return place_id;
        }

        public void setPlace_id(String place_id) {
            this.place_id = place_id;
        }

        public PlusCodeDTO getPlus_code() {
            return plus_code;
        }

        public void setPlus_code(PlusCodeDTO plus_code) {
            this.plus_code = plus_code;
        }

        public List<AddressComponentsDTO> getAddress_components() {
            return address_components;
        }

        public void setAddress_components(List<AddressComponentsDTO> address_components) {
            this.address_components = address_components;
        }

        public List<String> getTypes() {
            return types;
        }

        public void setTypes(List<String> types) {
            this.types = types;
        }

        public static class GeometryDTO {
            /**
             * location : {"lat":36.706533,"lng":119.1766979}
             * location_type : ROOFTOP
             * viewport : {"northeast":{"lat":36.7078819802915,"lng":119.1780468802915},"southwest":{"lat":36.7051840197085,"lng":119.1753489197085}}
             */

            private LocationDTO location;
            private String location_type;
            private ViewportDTO viewport;

            public LocationDTO getLocation() {
                return location;
            }

            public void setLocation(LocationDTO location) {
                this.location = location;
            }

            public String getLocation_type() {
                return location_type;
            }

            public void setLocation_type(String location_type) {
                this.location_type = location_type;
            }

            public ViewportDTO getViewport() {
                return viewport;
            }

            public void setViewport(ViewportDTO viewport) {
                this.viewport = viewport;
            }

            public static class LocationDTO {
                /**
                 * lat : 36.706533
                 * lng : 119.1766979
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

            public static class ViewportDTO {
                /**
                 * northeast : {"lat":36.7078819802915,"lng":119.1780468802915}
                 * southwest : {"lat":36.7051840197085,"lng":119.1753489197085}
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
                     * lat : 36.7078819802915
                     * lng : 119.1780468802915
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
                     * lat : 36.7051840197085
                     * lng : 119.1753489197085
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

        public static class AddressComponentsDTO {
            /**
             * long_name : 2370
             * short_name : 2370
             * types : ["street_number"]
             */

            private String long_name;
            private String short_name;
            private List<String> types;

            public String getLong_name() {
                return long_name;
            }

            public void setLong_name(String long_name) {
                this.long_name = long_name;
            }

            public String getShort_name() {
                return short_name;
            }

            public void setShort_name(String short_name) {
                this.short_name = short_name;
            }

            public List<String> getTypes() {
                return types;
            }

            public void setTypes(List<String> types) {
                this.types = types;
            }
        }
    }
}
