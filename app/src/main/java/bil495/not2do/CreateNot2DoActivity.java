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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sackcentury.shinebuttonlib.ShineButton;

import bil495.not2do.fragment.MyNotDoRecyclerViewAdapter;
import bil495.not2do.helper.LikeManager;
import bil495.not2do.holder.Not2DoViewHolder;
import bil495.not2do.model.Not2DoModel;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by burak on 7/18/2017.
 */

public class CreateNot2DoActivity extends AppCompatActivity {
    EditText mContentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_not2do);
        mContentView = (EditText) findViewById(R.id.content);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        Not2DoModel not2DoGiven = (Not2DoModel) getIntent().getSerializableExtra("not2do");
        if(not2DoGiven != null){
            mContentView.setText(not2DoGiven.getContent());
            setTitle("Edit Not2Do");
        }else{
            setTitle("New  Not2Do");
        }
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