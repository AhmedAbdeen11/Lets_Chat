package ahmed11.nivechatapp.chatapp.chat_application;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by root on 2/22/16.
 */
public class DBConnection {
    DBinfo obj;
    public DBConnection(Context context) {
        obj = new DBinfo(context);

    }

    public long dataInsert(String email,String pass,String username) {
        SQLiteDatabase sqLiteDatabase = obj.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(obj.Email,email);
        contentValues.put(obj.Password,pass);
        contentValues.put(obj.UserName,username);


        long id = sqLiteDatabase.insert(obj.tableName,null,contentValues);
        return id;

    }

//    public boolean search_movie(String id){
//
//
//        ArrayList<String[]> datasql = new ArrayList<>();
//
//        SQLiteDatabase sqLiteDatabase = obj.getReadableDatabase();
//        String[] columns = {obj.UID,obj.Email,obj.Password};
//        Cursor cursor = sqLiteDatabase.query(obj.tableName,columns,null,null,null,null,null);
//
//        String myid;
//
//        while (cursor.moveToNext()){
//            myid = cursor.getString(7);
//            if(myid.equals(id))
//                return true;
//        }
//
//        return false;
//    }

    public int delete(String id){
        SQLiteDatabase sqLiteDatabase = obj.getReadableDatabase();
        String [] whereArgs = {id};
        String s = obj.UserName + " =? ";
        int count = sqLiteDatabase.delete(obj.tableName,s,whereArgs);

        return count;
    }

    public String[] viewData(){

        SQLiteDatabase sqLiteDatabase = obj.getReadableDatabase();
        String[] columns = {obj.UID,obj.Email,obj.Password,obj.UserName};
        Cursor cursor = sqLiteDatabase.query(obj.tableName,columns,null,null,null,null,null);

        String[] myarr = new String[3];
        if(cursor.getCount()==0){
            return null;
        }
        else {
            cursor.moveToNext();
            myarr[0] = cursor.getString(1);
            myarr[1] = cursor.getString(2);
            myarr[2] = cursor.getString(3);

            return myarr;
        }
    }


    static class DBinfo extends SQLiteOpenHelper {

        private static final String dataBase_Name = "LoginDB";
        private static final String tableName = "user_data";
        private static final int dataBase_Version = 2;
        private static final String UID = "id";
        private static final String Email = "Email";
        private static final String Password = "Password";
        private static final String UserName = "UserName";



        private static final String DROP_TABLE = "DROP TABLE IF EXISTS " + tableName;
        private static final String CREATE_TABLE = "CREATE TABLE " + tableName +
                " ("+UID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+Email+" VARCHAR(255), "+Password+" VARCHAR(255), "+UserName+" VARCHAR(255))";



        private Context context;


        public DBinfo(Context context) {
            super(context, dataBase_Name, null, dataBase_Version);
            this.context = context;
            //Toast.makeText(context, "this Constructor", Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(CREATE_TABLE);
            // Toast.makeText(context,"OnCreate Method" ,Toast.LENGTH_LONG).show();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL(DROP_TABLE);
            onCreate(db);
            //Toast.makeText(context,"OnUpgrade Method" ,Toast.LENGTH_LONG).show();

        }
             }

}
