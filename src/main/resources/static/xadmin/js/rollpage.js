function page_nav(frm,a){
	console.log("进入了page方法")
	frm.pageIndex.value = a.getAttribute("dataCurrentPageNo");
	frm.submit();

}

function jump_to(frm,num){
    //alert(num);
	//验证用户的输入
	console.log("进入了jump方法");
	var regexp=/^[1-9]\d*$/;
	var toTalPage = document.getElementById("toTalPage").value;
	//alert(toTalPage);
	console.log(num);
	if(!regexp.test(num)){
		alert("请输入大于0的正整数！");
		return false;
	}else if((num-toTalPage) > 0){
		alert("请输入小于总页数的页码");
		return false;
	}else{
		frm.pageIndex.value = num;
		frm.submit();
	}
}