package bil495.not2do.holder;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;

import bil495.not2do.ParticipantsActivity;
import bil495.not2do.R;
import bil495.not2do.UserProfileActivity;
import bil495.not2do.app.AppConfig;
import bil495.not2do.fragment.MyNotDoRecyclerViewAdapter;
import bil495.not2do.model.Not2DoModel;
import bil495.not2do.model.UserModel;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by burak on 7/25/2017.
 */

public class Not2DoViewHolder extends RecyclerView.ViewHolder {
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

        btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mItem.isDidParticipate()){
                    mItem.setParticipants(mItem.getParticipants() - 1);
                    if(mItem.isDidFail()){
                        mItem.setFailures(mItem.getFailures()-1);
                        tsFailsCounter.setText(mItem.getFailures() + " failures");
                        mItem.setDidFail(false);
                    }
                }else{
                    mItem.setParticipants(mItem.getParticipants() + 1);
                }
                mItem.setDidParticipate(!mItem.isDidParticipate());
                setButtons();
                updateLikesCounter(true, mItem.getParticipants());
            }
        });

        btnFail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mItem.isDidParticipate()){
                    mItem.setDidFail(!mItem.isDidFail());
                    setButtons();
                    if(mItem.isDidFail()){
                        mItem.setFailures(mItem.getFailures()+1);
                    }else{
                        mItem.setFailures(mItem.getFailures()-1);
                    }
                    tsFailsCounter.setText(mItem.getFailures() + " failures");
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