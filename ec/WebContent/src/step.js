/* Copyright 2003 VMware, Inc.  All rights reserved. -- VMware Confidential */

// step.js --
//
//      Implements a general purpose object-oriented interface for structuring
//      workflow into discrete steps.


// --------------------------------------------------------------------------
//
// Step --
//
//      This is a constructor for objects that encapsulate the workflow re-
//      quired to complete a task. Step objects are intended to be linked to
//      each other through their .prv and .nxt[] properties. The .prv property
//      is set each time that .slct() is called with a value greater than -1;
//      in that case, .prv in the selected Step will point to the Step from
//      which it was selected.
//
// Prototype:
//
//      Object new Step(string name, Object ele)
//
// Arguments:
//
//      n ... The name of this step (so far for debugging purposes only)
//      o ... The HTML element responsible for the step's presentation
//
// Results:
//
//      A Step object.
//
// --------------------------------------------------------------------------

function Step(n, o) {
  this.n = n;      // Name of step (e.g., for debugging)
  this.o = o;      // Step presentation element
  this.prv = null; // Step previous to this one (see .slct(), below)
  this.nxt = [];   // Possible next Steps
  this.nxtIdx = 0; // Current next Step index (in .nxt[])

  // Move back and forth through the workflow for a task.
  this.slct = function (i) {
    // Use the current next index if none is specified.
    if (i == null) i = this.nxtIdx;

    if (i == -1) {
      // Hide the current presentation element, if one exists.
      if (this.o != null) { this.o.css("display", "none"); }
      // Return to the previous step, if one exists. Otherwise, exit the
      // current task.
      if (this.prv != null) {
        // Show the previous step's presentation.
        if (this.prv.o != null) { this.prv.o.css("display", ""); }
        // Return a reference to the previous step.
        return this.prv;
      } else {
        exit(-1);
      }
    } else {
      // Move to the chosen next step, if one exists. Otherwise, exit the
      // current task.
      if (this.nxt.length > 0 && i < this.nxt.length && this.nxt[i] != null) {
        // The current step will be the previous step for the chosen next step.
        this.nxt[i].prv = this;
        // Process anything of interest before continuing, bailing if the op fails.
        if (this.exec != null) {
          var ret = this.exec(i);
          if ((ret != null) && (ret == false)) {
            return this;
          }
        }
        // If the next step has a presentation element, hide the current one.
        if (this.o != null && this.nxt[i].o != null) {
          this.o.css("display", "none");
        }
        // Show the next step's presentation element, if one exists.
        if (this.nxt[i].o != null) { this.nxt[i].o.css("display", ""); }
        // Return a reference to the chosen next step.
        return this.nxt[i];
      } else {
        exit(1);
      }
    }
    return null;
  };
}
