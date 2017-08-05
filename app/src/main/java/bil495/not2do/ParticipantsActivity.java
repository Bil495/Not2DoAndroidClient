package bil495.not2do;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bil495.not2do.app.AppConfig;
import bil495.not2do.app.AppController;
import bil495.not2do.fragment.MyUserRecyclerViewAdapter;
import bil495.not2do.helper.SessionManager;
import bil495.not2do.model.Not2DoModel;
import bil495.not2do.model.UserModel;

import static android.content.ContentValues.TAG;

/**
 * Created by burak on 7/20/2017.
 */

public class ParticipantsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private MyUserRecyclerViewAdapter mAdapter;
    private ProgressDialog pDialog;

    private List<UserModel> users;

    private String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        URL = getIntent().getStringExtra("url");
        setTitle(getIntent().getStringExtra("title"));
        // Progress dialog
        pDialog = new ProgressDialog(ParticipantsActivity.this);
        pDialog.setCancelable(false);

        setContentView(R.layout.activity_participants);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        users = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseContext()));
        mAdapter = new MyUserRecyclerViewAdapter(getBaseContext(), users);
        recyclerView.setAdapter(mAdapter);

        requestToServer();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    private void requestToServer(){
        String tag_string_req = "req_global_timeline";

        pDialog.setMessage("Getting timeline ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Timeline Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = false;//jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        JSONArray arr = jObj.getJSONArray("users");
                        for (int i = 0; i < arr.length(); i++){
                            JSONObject obj = arr.getJSONObject(i);


                            UserModel user = new UserModel();
                            user.setId(obj.getInt("id"));
                            user.setUsername(obj.getString("username"));
                            user.setName(obj.getString("name"));
                            user.setSurname(obj.getString("surname"));
                            //user.setProfilePic(obj.getString("pp_url"));
                            users.add(user);
                        }
                        mAdapter.notifyDataSetChanged();
                        //Toast.makeText(getBaseContext(), "Participants loaded.", Toast.LENGTH_LONG).show();


                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getBaseContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getBaseContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getBaseContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                SessionManager sessionManager = new SessionManager(getBaseContext());
                params.put("user_id", sessionManager.getUserID().toString());
                params.put("token", sessionManager.getToken());

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
