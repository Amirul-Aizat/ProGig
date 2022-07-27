package com.amirul.progig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.amirul.progig.object.Service;
import com.amirul.progig.object.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class RequestBooking extends AppCompatActivity {

    User user1;
    String serviceID;
    TextInputLayout selected_date, selected_time, message;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterItems;
    RatingBar review;
    String[] items = {"8.00 AM", "9.00 AM", "10.00 AM", "11.00 AM", "12.00 PM", "1.00 PM", "2.00 PM", "3.00 PM", "4.00 PM", "5.00 PM"};
    Button hire;
    TextView category, name, phone, address, gig_name, service_id, gig_id, service_name, location, service_rate;
    TextInputEditText date2;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_booking);

        serviceID = getIntent().getStringExtra("Hold ID");

        autoCompleteTextView = findViewById(R.id.menuAutocomplete);
        adapterItems = new ArrayAdapter<String>(this, R.layout.list_item, items);
        autoCompleteTextView.setAdapter(adapterItems);

        date2 = findViewById(R.id.date2);
        selected_date = findViewById(R.id.date);
        selected_time = findViewById(R.id.time);
        message = findViewById(R.id.message);
        hire = findViewById(R.id.hire);
        review = findViewById(R.id.review);
        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        address = findViewById(R.id.address);
        gig_id = findViewById(R.id.gig_id);
        location = findViewById(R.id.location);
        gig_name = findViewById(R.id.gig_name);
        service_id = findViewById(R.id.service_id);
        category = findViewById(R.id.category);
        location = findViewById(R.id.location);
        service_name = findViewById(R.id.service_name);
        service_rate = findViewById(R.id.service_rate);


        //read data customer
        DocumentReference docRef = db.collection("customer").document("Customer " + user.getUid());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        name.setText(document.getData().get("name").toString());
                        phone.setText(document.getData().get("phone").toString());
                        address.setText(document.getData().get("address").toString());

                    } else {

                    }
                } else {
                    Toast.makeText(RequestBooking.this, "Failed to Connect", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //read data from collection service
        DocumentReference docRef2 = db.collection("service").document(serviceID);
        docRef2.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {

                        gig_name.setText(document.getData().get("gig_name").toString());
                        service_id.setText(document.getData().get("service_ID").toString());
                        gig_id.setText(document.getData().get("gigID").toString());
                        category.setText(document.getData().get("category").toString());
                        review.setRating(Float.parseFloat(document.getData().get("review").toString()));
                        service_name.setText(document.getData().get("service_name").toString());
                        service_rate.setText(document.getData().get("serviceRate").toString());
                        location.setText(document.getData().get("service_area").toString());

                    } else {

                    }
                } else {
                    Toast.makeText(RequestBooking.this, "Failed to Connect", Toast.LENGTH_SHORT).show();
                }
            }
        });


        Calendar kalendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        kalendar.clear();

        Long today = MaterialDatePicker.todayInUtcMilliseconds();

        kalendar.setTimeInMillis(today);

        CalendarConstraints.Builder constraintBuilder = new CalendarConstraints.Builder();
        constraintBuilder.setValidator(DateValidatorPointForward.now());

        MaterialDatePicker.Builder calendar = MaterialDatePicker.Builder.datePicker();
        calendar.setTitleText("SELECT A DATE");
        calendar.setSelection(today);
        calendar.setCalendarConstraints(constraintBuilder.build());
        final MaterialDatePicker materialDatePicker = calendar.build();

        date2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialDatePicker.show(getSupportFragmentManager(), "DATE_PICKER");
            }
        });

        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                selected_date.getEditText().setText(materialDatePicker.getHeaderText());
            }
        });

        hire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isValidate()) {

                    Map<String, Object> booking = new HashMap<>();
                    booking.put("booking_date", selected_date.getEditText().getText().toString());
                    booking.put("booking_time", selected_time.getEditText().getText().toString());
                    booking.put("message", message.getEditText().getText().toString());
                    booking.put("customerID", user.getUid());
                    booking.put("serviceID", serviceID);
                    booking.put("gigID", gig_id.getText().toString());
                    booking.put("gig_name", gig_name.getText().toString());
                    booking.put("location", location.getText().toString());
                    booking.put("service_name", service_name.getText().toString());
                    booking.put("status", "Pending");
                    booking.put("customer_name",name.getText().toString());

                    db.collection("booking").add(booking).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {

                            documentReference.update("bookingID", documentReference.getId());
                            Log.e("TAG", documentReference.getId());
                            Intent intent = new Intent(RequestBooking.this, Dashboard.class);
                            startActivity(intent);
                        }
                    });

                }
            }
        });

    }

    public boolean isValidate() {
        if (selected_date.getEditText().getText().toString().isEmpty() || selected_time.getEditText().toString().isEmpty()) {
            selected_time.setError("Please fill all the details");
            return false;
        } else {

        }
        return true;
    }


}