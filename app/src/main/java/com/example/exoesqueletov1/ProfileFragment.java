package com.example.exoesqueletov1;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.exoesqueletov1.clases.Authentication;
import com.example.exoesqueletov1.clases.Database;
import com.example.exoesqueletov1.clases.Storge;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private TextInputLayout textInputLayoutName;
    private TextInputLayout textInputLayoutLastName;
    private TextInputLayout textInputLayoutDate;
    private Spinner spinnerCountry;
    private RadioButton radioButtonWomen;
    private RadioButton radioButtonMen;
    private RadioButton radioButtonDoctor;
    private RadioButton radioButtonPatient;
    private CircleImageView imageViewProfile;
    private View view;

    private static final int PICK_IMAGE = 1;
    private static final int CUT_PICTURE = 3535;
    private static final int ASPECT_RATIO_X = 1;
    private static final int ASPECT_RATIO_Y = 1;
    private static final String DOCUMENT_USER = "user";
    private static final long ONE_MEGABYTE = 1024 * 1024;

    private boolean state = true;

    public ProfileFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Button save;

        view = inflater.inflate(R.layout.fragment_profile, container, false);

        textInputLayoutName = view.findViewById(R.id.edit_text_nombre);
        textInputLayoutLastName = view.findViewById(R.id.edit_text_apellidos);
        textInputLayoutDate = view.findViewById(R.id.edit_text_date);
        spinnerCountry = view.findViewById(R.id.spinner_contry);
        radioButtonWomen = view.findViewById(R.id.radio_women);
        radioButtonPatient = view.findViewById(R.id.radio_pasiente);
        imageViewProfile = view.findViewById(R.id.image_perfil_profile);
        save = view.findViewById(R.id.button_save_profile);
        radioButtonDoctor = view.findViewById(R.id.radio_doctor);
        radioButtonMen = view.findViewById(R.id.radio_men);

        imageViewProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(getContext());
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "hola", Toast.LENGTH_SHORT).show();
                if (state) {
                    textInputLayoutName.setError(null);
                    textInputLayoutLastName.setError(null);
                    textInputLayoutDate.setError(null);
                    if (verify()) { saveProfile(); }
                } else update ();
            }
        });

        verifyStateUser();

        return view;
    }

    private void verifyStateUser() {
        final String id = new Authentication().getCurrentUser().getEmail();
        FirebaseFirestore.getInstance().collection(id).document(DOCUMENT_USER).get().
                addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Map<String, Object> data = documentSnapshot.getData();
                        try {
                            if (Boolean.parseBoolean(String.valueOf(data.get(id)))) {
                                state = false;

                                TextView textViewCountry = view.findViewById(R.id.text_country_profile);
                                TextView textViewType = view.findViewById(R.id.text_type_user_profile);

                                spinnerCountry.setVisibility(View.INVISIBLE);
                                textViewCountry.setVisibility(View.INVISIBLE);
                                textViewType.setVisibility(View.INVISIBLE);
                                radioButtonDoctor.setVisibility(View.INVISIBLE);
                                radioButtonPatient.setVisibility(View.INVISIBLE);
                                radioButtonWomen.setClickable(false);
                                radioButtonMen.setClickable(false);
                                textInputLayoutDate.getEditText().setEnabled(false);

                                if (!Boolean.parseBoolean(data.get("gender").toString())) {
                                    radioButtonWomen.setChecked(false);
                                    radioButtonMen.setChecked(true);
                                }

                                textInputLayoutName.getEditText().setText(data.get("name").toString());
                                textInputLayoutLastName.getEditText().setText(data.get("lastName").toString());
                                textInputLayoutDate.getEditText().setText(data.get("date").toString());

                                FirebaseStorage.getInstance().getReference().child("pictureProfile").child(id)
                                        .getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        imageViewProfile.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                                    }
                                });

                            }
                        } catch (Exception ignored) {  }
                    }
                });
    }

    private void update() {

    }

    private void selectImage(Context context) {
        final CharSequence[] options = { getString(R.string.chose_from_gallery), getString(R.string.cancelar) };

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getString(R.string.fto_de_perf_l));

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (options[item].equals(getString(R.string.chose_from_gallery))) {
                    Intent gallery = new Intent();
                    gallery.setType("image/*");
                    gallery.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(gallery, getString(R.string.fto_de_perf_l)),  PICK_IMAGE);
                } if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void cropCapturedImage(Uri urlImagen){
        //inicializamos nuestro intent
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        cropIntent.setDataAndType(urlImagen, "image/*");
        //Habilitamos el crop en este intent
        cropIntent.putExtra("crop", "true");
        cropIntent.putExtra("aspectX", ASPECT_RATIO_X);
        cropIntent.putExtra("aspectY", ASPECT_RATIO_Y);
        //indicamos los limites de nuestra imagen a cortar
        cropIntent.putExtra("outputX", 400);
        cropIntent.putExtra("outputY", 250);
        //True: retornara la imagen como un bitmap, False: retornara la url de la imagen la guardada.
        cropIntent.putExtra("return-data", true);
        //iniciamos nuestra activity y pasamos un codigo de respuesta.
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
                            Bitmap picturePath = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);
                            imageViewProfile.setImageBitmap(picturePath);
                            new Storge(getFragmentManager(), "pictureProfile", new Authentication().getCurrentUser().getEmail(),
                                    getContext())
                                    .setDocument(resultUri);
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

        data.put(id, true);
        data.put("id", id);
        data.put("name", textInputLayoutName.getEditText().getText().toString().trim());
        data.put("lastName", textInputLayoutLastName.getEditText().getText().toString().trim());
        data.put("date", textInputLayoutDate.getEditText().getText().toString().trim());
        data.put("country", spinnerCountry.getSelectedItem().toString().trim());
        data.put("gender", radioButtonWomen.isChecked());
        if (!radioButtonPatient.isChecked()) { user = "c"; }
        else { user = "b"; }

        new Database(getFragmentManager(), getContext()).setDataUser(id, DOCUMENT_USER, data, user);
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
