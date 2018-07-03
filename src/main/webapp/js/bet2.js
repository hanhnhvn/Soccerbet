function isNumber(n) {
	return !isNaN(parseFloat(n)) && isFinite(n);
};

$(document).bind("mobileinit", function() {
	$.ajaxSetup({ cache: false });
	$.mobile.page.prototype.options.domCache = false;
	$.mobile.page.prototype.options.addBackBtn = true;
	
	jQuery('div').live('pagehide', function(event, ui){
		  var page = jQuery(event.target);

		  if(page.attr('data-cache') == 'never'){
		    page.remove();
		  };
	});

	
	$("a.editBet").live("click", function(e){
		e.stopImmediatePropagation();
		e.preventDefault();
		var li = $(this).parent().parent();
		li.find("div.placeBetContainer").show();
		li.find("div.readBetContainer").hide();
	});
	
	$("form#registerForm").live("submit", function(e) {
		e.stopImmediatePropagation();
		e.preventDefault();
		
		var error = false;
		$(this).find("input").each(function(index) {
			var value = $(this).val();
			
			if (value == "" || value.length < 4) {
				$(this).css("border-color","#ff0000");
				error = true;
			} else {
				$(this).css("border-color","");
			}
		});
		
		if (!error) {
			var queryString = $(this).serialize();
			$.mobile.showPageLoadingMsg();
			
			$.ajax({
				type : 'POST',
				url : 'register',
				data : queryString,
				success : function(data) {
					if (data == "success") {
						$("#registrationFormContainer").hide();
						$("#registrationErrorContainer").hide();
						$("#registrationErrors").empty();
						$("#registrationSuccessContainer").show();
						$.mobile.hidePageLoadingMsg();
					} else {
						$("#registrationErrorContainer").show();
						$("#registrationErrors").html(data);
						$.mobile.hidePageLoadingMsg();
					}
				},
				error : function() {
					$.mobile.hidePageLoadingMsg();
					alert("Sorry, please try again.");
				},
				dataType : 'text'
			});
		}
	});
	
	$("a.placeBet").live("click", function(e){ 
		e.stopImmediatePropagation();
		e.preventDefault();
		$(this).parent("form.betForm").submit();
	});
	
	$("a[rel='external']").live("click", function(e) {
		if ($(this).href) {
			e.stopImmediatePropagation();
			e.preventDefault();
			location.href = this.href;
		}
	});
	
	$("form.betForm").live("submit", function(e){ 
		e.stopImmediatePropagation();
		e.preventDefault();
		
		var form = $(this);
		var queryString = form.serialize();
		var error = false;
		
		form.find("input").each(function(index) {
			var value = parseInt($(this).val());
			
			if ($(this).attr("type") != "submit" && (!isNumber($(this).val()) || !(value >= 0))) {
				$(this).css("border-color","#ff0000");
				error = true;
			} else {
				$(this).css("border-color","");
			}
		});
		
		if (!error) {
			$.mobile.showPageLoadingMsg();   

			$.ajax({
				type : 'POST',
				url : ajax_bet_url,
				cache: false,
				data : queryString,
				success : function(data) {
					if (data == "true") {
						var li = form.parent().parent();
						var scoreString = form.children("input[name=home]").val() + " : " + form.children("input[name=away]").val();
						li.find("div.placeBetContainer").hide();
						li.find("div.readBetContainer").show().find("span.readOnlyBetScore").text(scoreString);
						$.mobile.hidePageLoadingMsg();
					} else {
						$.mobile.hidePageLoadingMsg();
						alert("Sorry, please try again.");
					}
				},
				error : function() {
					$.mobile.hidePageLoadingMsg();
					alert("Sorry, please try again.");
				},
				dataType : 'text'
			});
		}
	});   
});
