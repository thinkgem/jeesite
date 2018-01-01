package com.jeesite.common.utils.word;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

/**
 * 利用POI进行Word文件相关的操作，针对docx形式的封装
 * @author    
 * <p>Modification History:</p>
 * <p>Date       Author      Description</p>
 * <p>------------------------------------------------------------------</p>
 * <p> </p>
 */
public class BookMarks {

	/** 保存Word文件中定义的标签  **/
	private HashMap<String, BookMark> _bookmarks = null;

	/** 
	 * 构造函数，用以分析文档，解析出所有的标签
	 * @param document  Word OOXML document instance. 
	 */
	public BookMarks(XWPFDocument document) {

		//初始化标签缓存
		this._bookmarks = new HashMap<String, BookMark>();

		// 首先解析文档普通段落中的标签 
		this.procParaList(document.getParagraphs());

		//利用繁琐的方法，从所有的表格中得到得到标签，处理比较原始和简单
		List<XWPFTable> tableList = document.getTables();

		for (XWPFTable table : tableList) {
			//得到表格的列信息
			List<XWPFTableRow> rowList = table.getRows();
			for (XWPFTableRow row : rowList) {
				//得到行中的列信息
				List<XWPFTableCell> cellList = row.getTableCells();
				for (XWPFTableCell cell : cellList) {
					//逐个解析标签信息
					//this.procParaList(cell.getParagraphs(), row);
					this.procParaList(cell);
				}
			}
		}
	}

	/**
	 * 根据标签名称，获得标签的相关定义，如果不存在，则返回空
	 * @param bookmarkName   标签名称
	 * @return    返回封装好的对象
	 */
	public BookMark getBookmark(String bookmarkName) {
		BookMark bookmark = null;
		if (this._bookmarks.containsKey(bookmarkName)) {
			bookmark = this._bookmarks.get(bookmarkName);
		}
		return bookmark;
	}

	/** 
	 * 得到所有的标签信息集合
	 * 
	 * @return 缓存的标签信息集合 
	 */
	public Collection<BookMark> getBookmarkList() {
		return (this._bookmarks.values());
	}

	/** 
	 * 返回文档中的标签名称迭代器
	 * @return  由Map KEY 转换的迭代器
	 */
	public Iterator<String> getNameIterator() {
		return (this._bookmarks.keySet().iterator());
	}

	private void procParaList(XWPFTableCell cell) {
		List<XWPFParagraph> paragraphList = cell.getParagraphs();

		for (XWPFParagraph paragraph : paragraphList) {
			//得到段落中的标签标记
			List<CTBookmark> bookmarkList = paragraph.getCTP().getBookmarkStartList();
			for (CTBookmark bookmark : bookmarkList) {
				this._bookmarks.put(bookmark.getName(), new BookMark(bookmark, paragraph, cell));
			}
		}
	}

	/**
	 * 解析表格中的标签
	 * @param paragraphList   传入的段落列表
	 * @param tableRow   对应的表格行对象 
	 */
	@SuppressWarnings("unused")
	private void procParaList(List<XWPFParagraph> paragraphList, XWPFTableRow tableRow) {

		NamedNodeMap attributes = null;
		Node colFirstNode = null;
		Node colLastNode = null;
		int firstColIndex = 0;
		int lastColIndex = 0;

		//循环判断，解析段落中的标签
		for (XWPFParagraph paragraph : paragraphList) {
			//得到段落中的标签标记
			List<CTBookmark> bookmarkList = paragraph.getCTP().getBookmarkStartList();

			for (CTBookmark bookmark : bookmarkList) {
				// With a bookmark in hand, test to see if the bookmarkStart tag 
				// has w:colFirst or w:colLast attributes. If it does, we are 
				// dealing with a bookmarked table cell. This will need to be 
				// handled differnetly - I think by an different concrete class 
				// that implements the Bookmark interface!! 
				attributes = bookmark.getDomNode().getAttributes();
				if (attributes != null) {

					// Get the colFirst and colLast attributes. If both - for 
					// now - are found, then we are dealing with a bookmarked 
					// cell. 
					colFirstNode = attributes.getNamedItem("w:colFirst");
					colLastNode = attributes.getNamedItem("w:colLast");
					if (colFirstNode != null && colLastNode != null) {

						// Get the index of the cell (or cells later) from them. 
						// First convefrt the String values both return to primitive 
						// int value. TO DO, what happens if there is a 
						// NumberFormatException. 
						firstColIndex = Integer.parseInt(colFirstNode.getNodeValue());
						lastColIndex = Integer.parseInt(colLastNode.getNodeValue());
						// if the indices are equal, then we are dealing with a# 
						// cell and can create the bookmark for it. 
						if (firstColIndex == lastColIndex) {
							this._bookmarks.put(bookmark.getName(), new BookMark(bookmark, paragraph, tableRow.getCell(firstColIndex)));
						} else {
							System.out.println("This bookmark " + bookmark.getName() + " identifies a number of cells in the "
									+ "table. That condition is not handled yet.");
						}
					} else {
						this._bookmarks.put(bookmark.getName(), new BookMark(bookmark, paragraph, tableRow.getCell(1)));
					}
				} else {
					this._bookmarks.put(bookmark.getName(), new BookMark(bookmark, paragraph, tableRow.getCell(1)));
				}
			}
		}
	}

	/**
	 * 解析普通段落中的标签
	 * @param paragraphList  传入的段落
	 */
	private void procParaList(List<XWPFParagraph> paragraphList) {
		for (XWPFParagraph paragraph : paragraphList) {
			List<CTBookmark> bookmarkList = paragraph.getCTP().getBookmarkStartList();
			//循环加入标签
			for (CTBookmark bookmark : bookmarkList) {
				this._bookmarks.put(bookmark.getName(), new BookMark(bookmark, paragraph));
			}
		}
	}
}