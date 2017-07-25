package bil495.not2do;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sackcentury.shinebuttonlib.ShineButton;

import bil495.not2do.helper.LikeManager;
import bil495.not2do.model.Not2DoModel;

/**
 * Created by burak on 7/18/2017.
 */

public class Not2DoActivity extends AppCompatActivity {

    private Not2DoModel not2Do;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not2do);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        setTitle("Not To Do");

        Not2DoModel not2DoGiven = (Not2DoModel) getIntent().getSerializableExtra("not2do");
        not2Do = LikeManager.LIKES.get(not2DoGiven.getId());
        setUI();
        ShineButton likeButton = (ShineButton) findViewById(R.id.like_button);
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click", "like button clicked");
                if(not2Do.isDidParticipate()){
                    not2Do.setParticipants(not2Do.getParticipants() - 1);
                }else{
                    not2Do.setParticipants(not2Do.getParticipants() + 1);
                }
                not2Do.setDidParticipate(!not2Do.isDidParticipate());
                setUI();
            }
        });
    }

    private void setUI(){
        Log.d("Not2do", not2Do.toString());
        ((TextView) findViewById(R.id.fullname)).setText(not2Do.getCreator().getName() + " " +
                not2Do.getCreator().getSurname());
        ((TextView) findViewById(R.id.username)).setText("@" + not2Do.getCreator().getUsername());
        ((TextView) findViewById(R.id.content)).setText(not2Do.getContent());
        ((ImageView) findViewById(R.id.thumbnail)).setImageResource(R.drawable.user);
        TextView participants = ((TextView) findViewById(R.id.participants_clickable));
        participants.setText(Integer.toString(not2Do.getParticipants()).concat(" participants"));
        participants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), ParticipantsActivity.class);
                intent.putExtra("not2do", not2Do);
                startActivity(intent);
            }
        });



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem)
    {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(menuItem);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
