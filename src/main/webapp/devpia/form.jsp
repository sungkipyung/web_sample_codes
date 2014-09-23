<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<jsp:include page="/include/head.jsp">
  <jsp:param name="title" value="Hello World" />
</jsp:include>
<body>
<style type="text/css">
body {
  padding-top: 50px;
}
.starter-template {
  padding: 40px 15px;
  text-align: center;
}
.input-group-addon {
  width:150px;
}
.form-control {
  width: 1100px;
}
textarea {
  width: 1100px;
  height:100px;
  resize: none;
}
</style>
  <div class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
      <div class="navbar-header">
        <button type="button" class="navbar-toggle"
          data-toggle="collapse" data-target=".navbar-collapse">
          <span class="sr-only">Toggle navigation</span> <span
            class="icon-bar"></span> <span class="icon-bar"></span> <span
            class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="#">Project name</a>
      </div>
      <div class="collapse navbar-collapse">
        <ul class="nav navbar-nav">
          <li class="active"><a href="#">Home</a></li>
          <li><a href="#about">About</a></li>
          <li><a href="#contact">Contact</a></li>
        </ul>
      </div>
      <!--/.nav-collapse -->
    </div>
  </div>

  <div class="container">

    <div class="starter-template">
      <h1>Bootstrap starter template</h1>
      <p class="lead">
        Use this document as a way to quickly start any new project.<br>
        All you get is this text and a mostly barebones HTML document.
      </p>
    </div>
    <div>
    <form id="submitForm" action="/devpia/sendMessage" method="post">
    <div class="input-group">
      <span class="input-group-addon">Devpia ID</span>
      <input type="text" name="id" value="" class="form-control" placeholder="Devpia ID">
    </div>
    <div class="input-group">
      <span class="input-group-addon">Devpia PW</span>
      <input type="password" name="pw" value="" class="form-control" placeholder="Devpia PW">
    </div>
    <div class="input-group">
      <span class="input-group-addon">Receiver ID</span>
      <input type="text" name="rcvrIds" class="form-control" placeholder="Username">
    </div>
    <div class="input-group">
      <span class="input-group-addon">message</span>
      <textarea class="form-control" name="message" style="width: 1000px; height:450px;"></textarea>
    </div>
    </form>
    <a class="btn btn-lg btn-primary" href="javascript:void(0)" onclick="submitForm()" role="button">View navbar docs »</a>
    </div>
  </div>
  <!-- /.container -->

  <jsp:include page="/include/footer.jsp" />
<script type="text/javascript">
function submitForm() {
	document.forms["submitForm"].submit();
}
</script>
</body>
</html>