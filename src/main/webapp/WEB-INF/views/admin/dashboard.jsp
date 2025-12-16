<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>관리자 페이지</title>
</head>
<body>
<h1>관리자 대시보드</h1>

<p><a href="<c:url value="/" />">홈으로</a></p>

<h2>사용자 관리</h2>

<table border="1">
    <thead>
    <tr>
        <th>ID</th>
        <th>아이디</th>
        <th>이메일</th>
        <th>권한</th>
        <th>상태</th>
        <th>가입일</th>
        <th>관리</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach var="user" items="${users}">
        <tr>
            <td>${user.id}</td>
            <td>${user.username}</td>
            <td>${user.role}</td>
            <td>
                <c:choose>
                    <c:when test="${user.enabled}">
                        <span style="color: green;">활성</span>
                    </c:when>
                    <c:otherwise>
                        <span style="color: red;">비활성</span>
                    </c:otherwise>
                </c:choose>
            </td>
            <td>
                <fmt:parseDate value="${user.createdAt}"
                               pattern="yyyy-MM-dd'T'HH:mm:ss"
                               var="parsedDate"/>
                <fmt:formatDate value="${parsedDate}" pattern="yyyy-MM-dd"/>
            </td>
            <td>
                <form action="/admin/users/${user.id}/toggle" method="post" style="display:inline;">
                    <input type="hidden" name="enabled" value="${user.enabled}"/>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    <c:choose>
                        <c:when test="${user.enabled}">
                            <button type="submit">비활성화</button>
                        </c:when>
                        <c:otherwise>
                            <button type="submit">활성화</button>
                        </c:otherwise>
                    </c:choose>
                </form>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</body>
</html>
