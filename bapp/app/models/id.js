import DS from 'ember-data';
let Id = DS.Model.extend({
  name:DS.attr( 'string' ),
  desc:DS.attr( 'string' ),
  dta:DS.attr()
});
Id.reopenClass({
  FIXTURES: [
    { id: 1, name: 'Trek', desc: 'Glowacki', dta:'' },
    { id: 2, name: 'Tom' , desc: 'Dale', dta:''    }
  ]
});
export default Id;