import DS from 'ember-data';

export default DS.RESTAdapter.extend({
    namespace: 'get',
    host: 'http://localhost:8001',
    keyForRelationship: function(key, relationship) {
       return "";
    },
    pathForType: function(type) {
      return "";
    }
});
