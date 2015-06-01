
function selectCity(domCity){
	var $domCity = $(domCity);
	var cityUid = $domCity.val();
	var url=contextPath + "/town/findByCityUid/"+cityUid;
	if (cityUid!=0){
		$.ajax({type: 'GET', url: url, success: function(data){	
		renderComboBox("#quanHuyen", data, true);
		}});
	}
	else{
		renderComboBoxEmpty("#quanHuyen");
	}	
	renderComboBoxEmpty("#phuongXa");
	renderComboBoxEmpty("#tuyenDuong");
	renderComboBoxEmpty("#listDiemBH");
}
function selectTown(domTown){
	var $domTown = $(domTown);
	var townUid = $domTown.val();
	if (townUid!=0){
		var url= contextPath + "/ward/findByTownUid/"+townUid;
		$.ajax({type: 'GET', url: url, success: function(data){	
			renderComboBox("#phuongXa", data, true);	
		}});
	}
	else{
		renderComboBoxEmpty("#phuongXa", true);
	}	
	renderComboBoxEmpty("#tuyenDuong", true);
	renderComboBoxEmpty("#listDiemBH");
}
function selectWard(domWard){
	var $domWard = $(domWard);
	var wardUid = $domWard.val();
	if (wardUid!=0){
		var url=contextPath + "/street/findByWardUid/"+wardUid;
		$.ajax({type: 'GET', url: url, success: function(data){	
			renderComboBox("#tuyenDuong", data, true);
		}});
	}
	else{
		renderComboBoxEmpty("#tuyenDuong", true);
	}	
	renderComboBoxEmpty("#listDiemBH");	
}
function selectStreet(domStreet){
	var $domStreet = $(domStreet);
	var $domSearch = $("#timKiem")
	var streetUid = $domStreet.val();
	var search=$domSearch.val();
	if (search.length==0)
		search='#';
	if (streetUid!=0){
		var url=contextPath + "/sale/findByStreetUid/"+streetUid+"/"+search;
		$.ajax({type: 'GET', url: url, success: function(data){	
			renderComboBox("#listDiemBH", data);
		}});
	}
	else{
		renderComboBoxEmpty("#listDiemBH");	
	}	
}
function refreshAgent(){
	var $domCity = $("#tinhThanhPho");
	var $domTown = $("#quanHuyen");
	var $domWard = $("#phuongXa");
	var $domStreet = $("#tuyenDuong");
	var $domSearch = $("#timKiem");
	var cityUid = $domCity.val();
	var townUid = $domTown.val();
	var wardUid = $domWard.val();
	var streetUid = $domStreet.val();
	var search=$domSearch.val();
	var url='';
	if (parseInt(cityUid) =="0" ){
		/*if (search.length==0)
			url=contextPath + "/sale/getFilter";
		else
			url=contextPath + "/sale/getFilter/"+search;
		$.ajax({type: 'GET', contentType: 'application/x-www-form-urlencoded; charset=UTF-8', url: url, success: function(data){	
			renderComboBox("#listDiemBH", data);
		}});*/
		alert('Vui lòng chọn một tỉnh thành phố')
	}
	if (townUid=="0" && wardUid==null && streetUid==null && parseInt(cityUid) > 0){
		if (search.length==0) {
			url=contextPath + "/sale/findByCityUid/"+cityUid;
			method = 'GET';
			param = "";
		} else {
			url=contextPath + "/sale/findByCityUid/"+cityUid;
			method = 'POST';
			param = "search=" +search;
		}
		showLoading();
		$.ajax({type: method, contentType: 'application/x-www-form-urlencoded; charset=UTF-8',url: url, data:param, success: function(data){	
			renderComboBox("#listDiemBH", data);
		}});
	}	
	if  (wardUid=="0" && streetUid==null && parseInt(cityUid) > 0 && parseInt(townUid) > 0){
		if (search.length==0)
			url=contextPath + "/sale/findByTownUid/"+townUid;
		else
			url=contextPath + "/sale/findByTownUid/"+townUid+"/"+search;
		
		showLoading();
		$.ajax({type: 'GET', contentType: 'application/x-www-form-urlencoded; charset=UTF-8', url: url, success: function(data){	
			renderComboBox("#listDiemBH", data);
		}});
	}
	if  (streetUid=="0" && parseInt(cityUid) > 0 && parseInt(townUid) > 0 && parseInt(wardUid) > 0){
		if (search.length==0)
			url=contextPath + "/sale/findByWardUid/"+wardUid;
		else
			url=contextPath + "/sale/findByWardUid/"+wardUid+"/"+search;
		
		showLoading();
		$.ajax({type: 'GET', contentType: 'application/x-www-form-urlencoded; charset=UTF-8',url: url, success: function(data){	
			renderComboBox("#listDiemBH", data);
		}});
	}
	if  (parseInt(cityUid) > 0 && parseInt(townUid) > 0 && parseInt(wardUid) > 0 && parseInt(streetUid) > 0){
		if (search.length==0)
			url=contextPath + "/sale/findByStreetUid/"+streetUid;
		else
			url=contextPath + "/sale/findByStreetUid/"+streetUid+"/"+search;
		
		showLoading();
		$.ajax({type: 'GET', contentType: 'application/x-www-form-urlencoded; charset=UTF-8', url: url, success: function(data){	
			renderComboBox("#listDiemBH", data);
		}});
	}	
}

