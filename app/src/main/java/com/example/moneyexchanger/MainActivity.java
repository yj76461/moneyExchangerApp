package com.example.moneyexchanger;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements SendEventListener{
    private FragmentTransaction transaction;
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private Fragment fragment_converter;
    private Fragment fragment_news;
    String curPick = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        fragment_converter = new ConverterFragment();
        fragment_news = new NewsFragment();

        transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, fragment_converter).commitAllowingStateLoss();

        //BottomNavigationView bottomNavigationView = findViewById(R.id.navigationView);
        TabLayout tabLayout = findViewById(R.id.tb);
        /*
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                transaction = fragmentManager.beginTransaction();

                switch (item.getItemId())
                {
                    case R.id.homeItem:
                        transaction.replace(R.id.frame_layout, fragment_converter).commitAllowingStateLoss();
                        break;
                    case R.id.newsItem:
                        Bundle bundle = new Bundle();
                        bundle.putString("curPick", curPick);
                        fragment_news.setArguments(bundle);

                        transaction.replace(R.id.frame_layout, fragment_news).commitAllowingStateLoss();
                        break;
                }
                return true;
            }
        });
         */

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                transaction = fragmentManager.beginTransaction();

                Fragment selected = null;
                if(pos == 0){
                    selected = fragment_converter;
                }
                else if(pos == 1){
                    selected = fragment_news;
                    Bundle bundle = new Bundle();
                    bundle.putString("curPick", curPick);
                    selected.setArguments(bundle);
                }
                if(selected != null)
                    transaction.replace(R.id.frame_layout, selected).commitAllowingStateLoss();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    public void sendMessage(String message){
        Toast.makeText(this, "selected currency: " + message, Toast.LENGTH_SHORT).show();
        curPick = message;
    }


}