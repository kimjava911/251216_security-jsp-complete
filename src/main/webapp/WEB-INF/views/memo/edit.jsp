<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>메모 수정</title>
</head>
<body>
<h1>메모 수정</h1>

<form action="<c:url value="/memo/${memo.id}/edit" />" method="post">
    <div>
        <label>제목:</label><br/>
        <input type="text" name="title" value="${memo.title}" required style="width: 300px;"/>
    </div>
    <div>
        <label>내용:</label><br/>
        <textarea name="content" rows="10" cols="50" required>${memo.content}</textarea>
    </div>

    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

    <button>수정</button>
    <a href="<c:url value="/memo/${memo.id}" />">취소</a>
</form>
</body>
</html>