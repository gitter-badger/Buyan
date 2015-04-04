import Ember from 'ember';

export default Ember.Controller.extend({

    newN:"",
  init: function(){

    var peer=this.store.findAll('peer');
    this.set("peers", peer);

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
    menuToggle:function(){
      $('.left.sidebar').sidebar('setting', 'dimPage', false)
        .sidebar('toggle')
      ;
    },
    getpeersc: function(){
    function m(){
                return {model:this.store.findAll('scratch')};
              };
      //this.set("model", this.store.findAll('scratch'));
      var accountObj = this.store.findAll('peer');

      this.set('peers', accountObj);
      /*
      accountObj.then( function (r) {

          debugger;
          self.set('peers', r);
      });
      */

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
    },
    newNetwork: function(newNetworkName){

      this.store.createRecord('network',{ name: newNetworkName, myIdentity: null ,peers:[]  });
      
    },
    deleteNetwork: function(name){
      
      debugger;
      this.store.find('network', name).then(function (post) {
        post.deleteRecord();
        post.get('isDeleted'); // => true
        post.save(); // => DELETE to /posts/1
      });

    }

    
  }
});
