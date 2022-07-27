package com.amirul.progig;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.amirul.progig.object.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class UserProfile extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseFirestore firebaseFirestore;
    TextInputLayout name,phone,address;
    TextInputEditText editName,editPhone,editAddress;
    ImageView profileImage;
    Button btnSave, btnEdit,selectImage,btnUpload;
    final int take_photo = 100;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    FirebaseStorage storage=FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();
    String currentUID=user.getUid();
    Boolean clicked=false;
    Uri imageUri;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        name=findViewById(R.id.name);
        phone=findViewById(R.id.phone);
        address=findViewById(R.id.address);
        editName=findViewById(R.id.editName);
        editPhone=findViewById(R.id.editPhone);
        editAddress=findViewById(R.id.editAddress);
        btnSave=findViewById(R.id.btnSave);
        profileImage=findViewById(R.id.profileImage);
        selectImage=findViewById(R.id.selectImage);
        btnEdit=findViewById(R.id.btnEdit);
        btnUpload=findViewById(R.id.savePicture);

        DocumentReference docRef = db.collection("customer").document("Customer "+currentUID);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                            name.getEditText().setText(document.getData().get("name").toString());
                            phone.getEditText().setText(document.getData().get("phone").toString());
                            address.getEditText().setText(document.getData().get("address").toString());
                            String imageUri = document.getData().get("imageUri").toString();
                            Picasso.with(UserProfile.this).load(imageUri).into(profileImage);

                    } else {

                        Toast.makeText(UserProfile.this, currentUID, Toast.LENGTH_SHORT).show();

                    }
                } else {

                    Toast.makeText(UserProfile.this, "Failed to Connect", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void goBack(View view){

        Intent intent=new Intent(UserProfile.this,Profile.class);
        startActivity(intent);
    }

    public void editProfile(View View){

        if(clicked){

            btnEdit.setText("Edit");

            editName.setFocusableInTouchMode(false);
            editName.setFocusable(false);
            editPhone.setFocusableInTouchMode(false);
            editPhone.setFocusable(false);
            editAddress.setFocusableInTouchMode(false);
            editAddress.setFocusable(false);

            btnSave.setVisibility(View.INVISIBLE);
            btnUpload.setVisibility(View.INVISIBLE);
            selectImage.setVisibility(View.INVISIBLE);
            clicked=false;

        }else{

            btnSave.setVisibility(View.VISIBLE);
            selectImage.setVisibility(View.VISIBLE);
            btnUpload.setVisibility(View.VISIBLE);
            btnEdit.setText("Cancel");

            editName.setFocusableInTouchMode(true);
            editName.setFocusable(true);
            editPhone.setFocusableInTouchMode(true);
            editPhone.setFocusable(true);
            editAddress.setFocusableInTouchMode(true);
            editAddress.setFocusable(true);

            btnSave.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(android.view.View v) {
                    DocumentReference reference = db.collection("customer").document("Customer "+user.getUid());

                    reference
                            .update("name", name.getEditText().getText().toString());

                    reference
                            .update("phone", phone.getEditText().getText().toString());

                    reference
                            .update("address", address.getEditText().getText().toString());

                    Intent intent =new Intent(UserProfile.this,Profile.class);
                    startActivity(intent);
                }
            });

            clicked=true;
        }

    }

    public void uploadProfile(View view){

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

    public void saveProfile(View view){

        DocumentReference reference = db.collection("customer").document("Customer "+user.getUid());

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

    }
}