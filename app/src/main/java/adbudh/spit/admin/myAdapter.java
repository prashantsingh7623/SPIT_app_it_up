package adbudh.spit.admin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import adbudh.spit.R;
import adbudh.spit.base.EventDetails;
import adbudh.spit.base.modal;

public class myAdapter extends FirebaseRecyclerAdapter<modal, myViewHolder> {


    public myAdapter(@NonNull FirebaseRecyclerOptions<modal> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, final int position, @NonNull modal model) {
        final Context context = AdminLandingActivity.getContext();
        holder.title.setText(model.getEvent_name());
        holder.date.setText(model.getEvent_date());
        Picasso.get().load(model.getPosterUri()).into(holder.poster);

        myViewHolder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EventDetails.class);
                intent.putExtra("eventKey", getRef(position).getKey());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        MaterialButton btn_see_vol = myViewHolder.v.findViewById(R.id.btn_see_volunteers);
        btn_see_vol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AllVolunteers.class);
                intent.putExtra("eventKey", getRef(position).getKey());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_poster, parent, false);
        return new myViewHolder(view);
    }

}
