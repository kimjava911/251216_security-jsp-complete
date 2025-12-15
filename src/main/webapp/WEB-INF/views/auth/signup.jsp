<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>회원가입</title>
</head>
<body>
<h1>회원가입</h1>

<%-- 에러 메시지 (중복 아이디 등) --%>
<c:if test="${not empty errorMessage}">
    <p style="color: red;">${errorMessage}</p>
</c:if>

<form action="<c:url value="/auth/signup" />" method="post">
    <div>
        <label>아이디:</label>
        <input type="text" name="username" required
               minlength="4" maxlength="20"
               placeholder="4~20자"/>
    </div>
    <div>
        <label>비밀번호:</label>
        <input type="password" name="password" required
               minlength="8"
               placeholder="8자 이상"/>
    </div>

    <%-- CSRF 토큰 필수 --%>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

    <button>가입하기</button>
</form>

<p>이미 계정이 있으신가요? <a href=<c:url value="/auth/login" />>로그인</a></p>
</body>
</html>