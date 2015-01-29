// crypto.subtle.digest({name: "SHA-256"}, buffer)
//      .then(function (digest) {
function encode(str) {
  return new TextEncoder("utf-8").encode(str);
}

function decode(buf) {
  return new TextDecoder("utf-8").decode(buf);
}
function checkH(buf1, difficulty) {
  var a = new Uint8Array(buf1);


  for (var i = 0; (i < a.byteLength && i < difficulty); i++) {
    if (a[i] !== 0) {
      return false;
    }
  }

  return true;
}
function compare(buf1, buf2) {
  var a = new Uint8Array(buf1);
  var b = new Uint8Array(buf2);

  if (a.byteLength != b.byteLength) {
    return false;
  }

  for (var i = 0; i < a.byteLength; i++) {
    if (a[i] !== b[i]) {
      return false;
    }
  }

  return true;
}

/*
dummy miner
TODO put script loop instead of timeout
*/
var run=false;
var hash="asdsad";
var nonce="nonce";
var difficulty=1;
var run = true;
var not_found=false;
function compute_hash(merkleRoot){
  found=false;
  nonce=generateNonce(true);
  (function itteration(){
   if(run && !found){
    nonce=generateNonce(false);
    crypto.subtle.digest({name: "SHA-256"}, encode(merkleRoot+nonce))
      .then(function (digest) {

         found=checkH(digest,difficulty);
         itteration();
        return ;
      });

   }
   })();
   if(run==true){
    setTimeout(function(){
      //{'root':event.data, 'nonce': 101}
      console.log(JSON.stringify({root: "somehash: "+ merkleRoot,nonce: "somenonce "+ nonce}));
        postMessage(JSON.stringify({root: "somehash: "+ merkleRoot,nonce: "somenonce "+ nonce}));
    },0);
  }
  
}
onmessage = function(event) {
   console.log("recieved work ",event.data.args);
   switch(event.data.args[0]) {
    case "newjob":
        hash=event.data.args[1];
        run=true;
        nonce="";
        compute_hash(hash)
        break;
    case "stop":
        run=false;
        break;
    case "start":
        run=true;
        break;
    default:
        console.log("no message");
   } 
  
}