/* Copyright 1998 VMware, Inc.  All rights reserved. -- VMware Confidential */

// xuaLib.js --
//
//      Provides wrappers for MSIE 5.5+ and Gecko DHTML functionality.
//
//      By convention, this code is terse. Please comment thoroughly.


var ie = document.all;
var ie5_5 = (ie && (navigator.userAgent.toLowerCase()).indexOf('msie 5.5')>-1);

// --------------------------------------------------------------------------
//
// _dfa --
//
//      Dimension From Arguments: Given a list of arguments a, determine if
//      the arguments represent individual width and height arguments, or a
//      Dim object.
//
// Prototype:
//
//      Dim _dfa(Object a[ ])
//
// Arguments:
//
//      a ... The list of arguments
//
// Results:
//
//      A Dim object.
//
// --------------------------------------------------------------------------

function _dfa(a) {
  if (a.length) {
    // XXX: How should we flag an error?
    if (a.length > 2) return null;
    // Assume that 2 array elements specify width and height.
    if (a.length > 1) return new Dim(a[0], a[1]);
    // Assume a[0] is a valid Dim object.
    return a[0];
  }
  return null;
}


// --------------------------------------------------------------------------
//
// _pfa --
//
//      Position From Arguments: Given a list of arguments a, determine if
//      the arguments represent individual x, y and/or z coordinates, or a
//      Pos object.
//
// Prototype:
//
//      Pos _pfa(Object a[ ])
//
// Arguments:
//
//      a ... The list of arguments
//
// Results:
//
//      A Pos object.
//
// --------------------------------------------------------------------------

function _pfa(a) {
  if (a.length) {
    // XXX: How should we flag an error?
    if (a.length > 3) return null;
    // Assume 2 or more array elements specify x, y and maybe z coordinates.
    if (a.length > 1) return new Pos(a[0], a[1], a.length > 2 ? a[2] : null);
    // Assume a[0] is a valid Pos object.
    return a[0];
  }
  return null;
}


// --------------------------------------------------------------------------
//
// _dim --
//
//      This function is intended to be an object member method. As such it
//      operates on the implicit object this. _dim returns the this object's
//      dimensions as a Dim object. If any optional arguments are supplied,
//      the this object's dimensions are set before being returned.
//
// Prototype:
//
//      Dim _dim([[Dim d][int w, int h]])
//
// Arguments:
//
//      d ... An optional Dim object that supplies the dimensions for this
//      w ... An optional width for this
//      h ... An optional height for this
//
// Results:
//
//      A Dim object for the dimensions of this.
//
// --------------------------------------------------------------------------

function _dim() {
  var d = _dfa(arguments);
  if (d) {
    this.d = objDim(this, d);
  }
  return objCss(this, "position") == "absolute" ? this.d : objDim(this);
}


// --------------------------------------------------------------------------
//
// _pos --
//
//      This function is intended to be an object member method. As such it
//      operates on the implicit object this. _pos returns the this object's
//      position as a Pos object. If any optional arguments are supplied,
//      the this object's position is set before being returned.
//
// Prototype:
//
//      Pos _pos([[Pos p][int x, int y[, int z]]])
//
// Arguments:
//
//      p ... An optional Pos object that supplies the position for this
//      x ... An optional position on the x axis
//      y ... An optional position on the y axis
//      z ... An optional position on the z axis
//
// Results:
//
//      A Pos object for the position of this.
//
// --------------------------------------------------------------------------

function _pos() {
  var p = _pfa(arguments);
  if (p) {
    this.p = absPos(this, p);
  }
  return objCss(this, "position") == "absolute" ? this.p : absPos(this);
}


// --------------------------------------------------------------------------
//
// _ibp --
//
//      This function is intended to be an object member method. As such it
//      operates on the implicit object this. Given valid coordinates, _ibp
//      will position the this object as near to the specifed coordinates as
//      possible without placing any part of the this object outside of the
//      inner bounds of the window.
//
// Prototype:
//
//      Pos _pos([[Pos p][int x, int y[, int z]]])
//
// Arguments:
//
//      p ... A Pos object that supplies the position for this
//      x ... A position on the x axis
//      y ... A position on the y axis
//      z ... A position on the z axis
//
// Results:
//
//      A Pos object for the position of this.
//
// --------------------------------------------------------------------------

