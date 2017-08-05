package bil495.not2do;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import bil495.not2do.helper.LikeManager;
import bil495.not2do.holder.Not2DoViewHolder;
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
        Not2DoViewHolder viewHolder =
                new Not2DoViewHolder(getBaseContext(), findViewById(android.R.id.content));
        viewHolder.bindView(not2Do);
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
