var layer;
var table;
var form;
var $$;
var id = getUrlParam('id');
var name = getUrlParam('name');
var phone = getUrlParam('phone');
var mail = getUrlParam('mail');

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
        "name" : name,
        "phone" : phone,
        "mail" : mail
    });
});

layui.use('form', function(){
    var form = layui.form;

    form.on('submit(updateSubmit)', function(data){

            $.ajaxSettings.async = false;

            $.ajax({
                type: "POST",
                url: "http://localhost:8080" + "/admin/roomManagerUpdate",
                data: { roomManagerInfo : JSON.stringify(data.field)},
                dataType: "json",
                success: function(data){
                    if (data.status == "0") {
                        layer.msg(data.msg);
                        // loadModule("./roomManagerList.html");
                        parent.layer.closeAll();
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