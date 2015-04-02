
import DS from 'ember-data';


export default DS.RESTAdapter.extend({
    namespace: 'peers/',
    host: 'http://localhost:8000/prokletdajepapa',
    keyForRelationship: function(key, relationship) {
       return "";
    },
    pathForType: function(type) {
      return "";
    }
});