function _ibp() {
  var p = _pfa(arguments);
  if (p) {
    if (p.x + this.dim().w > self.dim().w + viewLeft())
      p.x = self.dim().w - this.dim().w + viewLeft();

    if (p.y + this.dim().h > self.dim().h + viewTop())
      p.y = self.dim().h - this.dim().h + viewTop();

    if (p.x < viewLeft())
      p.x = viewLeft();

    if (p.y < viewTop())
      p.y = viewTop();

    this.p = absPos(this, p);
  }
  return objCss(this, "position") == "absolute" ? this.p : absPos(this);
}


// --------------------------------------------------------------------------
//
// _css --
//
//      This function is intended to be an object member method. As such it
//      operates on the implicit this object. _css returns the value of the
//      this object's CSS property p, if any. If the optional v argument is
//      supplied, property p is set before being returned.
//
// Prototype:
//
//      string _css(string p[, string v])
//
// Arguments:
//
//      p ... A CSS property name
//      v ... An optional value to be assigned to property p
//
// Results:
//
//      The string value of CSS property p for this, or null. Note that the
//      value of p may be a null string (i.e., "").
//
// --------------------------------------------------------------------------

function _css(p, v) {
  return objCss(this, p, v);
}


// --------------------------------------------------------------------------
//
// _att --
//
//      This function is intended to be an object member method. As such it
//      operates on the implicit this object. _att returns the value of the
//      this object's HTML attribute a, if any. If the optional v argument is
//      supplied, attribute a is set before being returned.
//
// Prototype:
//
//      string _att(string a[, string v])
//
// Arguments:
//
//      a ... An HTML attribute name
//      v ... An optional value to be assigned to attribute a
//
// Results:
//
//      The string value, if any, of attribute a for this, if any.
//
// --------------------------------------------------------------------------

function _att(a, v) {
  return objAtt(this, a, v);
}


// --------------------------------------------------------------------------
//
// initXuaObj --
//
//      This function adds XUA object methods to HTML elements. Some initial-
//      ization is performed on positioned elements to make sure that they
//      behave consistently.
//
// Prototype:
//
//      Object initObj(Object o)
//
// Arguments:
//
//      o ... The object that will be initialized
//
// Results:
//
//      The initialized object.
//
// --------------------------------------------------------------------------

function initXuaObj(o) {
  var d;

  if (objCss(o, "position") == "absolute") {
    // Size the element once to enable calibration.
    if (ie) {
      d = new Dim(o.offsetWidth, o.offsetHeight);
    } else {
      // Calibrate an object's dimensions. This step is pretty expensive for
      // Gecko-based browsers, which is ironic considering the fact that it is
      // only necessary for them because they size an object before adding
      // padding and borders, and we really want the object's total dimensions
      // including padding and borders.
      var od = objDim(o);
      objCss(o, "width", 100);
      objCss(o, "height", 100);
      d = objDim(o, od);
    }
  }

  // Use of o.doc() instead of o.contentWindow.document or o.document to
  // provide a better guarantee of success.
  if (o.tagName.toLowerCase() == "iframe") {
    o.win = _ifwin;
    o.doc = _ifdoc;
  }

  o.dim = _dim;
  if (d != null) {
    o.d = d;
  } else {
    o.d = objDim(o);
  }

  o.pos = _pos;
  o.p = absPos(o);

  o.ibp = _ibp;
  o.css = _css;
  o.att = _att;

  o.scrollToX = _scrollToX;
  o.scrollToY = _scrollToY;
  o.scrollTo  = _scrollTo;

  return o;
}


// --------------------------------------------------------------------------
//
// obj --
//
//      This function is shorthand for
//
//           initXuaObj(window.document.getElementById(<id>));
//
// Prototype:
//
//      Object initObj(String s[, Window w])
//
// Arguments:
//
//      s ... The HTML ID of the desired element
//      w ... An optional window where the desired element can be found
//
// Results:
//
//      If successful, obj returns an HTML element object initialized with
//      XUA object methods. Otherwise it returns null.
//
// --------------------------------------------------------------------------

function obj(s, w) {
  if (!w) w = self;
  o = w.document.getElementById(s);

  if (!o || o == null) return null;

  initXuaObj(o);

  return o;
}


// --------------------------------------------------------------------------
//
// Pos --
//
//      Pos(ition) object constructor.
//
// Prototype:
//
//      new Pos(int x, int y, int z)
//
// Arguments:
//
//      x ... a position on the x axis
//      y ... a position on the y axis
//      z ... a position on the z axis
//
// Results:
//
//      A new Pos instance.
//
// --------------------------------------------------------------------------

