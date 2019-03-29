package com.example.emobilis.morningfirebase;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    EditText mName,mEmail,mId;
    Button mBtnSave,mBtnView;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mName = findViewById(R.id.edtName);
        mEmail = findViewById(R.id.edtMail);
        mId = findViewById(R.id.edtId);
        mBtnSave = findViewById(R.id.btnSave);
        mBtnView = findViewById(R.id.btnView);

        dialog = new ProgressDialog(this);
        dialog.setTitle("Saving");
        dialog.setMessage("Please wait...");

        mBtnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Create a unique value to form the table row
                long time = System.currentTimeMillis();
                //Create a table called users in the database
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users/"+time);

                //Check if the user has entered all the details
                String name = mName.getText().toString();
                String email = mEmail.getText().toString();
                String id = mId.getText().toString().trim();
                if (name.isEmpty()||email.isEmpty()||id.isEmpty()){
                    Toast.makeText(MainActivity.this, "Fill in all inputs", Toast.LENGTH_SHORT).show();
                }else {
                    //The code to save into the database
                    User mtu = new User(name,email,id,String.valueOf(time));
                    dialog.show();
                    ref.setValue(mtu).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            dialog.dismiss();
                        if (task.isSuccessful()){
                            Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                            mName.setText("");
                            mEmail.setText("");
                            mId.setText("");
                        }else {
                            Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                        }
                        }
                    });
                }
            }
        });
        mBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent view = new Intent(getApplicationContext(),UsersActivity.class);
                startActivity(view);

            }
        });

    }
}
