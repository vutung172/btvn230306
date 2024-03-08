
<%--
  Created by IntelliJ IDEA.
  User: TungV
  Date: 2024/03/07
  Time: 20:30
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Danh mục</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/category" method="post">
    <label for="categoryName">Nhập vào tên danh mục :</label></br>
    <input id="categoryName" type="text" name="categoryName"></br>
    <label for="description">Nhập vào mô tả :</label></br>
    <textarea id="description" type="text" name="description"></textarea></br>
    <label for="parentCategoryId">Nhập vào mã danh mục cha :</label></br>
    <input id="parentCategoryId" type="number" name="parentCategoryId"></br>
    <button>Thêm</button>
</form>

<form action="${pageContext.request.contextPath}/category" method="get">
    <button name="display" value="true">Hiển thị tất cả danh mục</button>
    <c:if test="${display == true}">
        <button value="false">Đóng</button>
        <table border="2">
            <tr>
                <th colspan="1">Mã danh mục</th>
                <th colspan="1">Tên danh mục</th>
                <th colspan="1">Mô tả</th>
                <th colspan="1">Mã danh mục cha</th>
            </tr>
            <c:forEach var="c" items="${txtCategories}">
                <tr>
                    <td colspan="1">${c.getCategoryId()}</td>
                    <td colspan="1">${c.getCategoryName()}</td>
                    <td colspan="1">${c.getDescription()}</td>
                    <td colspan="1">${c.getParentCategoryId()}</td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
</form>
</body>
</html>
