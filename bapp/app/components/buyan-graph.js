import Ember from 'ember';
import layout from '../templates/components/buyan-graph';

export default Ember.Component.extend({
  layout: layout,
  setupTooltip: function () {
    sigma.renderers.def = sigma.renderers.canvas;
    // Instantiate sigma:
  debugger;
    var s = new sigma({
      graph: this.get('peers'),
      container: 'graph-container'
    });

    // Initialize the dragNodes plugin:
    sigma.plugins.dragNodes(s, s.renderers[0]);

  }.on( 'didInsertElement' )
});
