package com.callrec.Activity;

import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.callrec.R;
import com.callrec.custom.ApplicationConstants;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkPermissionsAndShowPopup();
        }else{
            Toast.makeText(getApplicationContext(),"please check",Toast.LENGTH_SHORT).show();
        }

        }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case ApplicationConstants.MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    boolean permissionGranted = true;
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            permissionGranted = false;
                           // AlertUtils.showInfoDialog(this, "", getString(R.string.permissions_request));
                            break;
                        }
                    }
                    if (permissionGranted) {
                        //  onResume();
                    } else {
                        //AlertUtils.showInfoDialog(this, "", getString(R.string.permissions_request));
                    }
                }
            }
        }

    }

    private void checkPermissionsAndShowPopup() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : ApplicationConstants.RECORD_PERMISSIONS) {
            result = ContextCompat.checkSelfPermission(this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), ApplicationConstants.MULTIPLE_PERMISSIONS);
            }
        } else {
            // onResume();
        }



    }

}
