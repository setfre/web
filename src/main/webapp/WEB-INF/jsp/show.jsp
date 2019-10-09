<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<meta name="viewport" content="width=device-width, initial-scale=1">
		<link rel="stylesheet" href="https://cdn.staticfile.org/twitter-bootstrap/4.3.1/css/bootstrap.min.css">
		<script src="https://cdn.staticfile.org/jquery/3.2.1/jquery.min.js"></script>
		<script src="https://cdn.staticfile.org/popper.js/1.15.0/umd/popper.min.js"></script>
		<script src="https://cdn.staticfile.org/twitter-bootstrap/4.3.1/js/bootstrap.min.js"></script>
		<link rel="stylesheet" href="css/front-style.css"/> 
		<title></title>
	</head>
<style>
	th{
		text-align:center;
	}
	.cells,.table-search,.table-search input,.cells-label{
		float: left;
	}
	.table-search{
		margin-right:4px;
	}
	.cells-label{
		display:inline-block;
		height:38px;
		margin-top:0;
		margin-right:0;
		padding:5px 0;
	}
	.cell-label{
		padding:5px;
		text-align:center;
	}
	.table-search .cells{
		width: 100px; 
	}
	.table-search-btn button{
		margin-right: 15px;
	}
	.table-search-btn,.pagination,.pagination-index,.pagination-redirect{
		float:right;
	}
	.fm-lg-input{
		width: 60px;
	}
	.table-requestable{
		margin: 0 auto;
		width: 437px;
	}
	.form-cells{
		width: 437px;
	}
	.label-cells-radio{
		float: left;
		margin: 0 5px;
	}
	.form-cells .form-cells-label{
		float: left;
		clear: left;
		width: 80px;
		margin-top: 7px;
		margin-bottom: 7px;
		margin-right: 7px;
		text-align: center;
	}
	.form-cells .form-cells-input,.fm-cl-rd{
		float: left;
		width: 350px;
	}
	.sidbar{
		width: 216px;
		height: 615px;
		float: left; 
		background-color: rgb(247,247,247);
	}
	.sidbar-card{
		margin: 20px auto;
		width: 200px;
	} 
	.show-nab{
		width: 100%;
		height: 50px;
		background-color:rgb(247,247,247);
		border-bottom:1px solid rgba(0,0,0,.03);
	}
	.show-context{
		width: 100%;
	}
	.show-nab-title{
		width:216px;
	}
	.show-nab-title a:hover{
		text-decoration: none;
	}
	.show-nab-img{
		height: 40px;
		width: 40px;
		border-radius: 50%;
	}
	.show-nab-img:hover{
		cursor: pointer; 
		box-shadow:0px 1px 4px rgba(0,0,0,0.6),0 0 40px rgba(0,0,0,0.1) inset;
	}
	.show-nab-group{
		height: 40px;
		margin: 4px 13px;
		display:flex;
	}
	.show-nab-group,.show-nab-link{
		float: left;
	}
	.show-nab-link{
		margin:auto;
		padding: 0 15px;
		display:inline-block;
	}
	.show-nab-active{
		float: right;
	}
	.text-color{
		color:red;
	}
	.show-body{
		display:flex;
	}
</style>
<script>
	/*全选/取消 记录集 */
	function toCheckedRec(checked){
		var records = document.getElementsByName("recordId");
		if(checked == true){
			document.getElementById("records-checked").innerHTML ="取消";
		}
		else{
			document.getElementById("records-checked").innerHTML ="全选";
		}
		for(i=0;i<records.length;i++){
			var record = records[i];
			if(checked==true&&record.checked == false){
				record.checked = !record.checked;
			} 
			if(checked==false&&record.checked == true){
				record.checked = !record.checked;
			}
		}
	}
/* 	$(document).ready(function(){
		$(document).on('click','a',function({
			$().show().delay().hidden();
		}))
	}) */
