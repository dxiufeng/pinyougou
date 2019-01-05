 //控制层 
app.controller('itemCatController' ,function($scope,$controller   ,itemCatService){	
	
	$controller('baseController',{$scope:$scope});//继承

    //分页控件配置
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function () {
            $scope.reloadList();//重新加载
        }
    };

    //重新加载列表 数据
    $scope.reloadList = function () {
        //切换页码

        $scope.search($scope.myId,$scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    };
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		itemCatService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	};
	
	//分页
	$scope.findPage=function(page,rows){			
		itemCatService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){				
		itemCatService.findOne(id).success(
			function(response){
				$scope.entity= response;					
			}
		);				
	}
	
	//保存 
	$scope.save=function(){				
		var serviceObject;//服务层对象  				
		if($scope.entity.id!=null){//如果有ID
			serviceObject=itemCatService.update( $scope.entity ); //修改  
		}else{
			serviceObject=itemCatService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					//重新查询 
		        	$scope.reloadList();//重新加载
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		itemCatService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
					$scope.selectIds=[];
				}						
			}		
		);				
	}
	
	$scope.searchEntity={};//定义搜索对象 


	$scope.myId;//当加载分类导航栏是后会刷新一遍,用于记录当前的id,并刷新,传入
    //第一次加载分类管理中内容  //触发方法后执行查询和分页效果
	$scope.search=function(parentId,page,rows){
        $scope.myId=parentId;
		itemCatService.findByParentId(parentId,page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	};

	//定义分层级别
	$scope.grade=1;
	$scope.setGrade=function (index) {
        $scope.grade=index;
    };
	//定义面包屑逻辑
	$scope.selectList=function (entity, page, rows) {

		//第一级别,也就是第一级分类目录
		if ($scope.grade==1){
			$scope.entity_1=null;
			$scope.entity_2=null;
		}

        //第二级别,也就是第二级分类目录
		if ($scope.grade==2){
            $scope.entity_1=entity;
			$scope.entity_2=null
		}

		if ($scope.grade==3){
			$scope.entity_2=entity;
		}
        $scope.search(entity.id,page,rows)


    }
});	
