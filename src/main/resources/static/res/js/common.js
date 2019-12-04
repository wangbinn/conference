
layui.config({
	dir : '../res/lib/layui/', //layui.js 所在路径（注意，如果是script单独引入layui.js，无需设定该参数。），一般情况下可以无视
	version : false, //一般用于更新组件缓存，默认不开启。设为true即让浏览器不缓存。也可以设为一个固定的值，如：201610
	debug : false, //用于开启调试模式，默认false，如果设为true，则JS模块的节点会保留在页面
	base : '' //设定扩展的Layui组件的所在目录，一般用于外部组件扩展
});

//不刷新页面的情况下,修改Url
function addParamToUrl(pattern, data) {
	let oldUrl = window.location.href;
	if(oldUrl.indexOf(pattern + "=") != -1){
		return;
	}
	let newUrl;
	if(oldUrl.indexOf('?') == -1){
		newUrl = oldUrl + '?' + pattern + "=" + data;
	}else{
		newUrl = oldUrl + '&' + pattern + "=" + data;
	}
	history.pushState(null, null, encodeURI(newUrl));
}

function clearParam() {
	let oldUrl = window.location.href;
	let index = oldUrl.indexOf('?');
	if (index != -1) {
		let newUrl = oldUrl.substr(0, index);
		//		history.pushState(null, null, newUrl);
		history.replaceState(null, null, newUrl);
	}
}

function getUrlParam(key) {
	var result = {};
	var paramStr = decodeURI(window.document.location.search);
	if (paramStr && paramStr.indexOf("?") != -1) {
		var params = paramStr.substring(1).split('&');
		for (var p = 0; p < params.length; p++) {
			result[params[p].split('=')[0]] = unescape(params[p].split('=')[1]);
		}
	}
	return result[key];
}


function isEmpty(obj) {
	if (typeof obj == "undefined" || obj == null || obj == "") {
		return true;
	} else {
		return false;
	}
}

function getFormJsonObj(selector){
	if(isEmpty($(selector)) || isEmpty($(selector).serializeArray())){
		return null;
	}
	let formData = $(selector).serializeArray();
    var obj={}
    for (var i in formData) {
        obj[formData[i].name]=formData[i]['value'];
    }
    return obj;
}

