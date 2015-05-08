var divaGlobal = {
		rest_url : "http://localhost:8080/ismi-richfaces",
		debugModus : false,
		//rest_url : "https://openmind-ismi-dev.mpiwg-berlin.mpg.de/om4-ismi",
		//rest_url : "http://ismi-dev.mpiwg-berlin.mpg.de:8080/ismi-richfaces",
		iipServerURL: "https://images.rasi.mcgill.ca/fcgi-bin/iipsrv.fcgi",
		drupal_url: "https://drupal.mpiwg-berlin.mpg.de/ismi"
}

function showTitleDetailsBig(titleId){
	showTitleDetails(titleId, 'templates/templateTitleInformationBig.html');
}

function showTitleDetailsSmall(titleId){
	showTitleDetails(titleId, 'templates/templateTitleInformationSmall.html');
}

function showWitnessDetailsBig(witnessId){
	showWitnessDetails(witnessId, 'templates/templateWitnessInformationBig.html');
}

function showWitnessDetailsSmall(witnessId){
	showWitnessDetails(witnessId, 'templates/templateWitnessInformationSmall.html');
}

function setEntry(loc,field) {
    if ((field == null)||(field == "")) {
	$(loc).parent().hide();
    }
    $(loc).html(field);
}

function createIdFromRef(txt) {
   
    txt=txt.replace(/\W+/g, "");
    return txt;

}

function showTitleDetails(titleId, template){
	$.ajax({
		url: template,
		type: 'GET',
		success: function(data){ 
			//inserting the template in this document
    		$('#additional-information').html(data);
    		$('#title-details-show-more').attr('data-title-id', titleId);
    		$('#title-details-show-less').attr('data-title-id', titleId);
			
			var jsonRequest = divaGlobal.rest_url + '/jsonInterface?method=get_title_details&include_romanization=true&id=' + titleId;
    		
			console.log(jsonRequest);
			
    		$.ajax({
    		    url: jsonRequest,
    		    type: 'GET',
    		    success: function(data){ 
    		    	loadTitleInformation(data, titleId);
    		    	
    		    },
    		    error: function(data) {
    		    	console.error('jsonInterface error. = ' + jsonRequest);
    		    }
    		});
    		
		},
		error: function(data) {
			console.error(template + ' no found.');
		}
	});	
}

function showWitnessDetails(titleId, template){
	$.ajax({
		url: template,
		type: 'GET',
		success: function(data){ 
			//inserting the template in this document
    		$('#additional-information2').html(data);
    		$('#witness-details-show-more').attr('data-witness-id', titleId);
    		$('#witness-details-show-less').attr('data-witness-id', titleId);
			
			var jsonRequest = divaGlobal.rest_url + '/jsonInterface?method=get_witness_details&include_romanization=true&id=' + titleId;
    		
			console.log(jsonRequest);
			
    		$.ajax({
    		    url: jsonRequest,
    		    type: 'GET',
    		    success: function(data){ 
    		    	loadWitnessInformation(data, titleId);
    		    	
    		    },
    		    error: function(data) {
    		    	console.error('jsonInterface error. = ' + jsonRequest);
    		    }
    		});
    		
		},
		error: function(data) {
			console.error(template + ' no found.');
		}
	});	
}





function showBibliographyEntryFormatted(bibId,bibInf, bibLoc){
    var id=encodeURIComponent(bibId.trim());
    var loc = bibLoc;
    console.debug("setting up bib id="+id+" at loc="+loc);
	$.ajax({
		url: divaGlobal.drupal_url+"/formatbiblio/"+id,
		type: 'GET',
		success: function(data){ 
		    console.debug("got bib id="+id+" for loc="+loc);
			//inserting the template in this document
    		//mache rel urls zu absoluten
			if (data.nodes.length > 0) {
				var ref = data.nodes[0].node.citation.replace(new RegExp("/ismi","g"),divaGlobal.drupal_url);  	
			    var bib =  $(loc);
			   bib.html(ref);
			}
		},
		error: function(data) {
			console.error('unable to load formatted bib entry ' + id);
		}
	});	
}

