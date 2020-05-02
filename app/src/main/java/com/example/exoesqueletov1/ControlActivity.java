package com.example.exoesqueletov1;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.exoesqueletov1.clases.ViewPagerAdapter;
import com.example.exoesqueletov1.fragments.PairedDevisesFragment;
import com.example.exoesqueletov1.fragments.UpAndDownFragment;
import com.example.exoesqueletov1.fragments.WalkFragment;

public class ControlActivity extends AppCompatActivity {

    String address = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control);

        Bundle bundle = this.getIntent().getExtras();
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        final ViewPager viewPager = findViewById(R.id.viewPager2);
        final ImageView radio1 = findViewById(R.id.radio_1);
        final ImageView radio2 = findViewById(R.id.radio_2);
        final ImageView radio3 = findViewById(R.id.radio_3);
        final ImageView radio4 = findViewById(R.id.radio_4);

        address = bundle.getString(PairedDevisesFragment.DEVICE_ADDRESS);

        adapter.addFragment(new WalkFragment(0));
        adapter.addFragment(new UpAndDownFragment());
        adapter.addFragment(new WalkFragment(1));

        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        radio1.setImageResource(R.drawable.ic_radio_button_checked);
                        radio2.setImageResource(R.drawable.ic_radio_button_unchecked);
                        break;
                    case 1:
                        radio1.setImageResource(R.drawable.ic_radio_button_unchecked);
                        radio2.setImageResource(R.drawable.ic_radio_button_checked);
                        radio3.setImageResource(R.drawable.ic_radio_button_unchecked);
                        break;
                    case 2:
                        radio2.setImageResource(R.drawable.ic_radio_button_unchecked);
                        radio3.setImageResource(R.drawable.ic_radio_button_checked);
                        radio4.setImageResource(R.drawable.ic_radio_button_unchecked);
                        break;
                    case 3:
                        radio3.setImageResource(R.drawable.ic_radio_button_checked);
                        radio4.setImageResource(R.drawable.ic_radio_button_unchecked);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        radio1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });

        radio2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });

        radio3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(2);
            }
        });

        radio4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(3);
            }
        });
    }
}
