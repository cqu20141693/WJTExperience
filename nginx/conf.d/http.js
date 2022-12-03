
function hello(r) {
    r.log(r.uri+JSON.stringify(r.args));
    var qrCode = parseQrCode(r.uri);
    switch(qrCode.type){
        case 'd':
	case 'i':
	r.headersOut['Location'] = 'https://tianxiaomi.cc-tech.com/public/qrcode?type=' + qrCode.type + '&param=' + qrCode.param;
	r.return(302);
 	break;
    }
}
function parseQrCode(qrCodeString) {
  const codeReg = /\/([a-zA-Z]):(.+)/;
  const codeRegRes = codeReg.exec(qrCodeString);
  if (codeRegRes) {
    return {
      type: codeRegRes[1],
      param: codeRegRes[2],
    };
  }
  return null;
}
export default { hello };
