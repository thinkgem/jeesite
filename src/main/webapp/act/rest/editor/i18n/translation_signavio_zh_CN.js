ORYX.I18N.PropertyWindow.dateFormat = "y/m/d";

ORYX.I18N.View.East = "属性";
ORYX.I18N.View.West = "建模元素";

ORYX.I18N.Oryx.title	= "流程设计器";
ORYX.I18N.Oryx.pleaseWait = "请稍后，流程设计器正在加载...";
ORYX.I18N.Edit.cutDesc = "剪切";
ORYX.I18N.Edit.copyDesc = "复制";
ORYX.I18N.Edit.pasteDesc = "粘贴";
ORYX.I18N.ERDFSupport.noCanvas = "当前流程图上没有节点!";
ORYX.I18N.ERDFSupport.noSS = "这个流程设计器中没有流程模板集定义!";
ORYX.I18N.ERDFSupport.deprText = "导出  eRDF 不推荐，未来版本将不提供支持. 如果仍要导出则以 JSON 格式导出，是否继续?";
ORYX.I18N.Save.pleaseWait = "正在保存<br/>请稍等...";

ORYX.I18N.Save.saveAs = "另存为...";
ORYX.I18N.Save.saveAsDesc = "另存为...";
ORYX.I18N.Save.saveAsTitle = "另存为...";
ORYX.I18N.Save.savedAs = "复制保存";
ORYX.I18N.Save.savedDescription = "复制保存";
ORYX.I18N.Save.notAuthorized = "你没有权限访问."
ORYX.I18N.Save.transAborted = "请求超时，请检查你的网络连接.";
ORYX.I18N.Save.noRights = "保存路径没有权限，请检查文件夹权限。.";
ORYX.I18N.Save.comFailed = "连接流程设计器失败. 请检查你的网络连接.";
ORYX.I18N.Save.failed = "保存失败. 请再试一次.";
ORYX.I18N.Save.exception = "保存中出现异常. 请再试一次.";
ORYX.I18N.Save.retrieveData = "正在检索数据，请稍等.";

/** New Language Properties: 10.6.09*/
if(!ORYX.I18N.ShapeMenuPlugin) ORYX.I18N.ShapeMenuPlugin = {};
ORYX.I18N.ShapeMenuPlugin.morphMsg = "变换形状";
ORYX.I18N.ShapeMenuPlugin.morphWarningTitleMsg = "变换形状";
ORYX.I18N.ShapeMenuPlugin.morphWarningMsg = "有孩子形状不能包含在转换后的元素中.<br/>是否想改变?";

if (!Signavio) { var Signavio = {}; }
if (!Signavio.I18N) { Signavio.I18N = {} }
if (!Signavio.I18N.Editor) { Signavio.I18N.Editor = {} }

if (!Signavio.I18N.Editor.Linking) { Signavio.I18N.Editor.Linking = {} }
Signavio.I18N.Editor.Linking.CreateDiagram = "创建一个新流程模型";
Signavio.I18N.Editor.Linking.UseDiagram = "使用现有";
Signavio.I18N.Editor.Linking.UseLink = "使用WEB连接";
Signavio.I18N.Editor.Linking.Close = "关闭";
Signavio.I18N.Editor.Linking.Cancel = "取消";
Signavio.I18N.Editor.Linking.UseName = "模型名称";
Signavio.I18N.Editor.Linking.UseNameHint = "替换当前的建模元素的名称({type})的名称链接图。";
Signavio.I18N.Editor.Linking.CreateTitle = "创建连接";
Signavio.I18N.Editor.Linking.AlertSelectModel = "你必须选择一个模型.";
Signavio.I18N.Editor.Linking.ButtonLink = "连接模型图";
Signavio.I18N.Editor.Linking.LinkNoAccess = "你不能访问此模型.";
Signavio.I18N.Editor.Linking.LinkUnavailable = "模型图示不可用的.";
Signavio.I18N.Editor.Linking.RemoveLink = "移除连接";
Signavio.I18N.Editor.Linking.EditLink = "编辑连接";
Signavio.I18N.Editor.Linking.OpenLink = "打开连接";
Signavio.I18N.Editor.Linking.BrokenLink = "连接不可用!";
Signavio.I18N.Editor.Linking.PreviewTitle = "预览";

if(!Signavio.I18N.Glossary_Support) { Signavio.I18N.Glossary_Support = {}; }
Signavio.I18N.Glossary_Support.renameEmpty = "没有字典条目";
Signavio.I18N.Glossary_Support.renameLoading = "搜索中...";

/** New Language Properties: 08.09.2009*/
if(!ORYX.I18N.PropertyWindow) ORYX.I18N.PropertyWindow = {};
ORYX.I18N.PropertyWindow.oftenUsed = "主要属性";
ORYX.I18N.PropertyWindow.moreProps = "更多属性";

ORYX.I18N.PropertyWindow.btnOpen = "打开";
ORYX.I18N.PropertyWindow.btnRemove = "移除";
ORYX.I18N.PropertyWindow.btnEdit = "编辑";
ORYX.I18N.PropertyWindow.btnUp = "向上移动";
ORYX.I18N.PropertyWindow.btnDown = "向下移动";
ORYX.I18N.PropertyWindow.createNew = "创建";

if(!ORYX.I18N.PropertyWindow) ORYX.I18N.PropertyWindow = {};
ORYX.I18N.PropertyWindow.oftenUsed = "主要属性";
ORYX.I18N.PropertyWindow.moreProps = "更多属性";
ORYX.I18N.PropertyWindow.characteristicNr = "成本及资源分析";
ORYX.I18N.PropertyWindow.meta = "自定义属性";

if(!ORYX.I18N.PropertyWindow.Category){ORYX.I18N.PropertyWindow.Category = {}}
ORYX.I18N.PropertyWindow.Category.popular = "主要属性";
ORYX.I18N.PropertyWindow.Category.characteristicnr = "成本及资源分析";
ORYX.I18N.PropertyWindow.Category.others = "更多属性";
ORYX.I18N.PropertyWindow.Category.meta = "自定义属性";

if(!ORYX.I18N.PropertyWindow.ListView) ORYX.I18N.PropertyWindow.ListView = {};
ORYX.I18N.PropertyWindow.ListView.title = "编辑: ";
ORYX.I18N.PropertyWindow.ListView.dataViewLabel = "已经存在的条目.";
ORYX.I18N.PropertyWindow.ListView.dataViewEmptyText = "没有条目.";
ORYX.I18N.PropertyWindow.ListView.addEntryLabel = "添加新条目";
ORYX.I18N.PropertyWindow.ListView.buttonAdd = "添加";
ORYX.I18N.PropertyWindow.ListView.save = "保存";
ORYX.I18N.PropertyWindow.ListView.cancel = "取消";

if(!Signavio.I18N.Buttons) Signavio.I18N.Buttons = {};
Signavio.I18N.Buttons.save		= "保存";
Signavio.I18N.Buttons.cancel 	= "取消";
Signavio.I18N.Buttons.remove	= "移除";

if(!Signavio.I18N.btn) {Signavio.I18N.btn = {};}
Signavio.I18N.btn.btnEdit = "编辑";
Signavio.I18N.btn.btnRemove = "移除";
Signavio.I18N.btn.moveUp = "向上移动";
Signavio.I18N.btn.moveDown = "向下移动";

if(!Signavio.I18N.field) {Signavio.I18N.field = {};}
Signavio.I18N.field.Url = "URL";
Signavio.I18N.field.UrlLabel = "标签";
