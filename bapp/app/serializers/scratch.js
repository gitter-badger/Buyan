import DS from 'ember-data';

export default DS.RESTSerializer.extend({
  /*
  extractFindAll: function (store, type, payload, id, requestType) {

    var p= payload.map(function(v,i,a){

      debugger;
      return {id:1,handle: v,ip:'',peers:[]}
    });
    debugger;
    this._super(store, type, p, id, requestType)
  }
  */
  normalizePayload: function(payload) {
    var p= payload.map(function(v,i,a){

    //  debugger;
      return {id:1,handle: v,ip:'',peers1:1}
    });


    return {'scratch':[{id:1,handle: payload[0],ip:'a',peers1:1}]};
  }
});
