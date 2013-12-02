window = window || {};
window.Adgistics = window.Adgistics || {};

window.Adgistics.Annotations = function(JQuery, Mustache) {

	var $ = JQuery,
		tash = Mustache,
		timeoutReference = null,

		generatePreview = function (value){
			var template = $('#js_title_template').html();

			var tokens = {
				title : value
			};

			var output = tash.to_html(template, tokens);

	

			$.post( "http://localhost:8080/meteora/fop", { content: output})
  .done(function( data ) {
  	 	var ticks = ((new Date().getTime() * 10000) + 621355968000000000);

  		$('#js_preview_image').attr('src', $('#js_preview_image').attr('src')+'?'+ticks);
  });


		},

		init = function() {

		    $('#js_title').keypress(function() {
		        var _this = $(this);
		        
		        if (timeoutReference) {
		        	clearTimeout(timeoutReference);
		        }
		        timeoutReference = setTimeout(function() {
		            generatePreview(_this.val());
		        }, 500);
		    });
		};

		return {
			init : init
		}
};