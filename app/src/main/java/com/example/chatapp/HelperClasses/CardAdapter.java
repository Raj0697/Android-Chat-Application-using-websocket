package com.example.chatapp.HelperClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

import com.example.chatapp.R;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHold>  {

    ArrayList<CardHelper> cardviewwidgets;
    final private ListItemClickListener mOnClickListener;
    public static int increasecount = 0;
    public static String itemname="";
    private int current_pos, previous_pos;
    public static Set<String> productnames = new LinkedHashSet<String>();

    public CardAdapter(ArrayList<CardHelper> cardviewwidgets, ListItemClickListener listener) {
        this.cardviewwidgets = cardviewwidgets;
        mOnClickListener = listener;
    }

    @NonNull
    @Override
    public CardViewHold onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardviewwidgets, parent, false);
        return new CardViewHold(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardViewHold holder, int position) {

        CardHelper cardviewhelper = cardviewwidgets.get(position);
        holder.image.setImageResource(cardviewhelper.getImage());
        holder.title.setText(cardviewhelper.getTitle());
        holder.relativeLayout.setBackground(cardviewhelper.getgradient());
        //holder.count.setText(""+cardviewhelper.getCount());

        holder.add.setOnClickListener(v -> {
            itemname = holder.title.getText().toString();
            previous_pos = current_pos;
            current_pos = holder.getAdapterPosition();
            if(previous_pos != current_pos){
                increasecount = 0;
                previous_pos = current_pos;
            }

            if(increasecount < 10){
                increasecount++;
                productnames.add(itemname);
                holder.count.setText(""+increasecount);
            }
            else{
                Toast.makeText(v.getContext(), "Maximum order quantity is 10",Toast.LENGTH_SHORT).show();
            }
        });

        holder.sub.setOnClickListener(v -> {
            if(increasecount != 0){
                increasecount--;
                //productnames.remove(itemname);
                holder.count.setText(""+increasecount);
            }
            if(increasecount == 0){
                productnames.remove(itemname);
                holder.count.setText(""+0);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cardviewwidgets.size();

    }

    public interface ListItemClickListener {
        void oncardListClick(int clickedItemIndex);
    }

    public class CardViewHold extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView image;
        TextView title, count, add, sub;
        RelativeLayout relativeLayout;

        public CardViewHold(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            image = itemView.findViewById(R.id.phone_image);
            title = itemView.findViewById(R.id.phone_title);
            relativeLayout = itemView.findViewById(R.id.background_color);
            count = itemView.findViewById(R.id.count);
            add = itemView.findViewById(R.id.addition);
            sub = itemView.findViewById(R.id.subtract);

//            add.setOnClickListener(v -> {
//                if(increasecount < 10){
//                    increasecount++;
//                    itemname = title.getText().toString();
//                    count.setText(""+increasecount);
//                }
//                else{
//                    Toast.makeText(itemView.getContext(), "Maximum order quantity is 10",Toast.LENGTH_SHORT).show();
//                }
//            });
//            sub.setOnClickListener(v -> {
//                if(increasecount != 0){
//                    increasecount--;
//                    count.setText(""+increasecount);
//                }
//            });
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.oncardListClick(clickedPosition);
        }
    }

}
