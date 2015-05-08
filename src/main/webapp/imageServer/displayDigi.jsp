<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="org.mpi.openmind.repository.bo.Entity"%>
<%@page import="de.mpiwg.itgroup.diva.jsp.JSPDigitalization"%>

<html>
	<head>
		<title>ismi - image server</title>
		
		
		<link href="resources/css/bootstrap.css" type="text/css" rel="stylesheet" />
		<link href="resources/css/bootstrap-responsive.min.css" type="text/css" rel="stylesheet">
		<link href="resources/css/style.css" type="text/css" rel="stylesheet">
		<link href="resources/css/diva.min.css" type="text/css" rel="stylesheet" />
		<link href="resources/css/diva4ismi.css" type="text/css" rel="stylesheet" />
		
		<script type="text/javascript" src="resources/js/jquery.min.js"></script>
		<script type="text/javascript" src="resources/js/diva.min.js"></script>
		<script type="text/javascript" src="resources/js/bootstrap.min.js"></script>
		<script type="text/javascript" src="resources/js/typeahead.js"></script>
		<script type="text/javascript" src="resources/js/diva4ismi.js"></script>
		
		<jsp:useBean id="digiBean" class="de.mpiwg.itgroup.diva.jsp.JSPDigitalization" scope="session" />
		<jsp:setProperty name="digiBean" property="request" value="${pageContext.request}" />
		<jsp:setProperty name="digiBean" property="response" value="${pageContext.response}" />

		<%digiBean.init();%>
		<% if(!digiBean.hasLogin()){ %>
			<jsp:forward page="../imageServer/displayDigiList.xhtml?login=true" />
		<% } %>

<script type="text/javascript">
	$(document).ready(function () {
		
		$("#witness-edit").on('click', '.update-witness', function(ev){
			
			var witnessId = $(this).data('witness');
			
			var startPageInput = $( "#input-start-page-"+witnessId );
			var endPageInput = $( "#input-end-page-"+witnessId );
			var digiIdInput = $("#digi_id");
			
			var data = {};
			data["digi_id"] = digiIdInput[0].value;
			
			data["witness"] = {};
			data["witness"]["id"] = "" + witnessId;
			data["witness"]["start_page"] = startPageInput[0].value;
			data["witness"]["end_page"] = endPageInput[0].value;
			
			var witnessLink = $("#witness-link-" + witnessId);
			witnessLink.data('start', startPageInput[0].value);
			
			$.ajax(
					divaGlobal.rest_url + "/rest/witness/update?data=" + JSON.stringify(data), {
			         type: 'GET',
			            contentType: "application/json",
				        success: function(data, textStatus, jqXHR){
				        	var state = data["state"];
				        	if(state == "ok"){
				        		alert('Executed successfully');	
				        	}else{
				        		alert(data["message"]);
				        	}
				        },
				        error: function(jqXHR, textStatus, errorThrown){
				        	alert('Error: ' + textStatus);
				        }
			        });
			
			
		});
		
		/************************************************/
 
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
	        
	    });

	    function modifyWitness(witness_id, key, value, successCallback, caller) {
	        var csrf = $("[name=csrfmiddlewaretoken]").val();
	        var witnessURL = "/witness/" + witness_id;
	        var data = {};
	        data[key] = value;

	        $.ajax(witnessURL, {
	            type: 'PATCH',
	            headers: {
	                'X-CSRFToken': csrf
	            },
	            data: JSON.stringify(data),
	            contentType: "application/json",
	            success: function(data, status, xhr)
	            {
	                // we'll need to operate on the button, so pass it back...
	                successCallback(data, caller);
	            }
	        });
	    }

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
	        digiId: "<%=digiBean.getDigiId()%>",
	        objectData: divaGlobal.rest_url + "/rest/diva/proxy/json/<%=digiBean.getDigiLabel()%>",
	        imageDir: "/data7/srv/images/<%=digiBean.getDigiLabel()%>",
			onSetCurrentPage : handlePageSwitch,
			onDocumentLoaded : handleDocumentLoaded,
			zoomLevel: 1,
            canvasPlugin: {
                proxyURL: divaGlobal.rest_url + "/rest/diva/proxy/image"
            }			
		});
	});
	
	
</script>
</head>

