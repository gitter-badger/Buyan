import Ember from 'ember';

export default Ember.Route.extend({

    model: function() {
      this.store.push('scratch', {
        id: 1,
        handle: "Fewer Moving Parts",
        ip: "David Bazan",
        source: 10,
        peers:[1,1,2]
      });

      this.store.push('scratch', {
        id: 2,
        handle: "Fewer Moving Parts",
        ip: "David Bazan",
        source: 10,
        peers:{}
      });
      return  {
        id: 1,
        handle: "Fewer Moving Parts",
        ip: "David Bazan",
        source: 10,
        peers:{
           nodes: [],
           edges: []
         }

      }
    },
    setupController: function(controller, song) {
      // Set the IndexController's `title`
      controller.set('title', 'My App');
        controller.set('model', song);
    }
});
