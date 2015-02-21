'use strict'
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
function compare(buf1, difficulty) {
  var a = new Uint8Array(buf1);


  for (var i = 0; (i < a.byteLength && i < difficulty); i++) {
    if (a[i] !== 0) {
      return false;
    }
  }

  return true;
}