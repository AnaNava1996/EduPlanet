var name="",$xml,objSet = new Set();

$(document).ready(function(){
    $.get("PageManager",
    function(data){
        name = data;
        alert(name);
    });
    $.get("FileManager",function(data){
        xmlDoc = $.parseXML( data );
        $xml = $( xmlDoc );
        autoC();
    });
    
});
function autoC(){
    $xml.children().children().each(function(index,element){
                if($(element).attr("name")===name) particleList = JSON.parse($(element).text());
    });
}
////Simulates and draws Particles
//TODO: improve integration code. Why not add accelerations to both particles being compared?
function init(){
    var t=document.getElementById("canvas"),e=document.getElementById("controlbox");
    t.width=window.innerWidth-30,
    t.height=window.innerHeight-20,
    width=t.width,height=t.height,
    context=t.getContext("2d"),
    window.addEventListener("mousedown",mouseDownListener,!1),

    e.onmouseover=function()
    {
	onControlBox=!0;
    },
    e.onmouseout=function(){
	onControlBox=!1;
    };
}
function main(){
    starttime=Date.now(),
    integrate(),
    draw(),
    frametime=Date.now()-starttime;
}
function mouseDownListener(t)
{
    onControlBox||(shiftDown=t.shiftKey,startCoords[0]=t.clientX,startCoords[1]=t.clientY,endCoords[0]=t.clientX,endCoords[1]=t.clientY,window.addEventListener("mousemove",mouseMoveListener,!1),window.addEventListener("mouseup",mouseUpListener,!1));
}
function mouseMoveListener(t){
    endCoords[0]=t.clientX;
    endCoords[1]=t.clientY;
}
function mouseUpListener(t){
    if(window.removeEventListener("mousemove",mouseMoveListener),
       window.removeEventListener("mouseup",mouseUpListener),
       !t.shiftKey&&!shiftDown){
	var e=new Particle(newMass,startCoords[0],startCoords[1],
			   endCoords[0]-startCoords[0],
			   endCoords[1]-startCoords[1]);
	particleList.push(e);
    }
    t.shiftKey&&shiftDown&&(particleShift=[endCoords[0]-startCoords[0],endCoords[1]-startCoords[1]]);
    startCoords=[-1,-1],endCoords=[-1,-1];
}
function toLoad(){
    var file = document.getElementById('files');
    file.click();
    file.addEventListener('change',function(e){
	fromJSON();
    });
}
function toJSON(){
    var OBJson = JSON.stringify(particleList);
    var filename = "blob.json";
    var blob = new Blob([OBJson],{type: "text/plain;charset=utf-8"});
    saveAs(blob,filename);
}
function fromJSON(){
    particleList.length = 0;
    var fileInput = document.getElementById('files');
    var file = fileInput.files[0];
    var textType =/json.*/;
    var txt = "!";

    if (file.type.match(textType)) {
	var reader = new FileReader();
	
	reader.onload = function() {
	    particleList = JSON.parse(reader.result);
	};
	reader.readAsText(file);
    } else {alert("File not supported!");}
}

function serverSave(){
    var OBJson = JSON.stringify(particleList);
    $.post("FileManager",{
        json:OBJson,
        name:name,
        index:"save"
    },function(data){alert(data + " guardado");});
}

function serverLoad(){
    $.get("FileManager",function(data){
        particleList = JSON.parse(data);
    });
}

function setNewMass(t){
    console.log(t);
    newMass=t;
}
function integrate(){
    for(var t=new Array,e=0;e<particleList.length;e++){
	for(var o=particleList[e],r=0,i=0,s=0;s<particleList.length;s++)
	    if(otherParticle=particleList[s],o!==otherParticle&&!o.collided&&!otherParticle.collided){
		var a=otherParticle.x-o.x,n=otherParticle.y-o.y,c=Math.sqrt(a*a+n*n);
		if(c<o.radius/1.5+otherParticle.radius/1.5){
		    o.collided=!0,otherParticle.collided=!0;
		    var l=o.mass+otherParticle.mass,
			d=new Particle(l,(o.x*o.mass+otherParticle.x*otherParticle.mass)/l,
				       (o.y*o.mass+otherParticle.y*otherParticle.mass)/l,
				       (o.vx*o.mass+otherParticle.vx*otherParticle.mass)/l,
				       (o.vy*o.mass+otherParticle.vy*otherParticle.mass)/l);
		    t.push(d);
		}
		var p=otherParticle.mass/(c*c);
		r+=p*a/c,i+=p*n/c;}o.ax=r,o.ay=i;
    }
    for(var m=0;m<particleList.length;m++)
	particleList[m].vx+=particleList[m].ax*h,
    particleList[m].vy+=particleList[m].ay*h,
    particleList[m].x+=particleList[m].vx*h+particleShift[0],
    particleList[m].y+=particleList[m].vy*h+particleShift[1],
    (particleList[m].collided||
     particleList[m].x<-50||
     particleList[m].y<-50||
     particleList[m].x>width+50||
     particleList[m].y>height+50)&&
	(particleList.splice(m,1),m--);
    Array.prototype.push.apply(particleList,t),particleShift=[0,0];
}
function draw(){
    context.clearRect(0,0,width,height),
    context.beginPath(),
    context.moveTo(startCoords[0],startCoords[1]),
    context.lineTo(endCoords[0],endCoords[1]),
    context.strokeStyle="blue",
    context.strokeWidth=2,
    context.stroke();
    for(var t=0;t<particleList.length;t++){
	var e=particleList[t];
	context.beginPath(),context.arc(e.x,e.y,e.radius,0,2*Math.PI),context.closePath();
	var o=e.color;
	if(e.radius<3)context.fillStyle="#"+o[3];
	else{
	    var r=context.createRadialGradient(e.x,e.y,.75*e.radius,e.x,e.y,e.radius);
	    r.addColorStop(0,"rgba("+o[0]+","+o[1]+","+o[2]+",1.0)"),
	    r.addColorStop(1,"rgba("+o[0]+","+o[1]+","+o[2]+",0)");
	    context.fillStyle=r;
	}
	context.fill();
    }
}

var h=.005,particleList=new Array,context,frametime,starttime,width,height,startCoords=[-1,-1],endCoords=[-1,-1],newMass=1e3,onControlBox=!1,shiftDown=!1,particleShift=[0,0];
