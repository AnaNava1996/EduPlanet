var name="",$xml,objSet = new Set();
$(document).ready(function () {
    $.get("FileManager",function(data){
        xmlDoc = $.parseXML( data );
        $xml = $( xmlDoc );
        autoC();
    });
    $("input").hide();
    $("#boton2").hide();
    $("#boton").click(function () {
        $("input").show();
        $("#casitadeboton").show();
        $("#boton2").show();
        $("#boton2").click(function(){
            var pli = $("#nombreSS").val().length;
            if(pli > 0){
                gotoStar();
            }
        });
    });
    $(".editar").click(function(){
        alert("1");
        var name = $(this).attr("id");
        gotoStarEdit(name);
    });
});
function autoC(){
    $xml.children().children().each(function(index,element){
        txttoappend =  "<br/><div class='objdiv'><div style='display:inline;'><button class='editar' onclick='gotoStarEdit(\""+$(element).attr("name")+"\")'>editar</button><button class='eliminar' onclick='delServer(\""+$(element).attr("name")+"\")'>eliminar</button><div style='padding-left:40px;display:inline'>"+$(element).attr("name")+"</div></div>";
        $("#div1").append(txttoappend);
    });
}

function gotoStar(){
    $.post("PageManager",
    {
        name:$("#nombreSS").val(),
        type:"prof"
    },
    function(data){
        window.location.replace(data);
    });
}
function gotoStarEdit(name){
    $.post("PageManager",
    {
        name:name,
        type:"prof"
    },
    function(data){
        window.location.replace(data);
    });
}

function delServer(name){
    $.post("FileManager",{
        json:"",
        name:name,
        index:"delete"
    },function(data){
        alert(data + " borrado");
        window.location.reload();
    });
}