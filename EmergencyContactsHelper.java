package kishore.kannan.cse.emergencyapp;

import static java.sql.Types.INTEGER;
import static java.sql.Types.NULL;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import kishore.kannan.cse.emergencyapp.EmergencyContactsContract.ContactsInner;

public class EmergencyContactsHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Emergency_Contacts";
    public static final int DATABASE_VERSION = 1;

    public final String SQL_CREATE_DATABASE
            = "CREATE TABLE "+ ContactsInner.TABLE_NAME + "( " + ContactsInner.ID + " INTEGER PRIMARY KEY AUTOINCREMENT , "
              + ContactsInner.CONTACT_NAME + " Text NOT NULL, "
              + ContactsInner.CONTACT_NUMBER + " VARCHAR(20) NOT NULL " + " );";

    public EmergencyContactsHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_DATABASE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ContactsInner.TABLE_NAME);
        onCreate(db);
    }
}
