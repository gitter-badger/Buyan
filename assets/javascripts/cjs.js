'use strict'
function arrayBToString(digest){
    	function _arrayBufferToBase64( buffer ) {
						var ret = "",
						  i = 0,
						  len = buffer.length;
						while (i < len) {
							  var a = buffer[i];
								  var h = (a < 10 ? "0" : "") + a.toString(16);
									  ret += h;
										  i++;
						}
						return ret;
					}
					var str = _arrayBufferToBase64(new Uint8Array(digest));
					return str
}
function compare(buf1, difficulty) {
  var a = new Uint8Array(buf1);


  for (var i = 0; (i < a.byteLength && i < difficulty); i++) {
    if (a[i] !== 0) {
      return false;
    }
  }

  return true;
}

window.fja=(function () {

    var listDiv = document.getElementById("list"),

        showConnectionInfo = function (s) {
            listDiv.innerHTML = s;
            listDiv.style.display = "block";
        },
        hideConnectionInfo = function () {
            listDiv.style.display = "none";
        },
        connections = [],
        updateConnections = function (conn, remove) {
            if (!remove) connections.push(conn);
            else {
                var idx = -1;
                for (var i = 0; i < connections.length; i++) {
                    if (connections[i] == conn) {
                        idx = i;
                        break;
                    }
                }
                if (idx != -1) connections.splice(idx, 1);
            }
            if (connections.length > 0) {
                var s = "<span><strong>Connections</strong></span><br/><br/><table><tr><th>Scope</th><th>Source</th><th>Target</th></tr>";
                for (var j = 0; j < connections.length; j++) {
                    s = s + "<tr><td>" + connections[j].scope + "</td>" + "<td>" + connections[j].sourceId + "</td><td>" + connections[j].targetId + "</td></tr>";
                }
                showConnectionInfo(s);
            } else
                hideConnectionInfo();
        };

    jsPlumb.ready(function () {

        var instance = window.instance = jsPlumb.getInstance({
            DragOptions: { cursor: 'pointer', zIndex: 2000 },
            PaintStyle: { strokeStyle: '#777' },
            EndpointHoverStyle: { fillStyle: "orange" },
            HoverPaintStyle: { strokeStyle: "orange" },
            EndpointStyle: { width: 20, height: 16, strokeStyle: '#666' },
            Endpoint: "Rectangle",
            Anchors: ["TopCenter", "TopCenter"],
            Container: "drag-drop-demo"
        });

        // suspend drawing and initialise.


        jsPlumb.fire("jsPlumbDemoLoaded", instance);

    });

});