function Pos(x, y, z) {
  this.x = x;
  this.y = y;
  this.z = z;

  this.toString = function() {
    return this.x + ',' + this.y + (z ? ',' + z : '');
  };
}


// --------------------------------------------------------------------------
//
// absX --
//
//      This function returns the absolute position of object o on the x axis.
//      If the optional x argument is supplied, o will be moved before its
//      position is returned.
//
// Prototype:
//
//      int absX(Object o[, int x])
//
// Arguments:
//
//      o ... An HTML Element object
//      x ... An optional position on the x axis
//
// Results:
//
//      Object o's position on the x axis.
//
// --------------------------------------------------------------------------

function absX(o, x) {
  if (x != null) o.style.left = x;
  var ol = o.offsetLeft;
  while ((o = o.offsetParent)) ol += o.offsetLeft;
  return ol;
}


// --------------------------------------------------------------------------
//
// absY --
//
//      This function returns the absolute position of object o on the y axis.
//      If the optional y argument is supplied, o will be moved before its
//      position is returned.
//
// Prototype:
//
//      int absY(Object o[, int y])
//
// Arguments:
//
//      o ... An HTML Element object
//      y ... An optional position on the y axis
//
// Results:
//
//      Object o's position on the y axis.
//
// --------------------------------------------------------------------------

function absY(o, y) {
  if (y != null) o.style.top = y;
  var ot = o.offsetTop;
  while ((o = o.offsetParent)) ot += o.offsetTop;
  return ot;
}


// --------------------------------------------------------------------------
//
// objZ --
//
//      This function returns the absolute position of object o on the z axis.
//      If the optional z argument is supplied, o will be moved before its
//      position is returned.
//
// Prototype:
//
//      int objZ(Object o[, int z])
//
// Arguments:
//
//      o ... An HTML Element object
//      z ... An optional position on the z axis
//
// Results:
//
//      Object o's position on the z axis.
//
// --------------------------------------------------------------------------

function objZ(o, z) {
  if (z != null) o.style.zIndex = z;
  return o.style.zIndex;
}


// --------------------------------------------------------------------------
//
// absPos --
//
//      This function is shorthand for
//
//           new Pos(absX(o, x), absY(o, y), objZ(o, z))
//
// Prototype:
//
//      Pos absPos(Object o[,[Pos p][int x, int y[, int z]]])
//
// Arguments:
//
//      o ... An HTML Element object
//      p ... An optional Pos object representing x, y and z
//      x ... An optional position on the x axis
//      y ... An optional position on the y axis
//      z ... An optional position on the z axis
//
// Results:
//
//      A Pos object representing object o's position on the x, y and z axes.
//
// --------------------------------------------------------------------------

function absPos(o) {
  var x, y, z;
  if (arguments.length > 2) {
    x = arguments[1];
    y = arguments[2];
    z = arguments[3];
  } else if (arguments.length == 2) {
    x = arguments[1].x;
    y = arguments[1].y;
    z = arguments[1].z;
  }
  return new Pos(absX(o, x), absY(o, y), objZ(o, z));
}


// --------------------------------------------------------------------------
//
// scrollToObjX --
//
//      Set the window scroll position to the left position of the
//      specified object.
//
// Prototype:
//
//      Pos scrollToObjX(Object o)
//
// Arguments:
//
//      o ... An HTML Element object
//
// Results:
//
//      A Pos object representing the current x, y scroll position. The scroll
//      position will not necessarily equal the object's position.
//
// --------------------------------------------------------------------------

function scrollToObjX(o) {
  self.scrollTo(absPos(o).x, self.viewTop());
  return new Pos(self.viewLeft(), self.viewTop());
}


// --------------------------------------------------------------------------
//
// scrollToObjY --
//
//      Set the window scroll position to the top position of the
//      specified object.
//
// Prototype:
//
//      Pos scrollToObjY(Object o)
//
// Arguments:
//
//      o ... An HTML Element object
//
// Results:
//
//      A Pos object representing the current x, y scroll position. The scroll
//      position will not necessarily equal the object's position.
//
// --------------------------------------------------------------------------

function scrollToObjY(o) {
  self.scrollTo(self.viewLeft(), absPos(o).y);
  return new Pos(self.viewLeft(), self.viewTop());
}


// --------------------------------------------------------------------------
//
// scrollToObj --
//
//      Set the window scroll position to the top, left position of the
//      specified object.
//
// Prototype:
//
//      Pos scrollToObj(Object o)
//
// Arguments:
//
//      o ... An HTML Element object
//
// Results:
//
//      A Pos object representing the current x, y scroll position. The scroll
//      position will not necessarily equal the object's position.
//
// --------------------------------------------------------------------------

