package bil495.not2do.holder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import bil495.not2do.ParticipantsActivity;
import bil495.not2do.R;
import bil495.not2do.app.AppConfig;
import bil495.not2do.model.UserModel;
import butterknife.BindView;
import butterknife.ButterKnife;

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
                setFollowButton();
                mItem.setFollow(!mItem.isFollow());
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
                intent.putExtra("url", AppConfig.getURLFollowersOfUser(mItem.getUsername()));
                intent.putExtra("title", "Followers of " + mItem.getUsername());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        followingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ParticipantsActivity.class);
                intent.putExtra("url", AppConfig.getURLFollowingOfUser(mItem.getUsername()));
                intent.putExtra("title", "Followings of " + mItem.getUsername());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }
    private void setFollowButton(){
        if (mItem.isFollow()){
            mFollowButton.setBackground(context.getDrawable(R.drawable.btn_to_follow));
            mFollowButton.setText("Follow");
            mFollowButton.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }else{
            mFollowButton.setBackground(context.getDrawable(R.drawable.btn_following));
            mFollowButton.setText("Following");
            mFollowButton.setTextColor(Color.WHITE);
        }
    }
}
