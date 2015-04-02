import DS from 'ember-data';
let Network = DS.Model.extend({
  
  name: DS.attr(),
  myIdentity: DS.attr(),
  peers: DS.attr()//DS.hasMany('peer',{async:true})	
});
Network.reopenClass({
  FIXTURES: [
    { id: 1,name: "network einz", myIdentity: '2',peers:[]  },
    { id: 2, name: "n zweye", myIdentity: '1' ,peers:[]  }
  ]
});
export default Network;