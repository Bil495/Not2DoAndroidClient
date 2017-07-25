package bil495.not2do.fragment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import bil495.not2do.ParticipantsActivity;
import bil495.not2do.R;
import bil495.not2do.fragment.NotDoFragment.OnListFragmentInteractionListener;
import bil495.not2do.helper.LikeManager;
import bil495.not2do.model.Not2DoModel;
import butterknife.BindView;
import butterknife.ButterKnife;

import java.util.List;

public class MyNotDoRecyclerViewAdapter extends RecyclerView.Adapter<MyNotDoRecyclerViewAdapter.Not2DoViewHolder> {

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
                showPopupMenu(holder.mOverFlow);
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
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_not2do, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public static class Not2DoViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        public final View mView;
        @BindView(R.id.fullname)
        TextView mFullNameView;
        @BindView(R.id.username)
        TextView mUsernameView;
        @BindView(R.id.content)
        TextView mContentView;
        @BindView(R.id.thumbnail)
        ImageView mProfilePicView;
        @BindView(R.id.overflow)
        ImageView mOverFlow;
        @BindView(R.id.btnLike)
        ImageView btnLike;
        @BindView(R.id.tsLikesCounter)
        TextSwitcher tsLikesCounter;
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
            btnLike.setImageResource(not2DoModel.isDidParticipate() ? R.drawable.ic_heart_red : R.drawable.ic_heart_outline_grey);
            tsLikesCounter.setCurrentText(not2DoModel.getParticipants() + " participants");

            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mItem.isDidParticipate()){
                        mItem.setParticipants(mItem.getParticipants() - 1);

                    }else{
                        mItem.setParticipants(mItem.getParticipants() + 1);
                    }
                    mItem.setDidParticipate(!mItem.isDidParticipate());
                    btnLike.setImageResource(mItem.isDidParticipate() ? R.drawable.ic_heart_red : R.drawable.ic_heart_outline_grey);
                    updateLikesCounter(true, mItem.getParticipants());
                }
            });

            tsLikesCounter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ParticipantsActivity.class);
                    intent.putExtra("not2do", mItem);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent);
                }
            });
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
        }
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_share:
                    Toast.makeText(mContext, "Sharing", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_remove:
                    Toast.makeText(mContext, "Removing", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }
}
