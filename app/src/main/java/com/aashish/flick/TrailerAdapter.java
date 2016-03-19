package com.aashish.flick;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TrailerAdapter extends BaseAdapter {

    private Context mContext;

    public TrailerAdapter(Context context){
        mContext = context;
    }

    @Override
    public int getCount() {
        return trailers.size();
    }

    @Override
    public Object getItem(int i) {
        return trailers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        View trailerRow;
        if (convertView == null) {
            trailerRow = View.inflate(mContext, R.layout.trailer_list_row, null);
        } else {
            trailerRow = convertView;
        }
        trailerRow.setId(1000 + i);
        ((TextView) trailerRow.findViewById(R.id.trailerLabel)).setText(trailers.get(i).label);
        Picasso.with(mContext).load("http://img.youtube.com/vi/" + trailers.get(i).url + "/hqdefault.jpg")
                .placeholder(R.drawable.noposter)
                .into((ImageView) trailerRow.findViewById(R.id.trailerImage));

        final String url = trailers.get(i).url;
        trailerRow.findViewById(R.id.trailerImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailActivityFragment.instance.watchYoutubeVideo(url);
            }
        });
        return trailerRow;
    }

    public void addItem(Trailer trailer){
        trailers.add(trailer);
    }

    public ArrayList<Trailer> trailers = new ArrayList<Trailer>();
}
