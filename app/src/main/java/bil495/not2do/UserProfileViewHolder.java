package bil495.not2do;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

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
    @BindView(R.id.txtFollowersCount)
    TextView mFollowersCountView;
    @BindView(R.id.txtFollowingCount)
    TextView mFollowingCountView;
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
        mFollowersCountView.setText(Integer.toString(mItem.getFollowers()));
        mFollowingCountView.setText(Integer.toString(mItem.getFollowing()));
    }
}