function scrollToObj(o) {
  self.scrollTo(absPos(o).x, absPos(o).y);
  return new Pos(self.viewLeft(), self.viewTop());
}


// --------------------------------------------------------------------------
//
// _scrollToX --
//
//      This function is intended to be an object member method. As such it
//      operates on the implicit object this. _scrollToX sets the window scroll
//      position to the left position of the this object.
//
// Prototype:
//
//      Pos _scrollToX()
//
// Results:
//
//      A Pos object representing the current x, y scroll position. The scroll
//      position will not necessarily equal the object's position.
//
// --------------------------------------------------------------------------

function _scrollToX() {
  return scrollToObjX(this);
}


// --------------------------------------------------------------------------
//
// _scrollToY --
//
//      This function is intended to be an object member method. As such it
//      operates on the implicit object this. _scrollToY sets the window scroll
//      position to the top position of the this object.
//
// Prototype:
//
//      Pos _scrollToY()
//
// Results:
//
//      A Pos object representing the current x, y scroll position. The scroll
//      position will not necessarily equal the object's position.
//
// --------------------------------------------------------------------------

function _scrollToY() {
  return scrollToObjY(this);
}


// --------------------------------------------------------------------------
//
// _scrollTo --
//
//      This function is intended to be an object member method. As such it
//      operates on the implicit object this. _scrollToX sets the window scroll
//      position to the top, left position of the this object.
//
// Prototype:
//
//      Pos _scrollTo()
//
// Results:
//
//      A Pos object representing the current x, y scroll position. The scroll
//      position will not necessarily equal the object's position.
//
// --------------------------------------------------------------------------

function _scrollTo() {
  return scrollToObj(this);
}


// --------------------------------------------------------------------------
//
// Dim --
//
//      Dim(ensions) object constructor.
//
// Prototype:
//
//      new Dim(int w, int h)
//
// Arguments:
//
//      w ... width
//      h ... height
//
// Results:
//
//      A new Dim instance.
//
// --------------------------------------------------------------------------

function Dim(w, h) {
  this.w = w;
  this.h = h;

  this.toString = function() {
    return this.w + 'x' + this.h;
  };
}


// --------------------------------------------------------------------------
//
// objW --
//
//      This function returns the rendering width of object o. If the optional
//      w argument is supplied, o will be sized before its width is returned.
//      Specifying w as -1 is equivalent to
//
//           o.style.width = o.offsetWidth
//
// Prototype:
//
//      int objW(Object o[, int w])
//
// Arguments:
//
//      o ... An HTML Element object
//      w ... An optional width
//
// Results:
//
//      Object o's rendering width. An adjustment is made for Gecko-based
//      browsers because they do not include borders and padding when
//      reporting width. This adjustment may not be correctly applied if
//      object o has not been prepped by initXuaObj().
//
// --------------------------------------------------------------------------

function objW(o, w) {
  if (w != null) {
    if (w > -1 && ! ie && o.offsetWidth > -1 && objCss(o, 'width') != null) {
      // Gecko-based browsers will report o's offsetWidth as being smaller
      // than its rendering width because they do not include borders and
      // padding in offsetWidth. Add the difference between o's reported
      // offsetWidth and its current CSS width to the supplied width w.
      w += parseInt(objCss(o, 'width')) - o.offsetWidth;
    }

    o.style.width = w > -1 ? w : o.offsetWidth;
  }
  return o.offsetWidth;
}


// --------------------------------------------------------------------------
//
// objH --
//
//      This function returns the rendering height of object o. If the optional
//      h argument is supplied, o will be sized before its height is returned.
//      Specifying h as -1 is equivalent to
//
//           o.style.height = o.offsetHeight
//
// Prototype:
//
//      int objH(Object o[, int h])
//
// Arguments:
//
//      o ... An HTML Element object
//      h ... An optional height
//
// Results:
//
//      Object o's rendering height. An adjustment is made for Gecko-based
//      browsers because they do not include borders and padding when
//      reporting height. This adjustment may not be correctly applied if
//      object o has not been prepped by initXuaObj().
//
// --------------------------------------------------------------------------

