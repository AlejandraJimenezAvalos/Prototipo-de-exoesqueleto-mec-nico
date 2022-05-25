package com.example.exoesqueletov1.ui.fragments;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.format.DateFormat;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.exoesqueletov1.ui.activity.ConstantsDatabase;
import com.example.exoesqueletov1.R;
import com.example.exoesqueletov1.old.clases.Authentication;
import com.example.exoesqueletov1.old.clases.bleutooth.SerialListener;
import com.example.exoesqueletov1.old.clases.bleutooth.SerialService;
import com.example.exoesqueletov1.old.clases.bleutooth.SerialSocket;
import com.example.exoesqueletov1.ui.dialogs.DialogInfo;
import com.example.exoesqueletov1.ui.dialogs.DialogOops;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.example.exoesqueletov1.ui.activity.ConstantsDatabase.ACTION;
import static com.example.exoesqueletov1.ui.activity.ConstantsDatabase.COLLECTION_USERS;
import static com.example.exoesqueletov1.ui.activity.ConstantsDatabase.ID_EXOESQUELETO;
import static com.example.exoesqueletov1.ui.activity.ConstantsDatabase.NO;
import static com.example.exoesqueletov1.ui.activity.ConstantsDatabase.NUMBER;
import static com.example.exoesqueletov1.ui.activity.ConstantsDatabase.STATE;
import static com.example.exoesqueletov1.ui.activity.ControlActivity.getActionButtonHelp;
import static com.example.exoesqueletov1.ui.activity.ControlActivity.getActionButtonPause;
import static com.example.exoesqueletov1.ui.activity.ControlActivity.getActionButtonReport;
import static com.example.exoesqueletov1.ui.activity.ControlActivity.getActionButtonStop;
import static com.example.exoesqueletov1.ui.activity.ControlActivity.setActionButtonHelp;
import static com.example.exoesqueletov1.ui.activity.ControlActivity.setActionButtonPause;
import static com.example.exoesqueletov1.ui.activity.ControlActivity.setActionButtonReport;
import static com.example.exoesqueletov1.ui.activity.ControlActivity.setActionButtonStop;
import static com.example.exoesqueletov1.old.clases.bleutooth.Constants.FINALISE;
import static com.example.exoesqueletov1.old.clases.bleutooth.Constants.LISTEN;
import static com.example.exoesqueletov1.old.clases.bleutooth.Constants.PAUSE;
import static com.example.exoesqueletov1.old.clases.bleutooth.Constants.PLAY;
import static com.example.exoesqueletov1.old.clases.bleutooth.Constants.RECEIVED;
import static com.example.exoesqueletov1.old.clases.bleutooth.Constants.STOP;
import static com.example.exoesqueletov1.old.clases.bleutooth.Constants.WALK_MINUTES;
import static com.example.exoesqueletov1.old.clases.bleutooth.Constants.WALK_STEPS;

public class WalkFragment extends Fragment {

    WalkFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_walk, container, false);
        return view;
    }

}


