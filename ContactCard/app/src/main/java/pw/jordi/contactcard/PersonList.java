package pw.jordi.contactcard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

public class PersonList extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView lvPersonList;
    PersonAdapter listViewPersonAdapter;
    ArrayList personList = new ArrayList();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_list);

        // Get UI elem
        lvPersonList = (ListView) findViewById(R.id.lvPersonList);

        Person p = new Person("Marshall", "Fuller");
        p.avatar = BitmapFactory.decodeResource(getResources(), R.drawable.user_default);
        personList.add(p);

        // Koppel list aan
        listViewPersonAdapter = new PersonAdapter(this,
                getLayoutInflater(), personList);
        lvPersonList.setAdapter(listViewPersonAdapter);

        // Activate adapter, kan dan ook in een button, of network update
        listViewPersonAdapter.notifyDataSetChanged();

        // Enable listener
        lvPersonList.setOnItemClickListener(this);

        fetchUsers(1);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Intent detailIntent = new Intent(getApplicationContext(), MainActivity.class);
        Person person = (Person) personList.get(i);

        startActivity(detailIntent);
    }

    protected void fetchUsers(int amount) {
        final String API_URL = "https://randomuser.me/api?results=" + amount;
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        JsonObjectRequest userRequest = new JsonObjectRequest(API_URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gsonInstance = new Gson();
                        Person person = gsonInstance.fromJson(response.toString(), Person.class);


                        // Extract image URL and sent off to download async
                        //JSONObject personImage = person.getJSONObject("picture");
                        //String imageUrl = personImage.getString("large");
                        //new DownloadImage().execute(imageUrl);
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
            //ivAvatar.setImageBitmap(result);
        }
    }
}
