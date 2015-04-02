import Ember from 'ember';

export default Ember.Controller.extend({

  init: function(){
    this.set("peers", this.store.findAll('peer'));
  },
  networkOn:false,
  playMedia: function(){
    debugger
}.on("newid"),
  actions:{
    networkToggle:function(){
      //debugger;
      this.set("networkOn",!this.get("networkOn"));
    },
    newid: function(name,desc){
      //debugger;
      this.store.createRecord('id',{name:name,desc:desc,dta:''});
    },
    getpeers: function(){
    function m(){
                return {model:this.store.findAll('scratch')};
              };
      //this.set("model", this.store.findAll('scratch'));
      var accountObj = this.store.findAll('peer');
      var self=this;
      accountObj.then( function (r) {
          self.set('model', r);
      });
      //this.transitionToRoute('scratch',this.store.findAll('scratch'));
       //this.get('target.router').refresh();
    },
    selectNetwork: function(network){
      var n =this.get('networkSelected');
      if(n && n==network){
        this.set('networkSelected',null);
      }else{
        this.set('networkSelected',network);
      
      }
    },
    getsetpeers: function(){
      debugger;

      var post = store.createRecord('scratch', {
        pper: 'Rails is Omakase'
      });
      post.save();
    },
        makenewid: function(a){
          debugger;
        }

    
  }
});
