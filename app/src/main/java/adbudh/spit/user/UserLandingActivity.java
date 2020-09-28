package adbudh.spit.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import adbudh.spit.R;
import adbudh.spit.admin.CompletedEvents;
import adbudh.spit.base.LoginActivity;
import adbudh.spit.admin.UpcomingEvents;

public class UserLandingActivity extends AppCompatActivity implements UserUpcomingEvents.OnFragmentInteractionListener, UserCompletedEvents.OnFragmentInteractionListener {

    private FloatingActionButton image_btn_logout;
    private FirebaseAuth firebaseAuth;

    public static Context mContext;

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private UserCompletedEvents completedFragment;
    private UserUpcomingEvents upcomingFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_landing);

        mContext = getBaseContext();

        firebaseAuth = FirebaseAuth.getInstance();
        image_btn_logout = findViewById(R.id.image_btn_logout);

//        making full screen
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().getAttributes().layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);

        completedFragment = new UserCompletedEvents();
        upcomingFragment = new UserUpcomingEvents();

        tabLayout.setupWithViewPager(viewPager);

        UserLandingActivity.ViewPagerAdapter viewPagerAdapter = new UserLandingActivity.ViewPagerAdapter(getSupportFragmentManager(), 0);
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

        image_btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(UserLandingActivity.this);
                builder.setMessage("Are you sure you want to exit!");
                builder.setCancelable(false);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        firebaseAuth.signOut();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                AlertDialog alertDialog = builder.create();
                alertDialog.show();
            }
        });

    }

    public static Context getContext() {
        return mContext;
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
