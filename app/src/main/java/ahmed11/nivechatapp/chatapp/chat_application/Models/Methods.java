package ahmed11.nivechatapp.chatapp.chat_application.Models;

import com.firebase.client.Firebase;

import java.util.ArrayList;

/**
 * Created by root on 2/21/16.
 */
public class Methods {


    private static final Firebase sRef = new Firebase("https://myfirstchatap.firebaseio.com/UsersData");

    public int SearchUserName(ArrayList<UsersData> alldata,String username){
        int state = -1;
        for(int i=0;i<alldata.size();i++){

            if(alldata.get(i).getUsername().equals(username)){
                state = i;
                break;
            }
        }
        return state;
    }


    public int SearchEmail(ArrayList<UsersData> alldata,String email){
        int state = -1;

        for(int i=0;i<alldata.size();i++){
            if(alldata.get(i).getEmail().equals(email)){
                state = i;
                break;
            }
        }
        return state;
    }

    public int SearchPass(ArrayList<UsersData> alldata,String pass){

        int state = -1;
        for(int i=0;i<alldata.size();i++){
            if(alldata.get(i).getPass().equals(pass)){
                state = i;
                break;
            }
        }
        return state;
    }

}
