package bil495.not2do.holder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import bil495.not2do.ParticipantsActivity;
import bil495.not2do.R;
import bil495.not2do.app.AppConfig;
import bil495.not2do.app.AppController;
import bil495.not2do.helper.SessionManager;
import bil495.not2do.model.UserModel;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

/**
 * Created by burak on 7/25/2017.
 */

public class UserProfileViewHolder extends RecyclerView.ViewHolder {
    private Context context;
    @BindView(R.id.txt_fullname)
    TextView mFullNameView;
    @BindView(R.id.txt_username)
    TextView mUsernameView;
    @BindView(R.id.txt_bio)
    TextView mBioView;
    @BindView(R.id.txtPostCount)
    TextView mPostCountView;
    @BindView(R.id.txtParticipatedCount)
    TextView mParticipateCountView;
    @BindView(R.id.txtFollowersCount)
    TextView mFollowersCountView;
    @BindView(R.id.txtFollowingCount)
    TextView mFollowingCountView;
    @BindView(R.id.btnFollow)
    Button mFollowButton;
    @BindView(R.id.action_posts)
    LinearLayout postLayout;
    @BindView(R.id.action_participatings)
    LinearLayout participatingLayout;
    @BindView(R.id.action_followers)
    LinearLayout followersLayout;
    @BindView(R.id.action_following)
    LinearLayout followingLayout;
    @BindView(R.id.container)
    ViewPager viewPager;

    public UserModel mItem;
    public UserProfileViewHolder(Context context, View view) {
        super(view);
        this.context = context;
        ButterKnife.bind(this, view);
    }
    public void bindView(final UserModel userModel) {
        this.mItem = userModel;

        mFullNameView.setText(mItem.getName() + " " + mItem.getSurname());
        mUsernameView.setText("@" + mItem.getUsername());
        mBioView.setText(mItem.getBio());
        mPostCountView.setText(Integer.toString(mItem.getPosts()));
        mParticipateCountView.setText(Integer.toString(mItem.getParticipating()));
        mFollowersCountView.setText(Integer.toString(mItem.getFollowers()));
        mFollowingCountView.setText(Integer.toString(mItem.getFollowing()));
        setFollowButton();
        mFollowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SessionManager sessionManager = new SessionManager(context);
                if(mItem.getId().equals(sessionManager.getUserID())) {
                    Intent intent = new Intent(context, ParticipantsActivity.class);
                    intent.putExtra("url", AppConfig.getURLFollowersOfUser(mItem.getId()));
                    intent.putExtra("title", "Followers of " + mItem.getUsername());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }else if (mItem.isFollow()){
                    serverRequest(AppConfig.getURLUnfollow(mItem.getId()));
                    mItem.setFollow(!mItem.isFollow());
                }else {
                    serverRequest(AppConfig.getURLFollow(mItem.getId()));
                    mItem.setFollow(!mItem.isFollow());
                }
                setFollowButton();
            }
        });

        postLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });
        participatingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });
        followersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ParticipantsActivity.class);
                intent.putExtra("url", AppConfig.getURLFollowersOfUser(mItem.getId()));
                intent.putExtra("title", "Followers of " + mItem.getUsername());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        followingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ParticipantsActivity.class);
                intent.putExtra("url", AppConfig.getURLFollowingOfUser(mItem.getId()));
                intent.putExtra("title", "Followings of " + mItem.getUsername());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    private void serverRequest(String url){
        // Tag used to cancel the request
        String tag_string_req = "req_login";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Follow/Unfollow Response: " + response.toString());
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Follow/Unfollow Error: " + error.getMessage());
                Toast.makeText(context,
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                SessionManager sessionManager = new SessionManager(context);
                params.put("user_id", sessionManager.getUserID().toString());
                params.put("token", sessionManager.getToken());

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void setFollowButton(){
        SessionManager sessionManager = new SessionManager(context);
        if (mItem.isFollow()){
            mFollowButton.setBackground(context.getDrawable(R.drawable.btn_following));
            mFollowButton.setText("Following");
            mFollowButton.setTextColor(Color.WHITE);
        }else if(mItem.getId().equals(sessionManager.getUserID())) {
            mFollowButton.setBackground(context.getDrawable(R.drawable.btn_following));
            mFollowButton.setText("Edit Profile");
            mFollowButton.setTextColor(Color.WHITE);
        }else{
            mFollowButton.setBackground(context.getDrawable(R.drawable.btn_to_follow));
            mFollowButton.setText("Follow");
            mFollowButton.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
    }
}
