var layer;
var table;
var form;
var $$;
var id = getUrlParam('id');
var name = getUrlParam('name');
var phone = getUrlParam('phone');
var mail = getUrlParam('mail');
var floor = getUrlParam('floor');
var roomNumber = getUrlParam('roomNumber');
var seatNumber = getUrlParam('seatNumber');
var multimedia = getUrlParam('multimedia');
var userName = getUrlParam('userName');
var startTime = getUrlParam('startTime');
var endTime = getUrlParam('endTime');

layui.use('table', function(){
    table = layui.table;
});

layui.use('layer', function(){
    layer = layui.layer;
});

layui.use('form', function(){
    form = layui.form;

});

layui.use('element', function(){
    var element = layui.element;
});

layui.use('jquery', function(){
    $$ = layui.jquery;
});

layui.use('form', function() {
    form = layui.form;
    form.val("editPageInit", {
        "id" : id,
        "bookingWithStartDate" : startTime,
        "bookingWithEndDate" : endTime,
        "name" : name,
        "phone" : phone,
        "mail" : mail,
        "floor" : floor,
        "roomNumber" : roomNumber,
        "seatNumber" : seatNumber,
        "multimedia" : multimedia,
        "userName" : userName
    })
});

layui.use('form', function(){
    var form = layui.form;

    form.on('submit(bookingSubmit)', function(data){

        layer.confirm('真的预定么？（60s后若不确定将自动取消）',{time:60*1000}, function(index) {
            $.ajaxSettings.async = false;

            $.ajax({
                type: "POST",
                url: "http://localhost:8080" + "/conference/booking",
                data: { bookingInfo : JSON.stringify(data.field)},
                dataType: "json",
                success: function(data){
                    if (data.status == "0") {
                        layer.msg(data.msg,{time:2*1000},function () {
                            parent.layer.closeAll();
                            // loadModule("./historyList.html");
                        });
                    } else {
                        layer.msg(data.msg);
                    }
                },
                error:function(e){
                    console.log(e);
                }
            });
            $.ajaxSettings.async = true;
        });

        return false;
    });

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
        elem: '#bookingWithStartDate',
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
        elem: '#bookingWithEndDate',
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
