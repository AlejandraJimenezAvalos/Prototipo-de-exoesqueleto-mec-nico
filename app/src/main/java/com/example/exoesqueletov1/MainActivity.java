package com.example.exoesqueletov1;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exoesqueletov1.clases.MenuAdapter;
import com.example.exoesqueletov1.clases.Authentication;
import com.example.exoesqueletov1.clases.NewsItem;
import com.example.exoesqueletov1.dialog.DialogLoading;
import com.facebook.login.LoginManager;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements MenuAdapter.OnMenuListener {

    private List<NewsItem> mData;
    private static final String DOCUMENT = "user";
    private static final long ONE_MEGABYTE = 1024 * 1024;
    private boolean state = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initVerify();

    }

    private void initVerify () {
        final String id = new Authentication().getCurrentUser().getEmail();
        final DialogLoading loading = new DialogLoading();
        loading.show(getSupportFragmentManager(), getString(R.string.example));

        FirebaseFirestore.getInstance().collection(id).document(DOCUMENT).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object> data = documentSnapshot.getData();
                        try {
                            state = true;
                            if (Boolean.parseBoolean(String.valueOf(data.get(id)))) {
                                TextView name = findViewById(R.id.text_view_nombre_main);
                                name.setText(data.get("name").toString());
                                FirebaseStorage.getInstance().getReference().child("pictureProfile").child(id)
                                        .getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        CircleImageView circleImageView = findViewById(R.id.image_perfil_main);
                                        circleImageView.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                                    }
                                });

                                initComponents1();
                            }
                        } catch (Exception e) { initComponents2(); }
                        loading.dismiss();
                    }
                });
    }

    private void initComponents2 () {
        getSupportFragmentManager().beginTransaction().replace(R.id.container_main, new ProfileLogUpFragment()).commit();
    }

    private void initComponents1 () {
        RecyclerView recyclerMenu = findViewById(R.id.recycler_menu);
        MenuAdapter menuAdapter;
        mData = new ArrayList<>();
        mData.add(new NewsItem(getString(R.string.inicio), R.drawable.ic_notifications));
        mData.add(new NewsItem(getString(R.string.profile), R.drawable.ic_profile));
        menuAdapter = new MenuAdapter(this, mData, this);
        recyclerMenu.setAdapter(menuAdapter);
        recyclerMenu.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        getSupportFragmentManager().beginTransaction().replace(R.id.container_main, new NotifyFragment()).commit();
    }

    public void handleMain (View view) {
        switch (view.getId()) {
            case R.id.button_finish_main:
                finish();
                break;
            case R.id.button_singout_main:
                LoginManager.getInstance().logOut();
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(this, SplashActivity.class));
                break;
        }
    }

    @Override
    public void onMenuClick(int position) {
        Fragment fragment = null;
        if (mData.get(position).getTitle().equals(getString(R.string.inicio))) {
            fragment = new NotifyFragment();
        }
        if (mData.get(position).getTitle().equals(getString(R.string.profile))) {
            if (!state) { fragment = new ProfileLogUpFragment(); }
            else { fragment = new ProfileFragment(); }
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container_main, fragment).commit();
    }

}
