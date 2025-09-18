<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8" />
    <title></title>
</head>
<body>
<!--
      两种方法
1 用符号代替: > gt , >=  gte  ,< lt  , <= lte
2 加括号 #if(x>y)
-->

   ${name}

   <#if sex==0>
       男
   <#elseif sex==2>
       女
   <#else>
       其他

   </#if>
   <#list listResult as user>
        ${user}
   </#list>
</body>
</html>
