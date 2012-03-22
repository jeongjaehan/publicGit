/**
 * 전화번호 입력 검증및 치환
 * @param str
 * @returns
 */
function mphon( obj ) {
	obj.value =  PhonNumStr( obj.value );
} 
function _mphon( val ) {
	document.write(PhonNumStr( val ));
}
function PhonNumStr( str ){
	var RegNotNum  = /[^0-9]/g;
	var RegPhonNum = "";
	var DataForm   = "";

	// return blank    
	if( str == "" || str == null ) return "";

	// delete not number
	str = str.replace(RegNotNum,'');

	/* 4자리 이하일 경우 아무런 액션도 취하지 않음. */
	if( str.length < 4 ) return str;
	/* 지역번호 02일 경우 10자리 이상입력 못하도록 제어함. */
	if(str.substring(0,2)=="02" && str.length > 10){
		str = str.substring(0, 10);
	}
	if( str.length > 3 && str.length < 7 ) { 
		if(str.substring(0,2)=="02"){
			DataForm = "$1-$2";
			RegPhonNum = /([0-9]{2})([0-9]+)/; 

		} else {
			DataForm = "$1-$2";
			RegPhonNum = /([0-9]{3})([0-9]+)/; 
		}
	} else if(str.length == 7 ) {
		if(str.substring(0,2)=="02"){
			DataForm = "$1-$2-$3";
			RegPhonNum = /([0-9]{2})([0-9]{3})([0-9]+)/; 
		} else {
			DataForm = "$1-$2";
			RegPhonNum = /([0-9]{3})([0-9]{4})/; 
		}
	} else if(str.length == 9 ) {
		if(str.substring(0,2)=="02"){
			DataForm = "$1-$2-$3";
			RegPhonNum = /([0-9]{2})([0-9]{3})([0-9]+)/; 
		} else {
			DataForm = "$1-$2-$3";
			RegPhonNum = /([0-9]{3})([0-9]{3})([0-9]+)/; 
		}
	} else if(str.length == 10){ 
		if(str.substring(0,2)=="02"){
			DataForm = "$1-$2-$3";
			RegPhonNum = /([0-9]{2})([0-9]{4})([0-9]+)/; 
		}else{
			DataForm = "$1-$2-$3";
			RegPhonNum = /([0-9]{3})([0-9]{3})([0-9]+)/;
		}
	} else if(str.length > 10){ 
		if(str.substring(0,2)=="02"){
			DataForm = "$1-$2-$3";
			RegPhonNum = /([0-9]{2})([0-9]{4})([0-9]+)/; 
		}else{
			DataForm = "$1-$2-$3";
			RegPhonNum = /([0-9]{3})([0-9]{4})([0-9]+)/; 
		}
	} else {  
		if(str.substring(0,2)=="02"){
			DataForm = "$1-$2-$3";
			RegPhonNum = /([0-9]{2})([0-9]{3})([0-9]+)/; 
		}else{
			DataForm = "$1-$2-$3";
			RegPhonNum = /([0-9]{3})([0-9]{3})([0-9]+)/;
		}
	}

	while( RegPhonNum.test(str) ) { 
		str = str.replace(RegPhonNum, DataForm); 
	}
	return str;
}


!function( $ ){

	  "use strict"
	
}