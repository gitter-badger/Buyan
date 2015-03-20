//needs es6 promises

function arrayBToString(digest) {
function _arrayBufferToBase64(buffer) {
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
return str;
}

function encode(str) {
return new TextEncoder("utf-8").encode(str);
}

function decode(buf) {
return new TextDecoder("utf-8").decode(buf);
}

function f() {
var self = this;


this.digest = function(type, msg) {

return new Promise(function(resolve, reject) {
crypto.subtle.digest({
name: type
}, encode((typeof(msg) === "string") ? msg : JSON.stringify(msg)))
.then(function(digest) {
resolve(arrayBToString(digest))

return;
});
});
}
}

function digest(type, msg) {

return new Promise(function(resolve, reject) {
crypto.subtle.digest({
name: type
}, encode((typeof(msg) === "string") ? msg : JSON.stringify(msg)))
.then(function(digest) {
resolve(arrayBToString(digest))

return;
});
});
}
