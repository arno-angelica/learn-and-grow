<head>
<jsp:directive.include
	file="/WEB-INF/jsp/prelude/include-head-meta.jspf" />
<title>My Home Page</title>

	<style type="text/css">
		pre {
			outline: 1px solid #ccc;
		}

		.string {
			color: green;
		}

		.number {
			color: darkorange;
		}

		.boolean {
			color: blue;
		}

		.null {
			color: magenta;
		}

		.key {
			color: red;
		}
	</style>
</head>
<body>
	<pre id="jsonShow"></pre>
	<div class="container-lg">
		<button class="btn btn-lg btn-primary btn-block" onclick="showMBean('com.arno.grow.user.web.management:type=JMXDatasourceManager')">获取UserMBean</button>
		<button class="btn btn-lg btn-primary btn-block" onclick="showMBean('com.arno.grow.user.web.management:type=UserManager')">获取DataSourceMBean</button><br><br>
		<label class="sr-only">请输入类型(string/integer/double/float/byte/bigDecimal/bigInteger/long/short)<label><br>
		<input name="type" id="type" value="" type="text">
		<button class="btn btn-lg btn-primary btn-block" onclick="getApplication()">获取ApplicationName</button><br><br>

		<button class="btn btn-lg btn-primary btn-block" onclick="doget()">获取全部用户</button>
		<button class="btn btn-lg btn-primary btn-block" onclick="doDelete()">清空全部用户</button><br>
		<a href="goRegister">注册</a><br>
	</div>
	<div id="dataDetail">
		<table id = "table" border="1" cellspacing="0">
		</table>
	</div>
</body>

<script src="https://apps.bdimg.com/libs/jquery/2.1.4/jquery.min.js"></script>
<script>

	function getApplication() {
		var value = $('#type').val();
		$.ajax({
			url: '/user/getApplication',
			type: "post",
			dataType: "json",
			data:{"type": value},
			contentType: "application/x-www-form-urlencoded",
			success: function (data) {
				$('#jsonShow').html(jsonShowFn(data));
			}
		});
	}

	function jsonShowFn(json) {
		if (typeof json != 'string') {
			json = JSON.stringify(json, undefined, 2);
		}
		json = json.replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;');
		return json.replace(/("(\\u[a-zA-Z0-9]{4}|\\[^u]|[^\\"])*"(\s*:)?|\b(true|false|null)\b|-?\d (?:\.\d*)?(?:[eE][ \-]?\d )?)/g, function (match) {
			var cls = 'number';
			if (/^"/.test(match)) {
				if (/:$/.test(match)) {
					cls = 'key';
				} else {
					cls = 'string';
				}
			} else if (/true|false/.test(match)) {
				cls = 'boolean';
			} else if (/null/.test(match)) {
				cls = 'null';
			}
			return '<span class="' + cls + '">' + match + '</span>';
		});
	}

	function showMBean(bean) {
		$.ajax({
			url: '/jolokia/read/' + bean,
			type: "get",
			dataType: "json",
			contentType: "application/x-www-form-urlencoded",
			success: function (data) {
				$('#jsonShow').html(jsonShowFn(data));
			}
		});
	}

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