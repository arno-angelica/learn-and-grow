<head>
    <jsp:directive.include file="/WEB-INF/jsp/prelude/include-head-meta.jspf"/>
    <title>Register</title>
    <style>
        .bd-placeholder-img {
            font-size: 1.125rem;
            text-anchor: middle;
            -webkit-user-select: none;
            -moz-user-select: none;
            -ms-user-select: none;
            user-select: none;
        }

        @media (min-width: 768px) {
            .bd-placeholder-img-lg {
                font-size: 3.5rem;
            }
        }
    </style>
</head>
<body>
<div class="container">
    <form class="form-signin">
        <h1 class="h3 mb-3 font-weight-normal">注册</h1>
        <label for="inputEmail" class="sr-only">请输出电子邮件</label>
        <input type="email" id="inputEmail"  name="email" class="form-control" placeholder="请输入电子邮件" required autofocus>
        <br>
        <label for="inputPassword" class="sr-only">Password</label>
        <input type="password" id="inputPassword" name="password" class="form-control" placeholder="请输入密码" required>
        <br>
        <label for="inputPassword" class="sr-only">昵称</label>
        <input type="text" id="name" class="form-control" name="name" placeholder="请输入昵称" required>
        <br>
        <label for="inputPassword" class="sr-only">手机号</label>
        <input type="text" id="phoneNumber" class="form-control" name="phoneNumber" placeholder="请输入手机号" required>
        <br>
        <button class="btn btn-lg btn-primary btn-block" type="button" onclick="register()">Register
        </button>
        <p class="mt-5 mb-3 text-muted">&copy; 2017-2021</p>
    </form>
</div>
</body>

<script>
    register = function() {
        var email = document.getElementById("inputEmail").value;
        var password = document.getElementById("inputPassword").value;
        var name = document.getElementById("name").value;
        var phone = document.getElementById("phoneNumber").value;
        var sendData = {"email":email, "password":password,"name":name,"phoneNumber":phone};
        var httpRequest = new XMLHttpRequest();
        httpRequest.open('POST', "/user/register", true);
        httpRequest.setRequestHeader("Content-type", "application/json");
        httpRequest.send(JSON.stringify(sendData));
        /**
         * 获取数据后的处理程序
         */
        httpRequest.onreadystatechange = function () {
            if (httpRequest.readyState === 4 && httpRequest.status === 200) {
                var json = httpRequest.responseText;
                const obj = JSON.parse(json);
                if (obj.code === 10000) {
                    alert(obj.message);
                    window.location.href="home";
                } else {
                    alert(obj.message)
                }
            }
        };
    }

</script>