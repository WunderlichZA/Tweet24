/**
 * Group 1 Members
 * 1. Itumeleng Dimpane
 * 2. Luzuko Nodada
 * 3. Lusapho Jita
 */
package com.media24.thehub.tweet24;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.TimelineResult;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;
import com.twitter.sdk.android.tweetui.TweetUi;
import com.twitter.sdk.android.tweetui.UserTimeline;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;
import util.ConnectionManager;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.list) ListView listView;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout refreshLayout;

    private static final String TWITTER_KEY = "3KDI4di4WE3AWxb57WjwxWHHS";
    private static final String TWITTER_SECRET = "kyWVrbEzc1u7nYfwCylo3epRddisBoS8xF6KRvCXRLthLprvxw";

    private TweetTimelineListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
//        Fabric.with(this, new Twitter(authConfig));
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new TweetUi());
        setContentView(R.layout.activity_main);
        // bind views
        ButterKnife.bind(this);
        // Set Toolbar
        setSupportActionBar(toolbar);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (adapter != null){
                    refresh();
                }
            }
        });
        if(ConnectionManager.canConnect(this)){
            getTweets();
        }else{
            // we can't connect
            // show no connection layout
            Toast.makeText(MainActivity.this, "Cannot connect.\nCheck your internet connection",
                    Toast.LENGTH_LONG).show();
        }

    }

    /**
     * Gets user timeline
     */
    public void getTweets(){
        final UserTimeline userTimeline = new UserTimeline.Builder()
                .screenName("News24")
                .build();
        adapter = new TweetTimelineListAdapter.Builder(this)
                .setTimeline(userTimeline)
                .build();
        listView.setAdapter(adapter);
    }

    /**
     * Refreshes tweets list
     */
    public void refresh(){

//        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
//            boolean enableRefresh = false;
//            @Override
//            public void onScrollStateChanged(AbsListView view, int scrollState) {
//
//            }
//
//            @Override
//            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//                if(listView != null && listView.getChildCount() > 0){
//                    // check that the first item is visible and that its top matches the parent
//                    enableRefresh = listView.getFirstVisiblePosition() == 0 &&
//                            listView.getChildAt(0).getTop() >= 0;
//                }else{
//                    enableRefresh = false;
//                }
//                refreshLayout.setEnabled(enableRefresh);
//            }
//        });

        refreshLayout.setRefreshing(true);
        adapter.refresh(new Callback<TimelineResult<Tweet>>() {
            @Override
            public void success(Result<TimelineResult<Tweet>> result) {
                refreshLayout.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void failure(TwitterException exception) {
                refreshLayout.setRefreshing(false);
                Toast.makeText(MainActivity.this, "Cannot retrieve tweets at this time, please try again later", Toast.LENGTH_LONG).show();
            }
        });
    }

}
