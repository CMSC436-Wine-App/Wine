package wine.cmsc436.wine;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;

import com.facebook.Session;
import com.parse.ParseFacebookUtils;

import java.util.Arrays;

public class ShareDialogFragment extends DialogFragment {

    public static final int FB_SESSION_RESULT = 101;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Share review on Facebook?")
                .setPositiveButton("Share", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (!ParseFacebookUtils.getSession().isPermissionGranted("publish_actions")) {
                            String[] permissions = {"publish_actions"};
                            Session.NewPermissionsRequest permissionsRequest = new Session.NewPermissionsRequest(getActivity(), Arrays.asList(permissions));
                            permissionsRequest.setRequestCode(FB_SESSION_RESULT);
                            ParseFacebookUtils.getSession().requestNewPublishPermissions(permissionsRequest);
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        ShareDialogFragment.this.getDialog().cancel();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
