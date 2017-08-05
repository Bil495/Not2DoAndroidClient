package bil495.not2do.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

import bil495.not2do.R;
import bil495.not2do.UserProfileActivity;
import bil495.not2do.model.UserModel;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by burak on 7/20/2017.
 */
public class MyUserRecyclerViewAdapter extends RecyclerView.Adapter<MyUserRecyclerViewAdapter.ViewHolder> {

    private final Context mContext;
    private final List<UserModel> mValues;

    public MyUserRecyclerViewAdapter(Context context, List<UserModel> items) {
        mContext = context;
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mFullNameView.setText(holder.mItem.getName() + " " +
                holder.mItem.getSurname() );
        holder.mUsernameView.setText("@" + holder.mItem.getUsername() );
        holder.mProfilePicView.setImageResource(R.drawable.user);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO open user profile
                Intent intent = new Intent(mContext, UserProfileActivity.class);
                intent.putExtra("user", holder.mItem);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        @BindView(R.id.fullname)
        TextView mFullNameView;
        @BindView(R.id.username)
        TextView mUsernameView;
        @BindView(R.id.thumbnail)
        ImageView mProfilePicView;
        public UserModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            ButterKnife.bind(this, view);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mUsernameView.getText() + "'";
        }
    }
}
