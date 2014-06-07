
function toggleSelection(chkbxClicked,multiSelect){
  if(chkbxClicked.checked) {
    window.parent.cntrlMdlVms.selectVms(chkbxClicked.id,window.name,multiSelect);
  } else {
    window.parent.cntrlMdlVms.deselectVms(chkbxClicked.id,window.name);
  }
}
