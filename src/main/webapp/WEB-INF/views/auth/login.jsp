<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
<head>
    <title>로그인</title>
</head>
<body>
<h1>로그인</h1>

<%-- 에러 메시지 표시 영역 --%>
<%--<c:if test="${not empty errorMessage}">--%>
<%--    <p style="color: red;">${errorMessage}</p>--%>
<%--</c:if>--%>

<%-- 로그아웃 메시지 표시 영역 --%>
<%--<c:if test="${not empty logoutMessage}">--%>
<%--    <p style="color: green;">${logoutMessage}</p>--%>
<%--</c:if>--%>


<%-- 리다이렉트 안내 메시지 --%>
<c:if test="${param.redirect == 'true'}">
    <p style="color: blue;">로그인이 필요한 페이지입니다. 로그인 후 이동합니다.</p>
</c:if>

<%-- 에러 메시지 (커스텀 메시지 또는 기본 메시지) --%>
<c:if test="${param.error == 'true'}">
    <p style="color: red;">
        <c:choose>
            <c:when test="${not empty param.message}">
                ${param.message}
            </c:when>
            <c:otherwise>
                아이디 또는 비밀번호가 올바르지 않습니다.
            </c:otherwise>
        </c:choose>
    </p>
</c:if>

<%-- 회원가입 성공 메시지 --%>
<c:if test="${param.signup == 'success'}">
    <p style="color: green;">회원가입이 완료되었습니다. 로그인해주세요.</p>
</c:if>

<%-- 로그아웃 메시지 --%>
<c:if test="${param.logout != null}">
    <p style="color: green;">로그아웃되었습니다.</p>
</c:if>

<%--
    로그인 폼
    action: SecurityConfig에서 설정한 loginProcessingUrl과 일치해야 함
    method: 반드시 POST
--%>
<form action="<c:url value="/auth/login" />" method="post">

    <div>
        <label>아이디:</label>
        <%-- name="username": Spring Security가 기본으로 찾는 파라미터명 --%>
        <input type="text" name="username" required/>
    </div>
    <div>
        <label>비밀번호:</label>
        <%-- name="password": Spring Security가 기본으로 찾는 파라미터명 --%>
        <input type="password" name="password" required/>
    </div>

    <%--
        CSRF 토큰 (Cross-Site Request Forgery 방지)
        Spring Security가 자동으로 생성하는 보안 토큰
        이 토큰이 없으면 403 Forbidden 에러 발생
    --%>
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>

    <button>로그인</button>
</form>

<p><a href="<c:url value="/auth/signup" />">회원가입</a></p>
<p><a href="<c:url value="/" />">홈으로</a></p>
</body>
</html>