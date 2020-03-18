package com.example.exoesqueletov1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exoesqueletov1.clases.NewsAdapter;
import com.example.exoesqueletov1.clases.NewsItem;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            RecyclerView recyclerMenu = findViewById(R.id.recycler_menu);
            //recyclerMenu.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
             NewsAdapter newsAdapter;
            List<NewsItem> mData = new ArrayList<>();
            mData.add(new NewsItem("", R.drawable.ic_home));
            mData.add(new NewsItem("", R.drawable.ic_profile));
            newsAdapter = new NewsAdapter(this, mData);
            recyclerMenu.setAdapter(newsAdapter);
            recyclerMenu.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        } catch (Exception e) {
            Toast.makeText(this, "e: " + e, Toast.LENGTH_SHORT).show();
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();


        //fragmentTransaction.replace(R.id.container_main, new WelcomeFragment());
        //fragmentTransaction.commit();
    }

    public void handle (View view) {
        switch (view.getId()) {
            case R.id.button_finish_main:
                finish();
                break;
            case R.id.button_singout_main:
                LoginManager.getInstance().logOut();
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(this, SplashActivity.class));
                break;
        }
    }

    /*
     * When you use a RecyclerView, you need to specify a LayoutManager that is responsible
     * for laying out each item in the view. The LinearLayoutManager allows you to specify
     * an orientation, just like a normal LinearLayout would.
     *
     * LinearLayoutManager layoutManager
     * = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
     *
     * RecyclerView myList = (RecyclerView) findViewById(R.id.my_recycler_view);
     * myList.setLayoutManager(layoutManager);
     */
}
