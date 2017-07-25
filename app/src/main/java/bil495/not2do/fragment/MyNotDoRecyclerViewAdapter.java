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
import bil495.not2do.UserProfileActivity;
import bil495.not2do.app.AppConfig;
import bil495.not2do.fragment.NotDoFragment.OnListFragmentInteractionListener;
import bil495.not2do.helper.LikeManager;
import bil495.not2do.helper.SessionManager;
import bil495.not2do.holder.Not2DoViewHolder;
import bil495.not2do.model.Not2DoModel;
import bil495.not2do.model.UserModel;
import butterknife.BindView;
import butterknife.ButterKnife;

import java.util.List;

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
                showPopupMenu(holder.mOverFlow, holder.mItem.getCreator().getUsername()
                        .equals(sessionManager.getUsername()));
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
    private void showPopupMenu(View view, boolean owner) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        if(owner)
            inflater.inflate(R.menu.menu_not2do, popup.getMenu());
        else
            inflater.inflate(R.menu.menu_not2do, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
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
