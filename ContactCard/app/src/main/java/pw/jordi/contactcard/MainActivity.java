package pw.jordi.contactcard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

public class MainActivity extends AppCompatActivity {
    TextView tvFirstname;
    TextView tvLastname;
    ImageView ivAvatar;
    Person person;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get UI elements
        tvFirstname = (TextView) findViewById(R.id.tvFirstname);
        tvLastname = (TextView) findViewById(R.id.tvLastname);
        ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
        Button refreshButton = (Button) findViewById(R.id.btnRefresh);

        Bundle data = getIntent().getExtras();
        Person person = data.getParcelable("person");
    }
}
