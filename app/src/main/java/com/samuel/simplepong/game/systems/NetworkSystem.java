package com.samuel.simplepong.game.systems;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.games.Games;
import com.google.android.gms.games.multiplayer.Participant;
import com.samuel.simplepong.MainActivity;
import com.samuel.simplepong.framework.messaging.Callback0;

import java.util.ArrayList;

/**
 * Created by John on 2016/2/13.
 */
public class NetworkSystem extends Activity {
    /*
     * API INTEGRATION SECTION. This section contains the code that integrates
     * the game with the Google Play game services API.
     */
    final static String TAG = "ButtonClicker2000";

    // Request codes for the UIs that we show with startActivityForResult:
    final static int RC_SELECT_PLAYERS = 10000;
    final static int RC_INVITATION_INBOX = 10001;
    final static int RC_WAITING_ROOM = 10002;

    // Request code used to invoke sign in user interactions.
    private static final int RC_SIGN_IN = 9001;

    // Room ID where the currently active game is taking place; null if we're
    // not playing.
    String mRoomId = null;

    // Are we playing in multiplayer mode?
    boolean mMultiplayer = false;

    // The participants in the currently active game
    ArrayList<Participant> mParticipants = null;

    // My participant ID in the currently active game
    String mMyId = null;

    // If non-null, this is the id of the invitation we received via the
    // invitation listener
    String mIncomingInvitationId = null;

    // Message buffer for sending messages
    byte[] mMsgBuf = new byte[2];

    // Are we currently resolving a connection failure?
    private boolean mResolvingConnectionFailure = false;

    // Has the user clicked the sign-in button?
    private boolean mSignInClicked = false;

    // Set to true to automatically start the sign in flow when the Activity starts.
    // Set to false to require the user to click the button in order to sign in.
    private boolean mAutoStartSignInFlow = true;

    private Callback0 signOut;

    public void initialize() {
        signOut = new Callback0() {
            public void callback() {
                Log.d(TAG, "Sign-out button clicked");
                mSignInClicked = false;
                Games.signOut(MainActivity.mGoogleApiClient);
                MainActivity.mGoogleApiClient.disconnect();
            }
        };
    }
}
