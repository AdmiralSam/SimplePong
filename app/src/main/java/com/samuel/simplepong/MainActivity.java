package com.samuel.simplepong;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;
import com.samuel.simplepong.framework.messaging.Callback0;

public class MainActivity extends FragmentActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    public static final int REQUEST_RESOLUTION = 1001;
    private GLSurfaceView glSurfaceView;
    private GoogleApiClient googleApiClient;
    private boolean resolvingError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resolvingError = false;
        glSurfaceView = new MainGLSurfaceView(this);
        setContentView(glSurfaceView);
        googleApiClient = new GoogleApiClient.Builder(this).addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN).addApi(Games.API).addScope(Games.SCOPE_GAMES).addConnectionCallbacks(this).addOnConnectionFailedListener(this).build();
        MainGLRenderer.messageCenter.addListener("Sign In", new Callback0() {
            @Override
            public void callback() {
                signIn();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        signIn();
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.e("S", "S");
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (!resolvingError) {
            if (connectionResult.hasResolution()) {
                try {
                    resolvingError = true;
                    connectionResult.startResolutionForResult(this, REQUEST_RESOLUTION);
                } catch (IntentSender.SendIntentException e) {
                    googleApiClient.connect();
                }
            } else {
                resolvingError = true;
                showErrorDialog(connectionResult.getErrorCode());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RESOLUTION) {
            resolvingError = false;
            if (resultCode == RESULT_OK) {
                signIn();
            }
        }
    }

    private void signIn() {
        if (!resolvingError && !googleApiClient.isConnecting() && !googleApiClient.isConnected()) {
            googleApiClient.connect();
        }
    }
    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt("Dialog Error", errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "errordialog");
    }

    public void onDialogDismissed() {
        resolvingError = false;
    }

    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() { }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt("Dialog Error");
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLUTION);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((MainActivity) getActivity()).onDialogDismissed();
        }
    }
}
