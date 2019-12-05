var table;
var tables;
var layer;

layui.use('table', function() {
	var table = layui.table;
	// 第一个实例
	tables = table.render({
		elem : '#history',
		// limit : 10,
		// limits : [ 10, 20, 30, 40 ],
		url : "http://localhost:8080/conference/history?name=" + document.getElementById('user_name').innerHTML, // 数据接口
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
		}, {
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
            width : '15%',
            sort : true,
            title : '邮箱'
        }, {
            field : 'scheduledStartTime',
            align : 'center',
            width : '14%',
            sort : true,
            title : '开始时间'
        }, {
            field : 'scheduledEndTime',
            align : 'center',
            width : '14%',
            sort : true,
            title : '结束时间'
		}, {
			title : '操作',
			Width : '8%',
			fixed : 'right',
			align : 'center',
			toolbar : '#historyActive'
		} ] ],
		done : function() {
			clearParam();
		}
	});

	$("#searchBtn1").click(function() {
		tableReload();
	});

	table.on('tool(history)', function(obj) { //注：tool是工具条事件名，test是table原始容器的属性 lay-filter="对应的值"

		let data = obj.data; //获得当前行数据
		let layEvent = obj.event; //获得 lay-event 对应的值（也可以是表头的 event 参数对应的值）
		let tr = obj.tr; //获得当前行 tr 的DOM对象

		if (layEvent === 'del') { //取消预订
			layer.confirm('真的取消么', function(index) {
				$.ajax({
					type : "POST",
					url : "http://localhost:8080" + "/conference/deleteById",
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
		} else if (layEvent === 'noDel') {
			layer.msg("预定日期已过，不可取消");
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

layui.use('laydate', function() {
	var laydate = layui.laydate;

	//日期范围
	var startDate=laydate.render({
		elem: '#searchWithStartDate',
		type: 'datetime',
		istime: true,
		istoday: true,
		min:nowDateTime(),
		done:function(value,date){
			if(value!=""){
				date.month=date.month-1;
				endDate.config.min=date;
			}else{
				endDate.config.min=startDate.config.min;
			}
		},
	});
	var endDate =laydate.render({
		elem: '#searchWithEndDate',
		type: 'datetime',
		istime: true,
		istoday: true,
		done:function(value,date){
			if(value!=""){
				date.month=date.month-1;
				startDate.config.max=date;
			}else{
				startDate.config.max=endDate.config.max;
			}
		},
	});
});

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
