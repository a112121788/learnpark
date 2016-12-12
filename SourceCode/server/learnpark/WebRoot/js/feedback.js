$(document).ready(
		function() {
			//弹出一个自定义的对话框 填充回复信息
			$(".table td").click(
					function() {
						var tdSeq = $(this).parent().find("td").index(
								$(this)[0]);
						var trSeq = $(this).parent().parent().find("tr").index(
								$(this).parent()[0]);

						var obj = document.getElementById("feedback_table");
						//alert("第" + (trSeq + 1) + "行，第" + (tdSeq + 1) + "列");
						//alert(obj.rows[trSeq].cells[tdSeq].innerText);
						var email=obj.rows[trSeq+1].cells[1].innerText;
						var yijian=obj.rows[trSeq+1].cells[2].innerText;
						var replay=obj.rows[trSeq+1].cells[3].innerText;
						document.getElementById("email").innerHTML=email;
						document.getElementById("yijian").innerHTML=yijian;
						document.getElementById("opinion_email").value=email;
						document.getElementById("opinion_yijian").value=yijian;
					});
		})
//上传信息
function upload1(){
	document.getElementById("opinion_reply").value=document.getElementById("reply").value;
}