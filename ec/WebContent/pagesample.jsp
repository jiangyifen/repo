<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html> 
<head> 
<title></title>
<script src="src/init.js"></script> 
<script src="src/xuaLib.js"></script> 
<script src="src/util.js"></script> 
<script src="src/vmCtrlBar.js"></script> 
<script src="src/vmCtxMenu.js"></script> 
<script src="src/sxMonitor.js"></script> 
<link rel="stylesheet" href="css/usage.css" type="text/css" /> 
<link rel="stylesheet" href="css/page.css" type="text/css" /> 
<script language="javascript">

</script>
</head> 
 
<body id="bodyObj"> 
 
<div id="sxStatHdr" class="sctHdr"> 
  <table border="0" cellpadding="0" cellspacing="0"> 
    <tr> 
      <td width="100%">System Summary</td> 
      <td nowrap><div id="sxStatPeriod">1 Minute Average</div></td> 
    </tr> 
  </table> 
</div> 
 
<div id="sxStat" class="lst"> 
  <table border="0" cellpadding="0" cellspacing="0"> 
    <tr class="hdr"> 
      <td id="processors" width="33%" nowrap>Processors</td> 
      <td id="memory" width="33%" nowrap>Memory</td> 
      <td id="memory" width="33%" nowrap>IO</td> 
    </tr> 
    <tr class="statSummary"> 
      <td class="left"><table class="statSummary" border="0" cellpadding="0" cellspacing="0"> 
        <tr> 
          <td class="lbl" width="100%" nowrap>CTI Server</td> 
          <td id="vmCpu" class="val" align="right" nowrap>&nbsp;</td> 
          <td id="vmCpuBar" class="bar" nowrap><img class="usgFill050" src="imx/spacer.gif" alt=""><img class="usgFill050" src="imx/spacer.gif" alt=""><img class="usgFill050" src="imx/spacer.gif" alt=""><img class="usgFill050" src="imx/spacer.gif" alt=""><img class="usgFill050" src="imx/spacer.gif" alt=""><img class="usgFill050" src="imx/spacer.gif" alt=""><img class="usgFill050" src="imx/spacer.gif" alt=""><img class="usgFill080" src="imx/spacer.gif" alt=""><img class="usgFill080" src="imx/spacer.gif" alt=""><img class="usgFill080" src="imx/spacer.gif" alt=""><img class="usgFill080" src="imx/spacer.gif" alt=""><img class="usgFill100" src="imx/spacer.gif" alt=""><img class="usgFill100" src="imx/spacer.gif" alt=""><img class="usgFill100" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""></td> 
        </tr> 
        <tr> 
          <td class="lbl" width="100%" nowrap>Other</td> 
          <td id="osCpu" class="val" align="right" nowrap>&nbsp;</td> 
          <td id="osCpuBar" class="bar" nowrap><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""></td> 
        </tr> 
        <tr class="total"> 
          <td class="lbl" width="100%" nowrap>System Total</td> 
          <td id="sxCpu" class="val" align="right" nowrap>&nbsp;</td> 
          <td id="sxCpuBar" class="bar" valign="bottom" nowrap><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""></td> 
        </tr> 
      </table></td> 
      <td><table class="statSummary" border="0" cellpadding="0" cellspacing="0"> 
        <tr> 
          <td class="lbl" width="100%" nowrap>CTI Server</td> 
          <td id="vmRam" class="val" align="right" nowrap>&nbsp;</td> 
          <td id="vmRamBar" class="bar" nowrap><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""></td> 
        </tr> 
        <tr> 
          <td class="lbl" width="100%" nowrap>Other</td> 
          <td id="osRam" class="val" align="right" nowrap>&nbsp;</td> 
          <td id="osRamBar" class="bar" nowrap><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""></td> 
        </tr> 
        <tr class="total"> 
          <td class="lbl" width="100%" nowrap>System Total</td> 
          <td id="sxRam" class="val" align="right" nowrap>&nbsp;</td> 
          <td id="sxRamBar" class="bar" nowrap><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""></td> 
        </tr> 
      </table></td>
      <td><table class="statSummary" border="0" cellpadding="0" cellspacing="0"> 
        <tr> 
          <td class="lbl" width="100%" nowrap>CTI Server</td> 
          <td id="vmRam" class="val" align="right" nowrap>&nbsp;</td> 
          <td id="vmRamBar" class="bar" nowrap><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""></td> 
        </tr> 
        <tr> 
          <td class="lbl" width="100%" nowrap>Other</td> 
          <td id="osRam" class="val" align="right" nowrap>&nbsp;</td> 
          <td id="osRamBar" class="bar" nowrap><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""></td> 
        </tr> 
        <tr class="total"> 
          <td class="lbl" width="100%" nowrap>System Total</td> 
          <td id="sxRam" class="val" align="right" nowrap>&nbsp;</td> 
          <td id="sxRamBar" class="bar" nowrap><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""></td> 
        </tr> 
      </table></td> 
    </tr> 
  </table> 