</script>
<body>
<div class="container-fluid px-0">
	<div class="col-sm-12 show-layog">
		<div class="row">
			<!-- 顶部栏 -->
			<div class="show-nab">
				<div class="show-nab-group">
					<div class="show-nab-link show-nab-title">
						<a href="#">xxx宠物商店</a>
					</div>
				</div>
				<div class="show-nab-group">
					<div class="show-nab-link">
						<a href="/pet/petResourse/toPetshop.action">宠物商品</a>
					</div>
					<div class="show-nab-link">
						<a href="#">我的宠物</a>
					</div>
					<div class="show-nab-link">
						<a href="#">link3</a> 
					</div>
					<div class="show-nab-link">
						<a href="#">link4</a>
					</div>
				</div> 
				<div class="show-nab-active show-nab-group">
					<c:if test="${activeUser == null}">
						<div class="show-nab-link">
							<a href="/pet/userResourse/toLogin.action">登录</a>/
						</div>
						
						<div class="show-nab-link">
							<a href="/pet/userResourse/toRegist.action">注册</a>
						</div>
					</c:if>
					<c:if test="${activeUser != null}">
						<!--头像 -->
						<div class="show-nab-link">
							<span>${user.username}</span>
						</div> 
						<div class="show-nab-link">
							<img class="show-nab-img" src="img/p-71979987.jpg"/>
						</div>
						<div class="show-nab-link">
							<a class="show-nab-link" href="/pet/userResourse/doExit.action">登出</a>
						</div>
					</c:if>
				</div>
			</div>
			
			<div class="w-100 px-0 show-body">
				<!--侧边栏 -->
				<div class="show-sider">
					<div id="sidbar" class="sidbar">
						<div class="sidbar-card">
							<div class="sidbar-card-header card-header" >
								<a href="#sdbar-card-1" data-toggle="collapse">first</a>
							</div>
							<div id="sdbar-card-1" class="sdbar-card-body collapse"  data-parent="#sidbar">
								<div class="card-body"> 
									<a href="">first-one</a>
								</div>
								<div class="card-body">
									<a href="">first-two</a>
								</div>
							</div>
						</div>
					</div>
				</div>
				<!-- 数据表格 -->
				<div class="show-context">
					<div id="top-img" class="carousel slide" data-ride="carousel" style="width:400px;margin: 0 auto;">
						<ol class="carousel-indicators"> 
							<li data-target="#top-img" data-slide-to="0" class="active"></li>
							<li data-target="#top-img" data-slide-to="1"></li>
							<li data-target="#top-img" data-slide-to="2"></li>
						</ol>
						<div class="carousel-inner">
							<div class="carousel-item active">
								<img class="d-block w-100" src="${imgPaths[0]}" alt="First slide">
							</div>
							<div class="carousel-item">
								<img class="d-block w-100" src="${imgPaths[1]}" alt="Second slide">
							</div>
							<div class="carousel-item"> 
								<img class="d-block w-100" src="${imgPaths[2]}" alt="Third slide">
							</div>
						</div>
						<a class="carousel-control-prev" href="#top-img" role="button" data-slide="prev">
							<span class="carousel-control-prev-icon" aria-hidden="true"></span>
							<span class="sr-only">Previous</span>
						</a>
						<a class="carousel-control-next" href="#top-img" role="button" data-slide="next">
							<span class="carousel-control-next-icon" aria-hidden="true"></span>
							<span class="sr-only">Next</span>
						</a>
					</div>
					<div class="show-table">
						<div class="table-responsive"> 
							<table class="table table-hover table-striped">
								<thead> 
									<tr> 
										<th colspan="5">
											<h4> 
												目前已有<span class="text-color">${isAdoping}</span>只宠物被成功领养！
												还有<span class="text-color">${isAdoped}</span>只可爱的小动物等着您带回家！
												<c:if test="${!empty user}">
													<c:if test="${user.roleId == 1}">
														<p>欢迎<span class="text-color">${user.username}</span>来到宠物网站！</p>
													</c:if> 
													<c:if test="${user.roleId == 2}">
														欢迎<span class="text-color">${user.role.name}</span>登录网站
													</c:if>
												</c:if>
											</h4>
										</th>
									</tr>  
									<tr>
										<th colspan="5">
											<form action="/pet/petResourse/doRetrievePetByKey.action" method="post"> 
												<div class="table-header">
													<div class="form-group table-search">
														<div class="card bg-primary text-white cells-label">
															<div class="card-body cell-label">宠物类型</div>
														</div> 
														<select id="select-sc-id" class="form-control cells" name="petypeId">
															<option value="0">全部</option>
															<c:forEach items="${pettypes}" var="type">
																<option value="${type.id}">${type.typeName}</option> 
															</c:forEach>
														</select>
													</div>
													<c:if test="${!empty user && user.roleId == 2 }">
														<div class="form-group table-search">
															<div class="card bg-primary text-white cells-label">
																<div class="card-body cell-label">领养状态</div>
															</div> 
															<select id="select-sc-id" class="form-control cells" name="adoptStatus">
																<option value="0">全部</option>
																<option value="1">未领养</option>
																<option value="2">已领养</option>
															</select>
														</div>
														<div class="form-group table-search-btn">
															<a href='#' data-toggle='modal' data-target='#addPet'>添加宠物</a>
															<a href='#' data-toggle='modal' data-target='#addPetType'>添加宠物类型</a>
														</div>
													</c:if>
													
													<div class="form-group table-search"> 
														<button type="submit" class="btn btn-primary">查找</button>
													</div>
												</div>
											</form>
										</th>
									</tr> 
									<tr class="table-title">
										<th width="75px"><input type='checkbox' onclick="toCheckedRec(this.checked)"/><span id="records-checked">全选</span></th>
										<th>序号</th>
										<th>账号</th>
										<th>密码</th>
										<th>角色</th>
										<th>操作</th>
									</tr>
								</thead> 
								<tbody id="context">
									<c:forEach items="${pagination.bean}" var="sysuser" varStatus="v">
										<tr>
											<th><input type='checkbox' name='recordId'/></th>
											<th>${v.count}</th>
											<th>${sysuser.usercode}</th>
											<th>${sysuser.password}</th>
											<th>
												<c:forEach items="${sysuser.roles}" var="role">
													<span>${role.name}</span>
												</c:forEach>
											</th>
											<th>
												<a href='#' data-toggle='modal' data-target='#addPet'></a>
												<a href='#' data-toggle='modal' data-target='#deletePet'>删除</a>
												<a href='#' data-toggle='modal' data-target='#updatePet'>修改</a>
											</th>
										</tr>
									</c:forEach>
								</tbody>
								<tfoot>
									<tr>
										<th colspan="5">
											<div class="pagination-index col-7">
												<ul class="pagination" id="pages_index">
												
													<c:if test="${pagination.pageSize <= 10}">
														<c:set var="begin" value="1"></c:set> 
														<c:set var="end" value="${pagination.pageSize}"></c:set>
													</c:if>
													<c:if test="${pagination.pageSize > 10 }">
														<c:set var="begin" value="${pagination.currentIndex-4}"></c:set>
														<c:set var="end" value="${pagination.currentIndex+5}"></c:set>
														<c:if test="${begin <= 0 }">
															<c:set var="begin" value="1"></c:set>
															<c:set var="end" value="5"></c:set>
														</c:if>
														<c:if test="${end > pagination.pageSize}">
															<c:set var="begin" value="${pagination.pageSize-9}"></c:set>
															<c:set var="end" value="${pagination.pageSize}"></c:set>
														</c:if>
													</c:if>
													<li class="page-item">
														<a class="page-link" href="javascript:showRecord(${pagination.currentIndex-1})">&laquo;</a>
													</li>
													<c:forEach var="item" begin="${begin}" end="${end}" step="1">
														<!-- 当前页不跳转 -->
														<c:if test="${item == pagination.currentIndex}">
															<li class="page-item">
																<a class="page-link" href="javascript:void(0);">${item}</a>
															</li>
														</c:if> 
														<c:if test="${item != pagination.currentIndex}">
															<li class="page-item">
																<a class="page-link" href="http://localhost:8080/pet/petResourse/retrievePetPagination?currentIndex=${item}">${item}</a>
															</li>
														</c:if> 
													</c:forEach>
													<li class="page-item">
														<a class="page-link" href="javascript:showRecord(${pagination.currentIndex+1})">&raquo;</a>
													</li>
													<li class="page-item disabled">
														<a class="page-link" href="#">
															<strong> 
																共<b>${pagination.dbRecordSize}</b>条记录/ 
																共<b>${pagination.pageSize}</b>页
															</strong>
														</a>
													</li>
												</ul>	
											</div>
											<div class="pagination-redirect col-5">
												<div class="form-group">
													<select id="pg-record-size" class="form-control cells fm-lg-input" name="recordShowSize">
														<option value="5">5</option>
														<option value="10">10</option>
														<option value="15">15</option>
													</select>
													<button class="btn btn-primary  cells fm-lg-input disabled" type="button" onclick="">分页</button>
													<select id="pg-index-t" class="form-control cells fm-lg-input" name="pageIndex"></select>
													<button class="btn btn-primary  cells fm-lg-input" type="button" onclick="">跳转</button>
												</div> 
											</div>
										</th>
									</tr>
								</tfoot>
							</table>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
	
