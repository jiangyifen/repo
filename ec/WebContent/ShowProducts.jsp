<%@ page  contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<html>
<head>
    <title>Hello World</title>
</head>
<body>    
    <table>
        <tr style="background-color:powderblue; font-weight:bold;">
            <td>Product Name</td>
            <td>Price</td>
            <td>Date of production</td>
        </tr>
        <s:iterator value="products" status="stat">
            <tr>
                <td><s:property value="name"/></td>
                <td>$<s:property value="price"/></td>
                <td><s:property value="dateOfProduction"/></td>
            </tr>
        </s:iterator>
    </table>
</body>
</html>