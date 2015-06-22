package com.iconasystems.gula;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;


/**
 * A placeholder fragment containing a simple view.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    protected static final int STATE_DEFAULT = 0;
    protected static final int STATE_SIGN_IN = 1;
    protected static final int STATE_IN_PROGRESS = 2;
    protected static final int RC_SIGN_IN = 0;
    protected static final int DIALOG_PLAY_SERVICES_ERROR = 0;
    protected static final String SAVED_PROGRESS = "sign_in_progress";
    private static final String TAG = "com.iconasystems.gula";
    /**
     * GoogleApiClient wraps our service connection to Google Play Services and provides access
     * to the user's sign in state as well as the Google's APIs.
     */
    protected GoogleApiClient mGoogleApiClient;
    /**
     * We use mSignInProgress to track whether user has clicked sign in. mSignInProgress can be
     * one of three values:
     * <p/>
     * STATE_DEFAULT: The default state of the application before the user has clicked 'sign in',
     * or after they have clicked 'sign out'.  In this state we will not attempt to resolve sign
     * in errors so we will display our Activity in a signed out state.
     * <p/>
     * STATE_SIGN_IN: This state indicates that the user has clicked 'sign in', so resolve
     * successive errors preventing sign in until the user has successfully authorized an account
     * for our app.
     * <p/>
     * STATE_IN_PROGRESS: This state indicates that we have started an intent to resolve an
     * error, and so we should not start further intents until the current intent completes.
     */
    protected int mSignInProgress;
    /**
     * Used to store the PendingIntent most recently returned by Google Play Services until the
     * user clicks 'sign in'.
     */
    protected PendingIntent mSignInIntent;
    /**
     * Used to store the error code most recently returned by Google Play Services until the user
     * clicks 'sign in'.
     */
    protected int mSignInError;
    private SignInButton mPlusButton;
    private ConnectionResult result;
    private Button mRevoke;


    public RegisterFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_register, container, false);
        mPlusButton = (SignInButton) rootView.findViewById(R.id.plus_signin);
        mRevoke = (Button) rootView.findViewById(R.id.revoke);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
            mSignInProgress = savedInstanceState.getInt(SAVED_PROGRESS, STATE_DEFAULT);
        }

        rebuildGoogleApiClient();

        mPlusButton.setOnClickListener(this);
        mRevoke.setOnClickListener(this);
    }

    /**
     * Construct a client.
     *
     * @return un-connected client.
     */
    protected synchronized void rebuildGoogleApiClient() {
        // When we build the GoogleApiClient we specify where connected and connection failed
        // callbacks should be returned, which Google APIs our app uses and which OAuth 2.0
        // scopes our app requests.
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API)
                .addScope(new Scope(Scopes.PROFILE))
                        // TODO(developer): Specify any additional API Scopes or APIs you need here.
                        // The GoogleApiClient will ensure these APIs are available, and the Scopes
                        // are approved before invoking the onConnected callbacks.
                .build();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        this.result = result;

        if (result.hasResolution()) {
            try {
                result.startResolutionForResult(getActivity(), RC_SIGN_IN);
                Log.d(TAG, "Error = " + result.getErrorCode());
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }

        // In this sample we consider the user signed out when they do not have a connection to
        // Google Play services.
        //onSignedOut();
    }

    /**
     * Starts an appropriate intent or dialog for user interaction to resolve the current error
     * preventing the user from being signed in.  This could be a dialog allowing the user to
     * select an account, an activity allowing the user to consent to the permissions being
     * requested by your app, a setting to enable device networking, etc.
     */
    protected void resolveSignInError(ConnectionResult result) {
        if (mSignInIntent != null) {
            // We have an intent which will allow our user to sign in or resolve an error. For
            // example if the user needs to select an account to sign in with,
            // or if they need to  consent to the permissions your app is requesting.

            try {
                // Send the pending intent that we stored on the most recent OnConnectionFailed
                // callback.  This will allow the user to resolve the error currently preventing
                // our connection to Google Play Services.
                mSignInProgress = STATE_IN_PROGRESS;
                result.startResolutionForResult(getActivity(), RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                Log.i(TAG, "Sign in intent could not be sent: "
                        + e.getLocalizedMessage());
                // The intent was canceled before it was sent.  Attempt to
                // connect to get an updated ConnectionResult.
                mSignInProgress = STATE_SIGN_IN;
                mGoogleApiClient.connect();
            }
        } else {
            // Google Play Services wasn't able to provide an intent for some error types,
            // so we show the default Google Play services error dialog which may still start an
            // intent on our behalf if the user can resolve the issue.
            // showDialog(DIALOG_PLAY_SERVICES_ERROR);
        }
    }


    @Override
    public void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    public void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_PROGRESS, mSignInProgress);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent data) {
        switch (requestCode) {
            case RC_SIGN_IN:
                if (resultCode == Activity.RESULT_OK) {
                    // If the error resolution was successful we should continue processing errors.
                    mSignInProgress = STATE_SIGN_IN;
                } else {
                    // If the error resolution was not successful or the user canceled,
                    // we should stop processing errors.
                    mSignInProgress = STATE_DEFAULT;
                }

                if (!mGoogleApiClient.isConnecting()) {
                    // If Google Play services resolved the issue with a dialog then onStart is
                    // not called so we need to re-attempt connection here.
                    mGoogleApiClient.connect();
                }
                break;
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        // Reaching onConnected means we consider the user signed in and all APIs previously
        // specified are available.
        Log.i("com.iconasystems.gula", "onConnected");

        // IMPORTANT NOTE: If you are storing any user data locally or even in a remote
        // application DO NOT associate it to the accountName (which is also an email address).
        // Associate the user data to the Google Account ID. Under some circumstances it is possible
        // for a Google Account to have the primary email address change.

        Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

        // TODO(developer): Check the account ID against any previous login locally.
        // TODO(developer): Delete the local data if the account ID differs.
        // TODO(developer): Construct local storage keyed on the account ID.

        String displayName = currentPerson.getDisplayName();
        Log.i("Display Name", displayName);

        // Indicate that the sign in process is complete.
        mSignInProgress = STATE_DEFAULT;

        mRevoke.setVisibility(View.VISIBLE);
    }

    @Override
    public void onConnectionSuspended(int i) {
        // The connection to Google Play services was lost for some reason. We call connect() to
        // attempt to re-establish the connection or get a ConnectionResult that we can attempt
        // to resolve.
        mGoogleApiClient.connect();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.plus_signin:
                resolveSignInError(this.result);
                break;
            case R.id.revoke:
                Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient);
                Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                rebuildGoogleApiClient();
                mGoogleApiClient.connect();
                break;
        }
    }

    public Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_PLAY_SERVICES_ERROR:
                if (GooglePlayServicesUtil.isUserRecoverableError(mSignInError)) {
                    return GooglePlayServicesUtil.getErrorDialog(
                            mSignInError,
                            getActivity(),
                            RC_SIGN_IN,
                            new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    Log.e(TAG, "Google Play services resolution"
                                            + " cancelled");
                                    mSignInProgress = STATE_DEFAULT;
                                    //mStatus.setText(R.string.status_signed_out);
                                }
                            });
                } else {
                    return new AlertDialog.Builder(getActivity())
                            .setMessage("Error")
                            .setPositiveButton("Close",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Log.e(TAG, "Google Play services error could not be "
                                                    + "resolved: " + mSignInError);
                                            mSignInProgress = STATE_DEFAULT;
                                            //mStatus.setText(R.string.status_signed_out);
                                        }
                                    }).create();
                }
            default:
                return onCreateDialog(id);
        }
    }
}
