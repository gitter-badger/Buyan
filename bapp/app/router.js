import Ember from 'ember';
import config from './config/environment';

var Router = Ember.Router.extend({
  location: config.locationType
});

Router.map(function() {
  this.route("scratch",{path: '/'});
  this.resource("peers");




});

export default Router;
