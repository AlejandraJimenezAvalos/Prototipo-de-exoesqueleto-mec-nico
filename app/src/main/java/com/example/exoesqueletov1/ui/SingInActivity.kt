package com.example.exoesqueletov1;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.exoesqueletov1.clases.ViewPagerAdapter;
import com.example.exoesqueletov1.fragments.LogInFragment;
import com.example.exoesqueletov1.fragments.LogUpFragment;
import com.example.exoesqueletov1.fragments.WelcomeFragment;

public class LogActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        ViewPager viewPager = findViewById(R.id.pager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new WelcomeFragment());
        adapter.addFragment(new LogInFragment());
        adapter.addFragment(new LogUpFragment());

        viewPager.setAdapter(adapter);
    }

}
