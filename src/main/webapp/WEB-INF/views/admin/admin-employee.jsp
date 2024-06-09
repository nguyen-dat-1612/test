<%@include file="/WEB-INF/views/include/library.jsp"%>

<body>
<!-- admin-employee - Danh sách nhân viên -->
	<div class="row">

		<!-- Sidebar -->
		<div class="col-2 d-none d-lg-inline "><%@include
				file="/WEB-INF/views/admin/admin-header-nav.jsp"%></div>
		<div class="col-10 col-12-sm col-12-md">
			<div id="content-wrapper" class="d-flex flex-column">
				<nav
					class="  navbar navbar-light bg-white mb-4 static-top shadow d-none d-lg-inline">
					<%@include file="/WEB-INF/views/admin/admin-topbar.jsp"%>
				</nav>
				<div class="container">
					<div class="row mb-4">
						<div class="col-md-12">
							<div class="d-md-flex justify-content-between align-items-center">
								<!-- pageheader -->
								<div>
									<h2>Employee</h2>
									<!-- breacrumb -->
									<nav aria-label="breadcrumb">
										<ol class="breadcrumb mb-0 text-muted fs-6 fw-semibold">
											<li class="breadcrumb-item  "><a href="index.htm" style ="color:#FD6C9C !important"
												class="text-decoration-none text-success ">Dashboard </a></li>
											<li class="breadcrumb-item active" aria-current="page"
												class="text-decoration-none">Employee</li>
										</ol>

									</nav>
								</div>
								<!-- button -->
								<div>
									<a href="admin/user/create-employee.htm"
										class="btn btn-outline-success">Add New Employee</a>
								</div>
							</div>
						</div>
					</div>

					<table class="table table-hover table-striped table-categories ">
						<!-- Phần đầu bảng -->
						<thead class="position-sticky top-0 bg-white">
							<tr class="table-success">
								<th style = "background-color: #ffd3ed">Avatar</th>
								<th style = "background-color: #ffd3ed">Fullname</th>
								<th style = "background-color: #ffd3ed">Status</th>
								<th style = "background-color: #ffd3ed">Email</th>
								<th style = "background-color: #ffd3ed">Enable</th>
								<th style = "background-color: #ffd3ed">Edit</th>

							</tr>
						</thead>
						    <!-- Phần thân bảng -->
						<tbody>
							<!-- Sử dụng thẻ c:forEach để lặp qua các phần tử trong danh sách accounts -->
							<c:forEach varStatus="status" var="account" items="${accounts}">
								<!-- Mỗi phần tử account sẽ được hiển thị trong một hàng tr -->
								<tr>
									 <!-- Cột hiển thị hình đại diện của tài khoản -->
									<td class="align-middle"><img style="width: 50px"
										src="<c:url value = "/assets/img/account/${account.avatar}"/>">
									</td>
									
									<!-- Cột hiển thị tên đầy đủ của tài khoản -->
									<td class="align-middle"><c:set var="fullName"
											value="${account.lastName} ${account.firstName} " />
										<p class="m-0">${fullName}</p>
									</td>
									
									<!-- Cột hiển thị trạng thái của tài khoản -->
									<td class="align-middle">
										<!-- Sử dụng thẻ c:choose để kiểm tra trạng thái của tài khoản -->
										<!-- Nếu trạng thái là 1, hiển thị badge Enable -->
										<!-- Nếu trạng thái không phải là 1, hiển thị badge Disable -->
										<c:choose>
											<c:when test="${account.status eq '1'}">
												<span class="badge bg-success">Enable </span>
											</c:when>
											<c:otherwise>
												<span class="badge bg-danger">Disable </span>
											</c:otherwise>
										</c:choose>
									</td>
									<!-- Cột hiển thị email của tài khoản -->
									<td class="align-middle">
										<p class="p-3">${account.email }</p>
									</td>
									<!-- Cột chứa nút Enable/Disable tài khoản -->
									<td class="align-middle">
										<a href="admin/user/enable${account.accountId }.htm?source=${source}">
											<button class="btn btn-success" type="button" id="edit_button${status.index}">
												<c:choose>
													<c:when test="${ account.status eq '1'}">
														<i class="bi bi-lock-fill "></i>
													</c:when>
													<c:otherwise>
														<i class="bi bi-unlock-fill "></i>
													</c:otherwise>
												</c:choose>
											</button>
										</a>
									</td>
									<!-- Cột chứa liên kết đi đến mục chỉnh sửa tài khoản -->
									<td class="align-middle"><a class="link-success" href="admin/user/edit.htm?id=${account.accountId }"><i
											class="bi bi-pencil-square"></i>Edit</a>
									</td>

								</tr>
							</c:forEach>
						</tbody>
					</table>
				</div>
			</div>

			<!-- pagination -->
			<div class="d-flex justify-content-center ">

				<!-- nav -->
				<nav>
					<ul class="pagination d-flex justify-content-center ms-2">
						<li class="page-item ${(crrPage == 1) ? 'disabled' : '' }"><a
							class="page-link  mx-1 " aria-label="Previous"
							href="admin/user/${source }?crrPage=${crrPage - 1}"> <span
								aria-hidden="true">&laquo;</span>
						</a></li>
						<c:forEach var="i" begin="1" end="${totalPage }" varStatus="in">

							<li class="page-item "><a
								class="page-link  mx-1 ${(crrPage == in.count) ? 'active' : '' }"
								href="admin/user/${source }?crrPage=${in.count}">${in.count}</a></li>
						</c:forEach>
						<li class="page-item"><a
							class="page-link mx-1 text-body ${(crrPage == totalPage) ? 'disabled' : '' }"
							aria-label="Next"
							href="admin/user/${source }?crrPage=${crrPage + 1}"> <span
								aria-hidden="true">&raquo;</span>
						</a></li>
					</ul>
				</nav>

			</div>
			<!--End pagination -->

		</div>

	</div>


</body>
<script src="<c:url value="/assets/js/admin/AlertHandler.js"/>"></script>
</html>