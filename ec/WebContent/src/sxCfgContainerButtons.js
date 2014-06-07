function initSlctBtns() {
  var dom = {};
  dom.std = obj("std");
  dom.wiz = obj("wiz");
  dom.wizNoBack = obj("wizNoBack");
  dom.wizNoNext = obj("wizNoNext");
  dom.cls = obj("cls");

  slctBtns = function (s) {
    for (var n in dom) {
      dom[n].css("display", "none");
    }
    dom[s].css("display", "");
  };
}

function initPage() {
  initSlctBtns();
}
