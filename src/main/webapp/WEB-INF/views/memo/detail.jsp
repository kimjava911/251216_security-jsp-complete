<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>메모 상세</title>
</head>
<body>
<h1>${memo.title}</h1>

<p>작성자: ${memo.author.username}</p>
<p>
    작성일:
    <fmt:parseDate value="${memo.createdAt}"
                   pattern="yyyy-MM-dd'T'HH:mm:ss"
                   var="parsedDate"/>
    <fmt:formatDate value="${parsedDate}" pattern="yyyy-MM-dd HH:mm"/>
</p>

<hr/>

<%-- 줄바꿈 처리를 위해 pre 태그 또는 CSS white-space 사용 --%>
<div style="white-space: pre-wrap;">${memo.content}</div>

<hr/>

<%-- 본인 글일 때만 수정/삭제 버튼 표시 --%>
<c:if test="${isOwner}">
    <a href="<c:url value="/memo/${memo.id}/edit" />">수정</a>

    <form action="/memo/${memo.id}/delete" method="post" style="display:inline;">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button onclick="return confirm('정말 삭제하시겠습니까?');">삭제</button>
    </form>
</c:if>

<p><a href="<c:url value="/memo" />">목록으로</a></p>
</body>
</html>