package adri.suys.un_mutescan.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;
import java.util.Objects;

import adri.suys.un_mutescan.R;
import adri.suys.un_mutescan.activities.Activity;
import adri.suys.un_mutescan.activities.TicketInfosActivity;
import adri.suys.un_mutescan.utils.UnMuteDataHolder;

public class BarcodeScannerFragment extends Fragment {

    private SurfaceView surfaceView;
    private TextView barcodeText;
    private BarcodeDetector detector;
    private CameraSource cameraSrc;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    private String intentData = "";

    public BarcodeScannerFragment(){
        // required
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_barcode_scanner, container, false);
        setViewElements(view);
        return view;
    }

    @Override
    public void onPause(){
        super.onPause();
        cameraSrc.release();
    }

    @Override
    public void onResume(){
        super.onResume();
        initializeDetectorAndCameraSource();
    }

    /////////////////////
    // private methods //
    /////////////////////

    private void setViewElements(View view) {
        surfaceView = view.findViewById(R.id.surfaceView);
        barcodeText = view.findViewById(R.id.txtBarcodeValue);
    }

    private void initializeDetectorAndCameraSource(){
        detector = new BarcodeDetector.Builder(getContext()).setBarcodeFormats(Barcode.ALL_FORMATS).build();
        cameraSrc = new CameraSource.Builder(Objects.requireNonNull(getContext()), detector).setRequestedPreviewSize(1920, 1080).setAutoFocusEnabled(true).build();
        addCallbackToSurfaceView();
        setProcessor();
    }

    private void addCallbackToSurfaceView(){
        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                try {
                    if (ActivityCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSrc.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }
                } catch (IOException ex){
                    ex.printStackTrace();
                    ((Activity)(getActivity())).showToast(getResources().getString(R.string.sth_wrong));
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                // do nothing
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                cameraSrc.stop();
            }
        });
    }

    private void setProcessor(){
        detector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0){
                    barcodeText.post(new Runnable() {
                        @Override
                        public void run() {
                            intentData = barcodes.valueAt(0).displayValue;
                            barcodeText.setText(intentData);
                            Intent intent = new Intent(getActivity(), TicketInfosActivity.class);
                            intent.putExtra("barcode", intentData);
                            intent.putExtra("alert", true);
                            startActivity(intent);
                        }
                    });
                }
            }
        });
    }

}
