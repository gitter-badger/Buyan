import Ember from 'ember';

export default Ember.Route.extend({

    model: function() {

     return {
     		model: this.store.findAll('scratch'),
			ids: this.store.findAll('id'),
			networks: this.store.findAll('network')
 			};


    }
    ,
    actions:{
		makenewid: function(a){
		      debugger;
		    }

    }
 //   setupController: function(controller, song) {
      // Set the IndexController's `title`
   //   controller.set('title', 'My App');
 //       controller.set('model', song);
 //   }
});
