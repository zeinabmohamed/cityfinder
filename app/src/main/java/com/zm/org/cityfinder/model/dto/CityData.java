package com.zm.org.cityfinder.model.dto;

import com.google.gson.annotations.SerializedName;

public class CityData {

    /**
     * country : UA
     * name : Hurzuf
     * _id : 707860
     * coord : {"lon":34.283333,"lat":44.549999}
     */

    @SerializedName("country")
    public String country;
    @SerializedName("name")
    public String name;
    @SerializedName("_id")
    public int id;
    @SerializedName("coord")
    public Coord coord;

    public static class Coord {
        /**
         * lon : 34.283333
         * lat : 44.549999
         */

        @SerializedName("lon")
        public double lon;
        @SerializedName("lat")
        public double lat;
    }
}
