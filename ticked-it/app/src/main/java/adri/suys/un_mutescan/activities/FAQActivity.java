package adri.suys.un_mutescan.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

import adri.suys.un_mutescan.R;
import adri.suys.un_mutescan.utils.LocaleHelper;

public class FAQActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faq);
        configActionBar(getString(R.string.faq));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        return false;
    }

}
