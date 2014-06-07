<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>

<html>
<head>
<title>短信群发</title>
<script src="src/init.js"></script>
<script src="src/xuaLib.js"></script>
<script src="src/util.js"></script>
<script src="src/vmCtrlBar.js"></script>
<script src="src/vmCtxMenu.js"></script>
<script src="src/sxMonitor.js"></script>
<script language="javascript" type="text/javascript"
	src="/ec/src/My97DatePicker/WdatePicker.js"></script>
<link rel="stylesheet" href="css/usage.css" type="text/css" />
<link rel="stylesheet" href="css/page.css" type="text/css" />
<script language="javascript">
	function validate() {
		document.form1.submit();
		return true;
	}
</script>

<SCRIPT language="javascript">
	function checktext(text) {
		allValid = true;
		for (i = 0; i < text.length; i++) {
			if (text.charAt(i) != " ") {
				allValid = false;
				break;
			}
		}
		return allValid;
	}

	function gbcount(message, total, used, remain) {
		var max;
		max = total.value;
		if (message.value.length > max) {
			message.value = message.value.substring(0, max);
			used.value = max;
			remain.value = 0;
			alert("字数超出范围");
		} else {
			used.value = message.value.length;
			remain.value = max - used.value;
		}
	}

	function cleanTextArea() {
		var textarea = document.getElementById('smcontent');
		textarea.value = '';
	}
</script>
<style type="text/css">
* {
	padding: 0;
	margin: 0;
}

body,html {
	text-align: left;
	font-size: 12px;
	line-height: 150%;
	margin: 0 auto;
	background: #fff;
	padding-top: 20px;
}

fieldset {
	padding: 10px;
	width: 550px;
	margin: 0 auto;
}

legend {
	font-size: 14px;
	font-weight: bold;
}

.inputtext {
	border: none;
	background: #fff;
}
</style>
</head>
<body onload="cleanTextArea()">
<form action="dialTaskSmSend" method="post">

