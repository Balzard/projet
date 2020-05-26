package adri.suys.un_mutescan.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

import adri.suys.un_mutescan.R;

public class BarcodeManualActivity extends Activity {

    private EditText input;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode_manual);

        input = findViewById(R.id.input_barcode_value);
        AppCompatButton validationBtn = findViewById(R.id.btn_validation_request);
        validationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askForTicketValidation();
            }
        });
        configActionBar(getString(R.string.manual_barcode));
    }
    private void askForTicketValidation(){
        if (input.getText().toString().equals("")){
            //((Activity)(BarcodeManualActivity.this)).showToast(getResources().getString(R.string.no_barcode_input));
            Toast.makeText(BarcodeManualActivity.this,getString(R.string.no_barcode_detected),Toast.LENGTH_SHORT).show();
        } else {
            String barcodeValue = input.getText().toString();
            Intent intent = new Intent(BarcodeManualActivity.this, TicketInfosActivity.class);
            intent.putExtra("barcode", barcodeValue);
            intent.putExtra("alert", true);
            startActivity(intent);
        }
    }

}
