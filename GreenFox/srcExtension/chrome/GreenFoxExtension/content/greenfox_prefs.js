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
	get: function(prefName) {
		// Récupération de l'API des préférences sous forme d'objet JavaScript :
		var oPrefs = Components.classes["@mozilla.org/preferences-service;1"]
				.getService(Components.interfaces.nsIPrefService).getBranch("");
				
		// Récupération de la préférence.
		return oPrefs.getCharPref("extensions.greenfox." + prefName);
	},
	getDefault: function(prefName) {
		// Récupération de l'API des préférences sous forme d'objet JavaScript :
		var oPrefs = Components.classes["@mozilla.org/preferences-service;1"]
				.getService(Components.interfaces.nsIPrefService).getDefaultBranch("");
				
		// Récupération de la préférence.
		return oPrefs.getCharPref("extensions.greenfox." + prefName);
	},
	/**
	 * Chargement des préférences dans la boîte de dialogue.
	 */
	dialogInit: function() {
		try {
			document.getElementById("greenfox-endpoint").value = this.get('endpoint');
			document.getElementById("greenfox-challengerID").value = this.get('challengerID');
		} catch (e) {
			alert("Failed to load settings.\n" + e);
		}
	},
	/**
	 * Enregistrement des préférences + vérif valeurs OK.
	 * 
	 * @return true si on doit fermer la boîte de dialogue, false sinon.
	 */
	dialogSave: function() {
		try {
			// Récup de l'API des préférences :
			var oPrefs = Components.classes["@mozilla.org/preferences-service;1"]
					.getService(Components.interfaces.nsIPrefService).getBranch("");
					
			var challengerID = this.readValueFromDialog("greenfox-challengerID");
			var endpointVal = this.readValueFromDialog("greenfox-endpoint");

			if( this.dialogValidate( challengerID, endpointVal ) ) {
				// Enregistrement de la préférence dans le profil Mozilla :
				oPrefs.setCharPref("extensions.greenfox.endpoint", endpointVal);
				oPrefs.setCharPref("extensions.greenfox.challengerID", challengerID);
				// On peut fermer la boîte de dialogue :
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
		// Récup de la valeur saisie par l'utilisateur :
		var value = document.getElementById(domId).value;
		// Fait un trim :
		value = value.replace(/^\s+/g,'').replace(/\s+$/g,'');
		return value;
	},
	/**
	 * Validation des préférences dont les valeurs sont passées en paramètre.
	 * Pour l'instant : 1 seule préférence.
	 * @param endpointVal URL de l'endpoint
	 * @return true si et seulement si valeurs de prefs OK
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
				// Longueur == 0
				return false;
			}
			return true;
		};
		// TODO validation d'email : un peu légère...
		return validate(endpointVal,"^https?://([A-Za-z0-9\-\./]|:)+$","URL") /*&& validate(challengerID,"^.*@.*$","email")*/
	},
	/**
	 * Retour aux réglages d'usine.
	 */
	dialogReset: function() {
		// Affectation des valeurs par défaut des préférences aux champs du dialogue :
		document.getElementById("greenfox-endpoint").value = this.getDefault("endpoint");
		document.getElementById("greenfox-challengerID").value = this.getDefault("challengerID");
	},
}

