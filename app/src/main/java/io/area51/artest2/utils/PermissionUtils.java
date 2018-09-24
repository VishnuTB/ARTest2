package io.area51.artest2.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

public class PermissionUtils {

    private static final int CAMERA_PERMISSION_CODE = 0;
    private static final int STORAGE_PERMISSION_CODE = 1;
    private static final String CAMERA_PERMISSION = Manifest.permission.CAMERA;

    public static boolean hasCameraPermission(Activity activity) {
        return ContextCompat.checkSelfPermission(activity, CAMERA_PERMISSION)
                == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestCameraPermission(Activity activity) {
        ActivityCompat.requestPermissions(
                activity, new String[]{CAMERA_PERMISSION}, CAMERA_PERMISSION_CODE);
    }

    public static boolean shouldShowRequestPermissionRationale(Activity activity) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, CAMERA_PERMISSION);
    }

    public static void launchPermissionSettings(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        activity.startActivity(intent);
    }

    public static boolean checkStoragePermission(Context context) {
        return ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public static void requestStoragePermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
    }

}