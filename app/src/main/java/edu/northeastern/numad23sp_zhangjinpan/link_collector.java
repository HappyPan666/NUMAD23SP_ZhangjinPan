package edu.northeastern.numad23sp_zhangjinpan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class link_collector extends AppCompatActivity {

    private final ArrayList<itemLink> itemLinks = new ArrayList<>();//to store all the link collector map
    private RecyclerView recyclerView;
    private recycleViewAdapter recycleViewAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private FloatingActionButton addButton;

    private static final String KEY_OF_INSTANCE = "KEY_OF_INSTANCE";
    private static final String NUMBER_OF_ITEMS = "NUMBER_OF_ITEMS";
    private View.OnClickListener addItemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link_collector);

        init(savedInstanceState);

        addButton = findViewById(R.id.floatingActionButtonAdd);
        addItemClickListener = view -> {
            int position = 0;
            addItem(position);
        };
        addButton.setOnClickListener(addItemClickListener);


        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
//            other action implemented: Use swipe to delete
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Toast.makeText(link_collector.this, "Website Deleted", Toast.LENGTH_LONG).show();
                int position = viewHolder.getLayoutPosition();
                itemLinks.remove(position);
                recycleViewAdapter.notifyItemRemoved(position);
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    protected void onSaveInstanceState(Bundle outState){
        int size = itemLinks == null ? 0 : itemLinks.size();
        outState.putInt(NUMBER_OF_ITEMS,size);
        for(int i = 0; i < size; i ++){
            outState.putString(KEY_OF_INSTANCE + i + "0", itemLinks.get(i).getWebName());
            outState.putString(KEY_OF_INSTANCE + i + "1", itemLinks.get(i).getWebLink());
        }
        super.onSaveInstanceState(outState);
    }

    private void addItem(int pos) {
        View view = View.inflate(this, R.layout.activity_add_web_link_url, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(link_collector.this);
        builder.setMessage(R.string.header).setView(view).setPositiveButton(R.string.ok, (dialogInterface, i) -> { //header:header    ok:right below button
            EditText et_linkName = view.findViewById(R.id.et_linkName);
            EditText et_linkUrl = view.findViewById(R.id.et_linkUrl);

            String linkName = et_linkName.getText().toString();
            String linkURL = et_linkUrl.getText().toString();

            if(Patterns.WEB_URL.matcher(linkURL).matches()){
                itemLinks.add(pos, new itemLink(linkName, linkURL));
                Snackbar.make(recyclerView, "Add an item successfully!", Snackbar.LENGTH_LONG).show();
                recycleViewAdapter.notifyItemInserted(pos);
            } else {
                Snackbar.make(recyclerView, "Invalid URL", Snackbar.LENGTH_LONG).setAction("Input Again", addItemClickListener).show();
            }}).setNegativeButton(R.string.notok, (dialogInterface, i) -> { //notok: left below button
        });
        builder.create().show();
    }

    private void init(Bundle savedInstanceState) {
        initalItemData(savedInstanceState);
        createRecyclerView();
    }

    private void initalItemData(Bundle saveInstanceState){
        if(saveInstanceState != null && saveInstanceState.containsKey(NUMBER_OF_ITEMS)){
            if(itemLinks == null || itemLinks.size() == 0){
                int size = saveInstanceState.getInt(NUMBER_OF_ITEMS);

                for(int i = 0; i < size; i ++){
                    String itemName = saveInstanceState.getString(KEY_OF_INSTANCE + i + "0");
                    String itemURL = saveInstanceState.getString(KEY_OF_INSTANCE + i + "1");
                    itemLinks.add(new itemLink(itemName, itemURL));
                }
            }
        }
    }

    private void createRecyclerView(){
        layoutManager = new LinearLayoutManager(this);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        recycleViewAdapter  = new recycleViewAdapter(itemLinks);

        itemClickListener itemClickListener = position -> {
            itemLink itemLink = itemLinks.get(position);
            String linkURL = itemLink.getWebLink();
            if(linkURL.startsWith("www")){
                linkURL = "https://" + linkURL;
            } else if(!linkURL.startsWith("https://") && !linkURL.startsWith("http://")){
                linkURL = "https://www." + linkURL;
            }
            Uri uri = Uri.parse(linkURL);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        };

        recycleViewAdapter.setListener(itemClickListener);
        recyclerView.setAdapter(recycleViewAdapter);
        recyclerView.setLayoutManager(layoutManager);
    }

}