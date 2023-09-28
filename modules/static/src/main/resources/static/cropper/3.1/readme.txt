
1.container 容器
2.canvas 图片
3.crop  裁剪框

# option相关参数说明：

## viewMode 显示模式

	Type: Number
	Default: 0
	Options:
		0: the crop box is just within the container    裁剪框只能在 1内移动
		1: the crop box should be within the canvas   裁剪框 只能在  2图片内移动
		2: the canvas should not be within the container  2图片 不全部铺满1 （即缩小时可以有一边出现空隙）
		3: the container should be within the canvas  2图片 全部铺满1 （即 再怎么缩小也不会出现空隙）

## dragMode  拖动模式

	Default: 'crop'
	Options:
		'crop': create a new crop box  当鼠标 点击一处时根据这个点重新生成一个 裁剪框
		'move': move the canvas    可以拖动图片
		'none': do nothing  图片就不能拖动了
		
	Define the dragging mode of the cropper.
	toggleDragModeOnDblclick   默认true .是否允许 拖动模式 “crop” 跟“move” 的切换状态。。即当点下为crop 模式，如果未松开拖动这时就是“move”模式。放开后又为“crop”模式
	preview  截图的显示位置   型：String(jQuery选择器)，默认值''
	responsive ：类型：Boolean，默认值true。是否在窗口尺寸改变的时候重置cropper。
	checkImageOrigin：类型：Boolean，默认值true。默认情况下，插件会检测图片的源，如果是跨域图片，图片元素会被添加crossOrigin class，并会为图片的url添加一个时间戳来使getCroppedCanvas变为可用。添加时间戳会使图片重新加载，以使跨域图片能够使用getCroppedCanvas。在图片上添加crossOrigin class会阻止在图片url上添加时间戳，及图片的重新加载。
	background：类型：Boolean，默认值true。是否在容器上显示网格背景。 要想改背景，我是直接改，cropper.css样式中的 cropper-bg

## canvas（图片）相关

	movable：类型：Boolean，默认值true。是否允许移动图片
	rotatable：类型：Boolean，默认值true。是否允许旋转图片。
	scalable  默认 true 。 是否允许扩展图片。（暂时不知道干嘛用）
	zoomable 默认true, 石头允许缩放图片。
	zoomOnWheel 默认 true 是否允许鼠标滚轴 缩放图片
	zoomOnTouch 默认true 是否允许触摸缩放图片（触摸屏上两手指操作。）
	wheelZoomRatio 默认0.1 师表滚轴缩放图片比例。即滚一下。图片缩放多少。如 0.1 就是图片的10%

## crop(裁剪框)相关

	aspectRatio 裁剪框比例  默认NaN   例如：: 1 / 1,//裁剪框比例 1：1
	modal：类型：Boolean，默认值true。是否在剪裁框上显示黑色的模态窗口。
	cropBoxMovable :默认true ,是否允许拖动裁剪框cropBoxResizable :默认 true,//是否允许拖动 改变裁剪框大小
	autoCrop：类型：Boolean，默认值true。是否允许在初始化时自动出现裁剪框。autoCropArea：类型：Number，默认值0.8（图片的80%）。0-1之间的数值，定义自动剪裁框的大小。highlight：类型：Boolean，默认值true。是否在剪裁框上显示白色的模态窗口。
	guides：类型：Boolean，默认值true。是否在剪裁框上显示虚线。
	center:  默认true  是否显示裁剪框 中间的+ 
	restore :  类型：Boolean，默认值true  是否调整窗口大小后恢复裁剪区域。

## 大小相关

	minContainerWidth：类型：Number，默认值200。 容器的最小宽度。
	minContainerHeight：类型：Number，默认值100。 容器的最小高度。
	minCanvasWidth：类型：Number，默认值0。 canvas 的最小宽度（image wrapper）。
	minCanvasHeight：类型：Number，默认值0。 canvas 的最小高度（image wrapper）。
	
	监听触发的方法build：类型：Function，默认值null。 build.cropper事件的简写方式。 
	控件初始化前执行built：类型：Function，默认值null。 built.cropper事件的简写方式。  
	
	空间初始化完成后执行dragstart：类型：Function，默认值null。 dragstart.cropper事件的简写方式。 
	拖动开始执行dragmove：类型：Function，默认值null。 dragmove.cropper事件的简写方式。
	拖动移动中执行dragend：类型：Function，默认值null。dragend.cropper事件的简写方式。
	拖动结束执行zoomin：类型：Function，默认值null。zoomin.cropper事件的简写方式。
	缩小执行zoomout：类型：Function，默认值null。zoomout.cropper事件的简写方式。 

