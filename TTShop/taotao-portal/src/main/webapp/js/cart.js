var TTCart = {
	load : function(){ // 加载购物车数据
		
	},
	itemNumChange : function(){
		$(".increment").click(function(){//＋
			var _thisInput = $(this).siblings("input");
			_thisInput.val(eval(_thisInput.val()) + 1);
			$.post("/cart/add/"+_thisInput.attr("itemId")+".html?num=1",function(data){
				TTCart.refreshTotalPrice();
			});
		});
		$(".decrement").click(function(){//-
			var _thisInput = $(this).siblings("input");
			if(eval(_thisInput.val()) == 1){
				return ;
			}
			_thisInput.val(eval(_thisInput.val()) - 1);
			$.post("/cart/add/"+_thisInput.attr("itemId")+".html?num=-1",function(data){
				TTCart.refreshTotalPrice();
			});
		});
		$(".quantity-form .quantity-text").rnumber(1);//限制只能输入数字
		$(".quantity-form .quantity-text").change(function(){
			var _thisInput = $(this);
			$.post("/cart/update/"+_thisInput.attr("itemId")+"/"+_thisInput.val()+".html",function(data){
				TTCart.refreshTotalPrice();
			});
		});
	},
	refreshTotalPrice : function(){ //重新计算总价
		var total = 0;
		$(".quantity-form .quantity-text").each(function(i,e){
			var _this = $(e);
			total += (eval(_this.attr("itemPrice")) * 10000 * eval(_this.val())) / 10000;
		});
		$(".totalSkuPrice").html(new Number(total/100).toFixed(2)).priceFormat({ //价格格式化插件
			 prefix: '￥',
			 thousandsSeparator: ',',
			 centsLimit: 2
		});
	}
};

$(function(){
	TTCart.load();
	TTCart.itemNumChange();
});

$(function(){  
    //实现全选与反选  
  /*  $(".jdcheckbox").click(function() {   
        if (this.checked){    
            $("input[name='checkItem']:checkbox").each(function(){   
                  $(this).attr("checked", true);    
            });  
        } else {     
            $("input[name='checkItem']:checkbox").each(function() {     
                  $(this).attr("checked", false);    
            });  
        }    
    });  */
   
    $(".jdcheckbox").click(function() {   
        
        var flag=this.checked;  
        $(":checkbox[name='checkItem']").attr('checked',flag);  
    });  
      
    //而且还实现了:当其中不勾选某一个选项的时候,则去掉全选复选框  
    $(":checkbox[name='checkItem']").click(function(){  
          
        $(".jdcheckbox").attr('checked',  
            $(":checkbox[name='checkItem']").length==$(":checkbox[name='checkItem']:checked").length);  
    });   
    
    
    
    
    
    
    $('.deleteItem').live('click',function(){  
        var ids=[];  
          
        //获取被选中的checkbox,然后将其id塞进ids中  
        $(":checkbox[name='checkItem']:checked").each(function(){   
            ids.push($(this).attr('data-option'));  
        });  
        //采用逗号隔开塞进ids的每个id  
        var delIds=ids.join(",");  
        //console.log(ids+" -- "+ids.length);  
        if(delIds.length==0){  
            alert('请选择需要删除的数据');  
            return false;  
        }  
          
        if(confirm('是否批量删除选中的数据?')){  
            var url=this.href;  
            console.log(delIds);  
            $.post(url,{delIds:delIds},function(requestData){  
                window.location.reload(true);  
            });  
        }  
          
        return false;  
    });  
      
});  






