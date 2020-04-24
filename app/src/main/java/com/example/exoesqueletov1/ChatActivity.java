package com.example.exoesqueletov1;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exoesqueletov1.clases.Authentication;
import com.example.exoesqueletov1.clases.Database;
import com.example.exoesqueletov1.clases.MessageAdapter;
import com.example.exoesqueletov1.clases.MessageItem;
import com.example.exoesqueletov1.clases.Storge;
import com.example.exoesqueletov1.dialogs.DialogContact;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends AppCompatActivity {

    private TextView textViewName;
    private RecyclerView recyclerView;
    private TextInputEditText editTextMessage;
    private boolean stateTimer = true;

    private FirebaseFirestore db;
    private String idChat;
    private String id;
    private String idUserTo;
    private String typeUser;

    private List<MessageItem> mData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ImageView imageViewBack = findViewById(R.id.image_activity_chat_back);
        ImageView imageViewMore = findViewById(R.id.image_activity_chat_more);
        ImageView imageViewSend = findViewById(R.id.image_view_send);

        initComponents();

        getNameAndPhoto((CircleImageView) findViewById(R.id.circle_image_view_profile));

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backToMain();
            }
        });

        imageViewSend.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        imageViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMore();
            }
        });
        updateMessages();
    }

    private void initComponents() {
        Timer timer = new Timer();
        Bundle data = this.getIntent().getExtras();

        timer.execute();
        idChat = data.getString(Database.ID_CHAT);
        idUserTo = data.getString(Database.ID_SPECIALIST);
        id = new Authentication().getCurrentUser().getEmail();
        typeUser = data.getString(Database.USER);

        db = FirebaseFirestore.getInstance();

        textViewName = findViewById(R.id.text_activity_chat_name);
        recyclerView = findViewById(R.id.recycler_chats_activity);
        editTextMessage = findViewById(R.id.edit_text_message);
    }

    private void getNameAndPhoto(CircleImageView circleImageViewProfile) {
        new Storge().getProfileImage(circleImageViewProfile, idUserTo);

        db.collection(idUserTo).document(Database.DOCUMENT_PROFILE).get().
                addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        textViewName.setText(documentSnapshot.getData().get(Database.NAME).
                                toString());
                    }
                });
    }

    private void backToMain() {
        startActivity(new Intent(ChatActivity.this, MainActivity.class));
        finish();
    }

    private void showMore() {
        final CharSequence[] options = { getString(R.string.vaciar_el_chat),
                getString(R.string.ver_contacto), getString(R.string.cancelar) };

        AlertDialog.Builder builder = new AlertDialog.Builder(ChatActivity.this);
        builder.setTitle(R.string.opciones);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals(getString(R.string.vaciar_el_chat))) {
                    db.collection(idChat).get()
                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                    for (DocumentSnapshot documentSnapshot : task.getResult()) {
                                        db.collection(idChat).document(documentSnapshot.getId()).delete();
                                    }
                                }
                            });
                }
                if (options[item].equals(getString(R.string.ver_contacto))) {
                    DialogContact dialogContact = new DialogContact(id, idUserTo, idChat, typeUser);
                    dialogContact.show(getSupportFragmentManager(), "");
                }
                if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void updateMessages() {
        mData = new ArrayList<>();
            db.collection(idChat).orderBy(Database.NO, Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot document : task.getResult()){
                        try {
                            String message = document.getData().get(Database.MESSAGE).toString();
                            String hour = document.getData().get(Database.HOUR).toString();
                            boolean isMyMessage = document.getData().get(Database.FROM).toString().equals(id);
                            mData.add(new MessageItem(message, hour, isMyMessage));
                        } catch (NullPointerException ignored) { }
                    }

                    MessageAdapter notificationsAdapter;
                    notificationsAdapter = new MessageAdapter(ChatActivity.this, mData, mData);
                    recyclerView.setAdapter(notificationsAdapter);
                    recyclerView.setHasFixedSize(true);
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                    linearLayoutManager.setStackFromEnd(true);
                    recyclerView.setLayoutManager(linearLayoutManager);
                }
            });
    }

    @SuppressLint("SimpleDateFormat")
    private void sendMessage() {
        if (!editTextMessage.getText().toString().trim().isEmpty()){
            String message = editTextMessage.getText().toString().trim();
            Map<String, Object> data = new HashMap<>();
            data.put(Database.MESSAGE, message);
            data.put(Database.HOUR, new SimpleDateFormat("HH:mm").format(new Date()));
            data.put(Database.FROM, id);
            data.put(Database.NO, mData.size() + 1);
            db.collection(idChat).add(data);
            editTextMessage.setText("");
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class Timer extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            Timer timer = new Timer();
            timer.execute();
            if (stateTimer) {
                db.collection(idChat).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> document = queryDocumentSnapshots.getDocuments();
                        if (document.size() != mData.size()) { updateMessages(); }
                    }
                });
            }
        }

        @Override
        protected Boolean doInBackground(Void... voids) {
            for (int i = 1; i <= 1; i++) { try { Thread.sleep(500); } catch (InterruptedException ignored) { } }
            return null;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        stateTimer = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        stateTimer = false;
    }
}
