package com.fdxUser.app.Utills;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.content.PermissionChecker;

/**
 * Created by Pallab on 17-Aug-16.
 */
public class Permission {
    public static boolean selfPermissionGranted(Context mContext, String permission) {
        // For Android < Android M, self permissions are always granted.
        boolean result = true;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // targetSdkVersion >= Android M, we can
                // use Context#checkSelfPermission
                result = mContext.checkSelfPermission(permission)
                        == PackageManager.PERMISSION_GRANTED;
            } else {
                // targetSdkVersion < Android M, we have to use PermissionChecker
                result = PermissionChecker.checkSelfPermission(mContext, permission)
                        == PermissionChecker.PERMISSION_GRANTED;
            }
        }

        return result;
    }
}
