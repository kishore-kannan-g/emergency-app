package kishore.kannan.cse.emergencyapp;

import android.provider.BaseColumns;

public class EmergencyContactsContract {
    public static final class ContactsInner implements BaseColumns{

        public static final String TABLE_NAME = "Contacts";
        public static final String ID = BaseColumns._ID;
        public static final String CONTACT_NAME = "Name";
        public static final String CONTACT_NUMBER = "Phone_Number";

    }
}
