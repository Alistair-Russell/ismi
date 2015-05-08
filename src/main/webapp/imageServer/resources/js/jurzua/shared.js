
$(document).ready(function () {
	
	alert('asdfsafdasffsd');


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
           dv.gotoPageByNumber(start_page);

           ev.preventDefault();
       });

       $("#witness-edit").on('click', '.set-start-image-btn', function(ev)
       {
           var dv = $('#diva-wrapper').data('diva');
           var witness = $(this).data('witness');
           var page = dv.getCurrentPageNumber();

           var startSetCallback = function(data, caller)
           {
               var parent = caller.parent();
               var el = $("<span>" + data.start_page + "(<a href='#' class='remove-start-image' data-witness='" + data.ismi_id + "'>X</a>)</span>");
               caller.remove();
               parent.append(el);
           }

           modifyWitness(witness, 'start_page', page, startSetCallback, $(this));
       });

       $("#witness-edit").on('click', '.set-end-image-btn', function(ev)
       {
           var dv = $('#diva-wrapper').data('diva');
           var witness = $(this).data('witness');
           var page = dv.getCurrentPageNumber();

           var endSetCallback = function(data, caller)
           {
               var parent = caller.parent();
               var el = $("<span>" + data.end_page + "(<a href='#' class='remove-end-image' data-witness='" + data.ismi_id + "'>X</a>)</span>");
               caller.remove();
               parent.append(el);
           }

           modifyWitness(witness, 'end_page', page, endSetCallback, $(this));
       });

       $("#witness-edit").on('click', '.remove-start-image', function(ev)
       {
           var witness = $(this).data('witness');
           var startRemoveCallback = function(data, caller)
           {
               var parentTd = caller.parent().parent();
               var parent = caller.parent();
               var el = $("<button class='btn set-start-image-btn' data-witness='" + data.ismi_id + "'>Set Start Image</button>");
               parent.remove();
               parentTd.append(el);
           }

           modifyWitness(witness, 'start_page', null, startRemoveCallback, $(this));
       });

       $("#witness-edit").on('click', '.remove-end-image', function(ev)
       {
           var witness = $(this).data('witness');

           var endRemoveCallback = function(data, caller)
           {
               var parentTd = caller.parent().parent();
               var parent = caller.parent();
               var el = $("<button class='btn set-end-image-btn' data-witness='" + data.ismi_id + "'>Set End Image</button>");
               parent.remove();
               parentTd.append(el);
           }

           modifyWitness(witness, 'end_page', null, endRemoveCallback, $(this));
       });

       $("#test-highlight").on('click', function(ev)
       {
           var dv = $("#diva-wrapper").data('diva');
           dv.highlightOnPage('foo', 'bar', 'baz');
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
       /*
       {% for witness in content.witnesses %}
           {% if witness.start_page %}
               witnesses[{{ witness.ismi_id }}] = {{ witness.start_page }};
           {% endif %}
       {% endfor %}*/

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
               iipServerURL: "https://images.rasi.mcgill.ca/fcgi-bin/iipsrv.fcgi",
               imageDir: "/data7/srv/images/Majlis_shura_21",

               onSetCurrentPage: handlePageSwitch,
               onDocumentLoaded: handleDocumentLoaded
           });
});