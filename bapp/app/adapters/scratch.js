
import DS from 'ember-data';
export default DS.RESTAdapter.extend({

    namespace: 'prokletdajepapa/peers',
    host: 'http://localhost:8000',
    keyForRelationship: function(key, relationship) {
       return "";
    },
    pathForType: function(type) {
      return "";
    }
});
