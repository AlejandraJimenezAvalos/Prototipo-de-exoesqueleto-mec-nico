package com.example.exoesqueletov1.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.exoesqueletov1.Constants;
import com.example.exoesqueletov1.MainActivity;
import com.example.exoesqueletov1.R;
import com.example.exoesqueletov1.clases.Authentication;
import com.example.exoesqueletov1.clases.Database;
import com.example.exoesqueletov1.clases.Storge;
import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ProfileLogUpFragment extends Fragment {

    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutLastName;
    private TextInputLayout textInputLayoutDate;
    private Spinner spinnerCountry;
    private RadioButton radioButtonWomen;
    private RadioButton radioButtonPatient;
    private CircleImageView imageViewProfile;

    private static final int PICK_IMAGE = 1;
    private static final int CUT_PICTURE = 3535;
    private static final int ASPECT_RATIO_X = 1;
    private static final int ASPECT_RATIO_Y = 1;

    public ProfileLogUpFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Button save;
        View view;

        view = inflater.inflate(R.layout.fragment_log_up_profile, container, false);

        textInputLayoutName = view.findViewById(R.id.edit_text_nombre);
        textInputLayoutLastName = view.findViewById(R.id.edit_text_apellidos);
        textInputLayoutDate = view.findViewById(R.id.edit_text_date);
        spinnerCountry = view.findViewById(R.id.spinner_contry);
        radioButtonWomen = view.findViewById(R.id.radio_women);
        radioButtonPatient = view.findViewById(R.id.radio_pasiente);
        imageViewProfile = view.findViewById(R.id.image_perfil_profile);
        save = view.findViewById(R.id.button_save_profile);

        imageViewProfile.setOnClickListener(v -> selectImage(getContext()));

        save.setOnClickListener(v -> {
            textInputLayoutName.setError(null);
            textInputLayoutLastName.setError(null);
            textInputLayoutDate.setError(null);
            if (verify()) { saveProfile(); }
        });

        return view;
    }

    private void selectImage(Context context) {
        final CharSequence[] options = { getString(R.string.chose_from_gallery), getString(R.string.cancelar) };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getString(R.string.fto_de_perf_l));

        builder.setItems(options, (dialog, item) -> {
            if (options[item].equals(getString(R.string.chose_from_gallery))) {
                Intent gallery = new Intent();
                gallery.setType("image/*");
                gallery.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent
                        .createChooser(gallery, getString(R.string.fto_de_perf_l)),  PICK_IMAGE);
            } if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void cropCapturedImage(Uri urlImagen){
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(urlImagen, "image/*");
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("aspectX", ASPECT_RATIO_X);
        cropIntent.putExtra("aspectY", ASPECT_RATIO_Y);
        cropIntent.putExtra("outputX", 400);
        cropIntent.putExtra("outputY", 250);
        cropIntent.putExtra("return-data", true);
        startActivityForResult(cropIntent, CUT_PICTURE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case PICK_IMAGE:
                    cropCapturedImage(data.getData());
                    break;
                case CUT_PICTURE:
                    if (resultCode == RESULT_OK && data != null) {
                        Uri resultUri = data.getData();
                        try {
                            Bitmap picturePath = MediaStore.Images.Media
                                    .getBitmap(getActivity().getContentResolver(), resultUri);
                            imageViewProfile.setImageBitmap(picturePath);
                            new Storge(getFragmentManager(), "pictureProfile",
                                    new Authentication().getCurrentUser().getEmail(),
                                    getContext())
                                    .setProfile(resultUri);
                        } catch (IOException ignored) { }
                    }
                    break;
            }
        }
    }

    private void saveProfile() {
        String user;
        String id = new Authentication().getCurrentUser().getEmail();
        Map<String, Object> data = new HashMap<>();

        if (!radioButtonPatient.isChecked()) { user = "c"; }
        else { user = "b"; }

        data.put(id, true);
        data.put(Constants.ID, id);
        data.put(Constants.USER, user);
        data.put(Constants.NAME, textInputLayoutName.getEditText().getText().toString().trim());
        data.put(Constants.LAST_NAME, textInputLayoutLastName.getEditText()
                .getText().toString().trim());
        data.put(Constants.DATE, textInputLayoutDate.getEditText().getText().toString().trim());
        data.put(Constants.COUNTRY, spinnerCountry.getSelectedItem().toString().trim());
        data.put(Constants.GENDER, radioButtonWomen.isChecked());

        new Database(getFragmentManager(), getContext())
                .setDataUser(id, Constants.DOCUMENT_USER, data, user);
        getActivity().finish();
        startActivity(new Intent(getContext(), MainActivity.class));
    }

    private boolean verify() {
        boolean state = false;
        if (!textInputLayoutName.getEditText().getText().toString().isEmpty()) {
            if (!textInputLayoutLastName.getEditText().getText().toString().isEmpty()) {
                if (!textInputLayoutDate.getEditText().getText().toString().isEmpty()) {
                    state = true;
                } else { textInputLayoutDate.setError(getString(R.string.error_campo)); }
            } else { textInputLayoutLastName.setError(getString(R.string.error_campo)); }
        } else { textInputLayoutName.setError(getString(R.string.error_campo)); }
        return state;
    }
}
