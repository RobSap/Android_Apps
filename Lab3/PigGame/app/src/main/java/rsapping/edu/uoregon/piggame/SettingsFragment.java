package rsapping.edu.uoregon.piggame;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by Alphadog1939 on 7/3/16.
 */
public class SettingsFragment extends PreferenceFragment{

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Load the preferences from an XML resource, settings.xml
            addPreferencesFromResource(R.xml.settings);
        }

}
