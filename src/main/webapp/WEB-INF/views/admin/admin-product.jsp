<%@include file="/WEB-INF/views/include/library.jsp"%>

<body>
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
									<h2>Product</h2>
									<!-- breacrumb -->
									<nav aria-label="breadcrumb">
										<ol class="breadcrumb mb-0 text-muted fs-6 fw-semibold">
											<li class="breadcrumb-item  "><a style ="color:#FD6C9C !important"
												href="admin/user/dashboard.htm"
												class="text-decoration-none text-success ">Dashboard </a></li>
											<li class="breadcrumb-item active" aria-current="page"
												class="text-decoration-none">Product</li>
										</ol>

									</nav>
								</div>
								<!-- button -->
								<!-- <div>
									<a href="admin/products/create-product.htm" class="btn btn-success">Add
										New Product</a>
								</div> -->
							</div>
						</div>
					</div>
					<!--End Breadcrum -->
					
					<!-- End Search  Filter -->
					<!-- Bắt đầu phần hiển thị hàng -->
					<div class="row">
					    <!-- Cột chứa phần tìm kiếm, chiếm toàn bộ chiều rộng -->
					    <div class="col-xl-12 col-12 mb-5">
					        <!-- Phần chứa form tìm kiếm với padding -->
					        <div class="px-6 py-6 p-4">
					            <!-- Hàng con để căn giữa nội dung -->
					            <div class="row justify-content-between">
					                <!-- Cột chứa form tìm kiếm, chiếm 4 cột trên màn hình lớn, 6 cột trên màn hình trung bình, và toàn bộ chiều rộng trên màn hình nhỏ -->
					                <div class="col-lg-4 col-md-6 col-12 mb-2 mb-md-0">
					                    <!-- Form tìm kiếm sản phẩm -->
					                    <form class="d-flex" role="search" action="admin/products/searchProduct.htm">
					                        <!-- Input để nhập từ khóa tìm kiếm -->
					                        <input class="form-control" type="search" placeholder="Search Products" aria-label="Search" name="search">
					                    </form>
					                </div>
					            </div>
					        </div>
					    </div>
					</div>

					<!-- End Search  Filter -->
					<!-- table -->
					<div class="table-responsive ">
						<table class="table ">
							<thead class="position-sticky top-0 ">
								<tr class="table-success">
									<th style = "background-color: #ffd3ed">Image</th>
									<th style = "background-color: #ffd3ed">Name</th>
									<th style = "background-color: #ffd3ed">Category</th>
									<th style = "background-color: #ffd3ed">Posting Date</th>
									<th style = "background-color: #ffd3ed">Quantity</th>
									<th style = "background-color: #ffd3ed">Detail</th>
									<th style = "background-color: #ffd3ed">Price</th>
								</tr>
							</thead>
							<tbody>

								<c:forEach varStatus="status" var="item" items="${products}">
								    <tr>
								        <!-- Cột chứa ảnh sản phẩm với liên kết tới chi tiết sản phẩm -->
								        <td class="align-middle">
								            <a href="product/detail.htm?productId=${item.productId}">
								                <img src="<c:url value="assets/img/products/${item.image}" />" alt="Product Name" style="width: 80px">
								            </a>
								        </td>
								
								        <!-- Cột chứa tên sản phẩm với liên kết tới chi tiết sản phẩm -->
								        <td class="align-middle">
								            <a href="product/detail.htm?productId=${item.productId}" class="text-dark">
								                ${item.productName}
								            </a>
								        </td>
								
								        <!-- Cột chứa tên danh mục của sản phẩm -->
								        <td class="align-middle">
								            <span>${item.categoryName}</span>
								        </td>
								
								        <!-- Cột chứa ngày đăng sản phẩm, định dạng ngày theo mẫu dd-MM-yyyy -->
								        <td class="align-middle">
								            <fmt:formatDate value="${item.postingDate}" pattern="dd-MM-yyyy" />
								            <input type="hidden" id="postingDate${item.productId}" value="${item.postingDate}">
								        </td>
								
								        <!-- Cột chứa số lượng sản phẩm -->
								        <td class="align-middle">
								            <span>${item.quantity}</span>
								        </td>
								
								        <!-- Cột chứa mô tả ngắn gọn của sản phẩm -->
								        <td class="align-middle">
								            <p class="text-truncate" style="max-width: 50px;">
								                ${item.detail}
								            </p>
								            <input type="hidden" id="detail${item.productId}" value="${item.detail}">
								        </td>
								
								        <!-- Cột chứa giá sản phẩm, bao gồm cả giá giảm nếu có -->
								        <td class="align-middle">
								            <c:if test="${item.discount > 0}">
								                <span class="text-dark fw-bold">
								                    <fmt:formatNumber value="${item.price - (item.price * item.discount)}" type="currency" currencySymbol="VND" maxFractionDigits="0" />
								                </span>
								                <span class="text-decoration-line-through text-muted">
								                    <fmt:formatNumber value="${item.price}" type="currency" currencySymbol="VND" maxFractionDigits="0" />
								                </span>
								            </c:if>
								            <c:if test="${item.discount == 0}">
								                <span class="text-dark fw-bold">
								                    <fmt:formatNumber value="${item.price}" type="currency" currencySymbol="VND" maxFractionDigits="0" />
								                </span>
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
							href="admin/products.htm?crrPage=${crrPage - 1}"> <span
								aria-hidden="true">&laquo;</span>
						</a></li>
						<c:forEach var="i" begin="1" end="${totalPage }" varStatus="in">

							<li class="page-item "><a
								class="page-link  mx-1 ${(crrPage == in.count) ? 'active' : '' }"
								href="admin/products.htm?crrPage=${in.count}">${in.count}</a></li>
						</c:forEach>
						<li class="page-item"><a
							class="page-link mx-1 text-body ${(crrPage == totalPage) ? 'disabled' : '' }"
							aria-label="Next"
							href="admin/products.htm?crrPage=${crrPage + 1}"> <span
								aria-hidden="true">&raquo;</span>
						</a></li>
					</ul>
				</nav>

			</div>
			<!--End pagination -->
		</div>
</body>
<script src="<c:url value="/assets/js/admin/AlertHandler.js"/>"></script>
</html>