window.a = new (function(){
var self;
   self=this;
var
        showConnectionInfo = function (s) {
           var listDiv = document.getElementById("list");
            listDiv.innerHTML = s;
            listDiv.style.display = "block";
        },
        hideConnectionInfo = function () {
           var listDiv = document.getElementById("list");
            listDiv.style.display = "none";
        },
        connections = [],
        updateConnections = function (conn, remove) {
           var listDiv = document.getElementById("list");
            if (!remove) connections.push(conn);
            else {
                var idx = -1;
                for (var i = 0; i < connections.length; i++) {
                    if (connections[i] == conn) {
                        idx = i;
                        break;
                    }
                }
                if (idx != -1) connections.splice(idx, 1);
            }
            if (connections.length > 0) {
                var s = "<span><strong>Connections</strong></span><br/><br/><table><tr><th>Scope</th><th>Source</th><th>Target</th></tr>";
                for (var j = 0; j < connections.length; j++) {
                    s = s + "<tr><td>" + connections[j].scope + "</td>" + "<td>" + connections[j].sourceId + "</td><td>" + connections[j].targetId + "</td></tr>";
                }
                showConnectionInfo(s);
            } else
                hideConnectionInfo();
        },
          maxConnectionsCallback = function (info) {
                    alert("Cannot drop connection " + info.connection.id + " : maxConnections has been reached on Endpoint " + info.endpoint.id);
                };
  this.init=function(){
if(!self.inited){

self.instance=jsPlumb.getInstance({
            DragOptions: { cursor: 'pointer', zIndex: 2000 },
            PaintStyle: { strokeStyle: '#777' },
            EndpointHoverStyle: { fillStyle: "orange" },
            HoverPaintStyle: { strokeStyle: "orange" },
            EndpointStyle: { width: 20, height: 16, strokeStyle: '#666' },
            Endpoint: "Rectangle",
            Anchors: ["TopCenter", "TopCenter"],
            Container: "drag-drop-demo"
        });

    $("<div/>", {
      id: "main"
    }).appendTo($("body"));
    $("<div/>", { id: "drag-drop-demo", class: "demo drag-drop-demo"}).appendTo("#main");


       self.instance.batch(function () {

            // bind to connection/connectionDetached events, and update the list of connections on screen.
            self.instance.bind("connection", function (info, originalEvent) {
                updateConnections(info.connection);
                 //$(document).trigger("call", {typ: "connectTo", msg: info.targetId});
            });
            self.instance.bind("connectionDetached", function (info, originalEvent) {
                updateConnections(info.connection, true);
            });

            self.instance.bind("connectionMoved", function (info, originalEvent) {
                //  only remove here, because a 'connection' event is also fired.
                // in a future release of jsplumb this extra connection event will not
                // be fired.
                updateConnections(info.connection, true);
            });

            self.instance.bind("click", function (component, originalEvent) {
                alert("click!")
            });

            // configure some drop options for use by all endpoints.
            var exampleDropOptions = {
                tolerance: "touch",
                hoverClass: "dropHover",
                activeClass: "dragActive"
            };

            //
            // first example endpoint.  it's a 25x21 rectangle (the size is provided in the 'style' arg to the Endpoint),
            // and it's both a source and target.  the 'scope' of this Endpoint is 'exampleConnection', meaning any connection
            // starting from this Endpoint is of type 'exampleConnection' and can only be dropped on an Endpoint target
            // that declares 'exampleEndpoint' as its drop scope, and also that
            // only 'exampleConnection' types can be dropped here.
            //
            // the connection style for this endpoint is a Bezier curve (we didn't provide one, so we use the default), with a lineWidth of
            // 5 pixels, and a gradient.
            //
            // there is a 'beforeDrop' interceptor on this endpoint which is used to allow the user to decide whether
            // or not to allow a particular connection to be established.
            //
            var exampleColor = "#00f";
           self.exampleEndpoint = {
                endpoint: "Rectangle",
                paintStyle: { width: 25, height: 21, fillStyle: exampleColor },
                isSource: true,
                reattach: true,
                scope: "blue",
                connectorStyle: {
                    gradient: {stops: [
                        [0, exampleColor],
                        [0.5, "#09098e"],
                        [1, exampleColor]
                    ]},
                    lineWidth: 5,
                    strokeStyle: exampleColor,
                    dashstyle: "2 2"
                },
                isTarget: true,
                beforeDrop: function (params) {
                  if( confirm("Connect " + params.sourceId + " to " + params.targetId + "?")){
                     $(document).trigger("call", {typ: "connectTo", msg: params.targetId});
                  return true;
                  }
                  return false;

                },
                dropOptions: exampleDropOptions
            };

            //
            // the second example uses a Dot of radius 15 as the endpoint marker, is both a source and target,
            // and has scope 'exampleConnection2'.
            //
            var color2 = "#316b31";
           self.exampleEndpoint2 = {
                endpoint: ["Dot", { radius: 11 }],
                paintStyle: { fillStyle: color2 },
                isSource: true,
                scope: "green",
                connectorStyle: { strokeStyle: color2, lineWidth: 6 },
                connector: ["Bezier", { curviness: 63 } ],
                maxConnections: 3,
                isTarget: true,
                beforeDrop: function (params) {
                  if( confirm("Connect " + params.sourceId + " to " + params.targetId + "?")){
                     $(document).trigger("call", {typ: "connectTo", msg: params.targetId});
                  return true;
                  }
                  return false;

                },
                dropOptions: exampleDropOptions
            };

            //
            // the third example uses a Dot of radius 17 as the endpoint marker, is both a source and target, and has scope
            // 'exampleConnection3'.  it uses a Straight connector, and the Anchor is created here (bottom left corner) and never
            // overriden, so it appears in the same place on every element.
            //
            // this example also demonstrates the beforeDetach interceptor, which allows you to intercept
            // a connection detach and decide whether or not you wish to allow it to proceed.
            //
            var example3Color = "rgba(229,219,61,0.5)";
            self.exampleEndpoint3 ={
                endpoint: ["Dot", {radius: 17} ],
                anchor: "BottomLeft",
                paintStyle: { fillStyle: example3Color, opacity: 0.5 },
                isSource: true,
                scope: 'yellow',
                connectorStyle: {
                    strokeStyle: example3Color,
                    lineWidth: 4
                },
                connector: "Straight",
                isTarget: true,
                dropOptions: exampleDropOptions,
                beforeDetach: function (conn) {
                    return confirm("Detach connection?");
                },
                onMaxConnections: function (info) {
                    alert("Cannot drop connection " + info.connection.id + " : maxConnections has been reached on Endpoint " + info.endpoint.id);
                }
            };

            // setup some empty endpoints.  again note the use of the three-arg method to reuse all the parameters except the location
            // of the anchor (purely because we want to move the anchor around here; you could set it one time and forget about it though.)
            //var e1 = this.instance.addEndpoint('1', { anchor: [0.5, 1, 0, 1] }, this.exampleEndpoint2);

            // setup some DynamicAnchors for use with the blue endpoints
            // and a function to set as the maxConnections callback.
            var anchors = [
                    [1, 0.2, 1, 0],
                    [0.8, 1, 0, 1],
                    [0, 0.8, -1, 0],
                    [0.2, 0, 0, -1]
                ];


//            var e1 = this.instance.addEndpoint("#"+d, { anchor: anchors }, this.exampleEndpoint);
            // you can bind for a maxConnections callback using a standard bind call, but you can also supply 'onMaxConnections' in an Endpoint definition - see exampleEndpoint3 above.
  //          e1.bind("maxConnections", maxConnectionsCallback);


/*
            var e3 = this.instance.addEndpoint("dragDropWindow3", { anchor: [0.25, 0, 0, -1] }, this.exampleEndpoint);
            e3.bind("maxConnections", maxConnectionsCallback);
            this.instance.addEndpoint("dragDropWindow3", { anchor: [0.75, 0, 0, -1] }, this.exampleEndpoint2);

            var e4 = this.instance.addEndpoint("dragDropWindow4", { anchor: [1, 0.5, 1, 0] }, this.exampleEndpoint);
            e4.bind("maxConnections", maxConnectionsCallback);
            this.instance.addEndpoint("dragDropWindow4", { anchor: [0.25, 0, 0, -1] }, this.exampleEndpoint2);
*/
            // make .window divs draggable
            self.instance.draggable(jsPlumb.getSelector(".drag-drop-demo .window"));

            // add endpoint of type 3 using a selector.
           // this.instance.addEndpoint(jsPlumb.getSelector(".drag-drop-demo .window"), this.exampleEndpoint3);

            var hideLinks = jsPlumb.getSelector(".drag-drop-demo .hide");
            self.instance.on(hideLinks, "click", function (e) {
                self.instance.toggleVisible(this.getAttribute("rel"));
                jsPlumbUtil.consume(e);
            });

            var dragLinks = jsPlumb.getSelector(".drag-drop-demo .drag");
            self.instance.on(dragLinks, "click", function (e) {
                var s = self.instance.toggleDraggable(this.getAttribute("rel"));
                this.innerHTML = (s ? 'disable dragging' : 'enable dragging');
                jsPlumbUtil.consume(e);
            });

            var detachLinks = jsPlumb.getSelector(".drag-drop-demo .detach");
           // var self=this;
            this.instance.on(detachLinks, "click", function (e) {
                self.instance.detachAllConnections(this.getAttribute("rel"));
                jsPlumbUtil.consume(e);
            });

            self.instance.on(document.getElementById("clear"), "click", function (e) {
                self.instance.detachEveryConnection();
                showConnectionInfo("");
                jsPlumbUtil.consume(e);
            });
//          self.instance.bind("connection", function (info, originalEvent) {
  // debugger;

//updateConnections(info.connection);
//});
self.instance.bind("connectionDetached", function (info, originalEvent) {
updateConnections(info.connection, true);
});
self.instance.bind("connectionMoved", function (info, originalEvent) {
// only remove here, because a 'connection' event is also fired.
// in a future release of jsplumb this extra connection event will not
// be fired.
updateConnections(info.connection, true);
});
self.instance.bind("click", function (component, originalEvent) {
alert("click!")
});

        });

    self.inited=true;
  }
 }
  this.addPeer = function(d){
debugger;
jQuery('<div/>', {
    class: 'window',
    id: d
}).appendTo('#drag-drop-demo');
$("<br/>").appendTo("#"+d);
    $("<div/>",{html: d}).appendTo("#"+d);
  $("<a/>",{ href:"#", class: "cmdLink hide", rel: "dragDropWindow"+d,
                        html: "toggle connections"
            }).appendTo("#"+d);

$("<br/>").appendTo("#"+d);

  $("<a/>",{ href:"#", class: "cmdLink drag", rel: "dragDropWindow"+d,
                        html: "disable dragging"
            }).appendTo("#"+d);
$("<br/>").appendTo("#"+d);
  $("<a/>",{ href:"#", class: "cmdLink detach", rel: "dragDropWindow"+d,
                        html: "detach all"
            }).appendTo("#"+d);
$("<br/>").appendTo("#"+d);

            // make .window divs draggable
           // window.instance.draggable(jsPlumb.getSelector(".drag-drop-demo .window"));

            // add endpoint of type 3 using a selector.
           // window.instance.addEndpoint(jsPlumb.getSelector(".drag-drop-demo .window"), exampleEndpoint3);

            // make .window divs draggable


     //  var e3= self.instance.addEndpoint(jsPlumb.getSelector("#"+d), { anchor: [0.25, 0, 0, -1] }, self.exampleEndpoint);
        //    e3.bind("maxConnections", maxConnectionsCallback);
        //   self.instance.addEndpoint(jsPlumb.getSelector("#"+d), { anchor: [0.75, 0, 0, -1] }, self.exampleEndpoint3);
                //var e3 = self.instance.addEndpoint(jsPlumb.getSelector("#"+d), { anchor: [0.5, 1, 0, 1] }, self.exampleEndpoint);
            // again we bind manually. it's starting to get tedious.  but now that i've done one of the blue endpoints this way, i have to do them all...
            //e2.bind("maxConnections", maxConnectionsCallback);
  //          self.instance.addEndpoint(jsPlumb.getSelector("#"+d), { anchor: "RightMiddle" }, self.exampleEndpoint2);

    //debugger;

            self.instance.draggable(jsPlumb.getSelector(".drag-drop-demo .window"));

var anchors = [
[1, 0.2, 1, 0],
[0.8, 1, 0, 1],
[0, 0.8, -1, 0],
[0.2, 0, 0, -1]
];
            // add endpoint of type 3 using a selector.
    self.instance.addEndpoint(d, { anchor: [0.5, 1, 0, 1] }, self.exampleEndpoint2);
    self.instance.addEndpoint(d, { anchor: anchors }, self.exampleEndpoint);

            var hideLinks = jsPlumb.getSelector(".drag-drop-demo .hide");
            self.instance.on(hideLinks, "click", function (e) {
                this.instance.toggleVisible(this.getAttribute("rel"));
                jsPlumbUtil.consume(e);
            });

            var dragLinks = jsPlumb.getSelector(".drag-drop-demo .drag");
            self.instance.on(dragLinks, "click", function (e) {
                var s = self.instance.toggleDraggable(this.getAttribute("rel"));
                this.innerHTML = (s ? 'disable dragging' : 'enable dragging');
                jsPlumbUtil.consume(e);
            });

            var detachLinks = jsPlumb.getSelector(".drag-drop-demo .detach");
            self.instance.on(detachLinks, "click", function (e) {
                self.instance.detachAllConnections(this.getAttribute("rel"));
                jsPlumbUtil.consume(e);
            });

            self.instance.on(document.getElementById("clear"), "click", function (e) {
                self.instance.detachEveryConnection();
                showConnectionInfo("");
                jsPlumbUtil.consume(e);
            });




  }

});
$(document).on("buyanLoaded",function(){
//window.messages.peer=true;
window.messages.npeer=true;
//window.a.init()
$(document).on("npeer",function(ev,d)
{
console.log("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx",d);
//window.messages.peer=false;

window.a.addPeer(d)
//window.fja();
//$('#drag-drop-demo').

//$(document).trigger( "pubsub",{"typ": "peer", "msg": d});
//window.messages.peer=true;
//window.messages.npeer=false;
});






});
