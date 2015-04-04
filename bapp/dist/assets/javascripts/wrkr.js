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



var nonce=false;
var merkleRoot="root";
var difficulty=1;
var run = true;
var found=false;
function arrayBToString(digest){
function _arrayBufferToBase64( buffer ) {
  var ret = "",
  i = 0,
  len = buffer.length;
  while (i < len) {
  var a = buffer[i];
  var h = (a < 10 ? "0" : "") + a.toString(16);
  ret += h;
  i++;
}
return ret;
}
var str = _arrayBufferToBase64(new Uint8Array(digest));
return str
}
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

  return arrayBToString(a);
}

onmessage = function(event) {
   console.log("recieved work ",event.data.args);
   switch(event.data.args[0]) {
    case "newjob":
        merkleRoot =event.data.args[1];
        run=true;
        nonce=false;
        found=false;
        //compute_hash(hash)
          (function itteration(){
           if(run && !found){
            nonce=nonce+1;
            crypto.subtle.digest({name: "SHA-256"}, encode(merkleRoot+nonce))
              .then(function (digest) {
                 found=checkH(digest,difficulty);
                 itteration();
                return ;
              });

           }
              if(run==true && found){
               setTimeout(function(){
                 //{'root':event.data, 'nonce': 101}
                 var messag=JSON.stringify({root: "somehash: "+ merkleRoot,
                                             nonce: "somenonce "+ nonce,
                                             newhash:found});
                 console.log(messag);
                 postMessage(messag);
               },0);
               }
           })();
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