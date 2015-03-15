function Konnection(){
  var self=this;
  var cfg = {"iceServers":[{"url":"stun:23.21.150.121"}]},
      con = { 'optional': [{'DtlsSrtpKeyAgreement': true}] };
  self.dataChannelSetup=function(dc1){
    self.dc1=dc1;
    self.dc1.onopen = function (e) {
        console.log('data channel connect');
    }
    self.dc1.onmessage = function (e) {
      if (e.data.size) {
        self.onMessage(e.data);
      }
      else {
          if (e.data.charCodeAt(0) == 2) {
             // The first message we get from Firefox (but not Chrome)
             // is literal ASCII 2 and I don't understand why -- if we
             // leave it in, JSON.parse() will barf.
             return;
          }
          console.log(e);
          var data = JSON.parse(e.data);
          self.onMessage(e.data);
      }
    };
  }
  self.makeDataChannel=function(){
    self.dc1 = self.pc1.createDataChannel('test', {reliable:true});
    self.dataChannelSetup(self.dc1);
  }
  self.newConnection=function newConnection(){
    self.pc1 = new webkitRTCPeerConnection(cfg, con);
    self.pc1.onicecandidate = self.onCandidate;
    self.pc1.onconnection = self.onOpen;
    function onsignalingstatechange(state) {
      console.info('signaling state change:', state);
    }

    function oniceconnectionstatechange(state) {
        console.info('ice connection state change:', state);
    }

    function onicegatheringstatechange(state) {
        console.info('ice gathering state change:', state);
    }

    self.pc1.onsignalingstatechange = onsignalingstatechange;
    self.pc1.oniceconnectionstatechange = oniceconnectionstatechange;
    self.pc1.onicegatheringstatechange = onicegatheringstatechange;
    self.pc1.ondatachannel = function (e) {
      //var fileReceiver2 = new FileReceiver();
      self.dc1= e.channel || e; // Chrome sends event, FF sends raw channel
      self.dataChannelSetup(self.dc1);
    }
  }
  self.makeOffer=function(){
    if(self.dc1==undefined){
      self.makeDataChannel();
    }
    self.pc1.createOffer(function (desc) {
    self.pc1.setLocalDescription(desc, function () {});
      console.log("created local offer", desc);
    }, function () {console.warn("Couldn't create offer");});
  }
  self.onCandidate=function (e) {
      console.log("ICE candidate (pc1)", e);
      if (e.candidate == null) {
        debugger;
          console.log("local offer ",JSON.stringify(self.pc1.localDescription));
      }
  }
  self.onAnswerFromPeer=function(answer){
    var answerDesc=
     (typeof(offer) === "string")?
      new RTCSessionDescription(JSON.parse(offer)):new RTCSessionDescription(offer);
    console.log("Received remote answer: ", answerDesc);
    self.pc1.setRemoteDescription(answerDesc);
  }

  self.answerFromOffer=function(offer){
    var sdesc=
     (typeof(offer) === "string")?
      new RTCSessionDescription(JSON.parse(offer)):new RTCSessionDescription(offer);

    self.pc1.setRemoteDescription(sdesc);
    self.pc1.createAnswer(function (answerDesc) {
        console.log("Created local answer: ", answerDesc);
        self.pc1.setLocalDescription(answerDesc);
    }, function () { console.warn("No create answer"); });

  }
  self.sendAnswer=function(){
    var answerDesc = new RTCSessionDescription(JSON.parse(answer));
    handleAnswerFromPC2(answerDesc);
  }
  self.onOpen=function(){
    console.log("Datachannel connected");
  }
  self.onMessage=function(message){

  }
}
