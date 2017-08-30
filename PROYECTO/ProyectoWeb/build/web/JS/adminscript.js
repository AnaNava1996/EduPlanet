var name="",$xml,objSet = new Set();
$(document).ready(function () {
    $xml = "";
    $.get("UserManager",function(data){
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
    });
});
function autoC(){
    $xml.children().children().each(function(index,element){
        alert($(element).children("nombre").text());
        text = $(element).children("nombre").text();
        txttoappend = "<br/><div class='objdiv'><div style='display:inline;'><button class='editar' onclick='gotoStarEdit(\""+text+"\")'>Editar</button><div style='display:inline;'><button class='eliminar' onclick='delServer(\""+text+"\")'>Eliminar</button><div style='padding-left:40px;display:inline'>"+text+"</div></div>";
        $("#div1").append(txttoappend);
    });
}
function gotoStarEdit(name){
    $.post("UserManager",
    {
        name:name,
        type:"alumno"
    },
    function(data){
        window.location.replace(data);
    });
}

function delServer(name){
    $.post("UserManager",{
        json:"",
        name:name,
        index:"delete"
    },function(data){
        alert(data + " borrado");
        window.location.reload();
    });
}