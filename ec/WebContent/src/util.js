/* Copyright 2003 VMware, Inc.  All rights reserved. -- VMware Confidential */

// util.js --
//
//      Implement utility functions.


// --------------------------------------------------------------------------
//
// hrVal --
//
//      Return a value (e.g., for disk capacity) in a human readable format.
//      Assume that the units in are megabytes unless otherwise specified.
//
// Prototype:
//
//      string hrVal(int v[, int d[, string ui[, string uo[, int k[, string[] f]]]]])
//
// Arguments:
//
//       v ... The size value
//       d ... The desired number of decimal places to show
//      ui ... Units in (b, k, m, g, t)
//      uo ... Units out (b, k, m, g, t)
//       k ... The value of kilo (e.g., 1000)
//       f ... Value designator format ["", "K", "M", "G", "T"]
//
// Results:
//
//      A human readable version of the value v (e.g., 1.1 G).
//
// --------------------------------------------------------------------------

function hrVal(v, d, ui, uo, k, f) {
  // First, set up the defaults.
  if (d == null) d = 1;
  if (ui == null) ui = "m";
  if (k == null) k = 1024;
  if (f == null) f = ["", "K", "M", "G", "T"];

  // Second, set up the units.
  var kb = k;
  var mb = kb * kb;
  var gb = mb * kb;
  var tb = gb * kb;

  // Third, make sure the value is in bytes.
  switch (ui) {
    case "k":
      v = v * kb;
      break;
    case "m":
      v = v * mb;
      break;
    case "g":
      v = v * gb;
      break;
    case "t":
      v = v * tb;
      break;
    default:
      break;
  }

  // Finally, return v in the requested units with the requested designator.
  if ((v >= tb && uo == null) || uo == "t") {
    return new Number(v / tb).toFixed(d) + (f[4] ? " " + f[4] : "");
  } else if ((v >= gb && uo == null) || uo == "g") {
    return new Number(v / gb).toFixed(d) + (f[3] ? " " + f[3] : "");
  } else if ((v >= mb && uo == null) || uo == "m") {
    return new Number(v / mb).toFixed(d) + (f[2] ? " " + f[2] : "");
  } else if ((v >= kb && uo == null) || uo == "k") {
    return new Number(v / kb).toFixed(d) + (f[1] ? " " + f[1] : "");
  }
  return v + (f[0] ? " " + f[0] : "");
}


// --------------------------------------------------------------------------
//
// esc --
//
//      Escape everything that needs to be escaped but that JavaScript won't.
//
// Prototype:
//
//      string esc(string s)
//
// Arguments:
//
//      s ... The string to escape
//
// Results:
//
//      A URL-encoded version of s. Additionally, '+' will be encoded.
//
// --------------------------------------------------------------------------

function esc(s) {
  var s = escape(s);
  s = s.split("");
  for (var i = 0; i < s.length; i++) {
    if (s[i] == "+") s[i] = "%2B";
  }
  return s.join("");
}


// --------------------------------------------------------------------------
//
// escJs --
//
//      Given a string, return the string with all instances of JavaScript
//      escaped.
//
// Prototype:
//
//      string escJs(string s)
//
// Arguments:
//
//      s ... The string to be escaped
//
// Results:
//
//      A string with all instances of JavaScript special characters replaced
//      by their corresponding character codes.
//
// --------------------------------------------------------------------------

function initEscJs() {
  var d = "x";
  // The following array is in sort order. Please keep it that way.
  var cc = {"!":33,'"':34,"#":35,"$":36,"%":37,"&":38,"'":39,"(":40,")":41,"*":42,"+":43,",":44,"-":45,".":46,"/":47,":":58,";":59,"<":60,"=":61,">":62,"?":63,"@":64,"[":91,"\\":92,"]":93,"^":94,"x":120,"{":123,"|":124,"}":125,"~":126};

  escJs = function (s) {
    s = s.split("");
    var r = "";

    for (var i = 0; i < s.length; i++) {
      var c = cc[s[i]];
      r += c != null ? d + c + d : s[i];
    }

    return r;
  };

  unEscJs = function (s) {
    s = s.split("");
    var r = "";

    for(var i = 0; i < s.length; i++){
      if (s[i] == d) {
        var x = false;
        var t = "";

        while (! x) {
          i++;
          (s[i] == d) ? x = true : t += s[i];
        }

        r += String.fromCharCode(t);
      } else {
        r += s[i];
      }
    }

    return r;
  };

}
initEscJs();

