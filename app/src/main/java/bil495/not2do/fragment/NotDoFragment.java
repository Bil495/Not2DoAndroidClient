package bil495.not2do.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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

import bil495.not2do.R;
import bil495.not2do.app.AppConfig;
import bil495.not2do.app.AppController;
import bil495.not2do.helper.SessionManager;
import bil495.not2do.model.Not2DoModel;
import bil495.not2do.model.UserModel;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
    private MyNotDoRecyclerViewAdapter mAdapter;
    private ProgressDialog pDialog;
    private List<Not2DoModel> list;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public NotDoFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static NotDoFragment newInstance(int columnCount) {
        return new NotDoFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Progress dialog
        pDialog = new ProgressDialog(getContext());
        pDialog.setCancelable(false);


        list = new ArrayList<Not2DoModel>();
        requestToServer(0);
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

    private void requestToServer(int lessThan){
        String tag_string_req = "req_global_timeline";

        pDialog.setMessage("Getting timeline ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.getURLGlobalTimeline(lessThan), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Timeline Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    // Check for error node in json
                    if (!error) {
                        // user successfully logged in
                        JSONArray arr = jObj.getJSONArray("not2dos");
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

                            list.add(not2Do);
                        }
                        mAdapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Timeline is here.", Toast.LENGTH_LONG).show();


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
                params.put("username", sessionManager.getUsername());
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
