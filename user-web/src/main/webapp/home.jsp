<head>
<jsp:directive.include
	file="/WEB-INF/jsp/prelude/include-head-meta.jspf" />
<title>My Home Page</title>
</head>
<body>
	<div class="container-lg">
		<a href="goRegister">注册</a><br><br><br>
		<button class="btn btn-lg btn-primary btn-block" onclick="doget()">获取全部用户</button><br><br><br>
		<button class="btn btn-lg btn-primary btn-block" onclick="doDelete()">清空全部用户</button><br>
	</div>
	<div id="dataDetail">
		<table id = "table" border="1" cellspacing="0">
		</table>
	</div>
</body>


<script>
	doDelete = function () {
		var httpRequest = new XMLHttpRequest();
		httpRequest.open('POST', "/user/deleteAll", true);
		httpRequest.setRequestHeader("Content-type", "application/json");
		httpRequest.send();
		/**
		 * 获取数据后的处理程序
		 */
		httpRequest.onreadystatechange = function () {
			if (httpRequest.readyState === 4 && httpRequest.status === 200) {
				var json = httpRequest.responseText;
				alert(json)
				window.location.href="home";
			}
		};
	}
	var headArray = [];
	doget = function () {
		var httpRequest = new XMLHttpRequest();
		httpRequest.open('POST', "/user/getAll", true);
		httpRequest.setRequestHeader("Content-type", "application/json");
		httpRequest.send();
		/**
		 * 获取数据后的处理程序
		 */
		httpRequest.onreadystatechange = function () {
			if (httpRequest.readyState === 4 && httpRequest.status === 200) {
				var json = httpRequest.responseText;
				const obj = JSON.parse(json)
				console.log(obj.data);
				if (obj.data != null && obj.data !== 'undefined') {
					appendTable(obj.data);
				}
			}
		};
	}

	function parseHead(oneRow) {
		for (var i in oneRow) {
			headArray[headArray.length] = i;
		}
	}

	function appendTable(json) {
		console.log(json)
		parseHead(json[0]);
		var table = document.getElementById("table");
		var thead = document.createElement("tr");
		for (var count = 0; count < headArray.length; count++) {
			var td = document.createElement("th");
			td.innerHTML = headArray[count];
			thead.appendChild(td);
		}
		table.appendChild(thead);
		for (var tableRowNo = 0; tableRowNo < json.length; tableRowNo++) {
			var tr = document.createElement("tr");
			for (var headCount = 0; headCount < headArray.length; headCount++) {
				var cell = document.createElement("td");
				cell.innerHTML = json[tableRowNo][headArray[headCount]];
				tr.appendChild(cell);
			}
			table.appendChild(tr);
		}
		div.appendChild(table);
	}
</script>