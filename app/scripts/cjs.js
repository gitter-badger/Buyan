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
