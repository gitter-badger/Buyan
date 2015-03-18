import Ember from 'ember';

export default Ember.Route.extend({

    model: function() {
debugger;
      return this.store.findAll('scratch');
return [{
  handle: 1

}]

    },
    setupController: function(controller, song) {
      // Set the IndexController's `title`
      controller.set('title', 'My App');
        controller.set('model', song);
    }
});
