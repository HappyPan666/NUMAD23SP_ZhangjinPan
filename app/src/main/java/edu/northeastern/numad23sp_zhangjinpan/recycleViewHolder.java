package edu.northeastern.numad23sp_zhangjinpan;

import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class recycleViewHolder extends RecyclerView.ViewHolder {

    public TextView webName;
    public TextView webLink;

    public recycleViewHolder(@NonNull View itemView, final itemClickListener listener) {
        super(itemView);
        webName = itemView.findViewById(R.id.textViewWebName);
        webLink = itemView.findViewById(R.id.textViewWebLinkURL);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener != null){
                    int position = getLayoutPosition();
                    if(position != RecyclerView.NO_POSITION){
                        listener.onClick(position);
                    }
                }
            }
        });
    }
}