<body bgcolor=white>

	<jsp:include page="header.jsp" />
	<div class="custom-container">

		<div class="row-fluid">
			<div class="span4 page">
				<div style="height: 800px; overflow: scroll;">
				
				
            <input type='hidden' name='csrfmiddlewaretoken' value='psDBHsF5a1mttLSKeUgoUUVN3HL6f8Re' />
            
					<c:if test="${digiBean.getDigi() != null}">
					
						<input type="hidden" id="digi_id" value="${digiBean.getDigi().getId()}">
						
						<div class="divaBackgroud">
							
							<!-- XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX -->
							
							<h4><%=digiBean.getDigiLabel()%></h4>
							
							<c:if test="${digiBean.getWitnessList().size() > 0}">
								<span class="titlePanel">Titles in this Codex</span>
								
								<table class="table table-bordered table-condensed divaPanel">
					           		<thead>
					               		<tr>
					                   		<th class="tableHead">Title</th>
					                   		<th class="tableHead">Folios</th>
					               		</tr>
					           		</thead>
					           		<tbody>
						           		<c:forEach var="witness" items="${digiBean.getWitnessList()}">
						    			<tr>      
						       				<td class="tdTitle">
						       					<c:out value="[${witness.id}] ${witness.title}"/>
												<a 	target="_blank"
													href="<c:out value='../search/displayTitle.xhtml?witnessId=${witness.id}#witnesses'/>">
													<img src="../resources/images/display_32.png" alt="Display witness" width="20" height="20" >
												</a>
												<a target="_blank"
													href="<c:out value='../browse/entityDetails.xhtml?eid=${witness.id}'/>">
		            					 			<img src="../resources/images/network_32.png" alt="show witness"
	            					 				width="20" height="20">
												</a>
												<c:if test="${digiBean.canEdit()}">
													<a target="_blank"
														href="<c:out value='../entry/createEntity.xhtml?eid=${witness.id}'/>">
														<img src="../resources/css/xp/css-images/edit.gif" alt="Edit codex" >
													</a>								
												</c:if>
						       				</td>
						       				<td class="columnCentered">
						       					<a id="witness-link-${witness.id}" class="go-to-witness-link" data-start="${witness.startPage}" href="#">
													<c:out value="${witness.folios}"/>
												</a>
						       				</td>
						    			</tr>
						    			</c:forEach>
					           		</tbody>				
								</table>															
							</c:if>
							
							<h4>Unknown titles in this Codex</h4>
							<c:forEach var="witness" items="${digiBean.getUnknownList()}">
								<a id="witness-link-${witness.id}" class="go-to-witness-link" data-start="${witness.startPage}" href="#">
									<c:out value="${witness.folios}"/>
								</a>
							</c:forEach>
							
							<!-- XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX -->
						
							<h4>Edit Folio/Image Correspondence</h4>
							
							
		            		<div id="page-jump-group" class="control-group">
		                		<label for="page-jump" class="control-label">Go to page: </label>
		                		<div class="controls" id="page-jump-controls">
		                    		<input id="page-jump" type="text" class="form-control">
		                		</div>
		            		</div>
						
							<p>
								<strong>Current Page Index:</strong> <span id="current-page-idx"></span>
							</p>
							<p>
								<strong>Page Filename:</strong> <span id="current-page-fn"></span>
							</p>					
							
							<table>
								<tr>
									<td>
										<span class="titlePanel">[${digiBean.getDigi().getId()}] Digitalization</span>
										<c:if test="${digiBean.canEdit()}">										
											<a href="<c:out value='../entry/createEntity.xhtml?eid=${digiBean.getDigi().getId()}'/>">
												<img src="../resources/css/xp/css-images/edit.gif" alt="Edit digitalization" >
											</a>
										</c:if>
										<table class="divaPanel">
						                	<tr>
						                		<td class="tdTitle">Name</td>
						                		<td class="tdContent"><c:out value="${digiBean.getName()}"></c:out> </td>	                			
						                	</tr>	
						                	<tr>
						                		<td class="tdTitle">Number of files</td>
						                		<td class="tdContent"><c:out value="${digiBean.getNumFiles()}"></c:out> </td>
						                	</tr>									
										</table>
									</td>
								</tr>
								<tr>
									<td>
										<c:if test="${digiBean.getCodex() == null}">
											<span class="titlePanel">Codex not yet assigned!</span>
										</c:if>
										
										<c:if test="${digiBean.getCodex() != null}">
											<span class="titlePanel">[${digiBean.getCodex().getId()}] Codex</span>
											<c:if test="${digiBean.canEdit()}">
												<a href="<c:out value='../entry/createEntity.xhtml?eid=${digiBean.getCodex().getId()}'/>">
													<img src="../resources/css/xp/css-images/edit.gif" alt="Edit codex" >
												</a>		
											</c:if>
											<table class="divaPanel">
						                		<tr>
						                			<td class="tdTitle">Own Value</td>
						                			<td class="tdTitle"><c:out value="${digiBean.getCodex().getOv()}"></c:out> </td>	                			
						                		</tr>	
						                		<tr>
						                			<td class="tdTitle">Shelf Mark</td>
						                			<td class="tdContent"><c:out value="${digiBean.getCodex().getIdentifier()}"></c:out> </td>
						                		</tr>	
						                		<tr>
						                			<td class="tdTitle">Collection</td>
						                			<td class="tdContent"><c:out value="${digiBean.getCodex().getCollection()}"></c:out> </td>             			
						                		</tr>
						                		<tr>
						                			<td class="tdTitle">Repository</td>
						                			<td class="tdContent"><c:out value="${digiBean.getCodex().getRepository()}"></c:out> </td>
						                		</tr>		
						                		<tr>
						                			<td class="tdTitle">City</td>
						                			<td class="tdContent"><c:out value="${digiBean.getCodex().getCity()}"></c:out> </td>		
						                		</tr>	
						                		<tr>
						                			<td class="tdTitle">Country</td>
						                			<td class="tdContent"><c:out value="${digiBean.getCodex().getCountry()}"></c:out> </td>         			
						                		</tr>		                			                		                				                			                			                									
											</table>
											
											<c:if test="${digiBean.getWitnessList().size() > 0}">
												<span class="titlePanel">Witnesses in Codex</span>
												<table id="witness-edit" class="table table-bordered table-condensed divaPanel">
							                		<thead>
							                    		<tr>
							                        		<th></th>
							                        		<th class="tableHead">Start</th>
							                        		<th class="tableHead">End</th>
							                    		</tr>
							                		</thead>
							                		<tbody>
								                		<c:forEach var="witness" items="${digiBean.getWitnessList()}">
								        				<tr>      
								            				<td>
								            					<table class="witnessTableDetail">
								            						<tr>
								            							<td class="tdTitle">
								            								<c:out value="[${witness.id}]"/>
								            							</td>
								            						</tr>								            					
								            						<tr>
								            							<td class="tdTitle">
								            								<c:out value="${witness.title}"/>
								            							</td>
								            						</tr>
								            						<tr>
								            							<td class="tdTitle">
								            								<c:out value="Folios: ${witness.folios}"/>
								            							</td>
								            						</tr>		            						
								            						<tr>
								            							<td >
								            								<a href="<c:out value='../browse/entityDetails.xhtml?eid=${witness.id}'/>">
									            					 			<img src="../resources/images/network_32.png" alt="show witness"
									            					 				width="20" height="20">
																			</a>
																			<c:if test="${digiBean.canEdit()}">
																				<a href="<c:out value='../entry/createEntity.xhtml?eid=${witness.id}'/>">
																					<img src="../resources/css/xp/css-images/edit.gif" alt="Edit codex" >
																				</a>								
																			</c:if>											
								            							</td>
								            						</tr>           						
								            					</table>
								            				</td>	
								            				
								            				<td class="columnCentered">
								            					<input 
								            						id="input-start-page-<c:out value="${witness.id}"/>"
								            						data-witness="<c:out value="${witness.id}"/>"
								            						type="text"
								            						readonly="readonly"
								            						value="<c:out value="${witness.startPage}"/>" class="inputPageNumber">
								            					<c:if test="${digiBean.canEdit()}">
																	<button class="set-start-set ismi-button" data-witness="<c:out value="${witness.id}"/>">Set Start</button>
																	<button class="set-start-reset ismi-button" data-witness="<c:out value="${witness.id}"/>">Reset Start</button>
																</c:if>
								            				</td>
								            				
								            				<td class="columnCentered"> 
								            					<input 
								            						id="input-end-page-<c:out value="${witness.id}"/>"
								            						data-witness="<c:out value="${witness.id}"/>"
								            						type="text"
								            						readonly="readonly"
								            						value="<c:out value="${witness.endPage}"/>" class="inputPageNumber">
								            					<c:if test="${digiBean.canEdit()}">	
																	<button class="set-end-set ismi-button" data-witness="<c:out value="${witness.id}"/>">Set Start</button>
																	<button class="set-end-reset ismi-button" data-witness="<c:out value="${witness.id}"/>">Reset Start</button>
																</c:if>
								            				</td>
								            				<c:if test="${digiBean.canEdit()}">
									            				<td class="columnCentered">
									            					<button class="update-witness ismi-button" data-witness="<c:out value="${witness.id}"/>">Update</button>
									            				</td>
								            				</c:if>
								        				</tr>
								        				</c:forEach>
							                		</tbody>						
												</table>
											</c:if>
										
										</c:if>								
									
									</td>
								</tr>
							</table>
						</div>		
					</c:if>			


				</div>
			</div>
			<div class="span8">
				<input type='hidden' name='csrfmiddlewaretoken' value='psDBHsF5a1mttLSKeUgoUUVN3HL6f8Re' />
				<div id="diva-wrapper"></div>
			</div>
		</div>
	</div>
  

</body>
</html>
