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

			var form = {
				template : 'simple.fo',
				content: output,
				render: 'IMAGE'
			};
	

			$.post( "http://localhost:8080/Meteora/FopServer", form).done(function( data ) {
  				$('#js_preview_image').attr('src', 'http://localhost:8080/Meteora/renders/' + data);
  			});
		},

		download = function (){
			var template = $('#js_title_template').html();

			var value = $('#js_title').val();
			var tokens = {
				title : value
			};

			var output = tash.to_html(template, tokens);

			var form = {
				template : 'simple.fo',
				content: output,
				render: 'PDF'
			};

			$.post( "http://localhost:8080/Meteora/FopServer", form).done(function( data ) {
  				window.open('http://localhost:8080/Meteora/renders/' + data);
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

		    $('#js_download').click(download);
		};

		return {
			init : init
		}
};