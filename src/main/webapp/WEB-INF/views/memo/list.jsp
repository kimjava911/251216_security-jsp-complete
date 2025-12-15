<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>내 메모</title>
</head>
<body>
<h1>내 메모 목록</h1>

<p>
    <a href="<c:url value="/memo/new" />">새 메모 작성</a> |
    <a href="<c:url value="/" />">홈으로</a>
</p>

<c:choose>
    <c:when test="${empty memos}">
        <p>작성한 메모가 없습니다.</p>
    </c:when>
    <c:otherwise>
        <table border="1">
            <thead>
            <tr>
                <th>번호</th>
                <th>제목</th>
                <th>작성일</th>
            </tr>
            </thead>
            <tbody>
            <c:forEach var="memo" items="${memos}">
                <tr>
                    <td>${memo.id}</td>
                    <td>
                        <a href="<c:url value="/memo/${memo.id}" />">${memo.title}</a>
                    </td>
                    <td>
                            <%-- 날짜 포맷팅 --%>
                        <fmt:parseDate value="${memo.createdAt}"
                                       pattern="yyyy-MM-dd'T'HH:mm:ss"
                                       var="parsedDate"/>
                        <fmt:formatDate value="${parsedDate}"
                                        pattern="yyyy-MM-dd HH:mm"/>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:otherwise>
</c:choose>
</body>
</html>