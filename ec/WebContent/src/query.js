/* Copyright 2003 VMware, Inc.  All rights reserved. -- VMware Confidential */

// query.js --
//
//      Implements a general purpose object-oriented interface for URL and
//      HTML form data.


// --------------------------------------------------------------------------
//
// Query --
//
//      This is a constructor for objects that encapsulate the contents of
//      an HTML form or URL search string.
//
// Prototype:
//
//      Object new Query([string|Object] q)
//
// Arguments:
//
//      q ... Either a form object or a URL search string
//
// Results:
//
//      A Query object.
//
// --------------------------------------------------------------------------

function Query(q) {
  this.args = [];

  if (q && typeof q == "object") {
    // Assume that q is a Form object.
    this.f = q;

    // Get/set wrapper for form.action property
    this.action = function (v) {
      if (v != null) this.f.action = v;
      return this.f.action;
    };

    // Get/set wrapper for form.method property
    this.method = function (v) {
      if (v != null && v.toLowerCase() == "get" || v.toLowerCase() == "post") {
        this.f.method = v;
      }
      return this.f.method;
    };

    // Get wrapper for form.name property
    this.name = function () { return this.f.name; };

    // Get/set wrapper for form.target property
    this.target = function (v) {
      if (v != null) this.f.target = v;
      return this.f.target;
    };

    // exec wrapper for form.reset method
    this.reset = function () {
      return this.f.reset();
    };

    // exec wrapper for form.submit method
    this.submit = function () {
      return this.f.submit();
    };


    // ----------------------------------------------------------------------
    //
    // .list --
    //
    //      Collect indexes for form elements with a common name in an array
    //      and return it.
    //
    // Prototype:
    //
    //      int[] Query.list(string n)
    //
    // Arguments:
    //
    //      n ... The name for which to search
    //
    // Results:
    //
    //      An array of matching indexes in form.elements. The array length
    //      will be zero if no elements named n were found.
    //
    // ----------------------------------------------------------------------

    this.list = function (n) {
      if (this.f == null) return [];

      var a = [];
      for (var i = 0; i < this.f.length; i++) {
        if (this.f[i].name == n) a.push(i);
      }
      return a;
    };


    // ----------------------------------------------------------------------
    //
    // .select --
    //
    //      Hunt through the complete list of form elements with name n. If
    //      an element's value matches v, then select/check that element and
    //      return the selected/checked state. If b is supplied, set the
    //      selected/checked state of the element to b (true or false) first.
    //      If i is supplied, only inspect the ith element named n.
    //
    // Prototype:
    //
    //      boolean Query.select(string n, string v, boolean b, int i)
    //
    // Arguments:
    //
    //      n ... The name for which to search
    //      v ... The value to match
    //      b ... Whether or not the element should be selected
    //      i ... A position in the list of elements named n
    //
    // Results:
    //
    //      A boolean indicating whether or not the element in question is
    //      selected. Or null, if such a determination cannot be made.
    //
    //      .select only acts on the first element matching value v.
    //
    // ----------------------------------------------------------------------

    this.select = function (n, v, b, i) {
      var l = this.list(n);
      if (l.length == 0) return null;

      // Only examining the ith element in a group of radio buttons or check
      // boxes doesn't make any sense. Ignore i.
      if (this.f[l[0]].type == "radio" || this.f[l[0]].type == "checkbox") {
        i = null;
      }

      var lmt = i == null ? l.length : i + 1;

      if (i == null) i = 0;

      for (i; i < lmt; i++) {
        if (this.f[l[i]].type.indexOf("select") > -1) {
          // Iterate over a select element's options.
          for (var j = 0; j < this.f[l[i]].options.length; j++) {
            // Found match in the select element's option list.
            if (this.f[l[i]].options[j].value == v) {
              // Toggle the selection state of the matching option.
              if (b != null) {
                this.f[l[i]].options[j].selected = b;
                // Update this.args[n] since its value(s) may have changed.
                this.cache(n);
              }
              // Only return for the first matching element.
              return this.f[l[i]].options[j].selected;
            }
          }
        } else if (this.f[l[i]].type == "radio" || this.f[l[i]].type == "checkbox") {
          // Found match in checkbox/radio button group.
          if (this.f[l[i]].value == v) {
            if (b != null) {
              // Toggle the selection state of the matching option.
              this.f[l[i]].checked = b;
              // Update this.args[n] since its value(s) may have changed.
              this.cache(n);
            }
            // Only return for the first matching element.
            return this.f[l[i]].checked;
          }
        }
      }
      return null; // The named element/value does not exist or is not selectable.
    };


    // ----------------------------------------------------------------------
    //
    // .cache --
    //
    //      Generate the correct array of values for argument n based on the
    //      current contents of form this.f. Call this method each time the
    //      form is updated if you want the query object to mirror the form.
    //      Call this method with no args to cache the entire form.
    //
    // Prototype:
    //
    //      string[] Query.cache(string n)
    //      string[] Query.cache()
    //
    // Arguments:
    //
    //      n ... The name to search for
    //
    // Results:
    //
    //      An array of values. The array length will be 0 if no elements
    //      named n were found. The array in question replaces the list of
    //      values stored in args[n], which may lead to inconsistencies if
    //      the form contains elements that share a name (e.g., checkboxes)
    //      since args[n] represents the value(s) that will be submitted.
    //      This is probably not a problem in practice since any manipulation
    //      of argument values should happen via the .arg or .select
    //      methods, which correctly handle elements that might share a name.
    //
    //      If n is not provided, the array returned will be equivalent to the
    //      array returned by .arg (defined below); that is, the array will
    //      contain the names of all the args in this.
    //
    // ----------------------------------------------------------------------

    this.cache = function (n) {
      // Pick up new form elements, if any.
      for (var i = 0; i < this.f.length; i++) {
        if (this.args[this.f[i].name] == null) {
          this.args[this.f[i].name] = this.icache(this.f[i].name);
        }
      }

      if (n == null) {
        var a = this.arg();
        for (var i = 0; i < a.length; i++) {
          this.icache(a[i]);
        }

        return a;
      }

      return this.icache(n);
    };

    this.icache = function (n) {
      var l = this.list(n);
      var a = [];
      if (l.length == 0) return a;

      for (var i = 0; i < l.length; i++) {
        if (this.f[l[i]].type.indexOf("select") > -1) {
          for (var j = 0; j < this.f[l[i]].options.length; j++) {
            // Only add the values of selected options to the list.
            if (this.f[l[i]].options[j].selected) {
              a.push(this.f[l[i]].options[j].value);
            }
          }
        } else if (this.f[l[i]].type == "radio" || this.f[l[i]].type == "checkbox") {
          // Only add the values of selected options to the list.
          if (this.f[l[i]].checked) {
            a.push(this.f[l[i]].value);
          }
        } else {
          a.push(this.f[l[i]].value);
        }
      }
      // Update this.args[n] to reflect the current state of the world.
      this.args[n] = a;
      return a;
    };


    // ----------------------------------------------------------------------
    //
    // .fill --
    //
    //      Set the contents of form this.f to refelect the array of values
    //      for argument n. Call this method with no args to fill the entire
    //      form.
    //
    // Prototype:
    //
    //      string[] Query.fill(string n)
    //      string[] Query.fill()
    //
    // Arguments:
    //
    //      n ... The name to search for
    //
    // Results:
    //
    //      An array of values. The returned array length may not match the
    //      length of this.args[n]. The array length will be 0 if no elements
    //      named n were found.
    //
    //      If n is not provided, the array returned will be equivalent to the
    //      array returned by .arg (defined below); that is, the array will
    //      contain the names of all the args in this.
    //
    // ----------------------------------------------------------------------

    this.dump =
    this.fill = function (n) {
      if (n == null) {
        var a = this.arg();
        for (var i = 0; i < a.length; i++) {
          this.ifill(a[i]);
        }

        return a;
      }

      return this.ifill(n);
    };

    this.ifill = function (n) {
      var l = this.list(n);
      if (l.length == 0) return [];
      var v = this.arg(n);
      var a = [];

      for (var i = 0; i < l.length; i++) {
        if (this.f[l[0]].type.indexOf("select") > -1) {
          for (var j = 0; j < this.f[l[i]].options.length; j++) {
            // Only select the options with values in v.
            this.f[l[i]].options[j].selected = false;
            for (var k = 0; k < v.length; k++) {
              if (this.f[l[i]].options[j].value == v[k]) {
                this.f[l[i]].options[j].selected = true;
                a.push(v[k]);
                break;
              }
            }
          }
        } else if (this.f[l[0]].type == "radio" || this.f[l[0]].type == "checkbox") {
          // Only select the options with values in v.
          this.f[l[i]].checked = false;
          // XXX: Do we really need to loop, or will this.f[l[i]] always correspond to
          // v[k]?
          for (var k = 0; k < v.length; k++) {
            if (this.f[l[i]].value == v[k]) {
              this.f[l[i]].checked = true;
              a.push(v[k]);
              break;
            }
          }
        } else {
          this.f[l[i]].value = v[i];
          a.push(v[i]);
        }
      }
      // Update this.args[n] to reflect the current state of the world.
      return v;
    };


    // Initialize the Query object using the supplied form.
    for (var i = 0; i < q.length; i++) {
      if (this.args[q[i].name] == null) {
        this.args[q[i].name] = this.cache(q[i].name);
      }
    }
  } else {
    // q is not a form. That's OK.
    this._action = "";
    this._target = self;

    // Get/set wrapper for a fake form.action property
    this.action = function (v) {
      if (v != null) this._action = v;
      return this._action;
    };

    // Get/set wrapper for a fake form.target property
    this.target = function (v) {
      if (v != null) this._target = v;
      return this._target;
    };

    // Load query URL into target. Specify boolean r to replace location.
    this.submit = function (r) {
      if (! this.target || this.target == null) {
        alert("Query.submit: No target specified");
        return false;
      }

      if (r != null && r) {
        this._target.location.replace(this.toString());
      } else {
        this._target.location.href = this.toString();
      }
      return true;
    };

    if (q) {
      // Assume that q is a window.location.search string.
      q = q.substr(1).split("&");
      for (var i = 0; i < q.length; i++) {
        var r = q[i].split("=");
        if (r.length > 1 && r[1] != null && r[1] != "") {
          if (r[1].indexOf(",") > -1) {
            this.args[r[0]] = r[1].split(",");
            for (var j = 0; j < this.args[r[0]].length; j++) {
              this.args[r[0]][j] = unescape(this.args[r[0]][j]);
            }
          } else {
            this.args[r[0]] = [unescape(r[1])];
          }
        } else {
          this.args[r[0]] = [""];
        }
      }
    }
  }


  // ----------------------------------------------------------------------
  //
  // .arg --
  //
  //      This is a general purpose utility for inspecting and modifying
  //      the list of arguments known to a Query object. If no arguments are
  //      supplied, .arg returns a list of the known argument names. If n
  //      is supplied, then .arg returns an array of values for the named
  //      argument. If v is supplied, one of argument n's values will be
  //      modified before its list of values is returned. The first value in
  //      argument n's list is changed unless i is specified. If i is sup-
  //      plied, n's ith value will be modified.
  //
  // Prototype:
  //
  //      string[] Query.arg(string n, string v, int i)
  //
  // Arguments:
  //
  //      n ... The name to search for
  //      v ... The modified value
  //      i ... A position in the list of elements named n
  //
  // Results:
  //
  //      An array of values. The array length will be zero if no arguments
  //      named n exist. If n is not supplied, the array will contain the
  //      names of all the args in this.
  //
  // ----------------------------------------------------------------------

  this.arg = function (n, v, i) {
    if (n == null) {
      var a = [];
      for (var n in this.args) {
        a.push(n);
      }
      return a;
    }

    if (i == null) { i = 0; }

    var l = this.f != null ? this.list(n) : null;

    // If v is defined, make sure that the query is either not backed by a form
    // or that the backing form contains the named arg.
    if (v != null && (l == null || this.f[l[0]] != null)) {
      if (this.args[n] == null) {
        this.args[n] = [];
      }

      if (this.f != null) {
        if (l.length < i + 1) { throw "Query.arg: No such element: " + n + "."; }
        if (this.f[l[i]].type.indexOf("select") > -1 ||
          this.f[l[i]].type == "radio" ||
          this.f[l[i]].type == "checkbox") {
          // Select or check the named option with the specified value.
          this.select(n, v, true, i);
        } else {
          if (l.length > 0 && this.f[l[i]] != null) {
            this.f[l[i]].value = v;
            this.args[n][i] = v;
          } else {
            this.args[n][i] = v;
          }
        }
      } else {
        if (typeof v == "object") {
          // Assume v is an array.
          this.args[n] = v;
        } else {
          this.args[n][i] = v;
        }
      }
    }

    // Make sure that the query is either not backed by a form or that the
    // backing form contains the named arg.
    if (l == null || this.f[l[0]] != null) {
      return this.args[n] != null ? this.args[n] : [];
    }

    return [];
  };


  // ----------------------------------------------------------------------
  //
  // .toString --
  //
  //      Returns the query object as a properly formatted and escaped URL
  //      search string.
  //
  // Prototype:
  //
  //      string Query.toString()
  //
  // Arguments:
  //
  // Results:
  //
  //      An array of values. The return value will be null if no arguments
  //      named n exist.
  //
  // ----------------------------------------------------------------------

  this.toString = function () {
    var s = "";
    for (var n in this.args) {
      var a = this.args[n];
      var e = "";
      for (var i = 0; i < a.length; i++) { e += (e ? "," : "") + esc(a[i]); }

      if (e != "") { s += (s ? "&" : "?") + n + "=" + e; }
    }
    return this.action() + s;
  };


  // ----------------------------------------------------------------------
  //
  // .diff --
  //
  //      Given another query object, report whther or not it is different
  //      than the current query object. Useful if you create a query object
  //      immeadiately after the page is loaded and the form is initialized,
  //      and then again just before the form is submitted. If the objects,
  //      do not differ, the form doesn't need to be submitted.
  //
  // Prototype:
  //
  //      boolean Query.diff(Object o)
  //
  // Arguments:
  //
  //      o ... Another Query object
  //
  // Results:
  //
  //      A boolean: true if the objects differ, false otherwise.
  //
  // ----------------------------------------------------------------------

  this.diff = function (o) {
    if (o == null) return true;

    // this and o are not the same type of object.
    if (this.constructor != o.constructor) return true;

    // Compare each arg in this to o. Then compare each arg in o to this.
    // The intent is to ensure that args that exist in o but not in this are
    // not overlooked. Maybe there is a more efficient algorithm, but this one
    // is easy to prove.
    var d = [this, o];
    for (var x = 0; x < d.length; x++) {
      var y = x == 0 ? d[0] : d[1];
      var z = x == 0 ? d[1] : d[0];

      for (var n in y.args) {
        // y has an arg that z lacks.
        if (z.args[n] == null) return true;

        // y.args[n] has a value that z.args[n] lacks.
        if (y.args[n].length != z.args[n].length) return true;

        // Copy and sort the values of each object's n argument. Don't sort
        // the arguments themselves since their order may be significant,
        // especially if the objects encapsulate URL search strings.
        var a = new Array();
        for (var i = 0; i < y.args[n].length; i++) {
          a[i] = y.args[n][i];
        }
        a.sort();

        var b = new Array();
        for (var i = 0; i < z.args[n].length; i++) {
          b[i] = z.args[n][i];
        }
        b.sort();

        // Compare each value in the sorted lists.
        for (var i = 0; i < a.length; i++) {
          // A value in list a does not match the value in list b.
          if (a[i] != b[i]) return true;
        }
      }
    }
    return false;
  };


  // ----------------------------------------------------------------------
  //
  // .sync --
  //
  //      Given another query object, make sure that this query object has
  //      all of the args and arg values that the supplied object does. If d
  //      is supplied and true, then this object should be a duplicate of the
  //      supplied object.
  //
  // Prototype:
  //
  //      boolean Query.sync(Object o, boolean d)
  //
  // Arguments:
  //
  //      o ... Another Query object
  //      d ... Whether or not the objects should be duplicates of one another
  //
  // Results:
  //
  //      A boolean: false if the objects differ at the end of the procedure,
  //      true otherwise.
  //
  // ----------------------------------------------------------------------

  this.sync = function (o, d) {
    if (o == null) return false;
    if (d == null) d = false;

    // this and o are not the same type of object.
    if (this.constructor != o.constructor) return false;

    // Make sure that this and o are identical for all args that o has.
    for (var n in o.args) {
      // Make sure that this.args[n] is equal to o.args[n], value-for-value.
      this.args[n] = [];
      for (var i = 0; i < o.args[n].length; i++) {
        this.args[n][i] = o.args[n][i];
      }
    }

    // Make sure that this and o are completely identical by deleting any arg
    // in this that o lacks.
    if (d) {
      for (var n in this.args) {
        // this has an arg that o lacks.
        if (o.args[n] == null) {
          var v = this.arg(n);
          for (var i; i < this.args[n].length; i++) {
            // Make sure that the form, if one exists, is accurate.
            if (this.f != null || this.select(n, v[i], false) == null) {
              this.arg(n, "", i);
            }
          }

          this.args[n] = null;
        }
      }
    }

    // Make sure that the form, if one exists, is accurate.
    if (this.f != null) {
      this.fill();
    }

    return ! this.diff(o);
  };
}
