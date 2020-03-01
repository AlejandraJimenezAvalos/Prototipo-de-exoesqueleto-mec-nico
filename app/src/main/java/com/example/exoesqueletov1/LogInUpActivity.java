package com.example.exoesqueletov1;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.exoesqueletov1.clases.ViewPagerAdapter;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;

import java.util.Arrays;

public class LogInUpActivity extends AppCompatActivity  {

    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_up);

        viewPager = findViewById(R.id.pager);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new WelcomeFragment());
        adapter.addFragment(new LogInFragment());
        adapter.addFragment(new LogUpFragment());

        viewPager.setAdapter(adapter);
    }

}
