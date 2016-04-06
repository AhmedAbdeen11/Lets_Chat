package ahmed11.nivechatapp.chatapp.chat_application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ahmed11.nivechatapp.chatapp.chat_application.Models.EmailValidator;
import ahmed11.nivechatapp.chatapp.chat_application.Models.Methods;
import ahmed11.nivechatapp.chatapp.chat_application.Models.UsersData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by root on 2/22/16.
 */
public class PopDialoge extends Activity {

    private static final Firebase sRef = new Firebase("https://lets-chat11.firebaseio.com/UsersData");

    EditText username;
    EditText pass;
    EditText email;
    Button update;
    String name;
    UsersData userData;
    ArrayList<UsersData> mydata = new ArrayList<UsersData>();

    int key;
    String senderrr;
    private static final String Column_Pass = "pass";
    private static final String Column_Email = "email";
    private static final String Column_UserName = "username";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popdialoge);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");


        DisplayMetrics dm = new DisplayMetrics();

        getWindowManager().getDefaultDisplay().getMetrics(dm);  //bgeeb el width we el height btoo3 el ghaz
        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*.8),(int)(height*.8));  // dimensions el dialoge (hta5od 80% mn el screen kolha)

        username = (EditText) findViewById(R.id.update_user);
        pass = (EditText) findViewById(R.id.update_pwd);
        email = (EditText) findViewById(R.id.update_email);
        update = (Button) findViewById(R.id.update_done);

        sRef.push();


        sRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    UsersData user = ds.getValue(UsersData.class);
                    mydata.add(user);
                }

                Methods methods = new Methods();
                key = methods.SearchUserName(mydata, name);

                userData = mydata.get(key);

                senderrr = userData.getUsername();
                username.setText(userData.getUsername());
                pass.setText(userData.getPass());
                email.setText(userData.getEmail());

                mydata.remove(key);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EmailValidator obj1 = new EmailValidator();

                Methods obj = new Methods();



                int status_username = obj.SearchUserName(mydata, username.getText().toString());
                int status_email = obj.SearchEmail(mydata, email.getText().toString());


                if (username.length() == 0 || pass.length() == 0 || email.length() == 0)
                {

                    Toast.makeText(getApplicationContext(),"Please enter all informations",Toast.LENGTH_SHORT).show();
                }

                else if(obj1.validate(userData.getEmail()) == false){
//                    dia.dismiss();
                    Toast.makeText(getApplicationContext(),"Wrong email format.",Toast.LENGTH_SHORT).show();
                }

                else if (status_username != -1 && status_email != -1) {
                    Toast.makeText(getApplicationContext(), "Username and Email already exists", Toast.LENGTH_SHORT).show();
                } else if (status_username != -1) {
                    Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_SHORT).show();
                } else if (status_email != -1) {
                    Toast.makeText(getApplicationContext(), "Email already exists", Toast.LENGTH_SHORT).show();
                }
                else {
                    userData.setUsername(username.getText().toString());
                    userData.setEmail(email.getText().toString());
                    userData.setPass(pass.getText().toString());

                    sRef.child(String.valueOf(key)).removeValue();

                    HashMap<String, String> data = new HashMap<>();

                    data.put(Column_Pass, userData.getPass());
                    data.put(Column_Email, userData.getEmail());
                    data.put(Column_UserName, userData.getUsername());

                    sRef.child(String.valueOf(key)).setValue(data);

                    Toast.makeText(getApplicationContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();

                    DBConnection database = new DBConnection(getApplicationContext());
                    int count = database.delete(senderrr);

                    Intent intent2 = new Intent(getApplicationContext(),Login.class);
                    startActivity(intent2);

                }
            }
        });

    }

}
