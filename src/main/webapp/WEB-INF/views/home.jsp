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

<%--&lt;%&ndash; 로그인하지 않은 사용자에게만 표시 &ndash;%&gt;--%>
<%--<sec:authorize access="isAnonymous()">--%>
<%--    <p>로그인이 필요합니다.</p>--%>
<%--    <a href="<c:url value="/auth/login" />">로그인</a> |--%>
<%--    <a href="<c:url value="/auth/signup" />">회원가입</a>--%>
<%--</sec:authorize>--%>

<%--&lt;%&ndash; 로그인한 사용자에게만 표시 &ndash;%&gt;--%>
<%--<sec:authorize access="isAuthenticated()">--%>
<%--    <p>--%>
<%--        환영합니다,--%>
<%--        &lt;%&ndash; 현재 로그인한 사용자의 이름 출력 &ndash;%&gt;--%>
<%--        <sec:authentication property="name"/>님!--%>
<%--    </p>--%>

<%--    &lt;%&ndash; 현재 사용자의 권한(Role) 표시 &ndash;%&gt;--%>
<%--    <p>권한: <sec:authentication property="authorities"/></p>--%>

<%--    <a href="<c:url value="/memo" />">내 메모</a> |--%>

<%--    &lt;%&ndash; ADMIN 권한을 가진 사용자에게만 표시 &ndash;%&gt;--%>
<%--    <sec:authorize access="hasRole('ADMIN')">--%>
<%--        <a href="<c:url value="/admin" />">관리자 페이지</a> |--%>
<%--    </sec:authorize>--%>

<%--    &lt;%&ndash; 로그아웃 폼: POST 방식으로 전송 &ndash;%&gt;--%>
<%--    <form action="<c:url value="/auth/logout" />" method="post" style="display:inline;">--%>
<%--            &lt;%&ndash; CSRF 토큰: 보안을 위해 필수 (자동 생성됨) &ndash;%&gt;--%>
<%--        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>--%>
<%--        <button type="submit">로그아웃</button>--%>
<%--    </form>--%>
<%--</sec:authorize>--%>

<%-- 로그인 여부에 따른 분기 --%>
<sec:authorize access="isAuthenticated()">
    <h2>로그인 정보</h2>

    <%-- 사용자 이름 --%>
    <p>사용자명: <sec:authentication property="name"/></p>

    <%-- 권한 목록 --%>
    <p>권한: <sec:authentication property="authorities"/></p>

    <%-- Principal 객체의 특정 속성 (CustomUserDetails 사용 시) --%>
    <%-- <p>사욪자명: <sec:authentication property="principal.username"/></p> --%>
</sec:authorize>

<%-- 특정 권한에 따른 UI 분기 --%>
<h2>권한별 메뉴</h2>
<ul>
    <li><a href="<c:url value="/memo" />">내 메모</a></li>

    <%-- ADMIN 권한이 있을 때만 표시 --%>
    <sec:authorize access="hasRole('ADMIN')">
        <li><a href="<c:url value="/admin"/>">관리자 페이지</a></li>
        <li><a href="<c:url value="/admin/users"/>">">사용자 관리</a></li>
    </sec:authorize>

    <%-- USER 권한만 있을 때 표시 (ADMIN 제외) --%>
    <sec:authorize access="hasRole('USER') and !hasRole('ADMIN')">
        <li><span style="color: gray;">관리자 메뉴 (권한 없음)</span></li>
    </sec:authorize>
</ul>

<%-- 특정 조건에서만 버튼 표시 --%>
<sec:authorize access="isAuthenticated()">
    <form action="<c:url value="/auth/logout"/>" method="post">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        <button>로그아웃</button>
    </form>
</sec:authorize>

<sec:authorize access="isAnonymous()">
    <a href="<c:url value="/auth/login"/>">로그인</a>
</sec:authorize>
</body>
</html>
