/* jshint node: true */

module.exports = function(environment) {
  var ENV = {
    modulePrefix: 'bapp',
    environment: environment,
    locationType: 'auto',
    EmberENV: {
      FEATURES: {
        // Here you can enable experimental features on an ember canary build
        // e.g. 'with-controller': true
      }
    },

    APP: {
      // Here you can pass flags/options to your application instance
      // when it is created
    }
  };

  if (environment === 'development') {
    // ENV.APP.LOG_RESOLVER = true;
    // ENV.APP.LOG_ACTIVE_GENERATION = true;
    // ENV.APP.LOG_TRANSITIONS = true;
    // ENV.APP.LOG_TRANSITIONS_INTERNAL = true;
    // ENV.APP.LOG_VIEW_LOOKUPS = true;

    ENV.baseURL = '/';  
  }

  if (environment === 'test') {
    // Testem prefers this...
    ENV.baseURL = '/';
    ENV.locationType = 'none';

    // keep test console output quieter
    ENV.APP.LOG_ACTIVE_GENERATION = false;
    ENV.APP.LOG_VIEW_LOOKUPS = false;

    ENV.APP.rootElement = '#ember-testing';
  }

  if (environment === 'production') {

    ENV.baseURL= '/Buyan/bapp/dist/';
    ENV.fingerprint= {
      enabled: false
    }
  }
ENV.contentSecurityPolicy = {
  'default-src': "'none'",
  'script-src': "'self' *", // Allow scripts from https://cdn.mxpnl.com
  'font-src': "'self' *", // Allow fonts to be loaded from http://fonts.gstatic.com
  'connect-src': "'self' https://api.mixpanel.com http://custom-api.local ws://localhost:8000 http://localhost:8000", // Allow data (ajax/websocket) from api.mixpanel.com and custom-api.local
  'img-src': "'self'",
  'style-src': "'self' 'unsafe-inline' http://fonts.googleapis.com", // Allow inline styles and loaded CSS from http://fonts.googleapis.com 
  'media-src': "'self'"
}
  return ENV;
};
