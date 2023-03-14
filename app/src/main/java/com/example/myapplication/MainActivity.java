package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String key_name = "name";
    private static final String key_age = "age";

    private EditText editTextnombre;
    private EditText editTextedad;
    private TextView textViewData;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("users");
    private DocumentReference noteRef = db.collection("users").document();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextnombre = findViewById(R.id.edit_text_nombre);
        editTextedad = findViewById(R.id.edit_text_edad);
        textViewData = findViewById(R.id.text_view_data);
    }

    public void saveNote(View v) {
        String name = editTextnombre.getText().toString();
        String age = String.valueOf(editTextedad.getText());
        int edadbuena = Integer.parseInt(age);

        Map<String, Object> note = new HashMap<>();
        note.put(key_name, name);
        note.put(key_age, edadbuena);

        noteRef.set(note)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "note saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "Error!", Toast.LENGTH_SHORT).show();
                        Log.d(TAG, e.toString());

                    }
                });


    }

    public void loadNote(View v){
        notebookRef.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data="";
                        for (QueryDocumentSnapshot documenSnapshop : queryDocumentSnapshots){
                           Note note =documenSnapshop.toObject(Note.class);

                           String name = note.getName();
                           long age = note.getAge();

                            data +=   name + "\nAge: " + age + "\n\n";

                        }
                        textViewData.setText(data);
                    }
                });
    }

}