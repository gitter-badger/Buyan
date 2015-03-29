import DS from 'ember-data';
let Network = DS.Model.extend({
  
  name: DS.attr(),
  myIdentity: DS.attr(),
  peers:DS.attr()
});
Network.reopenClass({
  FIXTURES: [
    { id: 1, name: 'Trek', desc: 'Glowacki', dta:'' },
    { id: 2, name: 'Tom' , desc: 'Dale', dta:''    }
  ]
});
export default Network;