function objH(o, h) {
  if (h != null) {
    if (h > -1 && ! ie && o.offsetHeight > -1 && objCss(o, 'height') != null) {
      // Gecko-based browsers will report o's offsetHeight as being smaller
      // than its rendering height because they do not include borders and
      // padding in offsetHeight. Add the difference between o's reported
      // offsetHeight and its current CSS height to the supplied height h.
      h += parseInt(objCss(o, 'height')) - o.offsetHeight;
    }

    o.style.height = h > -1 ? h : o.offsetHeight;
  }
  return o.offsetHeight;
}


// --------------------------------------------------------------------------
//
// objDim --
//
//      This function is shorthand for
//
//           new Dim(objW(o, w), objH(o, h))
//
//      Specifying w as -1 is equivalent to
//
//           o.style.width = o.offsetWidth
//
//      Specifying h as -1 is equivalent to
//
//           o.style.height = o.offsetHeight
//
// Prototype:
//
//      Dim objDim(Object o[,[Dim d][int w, int h]])
//
// Arguments:
//
//      o ... An HTML Element object
//      d ... An optional Dim object representing w and h
//      w ... An optional width
//      h ... An optional height
//
// Results:
//
//      A Dim object representing object o's rendering width and height.
//      An adjustment is made for Gecko-based browsers because they do not
//      include borders and padding when reporting width and height. This
//      adjustment may not be correctly applied if object o has not been
//      prepped by initXuaObj().
//
// --------------------------------------------------------------------------

function objDim(o) {
  var w, h;
  if (arguments.length == 3) {
    w = arguments[1];
    h = arguments[2];
  } else if (arguments.length == 2) {
    w = arguments[1].w;
    h = arguments[1].h;
  }

  var d = new Dim(objW(o, w), objH(o, h));
  return d;
}


// --------------------------------------------------------------------------
//
// initXuaEvent --
//
//      This function adds XUA methods and properties to event objects.
//
// Prototype:
//
//      Event initXuaEvent(Event e)
//
// Arguments:
//
//      e ... The event object that will be initialized
//
// Results:
//
//      The initialized event object.
//
// --------------------------------------------------------------------------

function initXuaEvent(e) {
  if (! e) return null;

  e.src = null;
  e.pos = null;
  e.btn = null;

  // Let e.src be the literal target of event e.
  if (ie) {
    e.src = e.srcElement;
  } else {
    // XXX Perhaps we should recursively look up the hierarchy for a parent
    // with nodeType 1.
    if (e.target.nodeType != 1) {
      e.src = e.target.parentNode;
    }
    e.src = e.target;
  }

  // Let e.pos be the position of the event on the x and y axes.
  e.pos = new Pos((ie ? e.clientX : e.pageX), (ie ? e.clientY : e.pageY));
  // Adjust MSIE event coordinates for scrolling.
  if (ie && e.pos != null) {
    e.pos.x += viewLeft();
    e.pos.y += viewTop();
  }

  // XXX By the time e makes it's way back to the caller, e.btn is effectively
  // useless. Neither Gecko-based browsers nor MSIE are able to intercept a
  // right-click event.

  // Let e.btn be the mouse button that triggered the event: 1 for left, 2 for
  // right and 3 for middle.
  if (ie) {
    if (e.button == 1 || e.button == 2) {
      e.btn = e.button;
    } else if (e.button == 3 || e.button == 5) {
      e.btn = 3;
    } else {
      e.btn = 1;
    }
  } else {
    if (e.button == 0) {
      e.btn = 1;
    } else if (e.button == 1) {
      e.btn = 3;
    } else if (e.button == 2) {
      e.btn = 2;
    } else {
      e.btn = 1;
    }
  }

  e.stp = function () {
    if (ie) {
      this.cancelBubble = true;
      this.returnValue = false;
    } else {
      this.stopPropagation();
      this.preventDefault();
    }
  };

  return e;
}


// --------------------------------------------------------------------------
//
// eObj --
//
//      This function is shorthand for
//
//         initXuaEvent(event)
//
// Prototype:
//
//      Event initXuaEvent(Event e)
//
// Arguments:
//
//      e ... The event object that will be initialized
//
// Results:
//
//      The initialized event object.
//
// --------------------------------------------------------------------------

function eObj(e) {
  return initXuaEvent(ie && e == null ? self.event : e);
}


// --------------------------------------------------------------------------
//
// lstn --
//
//      Attach event handler f to HTML element o for events of type t.
//
// Prototype:
//
//      bool lstn(Object o, string t, Function f)
//
// Arguments:
//
//      o ... The HTML object that should listen for the event
//      t ... The type of event to listen for
//      f ... A reference to the handler function
//
// Results:
//
//      true if successful, false otherwise.
//
// --------------------------------------------------------------------------

