
layui.use('form', function() {
	var form = layui.form;

	form.on('submit(formDemo)', function(data) {
		$.ajax({
			url : "http://localhost:8080" + "/register",
			dataType : "json",
			data : {
				userJson : JSON.stringify(data.field)
			},
			type : "POST",
			success : function(data) {
				if (data.status == 0) {
					layer.msg(data.msg,{time:2*1000},function () {
						window.location.href = "./login.html";
					});
				} else if (data.status != 0) {
					layer.msg(data.msg);
				}
			},
			error : function() {

			}
		});
		return false;
	});
});

layui.use('element', function() {
	var element = layui.element;
});

layui.use('layer', function() {
	var layer = layui.layer;
});
