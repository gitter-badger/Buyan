
import DS from 'ember-data';

var myadapter;
myadapter=DS.Adapter.extend({
  // ...Post-specific adapter code goes here
    find: function(){

    },
    createRecord: function(){

    },
    updateRecord: function(){

    },
    deleteRecord: function(){

    },
    findAll: function(){

    },
    findQuery: function(){

    }

});


myadapter=DS.RESTAdapter.extend({
    namespace: 'peers',
    host: 'http://localhost:8000/prokletdajepapa',
    keyForRelationship: function(key, relationship) {
       return "";
    },
    pathForType: function(type) {
      return "";
    }
});

export default myadapter;