function loadWitnessInformation(json, titleId){
	
	if(divaGlobal.debugModus){
		$('#witness-id').html(titleId);
	}
	
	var fullTitle = json.data.fullTitle;
	var fullTitleTranslit = json.data.fullTitleTranslit;
	var titleContent = new String();
	if(fullTitle){
		titleContent = "<table><tr><td class='tdTitle' style='width: 300px;''>"+fullTitle+"</td></tr><tr><td class='tdTitle'>"+fullTitleTranslit+"</td></tr></table>";
	}else{
		titleContent = "<table><tr><td class='tdTitle'>"+fullTitleTranslit+"</td></tr></table>"
	}
	
	var authorName = json.data.authorName;
	var authorNameTranslit = json.data.authorNameTranslit;
	var authorContent = new String();
	if(authorName){
		authorContent = "<table><tr><td class='tdTitle' style='width: 300px;''>"+authorName+"</td></tr><tr><td class='tdTitle'>"+authorNameTranslit+"</td></tr></table>";
	}else{
		authorContent = "<table><tr><td class='tdTitle'>"+authorNameTranslit+"</td></tr></table>"
	}
	
	var tableOfContents = json.data.tableOfContents;
	var notesOnTitleAuthor = json.data.notesOnTitleAuthor;
	var notesOnCollationAndCorrections = json.data.notesOnCollationAndCorrections;
	var notesOnOwnership = json.data.notesOnOwnership;
	var notes = json.data.notes;
	var codex = json.data.codex;
	var collection = json.data.collection;
	var repository = json.data.repository;
	var city = json.data.city;
	var country = json.data.country;
	var folios = json.data.folios;
	var incipit = json.data.incipit;
	var explicit = json.data.explicit;
	var subject = json.data.subject;
	var ahlwardtNo = json.data.ahlwardtNo;
	var titleAsWrittenInWitness = json.data.titleAsWrittenInWitness;
	var authorAsWrittenInWitness = json.data.authorAsWrittenInWitness;
	var copyist = json.data.copyist;
	var placeOfCopying = json.data.placeOfCopying;
	var colophon = json.data.colophon;
	
	var pageDimensions = json.data.pageDimensions;
	var writtenAreaDimensions = json.data.writtenAreaDimensions;
	var linesPerPage = json.data.linesPerPage;
	var pageLayout = json.data.pageLayout;
	var script = json.data.script;
	var writingSurface = json.data.writingSurface;
	var creationDate = json.data.creationDate;
	
	var readersContent = "<table>";
    var readers= json.data.readers;
	for(var readerKey in json.data.readers){
		var reader = json.data.readers[readerKey];
		readersContent += "<tr><td class='tdTitle' style='width: 300px;'>"+reader+"</td></tr>";
	}
	readersContent += "</table>";

	

    var referencesContent="<table>";
	for(var refKey in json.data.references){
		var ref = json.data.references[refKey];
	    referencesContent += "<tr><td class='tdTitle' id='bibl-entry-"+titleId+"-"+createIdFromRef(refKey)+"' style='width: 300px;'>"+refKey+"</td></tr><tr><td class='tdTitle'>"+ref+"</td></tr>";
	}
	referencesContent += "</table>";	
	
        setEntry('#witness-title',"") //no need to display this already in titleInfomation
	//$('#witness-title').html(titleContent);

    setEntry('#witness-title-written-in-witness',titleAsWrittenInWitness);

       setEntry('#witness-author',"") //no need to display this already in titleInfomation
	//$('#witness-author').html(authorContent);

        
        setEntry('#witness-author-written-in-witness',authorAsWrittenInWitness);
	//$('#witness-codex').html(codex);
        setEntry('#witness-codex','');

    setEntry('#witness-ahlwardt-no',ahlwardtNo);
    setEntry('#witness-copyist',copyist);
    setEntry('#witness-place-copyist',placeOfCopying);
    setEntry('#witness-date-copyist',creationDate);
    setEntry('#witness-colophon',colophon);

	$('#witness-readers').html(readersContent);

   if (( readers == null)||( readers == "")) {
	$('#witness-readers').parent().hide();
    };
    setEntry('#witness-folios',folios);
    setEntry('#witness-dimensions',pageDimensions);
    setEntry('#witness-written-dimensions',writtenAreaDimensions);
    setEntry('#witness-lines',linesPerPage);
    setEntry('#witness-surface',writingSurface);
    setEntry('#witness-layout',pageLayout);
	
        //$('#title-incipit').text(incipit);
        setEntry('#witness-incipit',incipit);
	
        //$('#title-explicit').text(explicit);
        setEntry('#witness-explicit',explicit);

       //$('#title-table-contents').text(tableOfContents);
    setEntry('#witness-table-contents',tableOfContents);
    setEntry('#witness-script',script);
    setEntry('#witness-notes',notes);
    //setEntry('#witness-refs',"TODO");

    $('#witness-refs').html(referencesContent);
    for(refKey in json.data.references){
		var ref = json.data.references[refKey];
	var loc="#bibl-entry-"+titleId+"-"+ createIdFromRef(refKey) ;
	    var bibInf=json.data.references[refKey];
	showBibliographyEntryFormatted(refKey,bibInf,loc);	    
	}
	referencesContent += "</table>";

	
}

