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
		//url : "http://localhost:8080/conference/listByDate", // 数据接口
        url : "http://localhost:8080/conference/list", // 数据接口
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

		if (layEvent === 'booking') { //预定
			layer.open({
				type : 2,
				title : '预定会议室: ' + data.roomNumber,
				// btn : [ '关闭' ],
				area : [ '66%', '72%' ],
				content : './booking.html?id=' + data.id
					+ "&name=" + data.name + "&phone=" + data.phone + "&mail=" + data.mail + "&floor=" + data.floor
					+ "&roomNumber=" + data.roomNumber + "&seatNumber=" + data.seatNumber + "&multimedia=" + data.multimedia
					+ "&userName=" + document.getElementById('user_name').innerHTML
					+ "&startTime=" + $("#searchWithStartDate").val() + "&endTime=" + $("#searchWithEndDate").val(),
				// time: 60000,
				success : function(data) {
				},
				yes : function(index) {
					layer.close(index);
				}
			});
		}
	});

	table.on('rowDouble(conference)', function(obj) {
		let data = obj.data;
		layer.open({
			type : 2,
			title : '预定会议室: ' + data.roomNumber,
			// btn : [ '关闭' ],
			area : [ '66%', '72%' ],
			content : './booking.html?id=' + data.id
				+ "&name=" + data.name + "&phone=" + data.phone + "&mail=" + data.mail + "&floor=" + data.floor
				+ "&roomNumber=" + data.roomNumber + "&seatNumber=" + data.seatNumber + "&multimedia=" + data.multimedia
				+ "&userName=" + document.getElementById('user_name').innerHTML
				+ "&startTime=" + $("#searchWithStartDate").val() + "&endTime=" + $("#searchWithEndDate").val(),
			// time: 60000,
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
		}
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
