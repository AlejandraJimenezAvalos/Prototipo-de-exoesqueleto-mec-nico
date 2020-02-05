package com.example.exoesqueletov1;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.exoesqueletov1.clases.ViewPagerAdapter;

public class LogInUpActivity extends AppCompatActivity  {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_up);

        viewPager = findViewById(R.id.pager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new WelcomeFragment());
        adapter.addFragment(new LogUpFragment());
        adapter.addFragment(new LogInFragment());

        viewPager.setAdapter(adapter);
    }

}
