/* jshint ignore:start */

/* jshint ignore:end */

define('bapp/adapters/id', ['exports', 'ember-data'], function (exports, DS) {

	'use strict';


	exports['default'] = DS['default'].FixtureAdapter.extend({});

});
define('bapp/adapters/network', ['exports', 'ember-data'], function (exports, DS) {

	'use strict';


	exports['default'] = DS['default'].FixtureAdapter.extend({});

});
define('bapp/adapters/peer', ['exports', 'ember-data'], function (exports, DS) {

  'use strict';


  var myadapter;
  myadapter = DS['default'].Adapter.extend({
    // ...Post-specific adapter code goes here
    find: function find() {},
    createRecord: function createRecord() {},
    updateRecord: function updateRecord() {},
    deleteRecord: function deleteRecord() {},
    findAll: function findAll() {},
    findQuery: function findQuery() {}

  });

  myadapter = DS['default'].RESTAdapter.extend({
    namespace: "peers",
    host: "http://localhost:8000/prokletdajepapa",
    keyForRelationship: function keyForRelationship(key, relationship) {
      return "";
    },
    pathForType: function pathForType(type) {
      return "";
    }
  });

  exports['default'] = myadapter;

});
define('bapp/adapters/scratch', ['exports', 'ember-data'], function (exports, DS) {

    'use strict';

    exports['default'] = DS['default'].RESTAdapter.extend({
        namespace: "get",
        host: "http://localhost:8001",
        keyForRelationship: function keyForRelationship(key, relationship) {
            return "";
        },
        pathForType: function pathForType(type) {
            return "";
        }
    });

});
define('bapp/app', ['exports', 'ember', 'ember/resolver', 'ember/load-initializers', 'bapp/config/environment'], function (exports, Ember, Resolver, loadInitializers, config) {

  'use strict';

  Ember['default'].MODEL_FACTORY_INJECTIONS = true;

  var App = Ember['default'].Application.extend({
    modulePrefix: config['default'].modulePrefix,
    podModulePrefix: config['default'].podModulePrefix,
    Resolver: Resolver['default']
  });

  loadInitializers['default'](App, config['default'].modulePrefix);

  exports['default'] = App;

});
define('bapp/assets/javascripts/adapter', function () {

  'use strict';

  //https://github.com/cjb/serverless-webrtc/blob/master/js/adapter.js
  var RTCPeerConnection = null;
  var getUserMedia = null;
  var attachMediaStream = null;
  var reattachMediaStream = null;
  var webrtcDetectedBrowser = null;
  if (navigator.mozGetUserMedia) {
    console.log("This appears to be Firefox");
    webrtcDetectedBrowser = "firefox";
    // The RTCPeerConnection object.
    RTCPeerConnection = mozRTCPeerConnection;
    // The RTCSessionDescription object.
    RTCSessionDescription = mozRTCSessionDescription;
    // The RTCIceCandidate object.
    RTCIceCandidate = mozRTCIceCandidate;
    // Get UserMedia (only difference is the prefix).
    // Code from Adam Barth.
    getUserMedia = navigator.mozGetUserMedia.bind(navigator);
    // Attach a media stream to an element.
    attachMediaStream = function (element, stream) {
      console.log("Attaching media stream");
      element.mozSrcObject = stream;
      element.play();
    };
    reattachMediaStream = function (to, from) {
      console.log("Reattaching media stream");
      to.mozSrcObject = from.mozSrcObject;
      to.play();
    };
    // Fake get{Video,Audio}Tracks
    MediaStream.prototype.getVideoTracks = function () {
      return [];
    };
    MediaStream.prototype.getAudioTracks = function () {
      return [];
    };
  } else if (navigator.webkitGetUserMedia) {
    console.log("This appears to be Chrome");
    webrtcDetectedBrowser = "chrome";
    // The RTCPeerConnection object.
    RTCPeerConnection = webkitRTCPeerConnection;
    // Get UserMedia (only difference is the prefix).
    // Code from Adam Barth.
    getUserMedia = navigator.webkitGetUserMedia.bind(navigator);
    // Attach a media stream to an element.
    attachMediaStream = function (element, stream) {
      element.src = webkitURL.createObjectURL(stream);
    };
    reattachMediaStream = function (to, from) {
      to.src = from.src;
    };
    // The representation of tracks in a stream is changed in M26.
    // Unify them for earlier Chrome versions in the coexisting period.
    if (!webkitMediaStream.prototype.getVideoTracks) {
      webkitMediaStream.prototype.getVideoTracks = function () {
        return this.videoTracks;
      };
      webkitMediaStream.prototype.getAudioTracks = function () {
        return this.audioTracks;
      };
    }
    // New syntax of getXXXStreams method in M26.
    if (!webkitRTCPeerConnection.prototype.getLocalStreams) {
      webkitRTCPeerConnection.prototype.getLocalStreams = function () {
        return this.localStreams;
      };
      webkitRTCPeerConnection.prototype.getRemoteStreams = function () {
        return this.remoteStreams;
      };
    }
  } else {
    console.log("Browser does not appear to be WebRTC-capable");
  }

});
define('bapp/assets/javascripts/app', function () {

	'use strict';


});
define('bapp/assets/javascripts/cjs', function () {

    'use strict';

    "use strict";
    function arrayBToString(digest) {
        function _arrayBufferToBase64(buffer) {
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
        return str;
    }
    function compare(buf1, difficulty) {
        var a = new Uint8Array(buf1);

        for (var i = 0; i < a.byteLength && i < difficulty; i++) {
            if (a[i] !== 0) {
                return false;
            }
        }

        return true;
    }

    window.fja = function () {

        var listDiv = document.getElementById("list"),
            showConnectionInfo = function showConnectionInfo(s) {
            listDiv.innerHTML = s;
            listDiv.style.display = "block";
        },
            hideConnectionInfo = function hideConnectionInfo() {
            listDiv.style.display = "none";
        },
            connections = [],
            updateConnections = function updateConnections(conn, remove) {
            if (!remove) connections.push(conn);else {
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
            } else hideConnectionInfo();
        };

        jsPlumb.ready(function () {

            var instance = window.instance = jsPlumb.getInstance({
                DragOptions: { cursor: "pointer", zIndex: 2000 },
                PaintStyle: { strokeStyle: "#777" },
                EndpointHoverStyle: { fillStyle: "orange" },
                HoverPaintStyle: { strokeStyle: "orange" },
                EndpointStyle: { width: 20, height: 16, strokeStyle: "#666" },
                Endpoint: "Rectangle",
                Anchors: ["TopCenter", "TopCenter"],
                Container: "drag-drop-demo"
            });

            // suspend drawing and initialise.

            jsPlumb.fire("jsPlumbDemoLoaded", instance);
        });
    };

    window.a = new function () {
        var self;
        self = this;
        var showConnectionInfo = function showConnectionInfo(s) {
            var listDiv = document.getElementById("list");
            listDiv.innerHTML = s;
            listDiv.style.display = "block";
        },
            hideConnectionInfo = function hideConnectionInfo() {
            var listDiv = document.getElementById("list");
            listDiv.style.display = "none";
        },
            connections = [],
            updateConnections = function updateConnections(conn, remove) {
            var listDiv = document.getElementById("list");
            if (!remove) connections.push(conn);else {
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
            } else hideConnectionInfo();
        },
            maxConnectionsCallback = function maxConnectionsCallback(info) {
            alert("Cannot drop connection " + info.connection.id + " : maxConnections has been reached on Endpoint " + info.endpoint.id);
        };
        this.init = function () {
            if (!self.inited) {

                self.instance = jsPlumb.getInstance({
                    DragOptions: { cursor: "pointer", zIndex: 2000 },
                    PaintStyle: { strokeStyle: "#777" },
                    EndpointHoverStyle: { fillStyle: "orange" },
                    HoverPaintStyle: { strokeStyle: "orange" },
                    EndpointStyle: { width: 20, height: 16, strokeStyle: "#666" },
                    Endpoint: "Rectangle",
                    Anchors: ["TopCenter", "TopCenter"],
                    Container: "drag-drop-demo"
                });

                $("<div/>", {
                    id: "main"
                }).appendTo($("body"));
                $("<div/>", { id: "drag-drop-demo", "class": "demo drag-drop-demo" }).appendTo("#main");

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
                        alert("click!");
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
                            gradient: { stops: [[0, exampleColor], [0.5, "#09098e"], [1, exampleColor]] },
                            lineWidth: 5,
                            strokeStyle: exampleColor,
                            dashstyle: "2 2"
                        },
                        isTarget: true,
                        beforeDrop: function beforeDrop(params) {
                            if (confirm("Connect " + params.sourceId + " to " + params.targetId + "?")) {
                                $(document).trigger("call", { typ: "connectTo", msg: params.targetId });
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
                        connector: ["Bezier", { curviness: 63 }],
                        maxConnections: 3,
                        isTarget: true,
                        beforeDrop: function beforeDrop(params) {
                            if (confirm("Connect " + params.sourceId + " to " + params.targetId + "?")) {
                                $(document).trigger("call", { typ: "connectTo", msg: params.targetId });
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
                    self.exampleEndpoint3 = {
                        endpoint: ["Dot", { radius: 17 }],
                        anchor: "BottomLeft",
                        paintStyle: { fillStyle: example3Color, opacity: 0.5 },
                        isSource: true,
                        scope: "yellow",
                        connectorStyle: {
                            strokeStyle: example3Color,
                            lineWidth: 4
                        },
                        connector: "Straight",
                        isTarget: true,
                        dropOptions: exampleDropOptions,
                        beforeDetach: function beforeDetach(conn) {
                            return confirm("Detach connection?");
                        },
                        onMaxConnections: function onMaxConnections(info) {
                            alert("Cannot drop connection " + info.connection.id + " : maxConnections has been reached on Endpoint " + info.endpoint.id);
                        }
                    };

                    // setup some empty endpoints.  again note the use of the three-arg method to reuse all the parameters except the location
                    // of the anchor (purely because we want to move the anchor around here; you could set it one time and forget about it though.)
                    //var e1 = this.instance.addEndpoint('1', { anchor: [0.5, 1, 0, 1] }, this.exampleEndpoint2);

                    // setup some DynamicAnchors for use with the blue endpoints
                    // and a function to set as the maxConnections callback.
                    var anchors = [[1, 0.2, 1, 0], [0.8, 1, 0, 1], [0, 0.8, -1, 0], [0.2, 0, 0, -1]];

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
                        this.innerHTML = s ? "disable dragging" : "enable dragging";
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
                        alert("click!");
                    });
                });

                self.inited = true;
            }
        };
        this.addPeer = function (d) {
            debugger;
            jQuery("<div/>", {
                "class": "window",
                id: d
            }).appendTo("#drag-drop-demo");
            $("<br/>").appendTo("#" + d);
            $("<div/>", { html: d }).appendTo("#" + d);
            $("<a/>", { href: "#", "class": "cmdLink hide", rel: "dragDropWindow" + d,
                html: "toggle connections"
            }).appendTo("#" + d);

            $("<br/>").appendTo("#" + d);

            $("<a/>", { href: "#", "class": "cmdLink drag", rel: "dragDropWindow" + d,
                html: "disable dragging"
            }).appendTo("#" + d);
            $("<br/>").appendTo("#" + d);
            $("<a/>", { href: "#", "class": "cmdLink detach", rel: "dragDropWindow" + d,
                html: "detach all"
            }).appendTo("#" + d);
            $("<br/>").appendTo("#" + d);

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

            var anchors = [[1, 0.2, 1, 0], [0.8, 1, 0, 1], [0, 0.8, -1, 0], [0.2, 0, 0, -1]];
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
                this.innerHTML = s ? "disable dragging" : "enable dragging";
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
        };
    }();
    $(document).on("buyanLoaded", function () {
        //window.messages.peer=true;
        window.messages.npeer = true;
        //window.a.init()
        $(document).on("npeer", function (ev, d) {
            console.log("xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx", d);
            //window.messages.peer=false;

            window.a.addPeer(d);
        });
    });
    //window.fja();
    //$('#drag-drop-demo').

    //$(document).trigger( "pubsub",{"typ": "peer", "msg": d});
    //window.messages.peer=true;
    //window.messages.npeer=false;

});
define('bapp/assets/javascripts/stun', function () {

    'use strict';

    function Stun() {
        var a = [{ url: "stun:stun01.sipphone.com" }, { url: "stun:stun.ekiga.net" }, { url: "stun:stun.fwdnet.net" }, { url: "stun:stun.ideasip.com" }, { url: "stun:stun.iptel.org" }, { url: "stun:stun.rixtelecom.se" }, { url: "stun:stun.schlund.de" }, { url: "stun:stun.l.google.com:19302" }, { url: "stun:stun1.l.google.com:19302" }, { url: "stun:stun2.l.google.com:19302" }, { url: "stun:stun3.l.google.com:19302" }, { url: "stun:stun4.l.google.com:19302" }, { url: "stun:stunserver.org" }, { url: "stun:stun.softjoys.com" }, { url: "stun:stun.voiparound.com" }, { url: "stun:stun.voipbuster.com" }, { url: "stun:stun.voipstunt.com" }, { url: "stun:stun.voxgratia.org" }, { url: "stun:stun.xten.com" }, {
            url: "turn:numb.viagenie.ca",
            credential: "muazkh",
            username: "webrtc@live.com"
        }, {
            url: "turn:192.158.29.39:3478?transport=udp",
            credential: "JZEOEt2V3Qb0y27GRntt2u2PAYA=",
            username: "28224511:1379330808"
        }, {
            url: "turn:192.158.29.39:3478?transport=tcp",
            credential: "JZEOEt2V3Qb0y27GRntt2u2PAYA=",
            username: "28224511:1379330808"
        }];
        function getRandomInt(min, max) {
            return Math.floor(Math.random() * (max - min)) + min;
        }

        return a[getRandomInt(0, a.length)];
    }

});
define('bapp/assets/javascripts/wrkr', function () {

  'use strict';

  // crypto.subtle.digest({name: "SHA-256"}, buffer)
  //      .then(function (digest) {
  function encode(str) {
    return new TextEncoder("utf-8").encode(str);
  }

  function decode(buf) {
    return new TextDecoder("utf-8").decode(buf);
  }
  function checkH(buf1, difficulty) {
    var a = new Uint8Array(buf1);

    for (var i = 0; i < a.byteLength && i < difficulty; i++) {
      if (a[i] !== 0) {
        return false;
      }
    }

    return true;
  }
  function compare(buf1, buf2) {
    var a = new Uint8Array(buf1);
    var b = new Uint8Array(buf2);

    if (a.byteLength != b.byteLength) {
      return false;
    }

    for (var i = 0; i < a.byteLength; i++) {
      if (a[i] !== b[i]) {
        return false;
      }
    }

    return true;
  }

  /*
  dummy miner
  TODO put script loop instead of timeout
  */
  var run = false;
  var hash = "asdsad";
  var nonce = "nonce";
  var difficulty = 1;
  var run = true;
  var not_found = false;

  var nonce = false;
  var merkleRoot = "root";
  var difficulty = 1;
  var run = true;
  var found = false;
  function arrayBToString(digest) {
    function _arrayBufferToBase64(buffer) {
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
    return str;
  }
  function encode(str) {
    return new TextEncoder("utf-8").encode(str);
  }

  function decode(buf) {
    return new TextDecoder("utf-8").decode(buf);
  }
  function checkH(buf1, difficulty) {
    var a = new Uint8Array(buf1);

    for (var i = 0; i < a.byteLength && i < difficulty; i++) {
      if (a[i] !== 0) {
        return false;
      }
    }

    return arrayBToString(a);
  }

  onmessage = function (event) {
    console.log("recieved work ", event.data.args);
    switch (event.data.args[0]) {
      case "newjob":
        merkleRoot = event.data.args[1];
        run = true;
        nonce = false;
        found = false;
        //compute_hash(hash)
        (function itteration() {
          if (run && !found) {
            nonce = nonce + 1;
            crypto.subtle.digest({ name: "SHA-256" }, encode(merkleRoot + nonce)).then(function (digest) {
              found = checkH(digest, difficulty);
              itteration();
              return;
            });
          }
          if (run == true && found) {
            setTimeout(function () {
              //{'root':event.data, 'nonce': 101}
              var messag = JSON.stringify({ root: "somehash: " + merkleRoot,
                nonce: "somenonce " + nonce,
                newhash: found });
              console.log(messag);
              postMessage(messag);
            }, 0);
          }
        })();
        break;
      case "stop":
        run = false;
        break;
      case "start":
        run = true;
        break;
      default:
        console.log("no message");
    }
  };

});
define('bapp/components/buyan-graph', ['exports', 'ember', 'bapp/templates/components/buyan-graph'], function (exports, Ember, layout) {

  'use strict';

  function Network(id) {
    var g = { nodes: [], edges: [] };
    var self = this;
    sigma.renderers.def = sigma.renderers.canvas;
    var s = new sigma({
      graph: g,
      container: id
    });
    self.s = s;
    sigma.plugins.dragNodes(s, s.renderers[0]);
    function mknode(labl, x, y) {
      x = x || Math.random();
      y = y || Math.random();

      var i = i || 0;
      var label = labl;
      return {
        id: label,
        label: label,
        x: x,
        y: y,
        size: 100,
        color: "#676"
      };
    }
    function f(data) {
      var d = data.content;
      for (var i = 0; i < d.length; i++) {

        window.s.graph.addNode(mknode(d[i].get("handle")));
      }

      window.s.refresh();
    }

    function connectToAllInitial() {
      for (var i = 0; i < window.s.graph.nodes().length - 1; i++) {
        $(document).trigger("call", {
          typ: "connectTo",
          msg: [window.s.graph.nodes()[i].id] });
        window.s.graph.addEdge({
          id: "e" + i,
          source: window.s.graph.nodes()[window.s.graph.nodes().length - 1].id,
          target: window.s.graph.nodes()[i].id,
          size: 1,
          color: "#ccc"
        });
      }
    }
    function addMef(ev, myId) {
      connectToAllInitial();
      s.refresh();
    }
    self.addMe = function (myId) {
      debugger;
      self.me = myId;
      self.s.graph.addNode(mknode(myId));

      self.s.refresh();
    };
    self.addNode = function (name, x, y) {
      var x = Math.random();
      var y = Math.sqrt(1 - x * x) * (Math.random() > 0.5 ? -1 : 1);
      self.s.graph.addNode(mknode(name, x, y));
      self.s.graph.addEdge({
        id: "e-" + self.me + "-" + name,
        source: self.me,
        target: name,
        size: 1,
        color: "#ccc"
      });
      self.s.refresh();
    };
    $(document).on("UINewPeer", function (ev, id) {
      self.addNode(id);
    });
    $(document).on("setID", addMef);
  }
  exports['default'] = Ember['default'].Component.extend({
    layout: layout['default'],

    networkId: "" + (Math.floor(Math.random() * (10000000 - 0)) + 0),
    networkName: "",
    renderR: null,
    newId: "",
    newIdDesc: "",
    setupTooltip: (function () {
      //this.set("")
      var self = this;
      $(".search-select").dropdown({
        onChange: function onChange(value, text, $selectedItem) {
          debugger;
          self.set("myId", text.replace(" ", ""));
        } });
      var networkId = this.get("networkId");
      var renderR = new Network("networkView");
      this.set("renderR", renderR);
      this.set("networkName", networkId);
      $(document).on("UINewPeer", function (ev, id) {});
      /*
      var Ids=this.get('targetObject.store').findAll('ids').then(function(){
      
        this.set('ids',Ids);
      });
      */
    }).on("didInsertElement"),
    actions: {
      getpeers: function getpeers() {
        this.sendAction("getpeersc");
        return true;
      },
      makenewid: function makenewid(name, desc) {
        this.sendAction("newid", name, desc);
        return true;
      },
      setMyIdForThisNetwork: function setMyIdForThisNetwork() {
        this.get("renderR").addMe(this.get("myId"));
        $(document).trigger("setid", this.get("myId"));
        //console.log(id);
      }
    },
    computedProp: (function () {
      debugger;
      return "width" + this.get("peerl");
    }).observes("peerl")

  });

});
define('bapp/components/material-design-icon', ['exports', 'ember'], function (exports, Ember) {

  'use strict';

  exports['default'] = Ember['default'].Component.extend({
    tagName: "span",
    name: "",
    size: null,
    iconClass: null,
    spin: false,
    spinReverse: false,
    flip: null,
    rotate: null,

    iconClassDeclaration: (function () {
      return [mdPrefix(this.get("name")), mdPrefix(this.get("size")), getRotateClassDeclaration(this.get("rotate")), getFlipClassDeclaration(this.get("flip")), getSpinningClassDeclaration(this.get("spin"), this.get("spinReverse")), this.get("iconClass")].compact().join(" ");
    }).property("name", "size", "iconClass", "spin", "spinDirection") });

  function getSpinningClassDeclaration(spin, spinReverse) {
    if (spinReverse) {
      return "mdi-spin mdi-spin-reverse";
    } else if (spin) {
      return "mdi-spin";
    } else {
      return null;
    }
  }

  function getRotateClassDeclaration(rotate) {
    if (rotate) {
      return mdPrefix("rotate-" + rotate);
    } else {
      return null;
    }
  }

  function getFlipClassDeclaration(flip) {
    if (flip) {
      return mdPrefix("flip-" + flip);
    } else {
      return null;
    }
  }

  function mdPrefix(className) {
    if (className) {
      return "mdi-" + className;
    } else {
      return null;
    }
  }

});
define('bapp/controllers/scratch', ['exports', 'ember'], function (exports, Ember) {

  'use strict';

  exports['default'] = Ember['default'].Controller.extend({

    newN: "",
    init: function init() {

      var peer = this.store.findAll("peer");
      this.set("peers", peer);
    },
    networkOn: false,
    playMedia: (function () {
      debugger;
    }).on("newid"),
    actions: {
      networkToggle: function networkToggle() {
        //debugger;
        this.set("networkOn", !this.get("networkOn"));
      },
      newid: function newid(name, desc) {
        //debugger;
        this.store.createRecord("id", { name: name, desc: desc, dta: "" });
      },
      menuToggle: function menuToggle() {
        $(".left.sidebar").sidebar("setting", "dimPage", false).sidebar("toggle");
      },
      getpeersc: function getpeersc() {
        function m() {
          return { model: this.store.findAll("scratch") };
        };
        //this.set("model", this.store.findAll('scratch'));
        var accountObj = this.store.findAll("peer");

        this.set("peers", accountObj);
        /*
        accountObj.then( function (r) {
             debugger;
            self.set('peers', r);
        });
        */

        //this.transitionToRoute('scratch',this.store.findAll('scratch'));
        //this.get('target.router').refresh();
      },
      selectNetwork: function selectNetwork(network) {
        var n = this.get("networkSelected");
        if (n && n == network) {
          this.set("networkSelected", null);
        } else {
          this.set("networkSelected", network);
        }
      },
      getsetpeers: function getsetpeers() {
        debugger;

        var post = store.createRecord("scratch", {
          pper: "Rails is Omakase"
        });
        post.save();
      },
      makenewid: function makenewid(a) {
        debugger;
      },
      newNetwork: function newNetwork(newNetworkName) {

        this.store.createRecord("network", { name: newNetworkName, myIdentity: null, peers: [] });
      },
      deleteNetwork: function deleteNetwork(name) {

        debugger;
        this.store.find("network", name).then(function (post) {
          post.deleteRecord();
          post.get("isDeleted"); // => true
          post.save(); // => DELETE to /posts/1
        });
      }

    }
  });

});
define('bapp/initializers/app-version', ['exports', 'bapp/config/environment', 'ember'], function (exports, config, Ember) {

  'use strict';

  var classify = Ember['default'].String.classify;

  exports['default'] = {
    name: "App Version",
    initialize: function initialize(container, application) {
      var appName = classify(application.toString());
      Ember['default'].libraries.register(appName, config['default'].APP.version);
    }
  };

});
define('bapp/initializers/export-application-global', ['exports', 'ember', 'bapp/config/environment'], function (exports, Ember, config) {

  'use strict';

  exports.initialize = initialize;

  function initialize(container, application) {
    var classifiedName = Ember['default'].String.classify(config['default'].modulePrefix);

    if (config['default'].exportApplicationGlobal && !window[classifiedName]) {
      window[classifiedName] = application;
    }
  }

  ;

  exports['default'] = {
    name: "export-application-global",

    initialize: initialize
  };

});
define('bapp/models/id', ['exports', 'ember-data'], function (exports, DS) {

  'use strict';

  var Id = DS['default'].Model.extend({
    name: DS['default'].attr("string"),
    desc: DS['default'].attr("string"),
    dta: DS['default'].attr()
  });
  Id.reopenClass({
    FIXTURES: [{ id: 1, name: "Trek", desc: "Glowacki", dta: "" }, { id: 2, name: "Tom", desc: "Dale", dta: "" }]
  });
  exports['default'] = Id;

});
define('bapp/models/network', ['exports', 'ember-data'], function (exports, DS) {

  'use strict';

  var Network = DS['default'].Model.extend({

    name: DS['default'].attr(),
    myIdentity: DS['default'].attr(),
    peers: DS['default'].attr() //DS.hasMany('peer',{async:true})	
  });
  Network.reopenClass({
    FIXTURES: [{ id: 1, name: "network einz", myIdentity: "2", peers: [] }, { id: 2, name: "n zweye", myIdentity: "1", peers: [] }]
  });
  exports['default'] = Network;

});
define('bapp/models/peer', ['exports', 'ember-data'], function (exports, DS) {

  'use strict';

  var Peer = DS['default'].Model.extend({
    name: DS['default'].attr("string"),
    desc: DS['default'].attr("string"),
    dta: DS['default'].attr()
  });
  Peer.reopenClass({
    FIXTURES: [{ id: 1, name: "James", desc: "Glowacki", dta: "" }, { id: 2, name: "John1", desc: "Dale", dta: "" }, { id: 3, name: "John2", desc: "Dale", dta: "" }, { id: 4, name: "John3", desc: "Dale", dta: "" }, { id: 5, name: "John4", desc: "Dale", dta: "" }, { id: 6, name: "John5", desc: "Dale", dta: "" }]
  });
  exports['default'] = Peer;

});
define('bapp/models/scratch', ['exports', 'ember-data'], function (exports, DS) {

  'use strict';

  var attr = DS['default'].attr;
  exports['default'] = DS['default'].Model.extend({
    pper: attr()
  });

});
define('bapp/router', ['exports', 'ember', 'bapp/config/environment'], function (exports, Ember, config) {

  'use strict';

  var Router = Ember['default'].Router.extend({
    location: config['default'].locationType
  });

  Router.map(function () {
    this.route("scratch");
    this.resource("peers");
  });

  exports['default'] = Router;

});
define('bapp/routes/peers', ['exports', 'ember'], function (exports, Ember) {

	'use strict';

	exports['default'] = Ember['default'].Route.extend({});

});
define('bapp/routes/scratch', ['exports', 'ember'], function (exports, Ember) {

   'use strict';

   exports['default'] = Ember['default'].Route.extend({

      model: function model() {

         return {
            model: this.store.findAll("scratch"),
            ids: this.store.findAll("id"),
            networks: this.store.findAll("network")
         };
      },

      actions: {
         makenewid: function makenewid(a) {
            debugger;
         }

      }
      //   setupController: function(controller, song) {
      // Set the IndexController's `title`
      //   controller.set('title', 'My App');
      //       controller.set('model', song);
      //   }
   });

});
define('bapp/serializers/peer', ['exports', 'ember-data'], function (exports, DS) {

  'use strict';

  exports['default'] = DS['default'].RESTSerializer.extend({
    normalizePayload: function normalizePayload(payload) {
      debugger;
      var i = 0;
      function isJson(str) {
        try {
          JSON.parse(str);
        } catch (e) {
          return false;
        }
        return true;
      }
      var p = payload.map(function (v, i, a) {

        var parsed = { id: i, name: v, desc: "Glowacki", dta: "" };
        return parsed;
      });

      return { peer: p };
    }
  });

});
define('bapp/serializers/scratch', ['exports', 'ember-data'], function (exports, DS) {

  'use strict';

  exports['default'] = DS['default'].RESTSerializer.extend({
    /*
    extractFindAll: function (store, type, payload, id, requestType) {
         debugger;
      var p= payload.map(function(v,i,a){
         debugger;
        return {id:1,handle: v,ip:'',peers:[]}
      });
      debugger;
      this._super(store, type, p, id, requestType)
    },
      */
    normalizePayload: function normalizePayload(payload) {
      var i = 0;
      function isJson(str) {
        try {
          JSON.parse(str);
        } catch (e) {
          return false;
        }
        return true;
      }
      payload = _.uniq(payload);
      payload = payload.filter(function (v, i, a) {
        return v !== "" && isJson(v);
      });
      var p = payload.map(function (v, i, a) {

        //debugger;
        var parsed = JSON.parse(v);
        parsed.id = i;
        return parsed;
      });

      return { scratch: p };
    }
  });

});
define('bapp/templates/application', ['exports'], function (exports) {

  'use strict';

  exports['default'] = Ember.HTMLBars.template((function() {
    return {
      isHTMLBars: true,
      blockParams: 0,
      cachedFragment: null,
      hasRendered: false,
      build: function build(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createTextNode("\n	");
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("");
        dom.appendChild(el0, el1);
        return el0;
      },
      render: function render(context, env, contextualElement) {
        var dom = env.dom;
        var hooks = env.hooks, content = hooks.content;
        dom.detectNamespace(contextualElement);
        var fragment;
        if (env.useFragmentCache && dom.canClone) {
          if (this.cachedFragment === null) {
            fragment = this.build(dom);
            if (this.hasRendered) {
              this.cachedFragment = fragment;
            } else {
              this.hasRendered = true;
            }
          }
          if (this.cachedFragment) {
            fragment = dom.cloneNode(this.cachedFragment, true);
          }
        } else {
          fragment = this.build(dom);
        }
        if (this.cachedFragment) { dom.repairClonedNode(fragment,[1]); }
        var morph0 = dom.createMorphAt(fragment,0,1,contextualElement);
        content(env, morph0, context, "outlet");
        return fragment;
      }
    };
  }()));

});
define('bapp/templates/components/buyan-graph', ['exports'], function (exports) {

  'use strict';

  exports['default'] = Ember.HTMLBars.template((function() {
    var child0 = (function() {
      return {
        isHTMLBars: true,
        blockParams: 0,
        cachedFragment: null,
        hasRendered: false,
        build: function build(dom) {
          var el0 = dom.createDocumentFragment();
          var el1 = dom.createTextNode("          ");
          dom.appendChild(el0, el1);
          var el1 = dom.createElement("div");
          dom.setAttribute(el1,"class","item");
          var el2 = dom.createTextNode(" ");
          dom.appendChild(el1, el2);
          dom.appendChild(el0, el1);
          var el1 = dom.createTextNode("\n");
          dom.appendChild(el0, el1);
          return el0;
        },
        render: function render(context, env, contextualElement) {
          var dom = env.dom;
          var hooks = env.hooks, get = hooks.get, element = hooks.element, content = hooks.content;
          dom.detectNamespace(contextualElement);
          var fragment;
          if (env.useFragmentCache && dom.canClone) {
            if (this.cachedFragment === null) {
              fragment = this.build(dom);
              if (this.hasRendered) {
                this.cachedFragment = fragment;
              } else {
                this.hasRendered = true;
              }
            }
            if (this.cachedFragment) {
              fragment = dom.cloneNode(this.cachedFragment, true);
            }
          } else {
            fragment = this.build(dom);
          }
          var element0 = dom.childAt(fragment, [1]);
          var morph0 = dom.createMorphAt(element0,-1,0);
          var morph1 = dom.createMorphAt(element0,0,-1);
          element(env, element0, context, "bind-attr", [], {"data-value": get(env, context, "id.id")});
          content(env, morph0, context, "id.name");
          content(env, morph1, context, "id.desc");
          return fragment;
        }
      };
    }());
    return {
      isHTMLBars: true,
      blockParams: 0,
      cachedFragment: null,
      hasRendered: false,
      build: function build(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createTextNode("\n\n");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("div");
        dom.setAttribute(el1,"class","row");
        var el2 = dom.createTextNode("\n  ");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("div");
        dom.setAttribute(el2,"class","ui right labeled left icon input");
        var el3 = dom.createTextNode("\n    ");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("i");
        dom.setAttribute(el3,"class","tags icon");
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n    ");
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n    ");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("div");
        dom.setAttribute(el3,"class","ui tag label");
        var el4 = dom.createTextNode("\n      network name\n    ");
        dom.appendChild(el3, el4);
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n  ");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n      \n");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("div");
        dom.setAttribute(el1,"class","row");
        var el2 = dom.createTextNode("\n\n    ");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("div");
        dom.setAttribute(el2,"class","ui fluid search selection dropdown search-select");
        var el3 = dom.createTextNode("\n      ");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("input");
        dom.setAttribute(el3,"name","country");
        dom.setAttribute(el3,"type","hidden");
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n      ");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("i");
        dom.setAttribute(el3,"class","dropdown icon");
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n      ");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("div");
        dom.setAttribute(el3,"class","default text");
        var el4 = dom.createTextNode("Select Your Handle");
        dom.appendChild(el3, el4);
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n      ");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("div");
        dom.setAttribute(el3,"class","menu");
        var el4 = dom.createTextNode("\n");
        dom.appendChild(el3, el4);
        var el4 = dom.createTextNode("        ");
        dom.appendChild(el3, el4);
        var el4 = dom.createElement("div");
        dom.setAttribute(el4,"class","ui input");
        var el5 = dom.createTextNode("\n          ");
        dom.appendChild(el4, el5);
        var el5 = dom.createTextNode("\n          ");
        dom.appendChild(el4, el5);
        var el5 = dom.createTextNode("\n          ");
        dom.appendChild(el4, el5);
        var el5 = dom.createElement("button");
        dom.setAttribute(el5,"class","button");
        var el6 = dom.createTextNode("make");
        dom.appendChild(el5, el6);
        dom.appendChild(el4, el5);
        var el5 = dom.createTextNode("\n        ");
        dom.appendChild(el4, el5);
        dom.appendChild(el3, el4);
        var el4 = dom.createTextNode("\n        \n      ");
        dom.appendChild(el3, el4);
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n    ");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("div");
        dom.setAttribute(el1,"class","row ");
        var el2 = dom.createTextNode("\n  ");
        dom.appendChild(el1, el2);
        var el2 = dom.createComment(" network controll ");
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n\n \n  ");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("button");
        dom.setAttribute(el2,"class","ui button enterbuyan");
        var el3 = dom.createTextNode("enter network");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n\n  ");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("button");
        dom.setAttribute(el2,"class","ui button pullpeersfromserver");
        var el3 = dom.createTextNode("\n      pull peers from server\n  ");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n  ");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("button");
        dom.setAttribute(el2,"class","ui button getsomepeer");
        var el3 = dom.createTextNode("\n      tell server I want some peers\n  ");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n  ");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("button");
        dom.setAttribute(el2,"class","ui button getmeonemorepeer");
        var el3 = dom.createTextNode("\n      get me one more peer\n  ");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        var el1 = dom.createComment(" network controll ");
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n  \n\n\n\n");
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        return el0;
      },
      render: function render(context, env, contextualElement) {
        var dom = env.dom;
        var hooks = env.hooks, get = hooks.get, inline = hooks.inline, element = hooks.element, block = hooks.block, content = hooks.content;
        dom.detectNamespace(contextualElement);
        var fragment;
        if (env.useFragmentCache && dom.canClone) {
          if (this.cachedFragment === null) {
            fragment = this.build(dom);
            if (this.hasRendered) {
              this.cachedFragment = fragment;
            } else {
              this.hasRendered = true;
            }
          }
          if (this.cachedFragment) {
            fragment = dom.cloneNode(this.cachedFragment, true);
          }
        } else {
          fragment = this.build(dom);
        }
        var element1 = dom.childAt(fragment, [3, 1]);
        var element2 = dom.childAt(element1, [1]);
        var element3 = dom.childAt(element1, [7]);
        var element4 = dom.childAt(element3, [2]);
        var element5 = dom.childAt(element4, [3]);
        var element6 = dom.childAt(fragment, [5]);
        var element7 = dom.childAt(element6, [3]);
        var element8 = dom.childAt(element6, [5]);
        var element9 = dom.childAt(element6, [7]);
        var element10 = dom.childAt(element6, [9]);
        var morph0 = dom.createMorphAt(dom.childAt(fragment, [1, 1]),2,3);
        var morph1 = dom.createMorphAt(element3,0,1);
        var morph2 = dom.createMorphAt(element4,0,1);
        var morph3 = dom.createMorphAt(element4,1,2);
        var morph4 = dom.createMorphAt(fragment,8,9,contextualElement);
        inline(env, morph0, context, "input", [], {"class": "networkName", "placeholder": "network name  ", "value": get(env, context, "network.name")});
        element(env, element2, context, "bind-attr", [], {"value": get(env, context, "myId")});
        block(env, morph1, context, "each", [get(env, context, "ids")], {"keyword": "id"}, child0, null);
        inline(env, morph2, context, "input", [], {"placeholder": "new id name", "class": "ui input search_box myId ", "value": get(env, context, "newId")});
        inline(env, morph3, context, "input", [], {"placeholder": "description", "class": "ui input search_box myId ", "value": get(env, context, "newIdDesc")});
        element(env, element5, context, "action", ["makenewid", get(env, context, "newId"), get(env, context, "newIdDesc")], {});
        element(env, element7, context, "action", ["setMyIdForThisNetwork", get(env, context, "myId")], {});
        element(env, element8, context, "action", ["getpeers"], {});
        element(env, element9, context, "action", ["getsetpeers"], {});
        element(env, element10, context, "action", ["getmeonemorepeer"], {});
        content(env, morph4, context, "yield");
        return fragment;
      }
    };
  }()));

});
define('bapp/templates/components/material-design-icon', ['exports'], function (exports) {

  'use strict';

  exports['default'] = Ember.HTMLBars.template((function() {
    return {
      isHTMLBars: true,
      blockParams: 0,
      cachedFragment: null,
      hasRendered: false,
      build: function build(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createElement("i");
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        return el0;
      },
      render: function render(context, env, contextualElement) {
        var dom = env.dom;
        var hooks = env.hooks, get = hooks.get, element = hooks.element;
        dom.detectNamespace(contextualElement);
        var fragment;
        if (env.useFragmentCache && dom.canClone) {
          if (this.cachedFragment === null) {
            fragment = this.build(dom);
            if (this.hasRendered) {
              this.cachedFragment = fragment;
            } else {
              this.hasRendered = true;
            }
          }
          if (this.cachedFragment) {
            fragment = dom.cloneNode(this.cachedFragment, true);
          }
        } else {
          fragment = this.build(dom);
        }
        var element0 = dom.childAt(fragment, [0]);
        element(env, element0, context, "bind-attr", [], {"class": get(env, context, "iconClassDeclaration")});
        return fragment;
      }
    };
  }()));

});
define('bapp/templates/peers', ['exports'], function (exports) {

  'use strict';

  exports['default'] = Ember.HTMLBars.template((function() {
    return {
      isHTMLBars: true,
      blockParams: 0,
      cachedFragment: null,
      hasRendered: false,
      build: function build(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createTextNode("");
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        return el0;
      },
      render: function render(context, env, contextualElement) {
        var dom = env.dom;
        var hooks = env.hooks, content = hooks.content;
        dom.detectNamespace(contextualElement);
        var fragment;
        if (env.useFragmentCache && dom.canClone) {
          if (this.cachedFragment === null) {
            fragment = this.build(dom);
            if (this.hasRendered) {
              this.cachedFragment = fragment;
            } else {
              this.hasRendered = true;
            }
          }
          if (this.cachedFragment) {
            fragment = dom.cloneNode(this.cachedFragment, true);
          }
        } else {
          fragment = this.build(dom);
        }
        if (this.cachedFragment) { dom.repairClonedNode(fragment,[0]); }
        var morph0 = dom.createMorphAt(fragment,0,1,contextualElement);
        content(env, morph0, context, "outlet");
        return fragment;
      }
    };
  }()));

});
define('bapp/templates/scratch', ['exports'], function (exports) {

  'use strict';

  exports['default'] = Ember.HTMLBars.template((function() {
    var child0 = (function() {
      var child0 = (function() {
        return {
          isHTMLBars: true,
          blockParams: 0,
          cachedFragment: null,
          hasRendered: false,
          build: function build(dom) {
            var el0 = dom.createDocumentFragment();
            var el1 = dom.createTextNode("					");
            dom.appendChild(el0, el1);
            var el1 = dom.createElement("div");
            dom.setAttribute(el1,"class","item");
            var el2 = dom.createTextNode("\n						");
            dom.appendChild(el1, el2);
            var el2 = dom.createElement("div");
            dom.setAttribute(el2,"class","content ui right icon");
            var el3 = dom.createTextNode("\n							");
            dom.appendChild(el2, el3);
            var el3 = dom.createTextNode("\n							");
            dom.appendChild(el2, el3);
            var el3 = dom.createElement("i");
            dom.setAttribute(el3,"class","right remove icon");
            dom.appendChild(el2, el3);
            var el3 = dom.createTextNode(" \n							\n\n						");
            dom.appendChild(el2, el3);
            dom.appendChild(el1, el2);
            var el2 = dom.createTextNode("\n					");
            dom.appendChild(el1, el2);
            dom.appendChild(el0, el1);
            var el1 = dom.createTextNode("\n");
            dom.appendChild(el0, el1);
            return el0;
          },
          render: function render(context, env, contextualElement) {
            var dom = env.dom;
            var hooks = env.hooks, get = hooks.get, element = hooks.element, content = hooks.content;
            dom.detectNamespace(contextualElement);
            var fragment;
            if (env.useFragmentCache && dom.canClone) {
              if (this.cachedFragment === null) {
                fragment = this.build(dom);
                if (this.hasRendered) {
                  this.cachedFragment = fragment;
                } else {
                  this.hasRendered = true;
                }
              }
              if (this.cachedFragment) {
                fragment = dom.cloneNode(this.cachedFragment, true);
              }
            } else {
              fragment = this.build(dom);
            }
            var element9 = dom.childAt(fragment, [1]);
            var element10 = dom.childAt(element9, [1]);
            var element11 = dom.childAt(element10, [2]);
            var morph0 = dom.createMorphAt(element10,0,1);
            element(env, element9, context, "action", ["selectNetwork", get(env, context, "network")], {});
            content(env, morph0, context, "network.name");
            element(env, element11, context, "action", ["deleteNetwork", get(env, context, "network.id")], {});
            return fragment;
          }
        };
      }());
      var child1 = (function() {
        return {
          isHTMLBars: true,
          blockParams: 0,
          cachedFragment: null,
          hasRendered: false,
          build: function build(dom) {
            var el0 = dom.createDocumentFragment();
            var el1 = dom.createTextNode("					");
            dom.appendChild(el0, el1);
            var el1 = dom.createElement("div");
            var el2 = dom.createTextNode("\n						");
            dom.appendChild(el1, el2);
            var el2 = dom.createElement("div");
            dom.setAttribute(el2,"class","list");
            var el3 = dom.createTextNode("\n							");
            dom.appendChild(el2, el3);
            var el3 = dom.createElement("div");
            dom.setAttribute(el3,"class","item serverPeers");
            var el4 = dom.createTextNode("\n								");
            dom.appendChild(el3, el4);
            var el4 = dom.createElement("i");
            dom.setAttribute(el4,"class","right triangle icon");
            dom.appendChild(el3, el4);
            var el4 = dom.createTextNode("\n								");
            dom.appendChild(el3, el4);
            var el4 = dom.createElement("div");
            dom.setAttribute(el4,"class","content");
            var el5 = dom.createTextNode("\n									");
            dom.appendChild(el4, el5);
            var el5 = dom.createElement("a");
            dom.setAttribute(el5,"class","header");
            dom.appendChild(el4, el5);
            var el5 = dom.createTextNode("\n									");
            dom.appendChild(el4, el5);
            var el5 = dom.createElement("div");
            dom.setAttribute(el5,"class","description");
            dom.appendChild(el4, el5);
            var el5 = dom.createTextNode("\n								");
            dom.appendChild(el4, el5);
            dom.appendChild(el3, el4);
            var el4 = dom.createTextNode("\n							");
            dom.appendChild(el3, el4);
            dom.appendChild(el2, el3);
            var el3 = dom.createTextNode("\n						");
            dom.appendChild(el2, el3);
            dom.appendChild(el1, el2);
            var el2 = dom.createTextNode("\n						\n					");
            dom.appendChild(el1, el2);
            dom.appendChild(el0, el1);
            var el1 = dom.createTextNode("\n");
            dom.appendChild(el0, el1);
            return el0;
          },
          render: function render(context, env, contextualElement) {
            var dom = env.dom;
            var hooks = env.hooks, get = hooks.get, element = hooks.element, content = hooks.content;
            dom.detectNamespace(contextualElement);
            var fragment;
            if (env.useFragmentCache && dom.canClone) {
              if (this.cachedFragment === null) {
                fragment = this.build(dom);
                if (this.hasRendered) {
                  this.cachedFragment = fragment;
                } else {
                  this.hasRendered = true;
                }
              }
              if (this.cachedFragment) {
                fragment = dom.cloneNode(this.cachedFragment, true);
              }
            } else {
              fragment = this.build(dom);
            }
            var element7 = dom.childAt(fragment, [1]);
            var element8 = dom.childAt(element7, [1, 1, 3]);
            var morph0 = dom.createMorphAt(dom.childAt(element8, [1]),-1,-1);
            var morph1 = dom.createMorphAt(dom.childAt(element8, [3]),-1,-1);
            element(env, element7, context, "action", ["edit", get(env, context, "person")], {});
            content(env, morph0, context, "peer.name");
            content(env, morph1, context, "peer.desc");
            return fragment;
          }
        };
      }());
      return {
        isHTMLBars: true,
        blockParams: 0,
        cachedFragment: null,
        hasRendered: false,
        build: function build(dom) {
          var el0 = dom.createDocumentFragment();
          var el1 = dom.createElement("div");
          dom.setAttribute(el1,"class","ui wide left vertical inverted labeled icon sidebar menu");
          var el2 = dom.createTextNode("\n	");
          dom.appendChild(el1, el2);
          var el2 = dom.createElement("a");
          dom.setAttribute(el2,"class","item");
          var el3 = dom.createTextNode("\n		");
          dom.appendChild(el2, el3);
          var el3 = dom.createElement("i");
          dom.setAttribute(el3,"class","home icon");
          dom.appendChild(el2, el3);
          var el3 = dom.createTextNode("\n		Home\n	");
          dom.appendChild(el2, el3);
          dom.appendChild(el1, el2);
          var el2 = dom.createTextNode("\n	");
          dom.appendChild(el1, el2);
          var el2 = dom.createElement("div");
          dom.setAttribute(el2,"class","item");
          var el3 = dom.createTextNode("\n		");
          dom.appendChild(el2, el3);
          var el3 = dom.createElement("div");
          dom.setAttribute(el3,"class","ui  fluid accordion");
          var el4 = dom.createTextNode("\n			");
          dom.appendChild(el3, el4);
          var el4 = dom.createElement("div");
          dom.setAttribute(el4,"class","ui active title");
          var el5 = dom.createTextNode("\n				");
          dom.appendChild(el4, el5);
          var el5 = dom.createElement("i");
          dom.setAttribute(el5,"class","dropdown icon");
          dom.appendChild(el4, el5);
          var el5 = dom.createTextNode("\n				Networks\n			");
          dom.appendChild(el4, el5);
          dom.appendChild(el3, el4);
          var el4 = dom.createTextNode("\n			");
          dom.appendChild(el3, el4);
          var el4 = dom.createElement("div");
          dom.setAttribute(el4,"class","active content ");
          var el5 = dom.createTextNode("\n				");
          dom.appendChild(el4, el5);
          var el5 = dom.createElement("div");
          dom.setAttribute(el5,"class","ui small   header");
          var el6 = dom.createTextNode("\n					\n					\n					");
          dom.appendChild(el5, el6);
          var el6 = dom.createElement("div");
          dom.setAttribute(el6,"class","mini ui action left icon input ");
          var el7 = dom.createTextNode("\n						");
          dom.appendChild(el6, el7);
          var el7 = dom.createElement("input");
          dom.setAttribute(el7,"type","text");
          dom.setAttribute(el7,"placeholder","Search...");
          dom.appendChild(el6, el7);
          var el7 = dom.createTextNode("\n					");
          dom.appendChild(el6, el7);
          dom.appendChild(el5, el6);
          var el6 = dom.createTextNode("\n				");
          dom.appendChild(el5, el6);
          dom.appendChild(el4, el5);
          var el5 = dom.createTextNode("\n				");
          dom.appendChild(el4, el5);
          var el5 = dom.createElement("div");
          dom.setAttribute(el5,"class","menu ");
          var el6 = dom.createTextNode("\n");
          dom.appendChild(el5, el6);
          var el6 = dom.createTextNode("					");
          dom.appendChild(el5, el6);
          var el6 = dom.createElement("div");
          dom.setAttribute(el6,"class","item");
          var el7 = dom.createTextNode("\n						");
          dom.appendChild(el6, el7);
          var el7 = dom.createElement("div");
          dom.setAttribute(el7,"class","content");
          var el8 = dom.createTextNode("\n							");
          dom.appendChild(el7, el8);
          var el8 = dom.createElement("div");
          dom.setAttribute(el8,"class","mini ui action right icon input ");
          var el9 = dom.createTextNode("\n								\n					          ");
          dom.appendChild(el8, el9);
          var el9 = dom.createTextNode("\n							");
          dom.appendChild(el8, el9);
          var el9 = dom.createElement("i");
          dom.setAttribute(el9,"class","right add icon");
          var el10 = dom.createTextNode("\n								\n\n							");
          dom.appendChild(el9, el10);
          dom.appendChild(el8, el9);
          var el9 = dom.createTextNode(" ");
          dom.appendChild(el8, el9);
          dom.appendChild(el7, el8);
          var el8 = dom.createTextNode("\n\n						");
          dom.appendChild(el7, el8);
          dom.appendChild(el6, el7);
          var el7 = dom.createTextNode("\n					");
          dom.appendChild(el6, el7);
          dom.appendChild(el5, el6);
          var el6 = dom.createTextNode("\n					\n					\n					\n				");
          dom.appendChild(el5, el6);
          dom.appendChild(el4, el5);
          var el5 = dom.createTextNode("\n			");
          dom.appendChild(el4, el5);
          dom.appendChild(el3, el4);
          var el4 = dom.createTextNode("\n		");
          dom.appendChild(el3, el4);
          dom.appendChild(el2, el3);
          var el3 = dom.createTextNode("\n	");
          dom.appendChild(el2, el3);
          dom.appendChild(el1, el2);
          var el2 = dom.createTextNode("\n	");
          dom.appendChild(el1, el2);
          var el2 = dom.createElement("div");
          dom.setAttribute(el2,"class","item");
          var el3 = dom.createTextNode("\n		");
          dom.appendChild(el2, el3);
          var el3 = dom.createElement("div");
          dom.setAttribute(el3,"class","ui  fluid accordion");
          var el4 = dom.createTextNode("\n			");
          dom.appendChild(el3, el4);
          var el4 = dom.createElement("div");
          dom.setAttribute(el4,"class","ui active title");
          var el5 = dom.createTextNode("\n				");
          dom.appendChild(el4, el5);
          var el5 = dom.createElement("i");
          dom.setAttribute(el5,"class","dropdown icon");
          dom.appendChild(el4, el5);
          var el5 = dom.createTextNode("\n				Peers\n			");
          dom.appendChild(el4, el5);
          dom.appendChild(el3, el4);
          var el4 = dom.createTextNode("\n			");
          dom.appendChild(el3, el4);
          var el4 = dom.createElement("div");
          dom.setAttribute(el4,"class"," content");
          var el5 = dom.createTextNode("\n				");
          dom.appendChild(el4, el5);
          var el5 = dom.createElement("div");
          dom.setAttribute(el5,"class","ui small  inverted header");
          var el6 = dom.createTextNode("\n					\n					\n					");
          dom.appendChild(el5, el6);
          var el6 = dom.createElement("div");
          dom.setAttribute(el6,"class","mini ui action left icon input ");
          var el7 = dom.createTextNode("\n						");
          dom.appendChild(el6, el7);
          var el7 = dom.createElement("input");
          dom.setAttribute(el7,"type","text");
          dom.setAttribute(el7,"placeholder","Search...");
          dom.appendChild(el6, el7);
          var el7 = dom.createTextNode("\n					");
          dom.appendChild(el6, el7);
          dom.appendChild(el5, el6);
          var el6 = dom.createTextNode("\n				");
          dom.appendChild(el5, el6);
          dom.appendChild(el4, el5);
          var el5 = dom.createTextNode("\n				");
          dom.appendChild(el4, el5);
          var el5 = dom.createElement("div");
          dom.setAttribute(el5,"class","menu");
          var el6 = dom.createTextNode("\n");
          dom.appendChild(el5, el6);
          var el6 = dom.createTextNode("					\n					\n					\n				");
          dom.appendChild(el5, el6);
          dom.appendChild(el4, el5);
          var el5 = dom.createTextNode("\n			");
          dom.appendChild(el4, el5);
          dom.appendChild(el3, el4);
          var el4 = dom.createTextNode("\n		");
          dom.appendChild(el3, el4);
          dom.appendChild(el2, el3);
          var el3 = dom.createTextNode("\n	");
          dom.appendChild(el2, el3);
          dom.appendChild(el1, el2);
          var el2 = dom.createTextNode("\n	");
          dom.appendChild(el1, el2);
          var el2 = dom.createElement("a");
          dom.setAttribute(el2,"class","item");
          var el3 = dom.createTextNode("\n		");
          dom.appendChild(el2, el3);
          var el3 = dom.createElement("i");
          dom.setAttribute(el3,"class","smile icon");
          dom.appendChild(el2, el3);
          var el3 = dom.createTextNode("\n		Friends\n	");
          dom.appendChild(el2, el3);
          dom.appendChild(el1, el2);
          var el2 = dom.createTextNode("\n	");
          dom.appendChild(el1, el2);
          var el2 = dom.createElement("a");
          dom.setAttribute(el2,"class","item");
          var el3 = dom.createTextNode("\n		");
          dom.appendChild(el2, el3);
          var el3 = dom.createElement("i");
          dom.setAttribute(el3,"class","calendar icon");
          dom.appendChild(el2, el3);
          var el3 = dom.createTextNode("\n		History\n	");
          dom.appendChild(el2, el3);
          dom.appendChild(el1, el2);
          var el2 = dom.createTextNode("\n	");
          dom.appendChild(el1, el2);
          var el2 = dom.createElement("a");
          dom.setAttribute(el2,"class","item");
          var el3 = dom.createTextNode("\n		");
          dom.appendChild(el2, el3);
          var el3 = dom.createElement("i");
          dom.setAttribute(el3,"class","mail icon");
          dom.appendChild(el2, el3);
          var el3 = dom.createTextNode("\n		Messages\n	");
          dom.appendChild(el2, el3);
          dom.appendChild(el1, el2);
          var el2 = dom.createTextNode("\n	");
          dom.appendChild(el1, el2);
          var el2 = dom.createElement("a");
          dom.setAttribute(el2,"class","item");
          var el3 = dom.createTextNode("\n		");
          dom.appendChild(el2, el3);
          var el3 = dom.createElement("i");
          dom.setAttribute(el3,"class","chat icon");
          dom.appendChild(el2, el3);
          var el3 = dom.createTextNode("\n		Discussions\n	");
          dom.appendChild(el2, el3);
          dom.appendChild(el1, el2);
          var el2 = dom.createTextNode("\n	");
          dom.appendChild(el1, el2);
          var el2 = dom.createElement("a");
          dom.setAttribute(el2,"class","item");
          var el3 = dom.createTextNode("\n		");
          dom.appendChild(el2, el3);
          var el3 = dom.createElement("i");
          dom.setAttribute(el3,"class","trophy icon");
          dom.appendChild(el2, el3);
          var el3 = dom.createTextNode("\n		Achievements\n	");
          dom.appendChild(el2, el3);
          dom.appendChild(el1, el2);
          var el2 = dom.createTextNode("\n	");
          dom.appendChild(el1, el2);
          var el2 = dom.createElement("a");
          dom.setAttribute(el2,"class","item");
          var el3 = dom.createTextNode("\n		");
          dom.appendChild(el2, el3);
          var el3 = dom.createElement("i");
          dom.setAttribute(el3,"class","shop icon");
          dom.appendChild(el2, el3);
          var el3 = dom.createTextNode("\n		Store\n	");
          dom.appendChild(el2, el3);
          dom.appendChild(el1, el2);
          var el2 = dom.createTextNode("\n	");
          dom.appendChild(el1, el2);
          var el2 = dom.createElement("a");
          dom.setAttribute(el2,"class","item");
          var el3 = dom.createTextNode("\n		");
          dom.appendChild(el2, el3);
          var el3 = dom.createElement("i");
          dom.setAttribute(el3,"class","settings icon");
          dom.appendChild(el2, el3);
          var el3 = dom.createTextNode("\n		Settings\n	");
          dom.appendChild(el2, el3);
          dom.appendChild(el1, el2);
          var el2 = dom.createTextNode("\n");
          dom.appendChild(el1, el2);
          dom.appendChild(el0, el1);
          var el1 = dom.createTextNode("\n");
          dom.appendChild(el0, el1);
          return el0;
        },
        render: function render(context, env, contextualElement) {
          var dom = env.dom;
          var hooks = env.hooks, get = hooks.get, block = hooks.block, element = hooks.element, inline = hooks.inline;
          dom.detectNamespace(contextualElement);
          var fragment;
          if (env.useFragmentCache && dom.canClone) {
            if (this.cachedFragment === null) {
              fragment = this.build(dom);
              if (this.hasRendered) {
                this.cachedFragment = fragment;
              } else {
                this.hasRendered = true;
              }
            }
            if (this.cachedFragment) {
              fragment = dom.cloneNode(this.cachedFragment, true);
            }
          } else {
            fragment = this.build(dom);
          }
          var element12 = dom.childAt(fragment, [0]);
          var element13 = dom.childAt(element12, [3, 1, 3, 3]);
          var element14 = dom.childAt(element13, [2]);
          var element15 = dom.childAt(element14, [1, 1]);
          var element16 = dom.childAt(element15, [2]);
          var morph0 = dom.createMorphAt(element13,0,1);
          var morph1 = dom.createMorphAt(element15,0,1);
          var morph2 = dom.createMorphAt(dom.childAt(element12, [5, 1, 3, 3]),0,1);
          block(env, morph0, context, "each", [get(env, context, "model.networks")], {"keyword": "network"}, child0, null);
          element(env, element14, context, "action", ["selectNetwork", get(env, context, "network")], {});
          inline(env, morph1, context, "input", [], {"placeholder": "new network", "type": "text", "value": get(env, context, "newN")});
          element(env, element16, context, "action", ["newNetwork", get(env, context, "newN")], {});
          block(env, morph2, context, "each", [get(env, context, "serverPeers")], {"keyword": "peer"}, child1, null);
          return fragment;
        }
      };
    }());
    var child1 = (function() {
      return {
        isHTMLBars: true,
        blockParams: 0,
        cachedFragment: null,
        hasRendered: false,
        build: function build(dom) {
          var el0 = dom.createTextNode(" scratch ");
          return el0;
        },
        render: function render(context, env, contextualElement) {
          var dom = env.dom;
          dom.detectNamespace(contextualElement);
          var fragment;
          if (env.useFragmentCache && dom.canClone) {
            if (this.cachedFragment === null) {
              fragment = this.build(dom);
              if (this.hasRendered) {
                this.cachedFragment = fragment;
              } else {
                this.hasRendered = true;
              }
            }
            if (this.cachedFragment) {
              fragment = dom.cloneNode(this.cachedFragment, true);
            }
          } else {
            fragment = this.build(dom);
          }
          return fragment;
        }
      };
    }());
    var child2 = (function() {
      return {
        isHTMLBars: true,
        blockParams: 0,
        cachedFragment: null,
        hasRendered: false,
        build: function build(dom) {
          var el0 = dom.createDocumentFragment();
          var el1 = dom.createTextNode("	");
          dom.appendChild(el0, el1);
          var el1 = dom.createElement("div");
          var el2 = dom.createTextNode("\n		");
          dom.appendChild(el1, el2);
          var el2 = dom.createElement("div");
          dom.setAttribute(el2,"class","list");
          var el3 = dom.createTextNode("\n			");
          dom.appendChild(el2, el3);
          var el3 = dom.createElement("div");
          dom.setAttribute(el3,"class","item serverPeers");
          var el4 = dom.createTextNode("\n				");
          dom.appendChild(el3, el4);
          var el4 = dom.createElement("i");
          dom.setAttribute(el4,"class","right triangle icon");
          dom.appendChild(el3, el4);
          var el4 = dom.createTextNode("\n				");
          dom.appendChild(el3, el4);
          var el4 = dom.createElement("div");
          dom.setAttribute(el4,"class","content");
          var el5 = dom.createTextNode("\n					");
          dom.appendChild(el4, el5);
          var el5 = dom.createElement("a");
          dom.setAttribute(el5,"class","header");
          dom.appendChild(el4, el5);
          var el5 = dom.createTextNode("\n					");
          dom.appendChild(el4, el5);
          var el5 = dom.createElement("div");
          dom.setAttribute(el5,"class","description");
          dom.appendChild(el4, el5);
          var el5 = dom.createTextNode("\n				");
          dom.appendChild(el4, el5);
          dom.appendChild(el3, el4);
          var el4 = dom.createTextNode("\n			");
          dom.appendChild(el3, el4);
          dom.appendChild(el2, el3);
          var el3 = dom.createTextNode("\n		");
          dom.appendChild(el2, el3);
          dom.appendChild(el1, el2);
          var el2 = dom.createTextNode("\n		\n	");
          dom.appendChild(el1, el2);
          dom.appendChild(el0, el1);
          var el1 = dom.createTextNode("\n");
          dom.appendChild(el0, el1);
          return el0;
        },
        render: function render(context, env, contextualElement) {
          var dom = env.dom;
          var hooks = env.hooks, get = hooks.get, element = hooks.element, content = hooks.content;
          dom.detectNamespace(contextualElement);
          var fragment;
          if (env.useFragmentCache && dom.canClone) {
            if (this.cachedFragment === null) {
              fragment = this.build(dom);
              if (this.hasRendered) {
                this.cachedFragment = fragment;
              } else {
                this.hasRendered = true;
              }
            }
            if (this.cachedFragment) {
              fragment = dom.cloneNode(this.cachedFragment, true);
            }
          } else {
            fragment = this.build(dom);
          }
          var element5 = dom.childAt(fragment, [1]);
          var element6 = dom.childAt(element5, [1, 1, 3]);
          var morph0 = dom.createMorphAt(dom.childAt(element6, [1]),-1,-1);
          var morph1 = dom.createMorphAt(dom.childAt(element6, [3]),-1,-1);
          element(env, element5, context, "action", ["edit", get(env, context, "person")], {});
          content(env, morph0, context, "peer.name");
          content(env, morph1, context, "peer.desc");
          return fragment;
        }
      };
    }());
    var child3 = (function() {
      var child0 = (function() {
        return {
          isHTMLBars: true,
          blockParams: 0,
          cachedFragment: null,
          hasRendered: false,
          build: function build(dom) {
            var el0 = dom.createDocumentFragment();
            return el0;
          },
          render: function render(context, env, contextualElement) {
            var dom = env.dom;
            dom.detectNamespace(contextualElement);
            var fragment;
            if (env.useFragmentCache && dom.canClone) {
              if (this.cachedFragment === null) {
                fragment = this.build(dom);
                if (this.hasRendered) {
                  this.cachedFragment = fragment;
                } else {
                  this.hasRendered = true;
                }
              }
              if (this.cachedFragment) {
                fragment = dom.cloneNode(this.cachedFragment, true);
              }
            } else {
              fragment = this.build(dom);
            }
            return fragment;
          }
        };
      }());
      var child1 = (function() {
        var child0 = (function() {
          return {
            isHTMLBars: true,
            blockParams: 0,
            cachedFragment: null,
            hasRendered: false,
            build: function build(dom) {
              var el0 = dom.createDocumentFragment();
              var el1 = dom.createTextNode("				");
              dom.appendChild(el0, el1);
              var el1 = dom.createElement("div");
              var el2 = dom.createTextNode("\n					");
              dom.appendChild(el1, el2);
              var el2 = dom.createElement("div");
              dom.setAttribute(el2,"class","list");
              var el3 = dom.createTextNode("\n						");
              dom.appendChild(el2, el3);
              var el3 = dom.createElement("div");
              dom.setAttribute(el3,"class","item");
              var el4 = dom.createTextNode("\n							");
              dom.appendChild(el3, el4);
              var el4 = dom.createElement("i");
              dom.setAttribute(el4,"class","right triangle icon");
              dom.appendChild(el3, el4);
              var el4 = dom.createTextNode("\n							");
              dom.appendChild(el3, el4);
              var el4 = dom.createElement("div");
              dom.setAttribute(el4,"class","content");
              var el5 = dom.createTextNode("\n								");
              dom.appendChild(el4, el5);
              var el5 = dom.createElement("a");
              dom.setAttribute(el5,"class","header");
              dom.appendChild(el4, el5);
              var el5 = dom.createTextNode("\n								");
              dom.appendChild(el4, el5);
              var el5 = dom.createElement("div");
              dom.setAttribute(el5,"class","description");
              dom.appendChild(el4, el5);
              var el5 = dom.createTextNode("\n							");
              dom.appendChild(el4, el5);
              dom.appendChild(el3, el4);
              var el4 = dom.createTextNode("\n						");
              dom.appendChild(el3, el4);
              dom.appendChild(el2, el3);
              var el3 = dom.createTextNode("\n					");
              dom.appendChild(el2, el3);
              dom.appendChild(el1, el2);
              var el2 = dom.createTextNode("\n					\n				");
              dom.appendChild(el1, el2);
              dom.appendChild(el0, el1);
              var el1 = dom.createTextNode("\n");
              dom.appendChild(el0, el1);
              return el0;
            },
            render: function render(context, env, contextualElement) {
              var dom = env.dom;
              var hooks = env.hooks, get = hooks.get, element = hooks.element, content = hooks.content;
              dom.detectNamespace(contextualElement);
              var fragment;
              if (env.useFragmentCache && dom.canClone) {
                if (this.cachedFragment === null) {
                  fragment = this.build(dom);
                  if (this.hasRendered) {
                    this.cachedFragment = fragment;
                  } else {
                    this.hasRendered = true;
                  }
                }
                if (this.cachedFragment) {
                  fragment = dom.cloneNode(this.cachedFragment, true);
                }
              } else {
                fragment = this.build(dom);
              }
              var element0 = dom.childAt(fragment, [1]);
              var element1 = dom.childAt(element0, [1, 1, 3]);
              var morph0 = dom.createMorphAt(dom.childAt(element1, [1]),-1,-1);
              var morph1 = dom.createMorphAt(dom.childAt(element1, [3]),-1,-1);
              element(env, element0, context, "action", ["edit", get(env, context, "person")], {});
              content(env, morph0, context, "peer.name");
              content(env, morph1, context, "peer.desc");
              return fragment;
            }
          };
        }());
        return {
          isHTMLBars: true,
          blockParams: 0,
          cachedFragment: null,
          hasRendered: false,
          build: function build(dom) {
            var el0 = dom.createDocumentFragment();
            var el1 = dom.createTextNode("		");
            dom.appendChild(el0, el1);
            var el1 = dom.createElement("div");
            dom.setAttribute(el1,"class","item");
            var el2 = dom.createTextNode("\n			");
            dom.appendChild(el1, el2);
            var el2 = dom.createElement("div");
            dom.setAttribute(el2,"class","content");
            var el3 = dom.createTextNode("\n				");
            dom.appendChild(el2, el3);
            var el3 = dom.createElement("div");
            dom.setAttribute(el3,"class","header");
            dom.appendChild(el2, el3);
            var el3 = dom.createTextNode("\n				\n				\n");
            dom.appendChild(el2, el3);
            var el3 = dom.createTextNode("			");
            dom.appendChild(el2, el3);
            dom.appendChild(el1, el2);
            var el2 = dom.createTextNode("\n		");
            dom.appendChild(el1, el2);
            dom.appendChild(el0, el1);
            var el1 = dom.createTextNode("\n");
            dom.appendChild(el0, el1);
            return el0;
          },
          render: function render(context, env, contextualElement) {
            var dom = env.dom;
            var hooks = env.hooks, get = hooks.get, element = hooks.element, content = hooks.content, block = hooks.block;
            dom.detectNamespace(contextualElement);
            var fragment;
            if (env.useFragmentCache && dom.canClone) {
              if (this.cachedFragment === null) {
                fragment = this.build(dom);
                if (this.hasRendered) {
                  this.cachedFragment = fragment;
                } else {
                  this.hasRendered = true;
                }
              }
              if (this.cachedFragment) {
                fragment = dom.cloneNode(this.cachedFragment, true);
              }
            } else {
              fragment = this.build(dom);
            }
            var element2 = dom.childAt(fragment, [1]);
            var element3 = dom.childAt(element2, [1]);
            var morph0 = dom.createMorphAt(dom.childAt(element3, [1]),-1,-1);
            var morph1 = dom.createMorphAt(element3,2,3);
            element(env, element2, context, "action", ["selectNetwork", get(env, context, "network")], {});
            content(env, morph0, context, "network.name");
            block(env, morph1, context, "each", [get(env, context, "network.peers")], {"keyword": "peer"}, child0, null);
            return fragment;
          }
        };
      }());
      return {
        isHTMLBars: true,
        blockParams: 0,
        cachedFragment: null,
        hasRendered: false,
        build: function build(dom) {
          var el0 = dom.createDocumentFragment();
          var el1 = dom.createElement("div");
          dom.setAttribute(el1,"class","six wide column ");
          var el2 = dom.createTextNode("\n");
          dom.appendChild(el1, el2);
          var el2 = dom.createTextNode("	");
          dom.appendChild(el1, el2);
          var el2 = dom.createElement("div");
          dom.setAttribute(el2,"class","ui list");
          var el3 = dom.createTextNode("\n");
          dom.appendChild(el2, el3);
          var el3 = dom.createTextNode("	");
          dom.appendChild(el2, el3);
          dom.appendChild(el1, el2);
          var el2 = dom.createTextNode("\n");
          dom.appendChild(el1, el2);
          dom.appendChild(el0, el1);
          var el1 = dom.createTextNode("\n");
          dom.appendChild(el0, el1);
          return el0;
        },
        render: function render(context, env, contextualElement) {
          var dom = env.dom;
          var hooks = env.hooks, block = hooks.block, get = hooks.get;
          dom.detectNamespace(contextualElement);
          var fragment;
          if (env.useFragmentCache && dom.canClone) {
            if (this.cachedFragment === null) {
              fragment = this.build(dom);
              if (this.hasRendered) {
                this.cachedFragment = fragment;
              } else {
                this.hasRendered = true;
              }
            }
            if (this.cachedFragment) {
              fragment = dom.cloneNode(this.cachedFragment, true);
            }
          } else {
            fragment = this.build(dom);
          }
          var element4 = dom.childAt(fragment, [0]);
          var morph0 = dom.createMorphAt(element4,0,1);
          var morph1 = dom.createMorphAt(dom.childAt(element4, [2]),0,1);
          block(env, morph0, context, "view", ["connector"], {}, child0, null);
          block(env, morph1, context, "each", [get(env, context, "model.networks")], {"keyword": "network"}, child1, null);
          return fragment;
        }
      };
    }());
    var child4 = (function() {
      return {
        isHTMLBars: true,
        blockParams: 0,
        cachedFragment: null,
        hasRendered: false,
        build: function build(dom) {
          var el0 = dom.createDocumentFragment();
          var el1 = dom.createTextNode("");
          dom.appendChild(el0, el1);
          var el1 = dom.createTextNode("\n");
          dom.appendChild(el0, el1);
          var el1 = dom.createElement("div");
          dom.setAttribute(el1,"id","networkView");
          dom.setAttribute(el1,"style","\n	width: 100%;\n	height: 100%;\nposition: fixed;");
          dom.appendChild(el0, el1);
          var el1 = dom.createTextNode("\n");
          dom.appendChild(el0, el1);
          return el0;
        },
        render: function render(context, env, contextualElement) {
          var dom = env.dom;
          var hooks = env.hooks, get = hooks.get, inline = hooks.inline;
          dom.detectNamespace(contextualElement);
          var fragment;
          if (env.useFragmentCache && dom.canClone) {
            if (this.cachedFragment === null) {
              fragment = this.build(dom);
              if (this.hasRendered) {
                this.cachedFragment = fragment;
              } else {
                this.hasRendered = true;
              }
            }
            if (this.cachedFragment) {
              fragment = dom.cloneNode(this.cachedFragment, true);
            }
          } else {
            fragment = this.build(dom);
          }
          if (this.cachedFragment) { dom.repairClonedNode(fragment,[0]); }
          var morph0 = dom.createMorphAt(fragment,0,1,contextualElement);
          inline(env, morph0, context, "buyan-graph", [], {"network": get(env, context, "networkSelected"), "peerl": get(env, context, "model.peers"), "ids": get(env, context, "model.ids"), "getpeersc": "getpeersc", "newid": "newid", "class": "six wide column"});
          return fragment;
        }
      };
    }());
    return {
      isHTMLBars: true,
      blockParams: 0,
      cachedFragment: null,
      hasRendered: false,
      build: function build(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createTextNode("\n	\n\n");
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("div");
        dom.setAttribute(el1,"class","ui grid padded ");
        var el2 = dom.createTextNode("\n	");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("div");
        dom.setAttribute(el2,"class","row ");
        var el3 = dom.createTextNode("\n	");
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n	");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n	");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("div");
        dom.setAttribute(el2,"class","row ui grid padded");
        var el3 = dom.createTextNode("\n				\n\n");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("div");
        dom.setAttribute(el3,"class","one wide column ");
        var el4 = dom.createTextNode("\n	");
        dom.appendChild(el3, el4);
        var el4 = dom.createElement("div");
        var el5 = dom.createTextNode(" ");
        dom.appendChild(el4, el5);
        var el5 = dom.createTextNode(" menu");
        dom.appendChild(el4, el5);
        dom.appendChild(el3, el4);
        var el4 = dom.createTextNode("\n	\n\n");
        dom.appendChild(el3, el4);
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n");
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        return el0;
      },
      render: function render(context, env, contextualElement) {
        var dom = env.dom;
        var hooks = env.hooks, block = hooks.block, element = hooks.element, inline = hooks.inline, get = hooks.get;
        dom.detectNamespace(contextualElement);
        var fragment;
        if (env.useFragmentCache && dom.canClone) {
          if (this.cachedFragment === null) {
            fragment = this.build(dom);
            if (this.hasRendered) {
              this.cachedFragment = fragment;
            } else {
              this.hasRendered = true;
            }
          }
          if (this.cachedFragment) {
            fragment = dom.cloneNode(this.cachedFragment, true);
          }
        } else {
          fragment = this.build(dom);
        }
        var element17 = dom.childAt(fragment, [2]);
        var element18 = dom.childAt(element17, [3]);
        if (this.cachedFragment) { dom.repairClonedNode(element18,[3]); }
        var element19 = dom.childAt(element18, [1]);
        var morph0 = dom.createMorphAt(fragment,0,1,contextualElement);
        var morph1 = dom.createMorphAt(dom.childAt(element17, [1]),0,1);
        var morph2 = dom.createMorphAt(dom.childAt(element19, [1]),0,1);
        var morph3 = dom.createMorphAt(element19,2,-1);
        var morph4 = dom.createMorphAt(element18,2,3);
        var morph5 = dom.createMorphAt(element18,3,-1);
        block(env, morph0, context, "view", ["sidemenu"], {}, child0, null);
        block(env, morph1, context, "link-to", ["scratch"], {}, child1, null);
        element(env, element19, context, "action", ["menuToggle"], {"on": "click"});
        inline(env, morph2, context, "material-design-icon", [], {"name": "menu"});
        block(env, morph3, context, "each", [get(env, context, "serverPeers")], {"keyword": "peer"}, child2, null);
        block(env, morph4, context, "if", [get(env, context, "networkOn")], {}, child3, null);
        block(env, morph5, context, "if", [get(env, context, "networkSelected")], {}, child4, null);
        return fragment;
      }
    };
  }()));

});
define('bapp/tests/adapters/id.jshint', function () {

  'use strict';

  module('JSHint - adapters');
  test('adapters/id.js should pass jshint', function() { 
    ok(true, 'adapters/id.js should pass jshint.'); 
  });

});
define('bapp/tests/adapters/network.jshint', function () {

  'use strict';

  module('JSHint - adapters');
  test('adapters/network.js should pass jshint', function() { 
    ok(true, 'adapters/network.js should pass jshint.'); 
  });

});
define('bapp/tests/adapters/peer.jshint', function () {

  'use strict';

  module('JSHint - adapters');
  test('adapters/peer.js should pass jshint', function() { 
    ok(false, 'adapters/peer.js should pass jshint.\nadapters/peer.js: line 32, col 39, \'relationship\' is defined but never used.\nadapters/peer.js: line 32, col 34, \'key\' is defined but never used.\nadapters/peer.js: line 35, col 27, \'type\' is defined but never used.\n\n3 errors'); 
  });

});
define('bapp/tests/adapters/scratch.jshint', function () {

  'use strict';

  module('JSHint - adapters');
  test('adapters/scratch.js should pass jshint', function() { 
    ok(false, 'adapters/scratch.js should pass jshint.\nadapters/scratch.js: line 6, col 39, \'relationship\' is defined but never used.\nadapters/scratch.js: line 6, col 34, \'key\' is defined but never used.\nadapters/scratch.js: line 9, col 27, \'type\' is defined but never used.\n\n3 errors'); 
  });

});
define('bapp/tests/app.jshint', function () {

  'use strict';

  module('JSHint - .');
  test('app.js should pass jshint', function() { 
    ok(true, 'app.js should pass jshint.'); 
  });

});
define('bapp/tests/assets/javascripts/adapter.jshint', function () {

  'use strict';

  module('JSHint - assets/javascripts');
  test('assets/javascripts/adapter.js should pass jshint', function() { 
    ok(false, 'assets/javascripts/adapter.js should pass jshint.\nassets/javascripts/adapter.js: line 11, col 21, \'mozRTCPeerConnection\' is not defined.\nassets/javascripts/adapter.js: line 13, col 1, \'RTCSessionDescription\' is not defined.\nassets/javascripts/adapter.js: line 13, col 25, \'mozRTCSessionDescription\' is not defined.\nassets/javascripts/adapter.js: line 15, col 1, \'RTCIceCandidate\' is not defined.\nassets/javascripts/adapter.js: line 15, col 19, \'mozRTCIceCandidate\' is not defined.\nassets/javascripts/adapter.js: line 31, col 1, \'MediaStream\' is not defined.\nassets/javascripts/adapter.js: line 34, col 1, \'MediaStream\' is not defined.\nassets/javascripts/adapter.js: line 41, col 21, \'webkitRTCPeerConnection\' is not defined.\nassets/javascripts/adapter.js: line 47, col 15, \'webkitURL\' is not defined.\nassets/javascripts/adapter.js: line 54, col 6, \'webkitMediaStream\' is not defined.\nassets/javascripts/adapter.js: line 55, col 1, \'webkitMediaStream\' is not defined.\nassets/javascripts/adapter.js: line 58, col 1, \'webkitMediaStream\' is not defined.\nassets/javascripts/adapter.js: line 63, col 6, \'webkitRTCPeerConnection\' is not defined.\nassets/javascripts/adapter.js: line 64, col 1, \'webkitRTCPeerConnection\' is not defined.\nassets/javascripts/adapter.js: line 67, col 1, \'webkitRTCPeerConnection\' is not defined.\n\n15 errors'); 
  });

});
define('bapp/tests/assets/javascripts/app.jshint', function () {

  'use strict';

  module('JSHint - assets/javascripts');
  test('assets/javascripts/app.js should pass jshint', function() { 
    ok(false, 'assets/javascripts/app.js should pass jshint.\nassets/javascripts/app.js: line 1, col 23, Expected \'===\' and instead saw \'==\'.\nassets/javascripts/app.js: line 1, col 66, Expected \'===\' and instead saw \'==\'.\nassets/javascripts/app.js: line 10, col 6, Missing semicolon.\nassets/javascripts/app.js: line 18, col 13, Expected an assignment or function call and instead saw an expression.\nassets/javascripts/app.js: line 19, col 13, Expected an assignment or function call and instead saw an expression.\nassets/javascripts/app.js: line 148, col 13, Expected an assignment or function call and instead saw an expression.\nassets/javascripts/app.js: line 149, col 13, Expected an assignment or function call and instead saw an expression.\nassets/javascripts/app.js: line 150, col 13, Expected an assignment or function call and instead saw an expression.\nassets/javascripts/app.js: line 167, col 33, A constructor name should start with an uppercase letter.\nassets/javascripts/app.js: line 167, col 33, Missing \'()\' invoking a constructor.\nassets/javascripts/app.js: line 177, col 25, Expected \'!==\' and instead saw \'!=\'.\nassets/javascripts/app.js: line 193, col 23, Expected \'===\' and instead saw \'==\'.\nassets/javascripts/app.js: line 194, col 34, Expected \'===\' and instead saw \'==\'.\nassets/javascripts/app.js: line 209, col 28, Expected \'===\' and instead saw \'==\'.\nassets/javascripts/app.js: line 282, col 11, Expected \'===\' and instead saw \'==\'.\nassets/javascripts/app.js: line 292, col 23, Expected \'===\' and instead saw \'==\'.\nassets/javascripts/app.js: line 295, col 23, Expected \'===\' and instead saw \'==\'.\nassets/javascripts/app.js: line 295, col 66, Expected \'===\' and instead saw \'==\'.\nassets/javascripts/app.js: line 295, col 101, Expected \'!==\' and instead saw \'!=\'.\nassets/javascripts/app.js: line 295, col 153, Expected \'!==\' and instead saw \'!=\'.\nassets/javascripts/app.js: line 298, col 23, Expected \'===\' and instead saw \'==\'.\nassets/javascripts/app.js: line 298, col 67, Expected \'!==\' and instead saw \'!=\'.\nassets/javascripts/app.js: line 298, col 119, Expected \'!==\' and instead saw \'!=\'.\nassets/javascripts/app.js: line 305, col 13, Expected \'===\' and instead saw \'==\'.\nassets/javascripts/app.js: line 305, col 48, Expected \'===\' and instead saw \'==\'.\nassets/javascripts/app.js: line 318, col 29, Expected \'===\' and instead saw \'==\'.\nassets/javascripts/app.js: line 322, col 17, Expected \'===\' and instead saw \'==\'.\nassets/javascripts/app.js: line 322, col 36, Expected \'===\' and instead saw \'==\'.\nassets/javascripts/app.js: line 322, col 69, Expected \'===\' and instead saw \'==\'.\nassets/javascripts/app.js: line 325, col 57, Expected \'===\' and instead saw \'==\'.\nassets/javascripts/app.js: line 328, col 23, Expected \'===\' and instead saw \'==\'.\nassets/javascripts/app.js: line 331, col 23, Expected \'===\' and instead saw \'==\'.\nassets/javascripts/app.js: line 334, col 23, Expected \'===\' and instead saw \'==\'.\nassets/javascripts/app.js: line 337, col 29, Expected \'===\' and instead saw \'==\'.\nassets/javascripts/app.js: line 341, col 17, Expected \'===\' and instead saw \'==\'.\nassets/javascripts/app.js: line 341, col 52, Expected \'===\' and instead saw \'==\'.\nassets/javascripts/app.js: line 364, col 14, Expected \'===\' and instead saw \'==\'.\nassets/javascripts/app.js: line 364, col 34, Expected \'===\' and instead saw \'==\'.\nassets/javascripts/app.js: line 368, col 24, Expected \'===\' and instead saw \'==\'.\nassets/javascripts/app.js: line 381, col 15, Missing \'()\' invoking a constructor.\nassets/javascripts/app.js: line 397, col 94, Expected \'!==\' and instead saw \'!=\'.\nassets/javascripts/app.js: line 418, col 14, Missing \'()\' invoking a constructor.\nassets/javascripts/app.js: line 427, col 42, Expected \'!==\' and instead saw \'!=\'.\nassets/javascripts/app.js: line 451, col 6, Expected an assignment or function call and instead saw an expression.\nassets/javascripts/app.js: line 452, col 6, Expected an assignment or function call and instead saw an expression.\nassets/javascripts/app.js: line 467, col 42, Expected \'===\' and instead saw \'==\'.\nassets/javascripts/app.js: line 483, col 13, Expected an assignment or function call and instead saw an expression.\nassets/javascripts/app.js: line 509, col 29, A constructor name should start with an uppercase letter.\nassets/javascripts/app.js: line 509, col 29, Missing \'()\' invoking a constructor.\nassets/javascripts/app.js: line 517, col 16, Avoid arguments.callee.\nassets/javascripts/app.js: line 517, col 16, Too many errors. (0% scanned).\n\n52 errors'); 
  });

});
define('bapp/tests/assets/javascripts/cjs.jshint', function () {

  'use strict';

  module('JSHint - assets/javascripts');
  test('assets/javascripts/cjs.js should pass jshint', function() { 
    ok(false, 'assets/javascripts/cjs.js should pass jshint.\nassets/javascripts/cjs.js: line 1, col 1, Missing semicolon.\nassets/javascripts/cjs.js: line 0, col 0, Use the function form of "use strict".\nassets/javascripts/cjs.js: line 16, col 31, Missing semicolon.\nassets/javascripts/cjs.js: line 44, col 26, Expected \'{\' and instead saw \'connections\'.\nassets/javascripts/cjs.js: line 48, col 42, Expected \'===\' and instead saw \'==\'.\nassets/javascripts/cjs.js: line 53, col 27, Expected \'!==\' and instead saw \'!=\'.\nassets/javascripts/cjs.js: line 53, col 32, Expected \'{\' and instead saw \'connections\'.\nassets/javascripts/cjs.js: line 62, col 17, Expected \'{\' and instead saw \'hideConnectionInfo\'.\nassets/javascripts/cjs.js: line 109, col 26, Expected \'{\' and instead saw \'connections\'.\nassets/javascripts/cjs.js: line 113, col 42, Expected \'===\' and instead saw \'==\'.\nassets/javascripts/cjs.js: line 118, col 27, Expected \'!==\' and instead saw \'!=\'.\nassets/javascripts/cjs.js: line 118, col 32, Expected \'{\' and instead saw \'connections\'.\nassets/javascripts/cjs.js: line 127, col 17, Expected \'{\' and instead saw \'hideConnectionInfo\'.\nassets/javascripts/cjs.js: line 171, col 32, Missing semicolon.\nassets/javascripts/cjs.js: line 352, col 16, Missing semicolon.\nassets/javascripts/cjs.js: line 359, col 3, Missing semicolon.\nassets/javascripts/cjs.js: line 361, col 1, Forgotten \'debugger\' statement?\nassets/javascripts/cjs.js: line 442, col 4, Missing semicolon.\nassets/javascripts/cjs.js: line 93, col 12, Weird construction. Is \'new\' necessary?\nassets/javascripts/cjs.js: line 444, col 2, Missing \'()\' invoking a constructor.\nassets/javascripts/cjs.js: line 454, col 20, Missing semicolon.\nassets/javascripts/cjs.js: line 65, col 5, \'jsPlumb\' is not defined.\nassets/javascripts/cjs.js: line 67, col 42, \'jsPlumb\' is not defined.\nassets/javascripts/cjs.js: line 81, col 9, \'jsPlumb\' is not defined.\nassets/javascripts/cjs.js: line 135, col 15, \'jsPlumb\' is not defined.\nassets/javascripts/cjs.js: line 146, col 5, \'$\' is not defined.\nassets/javascripts/cjs.js: line 148, col 17, \'$\' is not defined.\nassets/javascripts/cjs.js: line 149, col 5, \'$\' is not defined.\nassets/javascripts/cjs.js: line 214, col 22, \'$\' is not defined.\nassets/javascripts/cjs.js: line 239, col 22, \'$\' is not defined.\nassets/javascripts/cjs.js: line 307, col 37, \'jsPlumb\' is not defined.\nassets/javascripts/cjs.js: line 312, col 29, \'jsPlumb\' is not defined.\nassets/javascripts/cjs.js: line 315, col 17, \'jsPlumbUtil\' is not defined.\nassets/javascripts/cjs.js: line 318, col 29, \'jsPlumb\' is not defined.\nassets/javascripts/cjs.js: line 322, col 17, \'jsPlumbUtil\' is not defined.\nassets/javascripts/cjs.js: line 325, col 31, \'jsPlumb\' is not defined.\nassets/javascripts/cjs.js: line 329, col 17, \'jsPlumbUtil\' is not defined.\nassets/javascripts/cjs.js: line 335, col 17, \'jsPlumbUtil\' is not defined.\nassets/javascripts/cjs.js: line 362, col 1, \'jQuery\' is not defined.\nassets/javascripts/cjs.js: line 366, col 1, \'$\' is not defined.\nassets/javascripts/cjs.js: line 367, col 5, \'$\' is not defined.\nassets/javascripts/cjs.js: line 368, col 3, \'$\' is not defined.\nassets/javascripts/cjs.js: line 372, col 1, \'$\' is not defined.\nassets/javascripts/cjs.js: line 374, col 3, \'$\' is not defined.\nassets/javascripts/cjs.js: line 377, col 1, \'$\' is not defined.\nassets/javascripts/cjs.js: line 378, col 3, \'$\' is not defined.\nassets/javascripts/cjs.js: line 381, col 1, \'$\' is not defined.\nassets/javascripts/cjs.js: line 402, col 37, \'jsPlumb\' is not defined.\nassets/javascripts/cjs.js: line 414, col 29, \'jsPlumb\' is not defined.\nassets/javascripts/cjs.js: line 417, col 17, \'jsPlumbUtil\' is not defined.\nassets/javascripts/cjs.js: line 417, col 17, Too many errors. (88% scanned).\n\n52 errors'); 
  });

});
define('bapp/tests/assets/javascripts/stun.jshint', function () {

  'use strict';

  module('JSHint - assets/javascripts');
  test('assets/javascripts/stun.js should pass jshint', function() { 
    ok(false, 'assets/javascripts/stun.js should pass jshint.\nassets/javascripts/stun.js: line 1, col 10, \'Stun\' is defined but never used.\n\n1 error'); 
  });

});
define('bapp/tests/assets/javascripts/wrkr.jshint', function () {

  'use strict';

  module('JSHint - assets/javascripts');
  test('assets/javascripts/wrkr.js should pass jshint', function() { 
    ok(false, 'assets/javascripts/wrkr.js should pass jshint.\nassets/javascripts/wrkr.js: line 26, col 22, Expected \'!==\' and instead saw \'!=\'.\nassets/javascripts/wrkr.js: line 71, col 11, Missing semicolon.\nassets/javascripts/wrkr.js: line 113, col 23, Expected \'===\' and instead saw \'==\'.\nassets/javascripts/wrkr.js: line 135, col 2, Missing semicolon.\nassets/javascripts/wrkr.js: line 93, col 1, \'onmessage\' is not defined.\nassets/javascripts/wrkr.js: line 105, col 13, \'crypto\' is not defined.\nassets/javascripts/wrkr.js: line 120, col 18, \'postMessage\' is not defined.\nassets/javascripts/wrkr.js: line 77, col 10, \'decode\' is defined but never used.\nassets/javascripts/wrkr.js: line 22, col 10, \'compare\' is defined but never used.\nassets/javascripts/wrkr.js: line 44, col 5, \'hash\' is defined but never used.\nassets/javascripts/wrkr.js: line 48, col 5, \'not_found\' is defined but never used.\n\n11 errors'); 
  });

});
define('bapp/tests/components/buyan-graph.jshint', function () {

  'use strict';

  module('JSHint - components');
  test('components/buyan-graph.js should pass jshint', function() { 
    ok(false, 'components/buyan-graph.js should pass jshint.\ncomponents/buyan-graph.js: line 10, col 17, A constructor name should start with an uppercase letter.\ncomponents/buyan-graph.js: line 29, col 8, Missing semicolon.\ncomponents/buyan-graph.js: line 60, col 9, Forgotten \'debugger\' statement?\ncomponents/buyan-graph.js: line 65, col 6, Missing semicolon.\ncomponents/buyan-graph.js: line 67, col 13, \'x\' is already defined.\ncomponents/buyan-graph.js: line 68, col 13, \'y\' is already defined.\ncomponents/buyan-graph.js: line 78, col 6, Missing semicolon.\ncomponents/buyan-graph.js: line 98, col 9, Forgotten \'debugger\' statement?\ncomponents/buyan-graph.js: line 131, col 5, Forgotten \'debugger\' statement?\ncomponents/buyan-graph.js: line 9, col 5, \'sigma\' is not defined.\ncomponents/buyan-graph.js: line 9, col 27, \'sigma\' is not defined.\ncomponents/buyan-graph.js: line 10, col 17, \'sigma\' is not defined.\ncomponents/buyan-graph.js: line 15, col 5, \'sigma\' is not defined.\ncomponents/buyan-graph.js: line 43, col 9, \'$\' is not defined.\ncomponents/buyan-graph.js: line 79, col 5, \'$\' is not defined.\ncomponents/buyan-graph.js: line 82, col 5, \'$\' is not defined.\ncomponents/buyan-graph.js: line 95, col 5, \'$\' is not defined.\ncomponents/buyan-graph.js: line 105, col 5, \'$\' is not defined.\ncomponents/buyan-graph.js: line 126, col 9, \'$\' is not defined.\ncomponents/buyan-graph.js: line 31, col 14, \'f\' is defined but never used.\ncomponents/buyan-graph.js: line 55, col 24, \'myId\' is defined but never used.\ncomponents/buyan-graph.js: line 55, col 21, \'ev\' is defined but never used.\ncomponents/buyan-graph.js: line 97, col 41, \'$selectedItem\' is defined but never used.\ncomponents/buyan-graph.js: line 105, col 44, \'id\' is defined but never used.\ncomponents/buyan-graph.js: line 105, col 41, \'ev\' is defined but never used.\n\n25 errors'); 
  });

});
define('bapp/tests/controllers/scratch.jshint', function () {

  'use strict';

  module('JSHint - controllers');
  test('controllers/scratch.js should pass jshint', function() { 
    ok(false, 'controllers/scratch.js should pass jshint.\ncontrollers/scratch.js: line 14, col 5, Forgotten \'debugger\' statement?\ncontrollers/scratch.js: line 14, col 13, Missing semicolon.\ncontrollers/scratch.js: line 33, col 16, Unnecessary semicolon.\ncontrollers/scratch.js: line 51, col 18, Expected \'===\' and instead saw \'==\'.\ncontrollers/scratch.js: line 59, col 7, Forgotten \'debugger\' statement?\ncontrollers/scratch.js: line 67, col 7, Forgotten \'debugger\' statement?\ncontrollers/scratch.js: line 76, col 7, Forgotten \'debugger\' statement?\ncontrollers/scratch.js: line 26, col 7, \'$\' is not defined.\ncontrollers/scratch.js: line 61, col 18, \'store\' is not defined.\ncontrollers/scratch.js: line 31, col 14, \'m\' is defined but never used.\ncontrollers/scratch.js: line 66, col 25, \'a\' is defined but never used.\n\n11 errors'); 
  });

});
define('bapp/tests/helpers/resolver', ['exports', 'ember/resolver', 'bapp/config/environment'], function (exports, Resolver, config) {

  'use strict';

  var resolver = Resolver['default'].create();

  resolver.namespace = {
    modulePrefix: config['default'].modulePrefix,
    podModulePrefix: config['default'].podModulePrefix
  };

  exports['default'] = resolver;

});
define('bapp/tests/helpers/resolver.jshint', function () {

  'use strict';

  module('JSHint - helpers');
  test('helpers/resolver.js should pass jshint', function() { 
    ok(true, 'helpers/resolver.js should pass jshint.'); 
  });

});
define('bapp/tests/helpers/start-app', ['exports', 'ember', 'bapp/app', 'bapp/router', 'bapp/config/environment'], function (exports, Ember, Application, Router, config) {

  'use strict';



  exports['default'] = startApp;
  function startApp(attrs) {
    var application;

    var attributes = Ember['default'].merge({}, config['default'].APP);
    attributes = Ember['default'].merge(attributes, attrs); // use defaults, but you can override;

    Ember['default'].run(function () {
      application = Application['default'].create(attributes);
      application.setupForTesting();
      application.injectTestHelpers();
    });

    return application;
  }

});
define('bapp/tests/helpers/start-app.jshint', function () {

  'use strict';

  module('JSHint - helpers');
  test('helpers/start-app.js should pass jshint', function() { 
    ok(true, 'helpers/start-app.js should pass jshint.'); 
  });

});
define('bapp/tests/models/id.jshint', function () {

  'use strict';

  module('JSHint - models');
  test('models/id.js should pass jshint', function() { 
    ok(true, 'models/id.js should pass jshint.'); 
  });

});
define('bapp/tests/models/network.jshint', function () {

  'use strict';

  module('JSHint - models');
  test('models/network.js should pass jshint', function() { 
    ok(true, 'models/network.js should pass jshint.'); 
  });

});
define('bapp/tests/models/peer.jshint', function () {

  'use strict';

  module('JSHint - models');
  test('models/peer.js should pass jshint', function() { 
    ok(true, 'models/peer.js should pass jshint.'); 
  });

});
define('bapp/tests/models/scratch.jshint', function () {

  'use strict';

  module('JSHint - models');
  test('models/scratch.js should pass jshint', function() { 
    ok(true, 'models/scratch.js should pass jshint.'); 
  });

});
define('bapp/tests/router.jshint', function () {

  'use strict';

  module('JSHint - .');
  test('router.js should pass jshint', function() { 
    ok(true, 'router.js should pass jshint.'); 
  });

});
define('bapp/tests/routes/peers.jshint', function () {

  'use strict';

  module('JSHint - routes');
  test('routes/peers.js should pass jshint', function() { 
    ok(true, 'routes/peers.js should pass jshint.'); 
  });

});
define('bapp/tests/routes/scratch.jshint', function () {

  'use strict';

  module('JSHint - routes');
  test('routes/scratch.js should pass jshint', function() { 
    ok(false, 'routes/scratch.js should pass jshint.\nroutes/scratch.js: line 15, col 5, Comma warnings can be turned off with \'laxcomma\'.\nroutes/scratch.js: line 14, col 5, Bad line breaking before \',\'.\nroutes/scratch.js: line 18, col 15, Forgotten \'debugger\' statement?\nroutes/scratch.js: line 17, col 29, \'a\' is defined but never used.\n\n4 errors'); 
  });

});
define('bapp/tests/serializers/peer.jshint', function () {

  'use strict';

  module('JSHint - serializers');
  test('serializers/peer.js should pass jshint', function() { 
    ok(false, 'serializers/peer.js should pass jshint.\nserializers/peer.js: line 5, col 11, Forgotten \'debugger\' statement?\nserializers/peer.js: line 6, col 7, \'i\' is defined but never used.\nserializers/peer.js: line 7, col 12, \'isJson\' is defined but never used.\nserializers/peer.js: line 15, col 37, \'a\' is defined but never used.\n\n4 errors'); 
  });

});
define('bapp/tests/serializers/scratch.jshint', function () {

  'use strict';

  module('JSHint - serializers');
  test('serializers/scratch.js should pass jshint', function() { 
    ok(false, 'serializers/scratch.js should pass jshint.\nserializers/scratch.js: line 28, col 70, Missing semicolon.\nserializers/scratch.js: line 28, col 72, Missing semicolon.\nserializers/scratch.js: line 27, col 13, \'_\' is not defined.\nserializers/scratch.js: line 18, col 7, \'i\' is defined but never used.\nserializers/scratch.js: line 28, col 41, \'a\' is defined but never used.\nserializers/scratch.js: line 28, col 39, \'i\' is defined but never used.\nserializers/scratch.js: line 29, col 37, \'a\' is defined but never used.\n\n7 errors'); 
  });

});
define('bapp/tests/test-helper', ['bapp/tests/helpers/resolver', 'ember-qunit'], function (resolver, ember_qunit) {

	'use strict';

	ember_qunit.setResolver(resolver['default']);

});
define('bapp/tests/test-helper.jshint', function () {

  'use strict';

  module('JSHint - .');
  test('test-helper.js should pass jshint', function() { 
    ok(true, 'test-helper.js should pass jshint.'); 
  });

});
define('bapp/tests/unit/adapters/ids-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleFor("adapter:ids", "IdsAdapter", {});

  // Replace this with your real tests.
  ember_qunit.test("it exists", function (assert) {
    var adapter = this.subject();
    assert.ok(adapter);
  });

  // Specify the other units that are required for this test.
  // needs: ['serializer:foo']

});
define('bapp/tests/unit/adapters/ids-test.jshint', function () {

  'use strict';

  module('JSHint - unit/adapters');
  test('unit/adapters/ids-test.js should pass jshint', function() { 
    ok(true, 'unit/adapters/ids-test.js should pass jshint.'); 
  });

});
define('bapp/tests/unit/adapters/network-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleFor("adapter:network", "NetworkAdapter", {});

  // Replace this with your real tests.
  ember_qunit.test("it exists", function (assert) {
    var adapter = this.subject();
    assert.ok(adapter);
  });

  // Specify the other units that are required for this test.
  // needs: ['serializer:foo']

});
define('bapp/tests/unit/adapters/network-test.jshint', function () {

  'use strict';

  module('JSHint - unit/adapters');
  test('unit/adapters/network-test.js should pass jshint', function() { 
    ok(true, 'unit/adapters/network-test.js should pass jshint.'); 
  });

});
define('bapp/tests/unit/adapters/peer-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleFor("adapter:peer", "PeerAdapter", {});

  // Replace this with your real tests.
  ember_qunit.test("it exists", function (assert) {
    var adapter = this.subject();
    assert.ok(adapter);
  });

  // Specify the other units that are required for this test.
  // needs: ['serializer:foo']

});
define('bapp/tests/unit/adapters/peer-test.jshint', function () {

  'use strict';

  module('JSHint - unit/adapters');
  test('unit/adapters/peer-test.js should pass jshint', function() { 
    ok(true, 'unit/adapters/peer-test.js should pass jshint.'); 
  });

});
define('bapp/tests/unit/adapters/scratch-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleFor("adapter:scratch", "ScratchAdapter", {});

  // Replace this with your real tests.
  ember_qunit.test("it exists", function (assert) {
    var adapter = this.subject();
    assert.ok(adapter);
  });

  // Specify the other units that are required for this test.
  // needs: ['serializer:foo']

});
define('bapp/tests/unit/adapters/scratch-test.jshint', function () {

  'use strict';

  module('JSHint - unit/adapters');
  test('unit/adapters/scratch-test.js should pass jshint', function() { 
    ok(true, 'unit/adapters/scratch-test.js should pass jshint.'); 
  });

});
define('bapp/tests/unit/components/buyan-graph-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleForComponent("buyan-graph", {});

  ember_qunit.test("it renders", function (assert) {
    assert.expect(2);

    // creates the component instance
    var component = this.subject();
    assert.equal(component._state, "preRender");

    // renders the component to the page
    this.render();
    assert.equal(component._state, "inDOM");
  });

  // specify the other units that are required for this test
  // needs: ['component:foo', 'helper:bar']

});
define('bapp/tests/unit/components/buyan-graph-test.jshint', function () {

  'use strict';

  module('JSHint - unit/components');
  test('unit/components/buyan-graph-test.js should pass jshint', function() { 
    ok(true, 'unit/components/buyan-graph-test.js should pass jshint.'); 
  });

});
define('bapp/tests/unit/controllers/scratch-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleFor("controller:scratch", {});

  // Replace this with your real tests.
  ember_qunit.test("it exists", function (assert) {
    var controller = this.subject();
    assert.ok(controller);
  });

  // Specify the other units that are required for this test.
  // needs: ['controller:foo']

});
define('bapp/tests/unit/controllers/scratch-test.jshint', function () {

  'use strict';

  module('JSHint - unit/controllers');
  test('unit/controllers/scratch-test.js should pass jshint', function() { 
    ok(true, 'unit/controllers/scratch-test.js should pass jshint.'); 
  });

});
define('bapp/tests/unit/models/ids-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleForModel("ids", {
    // Specify the other units that are required for this test.
    needs: []
  });

  ember_qunit.test("it exists", function (assert) {
    var model = this.subject();
    // var store = this.store();
    assert.ok(!!model);
  });

});
define('bapp/tests/unit/models/ids-test.jshint', function () {

  'use strict';

  module('JSHint - unit/models');
  test('unit/models/ids-test.js should pass jshint', function() { 
    ok(true, 'unit/models/ids-test.js should pass jshint.'); 
  });

});
define('bapp/tests/unit/models/network-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleForModel("network", {
    // Specify the other units that are required for this test.
    needs: []
  });

  ember_qunit.test("it exists", function (assert) {
    var model = this.subject();
    // var store = this.store();
    assert.ok(!!model);
  });

});
define('bapp/tests/unit/models/network-test.jshint', function () {

  'use strict';

  module('JSHint - unit/models');
  test('unit/models/network-test.js should pass jshint', function() { 
    ok(true, 'unit/models/network-test.js should pass jshint.'); 
  });

});
define('bapp/tests/unit/models/peer-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleForModel("peer", {
    // Specify the other units that are required for this test.
    needs: []
  });

  ember_qunit.test("it exists", function (assert) {
    var model = this.subject();
    // var store = this.store();
    assert.ok(!!model);
  });

});
define('bapp/tests/unit/models/peer-test.jshint', function () {

  'use strict';

  module('JSHint - unit/models');
  test('unit/models/peer-test.js should pass jshint', function() { 
    ok(true, 'unit/models/peer-test.js should pass jshint.'); 
  });

});
define('bapp/tests/unit/models/scratch-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleForModel("scratch", {
    // Specify the other units that are required for this test.
    needs: []
  });

  ember_qunit.test("it exists", function (assert) {
    var model = this.subject();
    // var store = this.store();
    assert.ok(!!model);
  });

});
define('bapp/tests/unit/models/scratch-test.jshint', function () {

  'use strict';

  module('JSHint - unit/models');
  test('unit/models/scratch-test.js should pass jshint', function() { 
    ok(true, 'unit/models/scratch-test.js should pass jshint.'); 
  });

});
define('bapp/tests/unit/routes/peers-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleFor("route:peers", {});

  ember_qunit.test("it exists", function (assert) {
    var route = this.subject();
    assert.ok(route);
  });

  // Specify the other units that are required for this test.
  // needs: ['controller:foo']

});
define('bapp/tests/unit/routes/peers-test.jshint', function () {

  'use strict';

  module('JSHint - unit/routes');
  test('unit/routes/peers-test.js should pass jshint', function() { 
    ok(true, 'unit/routes/peers-test.js should pass jshint.'); 
  });

});
define('bapp/tests/unit/routes/scratch-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleFor("route:scratch", {});

  ember_qunit.test("it exists", function (assert) {
    var route = this.subject();
    assert.ok(route);
  });

  // Specify the other units that are required for this test.
  // needs: ['controller:foo']

});
define('bapp/tests/unit/routes/scratch-test.jshint', function () {

  'use strict';

  module('JSHint - unit/routes');
  test('unit/routes/scratch-test.js should pass jshint', function() { 
    ok(true, 'unit/routes/scratch-test.js should pass jshint.'); 
  });

});
define('bapp/tests/unit/serializers/peer-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleFor("serializer:peer", {});

  // Replace this with your real tests.
  ember_qunit.test("it exists", function (assert) {
    var serializer = this.subject();
    assert.ok(serializer);
  });

  // Specify the other units that are required for this test.
  // needs: ['serializer:foo']

});
define('bapp/tests/unit/serializers/peer-test.jshint', function () {

  'use strict';

  module('JSHint - unit/serializers');
  test('unit/serializers/peer-test.js should pass jshint', function() { 
    ok(true, 'unit/serializers/peer-test.js should pass jshint.'); 
  });

});
define('bapp/tests/unit/serializers/scratch-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleFor("serializer:scratch", {});

  // Replace this with your real tests.
  ember_qunit.test("it exists", function (assert) {
    var serializer = this.subject();
    assert.ok(serializer);
  });

  // Specify the other units that are required for this test.
  // needs: ['serializer:foo']

});
define('bapp/tests/unit/serializers/scratch-test.jshint', function () {

  'use strict';

  module('JSHint - unit/serializers');
  test('unit/serializers/scratch-test.js should pass jshint', function() { 
    ok(true, 'unit/serializers/scratch-test.js should pass jshint.'); 
  });

});
define('bapp/tests/unit/views/connector-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleFor("view:connector");

  // Replace this with your real tests.
  ember_qunit.test("it exists", function (assert) {
    var view = this.subject();
    assert.ok(view);
  });

});
define('bapp/tests/unit/views/connector-test.jshint', function () {

  'use strict';

  module('JSHint - unit/views');
  test('unit/views/connector-test.js should pass jshint', function() { 
    ok(true, 'unit/views/connector-test.js should pass jshint.'); 
  });

});
define('bapp/tests/unit/views/scratch-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleFor("view:scratch");

  // Replace this with your real tests.
  ember_qunit.test("it exists", function (assert) {
    var view = this.subject();
    assert.ok(view);
  });

});
define('bapp/tests/unit/views/scratch-test.jshint', function () {

  'use strict';

  module('JSHint - unit/views');
  test('unit/views/scratch-test.js should pass jshint', function() { 
    ok(true, 'unit/views/scratch-test.js should pass jshint.'); 
  });

});
define('bapp/tests/unit/views/sidemenu-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleFor("view:sidemenu");

  // Replace this with your real tests.
  ember_qunit.test("it exists", function (assert) {
    var view = this.subject();
    assert.ok(view);
  });

});
define('bapp/tests/unit/views/sidemenu-test.jshint', function () {

  'use strict';

  module('JSHint - unit/views');
  test('unit/views/sidemenu-test.js should pass jshint', function() { 
    ok(true, 'unit/views/sidemenu-test.js should pass jshint.'); 
  });

});
define('bapp/tests/views/connector.jshint', function () {

  'use strict';

  module('JSHint - views');
  test('views/connector.js should pass jshint', function() { 
    ok(false, 'views/connector.js should pass jshint.\nviews/connector.js: line 7, col 9, Expected \'{\' and instead saw \'text\'.\nviews/connector.js: line 21, col 7, Forgotten \'debugger\' statement?\nviews/connector.js: line 25, col 7, \'$\' is not defined.\nviews/connector.js: line 23, col 19, \'e\' is defined but never used.\n\n4 errors'); 
  });

});
define('bapp/tests/views/sidemenu.jshint', function () {

  'use strict';

  module('JSHint - views');
  test('views/sidemenu.js should pass jshint', function() { 
    ok(false, 'views/sidemenu.js should pass jshint.\nviews/sidemenu.js: line 8, col 12, \'$\' is not defined.\n\n1 error'); 
  });

});
define('bapp/views/connector', ['exports', 'ember'], function (exports, Ember) {

  'use strict';

  function makeid() {
    var text = "";
    var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    for (var i = 0; i < 10; i++) text += possible.charAt(Math.floor(Math.random() * possible.length));

    return text;
  }

  exports['default'] = Ember['default'].View.extend({
    randomId: makeid(),
    idSet: false,
    actions: {
      toggleNetwork: function toggleNetwork(net) {
        this.set(net, !this.get(net));
      }
    },
    getpeers: function getpeers() {
      debugger;
    },
    click: function click(e) {
      if (arguments[0].target.id === "enterbuyan") {
        $(document).trigger("call", { typ: "setID", msg: [this.randomId] });

        this.idSet = true;
      }
      if (arguments[0].target.id === "pullpeersfromserver") {}
    }
  });

  //$(document).trigger("call",{typ: "setID",msg: [ this.randomId]});

  //this.idSet=true;

});
define('bapp/views/sidemenu', ['exports', 'ember'], function (exports, Ember) {

   'use strict';

   exports['default'] = Ember['default'].View.extend({
      didInsertElement: function didInsertElement() {
         this._super();
         Ember['default'].run.scheduleOnce("afterRender", this, function () {
            // perform your jQuery logic here
            $(".accordion").accordion({
               selector: {
                  trigger: ".title"
               }
            });
         });
      }
   });

});
/* jshint ignore:start */

/* jshint ignore:end */

/* jshint ignore:start */

define('bapp/config/environment', ['ember'], function(Ember) {
  var prefix = 'bapp';
/* jshint ignore:start */

try {
  var metaName = prefix + '/config/environment';
  var rawConfig = Ember['default'].$('meta[name="' + metaName + '"]').attr('content');
  var config = JSON.parse(unescape(rawConfig));

  return { 'default': config };
}
catch(err) {
  throw new Error('Could not read config from meta tag with name "' + metaName + '".');
}

/* jshint ignore:end */

});

if (runningTests) {
  require("bapp/tests/test-helper");
} else {
  require("bapp/app")["default"].create({"name":"bapp","version":"0.0.0.75824f06"});
}

/* jshint ignore:end */
//# sourceMappingURL=bapp.map