
package com.callrec.custom;

import android.Manifest;

/**
 * Created by alcord on 13/6/2017.
 */

public class ApplicationConstants {




	public static final String[] RECORD_PERMISSIONS = new String[] {
			            Manifest.permission.WRITE_EXTERNAL_STORAGE,
			            Manifest.permission.RECORD_AUDIO,
			            Manifest.permission.READ_EXTERNAL_STORAGE,
			            Manifest.permission.PROCESS_OUTGOING_CALLS,
			Manifest.permission.READ_CONTACTS,
			Manifest.permission.PROCESS_OUTGOING_CALLS,

			    };

	public static final int MULTIPLE_PERMISSIONS = 199;
}
