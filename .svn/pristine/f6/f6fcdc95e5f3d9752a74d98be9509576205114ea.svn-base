<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="org.mpi.openmind.repository.bo.Entity"%>
<%@page import="de.mpiwg.itgroup.diva.jsp.JSPDigitalization"%>


<html>
	<head>
		<title>Witness Details</title>
		
		<link href="../imageServer/resources/css/diva4ismi.css" type="text/css" rel="stylesheet" />
		<link href="../imageServer/resources/css/bootstrap.css" type="text/css" rel="stylesheet" />
		<link href="../imageServer/resources/css/bootstrap-responsive.min.css" type="text/css" rel="stylesheet">
		<link href="../imageServer/resources/css/style.css" type="text/css" rel="stylesheet">
		<link href="../imageServer/resources/css/diva.min.css" type="text/css" rel="stylesheet" />
		
		
		<script type="text/javascript" src="../imageServer/resources/js/jquery.min.js"></script>
		<script type="text/javascript" src="../imageServer/resources/js/diva.min.js"></script>
		<script type="text/javascript" src="../imageServer/resources/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="../imageServer/resources/js/typeahead.js"></script>
		<script type="text/javascript" src="../imageServer/resources/js/ismiUtils.js"></script>
		<script type="text/javascript" src="../imageServer/resources/js/diva4ismi.js"></script>
		
		<jsp:useBean id="witnessPage0" class="de.mpiwg.itgroup.ismi.publicView.pages.WitnessCodexDynamicPage" scope="session" />
		<jsp:setProperty name="witnessPage0" property="request" value="${pageContext.request}" />
		<jsp:setProperty name="witnessPage0" property="response" value="${pageContext.response}" />

		<%witnessPage0.init();
			if(!witnessPage0.isErrorLoading()){
				response.sendRedirect("../public/publicCodices.xhtml");
			}else{
		%>
		
		
	<script type="text/javascript">
	
	$(document).ready(function () {
		 
	    function getURLParams()
	    {
	        var urlParams = {},
	            match,
	            pl     = /\+/g,  // Regex for replacing addition symbol with a space
	            search = /([^&=]+)=?([^&]*)/g,
	            decode = function (s) { return decodeURIComponent(s.replace(pl, " ")); },
	            query  = window.location.search.substring(1);
	        while (match = search.exec(query))
	        {
	            urlParams[decode(match[1])] = decode(match[2]);
	        }

	        return urlParams;
	    }
	    
	    $('#table-titles-in-codex').on('click', '.show-title-details', function(ev){
	    	var dv = $('#diva-wrapper').data('diva');
	        var start_page = $(this).data('start');
	        
	        if(start_page){
	        	dv.gotoPageByNumber(start_page);
		        ev.preventDefault();	
	        }else{
	        	alert("No page assigned to this title.");	
	        }
	    	var titleId = $(this).data('title-id');
	    	showTitleDetailsSmall(titleId);
	        var witnessId = $(this).data('witness-id');
	    	showWitnessDetailsSmall(witnessId);
	    });	
	    
	    $('#additional-information').on('click',
	    '.title-details-show-more', function(ev){
	    	var titleId = $(this).data('title-id');
	    	showTitleDetailsBig(titleId);
	    });	
	    
	    $('#additional-information').on('click', '.title-details-show-less', function(ev){
	    	var titleId = $(this).data('title-id');
	    	showTitleDetailsSmall(titleId);
	    });	
	    
	    $('#additional-information2').on('click', '.witness-details-show-more', function(ev){
	    	var witnessId = $(this).data('witness-id');
	    	showWitnessDetailsBig(witnessId);
	    });	
	    
	    $('#additional-information2').on('click', '.witness-details-show-less', function(ev){
	    	var witnessId = $(this).data('witness-id');
	    	showWitnessDetailsSmall(witnessId);
	    });		
	    
	    $('.ismi-fullscreen-icon').on('click', function(ev) {	
			
	    	var jaja = $( this ).data('in-fullscreen-mode');
	    	var panel = $( "#attributesPanel" );
	    	if(jaja == true){
	    		//panel.css('backgroundColor','#EE178C');
				panel.css('left','0');
				panel.css('max-height','100%');
				panel.css('max-width','100%');
				panel.css('position','fixed');
				panel.css('top','0');
				panel.css('width','100%');
				panel.css('z-index','102');
				panel.css('overflow','scroll');
				
				//panel.data('in-fullscreen-mode') = !panel.data('in-fullscreen-mode');
				$( this ).data('in-fullscreen-mode', false);
				//alert("Fue true");
	    	}
	    	if(jaja == false){
	    		
				panel.css('left','');
				panel.css('max-height','');
				panel.css('max-width','');
				panel.css('position','');
				panel.css('top','0');
				panel.css('width','');
				panel.css('z-index','');
				panel.css('overflow','');
	    		
	    		$( this ).data('in-fullscreen-mode', true);
	    	}
		});
	
		
	    $('#page-jump').on('keypress', function(ev)
	    {
	        if (ev.which == '13')
	        {
	            var dv = $('#diva-wrapper').data('diva');
	            var value = $(this).val();
	            var success = dv.gotoPageByNumber(value);

	            if (!success)
	            {
	                if (!$('#page-jump-group').hasClass('error'))
	                {
	                    $('#page-jump-group').addClass('error');
	                    $('#page-jump-controls').append('<span id="jump-error-help" class="help-inline">The page you specified is not valid.</span>');
	                }
	            }
	            else
	            {
	                if ($('#page-jump-group').hasClass('error'))
	                {
	                    $('#page-jump-group').removeClass('error');
	                    $('#jump-error-help').remove();
	                }
	            }

	        }
	    });

	    $('.go-to-witness-link').on('click', function(ev) {
	        var dv = $('#diva-wrapper').data('diva');
	        var start_page = $(this).data('start');
	        
	        if(start_page){
	        	dv.gotoPageByNumber(start_page);
		        ev.preventDefault();	
	        }else{
	        	alert("No page assigned to this witness.");	
	        }
	        
	        var witnessId = $(this).data('witness-id');
	        showWitnessDetailsSmall(witnessId);
	        
	    });

	    function handlePageSwitch(idx, fn, divid)
	    {
	        // page number is what we're after, which is always
	        // page index + 1.
	        $('#current-page-idx').text(idx + 1);
	        $('#current-page-fn').text(fn);
	    }
		
	    function handleDocumentLoaded(idx, fn)
	    {
	        var witnesses = {};

	        // we could do this with an ajax request, but we have
	        // the variables already here, we just need to get them
	        // from Django and not JS. 
	        
	        var urlParams = getURLParams();
	        if (urlParams.hasOwnProperty('witness'))
	        {
	            urlWitness = parseInt(urlParams['witness'], 10);
	            
	            // this won't be populated if the witness doesn't have a
	            // start page set.
	            if (witnesses.hasOwnProperty(urlWitness))
	            {
	                this.gotoPageByNumber(witnesses[urlWitness]);
	            }
	        }
	    }			
		
		$("#diva-wrapper").diva(
		{
			enableAutoHeight: true,
	        enableAutoTitle: false,
	        enableGotoPage: false,
	        fixedHeightGrid: false,
	        contained: true,
	        iipServerURL: divaGlobal.iipServerURL,
	        digiId: "<%=witnessPage0.getDigiId()%>",
	        objectData: divaGlobal.rest_url + "/rest/diva/proxy/json/<%=witnessPage0.getDigiLabel()%>",
	        imageDir: "/data7/srv/images/<%=witnessPage0.getDigiLabel()%>",
			onSetCurrentPage : handlePageSwitch,
			onDocumentLoaded : handleDocumentLoaded,
			goDirectlyTo: <%=witnessPage0.getStartPage()%>,
			inFullscreen: <%=witnessPage0.getImageFullscreen()%>,
			zoomLevel: 1,
            canvasPlugin: {
                proxyURL: divaGlobal.rest_url + "/rest/diva/proxy/image"
            }			
		});
		


	var witnessId = <%=witnessPage0.getWitnessId()%>;
	showWitnessDetailsBig(witnessId);
	});
	
	
</script>
	<%	} %>
