var table;
var tables;
var layer;

layui.use('table', function() {
	var table = layui.table;
	// 第一个实例
	tables = table.render({
		elem : '#conference',
		// limit : 10,
		// limits : [ 10, 20, 30, 40 ],
		url : "http://localhost:8080/admin/list", // 数据接口
		page : false, // 开启分页
		// where : {},
		cols : [ [ // 表头
		{
			field : 'id',
			align : 'center',
			width : '5%',
			sort : true,
			title : 'ID'
			//hide : true
		},{
			field : 'roomNumber',
			align : 'center',
			width : '7%',
			sort : true,
			title : '房间号'
		}, {
			field : 'floor',
			align : 'center',
			width : '6%',
			sort : true,
			title : '楼层'
		}, {
			field : 'seatNumber',
			align : 'center',
			width : '7%',
			sort : true,
			title : '座位数'
		}, {
            field : 'multimedia',
            align : 'center',
            width : '7%',
            sort : true,
            title : '多媒体',
			templet: function (item) {
				if (item.multimedia == '0') {
					return "无";
				} else if (item.multimedia == '1') {
					return "有";
				} else {
					return "";
				}
			}
		}, {
		    field : 'principalId',
            align : 'center',
            width : '7%',
            sort : true,
            title : '负责人ID',
			hide : true
		}, {
			field : 'name',
			align : 'center',
			width : '7%',
			sort : true,
			title : '负责人'
        }, {
            field : 'phone',
            align : 'center',
            width : '10%',
            sort : true,
            title : '手机号'
        }, {
            field : 'mail',
            align : 'center',
            width : '14%',
            sort : true,
            title : '邮箱'
        }, {
            field : 'scheduledStartTime',
            align : 'center',
            width : '14%',
            sort : true,
            title : '最近预定开始时间'
        }, {
            field : 'scheduledEndTime',
            align : 'center',
            width : '14%',
            sort : true,
            title : '最近预定结束时间'
		}, {
			title : '操作',
			Width : '9%',
			fixed : 'right',
			align : 'center',
			toolbar : '#conferenceActive'
		} ] ],
		done : function() {
			clearParam();
		}
	});

	$("#searchBtn1").click(function() {
		tableReload();
	});

	table.on('tool(conference)', function(obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"

		let data = obj.data; //获得当前行数据
		let layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
		let tr = obj.tr; //获得当前行 tr 的DOM对象

		if (layEvent === 'update') { //更新
			layer.open({
				type : 2,
				title : '修改会议室信息: ' + data.roomNumber,
				// btn : [ '关闭' ],
				area : [ '66%', '72%' ],
				content : './meetingRoomUpdate.html?id=' + data.id
					+ "&principalId=" + data.principalId + "&floor=" + data.floor
					+ "&roomNumber=" + data.roomNumber + "&seatNumber=" + data.seatNumber + "&multimedia=" + data.multimedia,
				success : function(data) {
				},
				yes : function(index) {
					layer.close(index);
				}
			});
		}
		else if (layEvent === 'del') { //删除
				layer.confirm('真的删除行么', function(index) {
					$.ajax({
						type : "POST",
						url :"http://localhost:8080/admin/deleteById",
						data : {
							id : data.id
						},
						dataType : "json",
						success : function(data) {
							tableReload(tables.config.page.curr);
							obj.del(); //删除对应行（tr）的DOM结构，并更新缓存
							layer.close(index);
							layer.msg(data.msg);
						},
						error : function(e) {
							console.log(e);
						}
					});

				});
			}
	});

});

layui.use('layer', function() {
	layer = layui.layer;
});

layui.use('form', function() {
	var form = layui.form;
});

layui.use('element', function() {
	var element = layui.element;
});

function tableReload(pageNum) {
	tables.reload({
		where : {
			roomNumber : $("#searchWithRoomNumber").val()
		}
		// ,
		// page : {
		// 	curr : pageNum || 1
		// //重新从第 1 页开始
		// }
	});
}

function toMeetingRoomCreate() {
	loadModule("./meetingRoomCreate.html");
}
