<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>새 메모</title>
</head>
<body>
<h1>새 메모 작성</h1>

<form action="<c:url value="/memo/new" />" method="post">
    <div>
        <label>제목:</label><br/>
        <input type="text" name="title" required style="width: 300px;"/>
    </div>
    <div>
        <label>내용:</label><br/>
        <textarea name="content" rows="10" cols="50" required></textarea>
    </div>

    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

    <button>저장</button>
    <a href="<c:url value="/memo" />">취소</a>
</form>
</body>
</html>