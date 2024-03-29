package com.example.lab8_and103;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.example.lab8_and103.adapter.Adapter_Item_District_Select_GHN;
import com.example.lab8_and103.adapter.Adapter_Item_Province_Select_GHN;
import com.example.lab8_and103.adapter.Adapter_Item_Ward_Select_GHN;
import com.example.lab8_and103.databinding.ActivityLocationBinding;
import com.example.lab8_and103.models.District;
import com.example.lab8_and103.models.DistrictRequest;
import com.example.lab8_and103.models.Province;
import com.example.lab8_and103.models.Ward;
import com.example.lab8_and103.services.GHNRequest;
import com.example.lab8_and103.services.GHNServices;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LocationActivity extends AppCompatActivity {
    private ActivityLocationBinding binding;
    private GHNServices ghnServices;
    private String productId, productTypeId, productName, description, WardCode;
    private double rate, price;
    private int image, DistrictID, ProvinceID;
    Adapter_Item_District_Select_GHN adapterDistrict;
    Adapter_Item_Province_Select_GHN andapterProvince;
    Adapter_Item_Ward_Select_GHN adapterWard;
    public final static String SHOPID = "191592";
    public final static String TokenGHN = "7714c3b1-ed8e-11ee-a6e6-e60958111f48";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLocationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            productId = bundle.getString("productId");
            productTypeId = bundle.getString("productTypeId");
            productName = bundle.getString("productName");
            description = bundle.getString("description");
            rate = bundle.getDouble("rate");
            price = bundle.getDouble("price");
            image = bundle.getInt("image");
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://dev-online-gateway.ghn.vn/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(new OkHttpClient.Builder().addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader("ShopId", SHOPID)
                                .addHeader("Token", TokenGHN)
                                .build();
                        return chain.proceed(request);
                    }
                }).build())
                .build();

        ghnServices = retrofit.create((GHNServices.class));

        Call<ArrayList<Province>> call = ghnServices.getListProvince();
        call.enqueue(new Callback<ArrayList<Province>>() {
            @Override
            public void onResponse(Call<ArrayList<Province>> call, Response<ArrayList<Province>> response) {
                if(response.isSuccessful()){
                    ArrayList<Province> ds = new ArrayList<>(response.body());
                    SetDataSpinProvince(ds);
                }else{
                    Toast.makeText(LocationActivity.this, "Loi nguoc lai", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Province>> call, Throwable t) {
                Log.e("API Error", "Error: " + t.getMessage());
            }
        });


        binding.spProvince.setOnItemSelectedListener(onItemSelectedListener);
        binding.spDistrict.setOnItemSelectedListener(onItemSelectedListener);
        binding.spWard.setOnItemSelectedListener(onItemSelectedListener);
        binding.spProvince.setSelection(1);
        binding.spDistrict.setSelection(1);
        binding.spWard.setSelection(1);
    }

    AdapterView.OnItemSelectedListener onItemSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (parent.getId() == R.id.sp_province) {
                ProvinceID = ((Province) parent.getAdapter().getItem(position)).getProvinceID();
                DistrictRequest districtRequest = new DistrictRequest(ProvinceID);

                Call<ArrayList<District>> call = ghnServices.getListDistrict(districtRequest);
                call.enqueue(new Callback<ArrayList<District>>() {
                    @Override
                    public void onResponse(Call<ArrayList<District>> call, Response<ArrayList<District>> response) {
                        if (response.isSuccessful()){
                            ArrayList<District> ds = new ArrayList<>(response.body());
                            SetDataSpinDistrict(ds);
                        }else{
                            Toast.makeText(LocationActivity.this, "Loi nguoc lai", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<District>> call, Throwable t) {
                        Log.e("API Error", "Error: " + t.getMessage());
                    }
                });

            } else if (parent.getId() == R.id.sp_district) {
                DistrictID = ((District) parent.getAdapter().getItem(position)).getDistrictID();

                Call<ArrayList<Ward>> call = ghnServices.getListWard(DistrictID);
                call.enqueue(new Callback<ArrayList<Ward>>() {
                    @Override
                    public void onResponse(Call<ArrayList<Ward>> call, Response<ArrayList<Ward>> response) {
                        ArrayList<Ward> ds = new ArrayList<>(response.body());
                        SetDataSpinWard(ds);
                    }

                    @Override
                    public void onFailure(Call<ArrayList<Ward>> call, Throwable t) {
                        Log.e("API Error", "Error: " + t.getMessage());
                    }
                });
            } else if (parent.getId() == R.id.sp_ward) {
                WardCode = ((Ward) parent.getAdapter().getItem(position)).getWardCode();
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    };

    private void SetDataSpinProvince(ArrayList<Province> ds){
        andapterProvince = new Adapter_Item_Province_Select_GHN(this, ds);
        binding.spProvince.setAdapter(andapterProvince);
    }

    private void SetDataSpinDistrict(ArrayList<District> ds){
        adapterDistrict = new Adapter_Item_District_Select_GHN(this, ds);
        binding.spDistrict.setAdapter(adapterDistrict);
    }

    private void SetDataSpinWard(ArrayList<Ward> ds){
        adapterWard = new Adapter_Item_Ward_Select_GHN(this, ds);
        binding.spWard.setAdapter(adapterWard );
    }
}