import DS from 'ember-data';


let Peer = DS.Model.extend({
  name:DS.attr( 'string' ),
  desc:DS.attr( 'string' ),
  dta:DS.attr()
});
Peer.reopenClass({
  FIXTURES: [
    { id: 1, name: 'Mane', desc: 'Glowacki', dta:'' },
    { id: 2, name: 'Cane' , desc: 'Dale', dta:''    },
    { id: 3, name: 'Mane' , desc: 'Dale', dta:''    },

    { id: 4, name: 'Cane' , desc: 'Dale', dta:''    },
    { id: 5, name: 'Mane' , desc: 'Dale', dta:''    },
    { id: 6, name: 'Cane' , desc: 'Dale', dta:''    }
  ]
});
export default Peer;