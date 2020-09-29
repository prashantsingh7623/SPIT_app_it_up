package adbudh.spit.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

import adbudh.spit.R;
import adbudh.spit.base.modal;

public class AllVolunteers extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private LinearLayoutCompat layout_list;
    private FloatingActionButton btn_close;
    private CoordinatorLayout background;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_volunteers);

        layout_list = findViewById(R.id.layout_all_vol);
        btn_close = findViewById(R.id.btn_vol_all_close);
        background = findViewById(R.id.background);

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Events");
        final String eventKey = getIntent().getStringExtra("eventKey");
        databaseReference.child(eventKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Iterator<DataSnapshot> iterator = snapshot.child("event_volunteers").getChildren().iterator();
                    Log.d("----children count", snapshot.child("event_volunteers").getChildrenCount() + "");
                    while (iterator.hasNext()) {
                        DataSnapshot dataSnapshot = iterator.next();
                        String name = dataSnapshot.child("volunteer_name").getValue()+"";
                        String number = dataSnapshot.child("volunteer_contact").getValue()+"";
                        Log.d("----children", name);
                        Log.d("----children", number);
                        allVolunteers(name, number);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
    }

    private int getDips(int dps) {
        Resources resources = getResources();
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dps,
                resources.getDisplayMetrics());
    }

    public void close() {
        int cx = background.getWidth() - getDips(44);
        int cy = background.getBottom() - getDips(44);

        float finalRadius = Math.max(background.getWidth(), background.getHeight());
        Animator circularReveal = ViewAnimationUtils.createCircularReveal(background, cx, cy, finalRadius, 0);

        circularReveal.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                background.setVisibility(View.INVISIBLE);
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        circularReveal.setDuration(500);
        circularReveal.start();
    }

    private void allVolunteers(String name, String number) {
        final View volunteerView = getLayoutInflater().inflate(R.layout.single_volunteer, null, false);
        TextView vol_name = volunteerView.findViewById(R.id.text_all_vol_name);
        TextView vol_contact = volunteerView.findViewById(R.id.text_all_vol_contact);
        vol_name.setText(name);
        vol_contact.setText(number);
        layout_list.addView(volunteerView);
    }
}
