/*
 * 
	This file is part of Apple Crunch.

    Apple Crunch is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Apple Crunch is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Apple Crunch.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package com.malgon.applecrush;
 
import android.os.Bundle;
import android.preference.PreferenceActivity;
 
public class Settings extends PreferenceActivity {
 
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
 
        addPreferencesFromResource(R.xml.settings);
    }
}