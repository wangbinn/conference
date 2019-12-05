//显示管理员名
document.getElementById('admin_name').innerHTML = getUrlParam("adminName");

layui.use([ 'element', 'layer' ], function() {
	"user strict";
	var element = layui.element;
	var layer = layui.layer;

	window.logout = function() {
		layer.confirm('是否退出登录', function(index) {
			$.ajax({
				type : "GET",
				url : "http://localhost:8080" + "/logout",
				dataType : "json",
				success : function(data) {
					if (data.status == 0) {
						window.location.href = './adminLogin.html';
						layer.msg(data.msg);
					} else {
						layer.msg(data.msg);
					}
				},
				error : function(e) {
					console.log(e);
				}
			});
			layer.close(index);
		});
	}

});

function loadModule(module) {
	$("#conferenceManageContainer").load(module);
}

$(document).ready(function() {
	$("#meetingRoomListModule").click();
});