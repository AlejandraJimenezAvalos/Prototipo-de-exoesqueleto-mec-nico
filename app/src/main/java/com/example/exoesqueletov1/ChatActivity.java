package com.example.exoesqueletov1;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.exoesqueletov1.clases.Authentication;
import com.example.exoesqueletov1.clases.MessageAdapter;
import com.example.exoesqueletov1.clases.MessageItem;
import com.example.exoesqueletov1.clases.Storge;
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
    private ImageView imageViewBack;
    private ImageView imageViewMore;
    private ImageView imageViewSend;
    private RecyclerView recyclerView;
    private TextInputEditText editTextMessage;
    private CircleImageView circleImageViewProfile;
    private boolean stateTimer = true;
    private boolean stateChange = false;

    private FirebaseFirestore db;
    private String idChat;
    private String id;

    private List<MessageItem> mData;

    private static final String NAME = "name";
    private static final String DOCUMENT_PROFILE = "profile";
    private static final String ID_SPECIALIST = "idSpecialist";
    private static final String ID_CHAT = "idChat";
    private static final String FROM = "from";
    private static final String MESSAGE = "message";
    private static final String HOUR = "hour";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Timer timer = new Timer();
        timer.execute();

        Bundle data = this.getIntent().getExtras();
        idChat = data.getString(ID_CHAT);
        String idUser = data.getString(ID_SPECIALIST);
        id = new Authentication().getCurrentUser().getEmail();

        db = FirebaseFirestore.getInstance();

        textViewName = findViewById(R.id.text_activity_chat_name);
        imageViewBack = findViewById(R.id.image_activity_chat_back);
        imageViewMore = findViewById(R.id.image_activity_chat_more);
        imageViewSend = findViewById(R.id.image_view_send);
        recyclerView = findViewById(R.id.recycler_chats_activity);
        editTextMessage = findViewById(R.id.edit_text_message);
        circleImageViewProfile = findViewById(R.id.circle_image_view_profile);

        new Storge().getProfileImage(circleImageViewProfile, idUser);

        db.collection(idUser).document(DOCUMENT_PROFILE).get().
                addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                textViewName.setText(documentSnapshot.getData().get(NAME).toString());
            }
        });

        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ChatActivity.this, MainActivity.class));
                finish();
            }
        });

        imageViewSend.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SimpleDateFormat")
            @Override
            public void onClick(View v) {
                if (!editTextMessage.getText().toString().trim().isEmpty()){
                    String message = editTextMessage.getText().toString().trim();
                    Map<String, Object> data = new HashMap<>();
                    data.put(MESSAGE, message);
                    data.put(HOUR, new SimpleDateFormat("HH:mm:ss").format(new Date()));
                    data.put(FROM, id);
                    data.put(ID_CHAT, mData.size() + 1);
                    db.collection(idChat).add(data);
                    editTextMessage.setText("");
                    updateMessages();
                }
            }
        });

        updateMessages();
    }

    private void updateMessages() {
        mData = new ArrayList<>();
            db.collection(idChat).orderBy(ID_CHAT, Query.Direction.ASCENDING).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    for (QueryDocumentSnapshot document : task.getResult()){
                        try {
                            String message = document.getData().get(MESSAGE).toString();
                            String hour = document.getData().get(HOUR).toString();
                            boolean isMyMessage = document.getData().get(FROM).toString().equals(id);
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
