package com.example.bookingapp.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImgAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    Context context;
    ArrayList<Bitmap> bitmaps;
    ArrayList<String> bitmapstring;
    int oldbitmapstrsize;
    int layoutid;
    boolean canRemove = false;

    public boolean isCanRemove() {
        return canRemove;
    }

    public void setCanRemove(boolean canRemove) {
        this.canRemove = canRemove;
    }

    public ArrayList<String> getBitmapstring() {
        return bitmapstring;
    }

    public void setBitmapstring(ArrayList<String> bitmapstring)
    {
        this.bitmapstring = bitmapstring;
        oldbitmapstrsize = bitmapstring.size();
    }

    public ImgAdapter(Context context, ArrayList<Bitmap> bitmaps, int layoutid) {
        this.context = context;
        this.bitmaps = bitmaps;
        this.layoutid = layoutid;
    }

    public ImgAdapter(Context context, ArrayList<Bitmap> bitmaps, ArrayList<String> bitmapstring, int layoutid, boolean canRemove) {
        this.context = context;
        this.bitmaps = bitmaps;
        this.bitmapstring = bitmapstring;
        this.layoutid = layoutid;
        this.canRemove = canRemove;
    }

    public ArrayList<Bitmap> getBitmaps() {
        return bitmaps;
    }

    public void setBitmaps(ArrayList<Bitmap> bitmaps) {
        this.bitmaps = bitmaps;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ImgHolder(LayoutInflater.from(context).inflate(layoutid,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ImgHolder imgHolder = (ImgHolder) holder;
        int pos = position;
        if(bitmaps != null && bitmapstring == null && canRemove == false)
        {
            imgHolder.img.setImageBitmap(bitmaps.get(position));
            imgHolder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bitmaps.remove(pos);
                    notifyDataSetChanged();
                }
            });
        }
        else if(bitmapstring != null && canRemove == false)
        {
            Picasso.get().load(bitmapstring.get(position)).into(imgHolder.img);
        }
        else if(canRemove)
        {
            Picasso.get().load(bitmapstring.get(position)).into(imgHolder.img);
            imgHolder.img.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    bitmapstring.remove(pos);
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if(bitmaps != null && bitmapstring == null)
            return bitmaps.size();
        else if(bitmaps == null && bitmapstring != null)
            return bitmapstring.size();
        else
            return 0;
    }
    public class ImgHolder extends RecyclerView.ViewHolder
    {
        ImageView img;
        TextView txt;
        public ImgHolder(@NonNull View itemView) {
            super(itemView);
            txt = itemView.findViewById(R.id.pop_des_txt);
            txt.setVisibility(View.GONE);
            img = itemView.findViewById(R.id.pop_des_img);
        }
    }
}
