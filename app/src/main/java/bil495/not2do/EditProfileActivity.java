package bil495.not2do;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import bil495.not2do.app.AppConfig;
import bil495.not2do.app.AppController;
import bil495.not2do.helper.SessionManager;
import bil495.not2do.model.Not2DoModel;
import bil495.not2do.model.UserModel;

import static android.content.ContentValues.TAG;

/**
 * Created by burak on 8/5/2017.
 */

public class EditProfileActivity extends AppCompatActivity {
    private EditText inputName;
    private EditText inputSurName;
    private EditText inputUserName;
    private EditText inputEmail;
    private EditText inputBio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        inputName = (EditText) findViewById(R.id.name);
        inputSurName = (EditText) findViewById(R.id.surname);
        inputUserName = (EditText) findViewById(R.id.username);
        inputEmail = (EditText) findViewById(R.id.email);
        inputBio = (EditText) findViewById(R.id.bio);

        UserModel user = (UserModel) getIntent().getSerializableExtra("user");


        inputName.setText(user.getName());
        inputSurName.setText(user.getSurname());
        inputUserName.setText(user.getUsername());
        inputEmail.setText(user.getEmail());
        inputBio.setText(user.getBio());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);



        Button button = (Button) findViewById(R.id.editBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = inputName.getText().toString().trim();
                String surname = inputSurName.getText().toString().trim();
                String username = inputUserName.getText().toString().trim();
                String email = inputEmail.getText().toString().trim();
                String bio = inputBio.getText().toString().trim();
                requestToServer(name, surname, username, email, bio);
            }
        });
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

    private void requestToServer(final String name, final String surname, final String username, final String email, final String bio){
        final String tag_string_req = "req_edit_profile";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.getURLEditProfile(), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Edit Profile Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        SessionManager session = new SessionManager(getBaseContext());
                        Integer userId = jObj.getInt("user_id");
                        String token = jObj.getString("token");
                        String username = jObj.getString("username");

                        session.setUsernameAndToken(userId, username, token);
                        finish();

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
                // As of f605da3 the following should work
                NetworkResponse response = error.networkResponse;
                if (error instanceof ServerError && response != null) {
                    Log.e("ZAA", response.toString());
                    try {
                        String res = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));

                    } catch (UnsupportedEncodingException e1) {
                        // Couldn't properly decode data to string
                        e1.printStackTrace();
                    }
                }
                Toast.makeText(getBaseContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                SessionManager sessionManager = new SessionManager(getBaseContext());
                params.put("user_id", sessionManager.getUserID().toString());
                params.put("token", sessionManager.getToken());
                params.put("name", name);
                params.put("surname", surname);
                params.put("username", username);
                params.put("email", email);
                params.put("bio", bio);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}