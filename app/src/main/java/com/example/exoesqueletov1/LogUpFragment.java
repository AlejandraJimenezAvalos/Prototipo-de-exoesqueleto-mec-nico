package com.example.exoesqueletov1;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Arrays;
import java.util.Objects;

public class LogUpFragment extends Fragment {

    // wid
    private Button btRegistrar;
    private TextInputLayout email;
    private TextInputLayout password1;
    private TextInputLayout password2;
    private LinearLayout linearReCaptcha;
    private CheckBox ckReCaptcha;
    private CheckBox ckTerminos;

    private View view;
    private static final String KEY = "6LcJnrsUAAAAAKavHRkqVEkRLm8KJo5_pjYuX-uE";

    public LogUpFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_log_up, container, false);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.init();
        this.onClick();
    }

    private void onClick() {
        btRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        linearReCaptcha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SafetyNet.getClient(Objects.requireNonNull(getContext())).verifyWithRecaptcha(KEY)
                        .addOnSuccessListener(new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
                            @Override
                            public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response) {
                                String userResponseToken = response.getTokenResult();
                                if (!userResponseToken.isEmpty()) {
                                    ckReCaptcha.setChecked(true);
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
            }
        });

        ckTerminos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                builder.setTitle(R.string.terminos_y_condiciones)
                        .setMessage(R.string.terminos)
                        .setPositiveButton(R.string.aceptar,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        ckTerminos.setChecked(true);
                                    }
                                })
                        .setNegativeButton(R.string.no_acepto,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        AlertDialog.Builder builder1= new AlertDialog.Builder(getContext());
                                        builder1.setTitle("¿Esta seguro?").setMessage(R.string.condicion_terminos)
                                                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        ckTerminos.setChecked(false);
                                                    }
                                                }).show();
                                    }
                                }).show();
            }
        });

    }

    private void init() {
        btRegistrar = view.findViewById(R.id.bt_registrar);
        email = view.findViewById(R.id.email_fragment_logup);
        password1 = view.findViewById(R.id.password1_fragment_logup);
        password2 = view.findViewById(R.id.password2_fragment_logup);
        linearReCaptcha = view.findViewById(R.id.layaout_reCaptcha);
        ckReCaptcha = view.findViewById(R.id.ck_reCaptcha);
        ckTerminos = view.findViewById(R.id.ck_terminos);
    }

}
