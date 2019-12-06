var layer;
var table;
var form;
var $$;

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

layui.use('form', function(){
    var form = layui.form;

    form.on('submit(createSubmit)', function(data){

            $.ajaxSettings.async = false;

            $.ajax({
                type: "POST",
                url: "http://localhost:8080" + "/admin/roomManagerCreate",
                data: { roomManagerInfo : JSON.stringify(data.field)},
                dataType: "json",
                success: function(data){
                    if (data.status == "0") {
                        layer.msg(data.msg);
                        loadModule("./roomManagerList.html");
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