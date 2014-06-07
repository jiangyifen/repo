var list, form, btns, vr, lcol, rcol;

// An accounting object for the current top-level element (e.g., a context
// menu or control palette). This object will be discovered from init.js using
// initTle().
var tle = new Object();

function resize() {
  var w1 = parseInt(self.dim().w / 2);
  var w2 = parseInt(self.dim().w / 2) + (self.dim().w % 2);
  
  var bh = btns.contentWindow.dh();
  var lh = list.contentWindow.dh();
  var fh = form.contentWindow.dh();

  btns.dim(self.dim().w, bh);
  list.dim(w1, self.dim().h - btns.dim().h);
  form.dim(w2, self.dim().h - btns.dim().h);
  
  list.pos(0, 0);
  form.pos(list.dim().w, 0);
  btns.pos(0, list.dim().h);
  
  if (lh <= list.dim().h) {
    list.setAttribute("scrolling", "no");
    rcol = form.contentWindow.obj("bodyObj");
    rcol.att("class", "rcolPlain");
    lcol = list.contentWindow.obj("lcol");
    lcol.att("class", "lcol");
    vr = list.contentWindow.obj("vr");
    vr.css("display", "");
  } else {
    list.setAttribute("scrolling", "yes");
    vr = list.contentWindow.obj("vr");
    vr.css("display", "none");
    rcol = form.contentWindow.obj("bodyObj");
    rcol.att("class", "rcol");
    lcol = list.contentWindow.obj("lcol");
    lcol.att("class", "lcolPlain");
  }
  
  if (fh <= form.dim().h) {
    form.setAttribute("scrolling", "no");
  } else {
    form.setAttribute("scrolling", "yes");
  }
}

function initPage() {
  list = obj("list");
  form = obj("form");
  btns = obj("btns");

  dh = function () {
    var bh = btns.contentWindow.dh();
    var lh = list.contentWindow.dh();
    var fh = form.contentWindow.dh();
  
    return bh + (lh > fh ? lh : fh) +2; // +2 eliminates scrollbar, if possible.
  };
  
  resize();
  lstn(self, "resize", resize);
  if (parent.adjustSize) parent.adjustSize();
}
