package com.example.exoesqueletov1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exoesqueletov1.clases.Authentication;
import com.example.exoesqueletov1.clases.Database;
import com.example.exoesqueletov1.clases.MenuAdapter;
import com.example.exoesqueletov1.clases.MenuItem;
import com.example.exoesqueletov1.clases.Storge;
import com.example.exoesqueletov1.dialog.DialogLoading;
import com.example.exoesqueletov1.fragments.MessageFragment;
import com.example.exoesqueletov1.fragments.NotificationFragment;
import com.example.exoesqueletov1.fragments.ProfileFragment;
import com.example.exoesqueletov1.fragments.ProfileLogUpFragment;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements MenuAdapter.OnMenuListener {

    private CircleImageView circleImageView;
    private TextView textViewState;
    private TextView textViewTypeUser;

    private List<MenuItem> mData;

    private boolean state = false;
    private boolean isState = true;

    private static final int CODE_REGULAR = 1000;

    private String typeUser;
    private String id = new Authentication().getCurrentUser().getEmail();

    private DialogLoading loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView name = findViewById(R.id.text_view_nombre_main);
        loading = new DialogLoading();
        loading.show(getSupportFragmentManager(), "loading");

        textViewTypeUser = findViewById(R.id.text_type_user_main);
        textViewState = findViewById(R.id.text_state_main);
        circleImageView = findViewById(R.id.image_perfil_main);

        Database database = new Database(getSupportFragmentManager(), this);
        database.initMain(name, textViewTypeUser, textViewState, id, circleImageView);

        Timer timer = new Timer();
        timer.execute();
    }

    private void newUser() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container_main, new ProfileLogUpFragment()).commit();
    }

    private void userA() {
        RecyclerView recyclerMenu = findViewById(R.id.recycler_menu);
        MenuAdapter menuAdapter;
        mData = new ArrayList<>();
        mData.add(new MenuItem(getString(R.string.inicio), R.drawable.ic_notifications));
        mData.add(new MenuItem(getString(R.string.profile), R.drawable.ic_profile));
        mData.add(new MenuItem(getString(R.string.messages), R.drawable.ic_message));
        menuAdapter = new MenuAdapter(this, mData, this);
        recyclerMenu.setAdapter(menuAdapter);
        recyclerMenu.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        getSupportFragmentManager().beginTransaction().replace(R.id.container_main, new NotificationFragment(typeUser)).commit();
    }

    private void userB() {
        RecyclerView recyclerMenu = findViewById(R.id.recycler_menu);
        MenuAdapter menuAdapter;
        mData = new ArrayList<>();
        mData.add(new MenuItem(getString(R.string.inicio), R.drawable.ic_notifications));
        mData.add(new MenuItem(getString(R.string.profile), R.drawable.ic_profile));
        mData.add(new MenuItem(getString(R.string.messages), R.drawable.ic_message));
        menuAdapter = new MenuAdapter(this, mData, this);
        recyclerMenu.setAdapter(menuAdapter);
        recyclerMenu.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        getSupportFragmentManager().beginTransaction().replace(R.id.container_main, new NotificationFragment(typeUser)).commit();
    }

    private void userC() {
        RecyclerView recyclerMenu = findViewById(R.id.recycler_menu);
        MenuAdapter menuAdapter;
        mData = new ArrayList<>();
        mData.add(new MenuItem(getString(R.string.inicio), R.drawable.ic_notifications));
        mData.add(new MenuItem(getString(R.string.profile), R.drawable.ic_profile));
        mData.add(new MenuItem(getString(R.string.messages), R.drawable.ic_message));
        menuAdapter = new MenuAdapter(this, mData, this);
        recyclerMenu.setAdapter(menuAdapter);
        recyclerMenu.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        getSupportFragmentManager().beginTransaction().replace(R.id.container_main, new NotificationFragment(typeUser)).commit();
    }

    @Override
    public void onMenuClick(int position) {

        Fragment fragment = null;

        if (!state) { fragment = new ProfileLogUpFragment(); }
        else {
            if (mData.get(position).getTitle().equals(getString(R.string.inicio))) { fragment = new NotificationFragment(typeUser); }
            if (mData.get(position).getTitle().equals(getString(R.string.profile))) { fragment = new ProfileFragment(); }
            if (mData.get(position).getTitle().equals(getString(R.string.messages))) { fragment = new MessageFragment(typeUser, CODE_REGULAR); }
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.container_main, fragment).commit();
    }

    @SuppressLint("StaticFieldLeak")
    public class Timer extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            if (isState) {
                final String id = new Authentication().getCurrentUser().getEmail();
                Timer timer = new Timer();
                timer.execute();
                new Storge().getProfileImage(circleImageView, id);
                if (textViewState.getText().equals("t")) {
                    loading.dismiss();
                    state = true;
                    typeUser = textViewTypeUser.getText().toString();
                    textViewState.setText("");
                    switch (typeUser) {
                        case "a":
                            userA();
                            break;
                        case "b":
                            userB();
                            break;
                        case "c":
                            userC();
                            break;
                        case "n":
                            newUser();
                            break;
                    }
                }
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            for (int i = 1; i <= 2; i++) { try { Thread.sleep(1000); } catch (InterruptedException ignored) { } }
            return null;
        }
    }

    public void handleMain (View view) {
        isState = false;
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
    protected void onResume() {
        super.onResume();
        isState = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        isState = false;
    }
}
