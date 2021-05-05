<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="/GameController" />
<html>
<head>
    <jsp:include page="common/common-header.jsp"/>
    <title>Amoba - Replay: ${requestScope.player}</title>
    <script src="../js/replaySteps.js"></script>
</head>
<body class="bg-dark bg-gradient">
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
        <div class="container-fluid">
            <a class="navbar-brand" href="#">Amoba</a>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                    <li class="nav-item">
                        <a class="nav-link active" aria-current="page" href="..">Back</a>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
    <div class="container mt-2">
        <div class="row">
            <div class="col">
                <div class="grid gameTable"
                     style="grid-template-columns: repeat(${requestScope.boardSize}, 80px);
                             grid-template-rows: repeat(${requestScope.boardSize}, 80px)">
                    <c:forEach var="i" begin="0" end="${requestScope.boardSize * requestScope.boardSize - 1}">
                        <div class="cell" id="cell_${i}"></div>
                    </c:forEach>
                </div>
            </div>
            <div class="col-md-auto">
                <div class="d-md-flex justify-content-center nav-buttons">
                    <button name="back" id="back" class="btn btn-primary">Previous Step</button>
                    <button name="forward" id="forward" class="btn btn-primary">Next Step</button>
                </div>
                <div class="d-md-flex justify-content-center nav-buttons">
                    <button name="autoPlay" id="autoPlay" class="btn btn-primary">Auto Play</button>
                    <button name="stopAutoPlay" id="stopAutoPlay" class="btn btn-primary">Stop</button>
                </div>
                <div class="d-md-flex justify-content-center">
                    <span class="playerSpan" id="currentPlayer"></span>
                </div>
                <div class="d-md-flex justify-content-center">
                    <span class="playerSpan" id="winner"></span>
                </div>
            </div>
        </div>
    </div>
</body>
</html>