function lstn(o, t, f)
{
  if (o.addEventListener) {
    // Because MSIE only has a bubbling phase, capture will always be false.
    o.addEventListener(t, f, false);
    return true;
  } else if (o.attachEvent){
    return o.attachEvent("on" + t, f);
  }
  return false;
}


// --------------------------------------------------------------------------
//
// ignr --
//
//      Detach event handler f from HTML element o for events of type t.
//
// Prototype:
//
//      bool ignr(Object o, string t, Function f)
//
// Arguments:
//
//      o ... The HTML object that should ignore the event
//      t ... The type of event to ignore
//      f ... A reference to the handler function
//
// Results:
//
//      true if successful, false otherwise.
//
// --------------------------------------------------------------------------

function ignr(o, t, f)
{
  if (o.removeEventListener){
    // Because MSIE only has a bubbling phase, capture will always be false.
    o.removeEventListener(t, f, false);
    return true;
  } else if (o.detachEvent){
    return o.detachEvent("on" + t, f);
  }
  return false;
}


// --------------------------------------------------------------------------
//
// objCss --
//
//      Return the value of CSS property p for HTML element o. If the optional
//      v argument is supplied, CSS property p will be set before being
//      returned.
//
// Prototype:
//
//      string objCss(Object o, string p[, string v])
//
// Arguments:
//
//      o ... An HTML element
//      p ... A CSS property name
//      v ... An optional property value
//
// Results:
//
//      The string value of CSS property p for element o, or null. Note that
//      the value of p may be a null string (i.e., "").
//
// --------------------------------------------------------------------------

function objCss(o, p, v) {
  if (o == null) return null;
  if (v != null) o.style[p] = v;

  // Check the inline style first.
  var s = o.style[p];
  if (s != '' && s != null) {
    return s;
  }

  // Check MSIE's currentStyle property.
  if(o.currentStyle) {
    var s = o.currentStyle[p];
    if (s != '' && s != null) {
      return s;
    }
  }

  var slctrs = ["id", "class"];
  // Do it the hard way using styleSheets.
  for (var i = 0; i < slctrs.length; i++) {
    if (!objAtt(o, slctrs[i])) continue;
    s = probeCss(objAtt(o, slctrs[i]), p, slctrs[i]);
    if(s != '' && s != null) {
      return s;
    }
  }
  return null;

  // probeCss ---------------------------------------------------------------
  //
  // Return the css property p for selector s.
  //
  // s ... a selector string
  // p ... a css property name
  // t ... a css selector type (i.e, class, id or tag)
  function probeCss(s, p, t) {
    if (!s || !p || !document.styleSheets) return null;
    if (t == "class") s = '.' + s;
    if (t == "id") s = '#' + s;

    var css = document.styleSheets;
    if (css.length > 0) {
      // Check each style sheet.
      for (var i = 0; i < css.length; i++) {
        var rules;
        if (css[i].cssRules) {
          rules = css[i].cssRules;
        } else if (css[i].rules) {
          rules = css[i].rules;
        } else {
          return null;
        }

        if (rules.length > 0) {
          // Check each rule.
          for (var j = 0; j < rules.length; j++) {
            var st = rules[j].style;
            if (st[p] != '' && st[p] != null &&
              rules[j].selectorText == s) {
              return st[p];
            }
          }
        }
      }
    }
    return null;
  }
}


// --------------------------------------------------------------------------
//
// objAtt --
//
//      Return the value of HTML attribute a for HTML element o. If the
//      optional v argument is supplied, attribute a will be set before being
//      returned.
//
// Prototype:
//
//      string objCss(Object o, string p[, string v])
//
// Arguments:
//
//      o ... An HTML element
//      a ... An HTML attribute name
//      v ... An optional attribute value
//
// Results:
//
//      The string value, if any, of attribute a for element o.
//
// --------------------------------------------------------------------------

function objAtt(o, a, v) {
  if (! o) return null;

  // Neither gecko-based browsers nor IE can use o.getAttribute if o is an
  // iframe that was added with
  //
  //      doment.body.appendChild(document.createChild("iframe"))
  //
  // Ugh.
  var ga = o.getAttribute;
  if (ga == null && a.toLowerCase() == "scrolling") {
    if (v != null) o.scrolling = v;
    return o.scrolling;
  }

  if (ie) {
    // MSIE will not modify the name attribute as it does other attributes.
    if (a.toLowerCase() == "name") {
      if (v != null) o.name = v;
      return o.name;
    }

    var att;

    if (! o.attributes.getNamedItem) {
      for (var i = 0; o.attributes != null && i < o.attributes.length; i++) {
        if (o.attributes[i].nodeName == a) {
          att = o.attributes[i];
        }
      }
    } else {
      att = o.attributes.getNamedItem(a);
    }

    if (v != null) att.nodeValue = v;
    return att.nodeValue;
  }
  if (v != null) o.setAttribute(a, v);
  return o.getAttribute(a);
}


