package bil495.not2do.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import bil495.not2do.CreateNot2DoActivity;
import bil495.not2do.LoginActivity;
import bil495.not2do.ParticipantsActivity;
import bil495.not2do.R;
import bil495.not2do.RegisterActivity;
import bil495.not2do.UserProfileActivity;
import bil495.not2do.app.AppConfig;
import bil495.not2do.app.AppController;
import bil495.not2do.fragment.NotDoFragment.OnListFragmentInteractionListener;
import bil495.not2do.helper.LikeManager;
import bil495.not2do.helper.SessionManager;
import bil495.not2do.holder.Not2DoViewHolder;
import bil495.not2do.model.Not2DoModel;
import bil495.not2do.model.UserModel;
import butterknife.BindView;
import butterknife.ButterKnife;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class MyNotDoRecyclerViewAdapter extends RecyclerView.Adapter<Not2DoViewHolder> {

    private final Context mContext;
    private final List<Not2DoModel> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyNotDoRecyclerViewAdapter(Context context, List<Not2DoModel> items, OnListFragmentInteractionListener listener) {
        mContext = context;
        mValues = items;
        mListener = listener;
    }

    @Override
    public Not2DoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_notdo, parent, false);
        return new Not2DoViewHolder(mContext, view);
    }

    @Override
    public void onBindViewHolder(final Not2DoViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        final Not2DoModel realTime = LikeManager.LIKES.get(holder.mItem.getId());
        holder.bindView(realTime);

        holder.mOverFlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionManager sessionManager = new SessionManager(mContext);
                showPopupMenu(holder.mOverFlow, holder.mItem, holder.mItem.getCreator().getId()
                        .equals(sessionManager.getUserID()));
                notifyDataSetChanged();
            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view, Not2DoModel not2DoModel, boolean owner) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        if(owner)
            inflater.inflate(R.menu.menu_not2do_creator, popup.getMenu());
        else
            inflater.inflate(R.menu.menu_not2do, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener(not2DoModel));
        popup.show();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        Not2DoModel not2DoModel;
        public MyMenuItemClickListener(Not2DoModel not2DoModel) {
            this.not2DoModel = not2DoModel;
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_share:
                    Toast.makeText(mContext, "Sharing", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_edit:
                    Toast.makeText(mContext, "Editing", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, CreateNot2DoActivity.class);
                    intent.putExtra("not2do", not2DoModel);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    return true;
                case R.id.action_remove:
                    Toast.makeText(mContext, "Removing", Toast.LENGTH_SHORT).show();
                    requestToServer(not2DoModel.getId());
                    return true;
                default:
            }
            return false;
        }
    }
    private void requestToServer(final long itemId) {
        // Tag used to cancel the request
        String tag_string_req = "req_removal";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                AppConfig.getURLDeleteNot2Do(itemId), new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    Toast.makeText(mContext,
                            "REMOVED", Toast.LENGTH_LONG).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Registration Error: " + error.getMessage());
                Toast.makeText(mContext,
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<String, String>();
                SessionManager sessionManager = new SessionManager(mContext);
                params.put("user_id", Integer.toString(sessionManager.getUserID()));
                params.put("token", sessionManager.getToken());

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}
