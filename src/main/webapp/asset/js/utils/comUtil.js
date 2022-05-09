/********************************************************************************* 
* comUtil.js
* 공통 Util (프로젝트와 별도로 어디서든 사용 할 수 있는 함수 모음)
**********************************************************************************/

var loadingbarCnt = 0;
if (window.console) {}

function gfn_ajax(ajaxMap) {
	var tmpSuccess 	= ajaxMap.success; 	//성공시 처리로직
	var tmpError	= ajaxMap.error; 	//실패시 처리로직

	ajaxMap.success = function(data) {
		tmpSuccess(data);
	};

	ajaxMap.error = function(jqXHR, textStatus, errorThrown) {
		var status = gfn_nvl(jqXHR.status, -100);

		if(status == 500) {
//			alert(MSG_COM_ERR_001);				// 시스템 오류가 발생하였습니다.\n시스템관리자에게 문의 바랍니다.
		} else if(status == 404) {
//			alert(MSG_COM_ERR_024);			// 해당 화면 URL을 찾을 수 없습니다.
		} else if(status == 440) {
//			alert(M101659);						// 세션이 만료 되었습니다.\n로그인 페이지로 이동됩니다.
//			parent.location.href = CONTEXTROOT + "/login/login.do";
		} else {
//			alert(MSG_COM_ERR_001);				// 시스템 오류가 발생했습니다. /n관리자에게 문의하세요.
		}

		gfn_unblockUI();
		tmpError;
	};

	ajaxMap.data.GV_MENU_CD = $("#gvForm #GV_MENU_CD").val();

	ajaxMap.data = JSON.stringify(ajaxMap.data);
	ajaxMap.type = "post";
	ajaxMap.headers = {
			"Accept" : "application/json",
			"Content-Type" : "application/json",
			"AJAX" : true
	};

	$.ajax(ajaxMap);
}

/**
 * 로딩바 표출
 */
function gfn_blockUI() {
	if(loadingbarCnt < 0) {
		loadingbarCnt = 0;
	}

	loadingbarCnt++;

	$.blockUI({message : "<img src='" + "/asset/images/common/loading.png' />",
		css : { backgroundColor: 'rgba(0,0,0,0.0)', color: '#000000', border: '0px solid #000', cursor:'wait' , "z-index" : '99999'},
		overlayCSS : { backgroundColor: 'rgba(0,0,0,0.0)', color: '#000000', opacity:0.0, cursor:'wait' }
	});
}


/**
 * 로딩바 숨기기
 */
function gfn_unblockUI() {
	if(loadingbarCnt == 1) {
		$.unblockUI();
		loadingbarCnt--;
	} else {
		loadingbarCnt--;
	}
}

/**
 * 현재창이 팝업인지 확인하여 부모창을 리턴한다.
 * 팝업이 아니면 null을 리턴
 * @returns
 */
function gfn_getCurWindow() {
	var win = null;

	if(opener) {
		if(opener.opener) {
			win = opener.opener;
		} else {
			win = opener;
		}
	}

	return win;
}

/**
 * 입력값의 바이트 길이를 리턴
 * ex) if (getByteLength(form.title) > 100) {
 * 		alert("제목은 한글 50자(영문 100자) 이상 입력할 수 없습니다.");
 *     }
 * @param input
 * @returns {Number}
 */
function gfn_getByteLength(input) {
	var byteLength = 0;

	if ( input == null ) return 0;

	for (var inx = 0; inx < input.length; inx++) {
		var oneChar = escape(input.charAt(inx));

		if ( oneChar.length == 1 ) {
			byteLength ++;
		} else if (oneChar.indexOf("%u") != -1) {
			byteLength += 2;
		} else if (oneChar.indexOf("%") != -1) {
			byteLength += oneChar.length/3;
		}

	} // enf of for loop

	return byteLength;
}

/**
 * 문자열 바이트로 자르기
 */
function cutByLen(str, maxByte) {

	for(b=i=0;c=str.charCodeAt(i);) {

		b+=c>>7?2:1;

		if (b > maxByte)

			break;

		i++;

	}

	return str.substring(0,i);
}

/**
 * NULL 체크 (obj 가 boolean 형일때 체크가 안되어 변경함)
 * @param obj
 * @returns {Boolean}
 */
function gfn_isNull(obj) {
	var sObj = obj == undefined ? "Y" : "N";

	if(sObj == "N") {
		if (obj.toString() == undefined || obj.toString() == null || obj.toString() == "" || obj.toString() == 'undefined') {
			return true;
		} else {
			if(obj.toString() == "true") {
				return true;
			} else {
				return false;
			}
		}
	} else {
		return true;
	}
}

