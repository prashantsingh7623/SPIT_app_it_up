package adbudh.spit.base;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import adbudh.spit.R;

public class EventDetails extends AppCompatActivity {

    private FloatingActionButton btn_back;
    private ShapeableImageView main_img;
    private MaterialTextView event_name, event_desc, event_date, event_time, event_venue, event_committee,
            event_link, event_contact_name, event_contact_number;

    private DatabaseReference databaseReference;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        //        making full screen
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }



        btn_back = findViewById(R.id.float_btn_close);
        main_img = findViewById(R.id.main_img_container);
        event_name = findViewById(R.id.text_dummy_ename);
        event_desc = findViewById(R.id.text_dummy_desc);
        event_date = findViewById(R.id.text_dummy_date);
        event_time = findViewById(R.id.text_dummy_time);
        event_venue = findViewById(R.id.text_dummy_venue);
        event_committee = findViewById(R.id.text_dummy_committee);
        event_link = findViewById(R.id.text_dummy_link);
        event_contact_name = findViewById(R.id.text_dummy_contact_name);
        event_contact_number = findViewById(R.id.text_dummy_contact_number);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Events");
        final String eventKey = getIntent().getStringExtra("eventKey");
        databaseReference.child(eventKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String imageUri = snapshot.child("posterUri").getValue().toString();
                    String eName = snapshot.child("event_name").getValue().toString();
                    String eDesc = snapshot.child("event_desc").getValue().toString();
                    String eCommittee = snapshot.child("event_committee").getValue().toString();
                    String eCreatorName = snapshot.child("event_creator_name").getValue().toString();
                    String eCreatorNumber = snapshot.child("event_creator_number").getValue().toString();
                    String eDate = snapshot.child("event_date").getValue().toString();
                    String eTime = snapshot.child("event_time").getValue().toString();
                    String eVenue = snapshot.child("event_venue").getValue().toString();
                    String eLink = snapshot.child("form_link").getValue().toString();

                    Picasso.get().load(imageUri).into(main_img);
                    event_name.setText(eName);
                    event_desc.setText(eDesc);
                    event_date.setText(eDate);
                    event_time.setText(eTime);
                    event_venue.setText(eVenue);
                    event_committee.setText(eCommittee);
                    event_link.setText(eLink);
                    event_contact_name.setText(eCreatorName);
                    event_contact_number.setText(eCreatorNumber);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
