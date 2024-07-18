package com.example.wrappify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.datastore.preferences.core.MutablePreferences;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.core.PreferencesKeys;
import androidx.datastore.preferences.rxjava3.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava3.RxDataStore;
import androidx.datastore.rxjava3.RxDataStoreBuilder;

import android.os.Bundle;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import io.reactivex.rxjava3.core.Single;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    public static final String CLIENT_ID = "02d90a7fd04a4b65bb465bfcb33fb513";
    public static final String REDIRECT_URI = "wrappify://auth";

    public static final int AUTH_TOKEN_REQUEST_CODE = 0;
    public static final int AUTH_CODE_REQUEST_CODE = 1;
    public RxDataStore<Preferences> loginInfo;

    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    public static String mAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginInfo = new RxPreferenceDataStoreBuilder(this.getApplicationContext(), "loginInfo").build();
        //profileTextView = (TextView) findViewById(R.id.response_text_view);

        // Initialize the buttons
        Button tokenBtn = (Button) findViewById(R.id.token_btn);

        // Set the click listeners for the buttons
        tokenBtn.setOnClickListener((v) -> {
            getToken();
        });

    }

    /**
     * Get token from Spotify
     * This method will open the Spotify login activity and get the token
     * What is token?
     * https://developer.spotify.com/documentation/general/guides/authorization-guide/
     */
    public void getToken() {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.TOKEN);
        AuthorizationClient.openLoginActivity(LoginActivity.this, AUTH_TOKEN_REQUEST_CODE, request);
    }

    /**
     * Get code from Spotify
     * This method will open the Spotify login activity and get the code
     * What is code?
     * https://developer.spotify.com/documentation/general/guides/authorization-guide/
     */
    public void getCode() {
        final AuthorizationRequest request = getAuthenticationRequest(AuthorizationResponse.Type.CODE);
        AuthorizationClient.openLoginActivity(LoginActivity.this, AUTH_CODE_REQUEST_CODE, request);
    }


    /**
     * When the app leaves this activity to momentarily get a token/code, this function
     * fetches the result of that external activity to get the response from Spotify
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        final AuthorizationResponse response = AuthorizationClient.getResponse(resultCode, data);

        // Check which request code is present (if any)
        if (AUTH_TOKEN_REQUEST_CODE == requestCode) {
            mAccessToken = response.getAccessToken();
            Single<Preferences> updateResult = loginInfo.updateDataAsync(prefsIn -> {
                MutablePreferences mutablePreferences = prefsIn.toMutablePreferences();
                mutablePreferences.set(PreferencesKeys.stringKey("accessToken"), mAccessToken);
                return Single.just(mutablePreferences);
            });
            Intent myIntent = new Intent(this.getApplicationContext(), MainActivity.class);
            MainActivity.currentWrapped = null;
            MainActivity.pastWrapped = new ArrayList<>();
            MainActivity.topSongs = new ArrayList<>();
            MainActivity.topArtists = new ArrayList<>();
            this.startActivity(myIntent);
        }
    }

    /**
     * Creates a UI thread to update a TextView in the background
     * Reduces UI latency and makes the system perform more consistently
     *
     * @param text the text to set
     * @param textView TextView object to update
     */
    private void setTextAsync(final String text, TextView textView) {
        runOnUiThread(() -> textView.setText(text));
    }

    /**
     * Get authentication request
     *
     * @param type the type of the request
     * @return the authentication request
     */
    private AuthorizationRequest getAuthenticationRequest(AuthorizationResponse.Type type) {
        if (mAccessToken == null) {
            return new AuthorizationRequest.Builder(CLIENT_ID, type, getRedirectUri().toString())
                    .setShowDialog(true)
                    .setScopes(new String[] { "user-top-read user-read-recently-played" }) // <--- Change the scope of your requested token here
                    .build();
        } else {
            return new AuthorizationRequest.Builder(CLIENT_ID, type, getRedirectUri().toString())
                    .setShowDialog(false)
                    .setScopes(new String[] { "user-top-read user-read-recently-played" }) // <--- Change the scope of your requested token here
                    .build();
        }
    }

    /**
     * Gets the redirect Uri for Spotify
     *
     * @return redirect Uri object
     */
    private Uri getRedirectUri() {
        return Uri.parse(REDIRECT_URI);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public static void setAccessToken(String token) {
        mAccessToken = token;
    }
}