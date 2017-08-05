package bil495.not2do.holder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextSwitcher;
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
import bil495.not2do.UserProfileActivity;
import bil495.not2do.app.AppConfig;
import bil495.not2do.app.AppController;
import bil495.not2do.helper.SessionManager;
import bil495.not2do.model.Not2DoModel;
import bil495.not2do.model.UserModel;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.ContentValues.TAG;

/**
 * Created by burak on 7/25/2017.
 */

public class Not2DoViewHolder extends RecyclerView.ViewHolder {
    public static SessionManager sessionManager;
    private Context context;
    public final View mView;
    @BindView(R.id.fullname)
    TextView mFullNameView;
    @BindView(R.id.username)
    TextView mUsernameView;
    @BindView(R.id.failed)
    TextView mFailedView;
    @BindView(R.id.content)
    TextView mContentView;
    @BindView(R.id.thumbnail)
    ImageView mProfilePicView;
    @BindView(R.id.overflow)
    public ImageView mOverFlow;
    @BindView(R.id.btnLike)
    ImageView btnLike;
    @BindView(R.id.btnFail)
    ImageView btnFail;
    @BindView(R.id.tsLikesCounter)
    TextSwitcher tsLikesCounter;
    @BindView(R.id.tsFailsCounter)
    TextSwitcher tsFailsCounter;
    public Not2DoModel mItem;

    public Not2DoViewHolder(Context context, View view) {
        super(view);
        mView = view;
        this.context = context;
        ButterKnife.bind(this, view);
    }

    public void bindView(final Not2DoModel not2DoModel) {
        this.mItem = not2DoModel;
        btnLike.setImageResource(not2DoModel.isDidParticipate() ? R.drawable.ic_heart_red : R.drawable.ic_heart_outline_grey);
        tsLikesCounter.setCurrentText(mItem.getParticipants() + " participants");

        mFullNameView.setText(not2DoModel.getCreator().getName() + " " +
                not2DoModel.getCreator().getSurname() );
        mUsernameView.setText("@" + not2DoModel.getCreator().getUsername() );
        mContentView.setText(not2DoModel.getContent());
        mProfilePicView.setImageResource(R.drawable.user);
        tsLikesCounter.setCurrentText(not2DoModel.getParticipants() + " participants");
        tsFailsCounter.setCurrentText(not2DoModel.getFailures() + " failures");
        setButtons();
        MyProfileClickListener listener = new MyProfileClickListener(mItem.getCreator());
        mFullNameView.setOnClickListener(listener);
        mUsernameView.setOnClickListener(listener);
        mProfilePicView.setOnClickListener(listener);

        mFailedView.setVisibility(mItem.isDidCreatorFailed() ? View.VISIBLE : View.INVISIBLE);
        if (mItem.getCreator().getId().equals(sessionManager.getUserID())){
            btnLike.setVisibility(View.GONE);
            btnFail.setVisibility(View.VISIBLE);
            btnFail.setImageResource(mItem.isDidCreatorFailed() ? R.drawable.ic_fire_red : R.drawable.ic_fire_outline);
        }
        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mItem.isDidParticipate()){
                    serverRequest(AppConfig.getURLParticipate(not2DoModel.getId()));
                    mItem.setParticipants(mItem.getParticipants() + 1);
                    mItem.setDidParticipate(true);
                    setButtons();
                    updateLikesCounter(true, mItem.getParticipants());
                }
            }
        });

        btnFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItem.getCreator().getId().equals(sessionManager.getUserID())){
                    if(!mItem.isDidFail()){
                        serverRequest(AppConfig.getURLFailure(not2DoModel.getId()));
                        btnFail.setImageResource(R.drawable.ic_fire_red);
                        mItem.setDidCreatorFailed(true);
                        mFailedView.setVisibility(View.VISIBLE);
                    }
                }else{
                    if(!mItem.isDidFail()){
                        serverRequest(AppConfig.getURLFailure(not2DoModel.getId()));
                        mItem.setFailures(mItem.getFailures()+1);
                        mItem.setDidFail(true);
                        setButtons();
                        tsFailsCounter.setText(mItem.getFailures() + " failures");
                    }
                }
            }
        });

        tsLikesCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ParticipantsActivity.class);
                intent.putExtra("url", AppConfig.getURLParticipantsOfNot2Do(mItem.getId()));
                intent.putExtra("title", "Participants");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        tsFailsCounter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ParticipantsActivity.class);
                intent.putExtra("url", AppConfig.getURLFailuresOfNot2Do(mItem.getId()));
                intent.putExtra("title", "Failures");
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
                Log.d(TAG, "Participate Response: " + response.toString());
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Participate Error: " + error.getMessage());
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

    private void setButtons(){

        if(mItem.isDidParticipate()){
            btnLike.setImageResource(R.drawable.ic_heart_red);
            btnFail.setVisibility(View.VISIBLE);
            btnFail.setImageResource(mItem.isDidFail() ? R.drawable.ic_fire_red : R.drawable.ic_fire_outline);
        }else {
            btnLike.setImageResource(R.drawable.ic_heart_outline_grey);
            btnFail.setVisibility(View.GONE);
        }
    }

    private void updateLikesCounter(boolean animated, Integer count) {
        String likesCountText = count + " participants";

        if (animated) {
            tsLikesCounter.setText(likesCountText);
        } else {
            tsLikesCounter.setCurrentText(likesCountText);
        }
    }


    @Override
    public String toString() {
        return super.toString() + " '" + mContentView.getText() + "'";
    }class MyProfileClickListener implements View.OnClickListener {
        UserModel user;

        public MyProfileClickListener(final UserModel user) {
            this.user = user;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(context, UserProfileActivity.class);
            intent.putExtra("user", user);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }
}