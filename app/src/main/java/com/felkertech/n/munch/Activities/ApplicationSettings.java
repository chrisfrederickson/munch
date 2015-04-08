package com.felkertech.n.munch.Activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;

import com.felkertech.n.munch.R;

import java.util.List;

/**
 * A {@link android.preference.PreferenceActivity} that presents a set of application settings. On
 * handset devices, settings are presented as a single list. On tablets,
 * settings are split by category, with category headers shown to the left of
 * the list of settings.
 * <p>
 * See <a href="http://developer.android.com/design/patterns/settings.html">
 * Android Design: Settings</a> for design guidelines and the <a
 * href="http://developer.android.com/guide/topics/ui/settings.html">Settings
 * API Guide</a> for more information on developing a Settings UI.
 */
public class ApplicationSettings extends ActionBarActivity {
    /**
     * Determines whether to always show the simplified settings UI, where
     * settings are presented in a single list. When false, settings are shown
     * as a master/detail two-pane view on tablets. When true, a single pane is
     * shown on tablets.
     */
    private static final boolean ALWAYS_SIMPLE_PREFS = true;
    public static final String TAG = "munch::ApplictnSettings";
    public Context mContext = this;
//    private static SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // workaround for https://code.google.com/p/android/issues/detail?id=78701
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                getFragmentManager().beginTransaction()
                        .replace(android.R.id.content, new SettingsFragment())
                        .commit();
            }
        });
    }
    public static class SettingsFragment extends PreferenceFragment {
        @Override public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
//            addPreferencesFromResource(R.xml.pref_general);
        }
        @Override
        public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);

            setupSimplePreferencesScreen();
        }
        /**
         * Shows the simplified settings UI if the device configuration if the
         * device configuration dictates that a simplified, single-pane UI should be
         * shown.
         */
        private void setupSimplePreferencesScreen() {
            if (!isSimplePreferences(getActivity())) {
                return;
            }

            String VERSION = "";
            try {
                PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0);
                VERSION = ""+pInfo.versionName;
            } catch(Exception e) {

            }
            // Add 'general' preferences.
            addPreferencesFromResource(R.xml.pref_general);
            findPreference(getString(R.string.app_version)).setSummary(VERSION);
            bindPreferenceSummaryToValue(findPreference(getString(R.string.sm_fitness)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.sm_age)));
            bindPreferenceSummaryToValue(findPreference(getString(R.string.sm_sex)));
        }

        /**
         * Determines whether the simplified settings UI should be shown. This is
         * true if this is forced via {@link #ALWAYS_SIMPLE_PREFS}, or the device
         * doesn't have newer APIs like {@link android.preference.PreferenceFragment}, or the device
         * doesn't have an extra-large screen. In these cases, a single-pane
         * "simplified" settings UI should be shown.
         */
        private static boolean isSimplePreferences(Context context) {
            return true;
        }

        /**
         * A preference value change listener that updates the preference's summary
         * to reflect its new value.
         */
        private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object value) {
                String stringValue = value.toString();
                String stringKey = preference.getKey();
//            SharedPreferences sharedPreferences = ge
//            SharedPreferences.Editor editor = sharedPreferences.edit();
                if (preference instanceof ListPreference) {
                    // For list preferences, look up the correct display value in
                    // the preference's 'entries' list.
                    ListPreference listPreference = (ListPreference) preference;
                    int index = listPreference.findIndexOfValue(stringValue);
                    String newValue = index >= 0
                            ? listPreference.getEntries()[index].toString()
                            : null;
                    // Set the summary to reflect the new value.
//                editor.putString(stringKey, newValue);
                    Log.d(TAG, stringKey + ", " + newValue);
                    preference.setSummary(
                            newValue);

                } else if (preference instanceof RingtonePreference) {
                    // For ringtone preferences, look up the correct display value
                    // using RingtoneManager.
                    if (TextUtils.isEmpty(stringValue)) {
                        // Empty values correspond to 'silent' (no ringtone).
//                    preference.setSummary(R.string.pref_ringtone_silent);

                    } else {
                        Ringtone ringtone = RingtoneManager.getRingtone(
                                preference.getContext(), Uri.parse(stringValue));

                        if (ringtone == null) {
                            // Clear the summary if there was a lookup error.
                            preference.setSummary(null);
                        } else {
                            // Set the summary to reflect the new ringtone display
                            // name.
                            String name = ringtone.getTitle(preference.getContext());
                            preference.setSummary(name);
                            Log.d(TAG, stringKey + ", " + name);
                        }
                    }


                } else {
                    // For all other preferences, set the summary to the value's
                    // simple string representation.
                    preference.setSummary(stringValue);
//                editor.putString(stringKey, stringValue);
                    Log.d(TAG, stringKey + ", " + stringValue);
                }
                return true;
            }
        };

        /**
         * Binds a preference's summary to its value. More specifically, when the
         * preference's value is changed, its summary (line of text below the
         * preference title) is updated to reflect the value. The summary is also
         * immediately updated upon calling this method. The exact display format is
         * dependent on the type of preference.
         *
         * @see #sBindPreferenceSummaryToValueListener
         */
        private static void bindPreferenceSummaryToValue(Preference preference) {
            // Set the listener to watch for value changes.
            preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

            // Trigger the listener immediately with the preference's
            // current value.
            sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                    PreferenceManager
                            .getDefaultSharedPreferences(preference.getContext())
                            .getString(preference.getKey(), ""));
        }
    }
    @Override
    public void finish() {
        super.finish();
        Log.d(TAG, "Destroying foodntry");
        overridePendingTransition(R.anim.do_nothing, R.anim.exit_right);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            // TODO: If Settings has multiple levels, Up should navigate up
            // that hierarchy.
//            NavUtils.navigateUpFromSameTask(this);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