// --------------------------------------------------------------------------
//
// byAtt --
//
//      Given a list of candidate HTML elements, return the subset that have
//      an attribute a matching regular expression e.
//
// Prototype:
//
//      NodeList objCss(NodeList n, string a, regex e)
//
// Arguments:
//
//      n ... An HTML element
//      a ... A CSS property name
//      e ... An optional property value
//
// Results:
//
//      An array containing the matching nodes, if any.
//
// --------------------------------------------------------------------------

function byAtt(n, a, e) {
  var r = new Array();
  for (var i = 0; i < n.length; i++) {
    if (e.test(objAtt(n[i], a))) {
      r[r.length] = n[i];
    }
  }
  return r;
}


// --------------------------------------------------------------------------
//
// dim --
//
//      This function returns the inner dimensions of the current window. If
//      any of the optional dimension arguments are supplied, the window
//      will be resized before its dimensions are returned.
//
// Prototype:
//
//      Dim dim([[Dim d][int w, int h]])
//
// Arguments:
//
//      d ... An optional Dim object representing w and h
//      w ... An optional width
//      h ... An optional height
//
// Results:
//
//      A Dim object representing the current window's inner width and height.
//
// --------------------------------------------------------------------------

function dim() {
  var d = _dfa(arguments);

  if (d) {
    if (ie) {
      // The resizeBy can throw an exception if the user clicks too fast (PR39015);
      // catch and do nothing since the window is ultimately resized correctly anyway.
      try {
        var ow = document.body.clientWidth;
        var oh = document.body.clientHeight;
        self.resizeBy(d.w - ow, d.h - oh);
      } catch (e) {
        ;
      }
    } else {
      self.innerWidth = d.w;
      self.innerHeight = d.h;
    }
  }

  return new Dim(ie ? document.body.clientWidth : self.innerWidth,
    ie ? document.body.clientHeight : self.innerHeight);
}


// --------------------------------------------------------------------------
//
// pos --
//
//      This function returns the x and y position of the top-left of the
//      current window. If any of the optional position attributes are sup-
//      plied, the window will be moved before its position is returned.
//
// Prototype:
//
//      Pos pos([[Pos p][int x, int y]])
//
// Arguments:
//
//      p ... An optional Pos object representing x and y
//      x ... An optional position on the x axis
//      y ... An optional position on the y axis
//
// Results:
//
//      A Pos object representing the position of the top-left corner of the
//      current window relative to the desktop.
//
// --------------------------------------------------------------------------

function pos() {
  var p = _pfa(arguments);

  if (p) {
    self.moveTo(p.x, p.y);
  }

  return new Pos(ie ? self.screenLeft : self.screenX,
    ie ? self.screenTop : self.screenY);
}


// --------------------------------------------------------------------------
//
// viewLeft --
//
//      This function returns the left-most visible x coordinate. If the page
//      is not scrolled horizontally, this value will be 0.
//
// Prototype:
//
//      int viewLeft()
//
// Results:
//
//      The x coordinate value of the left-most visible column of pixels.
//
// --------------------------------------------------------------------------

function viewLeft() {
  if (self.pageXOffset)
    return self.pageXOffset;

  if (document.body && document.body.scrollLeft)
    return document.body.scrollLeft;

  return 0;
}


// --------------------------------------------------------------------------
//
// viewTop --
//
//      This function returns the top-most visible y coordinate. If the page
//      is not scrolled vertically, this value will be 0.
//
// Prototype:
//
//      int viewLeft()
//
// Results:
//
//      The y coordinate value of the top-most visible row of pixels.
//
// --------------------------------------------------------------------------

function viewTop() {
  if (self.pageYOffset)
    return self.pageYOffset;

  if (document.body && document.body.scrollTop)
    return document.body.scrollTop;

  return 0;
}


// --------------------------------------------------------------------------
//
// dh (document height) --
//
//      Return the total height of the current document.
//
// Prototype:
//
//      int dh()
//
// Results:
//
//      The height of the current document, adjusted for scrolling content, if
//      necessary.
//
// --------------------------------------------------------------------------