// --------------------------------------------------------------------------
//
// grep --
//
//      Overcome a glaring (at least to Perl programmers) Javascript
//      omission.  Search an array for strings matching the input regexp
//      or string and return an array containing the matches.  Currently
//      just does regexp or string matches; could be extended to take
//      full-fledged expressions like Perl.
//
// Prototype:
//
//      Array grep(Array a, string s)
//
// Arguments:
//
//      a ... The array to be searched
//      s ... The string or regex to be matched
//
// Results:
//
//      An array of elements that match the specified string/regexp.
//
// --------------------------------------------------------------------------

function grep(a, s) {
  var r = new Array();
  // Convert string into a regexp
  if (typeof s == "string") {
    s = new RegExp(s);
  } else if (s.constructor != RegExp) {
    throw "Invalid argument";
  }
  // Annonymous arrays will be undefined if they are empty.
  if (! a) return r;
  for (var i = 0; i < a.length; i++) {
    if (s.test(a[i])) {
      r.push(a[i]);
    }
  }
  return r;
}

// Note: since we use for...in to iterate over Arrays all over the MUI,
// the following assignment currently isn't safe.
// Array.prototype.grep = function (s) { return grep(this, s); };


// --------------------------------------------------------------------------
//
// search --
//
//      Binary search a sorted array.
//
// Prototype:
//
//      int search(Array a, string s)
//
// Arguments:
//
//       a ... The array to be searched
//       s ... The string to be matched
//
// Results:
//
//      The index of the matching array element, or -1.
//
// --------------------------------------------------------------------------

function search(a, s) {
  if (a == null) return -1;
  return _search(a, s, 0, a.length-1);
}

function _search(a,s,h,t) {
  if (h > t) return -1;
  var m = parseInt((h+t)/2);
  if (s == a[m]) return m;
  if (h == t) return -1;
  if (s < a[m]) return _search(a, s, h, m-1);
  return _search(a, s, m+1, t);
}


// --------------------------------------------------------------------------
//
// clone --
//
//      Clone an object using a special constructor method.
//
// Prototype:
//
//      Object new clone(Object o[, Boolean d]);
//
// Arguments:
//
//      o ... The object to be cloned
//      d ... An optional deep flag
//
// Results:
//
//      A cloned object.
//
// --------------------------------------------------------------------------

function clone(o, d) {
  if (o == null) {
    // Anonymous constructions such as [] or {} will report typeof == "object"
    // but will also be null.
    return o;
  }

  var r = new o.constructor();

  for (var i in o) {
    if (d && typeof o[i] == 'object') {
      r[i] = clone(o[i], d);
    } else {
      r[i] = o[i];
    }
  }

  return r;
}


// --------------------------------------------------------------------------
//
// escHtml --
//
//      Given a string, return the string with all instances of problematic
//      HTML characters escaped.
//
// Prototype:
//
//      string escHtml(string s)
//
// Arguments:
//
//      s ... The string to be escaped
//
// Results:
//
//      A string with all instances of problematic HTML characters replaced
//      by their corresponding character entities.
//
// --------------------------------------------------------------------------

function initEscHtml() {
  var escd = {"<":"&lt;",">":"&gt;","&":"&amp;","'":"&#039;",'"':"&quot;"};

  escHtml = function (s) {
    s = s.split("");
    var r = "";

    for (var i = 0; i < s.length; i++) {
      r += escd[s[i]] != null ? escd[s[i]] : s[i];
    }

    return r;
  };
}
initEscHtml();

function defined(a) {
  return typeof a != "undefined" && a != null;
}

function hashString(s) {
  var h = 5381;
  for (var i = 0; i < s.length; i++) {
      h = h * 33 + s.charCodeAt(i);
  }

  return s;
}