import Ember from 'ember';
import layout from '../templates/components/buyan-graph';

export default Ember.Component.extend({
  layout: layout,
  setupTooltip: function () {

    sigma.renderers.def = sigma.renderers.canvas;
    // Instantiate sigma:
    var a =this.get('peerl');
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
    // Initialize the dragNodes plugin:$(document).trigger("sendto",{to: "MdWfAHbgjM",msg:{}});
    sigma.plugins.dragNodes(s, s.renderers[0]);
    function mknode(labl){

      var i = i || 0;
      var label = labl ;
      return {
        id: label,
        label: label,
        x: Math.random(),
        y: Math.random(),
        size: 100,
        color: '#676'
      }
    }
    function f(data){
       var d=data.content;
       for(var i =0;i<d.length;i++){

         window.s.graph.addNode(mknode(d[i].get("handle")));
       }

      window.s.refresh();
    }
    a.then(f);
    function connectToAllInitial(){
      for (var i = 0; i <  window.s.graph.nodes().length-1; i++){
        $(document).trigger("call",{
          typ: "connectTo",
          "msg": [window.s.graph.nodes()[i].id]});
        window.s.graph.addEdge({
          id: 'e' + i,
          source: window.s.graph.nodes()[window.s.graph.nodes().length-1].id,
          target: window.s.graph.nodes()[i].id,
          size: 1,
          color: '#ccc'
        });
      }
    }
    function addMef(ev,myId){
      window.s.graph.addNode(mknode(myId));
      connectToAllInitial();
      window.s.refresh();
    }
    $(document).on("setID",addMef)
  }.on( 'didInsertElement' ),
  computedProp: function () {
    debugger;
    return 'width' + this.get('peerl');
  }.observes('peerl')
});