function dh() {
   // Make an adjustment to documents in scrolling frames so that the
   // scrollbars are only displayed when necessary.
   var a = 8; // XXX This should be 2. We need a first ele var in each frame.
   if (self != self.parent &&
      objAtt(parent.frames[self.name], "scrolling") == "no") a = 0;

   if (ie) return document.body.scrollHeight + a;

   // Gecko-based browsers do not report the height including document margins
   // so we need to make an educated guess. Look for the first visible element
   // and assume it's y position is equal to the top and bottom document
   // margins.
   var r = document.height;
   if (document.body.childNodes.length > 0) {
     for (var i = 0; i < document.body.childNodes.length; i++) {
       if (document.body.childNodes[i].nodeType == 1 &&
         objCss(document.body.childNodes[i], "display") != "none") {
         r += 2 * (absPos(document.body.childNodes[i]).y);
         break;
       }
     }
   }
   return r + a;
}


// --------------------------------------------------------------------------
//
// _ifwin --
//
//      This function is intended to be an object member method. As such it
//      operates on the implicit object this. _ifwin returns the this object's
//      contentWindow object, if one exists; null otherwise.
//
// Prototype:
//
//      Object _ifwin()
//
// Results:
//
//      If the this object is an iframe, its contentWindow; null otherwise.
//
// --------------------------------------------------------------------------

function _ifwin() {
  if (this.tagName.toLowerCase() != "iframe") return null;
  if (this.contentWindow) return this.contentWindow;
  return this;
}


// --------------------------------------------------------------------------
//
// _ifdoc --
//
//      This function is intended to be an object member method. As such it
//      operates on the implicit object this. _ifdoc returns the this object's
//      contentDocument object, if one exists; null otherwise.
//
// Prototype:
//
//      Object _ifdoc()
//
// Results:
//
//      If the this object is an iframe, its contentDocument; null otherwise.
//
// --------------------------------------------------------------------------

function _ifdoc() {
  if (this.tagName.toLowerCase() != "iframe") return null;
  if (this.contentWindow) return this.contentWindow.document;
  if (this.contentDocument) return this.contentDocument;
  return this.document;
}


// ------------------------------------------------------------------------
//
// _xuaKit --
//
//      This function is intended to be an object member method. As such it
//      operates on the implicit object this. _xuaKit returns style property p
//      of the this object's XUA style kit k. If value v is specified, property
//      p will be modified before returning; if kit k does not exist and v is
//      specified, kit k will be created automatically.
//
// Prototype:
//
//      string _xuaKit(string k, string p, string v)
//
// Arguments:
//
//      k ... The XUA style kit to modify or inspect
//      p ... The style property to modify or inspect
//      v ... The value used to modify the XuaStyle kit
//
// Results:
//
//      The current value of property p, or null if kit k or property p does
//      not exist.
//
// ------------------------------------------------------------------------

function _xuaKit(k, p, v) {
  if (this._kits[k] == null) {
    if (v == null) return;
    this._kits[k] = new Object();
  }

  if (v != null && v != this._kits[k][p]) {
    if (v == "") {
      delete this._kits[k][p];
    } else {
      this._kits[k][p] = v;
    }
  }

  return this._kits[k][p];
}


// ------------------------------------------------------------------------
//
// _useKit --
//
//      This function is intended to be an object member method. As such it
//      operates on the implicit object this. _useKit applies the style
//      properties found in this._kits[k] to the this HTML element.
//
// Prototype:
//
//      string _useKit(string k)
//
// Arguments:
//
//      k ... The name of the kit to use
//
// Results:
//
//      The name of the current kit, or null if no kit has been used.
//
// ------------------------------------------------------------------------

function _useKit(k) {
  if (k != null && k != "" && k != this._kit) {
    this._kit = k;
    for (p in this._kits[k]) {
      objCss(this, p, this._kits[k][p]);
    }
  }

  return this._kit;
}


// --------------------------------------------------------------------------
//
// initXuaStyle --
//
//      Add useful style properties and methods to an HTML element.
//
// Prototype:
//
//      void XuaStyle(Object o)
//
// Arguments:
//
//      o ... An HTML element object
//
// Results:
//
//      HTML element object o with new properties and methods.
//
// --------------------------------------------------------------------------

function initXuaStyle(o) {
  o._kits = new Object(); // Available style kits
  o._kit; // Current style kit
  o.xuaKit = _xuaKit;
  o.useKit = _useKit;

  return o;
}