</head>

<body bgcolor=white>

	<% if(witnessPage0.isErrorLoading()) { %>
	<input type='hidden' name='csrfmiddlewaretoken' value='psDBHsF5a1mttLSKeUgoUUVN3HL6f8Re' />
	<jsp:include page="header.jsp" />
	
	<!-- 
	<div class="magnifiedAttributePanel">
	</div>
	 -->
	
	<div class="custom-container">

 
		<div class="row-fluid">
			<div id="attributesPanel" class="span4 page" style="min-height: 700px;">
				
				<a class="ismi-fullscreen-icon" href="#" data-in-fullscreen-mode="true"></a>
				
				<div class="attPanel">
	        
					<c:if test="${witnessPage0.getDigi() != null}">
					
						<input type="hidden" id="digi_id" value="${witnessPage0.getDigi().getId()}">
						
						<div class="divaBackgroud">
							
							<!-- XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX -->
							
							<h4><%=witnessPage0.getDigiLabel()%></h4>
							
							<c:if test="${witnessPage0.getWitnessList().size() > 0}">
								<span class="titlePanel">Titles in this Codex</span>
								
								<table id="table-titles-in-codex" class="table table-bordered table-condensed divaPanel">
					           		<thead>
					               		<tr>
					                   		<th class="tableHead">Title</th>
					                   		<th class="tableHead">Folios</th>
					               		</tr>
					           		</thead>
					           		<tbody>
						           		<c:forEach var="witness" items="${witnessPage0.getWitnessList()}">
						    			<tr>      
						       				<td class="tdTitle">
						       					<!-- href="${witnessPage0.getAppBean().getRoot()}/public/dynamicPage.xhtml?eid=${witness.titleId}" -->
						       					<a class="show-title-details" 
						       						data-title-id="${witness.titleId}"
											       	data-witness-id="${witness.id}"
						       						data-start="${witness.startPage}">
											 		<c:out value="${witness.title}"/>
											 	</a>
						       					
						       				</td>
						       				<td class="columnCentered">
						       					<c:if test="${witness.startPage != null}">
							       					<a 	class="witness-new-window"  data-witness-id="${witness.id}" data-start="${witness.startPage}" 
							       						href="${witnessPage0.getAppBean().getRoot()}/public/publicCodex.jsp?eid=${witnessPage0.getCurrentEntId()}&startPage=${witness.startPage}&imgFullscreen=true"
							       						target="_blank">
														<img src="../resources/images/new_window-16.png">
													</a>
												</c:if>
						       					<a class="go-to-witness-link"  data-witness-id="${witness.id}" data-start="${witness.startPage}" href="#">
													<c:out value="${witness.folios}"/>
												</a>
												
						       				</td>
						    			</tr>
						    			</c:forEach>
					           		</tbody>				
								</table>															
							</c:if>
							
							<c:if test="${witnessPage0.getUnknownList().size() > 0}">
								<h4>Unknown titles in this Codex</h4>
								<c:forEach var="witness" items="${witnessPage0.getUnknownList()}">
									<a class="go-to-witness-link" data-witness-id="${witness.id}" data-start="${witness.startPage}" href="#">
										<c:out value="${witness.folios}"/>
									</a>
								</c:forEach>
								<br>
							</c:if>
							
						
							<div id="additional-information">
							</div>
							
							<div id="additional-information2">
							</div>
							
							<span class="titlePanel">Codex Information</span>
							<table class="table table-bordered table-condensed divaPanel">
								<tbody>							
									<c:forEach var="attLabel" items="${witnessPage0.getLabels()}">
										<tr>
											<td class="tdTitle">
												<c:out value="${attLabel}"/>
											<td>
											<td >
												<table class="tableContent">
													<tbody>
														<c:forEach var="attValue" items="${witnessPage0.getAttMap().get(attLabel)}">
															<tr>
																<td style="text-align: ${witnessPage0.getAttMapTextAlign().get(attLabel)};">
																	<c:out value="${attValue}"/>
																</td>
															</tr>
														</c:forEach>
													</tbody>
												</table>										
											<td>
										</tr>
									</c:forEach>
									<tr><td class="tdTitle">References</td><td></td><td><table class="tableContent">
									<tbody>
                                        <c:forEach var="ref" items="${witnessPage0.getReferenceList().keySet()}" varStatus="loop">
                                        <tr>
                                          <td id="bibl-entry-codex-${loop.index}">
                                            <c:out value="${ref}"/>
                                            <script>showBibliographyEntryFormatted("${ref}", null, "#bibl-entry-codex-${loop.index}")</script>
                                          </td>
                                        </tr>
										<tr>
										<td  style="text-align">	<c:out value="${witnessPage0.getReferenceList()[ref]}"/>
										</td>
										</tr>

										</c:forEach>
									</tbody>
								</table>
									</td></tr>	
								</tbody>
							</table>
							
						</div>		
					</c:if>			
				</div>
			</div>
			<div class="span8">
				<input type='hidden' name='csrfmiddlewaretoken' value='psDBHsF5a1mttLSKeUgoUUVN3HL6f8Re' />
				<div id="diva-wrapper" style="width: 600px;"></div>
			</div>
		</div>
	</div>
  
	<% } %>
</body>
</html>
