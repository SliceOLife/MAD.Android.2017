package pw.jordi.contactcard;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by jordi on 9/11/17.
 */

public class Person {
    public Bitmap avatar;
    public String firstName;
    public String lastName;

    public Person(String firstName, String lastName, Bitmap avatar) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.avatar = avatar;
    }

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }
}