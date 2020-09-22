package adbudh.spit.admin;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import adbudh.spit.R;

class myViewHolder extends RecyclerView.ViewHolder {
    public static View v;
    ImageView poster;
    TextView title, date;

    public myViewHolder(@NonNull View itemView) {
        super(itemView);
        poster = itemView.findViewById(R.id.imageView_poster);
        title = itemView.findViewById(R.id.text_event_name);
        date = itemView.findViewById(R.id.text_event_date);
        v = itemView;
    }
}
