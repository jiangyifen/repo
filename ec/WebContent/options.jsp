<%@page language="java" contentType="text/html; charset=utf-8"%>
<html>
<head>
<script src="src/init.js"></script>
<script src="src/xuaLib.js"></script>
<script src="src/util.js"></script>
<script src="src/query.js"></script>
<script src="src/sxOptions.js"></script>
<link rel="stylesheet" href="css/usage.css" type="text/css" />
<link rel="stylesheet" href="css/page.css" type="text/css" />

<script language="javascript">
function popWindow(page) {
    window.open(page, "", "scrollbars=yes,width=560,height=750,resizable");
}
</script>

</head>

<body id="bodyObj">

<div class="cfg">
  <table border="0" cellpadding="0" cellspacing="0">
    <tr class="hdr">
      <td colspan="3">系统管理</td>
    </tr>
    <tr valign="top">
    <!-- 左侧 -->
      <td width="50%" class="lcol">
        <table id="remDev" border="0" cellpadding="0" cellspacing="0">
        
          <tr id="devRow" class="cat">
            <td width="1%" valign="bottom" nowrap><img src="imx/user.png" /></td>
            <td nowrap><div><a href="javascript:" onClick="popWindow('usersGet.action')">用户管理</a></div></td>
          </tr>
          <tr id="keyRow">
            <td colspan="2" class="key"><div>添加、删除、修改用户</div></td>
          </tr>
          
          <tr id="devRow" class="cat">
            <td width="1%" valign="bottom" nowrap><img src="imx/key.png" /></td>
            <td nowrap><div><a href="javascript:" onClick="popWindow('rolesGet.action')">角色管理</a></div></td>
          </tr>
          <tr id="keyRow">
            <td colspan="2" class="key"><div>添加、删除、修改角色</div></td>
          </tr>          

          <tr id="devRow" class="cat">
            <td width="1%" valign="bottom" nowrap><img src="imx/group.png" /></td>
            <td nowrap><div><a href="javascript:" onClick="popWindow('departmentsGet.action')">部门管理</a></div></td>
          </tr>
          <tr id="keyRow">
            <td colspan="2" class="key"><div>添加、删除、修改部门</div></td>
          </tr>
                    
          <tr id="devRow" class="cat">
            <td width="1%" valign="bottom" nowrap><img src="imx/participation_rate.png" /></td>
            <td nowrap><div><a href="javascript:" onClick="popWindow('shiftConfigGet.action')">值班表</a></div></td>
          </tr>
          <tr id="keyRow">
            <td colspan="2" class="key"><div>添加、删除、修改值班人员和电话</div></td>
          </tr>
          
        </table>
      </td>
      <!-- 中间分割线 -->
      <td class="vr"><img src="imx/spacer.gif"></td>
      <!-- 右侧 -->
      <td width="50%" class="rcol">
        <table id="othDev" border="0" cellpadding="0" cellspacing="0">
        
          <tr id="devRow" class="cat">
            <td width="1%" valign="bottom" nowrap><img src="imx/headphone_mic.png" /></td>
            <td nowrap><div><a href="javascript:" onClick="popWindow('queuesGet.action')">队列管理</a></div></td>
          </tr>
          <tr id="keyRow">
            <td colspan="2" class="key"><div>添加、删除队列，管理分机和队列的对应关系</div></td>
          </tr>
          
          <tr id="devRow" class="cat">
            <td width="1%" valign="bottom" nowrap><img src="imx/mobilephone_blackberry.png" /></td>
            <td nowrap><div><a href="javascript:" onClick="popWindow('sipsGet.action')">分机管理</a></div></td>
          </tr>
          <tr id="keyRow">
            <td colspan="2" class="key"><div>添加、删除、修改分机</div></td>
          </tr>
          
          <tr id="devRow" class="cat">
            <td width="1%" valign="bottom" nowrap><img src="imx/premium_support.png" /></td>
            <td nowrap><div><a href="javascript:" onClick="popWindow('queueUserLinkGet.action')">动态队列管理</a></div></td>
          </tr>
          <tr id="keyRow">
            <td colspan="2" class="key"><div>配置用户和队列的对应关系</div></td>
          </tr>
          
        </table>
      </td>
      <!-- ---------------------------------------------------------- -->
    </tr>
  </table>
</div>

<br />


<jsp:include page="/jsp/about.jsp" flush="true"/>

</body>
</html>
