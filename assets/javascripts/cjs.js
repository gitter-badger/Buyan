"use strict";function arrayBToString(n){function e(n){for(var e="",t=0,o=n.length;o>t;){var i=n[t],r=(10>i?"0":"")+i.toString(16);e+=r,t++}return e}var t=e(new Uint8Array(n));return t}function compare(n,e){for(var t=new Uint8Array(n),o=0;o<t.byteLength&&e>o;o++)if(0!==t[o])return!1;return!0}window.fja=function(){document.getElementById("list");jsPlumb.ready(function(){var n=window.instance=jsPlumb.getInstance({DragOptions:{cursor:"pointer",zIndex:2e3},PaintStyle:{strokeStyle:"#777"},EndpointHoverStyle:{fillStyle:"orange"},HoverPaintStyle:{strokeStyle:"orange"},EndpointStyle:{width:20,height:16,strokeStyle:"#666"},Endpoint:"Rectangle",Anchors:["TopCenter","TopCenter"],Container:"drag-drop-demo"});jsPlumb.fire("jsPlumbDemoLoaded",n)})},window.a=new function(){var n;n=this;var e=function(n){var e=document.getElementById("list");e.innerHTML=n,e.style.display="block"},t=function(){var n=document.getElementById("list");n.style.display="none"},o=[],i=function(n,i){document.getElementById("list");if(i){for(var r=-1,c=0;c<o.length;c++)if(o[c]==n){r=c;break}-1!=r&&o.splice(r,1)}else o.push(n);if(o.length>0){for(var a="<span><strong>Connections</strong></span><br/><br/><table><tr><th>Scope</th><th>Source</th><th>Target</th></tr>",d=0;d<o.length;d++)a=a+"<tr><td>"+o[d].scope+"</td><td>"+o[d].sourceId+"</td><td>"+o[d].targetId+"</td></tr>";e(a)}else t()};this.init=function(){n.inited||(n.instance=jsPlumb.getInstance({DragOptions:{cursor:"pointer",zIndex:2e3},PaintStyle:{strokeStyle:"#777"},EndpointHoverStyle:{fillStyle:"orange"},HoverPaintStyle:{strokeStyle:"orange"},EndpointStyle:{width:20,height:16,strokeStyle:"#666"},Endpoint:"Rectangle",Anchors:["TopCenter","TopCenter"],Container:"drag-drop-demo"}),$("<div/>",{id:"main"}).appendTo($("body")),$("<div/>",{id:"drag-drop-demo","class":"demo drag-drop-demo"}).appendTo("#main"),n.instance.batch(function(){n.instance.bind("connection",function(n,e){i(n.connection)}),n.instance.bind("connectionDetached",function(n,e){i(n.connection,!0)}),n.instance.bind("connectionMoved",function(n,e){i(n.connection,!0)}),n.instance.bind("click",function(n,e){alert("click!")});var t={tolerance:"touch",hoverClass:"dropHover",activeClass:"dragActive"},o="#00f";n.exampleEndpoint={endpoint:"Rectangle",paintStyle:{width:25,height:21,fillStyle:o},isSource:!0,reattach:!0,scope:"blue",connectorStyle:{gradient:{stops:[[0,o],[.5,"#09098e"],[1,o]]},lineWidth:5,strokeStyle:o,dashstyle:"2 2"},isTarget:!0,beforeDrop:function(n){return confirm("Connect "+n.sourceId+" to "+n.targetId+"?")?($(document).trigger("call",{typ:"connectTo",msg:n.targetId}),!0):!1},dropOptions:t};var r="#316b31";n.exampleEndpoint2={endpoint:["Dot",{radius:11}],paintStyle:{fillStyle:r},isSource:!0,scope:"green",connectorStyle:{strokeStyle:r,lineWidth:6},connector:["Bezier",{curviness:63}],maxConnections:3,isTarget:!0,beforeDrop:function(n){return confirm("Connect "+n.sourceId+" to "+n.targetId+"?")?($(document).trigger("call",{typ:"connectTo",msg:n.targetId}),!0):!1},dropOptions:t};var c="rgba(229,219,61,0.5)";n.exampleEndpoint3={endpoint:["Dot",{radius:17}],anchor:"BottomLeft",paintStyle:{fillStyle:c,opacity:.5},isSource:!0,scope:"yellow",connectorStyle:{strokeStyle:c,lineWidth:4},connector:"Straight",isTarget:!0,dropOptions:t,beforeDetach:function(n){return confirm("Detach connection?")},onMaxConnections:function(n){alert("Cannot drop connection "+n.connection.id+" : maxConnections has been reached on Endpoint "+n.endpoint.id)}};n.instance.draggable(jsPlumb.getSelector(".drag-drop-demo .window"));var a=jsPlumb.getSelector(".drag-drop-demo .hide");n.instance.on(a,"click",function(e){n.instance.toggleVisible(this.getAttribute("rel")),jsPlumbUtil.consume(e)});var d=jsPlumb.getSelector(".drag-drop-demo .drag");n.instance.on(d,"click",function(e){var t=n.instance.toggleDraggable(this.getAttribute("rel"));this.innerHTML=t?"disable dragging":"enable dragging",jsPlumbUtil.consume(e)});var l=jsPlumb.getSelector(".drag-drop-demo .detach");this.instance.on(l,"click",function(e){n.instance.detachAllConnections(this.getAttribute("rel")),jsPlumbUtil.consume(e)}),n.instance.on(document.getElementById("clear"),"click",function(t){n.instance.detachEveryConnection(),e(""),jsPlumbUtil.consume(t)}),n.instance.bind("connectionDetached",function(n,e){i(n.connection,!0)}),n.instance.bind("connectionMoved",function(n,e){i(n.connection,!0)}),n.instance.bind("click",function(n,e){alert("click!")})}),n.inited=!0)},this.addPeer=function(t){jQuery("<div/>",{"class":"window",id:t}).appendTo("#drag-drop-demo"),$("<br/>").appendTo("#"+t),$("<div/>",{html:t}).appendTo("#"+t),$("<a/>",{href:"#","class":"cmdLink hide",rel:"dragDropWindow"+t,html:"toggle connections"}).appendTo("#"+t),$("<br/>").appendTo("#"+t),$("<a/>",{href:"#","class":"cmdLink drag",rel:"dragDropWindow"+t,html:"disable dragging"}).appendTo("#"+t),$("<br/>").appendTo("#"+t),$("<a/>",{href:"#","class":"cmdLink detach",rel:"dragDropWindow"+t,html:"detach all"}).appendTo("#"+t),$("<br/>").appendTo("#"+t),n.instance.draggable(jsPlumb.getSelector(".drag-drop-demo .window"));var o=[[1,.2,1,0],[.8,1,0,1],[0,.8,-1,0],[.2,0,0,-1]];n.instance.addEndpoint(t,{anchor:[.5,1,0,1]},n.exampleEndpoint2),n.instance.addEndpoint(t,{anchor:o},n.exampleEndpoint);var i=jsPlumb.getSelector(".drag-drop-demo .hide");n.instance.on(i,"click",function(n){this.instance.toggleVisible(this.getAttribute("rel")),jsPlumbUtil.consume(n)});var r=jsPlumb.getSelector(".drag-drop-demo .drag");n.instance.on(r,"click",function(e){var t=n.instance.toggleDraggable(this.getAttribute("rel"));this.innerHTML=t?"disable dragging":"enable dragging",jsPlumbUtil.consume(e)});var c=jsPlumb.getSelector(".drag-drop-demo .detach");n.instance.on(c,"click",function(e){n.instance.detachAllConnections(this.getAttribute("rel")),jsPlumbUtil.consume(e)}),n.instance.on(document.getElementById("clear"),"click",function(t){n.instance.detachEveryConnection(),e(""),jsPlumbUtil.consume(t)})}},$(document).on("buyanLoaded",function(){window.messages.npeer=!0,$(document).on("npeer",function(n,e){console.log("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",e),window.a.addPeer(e)})});