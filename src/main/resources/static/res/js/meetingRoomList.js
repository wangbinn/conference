var table;
var tables;
var layer;
// var status; //文章状态,作为查询条件

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
			width : '7%',
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
			Width : '8%',
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
			// addParamToUrl("id", data.id);
			// loadModule("./bookingOK.html");
			// layer.open({title: '提示', icon: '1', content:result.msg,time:2000,end:function(){
			// 		parent.layer.closeAll();
			// 	}});
			let article = obj.data;
			layer.open({
				type : 2,
				title : '修改会议室信息: ' + article.name,
				btn : [ '关闭' ],
				area : [ '76%', '90%' ],
				content : './articleView.html?articleId=' + article.id,
				time: 5000,
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

	table.on('rowDouble(conference)', function(obj) {
		let article = obj.data;
		layer.open({
			type : 2,
			title : '查看文章: ' + article.name,
			btn : [ '关闭' ],
			area : [ '76%', '90%' ],
			content : './articleView.html?articleId=' + article.id,
			time: 5000,
			success : function(data) {
			},
			yes : function(index) {
				layer.close(index);
			}
		});
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

//年-月-日
function nowDate(){
	var now = new Date();
	return now.getFullYear()+"-" + (now.getMonth()+1) + "-" + now.getDate();
}
//时-分-秒
function nowDateTime(){
	var now = new Date();
	return now.getFullYear()+"-" + (now.getMonth()+1) + "-" + now.getDate() + " " + now.getHours()+":"+now.getSeconds()+":"+now.getMinutes();
}

// layui.use('laydate', function() {
// 	var laydate = layui.laydate;
//
// 	//日期范围
// 	var startDate=laydate.render({
// 		elem: '#searchWithStartDate',
// 		type: 'datetime',
// 		istime: true,
// 		istoday: true,
// 		min:nowDateTime(),
// 		done:function(value,date){
// 			if(value!=""){
// 				date.month=date.month-1;
// 				endDate.config.min=date;
// 			}else{
// 				endDate.config.min=startDate.config.min;
// 			}
// 		},
// 	});
// 	var endDate =laydate.render({
// 		elem: '#searchWithEndDate',
// 		type: 'datetime',
// 		istime: true,
// 		istoday: true,
// 		done:function(value,date){
// 			if(value!=""){
// 				date.month=date.month-1;
// 				startDate.config.max=date;
// 			}else{
// 				startDate.config.max=endDate.config.max;
// 			}
// 		}
// 	});
// });

function tableReload(pageNum) {
	tables.reload({
		where : {
			startDate : $("#searchWithStartDate").val(),
			endDate : $("#searchWithEndDate").val()
			// status : status
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

function showPublished() {
	if ($('#publishedBtn').hasClass('layui-btn-normal')) {
		console.log("published has normal");
		$('#publishedBtn').removeClass('layui-btn-normal').addClass('layui-btn-primary');
		status = -1;
		tableReload();
	} else {
		$('#publishedBtn').removeClass('layui-btn-primary').addClass('layui-btn-normal');
		$('#unpublishedBtn').removeClass('layui-btn-normal').addClass('layui-btn-primary');
		status = 0;
		tableReload();
	}
}

function showUnpublished() {
	if ($('#unpublishedBtn').hasClass('layui-btn-normal')) {
		console.log("unPublished has normal");
		$('#unpublishedBtn').removeClass('layui-btn-normal').addClass('layui-btn-primary');
		status = -1;
		tableReload();
	} else {
		$('#unpublishedBtn').removeClass('layui-btn-primary').addClass('layui-btn-normal');
		$('#publishedBtn').removeClass('layui-btn-normal').addClass('layui-btn-primary');
		status = 1;
		tableReload();
	}
}
