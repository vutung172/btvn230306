`<%--
  Created by IntelliJ IDEA.
  User: TungV
  Date: 2024/03/09
  Time: 11:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Sản phẩm</title>
</head>
<body>
<h1>Danh sách sản phẩm</h1>
<div>
    <form action="/products">
        <input type="hidden" name="action" value="index">
        <b>Tìm kiếm: </b><input type="text" name="key" value="${key}" placeholder="Từ khóa tìm kiếm...">
        <button>Tìm</button>
    </form>
</div>
<button><a href="/products?action=create">Thêm mới</a></button>
<h5 style="color: red">${warning}</h5>
<h5 style="color: green">${success}</h5>
<p>Tổng số: ${totalProducts} sản phẩm</p>
<table border="1" cellpadding="5" cellspacing="0" width="100%">
    <tr>
        <th>Mã sp</th>
        <th>Tên sp</th>
        <th>NSX</th>
        <th>Số lô</th>
        <th>Số lượng</th>
        <th>Trạng thái</th>
        <th>Ngày tạo</th>
        <th>Hành động</th>
    </tr>
    <c:forEach var="p" items="${products}">
        <tr>
            <td>${p.getProductId()}</td>
            <td>${p.getProductName()}</td>
            <td>${p.getManufacturer()}</td>
            <td>${p.getBatch()}</td>
            <td>${p.getQuantity()}</td>
            <td>
                <c:if test="${p.getProductStatus() == true}">
                    <span>Hoạt động</span>
                </c:if>
                <c:if test="${p.getProductStatus() == false}">
                    <span>Không hoạt động</span>
                </c:if>
            </td>
            <td><fmt:formatDate value="${p.getCreated()}" pattern="dd/MM/yyyy"/></td>
            <td>
                <button><a href="/products?action=edit&id=${p.getProductId()}">Sửa</a></button>
                <button><a href="/products?action=postDelete&id=${p.getProductId()}">Xóa</a></button>
            </td>
        </tr>
    </c:forEach>
</table>
<c:forEach begin="1" end="${totalPages}" varStatus="loop">
    <c:if test="${page == loop.index}">
        <button type="button" disabled><a href="/products?action=index&key=${key}&page=${loop.index}">${loop.index}</a></button>
    </c:if>
    <c:if test="${page != loop.index}">
        <button><a href="/products?action=index&key=${key}&page=${loop.index}">${loop.index}</a></button>
    </c:if>
</c:forEach>
</body>
</html>
`