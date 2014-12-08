package wine.cmsc436.wine;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.facebook.Session;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;
import com.parse.ParseFacebookUtils;

import java.util.Arrays;

import parse.subclasses.Review;
import parse.subclasses.User;

/**
 * Created by Ethan on 12/6/2014.
 */
public class FacebookPost {
    Activity activity;
    UiLifecycleHelper uiHelper;

    public FacebookPost(Activity activity, UiLifecycleHelper uiHelper) {
        this.activity = activity;
        this.uiHelper = uiHelper;
    }

    public static Intent createDataFromReview(Review review) {
        Intent data = new Intent();
        String name = User.getCurrentUser().getName()+" shared a WineBarApp review!";
        String caption = "Wine: "+review.getWine().getName()+", Overall Rating: "+review.getRating();
        String description = review.getComment();
        data.putExtra("name", name);
        data.putExtra("caption", caption);
        data.putExtra("description", description);
        data.putExtra("link", review.getWine().getPhoto().getUrl());
        data.putExtra("picture", review.getWine().getPhoto().getUrl());
        return data;
    }

    public void showFacebookShareDialog(Intent data) {
        String name = data.getStringExtra("name");
        String caption = data.getStringExtra("caption");
        String description = data.getStringExtra("description");
        String link = data.getStringExtra("link");
        String picture = data.getStringExtra("picture");
        if (FacebookDialog.canPresentShareDialog(activity.getApplicationContext(),
                FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
            FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(activity)
                    .setName(name)
                    .setCaption(caption)
                    .setDescription(description)
                    .setLink(link)
                    .setPicture(picture)
                    .build();
            uiHelper.trackPendingDialogCall(shareDialog.present());
        } else {
            Bundle params = new Bundle();
            params.putString("name", name);
            params.putString("caption", caption);
            params.putString("description", description);
            params.putString("link", link);
            params.putString("picture", picture);
            WebDialog feedDialog = (
                    new WebDialog.FeedDialogBuilder(activity,
                            Session.getActiveSession(),
                            params))
                    .build();
            feedDialog.show();
        }
    }

}
