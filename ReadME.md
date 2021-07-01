### 技术栈
    springboot Mybatis-plus lombok JWT SpringSecurity Mysql Swagger 
### 项目结构
* eoa-generator：Mybatis代码生成器
+ eoa-server：核心服务
### 数据库规范
>  数据库不建议超过4张表进行多表查询
### 代码规范
> 参考Aibaba命名规范
> 
> controller：路由转换
> 
> service：具体实现
> 
> mapper：数据库接口

##### 小提醒
> resource下的mapper.xml要和mapper下的mapper.java的方法和参数一致， 但controller调用service接口时，参数可以和mapper不一致，因为serviceImpl里面仅调用mapper接口
> 
> 例子：getMenusByAdminId
> 