package adri.suys.un_mutescan.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.widget.AppCompatButton;

import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Objects;

import adri.suys.un_mutescan.R;
import adri.suys.un_mutescan.apirest.RestService;
import adri.suys.un_mutescan.presenter.PayPresenter;
import adri.suys.un_mutescan.viewinterfaces.PayViewInterface;

import static android.view.View.GONE;

public class PayActivity extends Activity implements PayViewInterface {

    private TextView amountToPay;
    private EditText emailInput, firstnameInput, lastnameInput;
    private PayPresenter presenter;
    private ProgressBar progressBar;
    private RestService restCommunication;
    private static final int EMAIL_NOT_OK = 0;
    private static final int NO_EMAIL = 1;
    private static final int VALIDATED = 2;
    private static final int NONE = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay);
        configActionBar(getString(R.string.my_event));
        hideMenuItem(true);
        presenter = new PayPresenter(this);
        restCommunication = new RestService(this);
        restCommunication.setPayPresenter(presenter);
        amountToPay = findViewById(R.id.amount_to_pay);
        progressBar = findViewById(R.id.pay_progressBar);
        emailInput = findViewById(R.id.buyer_email_input);
        firstnameInput = findViewById(R.id.buyer_firstname_input);
        lastnameInput = findViewById(R.id.buyer_lastname_input);
        hideProgressBar();
        setButtons();
        initPrice();
    }

    @Override
    protected void onResume(){
        super.onResume();
        initPrice();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id==android.R.id.home){
            this.finish();}

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        startActivity(new Intent(this, OneEventActivity.class));
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
    public void showErrorMessage(String message) {
        showToast(message);
    }

    @Override
    public void addTicket(boolean paidInCash, String email, String firstname, String lastname) {
        restCommunication.addTicket(paidInCash, email, firstname, lastname);
    }

    @Override
    public void addAnotherTicket() {
        restCommunication.addAnotherTicket();
    }

    @Override
    public void showTicketAddedToast() {
        showToast(getResources().getString(R.string.tickets_well_added));
    }

    public void hideProgressBar(){
        progressBar.setVisibility(GONE);
    }

    /////////////////////
    // private methods //
    /////////////////////

    private void initPrice(){
        presenter.setTotalTicketSold(getIntent().getIntExtra("nbTicketsSold", 0));
        double price = getIntent().getDoubleExtra("price", 0);
        amountToPay.setText(getResources().getString(R.string.amount_to_pay, price));
    }

    private void setButtons(){
        AppCompatButton cashBtn = findViewById(R.id.pay_cash_btn);
        cashBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailInput.getText().toString();
                String firstname = firstnameInput.getText().toString();
                String lastname = lastnameInput.getText().toString();
                switch (validateUserInput()){
                    case VALIDATED:
                        progressBar.setVisibility(View.VISIBLE);
                        presenter.addTickets(true, email, firstname, lastname);
                        break;
                    case EMAIL_NOT_OK:
                        showToast(getResources().getString(R.string.email_no_valid));
                        break;
                    case NO_EMAIL:
                        showToast(getResources().getString(R.string.name_but_no_email));
                        break;
                    default:
                        progressBar.setVisibility(View.VISIBLE);
                        presenter.addTickets(true, "", "", "");
                        break;
                }

            }
        });
    }

    private int validateUserInput(){
        String email = emailInput.getText().toString();
        String firstname = firstnameInput.getText().toString();
        String lastname = lastnameInput.getText().toString();
        if (email.equals("") && firstname.equals("") && lastname.equals("")){
            return NONE;
        }
        if (!email.equals("") && !firstname.equals("") && !lastname.equals("")){
            if (presenter.validateEmail(email)){
                return VALIDATED;
            } else {
                return EMAIL_NOT_OK;
            }
        }
        return NO_EMAIL;
    }
}
