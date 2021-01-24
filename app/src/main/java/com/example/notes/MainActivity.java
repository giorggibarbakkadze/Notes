package com.example.notes;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    private FirebaseAuth auth;
    private GoogleSignInClient mGoogleSignInClient;
    private RecyclerView recycler_users;
    private DocumentReference mDocRef = FirebaseFirestore.getInstance().document("sampleData/Notes");
    private FirebaseFirestore firebasefirestore;

    ArrayList<products> dataList;
    myAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        recycler_users = findViewById(R.id.recycleView);
        recycler_users.setLayoutManager(new LinearLayoutManager(this));
        firebasefirestore = FirebaseFirestore.getInstance();
        loadData();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);






    }
    public void loadData(){
        firebasefirestore.collection("sampleData").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments() ;
                        for (DocumentSnapshot d:list){
                            products obj = d.toObject(products.class);
                            dataList.add(obj);
                        }
                        adapter.notifyDataSetChanged();

                    }
                });
        dataList = new ArrayList<>();
        adapter = new myAdapter(dataList);
        recycler_users.setAdapter(adapter);

    }
    public void addNotesActivity(View view){
        showAlertDialog();

    }
        private void showAlertDialog(){
            final String POPUP_Notes_Title="Type Notes";

            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle(POPUP_Notes_Title);
            final EditText noteTitle = new EditText(this);
            noteTitle.setHint("Title");
            final EditText noteDescription = new EditText(this);
            noteDescription.setHint("Description");
            LinearLayout layout = new LinearLayout(getApplicationContext());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(noteTitle);
            layout.addView(noteDescription);
            alert.setView(layout);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    setNote(noteTitle.getText().toString(), noteDescription.getText().toString());
                }

            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                }
            });

            alert.show();

        }
        public void setNote(String title, String description){
        products pr = new products(title, description);
        firebasefirestore.collection("sampleData")
                .add(pr)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.w("kaira", "Note has been added succesfully");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(MainActivity.this, "arao araaa", Toast.LENGTH_SHORT).show();

                    }
                });
        loadData();

        }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.logOut){
            Log.d("LogOut", "LogOut");
            FirebaseAuth.getInstance().signOut();
            mGoogleSignInClient.signOut();
            Intent logoutIntent = new Intent(this, LogIn.class);
            logoutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(logoutIntent);
            finish();

        }
        return super.onOptionsItemSelected(item);
    }






}