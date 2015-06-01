$(document).ready(function() {
	handleGlobalAjaxError();//in ajax.js file
	
	//var url="cities/all";
	//$.ajax({type: 'GET', url: url, success: function(data){
	//	var cities = data;
	//	renderComboBox("#city", cities);
	//}});
});

function selectKPP(domKpp){
	var $domKpp = $(domKpp);
	var kppUid = $domKpp.val();
	var url=contextPath + "/planforsales/findBykppUid/"+kppUid;
	if (kppUid!=-1){
		$.ajax({type: 'GET', url: url, success: function(data){	
		renderComboBox("#kenhPhanPhoi", data);
		}});
	}
	else{
		renderComboBoxEmpty("#kenhPhanPhoi");
	}	
}


function selectHH(domHH){
	var $domHH = $(domHH);
	var HHUid = $domHH.val();
	var url=contextPath + "/planforsales/findGoodsByTypeGoodsId/"+HHUid;
	if (HHUid!=-1){
		$.ajax({type: 'GET', url: url, success: function(data){	
		renderComboBox("#hangHoa\\.id", data);
		}});
	}
	else{
		renderComboBoxEmpty("#hangHoa\\.id");
	}	
}


function selectStaff(domStaff){
	var $domStaff = $(domStaff);
	var staffId = $domStaff.val();
	var url=contextPath + "/planforsales/findByStaff/"+staffId;
	if (staffId!=-1){
		$.ajax({type: 'GET', url: url, success: function(data){	
		}});
	}
	else{
		//renderComboBoxEmpty("#kenhPhanPhoi");
	}	
}


function selectDshh(){
	
	var hangHoa = document.getElementById('hangHoa');
	var hangHoaId = hangHoa.options[hangHoa.selectedIndex].value;
	
	var toserial = document.getElementById("toserial").value
	
	var serial = document.getElementById("serial").value
	
	var url=contextPath + "/planforsales/findGoodsSerialByGoodsId/"+hangHoaId+"/"+serial+"/"+toserial+"/";
	
	
	
	if (hangHoaId!=-1){
		$.ajax({type: 'GET', url: url, success: function(data){
			//renderComboBox("#abc", data);
			document.getElementById('contentdshh').innerHTML = data;
		}});
	}
	else{
		//renderComboBoxEmpty("#abc");
		document.getElementById('contentdshh').innerHTML = '222222222222222222';
	}	
}


function renderComboBox(comboBoxSelector, items){
	//re-render provinces
	var $domCbo = $(comboBoxSelector);
	$domCbo.empty();//remove old options
	for ( var i = 0; i < items.length; i++) {
		var item = items[i];
		var $domOption = $("<option value = '"+item.uid+"'>"+item.name+"</option>");
		$domCbo.append($domOption);
	}
}
function renderComboBoxEmpty(comboBoxSelector){
	//re-render provinces
	var $domCbo = $(comboBoxSelector);
	$domCbo.empty();//remove old options
}