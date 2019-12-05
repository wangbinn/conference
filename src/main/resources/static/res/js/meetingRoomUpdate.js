var layer;
var table;
var form;
var $$;
var id = getUrlParam('id');
var principalId = getUrlParam('principalId');
var floor = getUrlParam('floor');
var roomNumber = getUrlParam('roomNumber');
var seatNumber = getUrlParam('seatNumber');
var multimedia = getUrlParam('multimedia');

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

getPrincipal();

layui.use('form', function() {
    form = layui.form;
    form.val("editPageInit", {
        "id" : id,
        "principal" : principalId,
        "floor" : floor,
        "roomNumber" : roomNumber,
        "seatNumber" : seatNumber,
        "multimedia" : multimedia
    });
    $('select', 'principal').val(principalId);
    $('select', 'multimedia').val(multimedia);
    form.render('select');
});

layui.use('form', function(){
    var form = layui.form;

    form.on('submit(updateSubmit)', function(data){

            $.ajaxSettings.async = false;

            $.ajax({
                type: "POST",
                url: "http://localhost:8080" + "/admin/meetingRoomUpdate",
                data: { meetingRoomInfo : JSON.stringify(data.field)},
                dataType: "json",
                success: function(data){
                    if (data.status == "0") {
                        layer.msg(data.msg);
                        loadModule("./meetingRoomList.html");
                    } else {
                        layer.msg(data.msg);
                    }
                },
                error:function(e){
                    console.log(e);
                }
            });
            $.ajaxSettings.async = true;

        return false;
    });

});

//选择负责人内容；
function getPrincipal() {
    var principal = [];
    layui.$.ajax({ // 使用layui模块化加载
        url: "http://localhost:8080" + "/admin/getPrincipal",
        type: 'GET',
        dataType: 'json',
        success: function(datas, textStatus, xhr) {
            var selects='';
            principal = datas.data;
            for(var b in principal){
                var its='<option value="'+principal[b].id+'">'+principal[b].name+'</option>';
                selects +=its;
            }
            $("#principal").append(selects);
            form.render();//菜单渲染 把内容加载进去
        },
        error: function(xhr, textStatus, errorThrown) {
            //called when there is an error
        }
    });
}