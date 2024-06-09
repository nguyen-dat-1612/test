<%@include file="/WEB-INF/views/include/library.jsp"%>

<body>
	<!-- admin-category.jsp - Danh sách loại hoa -->
	<c:choose>
		<c:when test="${alert == 1}">
			<div class="position-fixed bottom-0 end-0 p-3" style="z-index: 11">
				<div class="  alert alert-danger alert-dismissible fade show"
					role="alert">
					Delete Failed
					<button type="button" class="ms-auto btn-close"
						data-bs-dismiss="alert" aria-label="Close"></button>
				</div>
			</div>

		</c:when>
		<c:when test="${alert == 2}">
			<div class="position-fixed bottom-0 end-0 p-3" style="z-index: 11">
				<div class="  alert alert-success alert-dismissible fade show"
					role="alert">
					Delete Successfully
					<button type="button" class="ms-auto btn-close"
						data-bs-dismiss="alert" aria-label="Close"></button>
				</div>
			</div>

		</c:when>

	</c:choose>
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
					<!-- Breadcrum -->
					<div class="row mb-8">
						<div class="col-md-12">
							<div class="d-md-flex justify-content-between align-items-center">
								<div>
									<h2>Categories</h2>
									<!-- breacrumb -->
									<nav aria-label="breadcrumb">
										<ol class="breadcrumb mb-0 text-muted fs-6 fw-semibold">
											<li class="breadcrumb-item  "><a  style ="color:#FD6C9C !important"
												href="admin/user/dashboard.htm"
												class="text-decoration-none text-success ">Dashboard </a></li>
											<li class="breadcrumb-item active" aria-current="page"
												class="text-decoration-none">Category</li>
										</ol>

									</nav>
								</div>
								<!-- button -->
								<div>
									<a href="admin/category/add.htm" class="btn btn-success">Add New Category</a>
								</div>
							</div>
						</div>
					</div>
					<!--End Breadcrum -->
					
					<!-- Bắt đầu một hàng (row) mới -->
					<div class="row">
					    <!-- Tạo một cột chiếm toàn bộ chiều rộng màn hình -->
					    <div class="col-xl-12 col-12 mb-5">
					        <!-- Một khối chứa với khoảng đệm xung quanh -->
					        <div class="px-6 py-6 p-4">
					            <!-- Một hàng mới để chứa các phần tử bên trong -->
					            <div class="row justify-content-between">
					                <!-- Cột chứa biểu mẫu tìm kiếm (search form) -->
					                <div class="col-lg-4 col-md-6 col-12 mb-2 mb-md-0">
					                    <!-- Biểu mẫu tìm kiếm (search form) -->
					                    <form class="d-flex" role="search" action="admin/category/searchCategory.htm">
					                        <!-- Trường nhập liệu để tìm kiếm danh mục -->
					                        <input class="form-control" type="search" placeholder="Search Category" aria-label="Search" name="search">
					                    </form>
					                </div>
					                <!-- Cột chứa menu thả xuống (dropdown menu) -->
					                <div class="col-xl-2 col-md-4 col-12">
					                    <div class="dropdown">
					                        <!-- Nút mở menu thả xuống, màu nền và viền tùy chỉnh -->
					                        <button style="background-color: #FD6C9C !important; border-color: #FD6C9C !important;"
					                                class="btn btn-success dropdown-toggle" type="button" data-bs-toggle="dropdown" aria-expanded="false">
					                            <!-- Hiển thị văn bản nút dựa trên giá trị của biến 'filter' -->
					                            <c:if test="${filter == 0}">All</c:if>
					                            <c:if test="${filter == 1}">Unpublished</c:if>
					                            <c:if test="${filter == 2}">Published</c:if>
					                        </button>
					                        <!-- Danh sách các tùy chọn trong menu thả xuống -->
					                        <ul class="dropdown-menu">
					                            <li><a class="dropdown-item" href="admin/category.htm?filter=0">All</a></li>
					                            <li><a class="dropdown-item" href="admin/category.htm?filter=1">Unpublished</a></li>
					                            <li><a class="dropdown-item" href="admin/category.htm?filter=2">Published</a></li>
					                        </ul>
					                    </div>
					                </div>
					            </div>
					        </div>
					    </div>
					</div>


					<!-- End Search  Filter -->
					<!-- table -->
					<div class="table-responsive">
					    <!-- Bảng hiển thị danh sách -->
					    <table class="table">
					        <!-- Đầu bảng với tiêu đề được giữ ở vị trí cố định trên cùng -->
					        <thead class="position-sticky top-0">
					            <tr class="table-success">
					                <th style = "background-color: #ffd3ed;">Image</th>
					                <th style = "background-color: #ffd3ed;">Category</th>
					                <th style = "background-color: #ffd3ed;">Product Size</th>
					                <th style = "background-color: #ffd3ed;">Update</th>
					                <th style = "background-color: #ffd3ed;">Delete</th>
					                <th style = "background-color: #ffd3ed;">Status</th>
					            </tr>
					        </thead>
					        <tbody>
					            <!-- Lặp qua từng phần tử trong danh sách -->
					            <c:forEach varStatus="status" var="item" items="${list}">
					                <tr>
					                    <!-- Cột hiển thị hình ảnh -->
					                    <td class="align-middle"><a href="product/index.htm?categoryId=${item.id }">
					                            <img src="assets/img/category/${item.image}" alt="Category Name" style="width: 80px">
					                        </a>
					                    </td>
					                    
					                    <!-- Cột hiển thị tên danh mục -->
					                    <td class="align-middle">
					                    	<a href="product/index.htm?categoryId=${item.id }" class="text-dark">${item.name}</a>
					                    </td>
					                    
					                    <!-- Cột hiển thị số lượng sản phẩm thuộc danh mục -->
					                    <td class="align-middle">
					                    	${item.products.size()}
					                    </td>
					                    
					                    <!-- Cột chứa nút Update -->
					                    <td class="align-middle">
					                    	<a href="admin/category/update${item.id}.htm" class="btn btn-warning btn-sm"> Update</a>
					                    </td>
					                    
					                    <!-- Cột chứa nút Delete -->
					                    <td class="align-middle">
					                    	<a style = "background-color: red;" type="button" href="admin/category/delete.htm?id=${item.id}" class="btn btn-danger btn-sm ${item.products.size() > 0 ? 'disabled' : '' }">Delete</a>
					                    </td>
					                    
					                    <!-- Cột hiển thị trạng thái của danh mục -->
					                    <td class="align-middle">
					                        <!-- Nếu danh mục có sản phẩm, hiển thị là Published, ngược lại hiển thị Unpublished -->
					                        <c:if test="${item.products.size() > 0}">
					                            <span class="badge bg-success">Published</span>
					                        </c:if>
					                        <c:if test="${item.products.size() == 0}">
					                            <span class="badge bg-danger">Unpublished</span>
					                        </c:if>
					                    </td>
					                    
					                </tr>
					            </c:forEach>
					        </tbody>
					    </table>
					</div>
				</div>
			</div>
			
			<div class="d-flex justify-content-center ">
				<!-- nav -->
				<nav>
					<ul class="pagination d-flex justify-content-center ms-2">
						<li class="page-item ${(crrPage == 1) ? 'disabled' : '' }"><a
							class="page-link  mx-1 " aria-label="Previous"
							href="admin/category.htm?crrPage=${crrPage - 1}&filter=${filter}">
								<span aria-hidden="true">&laquo;</span>
						</a></li>
						<c:forEach var="i" begin="1" end="${totalPage }" varStatus="in">

							<li class="page-item "><a
								class="page-link  mx-1 ${(crrPage == in.count) ? 'active' : '' }"
								href="admin/category.htm?crrPage=${in.count}&filter=${filter}">${in.count}</a></li>
						</c:forEach>
						<li class="page-item"><a
							class="page-link mx-1 text-body ${(crrPage == totalPage) ? 'disabled' : '' }"
							aria-label="Next"
							href="admin/category.htm?crrPage=${crrPage + 1}&filter=${filter}">
								<span aria-hidden="true">&raquo;</span>
						</a></li>
					</ul>
				</nav>

			</div>
			<!--End pagination -->
		</div>
</body>
<script src="<c:url value="/assets/js/admin/AlertHandler.js"/>"></script>
</html>