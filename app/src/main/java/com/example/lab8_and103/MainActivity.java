package com.example.lab8_and103;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;

import com.example.lab8_and103.adapter.ProductAdapter;
import com.example.lab8_and103.databinding.ActivityMainBinding;
import com.example.lab8_and103.models.Product;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ArrayList<Product> list = new ArrayList<>();
    private ProductAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        list.add(new Product("1", "fruit", "Apple", "Fresh and juicy apple", 4.5, 2.99, R.drawable.apple));
        list.add(new Product("2", "fruit", "Banana", "Ripe banana", 4.2, 1.99, R.drawable.bana));
        list.add(new Product("3", "fruit", "Orange", "Sweet and tangy orange", 4.0, 3.49, R.drawable.apple));
        list.add(new Product("4", "fruit", "Grape", "Delicious grapes", 4.3, 4.99, R.drawable.bana));
        adapter = new ProductAdapter(list, this);
        binding.rcvProduct.setLayoutManager(new GridLayoutManager(this, 2));
        binding.rcvProduct.setAdapter(adapter);
    }
}