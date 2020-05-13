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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.exoesqueletov1.Constants;
import com.example.exoesqueletov1.R;
import com.example.exoesqueletov1.clases.Authentication;
import com.example.exoesqueletov1.clases.Database;
import com.example.exoesqueletov1.clases.Storge;
import com.example.exoesqueletov1.dialogs.DialogUpdateData;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private TextView textViewName;
    private TextView textViewUser;
    private TextView textViewDes;
    private TextView textViewMail;
    private TextView textViewAddress;
    private TextView textViewCell;
    private TextView textViewPhone;
    private TextView textViewSchool;
    private CircleImageView circleImageViewProfile;

    private static final int PICK_IMAGE = 1;
    private static final int CUT_PICTURE = 3535;
    private static final int ASPECT_RATIO_X = 1;
    private static final int ASPECT_RATIO_Y = 1;

    private String id;
    private Database database;

    public ProfileFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        textViewName = view.findViewById(R.id.text_profile_view_name);
        textViewUser = view.findViewById(R.id.text_profile_view_user);
        textViewDes = view.findViewById(R.id.text_profile_view_description);
        textViewMail = view.findViewById(R.id.text_profile_view_email);
        textViewAddress = view.findViewById(R.id.text_profile_view_address);
        textViewCell = view.findViewById(R.id.text_profile_view_cell);
        textViewPhone = view.findViewById(R.id.text_profile_view_phone);
        textViewSchool = view.findViewById(R.id.text_profile_view_school);
        circleImageViewProfile = view.findViewById(R.id.image_view_profile_view);
        LinearLayout linearLayoutSchool = view.findViewById(R.id.linear_profile_school);
        Button buttonOk = view.findViewById(R.id.button_profile_view_save);
        ImageView buttonChangeImage = view.findViewById(R.id.button_profile_view_image);

        id = new Authentication().getCurrentUser().getEmail();

        database = new Database(getFragmentManager(), getContext());
        database.getProfile(id, textViewName, textViewUser, textViewDes, textViewMail,
                textViewAddress, textViewCell, textViewPhone, textViewSchool,
                circleImageViewProfile, linearLayoutSchool);

        textViewDes.setOnClickListener(v -> dialog (textViewDes,
                getString(R.string.description), 0));

        textViewMail.setOnClickListener(v -> dialog (textViewMail,
                getString(R.string.correo), R.drawable.ic_mail));

        textViewAddress.setOnClickListener(v -> dialog (textViewAddress,
                getString(R.string.direcci_n), R.drawable.ic_place));

        textViewCell.setOnClickListener(v -> dialog (textViewCell,
                getString(R.string.celular), R.drawable.ic_phone_android));

        textViewPhone.setOnClickListener(v -> dialog (textViewPhone,
                getString(R.string.telefono), R.drawable.ic_phone));

        textViewSchool.setOnClickListener(v -> dialog (textViewSchool,
                getString(R.string.estudio), R.drawable.ic_school));

        buttonOk.setOnClickListener(v -> okOnClick());

        buttonChangeImage.setOnClickListener(v -> selectImage(getContext()));

        return view;
    }

    private void okOnClick() {
        String user = "a";
        Map<String, Object> data = new HashMap<>();

        if (textViewUser.getText().equals("Administrador")) { user = "a"; }
        if (textViewUser.getText().equals("Fisioterapeuta")) { user = "b"; }
        if (textViewUser.getText().equals("Paciente")) { user = "c"; }

        data.put(Constants.NAME, textViewName.getText().toString().trim());
        data.put(Constants.USER, user);
        data.put(Constants.DESCRIPTION, textViewDes.getText().toString().trim());
        data.put(Constants.EMAIL, textViewMail.getText().toString().trim());
        data.put(Constants.ADDRESS, textViewAddress.getText().toString().trim());
        data.put(Constants.CELL, textViewCell.getText().toString().trim());
        data.put(Constants.PHONE, textViewPhone.getText().toString().trim());
        data.put(Constants.SCHOOL, textViewSchool.getText().toString().trim());

        database.updateData(id, Constants.DOCUMENT_PROFILE, data);
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
                startActivityForResult(Intent.createChooser(gallery, getString(R.string.fto_de_perf_l)),  PICK_IMAGE);
            } if (options[item].equals("Cancel")) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void cropCapturedImage(Uri urlImagen) {
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
                            Bitmap picturePath = MediaStore.Images.Media.
                                    getBitmap(getActivity().getContentResolver(), resultUri);
                            circleImageViewProfile.setImageBitmap(picturePath);
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

    private void dialog(TextView textView, String string, int i) {
        DialogUpdateData updateData = new DialogUpdateData(string, i, textView);
        updateData.show(getFragmentManager(), "algo");
    }
}
