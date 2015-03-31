import Ember from 'ember';

export default Ember.Controller.extend({

  playMedia: function(){
    debugger
}.on("newid"),
  actions:{
    newid: function(name,desc){
      //debugger;
      this.store.createRecord('id',{name:name,desc:desc,dta:''});
    },
    getpeers: function(){
    function m(){
                return {model:this.store.findAll('scratch')};
              };
      //this.set("model", this.store.findAll('scratch'));
      var accountObj = this.store.findAll('scratch');
      var self=this;
      accountObj.then( function (r) {
          self.set('model', r);
      });
      //this.transitionToRoute('scratch',this.store.findAll('scratch'));
       //this.get('target.router').refresh();
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
