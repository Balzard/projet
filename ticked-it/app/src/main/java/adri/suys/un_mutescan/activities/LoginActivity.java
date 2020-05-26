package adri.suys.un_mutescan.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.AppCompatButton;
import androidx.preference.PreferenceManager;

import android.text.Editable;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialRequest;
import com.google.android.gms.auth.api.credentials.CredentialRequestResult;
import com.google.android.gms.auth.api.credentials.CredentialsOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import java.util.Objects;

import adri.suys.un_mutescan.R;
import adri.suys.un_mutescan.apirest.RestService;
import adri.suys.un_mutescan.fragments.PreferenceFragment;
import adri.suys.un_mutescan.presenter.LoginPresenter;
import adri.suys.un_mutescan.utils.LocaleHelper;
import adri.suys.un_mutescan.viewinterfaces.LoginViewInterface;


public class LoginActivity extends Activity implements LoginViewInterface, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private EditText usernameInput;
    private EditText pwdInput;
    private ProgressBar progressBar;
    private LoginPresenter presenter;
    private Button showPassword;
    private boolean isShown = false;
    private final static String KEY_USERNAME = "username";
    private static final int RC_SAVE = 999;
    private RestService restCommunication;
    private GoogleApiClient credentialsClient;
    private Credential credential;
    private AppCompatButton logBtn;
    private String initialLocale;
    private boolean isDarkTheme;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        hideMenuItem(true);
        configActionBar(getString(R.string.home));
        restCommunication = new RestService(this);
        initViewElements();
        if (savedInstanceState != null) {
            pwdInput.setText(savedInstanceState.getString(KEY_USERNAME));
        }
        presenter = new LoginPresenter(this);
        restCommunication.setUserPresenter(presenter);
        CredentialsOptions credentialsOptions = new CredentialsOptions.Builder().forceEnableSaveDialog().build();
        credentialsClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, 0, this)
                .addApi(Auth.CREDENTIALS_API, credentialsOptions)
                .build();
    }


    @Override
    public void onResume() {
        super.onResume();
        if (initialLocale != null && !initialLocale.equals(LocaleHelper.getPersistedLocale(this))) {
            recreate();}
    }

    @Override
    void configActionBar(String title){
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    public void onBackPressed() {
        // do nothing
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_USERNAME, pwdInput.getText().toString());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SAVE) {
            if (resultCode == RESULT_OK) {
                showToast(getResources().getString(R.string.credentials_saved));
            } else {

            }
            showToast(getResources().getString(R.string.user_logged, usernameInput.getText().toString()));
            changeScreen();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        System.out.println("onConnected");
        if (isInternetConnected()) {
            presenter.retrieveCredentials();
        } else {
            showInstructionPopUp();
            hideProgressBar();
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        System.out.println("onConnectionSuspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        System.out.println("onConnectionFailed");
    }

    @Override
    public void changeScreen(){
        Intent i = new Intent(this, EventListActivity.class);
        i.putExtra("refresh", false);
        startActivity(i);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showNoAccessToInternetToast() {
        showToast(getResources().getString(R.string.no_internet_back_up));
    }

    @Override
    public void showConnectionProblemToast() {
        showToast(getResources().getString(R.string.volley_error_no_connexion));
    }

    @Override
    public void showUnvalidUsernameToast() {
        showToast(getResources().getString(R.string.user_not_found));
    }

    @Override
    public void showCantUseAppToast() {
        showToast(getResources().getString(R.string.user_cant_access));
    }

    @Override
    public void showHelloToast(String username) {
        showToast(getResources().getString(R.string.user_logged, username));
    }

    @Override
    public void showBadCredentialsToast() {
        showToast(getResources().getString(R.string.user_wrong_credentials));
    }

    @Override
    public void showNoConnectionRetryToast() {
        showToast(getResources().getString(R.string.volley_error_no_connexion));
    }

    @Override
    public void showServerConnectionProblemToast() {
        showToast(getResources().getString(R.string.volley_error_server_error));
    }

    @Override
    public void showCredentialsSavedToast() {
        showToast(getResources().getString(R.string.credentials_saved));
    }

    @Override
    public void showSmartLockPopUp(String username) {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        String message = getResources().getString(R.string.right_account, username);
        dialogBuilder.setMessage(message).setTitle("Smart Lock");
        dialogBuilder.setCancelable(false)
                .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        showProgressBar();
                        logBtn.setEnabled(false);
                        presenter.logUserSmartlock(credential.getPassword(), credential.getId());
                    }
                })
                .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                        presenter.changeCreds();
                    }
                });
        AlertDialog alert = dialogBuilder.create();
        alert.setTitle("Smart Lock");
        alert.show();
    }

    @Override
    public void resolveResult(Status status, int rcSave, boolean isResolving) {
        if (isResolving) {
            System.out.println("resolveResult: already resolving.");
            return;
        }
        System.out.println("Resolving: " + status);
        if (status.hasResolution()) {
            System.out.println("STATUS: RESOLVING");
            try {
                status.startResolutionForResult(this, rcSave);
                isResolving = true;
            } catch (IntentSender.SendIntentException e) {
                System.out.println( "STATUS: Failed to send resolution." + e);
            }
        } else {
            System.out.println("STATUS: FAIL");
        }
    }

    @Override
    public void loginUser(String username) {
        restCommunication.loginUser(username);
    }

    @Override
    public void retrieveCredentials() {
        CredentialRequest request = new CredentialRequest.Builder().setPasswordLoginSupported(true).build();
        Auth.CredentialsApi.request(credentialsClient, request).setResultCallback(
                new ResultCallback<CredentialRequestResult>() {
                    @Override
                    public void onResult(CredentialRequestResult credentialRequestResult) {
                        Status status = credentialRequestResult.getStatus();
                        if (credentialRequestResult.getStatus().isSuccess()) {
                            onCredentialRetrieved(credentialRequestResult.getCredential());
                        } else if (status.getStatusCode() == CommonStatusCodes.SIGN_IN_REQUIRED) {
                            hideProgressBar();
                        } else {
                            hideProgressBar();
                            System.out.println("Unrecognized status code: " + status.getStatusCode());
                        }
                    }
                }
        );
    }

    @Override
    public void deleteCredential() {
        Auth.CredentialsApi.delete(credentialsClient,
                credential).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                if (status.isSuccess()) {
                    // yeah
                } else {
                    // oops
                }
            }
        });
    }

    @Override
    public void saveCredentials(String username, String password) {
        Credential credential = new Credential.Builder(username).setPassword(password).build();
        Auth.CredentialsApi.save(credentialsClient, credential).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                if (status.isSuccess()){
                    showCredentialsSavedToast();
                } else {
                    resolveResult(status, RC_SAVE, false);
                }
            }
        });
    }

    @Override
    public void enableButton() {
        logBtn.setEnabled(true);
    }

    public void loginViaUm(View v) {
        String username = usernameInput.getText().toString();
        if (!username.equals("")){
            showProgressBar();
            presenter.logUser(isInternetConnected(), username.toLowerCase(), pwdInput.getText().toString());
        } else {
            showToast(getResources().getString(R.string.user_no_login));
        }
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleHelper.onAttach(base));
    }

    public void goToUrl(View v){
        Uri uriUrl = Uri.parse("https://un-mute.be/inscription/");
        Intent LaunchBrowser = new Intent(Intent.ACTION_VIEW,uriUrl);
        startActivity(LaunchBrowser);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        int id = item.getItemId();
        if(id == R.id.disconnect){
            finish();
            System.exit(0);
        }
        return super.onOptionsItemSelected(item);
    }


    /////////////////////
    // private methods //
    /////////////////////

    private void initViewElements() {
        usernameInput = findViewById(R.id.input_username);
        pwdInput = findViewById(R.id.input_password);
        progressBar = findViewById(R.id.progressBar);
        showProgressBar();
        showPassword = findViewById(R.id.password_visibility);
        handleShowHidePwd();
        logBtn = findViewById(R.id.login_um_btn);
    }

    private void handleShowHidePwd(){
        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isShown) {
                    pwdInput.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isShown = true;
                    showPassword.setBackgroundResource(R.drawable.ic_visibility_off_green_24dp);
                } else {
                    pwdInput.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isShown = false;
                    showPassword.setBackgroundResource(R.drawable.ic_visibility_green_24dp);
                }
            }
        });
    }

    private void onCredentialRetrieved(Credential credential){
        presenter.setHasCredentialsStocked(true);
        this.credential = credential;
        hideProgressBar();
        showSmartLockPopUp(credential.getId());
    }

    private void showInstructionPopUp(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogNoInternetStyle);
        String title = getResources().getString(R.string.no_internet);
        builder.setMessage(R.string.instructions).setTitle(title);
        builder.setCancelable(false).setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }
}