/**
 * obj가 NULL 이면 rObj로 변환
 * @param obj
 * @param rObj
 * @returns
 */
function gfn_nvl(obj,rObj) {
	if (gfn_isNull(obj)) {
		return rObj;
	} else {
		return obj;
	}
}

/**
 * 마지막 문자열 삭제
 * @param str
 * @returns
 */
function removeLastStr(str) { return str.substring(0, str.length - 1); }

$.fn.serializeObject = function() {
	var o = {};

	$(this).find('input[type="hidden"], input[type="text"], input[type="password"], input[type="checkbox"]:checked, input[type="radio"]:checked, select, textarea').each(function() {

		if ($(this).attr('type') == 'hidden') { //If checkbox is checked do not take the hidden field
			var $parent = $(this).parent();
			var $chb = $parent.find('input[type="checkbox"][name="' + this.name.replace(/\[/g, '\[').replace(/\]/g, '\]') + '"]');

			if ($chb != null) {
				if ($chb.prop('checked')) return;
			}
		}

		if (this.name === null || this.name === undefined || this.name === '')
			return;

		var elemValue = null;

		if ($(this).is('select')) {
			if(!gfn_isNull($(this).attr("multiple"))) {
				elemValue = $(this).multipleSelect('getSelects').join();
			} else {
				elemValue = $(this).find('option:selected').val();
			}
		} else {
			//datepicker 처리
			if ($(this).hasClass("calendar")) {
				elemValue = this.value.replace(/-/g, '');
			} else if ($(this).hasClass("numberType") || $(this).hasClass("numberCommaType")) {
				elemValue = this.value.replace(/,/g, '');
			} else {
				elemValue = this.value;
			}
		}

		if (o[this.name] !== undefined) {
			if (!o[this.name].push) {
				o[this.name] = [o[this.name]];
			}
			o[this.name].push(elemValue || '');
		} else {
			o[this.name] = elemValue || '';
		}
	});

	return o;
};

/**
 * replaceAll 함수
 * @param str
 * @param targetStr
 * @param replace
 * @returns
 */
function replaceAll(str, targetStr, replace) {

	var str0 = gfn_nvl(str, "");
	var strr = "";

	// 값이 유효할 때만 처리
	if(str0 != "") {

		strr = str.split(targetStr).join(replace);
	}

	return strr;
}

/**
 * 1 ~ 9 숫자를 0을 붙여 두자리로 만드는 함수
 * @param str
 * @returns {String}
 */
function addZero(str) {

	var strr;

	try {

		// 숫자로 변환
		var num = Number(str);

		// 9와 대소비교
		strr    = num > 9 ? str : "0" + num;

	} catch(Err) { strr = ""; }

	return strr;
}

/**
 * 파일 확장자 체크
 * @param fileName
 * @param arrExt
 * @returns {Boolean}
 */
function gfn_fileExt(fileName, arrExt) {
	var ext = fileName.split(".");

	if($.inArray(ext[ext.length - 1], arrExt) == -1) {
		return false;
	} else {
		return true;
	}
}

/**
 * 파일 사이즈 체크
 * @param obj
 * @param size
 * @param unit
 * @returns {Boolean}
 */
function gfn_fileSize(obj, size, unit) {

	var maxSize  = 0;
	var fileSize = obj.size;

	if(unit == "b") {
		maxSize = size;
	} else if(unit == "kb") {
		maxSize = size * 1024;
	} else if(unit == "mb") {
		maxSize = size * 1024 * 1024;
	}

	if(fileSize > maxSize) {
		return false;
	} else {
		return true;
	}
}

/**
 * 콤마 추가
 */
function comma(obj) {
	obj = obj.toString();
	var regx    = new RegExp(/(-?[0-9]+)([0-9]{3})/gi);
    var bExists = obj.indexOf(".", 0);
    var strArr  = obj.split('.');

    while (regx.test(strArr[0])) {
        strArr[0] = strArr[0].replace(regx, "$1,$2");
    }

    if (bExists > -1) {
        obj = strArr[0] + "." + strArr[1];
    } else {
        obj = strArr[0];
    }
    return obj;

}

/**
 * 콤마 삭제
 */
function uncomma(str) {
	var bExists = str.indexOf(".", 0);
    var strArr  = str.split('.');

    if (bExists > -1) {
    	str = strArr[0].replace(/[^\d]+/g, '') + "." + strArr[1].replace(/[^\d]+/g, '');
    } else {
    	str = strArr[0].replace(/[^\d]+/g, '');
    }
    return str
}