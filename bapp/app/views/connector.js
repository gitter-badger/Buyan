import Ember from 'ember';
function makeid(){
    var text = "";
    var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    for( var i=0; i < 10; i++ )
        text += possible.charAt(Math.floor(Math.random() * possible.length));

    return text;
}

export default Ember.View.extend({
  randomId: makeid(),
  idSet:false,
  actions:{
    toggleNetwork: function(net){
      this.set(net,!this.get(net));
    }
  },
   getpeers:function(){
      debugger;
    },
  click: function(e){
    if(arguments[0].target.id==="enterbuyan"){
      $(document).trigger("call",{typ: "setID",msg: [ this.randomId]});


      this.idSet=true;
    }
    if(arguments[0].target.id==="pullpeersfromserver"){
      //$(document).trigger("call",{typ: "setID",msg: [ this.randomId]});


      //this.idSet=true;
    }
  }
});