function loadTitleInformation(json, titleId){
	
	if(divaGlobal.debugModus){
		$('#title-id').html(titleId);
	}
	
	var fullTitle = json.data.fullTitle;
	var fullTitleTranslit = json.data.fullTitleTranslit;
	var titleContent = new String();
    /*
	if(fullTitle){
		titleContent = "<table><tr><td class='tdTitle' style='width: 300px;''>"+
		'<a href="../public/dynamicPage.xhtml?eid='+titleId+'">'+fullTitle+"</a>"+'<a href="'+divaGlobal.drupal_url+"/entity/"+titleId+'">(full)</a>'
		+"</td></tr><tr><td class='tdTitle'>"+fullTitleTranslit+"</td></tr></table>";
	}else{
		titleContent = "<table><tr><td class='tdTitle'>"+
		'<a href="../public/dynamicPage.xhtml?eid='+titleId+'">'+fullTitleTranslit+"</a>"+'<a href="'+divaGlobal.drupal_url+"/entity/"+titleId+'">(full)</a>'
		+"</td></tr></table>"
	}
	
*/
        if(fullTitle){
		titleContent = "<table><tr><td class='tdTitle' style='width: 300px;''>"+
		'<a href="'+divaGlobal.drupal_url+"/entity/"+titleId+'">'+fullTitle+'</a>'
		+"</td></tr><tr><td class='tdTitle'>"+fullTitleTranslit+"</td></tr></table>";
	}else{
		titleContent = "<table><tr><td class='tdTitle'>"+
		'<a href="'+divaGlobal.drupal_url+"/entity/"+titleId+'">'+fullTitleTranslit+'</a>'
		+"</td></tr></table>"
	}
	var language = json.data.language;
	var category = json.data.category;
	var author = json.data.author;
	var authorId = json.data.authorId;
	var aliases = json.data.aliases;
	var personDedicatedTo = json.data.personDedicatedTo;
	var commentaryOnText = json.data.commentaryOnText;
	var translationOfText = json.data.translationOfText;
	var versionOfText = json.data.versionOfText;
	
	var aliasesContent = "<table>";
	for(aliasKey in aliases){
		var alias = aliases[aliasKey];
		//alert(alias);
		aliasesContent += "<tr><td class='tdTitle' style='width: 300px;'>"+alias+"</td></tr>";
	}
	aliasesContent += "</table>";
	
     if (( aliases == null)||( aliases == "")) {
	$('#title-aliases').parent().hide();
    };

	var dedication = json.data.dedication;
	var notes = json.data.notes;
	var tableOfContents = json.data.tableOfContents;
	var explicit = json.data.explicit;
	var incipit = json.data.incipit;
	var creationDate = json.data.creationDate;
	var createIn = json.data.createIn;
	var authorMisattribution = json.data.authorMisattribution;
	
	var authorMisattributionContent = "<table>";
	for(var missKey in authorMisattribution){
		var miss = authorMisattribution[missKey];
		authorMisattributionContent += "<tr><td class='tdTitle' style='width: 300px;'>"+miss+"</td></tr>";
	}
	authorMisattributionContent += "</table>";
	
    if (( authorMisattribution == null)||( authorMisattribution == "")) {
	$('#title-misattributions').parent().hide();
    };

/*        var authorContent = '<a href="../public/dynamicPage.xhtml?eid='+authorId+'">'+author+"</a>"+'<a href="'+divaGlobal.drupal_url+"/entity/"+authorId+'">(full)</a>';
*/     
    var authorContent = '<a href="'+divaGlobal.drupal_url+"/entity/"+authorId+'">'+author+"</a>";
    


        var referencesContent="<table>";
	for(var refKey in json.data.references){
		var ref = json.data.references[refKey];
	    referencesContent += "<tr><td class='tdTitle' id='bibl-entry-"+titleId+"-"+createIdFromRef(refKey)+"' style='width: 300px;'>"+refKey+"</td></tr><tr><td class='tdTitle'>"+ref+"</td></tr>";
	}
	referencesContent += "</table>";

        
	//inserting the values into the table
	$('#title-tile').html(titleContent);
	$('#title-aliases').html(aliasesContent);
	$('#title-misattributions').html(authorMisattributionContent);
	$('#title-author').html(authorContent);


	//$('#title-subject').text(category);
        setEntry('#title-subject',category);


	//$('#title-lang').text(language);
        setEntry('#title-lang',language);

	//$('#title-date-composition').text(creationDate);
        setEntry('#title-date-composition',creationDate);

	//$('#title-place-composition').text(createIn);
        setEntry('#title-place-composition',createIn);

        //$('#title-dedicated-to').text(personDedicatedTo);
        setEntry('#title-dedicated-to',personDedicatedTo);
	
        //$('#title-dedication').text(dedication);
        setEntry('#title-dedication',dedication);

	//$('#title-commentary-on').text(commentaryOnText);
        setEntry('#title-commentary-on',commentaryOnText);

	//$('#title-translation-of').text(translationOfText);
        setEntry('#title-translation-of',translationOfText);

	//$('#title-version-of').text(versionOfText);
        setEntry('#title-version-of',versionOfText);
	
        //$('#title-incipit').text(incipit);
        setEntry('#title-incipit',incipit);
	
        //$('#title-explicit').text(explicit);
        setEntry('#title-explicit',explicit);

       //$('#title-table-contents').text(tableOfContents);
       setEntry('#title-table-contents',tableOfContents);


	//$('#title-notes').text(notes);
        setEntry('#title-notes',notes);

	$('#title-bib').html(referencesContent);
       	for(var refKey in json.data.references){
		var ref = json.data.references[refKey];
	    var loc="#bibl-entry-"+titleId+"-"+ createIdFromRef(refKey) ;
	    var bibInf=json.data.references[refKey];
	    showBibliographyEntryFormatted(refKey,bibInf,loc);	    
	}
	referencesContent += "</table>";

    
   
   
}


