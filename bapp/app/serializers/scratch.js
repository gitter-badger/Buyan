import DS from 'ember-data';

export default DS.RESTSerializer.extend({
  /*
  extractFindAll: function (store, type, payload, id, requestType) {

      debugger;
    var p= payload.map(function(v,i,a){

      debugger;
      return {id:1,handle: v,ip:'',peers:[]}
    });
    debugger;
    this._super(store, type, p, id, requestType)
  },
    */
  normalizePayload: function(payload) {
  var i=0;
  function isJson(str){
        try {
            JSON.parse(str);
        } catch (e) {
            return false;
        }
        return true;
      }
    payload=_.uniq(payload);
    payload=payload.filter(function(v,i,a){return v!=="" && isJson(v)})
    var p= payload.map(function(v,i,a){

      //debugger;
      var parsed = JSON.parse(v);
      parsed.id=i;
      return parsed;
    });


    return {'scratch':p};
  }
});
