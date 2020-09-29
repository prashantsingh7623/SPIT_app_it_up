package adbudh.spit.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.res.ResourcesCompat;

import android.Manifest;
import android.animation.Animator;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.Timestamp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import adbudh.spit.R;

public class RegisterEvent extends AppCompatActivity {

    private FloatingActionButton btn_date, btn_time;
    private MaterialTextView text_date, text_time;
    private TextInputLayout input_event_name, input_event_desc,
            input_duration, input_venue, input_organising_committee, form_link, creator_name, creator_number;

    private SwitchMaterial switch_paid;

    private MaterialButton btn_upload, btn_create_event;
    private FloatingActionButton btn_add_volunteer;
    private Uri imageUri;

    private StorageReference mStorageRef;
    private DatabaseReference databaseReference;


    private String event_name, event_desc, event_date, event_time, event_duration, event_venue,
            event_committee, c_name, str_form;

    public static String c_number;

    Long date_number;

    private boolean isValid = true, isImageUploaded = false;

    private boolean event_paid;
    public static boolean event_created_successfully = false;

    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_event);

        //        making full screen
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        mStorageRef = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Events");

        input_event_name = findViewById(R.id.input_event_name);
        input_event_desc = findViewById(R.id.input_description);
        input_venue = findViewById(R.id.input_venue);
        input_duration = findViewById(R.id.input_duration);
        input_organising_committee = findViewById(R.id.input_committee_name);
        form_link = findViewById(R.id.text_form_link);
        creator_name = findViewById(R.id.text_creator_name);
        creator_number = findViewById(R.id.text_creator_number);
        form_link = findViewById(R.id.text_form_link);

        switch_paid = findViewById(R.id.switch_paid);

        btn_date = findViewById(R.id.input_date);
        btn_time = findViewById(R.id.input_time);
        btn_upload = findViewById(R.id.button_upload);
        btn_create_event = findViewById(R.id.button_create_event);
        btn_add_volunteer = findViewById(R.id.floating_add_volunteer);

        text_date = findViewById(R.id.text_date);
        text_time = findViewById(R.id.text_time);

        btn_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDateDialog(text_date);
            }
        });
        btn_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTimeDialog(text_time);
            }
        });
        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });
        btn_create_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createEvent();
            }
        });

        btn_add_volunteer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Volunteer.class));
            }
        });

    }

    private void createEvent() {
        isValid = validateForm();
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Creating Event");
        progressDialog.setCanceledOnTouchOutside(false);


        isValid = true;
        if(isValid && isImageUploaded) {
            progressDialog.show();
            event_paid = switch_paid.isChecked();
            mStorageRef.child(key+".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    HashMap<String, Object> map = new HashMap<>();
                    HashMap volunteer_map = new HashMap();
                    map.put("event_name", event_name);
                    map.put("event_desc", event_desc);
                    map.put("event_date", event_date);
                    map.put("date_number", date_number);
                    map.put("event_time", event_time);
                    map.put("event_duration", event_duration);
                    map.put("event_venue", event_venue);
                    map.put("event_committee", event_committee);
                    map.put("event_paid", event_paid+"");
                    map.put("posterUri", uri.toString());
                    map.put("event_creator_name", c_name);
                    map.put("event_creator_number", c_number);
                    map.put("form_link", str_form);

                    for(int i=0; i<Volunteer.arrVolunteerData.size(); i++) {
                        volunteer_map.put("volunteer" + i, Volunteer.arrVolunteerData.get(i));
                    }

                    map.put("event_volunteers", volunteer_map);

                    databaseReference.push().setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            event_created_successfully = true;
                            Toast.makeText(getApplicationContext(), "Event added successfully!", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), AdminLandingActivity.class));

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Operation failed!", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        } else { Toast.makeText(getApplicationContext(), "Please upload Event Poster!", Toast.LENGTH_LONG).show(); }
    }

    private boolean validateForm() {
        isValid = true;
        if(input_event_name.getEditText().getText().toString().equals("")) {
            input_event_name.setError("required!");
            isValid = false;
        } else {
            event_name = input_event_name.getEditText().getText().toString();
            input_event_name.setErrorEnabled(false);
        }

        if(text_date.getText().toString().equals("Date") || text_time.getText().toString().equals("Time")) {
            Toast.makeText(getApplicationContext(), "Date/Time is required!", Toast.LENGTH_LONG).show();
            isValid = false;
        } else {
            event_date = text_date.getText().toString();
            event_time = text_time.getText().toString();
        }

        if(input_event_desc.getEditText().getText().toString().equals("")) {
            input_event_desc.setError("required!");
            isValid = false;
        } else {
            event_desc = input_event_desc.getEditText().getText().toString();
            input_event_desc.setErrorEnabled(false);
        }


        if (input_duration.getEditText().getText().toString().equals("")) {
            input_duration.setError("required!");
            isValid = false;
        } else {
            event_duration = input_duration.getEditText().getText().toString();
            input_duration.setErrorEnabled(false);
        }

        if (input_venue.getEditText().getText().toString().equals("")) {
            input_venue.setError("required!");
            isValid = false;
        } else {
            event_venue = input_venue.getEditText().getText().toString();
            input_venue.setErrorEnabled(false);
        }

        if (input_organising_committee.getEditText().getText().toString().equals("")) {
            input_organising_committee.setError("required!");
            isValid = false;
        } else {
            event_committee = input_organising_committee.getEditText().getText().toString();
            input_organising_committee.setErrorEnabled(false);
        }

        if (creator_name.getEditText().getText().toString().equals("")) {
            creator_name.setError("required!");
            isValid = false;
        } else {
            c_name = creator_name.getEditText().getText().toString();
            creator_name.setErrorEnabled(false);
        }

        if (creator_number.getEditText().getText().toString().equals("")) {
            creator_number.setError("required!");
            isValid = false;
        } else {
            c_number = creator_number.getEditText().getText().toString();
            creator_number.setErrorEnabled(false);
        }

        if (form_link.getEditText().getText().toString().equals("")) {
            str_form = "";
        } else { str_form = form_link.getEditText().getText().toString(); }

        return isValid;
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            if (isValid) { uploadPoster(); }
            else { Toast.makeText(getApplicationContext(), "Form invalid!", Toast.LENGTH_LONG).show(); }
        }

    }

    private void uploadPoster() {

        final ProgressDialog  pd = new ProgressDialog(this);
        pd.setTitle("Uploading poster...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        key = UUID.randomUUID().toString();
//        final String key = input_event_name.getEditText().getText().toString();
        StorageReference riversRef = mStorageRef.child(key+".jpg");
        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Snackbar.make(findViewById(android.R.id.content), "Image Uploaded", Snackbar.LENGTH_LONG).show();
//                        shapeable_done.setVisibility(View.VISIBLE);
                        isImageUploaded = true;

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        pd.dismiss();
                        Toast.makeText(getApplicationContext(), "upload failed", Toast.LENGTH_LONG).show();

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                        double progressPercentage = (100.00 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        pd.setMessage("Percentage: " + (int)progressPercentage + "%");
                    }
                });
    }

    private void showTimeDialog(final MaterialTextView time_in) {
        final Calendar calendar=Calendar.getInstance();
        final Locale id = new Locale("in", "ID");
        TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                calendar.set(Calendar.MINUTE,minute);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("hh:mm aa", id);
                time_in.setText(simpleDateFormat.format(calendar.getTime()));
                text_time = time_in;
            }
        };

        new TimePickerDialog(RegisterEvent.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();
    }

    private void showDateDialog(final MaterialTextView date_in) {
        final Calendar calendar=Calendar.getInstance();
        final Locale id = new Locale("en", "IN");
        DatePickerDialog.OnDateSetListener dateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
                SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd-MMM-yyyy, EEE", id);
                date_in.setText(simpleDateFormat.format(calendar.getTime()));
                text_date = date_in;


                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy EEE", id);
                String future = sdf.format(calendar.getTime());
                String today = sdf.format(new Date());
                try {
                    Date d1 = sdf.parse(today);
                    Date d2 = sdf.parse(future);
                    date_number = (d2.getTime() - d1.getTime());
                    Log.d("----difference time----", (d2.getTime() - d1.getTime())+"");
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }
        };

        new DatePickerDialog(RegisterEvent.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
    }
}
