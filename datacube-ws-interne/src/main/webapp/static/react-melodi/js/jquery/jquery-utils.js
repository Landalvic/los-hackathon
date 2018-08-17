jQuery.fn.listeTexte = function(params){
	var $this = this;
	var listeTexte_less = function($tr) {
		var index = $tr.attr('data-index');
		if(index != 1){
			$tr.remove();
		} else {
			$tr.find('input').val('');
		}
	};
	var listeTexte_more = function($tr){
		newTr();
	};
	var newTr = function(value) {
		var $tr = $('<tr>');
		var $input = $('<input>',{ 'type' : 'text'}).val(value);
		$tr.append($('<td>', { 'class' : 'texte'}).append($input))				
			.append($('<td>', { 'class' : 'cellule-icone' }).append($('<div>', { 'class' : 'minus' })).click(function(){
				listeTexte_less($tr);
			}))
			.append($('<td>', { 'class' : 'cellule-icone'}).append($('<div>', { 'class' : 'plus' })).click(function(){
				listeTexte_more($tr);
			}));
		$table.append($tr);
		resetIndex();
		$input.click(function(){
			$this.data("focused", $(this));			
		});
		$this.data("focused", $input);
		$input.focus();
	};
	
	if(typeof params == 'object'){
		params = $.extend({values : []}, params);
		$(this).empty();
		
		var $table = $('<table>', { 'class' : 'liste-texte' });
		var resetIndex = function(){
			var index = 1;
			$table.find('tr').each(function(){
				$(this).attr('data-index', index++);
			});
		};
		
		
		this.append($table)
		newTr('');
		
		if(params.values.length > 0){
			for(var i=0;i<params.values.length;i++){
				var val = params.values[i];
				if(i == 0){
					$table.find('input').val(val);
				}else{
					newTr(val);
				}
			}
		}
		
		return this;
	} else if(params === 'values'){
		var values = [];
		this.find('input').each(function(){
			var val = $(this).val();
			if(!val.isEmpty()){
				values.push(val);
			}
		});
		return values;
	} else if(params === 'reset'){
		var first = true;
		this.find('table tr').each(function(){
			if(first){
				first = false;
				$('input').val('');
			} else {
				$(this).remove();
			}
		});
	} else if(params === 'focused'){
		return $this.data('focused');
	}
};

jQuery.fn.zoneSaisie = function(params){
	var message = function(lg, size){
		if(typeof lg != 'undefined' && lg == 'fr') {
			$div.text(fr.replace('$0', size).replace('$1', params.maxlength));
		} else {
			$div.text(en.replace('$0', size).replace('$1', params.maxlength));
		}
	}
	
	if(typeof params == 'object'){
		params = $.extend({ maxlength : 2000, id : 'id-textarea', 'clazz' : 'champ-formulaire', rows : 4 }, params);
		
		var $textarea = $('<textarea>', { 'class' : params.clazz, rows : params.rows, maxlength :  params.maxlength });
		var $div = $('<div>', { 'class' : 'text-email champ-formulaire' });
		
		var fr = '$0 caractères sur $1 caractères autorisés.', en = '$0 characters on $1 characters allowed.';
		message('fr', 0);
		
		$textarea.bind('input propertychange', function(){
			var l = $(this).val().length;				
			message('fr', l);
		});
		
		this.append($textarea);
		this.append($div);
		
		$textarea.attr('id', params.id);
	} else if(typeof params == 'string') {
		if(params == 'value') {
			return this.find('textarea').val();
		} else if(params == 'effacer') {
			this.find('textarea').val('');
			message(langue, 0);
		}
	}
};

String.prototype.stripAccent = function(){
    var accent = [
        /[\300-\306]/g, /[\340-\346]/g, // A, a
        /[\310-\313]/g, /[\350-\353]/g, // E, e
        /[\314-\317]/g, /[\354-\357]/g, // I, i
        /[\322-\330]/g, /[\362-\370]/g, // O, o
        /[\331-\334]/g, /[\371-\374]/g, // U, u
        /[\321]/g, /[\361]/g, // N, n
        /[\307]/g, /[\347]/g, // C, c
    ];
    var noaccent = ['A','a','E','e','I','i','O','o','U','u','N','n','C','c'];
     
    var str = this;
    for(var i = 0; i < accent.length; i++){
        str = str.replace(accent[i], noaccent[i]);
    }
     
    return str;
};

String.prototype.isEmpty = function() {
    return (this.length === 0 || !$.trim(this));
};

