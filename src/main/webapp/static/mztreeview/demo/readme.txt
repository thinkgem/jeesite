MzTreeView 类的一些属性： 
属性名 类型 属性的具体说明 
MzTreeView.nodes 集合 服务器端给树指定数据源时数据存放的对象，具体存放格式如：
MzTreeViewHandle.nodes["parentId_nodeId"] = "text: nodeText; icon: nodeIcon; url: nodeURL; ..."; 
MzTreeView.url 地址字符串 可读写，树缺省的URL，默认值是 # 
MzTreeView.target 目标框架名 可读写，树缺省的链接target，默认值是 _self 
MzTreeView.name 字符 只读，树的实例名，同树实例化时作为参数传入(大小写敏感)：
var Tree = new MzTreeView("Tree"); 
MzTreeView.currentNode 树节点 只读，树当前得到焦点的节点对象 
MzTreeView.icons 集合 树所使用的所有图标存放 
MzTreeView.iconsExpand 集合 树里展开状态的图标存放 
MzTreeView.colors 集合 树里使用到的几个颜色存放 


MzTreeView 在客户端的节点所拥有的属性： 
属性名 属性的具体说明 
node.id 数字文本，节点的ID 
node.parentId 数字文本，节点对应的父节点ID 
node.text 文本，节点的显示文本 
node.hint 文本，节点的注释说明 
node.icon 文本，节点对应的图标 
node.path 文本，节点在树里的绝对路径：0_1_10_34 
node.url 文本，该节点的 URL 
node.target 文本，该节点链接的目标框架名 
node.data 文本，该节点所挂载的数据 
node.method 文本，该节点的点击对应处理语句 
node.parentNode 对象，节点的父节点对象 
node.childNodes 数组，包含节点下所有子节点的数组 
node.sourceIndex 文本，服务器给予的数据里对象的 parentId_nodeId 的组合字符串 
node.hasChild 布尔值，指该节点是否有子节点 
node.isLoad 布尔值，本节点的子节点数据是否已经在客户端初始化 
node.isExpand 布尔值，节点的展开状态 

方法 
MzTreeView 类的一些方法： 
方法名 方法的具体说明 
MzTreeView.toString() 类的默认初始运行 
MzTreeView.buildNode(id) 将该节点的所有下级子节点转换成 HTML 并在网页上体现出来 
MzTreeView.nodeToHTML(node, AtEnd) 将 node 转换成 HTML 
MzTreeView.load(id) 从数据源里加载当前节点下的所有子节点 
MzTreeView.nodeInit(sourceIndex, parentId) 节点的信息初始，从数据源到客户端完整节点的转化 
MzTreeView.focus(id) 聚集到某个节点上 
MzTreeView.expand(id[, sureExpand]) 展开节点（包含下级子节点数据的加载初始化） 
MzTreeView.setIconPath(path) 给节点图片设置正确的路径 
MzTreeView.nodeClick(id) 节点链接点击时同时被触发的点击事件处理方法 
MzTreeView.upperNode() 跳转到当前聚集节点的父级节点 
MzTreeView.lowerNode() 跳转到当前聚集节点的子级节点 
MzTreeView.pervNode() 跳转到当前聚集节点的上一节点 
MzTreeView.nextNode() 跳转到当前聚集节点的下一节点 
MzTreeView.expandAll() 展开所有的树点，在总节点量大于500时这步操作将会比较耗时 

