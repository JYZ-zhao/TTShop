var TT = TAOTAO = {
	checkLogin : function(){
		var _ticket = $.cookie("TT_TOKEN");
		if(!_ticket){
			return ;
		}
		$.ajax({
			
			url : "http://sso.taotao.com/user/token/" + _ticket,
			dataType : "jsonp",
			type : "GET",
			success : function(data){
				if(data.status == 200){
					
					//json字符串不能直接拿值，需将taotaoresult中的data字符串转换为json对象
					var userdata = data.data;
					var user = JSON.parse(userdata);
					
					var username=user.username;
					var html = username + "，欢迎来到淘淘！<a href=\"http://sso.taotao.com/user/logout/"+_ticket+".html\" class=\"link-logout\">[退出]</a>";
					$("#loginbar").html(html);
				}
			}
		});
	}
}

$(function(){
	// 查看是否已经登录，如果已经登录查询登录信息
	TT.checkLogin();
});