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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class LogUpFragment extends Fragment {

    private View view;
    private static final String KEY = "6LcJnrsUAAAAAKavHRkqVEkRLm8KJo5_pjYuX-uE";
    private CallbackManager callbackManager;

    // wid
    private Button btRegistrar;
    private LoginButton btFacebook;
    private ImageView btGoogle;
    private TextInputLayout email;
    private TextInputLayout password1;
    private TextInputLayout password2;
    private LinearLayout linearReCaptcha;
    private CheckBox ckReCaptcha;
    private CheckBox ckTerminos;

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

        callbackManager = CallbackManager.Factory.create();

        this.init();
        this.onClick();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void onClick() {
        btRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btFacebook.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Toast.makeText(getContext(), "si se hizo", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getContext(), "se cancelo :c", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getContext(), "Errar es humano :c :" + error, Toast.LENGTH_SHORT).show();
            }
        });

        btGoogle.setOnClickListener(new View.OnClickListener() {
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
        btFacebook = view.findViewById(R.id.bt_registrar_facebook);
        //btGoogle = view.findViewById(R.id.bt_registrar_google);
        email = view.findViewById(R.id.email_fragment_logup);
        password1 = view.findViewById(R.id.password1_fragment_logup);
        password2 = view.findViewById(R.id.password2_fragment_logup);
        linearReCaptcha = view.findViewById(R.id.layaout_reCaptcha);
        ckReCaptcha = view.findViewById(R.id.ck_reCaptcha);
        ckTerminos = view.findViewById(R.id.ck_terminos);
    }


}
