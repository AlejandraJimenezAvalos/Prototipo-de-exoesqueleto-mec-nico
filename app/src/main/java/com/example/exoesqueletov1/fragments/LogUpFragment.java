package com.example.exoesqueletov1.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.exoesqueletov1.R;
import com.example.exoesqueletov1.clases.Authentication;
import com.example.exoesqueletov1.dialog.DialogOops;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogUpFragment extends Fragment {

    // wid
    private Button btRegistrar;
    private TextInputLayout emailLayout;
    private TextInputLayout password1Layout;
    private TextInputLayout password2Layout;
    private LinearLayout linearReCaptcha;
    private CheckBox ckReCaptcha;
    private CheckBox ckTerminos;

    private boolean terminosState = false;
    private boolean reCaptchaState = false;
    private View view;
    private static final String KEY = "6LcJnrsUAAAAAKavHRkqVEkRLm8KJo5_pjYuX-uE";
    private final static String REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@._$!%*?&])([A-Za-z\\d$@._$!%*?&]|[^ ]){8,15}$";

    public LogUpFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_log_up, container, false);
        this.init();
        this.onClick();
        return view;
    }

    private void onClick() {
        btRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailLayout = view.findViewById(R.id.email_fragment_logup);
                password1Layout = view.findViewById(R.id.password1_fragment_logup);
                password2Layout = view.findViewById(R.id.password2_fragment_logup);
                resetWidgets();
                createAnAccount (emailLayout.getEditText().getText().toString().trim(),
                        password1Layout.getEditText().getText().toString().trim(),
                        password2Layout.getEditText().getText().toString().trim());
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
                                    reCaptchaState = true;
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        ckReCaptcha.setChecked(false);
                        reCaptchaState = false;
                        DialogOops dialogOops = new DialogOops(e.getMessage());
                        dialogOops.show(getFragmentManager(), "example");
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
                                        terminosState = true;
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
                                                        terminosState = false;
                                                    }
                                                }).show();
                                    }
                                }).show();
            }
        });

    }

    private void resetWidgets() {
        emailLayout.setError(null);
        emailLayout.getEditText().setTextColor(Color.WHITE);
        password1Layout.setError(null);
        password1Layout.getEditText().setTextColor(Color.WHITE);
        password2Layout.setError(null);
        password2Layout.getEditText().setTextColor(Color.WHITE);
    }

    private void createAnAccount(String email, String pass1, String pass2) {
        if (validateFields(email, pass1, pass2)){
            new Authentication(getFragmentManager()).logUp(email, pass1);
        }
    }

    private boolean validateFields(String email, String pass1, String pass2) {
        boolean state = false;
        if (!email.isEmpty()) {
            Pattern pattern = Patterns.EMAIL_ADDRESS;
            if (pattern.matcher(email).matches()){
                if (!pass1.isEmpty()) {
                    Pattern pattern2 = Pattern.compile(REGEX, Pattern.MULTILINE);
                    Matcher matcher = pattern2.matcher(pass1);
                    if (matcher.find()) {
                        if (!pass2.isEmpty()) {
                            if (pass1.equals(pass2)) {
                                if (reCaptchaState && terminosState) {
                                    state = true;
                                } else {
                                    DialogOops dialogOops = new DialogOops(getString(R.string.condicion_casillas));
                                    dialogOops.show(getFragmentManager(), "example");
                                }
                            } else { setError(getString(R.string.error_pass_no_coincide), password2Layout); }
                        } else { setError(getString(R.string.error_introducir_pass), password2Layout); }
                    } else {
                        DialogOops dialogOops = new DialogOops(getString(R.string.condición_contraseña));
                        dialogOops.show(getFragmentManager(), "example");
                    }
                } else { setError(getString(R.string.error_introducir_pass), password1Layout); }
            } else { setError(getString(R.string.error_correo_invalido), emailLayout); }
        } else { setError(getString(R.string.error_introducir_email), emailLayout); }
        return state;
    }

    private void setError(String error, TextInputLayout inputLayout) {
        inputLayout.setError(error);
        inputLayout.setErrorTextColor(ColorStateList.valueOf(Color.WHITE));
        inputLayout.setHintTextColor(ColorStateList.valueOf(Color.WHITE));
        inputLayout.getEditText().setTextColor(Color.BLACK);
    }

    private void init() {
        btRegistrar = view.findViewById(R.id.bt_registrar);
        linearReCaptcha = view.findViewById(R.id.layaout_reCaptcha);
        ckReCaptcha = view.findViewById(R.id.ck_reCaptcha);
        ckTerminos = view.findViewById(R.id.ck_terminos);
    }

}
