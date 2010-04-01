/*
GreenFox

Copyright (c) 2010, OCTO Technology
All rights reserved.
Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

    * Redistributions of source code must retain the above copyright
      notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above copyright
      notice, this list of conditions and the following disclaimer in the
      documentation and/or other materials provided with the distribution.
    * Neither the name of GreenFox nor the
      names of its contributors may be used to endorse or promote products
      derived from this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY CONTRIBUTORS ``AS IS'' AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE CONTRIBUTORS BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

var greenfoxPrefsMgr = {
	getPrefs: function() {
		return Components.classes["@mozilla.org/preferences-service;1"]
				.getService(Components.interfaces.nsIPrefService).getBranch("extensions.greenfox.");
	},
	getDefaultPrefs: function() {
		return Components.classes["@mozilla.org/preferences-service;1"]
				.getService(Components.interfaces.nsIPrefService).getDefaultBranch("extensions.greenfox.");
	},
	/**
	 * Init dialog box with GreenFox preferences.
	 */
	dialogInit: function() {
		try {
			var prefs = this.getPrefs();
			document.getElementById("greenfox-endpoint").value = prefs.getCharPref('endpoint');
			document.getElementById("greenfox-challengerID").value = prefs.getCharPref('challengerID');
			document.getElementById("greenfox-collection-enabled").checked = prefs.getBoolPref('collection.enabled');
		} catch (e) {
			alert("Failed to load settings.\n" + e);
		}
	},
	/**
	 * Check submitted values & set preferences.
	 * @return true if values are OK
	 */
	dialogSave: function() {
		try {
			var oPrefs = this.getPrefs();
			
			var challengerID = this.readValueFromDialog("greenfox-challengerID");
			var endpointVal = this.readValueFromDialog("greenfox-endpoint");
			var collectionEnabled = this.readCheckboxFromDialog("greenfox-collection-enabled");

			if( this.dialogValidate( challengerID, endpointVal ) ) {
				oPrefs.setCharPref('endpoint', endpointVal)
				oPrefs.setCharPref('challengerID', challengerID)
				oPrefs.setBoolPref('collection.enabled', collectionEnabled)
				return true;
			} else {
				return false;
			}
		} catch (e) {
			alert("Failed to load settings.\n" + e);
			return false;
		}
	},
	readValueFromDialog: function(domId) {
		// Get submitted value:
		var value = document.getElementById(domId).value;
		// Trim spaces:
		value = value.replace(/^\s+/g,'').replace(/\s+$/g,'');
		return value;
	},
	readCheckboxFromDialog: function(domId) {
		var value = document.getElementById(domId).checked;
		return value;
	},
	/**
	 * Validate submitted preferences.
	 * @param challengerID challenger ID to validate
	 * @param endpointVal endpoint URL to validate
	 * 
	 * @return true if all is OK
	 */
	dialogValidate: function(challengerID, endpointVal) {
		var validate = function(value,regexp,what) {
			var lengthValue = value.length;
			if(lengthValue > 0) {
				var r = new RegExp();
				r.compile(regexp);
				if (!r.test( value ))
				{
					alert("Invalid " + what);
					return false;
				}
			} else {
				// lengthValue==0
				return false;
			}
			return true;
		};
		// TODO implement email validation
		return validate(endpointVal,"^https?://([A-Za-z0-9\-\./]|:)+$","URL") /*&& validate(challengerID,"^.*@.*$","email")*/
	},
	/**
	 * Revert to factory settings.
	 */
	dialogReset: function() {
		var defaults = this.getDefaultPrefs()
		document.getElementById("greenfox-endpoint").value = defaults.getCharPref("endpoint");
		document.getElementById("greenfox-challengerID").value = defaults.getCharPref("challengerID");
		document.getElementById("greenfox-collection-enabled").checked = defaults.getBoolPref("collection.enabled");
	},
}

