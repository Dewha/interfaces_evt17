package com.example.eswallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {

    Context context;
    String[] categories;
    String[] comments;
    int[] images;

    public CustomAdapter(Context context, String[] categories, String[] comments, int[] images) {
        this.context = context;
        this.categories = categories;
        this.comments = comments;
        this.images = images;
    }

    @Override
    public int getCount() {
        return categories.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_layout, parent,false);
        ImageView category_icon = view.findViewById(R.id.iv_category_icon);
        TextView category = view.findViewById(R.id.tv_category);
        TextView comment = view.findViewById(R.id.tv_comment);

        int iconsSet=images[position];
        String categorySet=categories[position];
        String commentSet=comments[position];

        category_icon.setImageResource(iconsSet);
        category.setText(categorySet);
        comment.setText(commentSet);

        if (iconsSet<0 && commentSet == " " && categorySet == " ");

        return view;
    }
}
