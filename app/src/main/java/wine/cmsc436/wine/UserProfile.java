package wine.cmsc436.wine;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseFile;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.Arrays;

import parse.subclasses.Review;
import parse.subclasses.User;

/**
 * Created by dylan on 11/19/14.
 */
public class UserProfile extends ListActivity {

    private static final int ADD_REVIEW_REQEST = 0;
    public static final int FB_SESSION_RESULT = 101;

    UserReviewListAdapter mAdapter;

    FacebookPost facebookPost;
    private UiLifecycleHelper uiHelper;
    private Intent shareData;

    User selectedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.getActionBar().setDisplayHomeAsUpEnabled(true);

        String userId = User.getObjectId(getIntent().getData());
        GetCallback<ParseUser> userGetCallback = new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e != null) {
                    Toast toast = Toast.makeText(UserProfile.this, e.getMessage(), Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                selectedUser = (User) user;
                updateView(selectedUser);
            }
        };

        facebookPost = new FacebookPost(this, uiHelper);
        uiHelper = new UiLifecycleHelper(this, null);
        uiHelper.onCreate(savedInstanceState);

        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.your_profile, getListView(), false);
        getListView().addHeaderView(header);

        // Create new ListAdapter
        mAdapter = new UserReviewListAdapter(getApplicationContext(), userId);
        // Attach the adapter to this ListActivity's ListView
        setListAdapter(mAdapter);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Review review = (Review) parent.getItemAtPosition(position);
//                Review review = (Review) parent.getAdapter().getItem(position);
                if (getCallingActivity() == null) {
                    Intent intent = new Intent(UserProfile.this, ReviewDetailActivity.class);
                    intent.setData(review.getUri());
                    startActivity(intent);
                } else {
                    Intent intent = new Intent();
                    intent.setData(review.getUri());
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            }
        });

        Button earnBadgeButton = (Button)findViewById(R.id.b_badges);
        earnBadgeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this, BadgeListActivity.class);
                intent.putExtra("isUser", true);
                intent.putExtra("title", "User Profile");
                intent.setData(selectedUser.getUri());
                startActivity(intent);
            }
        });

        Button phButton = (Button)findViewById(R.id.b_purchase_history);
        phButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserProfile.this, PurchaseHistoryActivity.class);
                intent.setData(selectedUser.getUri());
                startActivity(intent);
            }
        });

        if (userId.equals(User.getCurrentUser().getObjectId())) {
            selectedUser = User.getCurrentUser();
            updateView(selectedUser);

            // Put divider between ToDoItems and FooterView
            getListView().setFooterDividersEnabled(true);
            // Inflate footerView for footer_view.xml file
            TextView footerView = (TextView) getLayoutInflater().inflate(R.layout.review_footer, null);
            // Add footerView to ListView
            getListView().addFooterView(footerView);
            footerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent wine_item = new Intent(UserProfile.this, NewWineReview.class);
                    startActivityForResult(wine_item, ADD_REVIEW_REQEST);
                }
            });
        } else {
            ParseQuery<ParseUser> userQuery = User.getQuery();
            userQuery.getInBackground(userId, userGetCallback);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateView(selectedUser);
        mAdapter.notifyDataSetChanged();
        uiHelper.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        uiHelper.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data){

        // Checking request code
        if (requestCode == ADD_REVIEW_REQEST){
            if (resultCode == RESULT_OK){
                mAdapter.loadObjects();
                shareData = data;
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Review submitted.\nShare review on Facebook?")
                        .setPositiveButton("Share", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Session session = ParseFacebookUtils.getSession();
                                if (session != null) {
                                    if (!session.isPermissionGranted("publish_actions")){
                                        // ask for publish permissions
                                        String[] permissions = {"publish_actions"};
                                        Session.NewPermissionsRequest permissionsRequest = new Session.NewPermissionsRequest(UserProfile.this, Arrays.asList(permissions));
                                        permissionsRequest.setRequestCode(FB_SESSION_RESULT);
                                        session.requestNewPublishPermissions(permissionsRequest);
                                    } else {
                                        // publish permissions already granted
                                        facebookPost.showFacebookShareDialog(shareData);
                                    }
                                } else {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Facebook session invalid", Toast.LENGTH_LONG);
                                    toast.show();
                                    Intent data = new Intent();
                                    setResult(RESULT_OK, data);
                                    finish();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", null);
                builder.create().show();
            }
        } else if (requestCode == FB_SESSION_RESULT){
            // publish permission granted
            facebookPost.showFacebookShareDialog(shareData);
        }

        // facebook request result handler
        uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
            @Override
            public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
                Toast toast = Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG);
                toast.show();
            }

            @Override
            public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
                boolean didComplete = FacebookDialog.getNativeDialogDidComplete(data);
                if (didComplete) {
                    String completionGesture = FacebookDialog.getNativeDialogCompletionGesture(data);
                    String postId = FacebookDialog.getNativeDialogPostId(data);
                    Log.i(App.APPTAG, completionGesture);
                    Log.i(App.APPTAG, postId);
                    if (completionGesture.equals("post")) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Review shared", Toast.LENGTH_LONG);
                        toast.show();
                    }
                }
            }
        });

    }

    private void updateView(final User user) {
        if (user != null) {
            // get profile data
            TextView nameView = (TextView) findViewById(R.id.tv_wine_review);
            nameView.setText(user.getString("name"));

//        ParseImageView photoImageView = (ParseImageView) findViewById(R.id.iv_user_profile_pic);
//        ParseFile imageFile = user.getPhoto();
//        if (imageFile != null) {
//            photoImageView.setParseFile(imageFile);
//            photoImageView.loadInBackground();
//        }
            ImageView photoImageView = (ImageView) findViewById(R.id.iv_user_profile_pic);
            ParseFile imageFile = user.getPhoto();
            if (imageFile != null) {
                Picasso.with(this)
                        .load(imageFile.getUrl())
                        .into(photoImageView);
            }

            RelativeLayout layout = (RelativeLayout) findViewById(R.id.user_profile_layout);
            layout.setVisibility(View.VISIBLE);
        }
    }

}
