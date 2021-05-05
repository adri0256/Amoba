<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="/MainController" />
<html>
    <head>
        <title>Amoba - Replay</title>
        <jsp:include page="common/common-header.jsp"/>
    </head>
    <body class="bg-dark bg-gradient">
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="#">Amoba</a>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link" href="..">Refresh</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
        <div class="container mt-1">
            <table class="table table-dark">
                <thead>
                <tr>
                    <th scope="col">#</th>
                    <th scope="col">Player</th>
                    <th scope="col">Board Size</th>
                    <th scope="col">Action</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="item" items="${requestScope.games}">
                    <tr>
                        <td>${item.id}</td>
                        <td>${item.player}</td>
                        <td>${item.boardSize}</td>
                        <td>
                            <form method="post" action="../MainController">
                                <button type="submit" name="gameId" value="${item.id}" class="btn btn-primary">Replay</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </body>
</html>