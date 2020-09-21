package adbudh.spit.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;

import android.animation.Animator;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

import adbudh.spit.R;

public class Volunteer extends AppCompatActivity implements View.OnClickListener {

    private View background;
    private FloatingActionButton btn_back;
    private MaterialButton btn_add, btn_submit_list;
    private LinearLayoutCompat layoutList;
    private TextInputEditText vol_number;

    private static final int RESULT_PICK_CONTACT = 1;

    public static ArrayList<VolunteerData> arrVolunteerData = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volunteer);

        layoutList = findViewById(R.id.layout_list);

        btn_back = findViewById(R.id.btn_back_to_register);
        btn_add = findViewById(R.id.btn_add_vol);
        btn_submit_list = findViewById(R.id.btn_submit_list);
        vol_number = findViewById(R.id.text_vol_number);


        background = findViewById(R.id.background);
        if (savedInstanceState == null) {
            background.setVisibility(View.INVISIBLE);

            final ViewTreeObserver viewTreeObserver = background.getViewTreeObserver();

            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        circularRevealActivity();
                        background.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }

                });
            }

        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
            }
        });
        btn_submit_list.setOnClickListener(this);
        btn_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_add_vol:
                addAnotherVolunteer();
                break;

            case R.id.btn_submit_list:
                if(checkIfValidAndRead()) {
                    close();
                } else {
                    Log.d("------ false ------", "submit false");
                }
                break;
        }

    }

    private boolean checkIfValidAndRead() {
        arrVolunteerData.clear();
        boolean result = true;
        for(int i=0; i<layoutList.getChildCount(); i++) {
            View volunteerView = layoutList.getChildAt(i);
            TextInputLayout text_vol_name = volunteerView.findViewById(R.id.text_vol_name);
            TextInputEditText text_vol_number = volunteerView.findViewById(R.id.text_vol_number);
            TextInputLayout text_vol_job = volunteerView.findViewById(R.id.text_vol_job);

            VolunteerData volunteerData = new VolunteerData();

            if (!text_vol_name.getEditText().getText().toString().equals("")) {
                volunteerData.setVolunteer_name(text_vol_name.getEditText().getText().toString());
            } else {
                result = false;
            }

            if (!text_vol_number.getText().toString().equals("")) {
                volunteerData.setVolunteer_contact(text_vol_number.getText().toString());
            } else {
                result = false;
            }

            if (!text_vol_job.getEditText().getText().toString().equals("")) {
                volunteerData.setVolunteer_job(text_vol_job.getEditText().getText().toString());
            } else {
                result = false;
            }

            arrVolunteerData.add(volunteerData);
        }

        if (arrVolunteerData.size() == 0) {
            result = false;
            Toast.makeText(this, "Add Volunteers First!", Toast.LENGTH_LONG).show();
        } else if (!result) {
            Toast.makeText(this, "Incorrect Details!", Toast.LENGTH_LONG).show();
        }

        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == RESULT_PICK_CONTACT) {
                contactPicked(data);
            }
        } else {
            Toast.makeText(this, "Failed to select contact", Toast.LENGTH_LONG).show();
        }
    }

    private void contactPicked(Intent data) {
        vol_number = findViewById(R.id.text_vol_number);
        Cursor cursor;
        try {
            String phoneNo;
            Uri uri = data.getData();
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            int phoneIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            phoneNo = cursor.getString(phoneIndex);
            Log.d("----- phone ------", phoneNo.trim());
            vol_number.setText(phoneNo.trim());

        } catch (Exception e) { e.printStackTrace(); }
    }

    private void addAnotherVolunteer() {
        final View volunteerView = getLayoutInflater().inflate(R.layout.row_add_volunteer, null, false);
        TextInputLayout vol_name = volunteerView.findViewById(R.id.text_vol_name);
        TextInputEditText vol_number = volunteerView.findViewById(R.id.text_vol_number);
        TextInputLayout vol_job = volunteerView.findViewById(R.id.text_vol_job);
        ShapeableImageView btn_remove_vol = volunteerView.findViewById(R.id.btn_remove_vol);

        // adding volunteer to list

        btn_remove_vol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeVolunteer(volunteerView);
            }
        });

        vol_number.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                    startActivityForResult(intent, RESULT_PICK_CONTACT);
                }
            }
        });

        layoutList.addView(volunteerView);
    }

    private void removeVolunteer(View volunteerView) {
        layoutList.removeView(volunteerView);
    }

    private void circularRevealActivity() {
        int cx = background.getRight() - getDips(44);
        int cy = background.getBottom() - getDips(44);

        float finalRadius = Math.max(background.getWidth(), background.getHeight());

        Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                background,
                cx,
                cy,
                0,
                finalRadius);

        circularReveal.setDuration(1000);
        background.setVisibility(View.VISIBLE);
        circularReveal.start();

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

}