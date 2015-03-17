import Ember from 'ember';

export default Ember.Route.extend({

    model: function() {
      var nId=1;
      var nodeRadius=3;
      var eId=1;
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
    nodes: [
      {
        id: (++nId) + '',
        size: nodeRadius,
        x: 0,
        y: -80,
        dX: 0,
        dY: 0,
        type: 'goo'
      },
      {
        id: (++nId) + '',
        size: nodeRadius,
        x: 10,
        y: -100,
        dX: 0,
        dY: 0,
        type: 'goo'
      },
      {
        id: (++nId) + '',
        size: nodeRadius,
        x: 20,
        y: -80,
        dX: 0,
        dY: 0,
        type: 'goo'
      }
    ],
    edges: [
      {
        id: (++eId) + '',
        source: '1',
        target: '2',
        type: 'goo'
      },
      {
        id: (++eId) + '',
        source: '1',
        target: '3',
        type: 'goo'
      },
      {
        id: (++eId) + '',
        source: '2',
        target: '3',
        type: 'goo'
      }
    ]
  }

      }
    },
    setupController: function(controller, song) {
      // Set the IndexController's `title`
      controller.set('title', 'My App');
        controller.set('model', song);
    }
});
