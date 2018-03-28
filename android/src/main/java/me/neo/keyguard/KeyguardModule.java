package me.neo.keyguard;

import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.facebook.react.ReactNativeHost;
import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.bridge.BaseActivityEventListener;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import static android.content.Context.KEYGUARD_SERVICE;

/**
 * Created by Nishanth Shankar on 9/24/15.
 */
public class KeyguardModule extends ReactContextBaseJavaModule {
    private ReactApplicationContext mContext;
    private static final int KEYGUARD_REQUEST = 467081;
    private static final String E_ACTIVITY_DOES_NOT_EXIST = "E_ACTIVITY_DOES_NOT_EXIST";
    private static final String E_FAILED_TO_SHOW_KEYGUARD = "E_FAILED_TO_SHOW_KEYGUARD";
    private static final String E_KEYGUARD_CANCELLED = "E_KEYGUARD_CANCELLED";
    private Promise mKeyguardPromise;

    private final ActivityEventListener mActivityEventListener = new BaseActivityEventListener() {
        @Override
        public void onActivityResult(Activity activity, int requestCode, int resultCode, Intent intent) {
            if (requestCode != KEYGUARD_REQUEST)
                return;
            if (mKeyguardPromise == null)
                return;
            if (resultCode == Activity.RESULT_CANCELED) {
                mKeyguardPromise.reject(E_KEYGUARD_CANCELLED, "Keyguard cancelled");
            } else if (resultCode == Activity.RESULT_OK) {
                mKeyguardPromise.resolve("OPENED");
            }
        };
    };

    public KeyguardModule(ReactApplicationContext reactContext) {
        super(reactContext);
        mContext = reactContext;
        reactContext.addActivityEventListener(mActivityEventListener);
    }

    @Override
    public String getName() {
        return "Keyguard";
    }

    @ReactMethod
    public void unlock(String title, String description, final Promise promise) {
        Activity currentActivity = getCurrentActivity();

        if (currentActivity == null) {
            promise.reject(E_ACTIVITY_DOES_NOT_EXIST, "Activity doesn't exist");
            return;
        }

        mKeyguardPromise = promise;
        try {
            KeyguardManager km = (KeyguardManager) mContext.getSystemService(KEYGUARD_SERVICE);

            if (Build.VERSION.SDK_INT < 21) {
                promise.resolve("OPENED");
                return;
            }
            if(!km.isKeyguardSecure()){
                promise.resolve("OPENED_WITHOUT_SECURITY");
                return;
            }
            if(Build.VERSION.SDK_INT >= 21) {
                Intent i = km.createConfirmDeviceCredentialIntent(title, description);
                currentActivity.startActivityForResult(i, KEYGUARD_REQUEST);
            }
        } catch (Exception e) {
            mKeyguardPromise.reject(E_FAILED_TO_SHOW_KEYGUARD, e);
            mKeyguardPromise = null;
        }
    }
}
