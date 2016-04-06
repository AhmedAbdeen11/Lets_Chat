package ahmed11.nivechatapp.chatapp.chat_application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import ahmed11.nivechatapp.chatapp.chat_application.Adapters.users_adapter;
import ahmed11.nivechatapp.chatapp.chat_application.Models.Methods;
import ahmed11.nivechatapp.chatapp.chat_application.Models.UsersData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class ChatHomeScreen extends Activity{

    ListView all_users_list;
    users_adapter myadapter;
    private static final Firebase sRef = new Firebase("https://lets-chat11.firebaseio.com/UsersData");

    String sender;
    public static ArrayList<UsersData> mydata2 = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_home_screen);

        all_users_list = (ListView) findViewById(R.id.all_users_listview);

        Intent i = getIntent();
        sender = i.getExtras().getString("Sender");


        sRef.push();
        sRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 ArrayList<UsersData> mydata = new ArrayList<UsersData>();


                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    UsersData user = ds.getValue(UsersData.class);
                    mydata.add(user);
                }

                Methods obj = new Methods();
                int index = obj.SearchUserName(mydata,sender);

                for(int i=0;i<mydata.size();i++){
                    UsersData x = mydata.get(i);
                    mydata2.add(x);
                }

                mydata.remove(index);
                myadapter = new users_adapter(getApplicationContext(), mydata);
                all_users_list.setAdapter(myadapter);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });


        //--------------- Chat list Listener click ----------//


        all_users_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                UsersData u = myadapter.getItem(position);
                String[] arr = new String[2];
                arr[0] = sender;
                arr[1] = u.getUsername();
                Intent intent = new Intent(getApplicationContext(),Chat_Page.class);
                intent.putExtra("Sender_mRecipient",arr);
                        startActivity(intent);
            }
        });

    }



    //----------Menu Setting ---------//


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chatmenu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.logout:
                logout();
                return true;
            case R.id.UpdateData:
                updatedata();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }


    public void logout(){

        DBConnection database = new DBConnection(getApplicationContext());


        int count = database.delete(sender);
        Intent intent5 = new Intent(getApplicationContext(),Login.class);
        startActivity(intent5);
    }



    public void updatedata(){

        Intent intent4 = new Intent(getApplicationContext(),PopDialoge.class);
        intent4.putExtra("name",sender);
        startActivity(intent4);
    }
}
