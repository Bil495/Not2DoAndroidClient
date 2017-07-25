package bil495.not2do;

/**
 * Created by burak on 7/25/2017.
 */
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import bil495.not2do.app.AppConfig;
import bil495.not2do.app.AppController;
import bil495.not2do.fragment.MyNotDoRecyclerViewAdapter;
import bil495.not2do.fragment.NotDoFragment;
import bil495.not2do.helper.LikeManager;
import bil495.not2do.helper.SessionManager;
import bil495.not2do.model.Not2DoModel;
import bil495.not2do.model.UserModel;

import static android.content.ContentValues.TAG;

public class UserProfileActivity extends AppCompatActivity  implements NotDoFragment.OnListFragmentInteractionListener {
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private UserModel user;
    private List<Not2DoModel> participated;
    private List<Not2DoModel> created;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tlUserProfileTabs);
        tabLayout.setupWithViewPager(mViewPager);

        user = (UserModel) getIntent().getSerializableExtra("user");
        participated = new ArrayList<Not2DoModel>();
        created = new ArrayList<Not2DoModel>();

        setTitle(user.getName() + " " + user.getSurname());
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

    @Override
    public void onListFragmentInteraction(Not2DoModel item) {
        //TODO Start not2do activity here
        Intent intent = new Intent(this, Not2DoActivity.class);
        intent.putExtra("not2do", item);
        startActivity(intent);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Log.d("Tag", (position+1) + " fragment olusturuldu");
            switch (position) {
                case 0:
                    NotDoFragment fragment = new NotDoFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("list", (Serializable) created);
                    fragment.setArguments(bundle);
                    return fragment;
                case 1:
                    NotDoFragment fragment2 = new NotDoFragment();
                    Bundle bundle2 = new Bundle();
                    bundle2.putSerializable("list", (Serializable) participated);
                    fragment2.setArguments(bundle2);
                    return fragment2;
            }
            return new NotDoFragment();
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.title_fragment_user_timeline);
                case 1:
                    return getString(R.string.title_fragment_participated);
            }
            return null;
        }
    }

    private void requestToServer(){
        final String tag_string_req = "req_user_profile";
        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.getURLUserProfile(user.getUsername()), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Timeline Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        JSONObject oUser = jObj.getJSONObject("user");
                        user = new UserModel();
                        user.setUsername(oUser.getString("username"));
                        user.setName(oUser.getString("name"));
                        user.setSurname(oUser.getString("surname"));
                        user.setProfilePic(oUser.getString("pp_url"));
                        user.setBio(oUser.getString("bio"));
                        user.setFollowing(oUser.getInt("followed"));
                        user.setFollowers(oUser.getInt("followers"));
                        JSONArray arr = jObj.getJSONArray("participating");
                        JSONArray arr2 = jObj.getJSONArray("owning");
                        user.setPosts(arr2.length());
                        for (int i = 0; i < arr.length(); i++){
                            JSONObject obj = arr.getJSONObject(i);


                            UserModel user = new UserModel();
                            user.setUsername(obj.getString("username"));
                            user.setName(obj.getString("name"));
                            user.setSurname(obj.getString("surname"));
                            user.setProfilePic(obj.getString("pp_url"));

                            Not2DoModel not2Do = new Not2DoModel();
                            not2Do.setId(obj.getLong("not2do_id"));
                            not2Do.setContent(obj.getString("content"));
                            not2Do.setParticipants(obj.getInt("participants"));

                            Date date = null;
                            try {
                                date = AppConfig.format.parse(obj.getString("created_at"));
                            }catch (ParseException parseException){

                            }
                            not2Do.setCreatedAt(date);
                            not2Do.setCreator(user);

                            participated.add(not2Do);
                            LikeManager.LIKES.put(not2Do.getId(), not2Do);
                        }
                        for (int i = 0; i < arr2.length(); i++){
                            JSONObject obj = arr2.getJSONObject(i);
                            Not2DoModel not2Do = new Not2DoModel();
                            not2Do.setId(obj.getLong("not2do_id"));
                            not2Do.setContent(obj.getString("content"));
                            not2Do.setParticipants(obj.getInt("participants"));

                            Date date = null;
                            try {
                                date = AppConfig.format.parse(obj.getString("created_at"));
                            }catch (ParseException parseException){

                            }
                            not2Do.setCreatedAt(date);
                            not2Do.setCreator(user);

                            created.add(not2Do);
                            LikeManager.LIKES.put(not2Do.getId(), not2Do);
                        }
                        UserProfileViewHolder viewHolder =
                                new UserProfileViewHolder(getBaseContext(), findViewById(android.R.id.content));
                        viewHolder.bindView(user);
                        Toast.makeText(getBaseContext(), "Profile loaded.", Toast.LENGTH_LONG).show();


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
                params.put("username", sessionManager.getUsername());
                params.put("token", sessionManager.getToken());

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}