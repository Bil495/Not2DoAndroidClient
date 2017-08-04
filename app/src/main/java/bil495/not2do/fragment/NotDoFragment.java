package bil495.not2do.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import bil495.not2do.LoginActivity;
import bil495.not2do.R;
import bil495.not2do.UserProfileActivity;
import bil495.not2do.app.AppConfig;
import bil495.not2do.app.AppController;
import bil495.not2do.helper.LikeManager;
import bil495.not2do.helper.SessionManager;
import bil495.not2do.model.Not2DoModel;
import bil495.not2do.model.UserModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class NotDoFragment extends Fragment {
    private OnListFragmentInteractionListener mListener;
    private RecyclerView recyclerView;
    MyNotDoRecyclerViewAdapter mAdapter;
    private ProgressDialog pDialog;
    private List<Not2DoModel> list;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NotDoFragment() {
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Progress dialog
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            list = (List<Not2DoModel>) bundle.getSerializable("list");
            if(list == null){
                list = new ArrayList<>();
                String url = bundle.getString("url");
                requestToServer(url);
                setHasOptionsMenu(true);
            }
        }
    }

    public void refreshList(List<Not2DoModel> list){
        this.list = list;
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notdo_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            mAdapter = new MyNotDoRecyclerViewAdapter(getContext(), list, mListener);
            recyclerView.setAdapter(mAdapter);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
        Log.d(TAG, "NotDoFragment onResume called");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_reload) {
            String url = getArguments().getString("url");
            list = new ArrayList<>();
            requestToServer(url);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void requestToServer(String url){
        String tag_string_req = "req_global_timeline";

        pDialog.setMessage("Getting timeline ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Timeline Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = false;

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        JSONArray usersArr = jObj.getJSONArray("users");
                        Map<Integer, UserModel> users = new HashMap<>();
                        for (int i = 0; i < usersArr.length(); i++) {
                            JSONObject obj = usersArr.getJSONObject(i);

                            UserModel user = new UserModel();
                            user.setId(obj.getInt("id"));
                            user.setUsername(obj.getString("username"));
                            user.setName(obj.getString("name"));
                            user.setSurname(obj.getString("surname"));
                            //user.setProfilePic(obj.getString("pp_url"));
                            user.setBio("bio");

                            users.put(user.getId(), user);
                        }
                        JSONObject participants = jObj.getJSONObject("participants_count");
                        JSONObject failures = jObj.getJSONObject("failed_participants_count");
                        JSONObject myFails = jObj.getJSONObject("failed?");
                        JSONObject myParticipates = jObj.getJSONObject("participated?");

                        JSONArray arr = jObj.getJSONArray("items");
                        for (int i = 0; i < arr.length(); i++){
                            JSONObject obj = arr.getJSONObject(i);


                            Not2DoModel not2Do = new Not2DoModel();
                            not2Do.setId(obj.getLong("id"));
                            not2Do.setContent(obj.getString("title"));
                            not2Do.setDidCreatorFailed(obj.getBoolean("failed"));
                            not2Do.setParticipants(participants.getInt(Long.toString(not2Do.getId())));
                            not2Do.setFailures(failures.getInt(Long.toString(not2Do.getId())));
                            not2Do.setDidParticipate(myParticipates.getBoolean(Long.toString(not2Do.getId())));
                            not2Do.setDidFail(myFails.getBoolean(Long.toString(not2Do.getId())));

                            Date date = null;
                            try {
                                date = AppConfig.format.parse(obj.getString("created_at"));
                            }catch (ParseException parseException){

                            }
                            not2Do.setCreatedAt(date);
                            not2Do.setCreator(users.get(obj.getInt("user_id")));

                            list.add(not2Do);
                            LikeManager.LIKES.put(not2Do.getId(), not2Do);
                        }
                        mAdapter.notifyDataSetChanged();


                    } else {
                        // Error in login. Get the error message
                        String errorMsg = jObj.getString("error_msg");
                        Toast.makeText(getContext(),
                                errorMsg, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
                hideDialog();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                SessionManager sessionManager = new SessionManager(getContext());
                params.put("user_id", Integer.toString(sessionManager.getUserID()));
                params.put("token", sessionManager.getToken());

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(Not2DoModel item);
    }
}
