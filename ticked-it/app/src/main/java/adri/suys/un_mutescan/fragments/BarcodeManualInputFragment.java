package adri.suys.un_mutescan.fragments;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.Objects;

import adri.suys.un_mutescan.R;
import adri.suys.un_mutescan.activities.Activity;
import adri.suys.un_mutescan.activities.TicketInfosActivity;

public class BarcodeManualInputFragment extends Fragment {

    private EditText input;

    public BarcodeManualInputFragment(){
        // required
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_barcode_manually_input, container, false);
        input = view.findViewById(R.id.input_barcode_value);
        AppCompatButton validationBtn = view.findViewById(R.id.btn_validation_request);
        validationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askForTicketValidation();
            }
        });
        return view;
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    private void askForTicketValidation(){
        if (input.getText().toString().equals("")){
            ((Activity)(Objects.requireNonNull(getActivity()))).showToast(getResources().getString(R.string.no_barcode_input));
        } else {
            String barcodeValue = input.getText().toString();
            Intent intent = new Intent(getActivity(), TicketInfosActivity.class);
            intent.putExtra("barcode", barcodeValue);
            intent.putExtra("alert", true);
            startActivity(intent);
        }
    }

}
