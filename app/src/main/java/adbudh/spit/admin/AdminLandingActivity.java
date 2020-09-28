package adbudh.spit.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import adbudh.spit.R;

public class AdminLandingActivity extends AppCompatActivity implements UpcomingEvents.OnFragmentInteractionListener, CompletedEvents.OnFragmentInteractionListener{

    private FloatingActionButton btn_add_event;
    private ShapeableImageView image_btn_logout;
    private FirebaseAuth firebaseAuth;

    public static Context mContext;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private CompletedEvents completedFragment;
    private UpcomingEvents upcomingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_landing);

        mContext = getBaseContext();

        firebaseAuth = FirebaseAuth.getInstance();
        image_btn_logout = findViewById(R.id.image_logout);

//        making full screen
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        completedFragment = new CompletedEvents();
        upcomingFragment = new UpcomingEvents();

        tabLayout.setupWithViewPager(viewPager);

        AdminLandingActivity.ViewPagerAdapter viewPagerAdapter = new AdminLandingActivity.ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.addFragment(upcomingFragment, "Upcoming");
        viewPagerAdapter.addFragment(completedFragment, "Completed");
        viewPager.setAdapter(viewPagerAdapter);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_explore_black);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_completed_black);
        tabLayout.getTabAt(0).getIcon().setTint(getResources().getColor(R.color.white, getTheme()));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                tab.getIcon().setTint(getResources().getColor(R.color.white, getTheme()));
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.getIcon().setTint(getResources().getColor(R.color.black, getTheme()));
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        btn_add_event = findViewById(R.id.button_add_event);
        btn_add_event.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterEvent.class));
            }
        });

        image_btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        if(RegisterEvent.event_created_successfully) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                sendSMS();
            } else {
                requestPermissions(new String[]{Manifest.permission.SEND_SMS}, 1);
            }
        }

    }

    public static Context getContext() {
        return mContext;
    }

    private void sendSMS() {
        for(int i=0; i<Volunteer.arrVolunteerData.size(); i++) {
            String phone_no = Volunteer.arrVolunteerData.get(i).volunteer_contact.trim();
            String job = Volunteer.arrVolunteerData.get(i).volunteer_job;
            String user_name = Volunteer.arrVolunteerData.get(i).volunteer_name;
            String message = "Hello " + user_name +
                    "\nYou are assigned as volunteer for the " + Volunteer.arrVolunteerData.get(i).getVolunteer_name() + " event" +
                    "\nYour assigned task is - \n" + job;

            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(phone_no, null, message, null, null);
                Toast.makeText(getApplicationContext(), "Volunteers are informed", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error sending message!", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragmentsList = new ArrayList<>();
        private List<String> fragmentTitle = new ArrayList<>();

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm, behavior);
        }

        public void addFragment(Fragment fragment, String title) {
            fragmentsList.add(fragment);
            fragmentTitle.add(title);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentsList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentsList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitle.get(position);
        }
    }

}
