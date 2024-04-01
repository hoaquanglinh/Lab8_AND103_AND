package com.example.lab8_and103.services;

import com.example.lab8_and103.models.District;
import com.example.lab8_and103.models.DistrictRequest;
import com.example.lab8_and103.models.Province;
import com.example.lab8_and103.models.ResponseGHN;
import com.example.lab8_and103.models.Ward;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface GHNServices {
    public static String GHN_URL = "https://dev-online-gateway.ghn.vn/";

    @GET("/shiip/public-api/master-data/province")
    Call<ResponseGHN<ArrayList<Province>>> getListProvince();

    @POST("/shiip/public-api/master-data/district")
    Call<ResponseGHN<ArrayList<District>>> getListDistrict(@Body DistrictRequest districtRequest);

    @GET("/shiip/public-api/master-data/ward")
    Call<ResponseGHN<ArrayList<Ward>>> getListWard(@Query("district_id") int district_id);
}