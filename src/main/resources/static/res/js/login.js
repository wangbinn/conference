
layui.use('form', function() {
	var form = layui.form;

	form.on('submit(formDemo)', function(data) {
		$.ajax({
			url : "http://localhost:8080" + "/loginAuth",
			dataType : "json",
			data : {
				userJson : JSON.stringify(data.field)
			},
			type : "POST",
			success : function(data) {
				if (data.status == 0) {
					// addParamToUrl("userName", "123");
					window.location.href = "./layout.html?userName="+data.name;
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
