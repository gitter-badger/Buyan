var nodeinfo={
    role: 'initiator'

}
var protocol=
{
    systemInfo: system,
    states: {
        start:{
            versioninit: [],
            versionack: ['exchangepeers'],
            rejectpeer: ['work']
        },
        exchangepeers:{
            addresses: ['work']
        },
        work:{
            inv:[],
            getdata:[],
            notfound:[],
            getblocks:[],
            getheaders:[]
        }
    }
};
function protocolRunner(state,protocol){
    console.log('started protocol runner');
    
    protocol.states[state];
}