function renderComboBox(comboBoxSelector, items, reindex){
	//re-render provinces
	var $domCbo = $(comboBoxSelector);
	$domCbo.empty();//remove old options
	for ( var i = 0; i < items.length; i++) {
		var item = items[i];
		var $domOption = $("<option value = '"+item.uid+"'>"+item.name+"</option>");
		$domCbo.append($domOption);
	}
	if (reindex) {
		$domCbo.select2();
	}
	hideLoading();
	
}
function renderComboBoxEmpty(comboBoxSelector, reindex){
	//re-render provinces
	var $domCbo = $(comboBoxSelector);
	$domCbo.empty();//remove old options
	if (reindex) {
		$domCbo.select2();
	}
	hideLoading();
}

//====================
function refreshFilter(){
	
	var $domTown = $("#quanHuyen");
	var $domWard = $("#phuongXa");
	var $domStreet = $("#tuyenDuong");
	var $domSearch = $("#timKiem");
	var cityUid = "1263";
	var townUid = $domTown.val();
	var wardUid = $domWard.val();
	var streetUid = $domStreet.val();
	var search=$domSearch.val();
	var url='';

	if (townUid=="0" && wardUid==null && streetUid==null && parseInt(cityUid) > 0){
		if (search.length==0)
			url=contextPath + "/sale/findByCityUid/"+cityUid;
		else
			url=contextPath + "/sale/findByCityUid/"+cityUid+"/"+search;
		$.ajax({type: 'GET', contentType: 'application/x-www-form-urlencoded; charset=UTF-8',url: url, success: function(data){	
			renderComboBox("#listDiemBH", data);
		}});
	}	
	if  (wardUid=="0" && streetUid==null && parseInt(cityUid) > 0 && parseInt(townUid) > 0){
		if (search.length==0)
			url=contextPath + "/sale/findByTownUid/"+townUid;
		else
			url=contextPath + "/sale/findByTownUid/"+townUid+"/"+search;
		$.ajax({type: 'GET', contentType: 'application/x-www-form-urlencoded; charset=UTF-8', url: url, success: function(data){	
			renderComboBox("#listDiemBH", data);
		}});
	}
	if  (streetUid=="0" && parseInt(cityUid) > 0 && parseInt(townUid) > 0 && parseInt(wardUid) > 0){
		if (search.length==0)
			url=contextPath + "/sale/findByWardUid/"+wardUid;
		else
			url=contextPath + "/sale/findByWardUid/"+wardUid+"/"+search;
		$.ajax({type: 'GET', contentType: 'application/x-www-form-urlencoded; charset=UTF-8',url: url, success: function(data){	
			renderComboBox("#listDiemBH", data);
		}});
	}
	if  (parseInt(cityUid) > 0 && parseInt(townUid) > 0 && parseInt(wardUid) > 0 && parseInt(streetUid) > 0){
		if (search.length==0)
			url=contextPath + "/sale/findByStreetUid/"+streetUid;
		else
			url=contextPath + "/sale/findByStreetUid/"+streetUid+"/"+search;
		$.ajax({type: 'GET', contentType: 'application/x-www-form-urlencoded; charset=UTF-8', url: url, success: function(data){	
			renderComboBox("#listDiemBH", data);
		}});
	}	
}