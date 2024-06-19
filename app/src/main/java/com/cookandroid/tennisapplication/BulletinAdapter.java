package com.cookandroid.tennisapplication;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BulletinAdapter extends RecyclerView.Adapter<BulletinAdapter.BulletinViewHolder> {

    private Context context;
    private List<Bulletin> bulletins;

    public BulletinAdapter(Context context, List<Bulletin> bulletins) {
        this.context = context;
        this.bulletins = bulletins;
    }

    @NonNull
    @Override
    public BulletinViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bulletin, parent, false);
        return new BulletinViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BulletinViewHolder holder, int position) {
        Bulletin bulletin = bulletins.get(position);
        holder.titleTextView.setText(bulletin.getTitle());
        holder.dateTextView.setText(bulletin.getDate());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BulletinDetailActivity.class);
            intent.putExtra("title", bulletin.getTitle());
            intent.putExtra("date", bulletin.getDate());
            intent.putExtra("content", bulletin.getContent());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return bulletins.size();
    }

    public static class BulletinViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView dateTextView;

        public BulletinViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.title_text);
            dateTextView = itemView.findViewById(R.id.date_text);
        }
    }
}
