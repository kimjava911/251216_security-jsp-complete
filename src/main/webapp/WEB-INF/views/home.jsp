<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%-- Spring Security 태그 라이브러리: 인증 정보에 접근할 수 있게 해줌 --%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
    <title>Security Demo</title>
</head>
<body>
<h1>Spring Security 실습</h1>

<%-- 로그인하지 않은 사용자에게만 표시 --%>
<sec:authorize access="isAnonymous()">
    <p>로그인이 필요합니다.</p>
    <a href="<c:url value="/auth/login" />">로그인</a> |
    <a href="<c:url value="/auth/signup" />">회원가입</a>
</sec:authorize>

<%-- 로그인한 사용자에게만 표시 --%>
<sec:authorize access="isAuthenticated()">
    <p>
        환영합니다,
        <%-- 현재 로그인한 사용자의 이름 출력 --%>
        <sec:authentication property="name"/>님!
    </p>

    <%-- 현재 사용자의 권한(Role) 표시 --%>
    <p>권한: <sec:authentication property="authorities"/></p>

    <a href="<c:url value="/memo" />">내 메모</a> |

    <%-- ADMIN 권한을 가진 사용자에게만 표시 --%>
    <sec:authorize access="hasRole('ADMIN')">
        <a href="<c:url value="/admin" />">관리자 페이지</a> |
    </sec:authorize>

    <%-- 로그아웃 폼: POST 방식으로 전송 --%>
    <form action="<c:url value="/auth/logout" />" method="post" style="display:inline;">
            <%-- CSRF 토큰: 보안을 위해 필수 (자동 생성됨) --%>
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button type="submit">로그아웃</button>
    </form>
</sec:authorize>
</body>
</html>