</div> 
 
<div class="sctHdr"> 
  <table border="0" cellpadding="0" cellspacing="0"> 
    <tr> 
      <td>用户(<span id="vmCount">&nbsp;</span>)</td> 
    </tr> 
  </table> 
</div> 
 
<div id="vmStat" class="lst"> 
  <table border="0" cellpadding="0" cellspacing="0"> 
    <tr class="hdr"> 
      <td class="left" colspan="2">&nbsp;</td> 
      <td>&nbsp;</td> 
      <td nowrap>HB</td> 
      <td class="dn" width="100%" nowrap>Display Name</td> 
      <td nowrap>Up</td> 
      <td nowrap>No.</td> 
      <td nowrap>%</td> 
      <td align="left" nowrap style="border-left:none;text-align:left;">CPU</td> 
      <td nowrap>RAM</td> 
    </tr> 

    <tr id="noVms" style="display:none;">
        <td id="noVmsMsg" colspan="10">No VMS!</td>
    </tr>
    
    <tr id="123" class="vms">
        <td class="rc" nowrap  title="attach a console">
            <div>
                <img src="imx/linux.png" alt="" title="attach a console">
            </div>
        </td>
        <td class="ctx" nowrap title="attach a menu" onMouseOver="this.className='ctxHvr';" onMouseOut="this.className='ctx';" onClick="this.className='ctx';">
            <div>
                <a><img src="imx/ctxArrow.gif" alt="" title="attach a console"></a>
            </div>
        </td>
        <td class="es" nowrap title="attach a console">
            <div>
                <img src="imx/power_on.gif" alt="" title="attach a power">
            </div>
        </td>
        <td class="hb" nowrap title="heart beat is not available">
            <img class="hbGood" src="imx/spacer.gif" alt="" title="heart beat is not available"><br/><img class="hbBad" src="imx/spacer.gif" alt="" title="heart beat is not available"><br/><img class="hbUgly" src="imx/spacer.gif" alt="" title="heart beat is not available"><br/><img class="hbSlice" src="imx/spacer.gif" alt="" title="heart beat is not available"><br/>            
        </td>
        <td class="dn">
            <div>
                <a href="/">hostname</a>
            </div>
            <div class="vmGuestState">adf | jsijsdifj | fsfsdf</div>
        </td>
        <td class="up" nowrap>&nbsp;</td>
        <td class="vcpu" nowrap>&nbsp;</td>
        <td class="cpu" nowrap>&nbsp;</td>
        <td class="cpuBar" nowrap><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice050" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice080" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""><img class="usgSlice100" src="imx/spacer.gif" alt=""></td>
        <td class="ram" nowrap>&nbsp;</td>
    </tr>

  </table> 
</div> 
 

 
 <div class="lst">
    <table border="0" cellpadding="0" cellspacing="0">
                <tr class="hdr">
                    <td nowrap>主叫号码</td>
                    <td nowrap>被叫号码</td>
                    <td nowrap>呼叫状态</td>
                    <td nowrap>呼叫时长</td>
                </tr>
                <s:iterator value="statusEvents">
                    <tr class="cdr">
                        <td class="dn" nowrap><s:property value="callerIdNum"/></td>
                        <td class="dn" nowrap><s:property value="extension"/></td>
                        <td class="dn" nowrap><s:property value="state"/></td>
                        <td class="dn" nowrap><s:property value="seconds"/></td>
                    </tr>
                </s:iterator> 
    </table> 
 </div>
 
 
 
 
 <table class="cntnr" width="100%" border="0" cellpadding="0" cellspacing="0"> 
  <tr> 
    <td width="100%"><div class="note">Download VMware Server Console:
      <a href="/vmware/bin/VMware-console-1.0.7-108231.exe">Windows</a> 
      <span class="note">(exe)</span> |
      <a href="/vmware/bin/VMware-server-console-1.0.7-108231.i386.rpm">Linux</a> 
      <span class="note">(rpm)</span> |
      <a href="/vmware/bin/VMware-server-console-1.0.7-108231.tar.gz">Linux</a> 
      <span class="note">(tar.gz)</span></div> 
    </td> 
  </tr> 
</table> 
 
<br /> 
 
<jsp:include page="/jsp/about.jsp" flush="true"/>
 
</body> 
</html> 




