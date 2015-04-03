import DS from 'ember-data';

export default DS.RESTSerializer.extend({
	  normalizePayload: function(payload) {
	  	debugger;
  var i=0;
  function isJson(str){
        try {
            JSON.parse(str);
        } catch (e) {
            return false;
        }
        return true;
      }
    var p= payload.map(function(v,i,a){

      var parsed = { id: i, name: v, desc: 'Glowacki', dta:'' };
      return parsed;
    });


    return {'peer':p};
  }
});
