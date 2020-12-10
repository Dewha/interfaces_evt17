package com.example.eswallet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    private final Context context;
    private final ArrayList<String> categories;
    private final ArrayList<String> comments;
    private final ArrayList<String> date;
    private final ArrayList<Integer> icons;
    private final ArrayList<String> ids;
    private final String login;
    private final boolean isCost;

    public CustomAdapter(Context context,
                         ArrayList<String> categories,
                         ArrayList<String> comments,
                         ArrayList<String> date,
                         ArrayList<Integer> icons,
                         ArrayList<String> ids,
                         String login,
                         boolean isCost) {
        this.context = context;
        this.categories = categories;
        this.comments = comments;
        this.date = date;
        this.icons = icons;
        this.ids = ids;
        this.login = login;
        this.isCost = isCost;
    }

    @Override
    public int getCount() {
        return categories.size();
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

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.row_layout, parent, false);
        }

        ImageView category_icon = convertView.findViewById(R.id.iv_category_icon);
        TextView category_ = convertView.findViewById(R.id.tv_category);
        TextView comment_ = convertView.findViewById(R.id.tv_comment);
        TextView date_ = convertView.findViewById(R.id.tv_date);

        int iconsSet = icons.get(position);
        String categorySet = categories.get(position);
        String commentSet = comments.get(position);
        String dateSet = date.get(position);
        String idSet = ids.get(position);

        if (!(iconsSet < 0 && commentSet == " " && categorySet == " " && dateSet == " ")) {
            category_icon.setImageResource(iconsSet);
            category_.setText(categorySet);
            comment_.setText(commentSet);
            date_.setText(dateSet);
            category_.setTag(idSet);
        }

        ImageButton btn_delete = (ImageButton) convertView.findViewById(R.id.btn_del);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("users").child(login);
        btn_delete.setOnClickListener(v -> {
            if (isCost) {
                reference.child("cost").child(category_.getTag().toString()).removeValue();

            } else {
                reference.child("income").child(category_.getTag().toString()).removeValue();
            }
            if(context instanceof Dashboard){
                ((Dashboard)context).loadDataFromDB();
            }
        });
        return convertView;
    }
}
