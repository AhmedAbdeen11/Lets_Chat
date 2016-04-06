package ahmed11.nivechatapp.chatapp.chat_application;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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

public class Register extends Activity {

    private static final Firebase sRef = new Firebase("https://lets-chat11.firebaseio.com/UsersData");
    private static final String Column_Pass = "pass";
    private static final String Column_Email = "email";
    private static final String Column_UserName = "username";
    UsersData userData = new UsersData();

    /** The username EditText. */
    private EditText user;

    /** The password EditText. */
    private EditText pwd;

    /** The email EditText. */
    private EditText email;

    Button submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        user = (EditText) findViewById(R.id.user);
        pwd = (EditText) findViewById(R.id.pwd);
        email = (EditText) findViewById(R.id.email);
        submit = (Button) findViewById(R.id.btnReg);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                userData.setUsername(user.getText().toString());
                userData.setPass(pwd.getText().toString());
                userData.setEmail(email.getText().toString());





                if (userData.getUsername().length() == 0 || userData.getPass().length() == 0 || userData.getEmail().length() == 0)
                {

                    Toast.makeText(getApplicationContext(),"Please enter all informations",Toast.LENGTH_SHORT).show();
                    return;
                }

//                final ProgressDialog dia = ProgressDialog.show(getApplicationContext(),getTitle() ,
//                        getString(R.string.alert_wait));


                EmailValidator obj1 = new EmailValidator();
                if(obj1.validate(userData.getEmail()) == false){
//                    dia.dismiss();
                    Toast.makeText(getApplicationContext(),"Wrong email format.",Toast.LENGTH_SHORT).show();
                    return;
                }



                sRef.push();


                sRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       // Map<String, UsersData> myhashmap = (Map<String, UsersData>) dataSnapshot.getValue();

                        ArrayList<UsersData> mydata = new ArrayList<UsersData>();


                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            UsersData user = ds.getValue(UsersData.class);
                            mydata.add(user);
                        }

                        if (mydata.size() == 0) {

                            HashMap<String, String> data = new HashMap<>();
                            data.put(Column_Pass, userData.getPass());
                            data.put(Column_Email, userData.getEmail());
                            data.put(Column_UserName, userData.getUsername());
                            sRef.child("0").setValue(data);
                            Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                            final Intent intent = new Intent(getApplicationContext(), Login.class);
                            startActivity(intent);
                            return;
                        } else {



                            Methods obj = new Methods();


                            int status_username = obj.SearchUserName(mydata, userData.getUsername());
                            int status_email = obj.SearchEmail(mydata, userData.getEmail());

                            if (status_username != -1 && status_email != -1) {
                                Toast.makeText(getApplicationContext(), "Username and Email already exists", Toast.LENGTH_SHORT).show();
                            } else if (status_username != -1) {
                                Toast.makeText(getApplicationContext(), "Username already exists", Toast.LENGTH_SHORT).show();
                            } else if (status_email != -1) {
                                Toast.makeText(getApplicationContext(), "Email already exists", Toast.LENGTH_SHORT).show();
                            } else {
                                HashMap<String, String> data = new HashMap<>();
                                data.put(Column_Pass, userData.getPass());
                                data.put(Column_Email, userData.getEmail());
                                data.put(Column_UserName, userData.getUsername());

                                String my = String.valueOf(mydata.size());
                                sRef.child(my).setValue(data);
                                Toast.makeText(getApplicationContext(), "Registered Successfully", Toast.LENGTH_SHORT).show();
                                final Intent intent = new Intent(getApplicationContext(), Login.class);
                                startActivity(intent);
                            }
                        }

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {
                        Toast.makeText(getApplicationContext(), "Registered Failed", Toast.LENGTH_SHORT).show();

                    }
                });




            }
        });



    }
}
