<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MYSITE: 일반게시판</title>

<!-- CSS -->
<link href="${pageContext.request.contextPath}/assets/css/mysite.css" rel="stylesheet" type="text/css">
<link href="${pageContext.request.contextPath}/assets/css/board.css" rel="stylesheet" type="text/css">

<!-- js -->
<!-- js -->
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/js/jquery/jquery-1.12.4.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/assets/bootstrap/js/bootstrap.js"></script>

</head>
<body>
	<div id="wrap">

		<c:import url="/WEB-INF/views/includes/header.jsp"></c:import>
		<!-- //header -->

		<div id="container" class="clearfix">
			
			<c:import url="/WEB-INF/views/includes/aside/board.jsp"></c:import>
			<!-- //aside -->

			<div id="content">

				<div id="content-head">
					<h3>게시판</h3>
					<div id="location">
						<ul>
							<li>홈</li>
							<li>게시판</li>
							<li class="last">일반게시판</li>
						</ul>
					</div>
					<div class="clear"></div>
				</div>
				<!-- //content-head -->
	
				<div id="board">
					<div id="list">
						<form action="${pageContext.request.contextPath}/board/list/" method="get">
							<div class="form-group text-right">
								<input type="text" name="keyword" value="">
								<button type="submit" id=btn_search>검색</button>
							</div>
						</form>
						<table >
							<colgroup>
							<col style="width: 8%;">
							<col style="width: 38%;">
							<col style="width: 12%;">
							<col style="width: 12%;">
							<col style="width: 19.5%;">
							<col style="width: 10.5%;">
							</colgroup>
							<thead>
								<tr>
									<th>번호</th>
									<th>제목</th>
									<th>글쓴이</th>
									<th>조회수</th>
									<th>작성일</th>
									<th>관리</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${pMap.boardList}" var="boardVo">
									<tr>
										<td>${boardVo.no}</td>
										<td class="text-left"><a href="${pageContext.request.contextPath}/board/read?no=${boardVo.no}">${boardVo.title}</a></td>
										<td>${boardVo.name}</td>
										<td>${boardVo.hit}</td>
										<td>${boardVo.regDate}</td>
										<td>
											<c:if test="${authUser.no == boardVo.userNo}">
												<a href="./delete?no=${boardVo.no}">[삭제]</a>
											</c:if>
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
			
						<div id="paging">
							<ul>
								<c:choose>
									<c:when test="${pMap.prev}">
										<li><a href="${pageContext.request.contextPath}/board/list?keyword=${pMap.keyword}&crtPage=${pMap.startPageBtnNo-1}">◀</a></li>
									</c:when>
									<c:otherwise>
										<li><a href="">◀</a></li>
									</c:otherwise>
								</c:choose>
								
								<c:forEach begin="${pMap.startPageBtnNo}" end="${pMap.endPageBtnNo}" step="1" var="page">
									<c:choose>
										<c:when test="${pMap.crtPage==page}">
											<li class="active"><a href="${pageContext.request.contextPath}/board/list?keyword=${pMap.keyword}&crtPage=${page}">${page}</a></li>
										</c:when>
										<c:otherwise>
											<li><a href="${pageContext.request.contextPath }/board/list?keyword=${pMap.keyword}&crtPage=${page}">${page}</a></li>
										</c:otherwise>
									</c:choose>
								</c:forEach>

								<c:choose>
									<c:when test="${pMap.next}">
										<li><a href="${pageContext.request.contextPath}/board/list?keyword=${pMap.keyword}&crtPage=${pMap.endPageBtnNo+1}">▶</a></li>
									</c:when>
									<c:otherwise>
										<li><a href="">▶</a></li>
									</c:otherwise>
								</c:choose>
							</ul>
							
							
							<div class="clear"></div>
						</div>
						<c:if test="${!empty authUser}">
							<a id="btn_write" href="./writeForm">글쓰기</a>
						</c:if>
					</div>
					<!-- //list -->
				</div>
				<!-- //board -->
			</div>
			<!-- //content  -->

		</div>
		<!-- //container  -->
		

		<c:import url="/WEB-INF/views/includes/footer.jsp"></c:import>
		<!-- //footer -->
	</div>
	<!-- //wrap -->

</body>

<script type="text/javascript">

</script>

</html>