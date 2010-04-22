greenit = function(){
    var URI_BASE_IMG = "http://qr-decode.appspot.com/png/", PERIOD = 3, ID_VCARD_CONTAINER = "list-vcard", ID_SPINNER = "spinner", VCARD_FORMATER = new Template( //TODO use vcardmicroformat
'<li class="vcard">' +
    '<img class="flashcode" src="#{src}" width="#{height}" height="#{height}" />' +
    '<span class="name">#{name}</span>' +
    '<span class="orga">#{orga}</span>' +
    '<span class="addr">#{addr}</span>' +
    '</li>'), MAPPING = {
        "Amazon": "amazon",
        "Apache Software Foundation": "apache",
        "Apple": "apple",
        "AT&T": "att",
        "Cisco Systems": "cisco",
        "Dell": "dell",
        "eBay": "ebay",
        "Facebook": "facebook",
        "Google": "google",
        "Hewlett-Packard": "hp",
        "IBM": "ibm",
        "Intel": "intel",
        "LinkedIn": "linkedin",
        "": "microsoft",
        "Mozilla Foundation": "mozilla",
        "Oracle": "oracle",
        "O Reilly Media ": "oreilly",
        "RedHat": "redhat",
        "TechCrunch": "techcrunch",
        "Wired": "wired"
    };
    
    
    return ({
        map: null,
        geocoder: null,
        vcards: null,
        
        init: function(wrapper_id){
			
        	this.addDomElements(wrapper_id);
            this.initGMap();
			
            new Ajax.Request("/qrdecoder/", {
                method: 'get',
                
                onSuccess: function(transport){
                    greenit.initPeriodicalExecuter(transport.responseText.split("BEGIN:VCARD\r\n").without(""));
                }
            });
        },
		
		addDomElements: function(wrapper_id) {
			console.log(wrapper_id)
			console.log($(wrapper_id))
			var vcards = new Element('ul', {id: 'list-vcard'}).insert(new Element('li', {id: 'spinner'})),
				container_gmap = new Element('div', {id: 'container-gmap'}).insert(new Element('div', {id: "gmap", style: 'width: 800px; height: 450px'}));
				
			$(wrapper_id).insert(vcards);
			$(wrapper_id).insert(container_gmap)
				
		},
        
        initGMap: function(){
            this.map = new GMap2(document.getElementById("gmap"));
            this.geocoder = new GClientGeocoder();
            
            this.map.setCenter(new GLatLng(39.16414104768742, -101.689453125), 4);
            this.map.setUIToDefault();
        },
        
        showAddress: function(address){
            this.geocoder.getLatLng(address, function(latlng){
                if (!latlng) {
                    alert(address + " not found");
                }
                else {
                    var marker = new GMarker(latlng);
                    
                    this.map.panTo(latlng);
                    this.map.addOverlay(marker);
                    marker.openInfoWindowHtml(address);
                }
            });
        },
        
        initPeriodicalExecuter: function(vcards){
            this.vcards = vcards;
            
            var pExec = new PeriodicalExecuter(function(){
                this.next();
            }, PERIOD), _self = this, showVcard = function(address, organisation, name){
                //populate barcode
                var data = {
                    src: URI_BASE_IMG + MAPPING[organisation] + '.png',
                    height: '50',
                    name: name,
                    addr: address,
                    orga: organisation
                };
                $(ID_VCARD_CONTAINER).insert(VCARD_FORMATER.evaluate(data));
                
                //add marker
                _self.geocoder.getLatLng(address, function(latlng){
                    if (!latlng) {
                        alert(address + " not found");
                    }
                    else {
                        var marker = new GMarker(latlng);
                        
                        _self.map.panTo(latlng);
                        _self.map.addOverlay(marker);
                        marker.openInfoWindowHtml(organisation);
                    }
                });
            };
            
            pExec.data = vcards;
            pExec.next = function(){
                var vcard = this.data.pop();
                
                if (this.data.length === 0) {
                    this.stop();
                    $(ID_SPINNER).hide();
                    return;
                }
                var address = /ADR;WORK:;;(.*)/.exec(vcard), name = /N:(.*)/.exec(vcard), organisation_ending_with_dot_coma = /ORG:(.*)/.exec(vcard), organisation = "";
                
                if (address) {
                    address = address[1];
                }
                if (name) {
                    name = name[1].split(";").reverse().join(" ");
                }
                if (organisation_ending_with_dot_coma) {
                    organisation_ending_with_dot_coma = organisation_ending_with_dot_coma[1];
                    organisation = organisation_ending_with_dot_coma.substring(0, (organisation_ending_with_dot_coma.length - 1));
                }
                showVcard(address, organisation, name);
            }
            $(ID_SPINNER).show();
            pExec.next();
        }
    });
}();

