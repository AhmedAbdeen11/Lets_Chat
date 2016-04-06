package ahmed11.nivechatapp.chatapp.chat_application;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import ahmed11.nivechatapp.chatapp.chat_application.Models.Methods;

import com.firebase.client.Firebase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class Chat_Page extends Activity implements View.OnClickListener,
        MessageDataSource.MessagesCallbacks{
    public static final String USER_EXTRA = "USER";

    public static final String TAG = "ChatActivity";
    private static final Firebase sRef = new Firebase("https://lets-chat11.firebaseio.com/UsersData");


    private ArrayList<Message> mMessages;
    private MessagesAdapter mAdapter;
    private String mRecipient;
    private ListView mListView;
    private Date mLastMessageDate = new Date();
    private String mConvoId;
    private MessageDataSource.MessagesListener mListener;
    private String[] x;
    private String id_sender;
    private String id_rec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat__page);

        Intent intent = getIntent();
        x = intent.getExtras().getStringArray("Sender_mRecipient");

        mRecipient = x[1];


        mListView = (ListView)findViewById(R.id.messages_list);
        mMessages = new ArrayList<>();
        mAdapter = new MessagesAdapter(mMessages);
        mListView.setAdapter(mAdapter);

        setTitle(mRecipient);
//        if (getSupportActionBar() != null){
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        }

        Button sendMessage = (Button)findViewById(R.id.send_message);
        sendMessage.setOnClickListener(this);



                Methods methods = new Methods();


                int sender_v = methods.SearchUserName(ChatHomeScreen.mydata2,x[0]);
                int rec_v = methods.SearchUserName(ChatHomeScreen.mydata2,x[1]);

                id_sender = String.valueOf(sender_v);
                id_rec = String.valueOf(rec_v);



        String[] ids = {id_rec, id_sender};
        Arrays.sort(ids);
        mConvoId = ids[0]+ids[1];

        mListener = MessageDataSource.addMessagesListener(mConvoId, this);
        scrollMyListViewToBottom();
    }

    public void onClick(View v) {





        //----------------------//
        EditText newMessageView = (EditText)findViewById(R.id.new_message);
        String newMessage = newMessageView.getText().toString();
        if(newMessage.trim().equals("")){
            return;
        }
        newMessageView.setText("");
        Message msg = new Message();
        msg.setDate(new Date());
        msg.setText(newMessage);
        msg.setSender(x[0]);

        MessageDataSource.saveMessage(msg, mConvoId,x[0]);

        Intent intent = new Intent();
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, 0);
    }

    @Override
    public void onMessageAdded(Message message) {
        mMessages.add(message);
        mAdapter.notifyDataSetChanged();
        scrollMyListViewToBottom();



    }
    private void scrollMyListViewToBottom() {
        mListView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                mListView.setSelection(mAdapter.getCount() - 1);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MessageDataSource.stop(mListener);
    }


    private class MessagesAdapter extends ArrayAdapter<Message> {
        MessagesAdapter(ArrayList<Message> messages){
            super(Chat_Page.this, R.layout.message, R.id.message, messages);
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = super.getView(position, convertView, parent);
            Message message = getItem(position);

            TextView nameView = (TextView)convertView.findViewById(R.id.message);
            nameView.setText(message.getText());

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams)nameView.getLayoutParams();

            int sdk = Build.VERSION.SDK_INT;
            if (message.getSender().equals(x[0])){

                    //nameView.setBackgroundDrawable(getDrawable(R.drawable.bubble_right_green));
                nameView.setBackgroundResource(R.drawable.bubble_right_green);

                layoutParams.gravity = Gravity.RIGHT;
            }else{
//                    nameView.setBackgroundDrawable(getDrawable(R.drawable.bubble_left_gray));
                nameView.setBackgroundResource(R.drawable.bubble_left_gray);
                layoutParams.gravity = Gravity.LEFT;
            }

            nameView.setLayoutParams(layoutParams);


            return convertView;
        }
    }
}
