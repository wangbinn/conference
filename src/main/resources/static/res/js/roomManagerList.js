var table;
var tables;
var layer;

layui.use('table', function() {
	var table = layui.table;
	// 第一个实例
	tables = table.render({
		elem : '#manager',
		// limit : 10,
		// limits : [ 10, 20, 30, 40 ],
		url : "http://localhost:8080/prin/getPrincipal", // 数据接口
		page : false, // 开启分页
		// where : {},
		cols : [ [ // 表头
		{
			field : 'id',
			align : 'center',
			width : '15%',
			sort : true,
			title : 'ID'
			//hide : true
		},{
		    field : 'name',
            align : 'center',
            width : '20%',
            sort : true,
            title : '负责人'
        }, {
            field : 'phone',
            align : 'center',
            width : '20%',
            sort : true,
            title : '手机号'
        }, {
            field : 'mail',
            align : 'center',
            width : '20%',
            sort : true,
            title : '邮箱'
        }, {
			title : '操作',
			Width : '25%',
			fixed : 'right',
			align : 'center',
			toolbar : '#managerActive'
		} ] ],
		done : function() {
			clearParam();
		}
	});

	$("#searchBtn1").click(function() {
		tableReload();
	});

	table.on('tool(manager)', function(obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"

		let data = obj.data; //获得当前行数据
		let layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
		let tr = obj.tr; //获得当前行 tr 的DOM对象

		if (layEvent === 'update') { //更新
			// layer.open({
			// 	type : 2,
			// 	title : '修改会议室信息: ' + data.name,
			// 	btn : [ '关闭' ],
			// 	area : [ '76%', '90%' ],
			// 	content : './articleView.html?articleId=' + article.id,
			// 	time: 5000,
			// 	success : function(data) {
			// 	},
			// 	yes : function(index) {
			// 		layer.close(index);
			// 	}
			// });
			layer.open({
				type : 2,
				title : '修改负责人信息: ' + data.name,
				// btn : [ '关闭' ],
				area : [ '66%', '72%' ],
				content : './roomManagerUpdate.html?id=' + data.id + "&name=" + data.name
					+ "&phone=" + data.phone + "&mail=" + data.mail,
				success : function(data) {
				},
				yes : function(index) {
					layer.close(index);
				}
			});
		}
		else if (layEvent === 'del') { //删除
				layer.confirm('真的删除负责人么', function(index) {
					$.ajax({
						type : "POST",
						url :"http://localhost:8080/prin/deleteById",
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

	// table.on('rowDouble(conference)', function(obj) {
	// 	let article = obj.data;
	// 	layer.open({
	// 		type : 2,
	// 		title : '查看文章: ' + article.name,
	// 		btn : [ '关闭' ],
	// 		area : [ '76%', '90%' ],
	// 		content : './articleView.html?articleId=' + article.id,
	// 		time: 5000,
	// 		success : function(data) {
	// 		},
	// 		yes : function(index) {
	// 			layer.close(index);
	// 		}
	// 	});
	// });

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
		// where : {
		// 	roomNumber : $("#searchWithRoomNumber").val()
			// endDate : $("#searchWithEndDate").val()
			// status : status
		// }
		// ,
		// page : {
		// 	curr : pageNum || 1
		// //重新从第 1 页开始
		// }
	});
}

function toRoomManagerCreate() {
	loadModule("./roomManagerCreate.html");
}
