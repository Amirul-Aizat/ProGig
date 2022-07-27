package com.amirul.progig;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.amirul.progig.object.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FillProfile extends AppCompatActivity {

    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    String currentUID=user.getUid();
    TextInputLayout name, phone, address;
    Button btnSave, selectImage;
    ImageView profileImage;
    FirebaseFirestore firebaseFirestore;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    Uri imageUri;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_profile);

        FirebaseUser currentUser = mAuth.getCurrentUser();

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        btnSave = findViewById(R.id.btnSave);
        profileImage = findViewById(R.id.profileImage);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name.setError(null);
                phone.setError(null);
                address.setError(null);

                String username = name.getEditText().getText().toString();
                String userphone = phone.getEditText().getText().toString();
                String useraddress = address.getEditText().getText().toString();



                if (username.isEmpty() || userphone.isEmpty() || useraddress.isEmpty()) {
                    address.setError("Please fill all the details");
                } else {

                    DocumentReference reference = db.collection("customer").document("Customer " + currentUser.getUid());

                    storageReference= FirebaseStorage.getInstance().getReference("images/"+currentUID);

                    storageReference.putFile(imageUri)
                            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @Override
                                        public void onSuccess(Uri uri) {

                                            profileImage.setImageURI(null);
                                            reference
                                                    .update("imageUri",uri.toString());
                                        }
                                    });


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });



                    reference
                            .update("name", username);

                    reference
                            .update("phone", userphone);

                    reference
                            .update("address", useraddress);

                    reference
                            .update("complete", true);



                    Intent intent = new Intent(FillProfile.this, Dashboard.class);
                    startActivity(intent);
                }


            }
        });

    }

    public void chooseImage(View view) {

        pickImage();

    }

    private void pickImage() {

        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData() != null) {

            imageUri=data.getData();
            profileImage.setImageURI(imageUri);

        }
    }

//    private void uploadImage(){
//
//        storageReference= FirebaseStorage.getInstance().getReference("images/"+currentUID);
//
//        storageReference.putFile(imageUri)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                        profileImage.setImageURI(null);
//                        Toast.makeText(FillProfile.this, currentUID, Toast.LENGTH_SHORT).show();
//
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });
//    }
}