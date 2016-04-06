package ahmed11.nivechatapp.chatapp.chat_application;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ahmed11.nivechatapp.chatapp.chat_application.Models.Methods;
import ahmed11.nivechatapp.chatapp.chat_application.Models.UsersData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class Login extends Activity {

    private static final Firebase sRef = new Firebase("https://lets-chat11.firebaseio.com/UsersData");


    /** The username edittext. */
    private EditText user;

    /** The password edittext. */
    private EditText pwd;


    Button login;
    Button register;
    DBConnection database ;
    String[] login_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //---------Title Bar -----------//



        //----------------Check for login information in Database ----//

        database = new DBConnection(getApplicationContext());

        login_info = database.viewData();

        if(login_info != null){


            Intent intent2 = new Intent(getApplicationContext(), ChatHomeScreen.class);
            intent2.putExtra("Sender", login_info[2]);
            startActivity(intent2);
        }


else {
            user = (EditText) findViewById(R.id.user);
            pwd = (EditText) findViewById(R.id.pwd);
            login = (Button) findViewById(R.id.btnLogin);
            register = (Button) findViewById(R.id.btnReg);

            final Intent intent = new Intent(this, Register.class);

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final UsersData login_user = new UsersData();
                    login_user.setEmail(user.getText().toString());
                    login_user.setPass(pwd.getText().toString());


                    if (login_user.getPass().length() == 0 || login_user.getEmail().length() == 0) {

                        Toast.makeText(getApplicationContext(), "Please enter all informations.", Toast.LENGTH_SHORT).show();
                        return;
                    }


                    sRef.push();
                    sRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            ArrayList<UsersData> mydata = new ArrayList<UsersData>();


                            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                UsersData user = ds.getValue(UsersData.class);
                                mydata.add(user);
                            }


                            if (mydata.size() == 0) {
                                Toast.makeText(getApplicationContext(), "Wrong email or password .", Toast.LENGTH_SHORT).show();
                                return;
                            } else {

                                Methods obj = new Methods();

                                int status_email = obj.SearchEmail(mydata, login_user.getEmail());
                                int status_pass = obj.SearchPass(mydata, login_user.getPass());

//                            int status_email = obj.SearchEmail(mydata, "ahmedabdeen171@gmail.com");
//                            int status_pass = obj.SearchPass(mydata, "011424611cr");

                                if (status_email != -1 && status_pass != -1) {
                                    Toast.makeText(getApplicationContext(), "Login Success", Toast.LENGTH_SHORT).show();


                                    //------Save Login info in db

                                    database.dataInsert(login_user.getEmail().toString(), login_user.getPass().toString(), mydata.get(status_email).getUsername());

                                    //----------------------------//

                                    Intent intent2 = new Intent(getApplicationContext(), ChatHomeScreen.class);
                                    intent2.putExtra("Sender", mydata.get(status_email).getUsername());
                                    startActivity(intent2);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Wrong email or password .", Toast.LENGTH_SHORT).show();
                                }

                            }
                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });


                }
            });

            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(intent);
                }
            });




   /*     Parse.initialize(this, "ohITQjQyJJA6CwW0zD5fI9L20uee0ea8fh8k6C3h", "TwYMDyuOlxJIbkJv2DKE7PlX8y7UKW94LaGuZYyn");

        ParseUser user = new ParseUser();
        user.setUsername("my name");
        user.setPassword("my pass");
        user.setEmail("email@nivechatapp.com");

// other fields can be set just like with ParseObject
        user.put("phone", "650-555-0000");

        user.signUpInBackground(new SignUpCallback() {
            public void done(ParseException e) {
                if (e == null) {
                    // Hooray! Let them use the app now.
                } else {
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                }
            }
        }); */


        }

    }
}