$(document).ready(function () {
	
    $("#witness-edit").on('click', '.set-start-set', function(ev)
	{
    	var dv = $('#diva-wrapper').data('diva');
    	var pageNumber = dv.getCurrentPageNumber();
    	var witnessId = $(this).data('witness');
    	var inputElement = $('#input-start-page-' + witnessId)    	
    	inputElement.val(pageNumber);
	});
    
    $("#witness-edit").on('click', '.set-start-reset', function(ev)
    		{
    	    	var dv = $('#diva-wrapper').data('diva');
    	    	var witnessId = $(this).data('witness');
    	    	var inputElement = $('#input-start-page-' + witnessId)
    	    	inputElement.val("");
   	});
    
    $("#witness-edit").on('click', '.set-end-set', function(ev) {
    	    	var dv = $('#diva-wrapper').data('diva');
    	    	var pageNumber = dv.getCurrentPageNumber();
    	    	var witnessId = $(this).data('witness');
    	    	var inputElement = $('#input-end-page-' + witnessId)
    	    	
    	    	inputElement.val(pageNumber);
    });
    	    
    $("#witness-edit").on('click', '.set-end-reset', function(ev) {
       	    	var dv = $('#diva-wrapper').data('diva');
       	    	var witnessId = $(this).data('witness');
       	    	var inputElement = $('#input-end-page-' + witnessId)
       	    	inputElement.val("");       	    	
  	   	});    
    
});

