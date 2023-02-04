package edu.northeastern.numad23sp_zhangjinpan;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class recycleViewAdapter extends RecyclerView.Adapter<recycleViewHolder> {
    private final ArrayList<itemLink> itemList;
    private itemClickListener listener;

    public recycleViewAdapter(ArrayList<itemLink> itemList) {
        this.itemList = itemList;
    }

    public void setListener(itemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public recycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_link_card, parent, false);
        return new recycleViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull recycleViewHolder holder, int position) {
        itemLink currentItem = itemList.get(position);
        holder.webName.setText(currentItem.getWebName());
        holder.webLink.setText(currentItem.getWebLink());

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }
}