String.isBlank = function(chaine){
	var is = true;
	if(typeof chaine){
		is = chaine == null || chaine === '';
	}
	return is;
};


(function($){
  $.isBlank = function(obj){
    return(!obj || $.trim(obj) === '');
  };
  
  $.urlParam = function(name){
	    var results = new RegExp('[\?&]' + name + '=([^&#]*)').exec(window.location.href);
	    if (results==null){
	       return null;
	    }
	    else{
	       return decodeURIComponent(results[1]) || 0;
	    }
	};

	$.urlParamsList = function(){
		var params = {};

		if (location.search) {
		    var parts = location.search.substring(1).split('&');

		    for (var i = 0; i < parts.length; i++) {
		        var nv = parts[i].split('=');
		        if (!nv[0]) continue;
		        params[nv[0]] = decodeURIComponent(nv[1]) || true;
		    }
		}
		
		return params;
	};
	
	String.isBlank = function(chaine){
		var is = true;
		if(typeof chaine){
			is = chaine == null || chaine === '';
		}
		return is;
	};
	
	jQuery.fn.listeChooser = function(data){
		var context = this;
		if(data == 'refresh'){
			this.filtrage($(this).val());
		}else if(data == 'reset'){
			this.resetFiltrage();
		}else{
			// initialisation
			var settings = $.extend({ $ul : $("<ul>"), visible : function(){ return true; }, change : function(){} }, data),
				li = settings.$ul.find("li"),
				options = [];
			li.each(function(){
				options.push({libelle : $(this).text().trim().toUpperCase().stripAccent(), li : this});
			});
			
			this.$searchBar = this;
			
			this.autocomplete({
				source : function(request, response){
					context.filtrage(request.term);
				}
			});
		
			this.$searchBar.keyup(function(event, ui) {	
				if($(this).val().length == 0){
					context.resetFiltrage();
				}
			});
		
			this.filtrage = function(term){	
				var termEx = term.trim().toUpperCase().stripAccent();
				
				$(options).each(function(){
					if(settings.visible(this.li) && this.libelle.indexOf(termEx) != -1){
						$(this.li).show();
					}else{
						$(this.li).hide();
					}
				});
			};
			
			this.resetFiltrage = function(){
				$(options).each(function(){
					if(settings.visible(this.li)){
						$(this.li).show();
					}
				});	
			};
		}
		
		return this;
	};

	jQuery.fn.commutateur = function(params){
		if(typeof params == 'object'){
			params = $.extend({
				on : function(){},
				off : function(){}
			}, params);

			this.empty();
			var $img = $('<img>', {
				'class' : 'icone-large left',
				'src' : '/static/icons/switch-on.svg',
				'data-state' : 'on',
				'alt' : 'switch-on',
				'height' : '42',
				'width' : '42'
			}).click(function(){
				var state = $(this).attr('data-state');
				if(state == 'off'){
					$(this).attr('data-state', 'on');
					$img.attr('src', '/static/icons/switch-on.svg');
					params.on.call(this);
				} else if(state == 'on'){
					$(this).attr('data-state', 'off');
					$img.attr('src', '/static/icons/switch-off.svg');					
					params.off.call(this);
				}
			});
			this.append($img);
		} else if(params == 'on'){
			$(this).attr('data-state', 'on');
			$(this).find('img').attr('src', '/static/icons/switch-on.svg');
		} else if(params == 'off'){
			$(this).attr('data-state', 'off');
			$(this).find('img').attr('src', '/static/icons/switch-off.svg');
		}	
	};
	
	jQuery.fn.commutateurListe = function(params){
		if(typeof params == 'object'){
			params = $.extend({
				on : function(){},
				off : function(){},
				label : { on : 'oui', off : 'non' },
				id : null
			}, params);

			this.empty();
			var $img = $('<select>', {})
				.append($('<option>',{ value : 'on' }).text(params.label.on))
				.append($('<option>',{ value : 'off' }).text(params.label.off))
				.change(function(){
					var state = $(this).val();
					if(state == 'on'){
						params.on.call(this);
					} else if(state == 'off'){					
						params.off.call(this);
					}
				});
			if(params.id != null){
				$img.attr('id', params.id);
			}
			this.append($img);
		} else if(params === 'on'){
			$(this).find('select').val('on');
		} else if(params === 'off'){
			$(this).find('select').val('off');
		}	
	};
	
	
	
  
})(jQuery);