<table>
	<tr>
		<td>
		<fieldset><legend>外呼任务：<s:property value="dialTaskName"/></legend>
		<legend>请输入内容：</legend> <textarea id="smcontent" name="smcontent" rows="10" cols="70"
			onKeyDown="gbcount(this.form.smcontent,this.form.total,this.form.used,this.form.remain);"
			onKeyUp="gbcount(this.form.smcontent,this.form.total,this.form.used,this.form.remain);">
		</textarea>

		<p>最多字数： <input disabled maxLength="4" name="total" size="3"
			value="700" class="inputtext"> 已用字数： <input disabled
			maxLength="4" name="used" size="3" value="0" class="inputtext">
		剩余字数： <input disabled maxLength="4" name="remain" size="3" value="700"
			class="inputtext"></p>
		</fieldset>
		</td>
	</tr>
	<tr>
		<td>
		发送给（多个号码请用“;”分开）：<input name="phonenumbers" type="text"  class="text" size="65"/>
		</td>
	</tr>
	<tr>
		<td>
		<input type="hidden" name="dialTaskId" value="<s:property value="dialTaskId"/>" />
		发送部门：<s:select theme="simple" list="dpmts" name="departmentname" listKey="departmentname" listValue="description" />
		日期：<input name="date" type="text" onclick="WdatePicker({skin:'blue'})" class="text" value="${sessionScope.today}" />
		<select name="hour">
			<option value="00">00</option>
			<option value="01">01</option>
			<option value="02">02</option>
			<option value="03">03</option>
			<option value="04">04</option>
			<option value="05">05</option>
			<option value="06">06</option>
			<option value="07">07</option>
			<option value="08">08</option>
			<option value="09">09</option>
			<option value="10">10</option>
			<option value="11">11</option>
			<option value="12">12</option>
			<option value="13">13</option>
			<option value="14">14</option>
			<option value="15">15</option>
			<option value="16">16</option>
			<option value="17">17</option>
			<option value="18">18</option>
			<option value="19">19</option>
			<option value="20">20</option>
			<option value="21">21</option>
			<option value="22">22</option>
			<option value="23">23</option>
		</select> 时 <select name="minute">
			<option value="00">00</option>
			<option value="01">01</option>
			<option value="02">02</option>
			<option value="03">03</option>
			<option value="04">04</option>
			<option value="05">05</option>
			<option value="06">06</option>
			<option value="07">07</option>
			<option value="08">08</option>
			<option value="09">09</option>
			<option value="10">10</option>
			<option value="11">11</option>
			<option value="12">12</option>
			<option value="13">13</option>
			<option value="14">14</option>
			<option value="15">15</option>
			<option value="16">16</option>
			<option value="17">17</option>
			<option value="18">18</option>
			<option value="19">19</option>
			<option value="20">20</option>
			<option value="21">21</option>
			<option value="22">22</option>
			<option value="23">23</option>
			<option value="24">24</option>
			<option value="25">25</option>
			<option value="26">26</option>
			<option value="27">27</option>
			<option value="28">28</option>
			<option value="29">29</option>
			<option value="30">30</option>
			<option value="31">31</option>
			<option value="32">32</option>
			<option value="33">33</option>
			<option value="34">34</option>
			<option value="35">35</option>
			<option value="36">36</option>
			<option value="37">37</option>
			<option value="38">38</option>
			<option value="39">39</option>
			<option value="40">40</option>
			<option value="41">41</option>
			<option value="42">42</option>
			<option value="43">43</option>
			<option value="44">44</option>
			<option value="45">45</option>
			<option value="46">46</option>
			<option value="47">47</option>
			<option value="48">48</option>
			<option value="49">49</option>
			<option value="50">50</option>
			<option value="51">51</option>
			<option value="52">52</option>
			<option value="53">53</option>
			<option value="54">54</option>
			<option value="55">55</option>
			<option value="56">56</option>
			<option value="57">57</option>
			<option value="58">58</option>
			<option value="59">59</option>
		</select> 分 <select name="second">
			<option value="00">00</option>
			<option value="01">01</option>
			<option value="02">02</option>
			<option value="03">03</option>
			<option value="04">04</option>
			<option value="05">05</option>
			<option value="06">06</option>
			<option value="07">07</option>
			<option value="08">08</option>
			<option value="09">09</option>
			<option value="10">10</option>
			<option value="11">11</option>
			<option value="12">12</option>
			<option value="13">13</option>
			<option value="14">14</option>
			<option value="15">15</option>
			<option value="16">16</option>
			<option value="17">17</option>
			<option value="18">18</option>
			<option value="19">19</option>
			<option value="20">20</option>
			<option value="21">21</option>
			<option value="22">22</option>
			<option value="23">23</option>
			<option value="24">24</option>
			<option value="25">25</option>
			<option value="26">26</option>
			<option value="27">27</option>
			<option value="28">28</option>
			<option value="29">29</option>
			<option value="30">30</option>
			<option value="31">31</option>
			<option value="32">32</option>
			<option value="33">33</option>
			<option value="34">34</option>
			<option value="35">35</option>
			<option value="36">36</option>
			<option value="37">37</option>
			<option value="38">38</option>
			<option value="39">39</option>
			<option value="40">40</option>
			<option value="41">41</option>
			<option value="42">42</option>
			<option value="43">43</option>
			<option value="44">44</option>
			<option value="45">45</option>
			<option value="46">46</option>
			<option value="47">47</option>
			<option value="48">48</option>
			<option value="49">49</option>
			<option value="50">50</option>
			<option value="51">51</option>
			<option value="52">52</option>
			<option value="53">53</option>
			<option value="54">54</option>
			<option value="55">55</option>
			<option value="56">56</option>
			<option value="57">57</option>
			<option value="58">58</option>
			<option value="59">59</option>
		</select> 秒 <input type="reset" value="重写" /> <input type="submit" value="发送" />
		</td>
	</tr>
</table>
</form>


</body>
</html>




