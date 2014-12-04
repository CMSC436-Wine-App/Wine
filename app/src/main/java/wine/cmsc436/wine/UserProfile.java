package wine.cmsc436.wine;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;

import java.util.List;

import parse.subclasses.Review;
import parse.subclasses.User;

/**
 * Created by dylan on 11/19/14.
 */
public class UserProfile extends ListActivity {

    // Local Vars
    private static final int ADD_REVIEW_REQEST = 0;
    private static final String TAG = "CMSC436-Wine-App";

//    WineReviewListAdapter mAdapter;
    ReviewListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getActionBar().setDisplayHomeAsUpEnabled(true);

        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.your_profile, getListView(), false);
        getListView().addHeaderView(header);

        // Create new ListAdapter
//        mAdapter = new WineReviewListAdapter(getApplicationContext());
        mAdapter = new ReviewListAdapter(getApplicationContext());

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

        User user = User.getCurrentUser();

        // get profile data
        TextView nameView = (TextView) findViewById(R.id.tv_wine_review);
        nameView.setText(user.getString("name"));

        ParseImageView photoImageView = (ParseImageView) findViewById(R.id.iv_user_profile_pic);
        ParseFile imageFile = user.getPhoto();
        // TODO: User image is broken for me
        if (imageFile != null) {
            photoImageView.setParseFile(imageFile);
            photoImageView.loadInBackground();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestcode, int resultcode, Intent data){

        // Checking request code
        if (requestcode == ADD_REVIEW_REQEST){
            if (resultcode == RESULT_OK){
//                mAdapter.add(new WineReviewItem(data));
                mAdapter.loadObjects();

            } else {
                Toast.makeText(getApplicationContext(), "Review did not go through", Toast.LENGTH_LONG).show();
            }
        }

    }

}
