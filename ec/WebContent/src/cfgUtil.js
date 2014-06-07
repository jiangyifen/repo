/* Copyright 2003 VMware, Inc.  All rights reserved. -- VMware Confidential */

// cfgUtil.js --
//
//      Implement utility functions for viewing and modifying configuration
//      details.


// ------------------------------------------------------------------------
//
// initKeyRow --
//
//      Given a seed id, a key name and a key label, dup a key row and fill
//      in the label. The key row is expected to be of a form similar to:
//
//           <tr id="keyRow">
//             <td><div id="key"></div></td>
//             <td><div id="val"></div></td>
//           </tr>
//
// Prototype:
//
//      string initKeyRow(string i, string n, string l [,Object d, Object t,
//                        Object c])
//
// Arguments:
//
//      i ... A seed id
//      n ... The key name
//      l ... The human readable key label
//      d ... An object in which this keyrow should be listed
//      t ... The table to which the key row should be appended
//      k ... The key row to clone
//      b ... An optional sybling row, before which k should be inserted
//
// Results:
//
//      initKeyRow inserts a key-value row, and returns the key row ID con-
//      structed by append n to i.
//
// ------------------------------------------------------------------------

function initKeyRow(i, n, l, d, t, k, b) {
  i = i + (i ? ":" : "") + n;

  if (d == null) d = dom;
  if (t == null) t = dom.bdy;
  if (k == null) k = dom.keyRow;

  d[i] = new Object();

  if (b == null) {
    d[i].keyRow = t.appendChild(k.cloneNode(true));
  } else {
    d[i].keyRow = t.insertBefore(k.cloneNode(true), b);
  }
  objAtt(d[i].keyRow, "id", i);

  var td = d[i].keyRow.getElementsByTagName("TD");
  for (var j = 0; j < td.length; j++) {
    var id = objAtt(td[j], "id");
    if (id != null && id != "") {
      d[i][id+"Td"] = td[j];
      objAtt(td[j], "id", "");
    }

    d[i]["valTd" + j] = td[j]; // dom[i].valTd2, etc.
  }

  d[i].key = obj("key");
  d[i].key.att("id", "");
  if (l) {
    d[i].key.innerHTML = l;
  }

  d[i].val = obj("val");
  d[i].val.att("id", "");

  return i;
}


// ------------------------------------------------------------------------
// htmlSlct --
//
//      Given a name, an array of values, an optional selected value, an
//      optional array of labels and an optional onchange method, return
//      an HTMLSelectElement.
//
// Prototype:
//
//      string htmlSlct(string n, Array v[, string s[, Array k[, string m]]])
//
// Arguments:
//
//      n ... The name that should be assigned to the HTMLSelectElement
//      v ... An array of values for the HTMLSelectElement options
//      s ... The value of the selected option, if any
//      l ... An optional array of labels
//      m ... An optional onChange event handler
//
// Results:
//
//      An HTMLSelectElement string.
//
// ------------------------------------------------------------------------

function htmlSlct(n, v, s, l, m) {
  m = m == null ? "" : ' onchange="'+m+'"';
  var h = '<select name="'+n+'"'+m+'">';

  for (var i in v) {
    h += '<option value="' + v[i] + '"' + (s && v[i] == s ? ' selected' : '') +
      '>' + (l != null ? l[i] : v[i]) + '</option>';
  }

  return h + '</select>';
}


// ------------------------------------------------------------------------
// htmlTxtInpt --
//
//      Given a name, an optional value and an optional onchange method,
//      return an HTMLTextInputElement.
//
// Prototype:
//
//      string htmlTxtInpt(string n[, string v[, string m]])
//
// Arguments:
//
//      n ... The name that should be assigned to the HTMLTextInputElement
//      v ... A value for the HTMLTextInputElement
//      m ... An optional onChange event handler
//
// Results:
//
//      An HTMLTextInputElement string.
//
// ------------------------------------------------------------------------

function htmlTxtInpt(n, v, m) {
  if (v == null) v = "";
  m = m == null ? "" : ' onchange="'+m+'"';

  return '<input type="text" class="text" name="'+n+'" value="'+v+'"'+m+' />';
}


// ------------------------------------------------------------------------
// htmlHdnInpt --
//
//      Given a name and an optional value, return an HTMLHiddenInputElement.
//
// Prototype:
//
//      string htmlHdnInpt(string n[, string v[, string m]])
//
// Arguments:
//
//      n ... The name that should be assigned to the HTMLHiddenInputElement
//      v ... A value for the HTMLHiddenInputElement
//
// Results:
//
//      An HTMLHiddenInputElement string.
//
// ------------------------------------------------------------------------

function htmlHdnInpt(n, v) {
  if (v == null) v = "";

  return '<input type="hidden" name="'+n+'" value="'+v+'" />';
}
