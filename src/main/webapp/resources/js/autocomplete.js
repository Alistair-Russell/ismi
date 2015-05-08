function setFocus(caller) {

	var aa = this;
	var rs = $("a[id*=':out:0:selectItem']");
	if (rs.length > 0) {
		var firstItem = rs.get(0);
		var divSuggestion = firstItem.children[0];
		if(divSuggestion){
			divSuggestion.style.backgroundColor = '#2A6CC2';
			caller.blur();
			divSuggestion.focus();	
			
		}
		/*
		var element = firstItem.childNodes.item("suggestionItem");
		element.focus();
		alert("setFocus!");
		*/
		
	}

}
/*
$( "div" ).focus(function() {
	alert($( this ).className);
});*/