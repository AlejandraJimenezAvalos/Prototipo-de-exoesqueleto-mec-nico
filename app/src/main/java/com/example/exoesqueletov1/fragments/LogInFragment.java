package com.example.exoesqueletov1.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.exoesqueletov1.MainActivity;
import com.example.exoesqueletov1.R;
import com.example.exoesqueletov1.clases.Authentication;
import com.example.exoesqueletov1.dialogs.DialogLostYourPass;
import com.example.exoesqueletov1.dialogs.DialogOops;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.Arrays;
import java.util.regex.Pattern;

public class LogInFragment extends Fragment {

    private static final String EMAIL = "email";
    private static final String USER_POSTS = "user_posts";
    private static final int GOOGLE_SIGN_IN_REQUEST_CODE = 0;
    private static final String WEB_CLIENT_ID =
            "704991108642-m4nkhc6r3t7v34pc737sgr7sj10gqmr1.apps.googleusercontent.com";

    private CallbackManager mCallbackManager;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;
    private GoogleApiClient googleApiClient;

    private View view;

    public LogInFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Button btLoging;

        view = inflater.inflate(R.layout.fragment_log_in, container, false);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseAuthListener = firebaseAuth -> {
            FirebaseUser user;
            user = firebaseAuth.getCurrentUser();
            if (null != user) {
                startActivity(new Intent(getActivity(), MainActivity.class));
                getActivity().finish();
            }
        };

        btLoging = view.findViewById(R.id.iniciar_sesion);
        btLoging.setOnClickListener(v -> singIn());

        initGoogle();
        initFacebook();
        initLostYourPass();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GOOGLE_SIGN_IN_REQUEST_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                GoogleSignInAccount account = result.getSignInAccount();
                assert account != null;
                new Authentication(getActivity().getSupportFragmentManager())
                        .logInWithCredential(GoogleAuthProvider
                                .getCredential(account.getIdToken(), null));
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(firebaseAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        firebaseAuth.removeAuthStateListener(firebaseAuthListener);
    }

    private void initFacebook() {
        LoginButton loginButton;

        mCallbackManager = CallbackManager.Factory.create();
        loginButton = view.findViewById(R.id.login_button);
        loginButton.setPermissions(Arrays.asList(EMAIL, USER_POSTS));
        loginButton.setFragment(this);
        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                new Authentication(getActivity().getSupportFragmentManager())
                        .logInWithCredential(FacebookAuthProvider.
                                getCredential(loginResult.getAccessToken().getToken()));
            }

            @Override
            public void onCancel() { }

            @Override
            public void onError(FacebookException error) {
                DialogOops dialogOops = new DialogOops(error.getMessage());
                dialogOops.show(getActivity().getSupportFragmentManager(), "example");
            }
        });
    }

    private void initGoogle() {
        SignInButton signInButton;
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(WEB_CLIENT_ID)
                .requestEmail()
                .build();

        final Context context = getContext();
        googleApiClient = new GoogleApiClient.Builder(context)
                .enableAutoManage(getActivity(), connectionResult -> {
                    DialogOops dialogOops = new DialogOops(connectionResult.getErrorMessage());
                    dialogOops.show(getActivity().getSupportFragmentManager(), "example");
                }).addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        signInButton = view.findViewById(R.id.login_button_google);
        signInButton.setOnClickListener(v -> {
            startActivityForResult(Auth.GoogleSignInApi
                    .getSignInIntent(googleApiClient), GOOGLE_SIGN_IN_REQUEST_CODE);
        });
    }

    private void initLostYourPass() {
        TextView btLostYourPass = view.findViewById(R.id.bt_lost_your_pass);
        btLostYourPass.setOnClickListener(v -> {
            DialogLostYourPass dialogLostYourPass = new DialogLostYourPass();
            dialogLostYourPass.show(getActivity().getSupportFragmentManager(), "example");
        });
    }

    private void singIn() {
        final TextInputLayout layoutEmail, layoutPass;
        final String email, password;

        layoutEmail = view.findViewById(R.id.correo_fragment_login);
        layoutPass = view.findViewById(R.id.password_fragment_login);

        resetWidgets(layoutEmail, layoutPass);

        email = layoutEmail.getEditText().getText().toString();
        password = layoutPass.getEditText().getText().toString();
        if(validateFields(email, password, layoutEmail, layoutPass)) {
            new Authentication(getFragmentManager()).logIn(email, password);
        }
    }

    private void resetWidgets(TextInputLayout layoutEmail, TextInputLayout layoutPassword) {
        layoutEmail.setError(null);
        layoutPassword.setError(null);
        layoutEmail.getEditText().setTextColor(Color.WHITE);
        layoutPassword.getEditText().setTextColor(Color.WHITE);
    }

    private boolean validateFields(String email, String password, TextInputLayout layoutEmail,
                                   TextInputLayout layoutPassword) {
        boolean result = false;
        if (!email.equals("")) {
            Pattern pattern;
            pattern = Patterns.EMAIL_ADDRESS;
            if (pattern.matcher(email).matches()) {
                if (!password.isEmpty()) { result = true; }
                else { setError(getString(R.string.error_introducir_pass), layoutPassword); }
            }
            else { setError(getString(R.string.error_correo_invalido), layoutEmail); }
        } else { setError(getString(R.string.error_introducir_email), layoutEmail); }
        return result;
    }

    private void setError(String error, TextInputLayout inputLayout) {
        inputLayout.setError(error);
        inputLayout.setErrorTextColor(ColorStateList.valueOf(Color.WHITE));
        inputLayout.setHintTextColor(ColorStateList.valueOf(Color.WHITE));
        inputLayout.getEditText().setTextColor(Color.BLACK);
    }

}
