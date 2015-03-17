import DS from 'ember-data';
var attr = DS.attr;
export default DS.Model.extend({
  handle: attr(),
  ip: attr(),
  source: attr(),
  peers: attr()
});
