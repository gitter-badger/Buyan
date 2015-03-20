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
    var p= payload.map(function(v,i,a){

      debugger;
      return {id:i,handle: v,ip:'',peers1:1}
    });


    return {'scratch':p};
  }
});
