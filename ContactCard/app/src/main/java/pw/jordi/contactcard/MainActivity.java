package pw.jordi.contactcard;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get UI elements
        tvFirstname = (TextView) findViewById(R.id.tvFirstname);
        tvLastname = (TextView) findViewById(R.id.tvLastname);
        ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
        Button refreshButton = (Button) findViewById(R.id.btnRefresh);

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fetchUsers(1);
            }
        });
    }

    protected void fetchUsers(int amount) {
        final String API_URL = "https://randomuser.me/api?results=" + amount;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest userRequest = new JsonObjectRequest(API_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray resultArray = response.getJSONArray("results");
                            JSONObject person = resultArray.getJSONObject(0);
                            JSONObject personName = person.getJSONObject("name");

                            // Set new name data
                            tvFirstname.setText(personName.getString("first"));
                            tvLastname.setText(personName.getString("last"));

                            // Extract image URL and sent off to download async
                            JSONObject personImage = person.getJSONObject("picture");
                            String imageUrl = personImage.getString("large");
                            new DownloadImage().execute(imageUrl);
                        }
                        catch (JSONException e) {
                            Log.e("JSON", e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley", error.getMessage());
                    }
                }
        );
        requestQueue.add(userRequest);
    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... URL) {
            String imageURL = URL[0];

            Bitmap bitmap = null;
            try {
                InputStream input = new java.net.URL(imageURL).openStream();
                bitmap = BitmapFactory.decodeStream(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            ivAvatar.setImageBitmap(result);
        }
    }
}
