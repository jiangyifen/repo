var alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/".split("");

function toBase64(str) {
  var out = new Array();
  var i = 0;
  var o = 0;

  while (i < str.length) {
    out[o] = (((str.charCodeAt(i)) >> 2) & 63);
    out[o + 1] = (((str.charCodeAt(i) << 4) | str.charCodeAt(i + 1) >> 4) & 63);
    out[o + 2] = (((str.charCodeAt(i + 1) << 2) | str.charCodeAt(i + 2) >> 6) & 63);
    out[o + 3] = (str.charCodeAt(i + 2) & 63);

    out[o] = alphabet[out[o]];
    out[o + 1] = alphabet[out[o + 1]];
    out[o + 2] = alphabet[out[o + 2]];
    out[o + 3] = alphabet[out[o + 3]];

    o += 4;
    if ((i + 2) >= str.length) {
      if ((i + 1) == str.length) {
        o -= 2;
      }
      if ((i + 2) == str.length) {
        o--;
      }
      while (o % 4) {
        out[o++] = '=';
      }
    }
    i += 3;
  }
  return out.join('');
}

var tebahpla = new Array(
  127,127,127,127,127,127,127,127,
  127,127,127,127,127,127,127,127,
  127,127,127,127,127,127,127,127,
  127,127,127,127,127,127,127,127,
  127,127,127,127,127,127,127,127,
  127,127,127, 62,127,127,127, 63,
   52, 53, 54, 55, 56, 57, 58, 59,
   60, 61,127,127,127,127,127,127,
  127,  0,  1,  2,  3,  4,  5,  6,
    7,  8,  9, 10, 11, 12, 13, 14,
   15, 16, 17, 18, 19, 20, 21, 22,
   23, 24, 25,127,127,127,127,127,
  127, 26, 27, 28, 29, 30, 31, 32,
   33, 34, 35, 36, 37, 38, 39, 40,
   41, 42, 43, 44, 45, 46, 47, 48,
   49, 50, 51,127,127,127,127,127,
  127,127,127,127,127,127,127,127,
  127,127,127,127,127,127,127,127,
  127,127,127,127,127,127,127,127,
  127,127,127,127,127,127,127,127,
  127,127,127,127,127,127,127,127,
  127,127,127,127,127,127,127,127,
  127,127,127,127,127,127,127,127,
  127,127,127,127,127,127,127,127,
  127,127,127,127,127,127,127,127,
  127,127,127,127,127,127,127,127,
  127,127,127,127,127,127,127,127,
  127,127,127,127,127,127,127,127,
  127,127,127,127,127,127,127,127,
  127,127,127,127,127,127,127,127,
  127,127,127,127,127,127,127,127,
  127,127,127,127,127,127,127,127);

function fromBase64(str) {
  var pad = 0;

  for (var i = str.length - 1; str.charAt(i) == '='; i--)
    pad++;

  var length = parseInt(str.length * 6 / 8 - pad);
  var chars = new Array(length);
  index = 0;

  for (var i = 0; i < str.length; i += 4) {
    block = (tebahpla[str.charCodeAt(i)] << 18);
    block += (tebahpla[str.charCodeAt(i + 1)] << 12);
    block += (tebahpla[str.charCodeAt(i + 2)] << 6);
    block += (tebahpla[str.charCodeAt(i + 3)]);

    for (var j = 0; j < 3 && index + j < length; j++)
      chars[index + j] = String.fromCharCode((block >> (8 * (2 - j))) & 0xff);

    index += 3;
  }
  return chars.join('');
}
