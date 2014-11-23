package wine.cmsc436.wine;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import parse.subclasses.User;

/**
 * Created by dylan on 11/19/14.
 */
public class UserProfile extends ListActivity {

    // Local Vars
    private static final int ADD_REVIEW_REQEST = 0;
    private static final String TAG = "CMSC436-Wine-App";

    WineReviewListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.your_profile, getListView(), false);
        getListView().addHeaderView(header);

        // Create new ListAdapter
        mAdapter = new WineReviewListAdapter(getApplicationContext());

        // Put divider between ToDoItems and FooterView
        getListView().setFooterDividersEnabled(true);

        // Inflate footerView for footer_view.xml file
        TextView footerView = (TextView) getLayoutInflater().inflate(R.layout.review_footer, null);

        // Attach the adapter to this ListActivity's ListView
        setListAdapter(mAdapter);

        // Add footerView to ListView
        getListView().addFooterView(footerView);

        footerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Entered footerView.OnClickListener.onClick()");

                // Implement OnClick().
                Intent wine_item = new Intent(UserProfile.this, NewWineReview.class);
                startActivityForResult(wine_item, ADD_REVIEW_REQEST);
            }
        });

        // get profile data
        TextView nameView = (TextView) findViewById(R.id.tv_wine_review);
        nameView.setText(User.getCurrentUser().getString("name"));

    }
}
