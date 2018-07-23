var app1 = new Vue({
	el:'#app1',
	data:{
		msg:'声明式渲染'
	}
});

var app2 = new Vue({
	el:'#app2',
	data:function(){
		return {msg:'页面加载于：'+new Date().toLocaleString()};
	}
});

var app3 = new Vue({
	el:'#app3',
	data:{
		btnMsg:'隐藏图片',
		showImg:true,
		srcUrl:'../res/img/Chrysanthemum.jpg'
	},
	methods:{
		toggle:function(){
			this.showImg = !this.showImg;
			if(this.showImg){
				this.btnMsg='隐藏图片';
			}else{
				this.btnMsg='显示图片';
			}
		}
	}
});

var app4 = new Vue({
	el:'#app4',
	data:{
		items:[
			{id:1,text:'学习javascript'},
			{id:2,text:'学习Vue'},
			{id:3,text:'整个牛项目'}
		]
	}
});

var app5 = new Vue({
	el:'#app5',
	data:{
		msg:'Hello Vue.js!'
	},
	methods:{
		reverse:function(){
			this.msg = this.msg.split('').reverse().join('');
		}
	}
});

var app6 = new Vue({
	el:'#app6',
	data:{
		msg:'请输入内容'
	}
	
});