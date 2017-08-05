package bil495.not2do;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import bil495.not2do.app.AppConfig;
import bil495.not2do.fragment.NotDoFragment;
import bil495.not2do.helper.SessionManager;
import bil495.not2do.holder.Not2DoViewHolder;
import bil495.not2do.model.Not2DoModel;
import bil495.not2do.model.UserModel;

public class MainActivity extends AppCompatActivity implements NotDoFragment.OnListFragmentInteractionListener {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Not2DoViewHolder.sessionManager = new SessionManager(getBaseContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), CreateNot2DoActivity.class);
                startActivity(intent);
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == R.id.action_logout){
            SessionManager sessionManager = new SessionManager(this);
            sessionManager.setLogin(false);
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else if(id == R.id.action_my_profile){
            SessionManager sessionManager = new SessionManager(this);
            Intent intent = new Intent(getBaseContext(), UserProfileActivity.class);
            UserModel userModel = new UserModel();
            userModel.setId(sessionManager.getUserID());
            userModel.setUsername(sessionManager.getUsername());
            intent.putExtra("user", userModel);
            startActivity(intent);
        }else if(id == R.id.action_everyone){
            Intent intent = new Intent(getBaseContext(), ParticipantsActivity.class);
            intent.putExtra("url", AppConfig.getURLAllUsers());
            intent.putExtra("title", "All Users");
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onListFragmentInteraction(Not2DoModel item) {
        Intent intent = new Intent(this, Not2DoActivity.class);
        intent.putExtra("not2do", item);
        startActivity(intent);
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Log.d("Tag", (position+1) + " fragment olusturuldu");
            switch (position) {
                case 0:
                    NotDoFragment fragment0 = new NotDoFragment();
                    Bundle bundle0 = new Bundle();
                    bundle0.putString("url", AppConfig.getURLGlobalTimeline(0));
                    fragment0.setArguments(bundle0);
                    return fragment0;
                case 1:
                    NotDoFragment fragment1 = new NotDoFragment();
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("url", AppConfig.getURLFriendsTimeline(0));
                    fragment1.setArguments(bundle1);
                    return fragment1;
                case 2:
                    NotDoFragment fragment2 = new NotDoFragment();
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("url", AppConfig.getURLFriendsTimeline(0));
                    fragment2.setArguments(bundle2);
                    return fragment2;
            }
            return new NotDoFragment();
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.title_fragment_timeline);
                case 1:
                    return getString(R.string.title_fragment_followings);
                case 2:
                    return getString(R.string.title_fragment_profile);
            }
            return null;
        }
    }
}
