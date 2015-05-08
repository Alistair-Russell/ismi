

function Entity (json) {
	this.id = json.id;
	this.oc = json.oc;
	this.ov = json.ov;
	this.nov = json.nov;
	this.lw = json.lw;
	this.node_type = json.node_type;
	
	//attention: this array are saved as map<index, object>
	this.atts = json.atts;
	this.src_rels = json.src_rels;
	this.tar_rels = json.tar_rels;
    
	this.getAttOvByName = function(attName){
		for(var mapKey in this.atts){
			var name = this.atts[mapKey].name;
			if(name == attName){
				return this.atts[mapKey].ov;
			}
		}
	}
	
	this.toString = function() {
        return "Entity [id="+this.id+", ov="+this.ov+"]";
    };
}

