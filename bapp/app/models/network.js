import DS from 'ember-data';
let Network = DS.Model.extend({
  
  id: attr(),
  name: attr(),
  myIdentity: attr(),
  peers:attr()
});
Network.reopenClass({
  FIXTURES: [
    { id: 1, name: 'Trek', desc: 'Glowacki', dta:'' },
    { id: 2, name: 'Tom' , desc: 'Dale', dta:''    }
  ]
});
export default Network;