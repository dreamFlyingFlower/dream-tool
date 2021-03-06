$(function() {
	$("#jqGrid").jqGrid({
		url : 'generator/getTableinfos',
		datatype : "json",
		colModel : [ {
			label : '表名',
			name : 'tableName',
			width : 100,
			key : true
		}, {
			label : 'Engine',
			name : 'ENGINE',
			width : 70
		}, {
			label : '表备注',
			name : 'tableComment',
			width : 100
		}, {
			label : '创建时间',
			name : 'createtime',
			width : 100
		} ],
		viewrecords : true,
		height : 385,
		rowNum : 10,
		rowList : [ 10, 30, 50, 100, 200 ],
		rownumbers : true,
		rownumWidth : 25,
		autowidth : true,
		multiselect : true,
		pager : "#jqGridPager",
		jsonReader : {
			root : "data",
			page : "pageIndex",
			total : "totalPage",
			records : "total"
		},
		prmNames : {
			page : "pageIndex",
			rows : "pageSize",
			order : "pageDirection"
		},
		gridComplete : function() {
			// 隐藏grid底部滚动条
			$("#jqGrid").closest(".ui-jqgrid-bdiv").css({
				"overflow-x" : "hidden"
			});
		}
	});
});

var vm = new Vue({
	el : '#rrapp',
	data : {
		q : {
			tableName : null
		}
	},
	methods : {
		query : function() {
			$("#jqGrid").jqGrid('setGridParam', {
				postData : {
					'tableName' : vm.q.tableName
				},
				page : 1
			}).trigger("reloadGrid");
		},
		generator : function() {
			var tableNames = getSelectedRows();
			if (tableNames == null) {
				return;
			}
			location.href = "generator/code?tables=" + tableNames.join();
		}
	}
});