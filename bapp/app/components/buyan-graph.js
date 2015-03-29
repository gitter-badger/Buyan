import Ember from 'ember';
import layout from '../templates/components/buyan-graph';
 

  
function Network(id){
    var g={nodes:[],edges:[]};
    var self=this;
    sigma.renderers.def = sigma.renderers.canvas;
    var s = new sigma({
      graph:g ,
      container: id
    });
    self.s=s;
    sigma.plugins.dragNodes(s, s.renderers[0]);
    function mknode(labl,x,y){
      x=x || Math.random();
      y=y || Math.random();

      var i = i || 0;
      var label = labl ;
      return {
        id: label,
        label: label,
        x: x,
        y: y,
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
      connectToAllInitial();
      s.refresh();
    }
    self.addMe=function(myId){
        debugger;
      self.me=myId;
      self.s.graph.addNode(mknode(myId));

      self.s.refresh();
    }
    self.addNode=function(name,x,y){
      var x = Math.random();
      var y = sqrt(1-x*x)*(Math.random()>0.5?-1:1);
      self.s.graph.addNode(mknode(name,x,y));
      self.s.graph.addEdge({
              id: 'e-'+self.me + "-"+name,
              source: self.me,
              target: name,
              size: 1,
              color: '#ccc'
            });
      self.s.refresh();
    }
    $(document).on("setID",addMef);
}
export default Ember.Component.extend({
  layout: layout,

  networkId: ''+(Math.floor(Math.random() * (10000000 - 0)) + 0),
  networkName: '',
  renderR: null ,

  setupTooltip: function () {
    //this.set("")
    $('.search-select')
      .dropdown()
    ;
    var networkId=this.get('networkId');
    var renderR = new Network(networkId);
    this.set("renderR",renderR);
    this.set("networkName",networkId);
    debugger;
    /*
    var Ids=this.get('targetObject.store').findAll('ids').then(function(){
    
      this.set('ids',Ids);
    });
*/
  }.on( 'didInsertElement' ),
  actions:{
    setMyIdForThisNetwork: function(id){
        debugger;
        this.get('renderR').addMe(id);
        console.log(id);
    }
      
  },
  computedProp: function () {
    debugger;
    return 'width' + this.get('peerl');
  }.observes('peerl')
});