<!--模态框-->
<c:if test="${!empty activeUser}">
<div class="modal fade" id="deletePet">
	<div class="modal-dialog">
		<div class="modal-content">
			<div class="modal-header">
				<div class="modal-title">
				</div>
				<button type="button" class="close" data-dismiss="modal">&times;</button>
			</div>
			<div class="modal-body">
				<div class="alert alert-danger" style="text-align: center;">
					<strong>确认删除么</strong>
				</div>
			</div>
			<div class="modal-footer">
				<a href="/pet/petResourse/doDeletePet.action"></a>
				<button type="button" class="btn btn-primary">确认</button>
				<button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
			</div>
		</div>
	</div>
</div>
<div class="modal fade" id="addPet">
	<!-- 添加宠物 -->
	<div class="modal-dialog">
		<div class="modal-content">
			<form action="/pet/petResourse/doCreatePet.action">
				<div class="modal-header">
					<div class="modal-title"></div>
					 <button type="button" class="close" data-dismiss="modal">&times;</button>
				</div>
				
				<div class="modal-body">
					<div class="table-requestable">
							<div class="form-group form-cells">
								<div class="form-cell form-cells-label">
									<label>宠物名字</label>
								</div>
								<div class=" form-cell form-cells-input">
									<input class="form-control" name="petName" />
								</div>
							</div>
							<div class="form-group form-cells">
								<div class="form-cell form-cells-label">
									<label class="form-cell form-cells-label">宠物种类</label>
								</div>
								<div class="form-group table-search">
									<div class="card bg-primary text-white cells-label">
										<div class="card-body cell-label">宠物类型</div>
									</div> 
									<select id="select-sc-id" class="form-control cells" name="petTypeId">
										<c:forEach items="${pettypes}" var="type">
											<option value="${type.id}">${type.typeName}</option> 
										</c:forEach>
									</select>
								</div>
							</div>
							<div class="form-group form-cells">
								<div class="form-cell form-cells-label">
									<label>宠物照片</label>
								</div>
								<div class=" form-cell form-cells-input">
									<input class="form-control" name="picPath" />
								</div>
							</div>
						
					</div>
				</div>
				<div class="modal-footer">
					<button type="submit" class="btn btn-primary">submit</button>
					<button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
				</div>
			</form>
		</div>
	</div>
</div>
<div class="modal fade" id="addPetType">
	<!-- 添加宠物类型 -->
	<div class="modal-dialog">
		<div class="modal-content">
			<form action="/pet/petResourse/doCreatePetType.action">
				<div class="modal-header">
					<div class="modal-title"></div>
					 <button type="button" class="close" data-dismiss="modal">&times;</button>
				</div>
				<div class="modal-body">
					<div class="table-requestable">
						<div class="form-group form-cells">
							<div class="form-cell form-cells-label">
								<label>宠物类型名字</label>
							</div>
							<div class=" form-cell form-cells-input">
								<input class="form-control" name="typeName" />
							</div>
						</div> 
					</div>
				</div>
				<div class="modal-footer">
					<button type="submit" class="btn btn-primary">submit</button>
					<button type="button" class="btn btn-secondary" data-dismiss="modal">关闭</button>
				</div>
			</form>
		</div>
	</div>
</div>
</c:if>
</body>
</html>
