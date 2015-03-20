function addHook(name,f){
  window.messages=window.messages||{};
  window.messages[name]=1;
  $(document).on(name,f);

}
function BuyanJS(){
  var self=this;
  self.peers=[];
  self.newPeer=function(peerr){
    self.peers.push(peerr);
  }
  addHook("peer",self.newPeer);
  function addMef(ev,myId){
      self.me=myId;
    }
    $(document).on("setID",addMef)
  self.sendto=function(who,wat){
    $(document).trigger("sendto",{to: who,msg:wat});
  }
}
window.buyanjs=new BuyanJS();
