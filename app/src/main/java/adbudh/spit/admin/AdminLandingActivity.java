package adbudh.spit.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;;
import com.google.firebase.database.FirebaseDatabase;

import adbudh.spit.R;

public class AdminLandingActivity extends AppCompatActivity {

    private FloatingActionButton btn_add_event;
    private EditText search;
    private RecyclerView recyclerView;

    myAdapter myadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_landing);

//        making full screen
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        btn_add_event = findViewById(R.id.button_add_event);
        search = findViewById(R.id.edit_text_search);

        recyclerView = findViewById(R.id.recycler_view_poster);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        btn_add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterEvent.class));
            }
        });

        FirebaseRecyclerOptions<modal> options =
                new FirebaseRecyclerOptions.Builder<modal>()
                    .setQuery( FirebaseDatabase.getInstance().getReference().child("Events"), modal.class)
                    .build();


        myadapter = new myAdapter(options);
        recyclerView.setAdapter(myadapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        myadapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myadapter.startListening();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keyCode, event);
    }

}
