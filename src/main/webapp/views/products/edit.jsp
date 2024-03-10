<%--
  Created by IntelliJ IDEA.
  User: TungV
  Date: 2024/03/09
  Time: 18:26
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Cập nhật sản phẩm</title>
</head>
<body>
<h1>Cập nhật sản phẩm</h1>
<h4 style="color: red">${warning}</h4>
<form action="/products">
        <input id="productId" type="text" name="productId" hidden>
        <label for="productName">Tên sản phẩm :</label>
        <input id="productName" type="text" name="productName" value="${editName}"><br>
        <label for="manufacturer">Nhà sản xuất :</label>
        <input id="manufacturer" type="text" name="manufacturer" value="${editManufacturer}"><br>
        <label for="productBatch">Lô sản xuất :</label>
        <input id="productBatch" type="text" name="productBatch" value="${editBatch}"><br>
        <label for="productStatus">Trạng thái :</label>
        <input id="productStatus" type="text" name="productStatus" value="${editStatus}"><br>
        <button name="action" value="postEdit">Cập nhật</button><button><a href="/products?action=index">Trang tìm kiếm</a></button>
</form>
</body>
</html>
