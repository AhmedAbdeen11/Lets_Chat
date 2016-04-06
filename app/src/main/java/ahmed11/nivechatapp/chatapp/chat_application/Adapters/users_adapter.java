package ahmed11.nivechatapp.chatapp.chat_application.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import ahmed11.nivechatapp.chatapp.chat_application.Models.UsersData;
import ahmed11.nivechatapp.chatapp.chat_application.R;

import java.util.ArrayList;

/**
 * Created by root on 2/21/16.
 */

public class users_adapter extends ArrayAdapter<UsersData> {

    ArrayList<UsersData> data = new ArrayList<UsersData>();

    public users_adapter(Context context,ArrayList<UsersData> data) {
        super(context,0);
        this.data = data;
    }

    @Override
    public int getCount() {
        return this.data.size();
    }

    @Override
    public UsersData getItem(int position) {
        return this.data.get(position);
    }
    static class datahandler{
        TextView username;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        datahandler handler;
        if(convertView == null){

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.users_listitem, parent, false);

            handler = new datahandler();
            handler.username = (TextView) convertView.findViewById(R.id.user_name_textview);


            convertView.setTag(handler);
        }
        else
        {
            handler = (datahandler) convertView.getTag();
        }

        UsersData useritem = getItem(position);

        handler.username.setText(useritem.getUsername());

//        Picasso.with(getContext())
//                .load("http://image.tmdb.org/t/p/w185"+data[position])
//                .into(handler.Poster_Image);

        //handler.Poster_Image.setScaleType(ImageView.ScaleType.MATRIX);

        return convertView;
    }
}
