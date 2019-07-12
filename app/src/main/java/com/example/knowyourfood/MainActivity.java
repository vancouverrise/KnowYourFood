package com.example.knowyourfood;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    DatabaseReference myRef = database.getReference();
    StorageReference storageRef = storage.getReference();

    Button button, dbbutton;
    ImageView imageView;
    CircleImageView c;

    String TAG = "DATABASE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        c = findViewById(R.id.profile_image);
        button = findViewById(R.id.btn_photo);
        dbbutton = findViewById(R.id.dbbutton);

        Map<String, Object> user = new HashMap<>();
        user.put("first", "Ada");
        user.put("last", "Lovelace");
        user.put("born", 1815);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CropImage.activity()
                        .
                        .setFixAspectRatio(true)
                        .start(MainActivity.this);
            }
        });
        dbbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                writeNewUser("841248149", "Галя", "vancouverrise@gmail.com");
            }
        });

    }

    private void writeNewUser(String userId, String name, String email) {
        User user = new User(name, email);

        myRef.child("users").child(userId).setValue(user);
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                System.out.println(resultUri.toString());

               c.setImageURI(resultUri);
               Uri file = resultUri;
               StorageReference ref = storageRef.child("images/" + file.getLastPathSegment());
                UploadTask uploadTask = ref.putFile(file);
                uploadTask.addOnSuccessListener(taskSnapshot -> writeNewUser("841248113", "R", result.get));


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }




}

