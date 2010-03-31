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

// Error handling:
function handleError(e) {
	window.alert('GreenFox error: \n' + e);
}

// GreenFox initialization:
window.addEventListener("load", function() { greenfoxController.init(); }, false);
window.addEventListener("unload", function() { greenfoxController.destroy(); }, false);

/**
 * GreenFox controller.
 */
var greenfoxController = {

	getSampler: function() { return sampler },
	
	getCollect: function() { return greenfoxCollect },
	
	///////////////
	// Lifecycle //    
	///////////////
	
	init: function() {
		try {
			this.getCollect().init();
			var samplerOK = this.getSampler().init();
			
			// Get out prefs:
			this.prefs = Components.classes["@mozilla.org/preferences-service;1"]
				 .getService(Components.interfaces.nsIPrefService)
				 .getBranch("extensions.greenfox.");
				 
			// Listen to the prefs:
			this.prefs.QueryInterface(Components.interfaces.nsIPrefBranch2)
			this.prefs.addObserver("", this, false)

			this.lastMeasure = '';
			if( ! samplerOK ) {
				this.setState("sampler_ko");
			} else {
				if( this.isConfigured() ) {
					this.setState('ready');
				} else {
					this.setState('not_configured');
				}
			}
		} catch( e ) {
			handleError(e);
		}
	},
	destroy: function() {
		this.prefs.removeObserver("", this);
		this.getSampler().destroy();
		this.getCollect().destroy();
	},
	
	////////////////////////////
	// Preferences management //    
	////////////////////////////
	
	prefs: null,
	
	/*
	 * Called when prefs are changed.
	 */
	observe: function(subject, topic, data) {
		if( topic == 'nsPref:changed' ) {
			this.destroy();
			this.init();
		}
	},
	/**
	 * @return true if GreenFox has been configured by the challenger, false otherwise.
	 */
	isConfigured: function() {
		return this.isPrefConfigured("challengerID")
	},
	isPrefConfigured: function(prefName) {
		var defaults = Components.classes["@mozilla.org/preferences-service;1"]
				.getService(Components.interfaces.nsIPrefService).getDefaultBranch("extensions.greenfox.");
		var prefs = Components.classes["@mozilla.org/preferences-service;1"]
					.getService(Components.interfaces.nsIPrefService).getBranch("extensions.greenfox.");
		var d = defaults.getCharPref(prefName)
		var p = prefs.getCharPref(prefName)
		return d != p;
	},

	/////////////////////////////////////
	// Controller and state management //
	/////////////////////////////////////
	
	onToolbarClick: function() {
		try {
			var st = this.states[this.currentState]
			eval('this.'+st.action+'()')
		} catch( e ) {
			handleError(e)
		}
	},
	
    onStart: function() {
    	this.lastMeasure = ''
    	this.setState('running')
    	this.getSampler().startMeasure()
    },
    
    onEnd: function() {
    	var measure = this.getSampler().endMeasure()
    	this.lastMeasure = measure / 10 / 1000; // Convert thicks to ms
    	this.setState('sending_sample')
    	var sampleOk = this.getCollect().postSample(measure)
    	if( sampleOk ) {
	    	this.setState('sample_sent')
	    } else {
	    	this.setState('fail')
	    }
    },
    onConfigure: function() {
		try {
			window.openDialog("chrome://greenfox/content/prefs.xul","","chrome, dialog, modal, resizable=no", {})
    	} catch( e ) { handleError(e) }
    },
    noop: function() {
    	// Function that does nothing
    },
    
  	states: {
  		ready: { label: 'ready', action: 'onStart' },
  		running: { label: 'sampling...', action: 'onEnd' },
  		sending_sample: { label: 'sending sample...', action: 'noop' },
  		sample_sent: { label: 'sample successfully sent', action: 'onStart' },
  		fail: { label: 'FAILURE!', action: 'onStart' },
  		not_configured: { label: 'please configure GreenFox', action: 'noop' },
  		not_initialized: { label: 'Greenfox is not loaded!!!', action: 'noop' },
  		sampler_ko: { label: 'Native component not loaded!!!', action: 'noop' },
  	},
  	
    currentState: 'not_initialized',
  	
    lastMeasure: '',
  	
    setState: function(stateName) {
    
    	// Get state info:
    	var nextState = this.states[stateName]

		// Update status bar label:
		document.getElementById("greenfox-status-label").value = nextState.label;
		
		// Update status bar measure display:
		var m = this.lastMeasure;
		if( m !== '' ) {
			m = 'cpu='+m+' ms'
		}
		document.getElementById("greenfox-status-lastmeasure").value = m;
		
		// Update status bar image:
		var statusImg = document.getElementById("greenfox-status-image");
		statusImg.className='greenfox-status-image-'+stateName;
		
		// Update status bar button:
		var btn = document.getElementById("greenfox-status-button");
		switch( nextState.action ) {
			case 'onStart':
				btn.label='Start';
				btn.disabled=false;
				break;
			case 'onEnd':
				btn.label='Stop';
				btn.disabled=false;
				break;
			default:
				btn.label='Start';
				btn.disabled=true;
				break;
		}
		
		// Update GreenFox current state:
		this.currentState = stateName;
    },
}

