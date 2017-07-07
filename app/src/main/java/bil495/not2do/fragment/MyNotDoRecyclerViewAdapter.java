package bil495.not2do.fragment;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import bil495.not2do.R;
import bil495.not2do.fragment.NotDoFragment.OnListFragmentInteractionListener;
import bil495.not2do.model.Not2DoModel;

import java.util.List;

public class MyNotDoRecyclerViewAdapter extends RecyclerView.Adapter<MyNotDoRecyclerViewAdapter.ViewHolder> {

    private final List<Not2DoModel> mValues;
    private final OnListFragmentInteractionListener mListener;

    public MyNotDoRecyclerViewAdapter(List<Not2DoModel> items, OnListFragmentInteractionListener listener) {
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
        holder.mFullNameView.setText(mValues.get(position).getCreator().getName() + " " +
                mValues.get(position).getCreator().getSurname() );
        holder.mUsernameView.setText("@" + mValues.get(position).getCreator().getUsername() );
        holder.mContentView.setText(mValues.get(position).getContent());
        holder.mProfilePicView.setImageResource(R.drawable.user);

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
        public Not2DoModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mFullNameView = (TextView) view.findViewById(R.id.fullname);
            mUsernameView = (TextView) view.findViewById(R.id.username);
            mContentView = (TextView) view.findViewById(R.id.content);
            mProfilePicView = (ImageView) view.findViewById(R.id.thumbnail);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
