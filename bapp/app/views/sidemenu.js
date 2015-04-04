import Ember from 'ember';

export default Ember.View.extend({
 didInsertElement : function(){
    this._super();
    Ember.run.scheduleOnce('afterRender', this, function(){
    	// perform your jQuery logic here
    	   $('.accordion')
	      .accordion({
	        selector: {
	          trigger: '.title'
	        }
	      })
	    ;
    });
  }
});
