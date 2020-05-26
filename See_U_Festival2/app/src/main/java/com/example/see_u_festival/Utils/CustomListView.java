package com.example.see_u_festival.Utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.see_u_festival.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CustomListView extends ArrayAdapter<String> {

    private String[] artistesName;
    private String[] schedule;
    private Integer[] imgID;
    private String[] onAir;
    private Activity context;
    String currentDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

    public CustomListView(Activity context, Integer[] imgID, String[] schedule, String[] artistesName,String[] onAir) {
        super(context, R.layout.mylist,artistesName);

        this.artistesName = artistesName;
        this.context = context;
        this.schedule = schedule;
        this.imgID = imgID;
        this.onAir = onAir;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        ViewHolder viewHolder = null;
        if (r==null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r=layoutInflater.inflate(R.layout.mylist,null,true);
            viewHolder=new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else{
            viewHolder = (ViewHolder) r.getTag();
        }
        viewHolder.img.setImageResource(imgID[position]);
        viewHolder.textView1.setText(onAir[position]);
        viewHolder.textView2.setText(artistesName[position]);
        viewHolder.textView3.setText(schedule[position]);
        viewHolder.textView1.setTextColor(context.getResources().getColor(R.color.red));

        return r;
    }

    class ViewHolder{
        TextView textView1;
        TextView textView2;
        ImageView img;
        TextView textView3;
        ViewHolder(View v){
            textView1 = v.findViewById(R.id.textView1);
            textView2 = v.findViewById(R.id.textView2);
            img = v.findViewById(R.id.image);
            textView3 = v.findViewById(R.id.textView3);

        }
    }
}
