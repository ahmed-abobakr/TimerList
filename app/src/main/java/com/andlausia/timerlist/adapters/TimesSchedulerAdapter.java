package com.andlausia.timerlist.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andlausia.timerlist.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.media.CamcorderProfile.get;

/**
 * Created by ahmedabobakr on 6/13/17.
 */

public class TimesSchedulerAdapter extends RecyclerView.Adapter<TimesSchedulerAdapter.MyViewHolder>  {

    List<Calendar> timesList;
    Context context;
    OnItemClicked itemClicked;

    public TimesSchedulerAdapter(Context context, OnItemClicked itemClicked){
        timesList = new ArrayList<>();
        
        int hour = 7;
        int minute = 30;
        for(int i = 0; i < 14; i++){
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, hour);
            calendar.set(Calendar.MINUTE, minute);
            hour++;
            timesList.add(calendar);
        }

        this.context = context;
        this.itemClicked = itemClicked;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_row, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        if(timesList.get(position).get(Calendar.HOUR_OF_DAY) < 12)
        holder.txtTime.setText(timesList.get(position).get(Calendar.HOUR_OF_DAY) + ":" + timesList.get(position).get(Calendar.MINUTE) + " " + "AM");
        else
            holder.txtTime.setText(timesList.get(position).get(Calendar.HOUR_OF_DAY) + ":" + timesList.get(position).get(Calendar.MINUTE) + " " + "PM");

        Calendar currentTime = Calendar.getInstance();
        if(currentTime.after(timesList.get(position))){
            holder.view.setBackgroundColor(context.getResources().getColor(android.R.color.darker_gray));
        }else {
            holder.view.setBackgroundColor(context.getResources().getColor(android.R.color.white));
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(itemClicked != null)
                    itemClicked.itemClicked();
            }
        });
    }

    @Override
    public int getItemCount() {
        return timesList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView txtTime;
        public View view;

        public MyViewHolder(View itemView) {
            super(itemView);

            txtTime = (TextView) itemView.findViewById(R.id.txt_time);
            view = itemView.findViewById(R.id.row_view);
        }
    }

    public interface OnItemClicked{
        void itemClicked();
    }
}
