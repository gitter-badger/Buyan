import Ember from 'ember';
function makeid()
{
    var text = "";
    var possible = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    for( var i=0; i < 10; i++ )
        text += possible.charAt(Math.floor(Math.random() * possible.length));

    return text;
}

export default Ember.View.extend({
  randomId: makeid(),
  click: function(e){
    if(arguments[0].target.id==="enterbuyan"){
      $(document).trigger("setid",this.randomId);  
    }

  }
});
