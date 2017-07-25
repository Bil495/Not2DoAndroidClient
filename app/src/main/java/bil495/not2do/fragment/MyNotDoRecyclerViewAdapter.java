package bil495.not2do.fragment;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.Image;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sackcentury.shinebuttonlib.ShineButton;

import bil495.not2do.R;
import bil495.not2do.fragment.NotDoFragment.OnListFragmentInteractionListener;
import bil495.not2do.helper.LikeManager;
import bil495.not2do.model.Not2DoModel;

import java.util.List;

public class MyNotDoRecyclerViewAdapter extends RecyclerView.Adapter<MyNotDoRecyclerViewAdapter.ViewHolder> {

    private final Context mContext;
    private final List<Not2DoModel> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyNotDoRecyclerViewAdapter(Context context, List<Not2DoModel> items, OnListFragmentInteractionListener listener) {
        mContext = context;
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_notdo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        final Not2DoModel realTime = LikeManager.LIKES.get(holder.mItem.getId());
        holder.mFullNameView.setText(holder.mItem.getCreator().getName() + " " +
                holder.mItem.getCreator().getSurname() );
        holder.mUsernameView.setText("@" + holder.mItem.getCreator().getUsername() );
        holder.mContentView.setText(holder.mItem.getContent());
        holder.mProfilePicView.setImageResource(R.drawable.user);
        holder.mParticipantCount.setText(Integer.toString(realTime.getParticipants()));


        if(realTime.isDidParticipate()){
            holder.mParticipantCount.setTextColor(Color.parseColor("#CC9999"));
            holder.mLikeButton.setChecked(true);
        }else{
            holder.mParticipantCount.setTextColor(Color.parseColor("#666666"));
            holder.mLikeButton.setChecked(false);
        }

        holder.mLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(realTime.isDidParticipate()){
                    realTime.setParticipants(realTime.getParticipants() - 1);
                }else{
                    realTime.setParticipants(realTime.getParticipants() + 1);
                }
                realTime.setDidParticipate(!realTime.isDidParticipate());
                notifyDataSetChanged();
            }
        });

        holder.mOverFlow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(holder.mOverFlow);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mFullNameView;
        public final TextView mUsernameView;
        public final TextView mContentView;
        public final ImageView mProfilePicView;
        public final ImageView mOverFlow;
        public final TextView mParticipantCount;
        public final ShineButton mLikeButton;
        public Not2DoModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mFullNameView = (TextView) view.findViewById(R.id.fullname);
            mUsernameView = (TextView) view.findViewById(R.id.username);
            mContentView = (TextView) view.findViewById(R.id.content);
            mProfilePicView = (ImageView) view.findViewById(R.id.thumbnail);
            mOverFlow = (ImageView) view.findViewById(R.id.overflow);
            mParticipantCount = (TextView) view.findViewById(R.id.participant_count);
            mLikeButton = (ShineButton) view.findViewById(R.id.like_button);
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
