function addHook(name,f){
  window.messages=window.messages||{};
  window.messages[name]=true;
  $(document).on(name,f);

}
function BuyanJS(){
  var self=this;
  self.peers=[];
  self.newPeer=function(peerr){
    self.peers.push(peerr);
  }


  self.onPeerRequest=function(src,message){

  }

  self.onMessage=function(message){
    debugger;
    console.log("got message ",message);
  }
  function addMef(ev,myId){
      self.me=myId;
    }
    $(document).on("setID",addMef)
  self.sendto=function(who,wat){
    $(document).trigger("sendto",{to: who,msg:wat});
  }

    addHook("peersNeeded",self.onPeerRequest);
    addHook("peermessage",self.onMessage);
    addHook("peer",self.newPeer);
}
$(document).on("buyanLoaded",function(){
window.buyanjs=new BuyanJS();
});
