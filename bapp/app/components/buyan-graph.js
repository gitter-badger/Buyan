import Ember from 'ember';
import layout from '../templates/components/buyan-graph';

export default Ember.Component.extend({
  layout: layout,
  setupTooltip: function () {
    sigma.renderers.def = sigma.renderers.canvas;
    // Instantiate sigma:
    var g={nodes:[],edges:[]};
    /*
    var i,
        s,
        N = 100,
        E = 500,
        g = {
          nodes: [],
          edges: []
        };

    // Generate a random graph:
    for (i = 0; i < N; i++){
            g.nodes.push({
        id: 'n' + i,
        label: 'Node ' + i,
        x: Math.random(),
        y: Math.random(),
        size: Math.random(),
        color: '#666'
      });
    }

    for (i = 0; i < E; i++){
      g.edges.push({
        id: 'e' + i,
        source: 'n' + (Math.random() * N | 0),
        target: 'n' + (Math.random() * N | 0),
        size: Math.random(),
        color: '#ccc'
      });
    }
    */
    var s = new sigma({
      graph:g ,
      container: 'graph-container'
    });
    window.s=s;
    // Initialize the dragNodes plugin:
    sigma.plugins.dragNodes(s, s.renderers[0]);
    function f(){

      window.s.graph.addNode({
        id: 'n4' + 18988+(++i),
        label: 'Node 1i' + 1+(++i),
        x: Math.random(),
        y: Math.random(),
        size: Math.random()*100,
        color: '#676'
      });
      window.s.refresh();
    }

    //$(document).on("npeer",f)
  }.on( 'didInsertElement' )
});
