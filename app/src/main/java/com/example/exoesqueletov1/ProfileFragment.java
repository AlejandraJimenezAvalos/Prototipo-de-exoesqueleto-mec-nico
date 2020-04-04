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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.example.exoesqueletov1.dialog.DialogUpdateData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;

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
    private LinearLayout linearLayoutSchool;
    private Button buttonOk;
    private ImageView buttonChangeImage;

    private static final String COLLECTION_USERS = "users";
    private static final long ONE_MEGABYTE = 1024 * 1024;
    private static final String USER = "user";
    private static final String NAME = "name";
    private static final String ADDRESS = "address";
    private static final String CELL = "cell";
    private static final String PHONE = "phone";
    private static final String EMAIL = "email";
    private static final String SCHOOL = "school";
    private static final String DESCRIPTION = "description";
    private static final String DOCUMENT_PROFILE = "profile";
    private static final String PICTURE_PROFILE = "pictureProfile";
    private static final int PICK_IMAGE = 1;
    private static final int CUT_PICTURE = 3535;
    private static final int ASPECT_RATIO_X = 1;
    private static final int ASPECT_RATIO_Y = 1;
    private String user;

    public ProfileFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
        linearLayoutSchool = view.findViewById(R.id.linear_profile_school);
        buttonOk = view.findViewById(R.id.button_profile_view_save);
        buttonChangeImage = view.findViewById(R.id.button_profile_view_image);

        updateProfile ();

        textViewDes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog (textViewDes, getString(R.string.description), 0);
            }
        });

        textViewMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog (textViewMail, getString(R.string.correo), R.drawable.ic_mail);
            }
        });

        textViewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog (textViewAddress, getString(R.string.direcci_n), R.drawable.ic_place);
            }
        });

        textViewCell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog (textViewCell, getString(R.string.celular), R.drawable.ic_phone_android);
            }
        });

        textViewPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog (textViewPhone, getString(R.string.telefono), R.drawable.ic_phone);
            }
        });

        textViewSchool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog (textViewSchool, getString(R.string.estudio), R.drawable.ic_school);
            }
        });

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object> data = new HashMap<>();
                data.put(NAME, textViewName.getText().toString().trim());
                data.put(USER, user);
                data.put(DESCRIPTION, textViewDes.getText().toString().trim());
                data.put(EMAIL, textViewMail.getText().toString().trim());
                data.put(ADDRESS, textViewAddress.getText().toString().trim());
                data.put(CELL, textViewCell.getText().toString().trim());
                data.put(PHONE, textViewPhone.getText().toString().trim());
                data.put(SCHOOL, textViewSchool.getText().toString().trim());
                Database database = new Database(getFragmentManager(), getContext());
                database.updateData(new Authentication().getCurrentUser().getEmail(), DOCUMENT_PROFILE, data);
                updateProfile();
            }
        });

        buttonChangeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(getContext());
            }
        });

        return view;
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

    private void updateProfile() {
        final String id = new Authentication().getCurrentUser().getEmail();

        FirebaseFirestore.getInstance().collection(id).document(DOCUMENT_PROFILE).
                get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String typeUser = "";

                user = documentSnapshot.getData().get(USER).toString();
                if (user.equals("a")) { typeUser = "Administrador"; }
                if (user.equals("b")) { typeUser = "Fisioterapeuta"; }
                if (user.equals("c")) {
                    typeUser = "Paciente";
                    linearLayoutSchool.setVisibility(View.INVISIBLE);
                }
                textViewName.setText(documentSnapshot.getData().get(NAME).toString());
                textViewUser.setText(typeUser);
                textViewDes.setText(documentSnapshot.getData().get(DESCRIPTION).toString());
                textViewMail.setText(documentSnapshot.getData().get(EMAIL).toString());
                textViewAddress.setText(documentSnapshot.getData().get(ADDRESS).toString());
                textViewCell.setText(documentSnapshot.getData().get(CELL).toString());
                textViewPhone.setText(documentSnapshot.getData().get(PHONE).toString());
                textViewSchool.setText(documentSnapshot.getData().get(SCHOOL).toString());

                FirebaseStorage.getInstance().getReference().child(PICTURE_PROFILE).child(id)
                        .getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(byte[] bytes) {
                        circleImageViewProfile.setImageBitmap(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
                    }
                });
            }
        });
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
                            circleImageViewProfile.setImageBitmap(picturePath);
                            new Storge(getFragmentManager(), "pictureProfile", new Authentication().getCurrentUser().getEmail(),
                                    getContext())
                                    .setDocument(resultUri);
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
