
package info.guardianproject.panic;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

public class PanicReceiver {

    public static final String PREF_TRIGGER_PACKAGE_NAME = "panicReceiverTriggerPackageName";

    /**
     * Checks whether the provided {@link Activity} was started with the action
     * {@link Panic.ACTION_DISCONNECT}, and if so, processes that {@link Intent}
     * , removing the sending app as the panic trigger if it is currently
     * configured to be so.
     *
     * @param activity the {@code Activity} to check for the {@code Intent}
     * @return whether an {@code ACTION_DISCONNECT Intent} was received
     */
    public static boolean checkForDisconnectIntent(Activity activity) {
        boolean result = false;
        Intent intent = activity.getIntent();
        if (TextUtils.equals(intent.getAction(), Panic.ACTION_DISCONNECT)) {
            result = true;
            if (TextUtils.equals(PanicUtils.getCallingPackageName(activity),
                    getTriggerPackageName(activity)))
                setTriggerPackageName(activity, null);
        }
        return result;
    }

    /**
     * Get the {@code packageName} of the currently configured panic trigger
     * app, or {@code null} if none.
     *
     * @param context
     * @return the {@code packageName} or null
     */
    public static String getTriggerPackageName(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(PREF_TRIGGER_PACKAGE_NAME, null);
    }

    /**
     * Set the currently configured panic trigger app using the {@link Activity}
     * that received a {@link Panic#ACTION_CONNECT} {@link Intent}. If that
     * {@code Intent} was not set with either
     * {@link Activity#startActivityForResult(Intent, int)} or
     * {@link Intent#setPackage(String)}, then this will result in no panic
     * trigger app being active.
     *
     * @param activity the {@link Activity} that received an
     *            {@link Panic.ACTION_CONNECT} {@link Intent}
     */
    public static void setTriggerPackageName(Activity activity) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(activity);
        String packageName = PanicUtils.getCallingPackageName(activity);
        if (TextUtils.isEmpty(packageName)) {
            prefs.edit().remove(PREF_TRIGGER_PACKAGE_NAME).apply();
        } else {
            prefs.edit().putString(PREF_TRIGGER_PACKAGE_NAME, packageName).apply();
        }
    }

    /**
     * Set the {@code packageName} as the currently configured panic trigger
     * app. Set to {@code null} to have no panic trigger app active.
     *
     * @param context
     * @param packageName the app to set as the panic trigger
     */
    public static void setTriggerPackageName(Context context, String packageName) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        if (TextUtils.isEmpty(packageName))
            prefs.edit().remove(PREF_TRIGGER_PACKAGE_NAME).apply();
        else
            prefs.edit().putString(PREF_TRIGGER_PACKAGE_NAME, packageName).apply();
    }
}
