package com.zm.org.cityfinder.model.dto;

import androidx.annotation.Nullable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CityData implements Serializable {

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
    @SerializedName("lon")
    public double lon;
    @SerializedName("lat")
    public double lat;



    public CityData(int id, String name, String country, double lon, double lat) {

        this.id = id;
        this.name = name;
        this.country = country;
        this.lon = lon;
        this.lat = lat;
    }

    public CityData(String name) {
        this.name = name;
    }


    @Override
    public boolean equals(@Nullable Object obj) {
        return obj != null && ((CityData) obj).name.toLowerCase().startsWith(this.name.toLowerCase());
    }

}
