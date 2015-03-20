import Ember from 'ember';

export default Ember.Route.extend({

    model: function() {

     return {peers:this.store.findAll('scratch')};


    },
    setupController: function(controller, song) {
      // Set the IndexController's `title`
      controller.set('title', 'My App');
        controller.set('model', song);
    }
});
