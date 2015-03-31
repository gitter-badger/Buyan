import DS from 'ember-data';


let Peer = DS.Model.extend({
  name:DS.attr( 'string' ),
  desc:DS.attr( 'string' ),
  dta:DS.attr()
});
Peer.reopenClass({
  FIXTURES: [
    { id: 1, name: 'James', desc: 'Glowacki', dta:'' },
    { id: 2, name: 'John1' , desc: 'Dale', dta:''    },
    { id: 3, name: 'John2' , desc: 'Dale', dta:''    },

    { id: 4, name: 'John3' , desc: 'Dale', dta:''    },
    { id: 5, name: 'John4' , desc: 'Dale', dta:''    },
    { id: 6, name: 'John5' , desc: 'Dale', dta:''    }
  ]
});
export default Peer;