function addHook(name,f){
  window.messages=window.messages||{};
  window.messages[name]=true;
  $(document).on(name,f);

}
function L1(){
 var self=this;
 self.demultiplex=function(src,m){
    console.log("demultiplex ",src," ", m);
    var typ=m.type;
    var msg=m.msg;
    self.demultiplex[typ](src,msg);
 }

 self.multiplex=function(src,dst,type,msg){
    console.log("multiplex src: ",src," dst: ",dst," ", m);
    var m={};
    m.type=type;
    m.msg=msg;
    self.multiplex[type](src,dst,m);
 }

 self.multiplex["transitoffer"]=function(src,dst,msg){
       console.log("multiplex transitoffer ");
       //var transit={type:"transitoffer",previus: src,msg: msg};
       var prevhopsealed=seal(src);
       msg.prevhopsealed.push(prevhopsealed);

       window.buyanjs.send(dst,msg);
 }
 self.multiplex["answer"]=function(src,dst,msg){
      window.buyanjs.send(dst,msg);
 }

 self.multiplex["offer"]=function(src,dst,msg){
     var nonce = Date.now() ;
     var id = 1;
     var token = signtoken(id);
     var prevhopsealed=seal(src);
     var o = {nonce:nonce,id:id,type:"transitoffer",msg: msg,prevhopsealed:[prevhopsealed]};
     //msg.prevhopsealed.push(prevhopsealed);

     window.buyanjs.send(dst,o);
 }
 function seal(src){
   var prev=src;
   return prev;
   };
 self.demultiplex["transitoffer"]=function(src,msg){
   //if we have connection to this peer pass the offer
   if(window.buyan.peersd[msg.origin]){

     for (var peer in window.buyanjs.peersd) {
       //pass not to the guy we recieved it from and to the peer that wants conn
       if(peer !== src && peer!= msg.origin){
         //seal the hop
         self.multiplex(src,peer,"transitoffer",msg);
         break;
       }
     }
   }else{
     //peer with the peer
     window.offers[msg.nonce].handlePeer(msg.msg).then(function(answer){
       self.multiplex(src,src,"answer",{dst:msg.origin, answer:answer,prevhopsealed:msg.prevhopsealed});

     });
   }
 }

 self.demultiplex["answer"]=function(src,msg){
   //if we know destination forward straight awy
   if(window.buyan.peersd[msg.dst]){
     self.multiplex(src,msg.dst,"answer",{dst:msg.origin, answer:answer,prevhopsealed:msg.prevhopsealed});

  }else if(msg.dst==window.buyan.myid){
    //if we are destination connect
    window.offers[msg.nonce].handlePeer(msg.answer);
  }

 }
 self.demultiplex["offer"]=function(src,msg){

    for (var peer in window.buyanjs.peersd) {
      if(peer !== src){

        var prevhopsealed=seal(src);
        self.multiplex(src,peer,"transitoffer",{origin:src,msg:msg,prevhopsealed:[prevhopsealed]});
        break;
      }
    }

 }
}

function BuyanJS(){
  var self=this;
  self.peers=[];
  self.l1=new L1(); //tbd. strategy
  self.myid=1;

  self.newPeer=function(peerr){
    console.log("newPeer ",peerr);
    self.peers.push(peerr);
    self.peersd[peerr]=true;
  }

  self.onPeerRequest=function(src,message){

  }
  function signtoken(id){
    return 5;
  }

  self.getSomePeers=function(){

    console.log("getSomePeersVia ", message);
    var konnection=new K();
    konnection.makeOffer().then(function(a){
      var peer = "H9gBOZ3rl9";
      var x={origin:self.myID,msg:msg,prevhopsealed:[]};
      console.log("making offer ",x);

      self.l1.multiplex(myid,"H9gBOZ3rl9",a);

      window.offers=window.offers||{};
      window.offers[nonce]=a;
    });
  }
  self.onMessage=function(e,message){
    console.log("onMessage ",message);
    if(message.type=='peerdata' ){
      var src= message.peer.conn.peer;
      var msg=message.msg;

    }
    console.log("got message ",message);
  }
  function addMef(ev,myId){

      console.log("addMef ", myId);
      self.me=myId;
  }
  $(document).on("setID",addMef)

  self.sendto=function(who,wat){
    var pd={"type":"peerdata",msg:wat}
    $(document).trigger("sendto",{to: who,msg:pd});
  }

    addHook("peersNeeded",self.onPeerRequest);
    addHook("peermessage",self.onMessage);
    addHook("peer",self.newPeer);
}
$(document).on("buyanLoaded",function(){
window.buyanjs=new BuyanJS();
});
