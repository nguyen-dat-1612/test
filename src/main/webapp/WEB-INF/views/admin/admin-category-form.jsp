<%@include file="/WEB-INF/views/include/library.jsp"%>
<c:if test="${not empty addBean }">
    <!-- Kiểm tra xem biến addBean có tồn tại và không rỗng không -->
    <!-- Nếu điều kiện đúng, các biến crrBean, nameBean, actionString sẽ được khởi tạo -->
    <c:set var="crrBean" value="${addBean}" />
    <c:set var="nameBean" value="addBean" />
    <c:set var="actionString" value="admin/category/add.htm" />
</c:if>

<c:if test="${not empty updateBean }">
    <!-- Kiểm tra xem biến updateBean có tồn tại và không rỗng không -->
    <!-- Nếu điều kiện đúng, các biến crrBean, nameBean, actionString sẽ được khởi tạo -->
    <c:set var="crrBean" value="${updateBean}" />
    <c:set var="nameBean" value="updateBean" />
    <c:set var="actionString" value="admin/category/update.htm" />
</c:if>

<body>
	<!-- admin-category-form.jsp - update loại hoa và thêm hoa mới-->
	
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
				<!-- Main -->
				<div class="container">
					<div class="row mt-3">

						<div class="col-md-12 mb-4">
							<div class="d-md-flex justify-content-between align-items-center">
								<!-- pageheader -->
								<div>
									<h2>Create Category</h2>
									<!-- breacrumb -->
									<nav aria-label="breadcrumb">
										<ol class="breadcrumb mb-0 text-muted fs-6 fw-semibold">
											<li class="breadcrumb-item  "><a style ="color:#FD6C9C !important"
												href="admin/user/dashboard.htm"
												class="text-decoration-none text-success ">Dashboard </a></li>
											<li class="breadcrumb-item "><a style ="color:#FD6C9C !important"
												href="admin/user/get-employee.htm"
												class="text-decoration-none text-success ">Category </a>
											<li class="breadcrumb-item active" aria-current="page"
												class="text-decoration-none">Create Category</li>
										</ol>

									</nav>
								</div>

							</div>
						</div>

						<!-- Bắt đầu một cột trong Bootstrap grid system -->
						<div class="col-md-6 offset-md-3 card p-5">
						    <!-- Form với Spring form tag, các giá trị động được lấy từ các biến JSP -->
						    <form:form action="${actionString}" method="post" modelAttribute="${nameBean}" enctype="multipart/form-data">
						        <!-- Input ẩn để lưu trữ ID của danh mục -->
						        <form:input type="hidden" path="id" />
						        
						        <!-- Nhóm form cho tên danh mục -->
						        <div class="form-group">
						            <label for="name-category" class="mb-2 fw-bold">Name Category</label>
						            <!-- Input text cho tên danh mục -->
						            <form:input type="text" class="form-control" id="name-category" path="name" />
						        </div>
						        
						        <!-- Nhóm form để hiển thị hình ảnh hiện tại -->
						        <div class="form-group mt-3">
						            <p class="mb-2 fw-bold">Current Image</p>
						            <!-- Hình ảnh hiện tại của danh mục -->
						            <img id="output" class="width-100" style="width: 100px" src="<c:url value="assets/img/category/${crrBean.image}" />" alt="Image not found" />
						        </div>
						        
						        <!-- Nhóm form để thay đổi hình ảnh -->
						        <div class="form-group mt-3">
						            <label for="image" class="mb-2 fw-bold">Change Image</label>
						            <!-- Input file để chọn hình ảnh mới -->
						            <form:input id="image" type="file" class="form-control" accept="image/*" path="fileImage" onchange="loadFile(event)" />
						        </div>
						        
						        <!-- Nút submit -->
						        <button type="submit" class="mt-3 btn btn-success">Submit</button>
						    </form:form>
						</div>
					</div>
				</div>
				<!-- End Main -->
			</div>
		</div>
	</div>

</body>
<script type="text/javascript"
	src="<c:url value="assets/js/user/account.js" />"></script>
</html>
