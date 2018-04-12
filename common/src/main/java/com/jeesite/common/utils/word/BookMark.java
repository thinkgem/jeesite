package com.jeesite.common.utils.word;

import java.util.List;
import java.util.Stack;

import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBookmark;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTText;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Word 文件中标签的封装类，保存了其定义和内部的操作
 * <p>Modification History:</p>
 * <p>Date       Author      Description</p>
 * <p>------------------------------------------------------------------</p>
 * <p> </p>
 */
public class BookMark {

	//以下为定义的常量

	/** 替换标签时，设于标签的后面   **/
	public static final int INSERT_AFTER = 0;

	/** 替换标签时，设于标签的前面   **/
	public static final int INSERT_BEFORE = 1;

	/** 替换标签时，将内容替换书签   **/
	public static final int REPLACE = 2;

	/** docx中定义的部分常量引用  **/
	public static final String RUN_NODE_NAME = "w:r";
	public static final String TEXT_NODE_NAME = "w:t";
	public static final String BOOKMARK_START_TAG = "bookmarkStart";
	public static final String BOOKMARK_END_TAG = "bookmarkEnd";
	public static final String BOOKMARK_ID_ATTR_NAME = "w:id";
	public static final String STYLE_NODE_NAME = "w:rPr";

	/** 内部的标签定义类  **/
	private CTBookmark _ctBookmark = null;

	/** 标签所处的段落  **/
	private XWPFParagraph _para = null;

	/** 标签所在的表cell对象  **/
	private XWPFTableCell _tableCell = null;

	/** 标签名称 **/
	private String _bookmarkName = null;

	/** 该标签是否处于表格内  **/
	private boolean _isCell = false;

	/**
	 * 构造函数
	 * @param ctBookmark
	 * @param para
	 */
	public BookMark(CTBookmark ctBookmark, XWPFParagraph para) {
		this._ctBookmark = ctBookmark;
		this._para = para;
		this._bookmarkName = ctBookmark.getName();
		this._tableCell = null;
		this._isCell = false;
	}

	/**
	 * 构造函数，用于表格中的标签
	 * @param ctBookmark
	 * @param para
	 * @param tableCell
	 */
	public BookMark(CTBookmark ctBookmark, XWPFParagraph para, XWPFTableCell tableCell) {
		this(ctBookmark, para);
		this._tableCell = tableCell;
		this._isCell = true;
	}

	public boolean isInTable() {
		return this._isCell;
	}

	public XWPFTable getContainerTable() {
		return this._tableCell.getTableRow().getTable();
	}

	public XWPFTableRow getContainerTableRow() {
		return this._tableCell.getTableRow();
	}

	public String getBookmarkName() {
		return this._bookmarkName;
	}

	/** 
	 * Insert text into the Word document in the location indicated by this 
	 * bookmark. 
	 * 
	 * @param bookmarkValue An instance of the String class that encapsulates 
	 * the text to insert into the document. 
	 * @param where A primitive int whose value indicates where the text ought 
	 * to be inserted. There are three options controlled by constants; insert 
	 * the text immediately in front of the bookmark (Bookmark.INSERT_BEFORE), 
	 * insert text immediately after the bookmark (Bookmark.INSERT_AFTER) and 
	 * replace any and all text that appears between the bookmark's square 
	 * brackets (Bookmark.REPLACE). 
	 */
	public void insertTextAtBookMark(String bookmarkValue, int where) {

		//根据标签的类型，进行不同的操作
		if (this._isCell) {
			this.handleBookmarkedCells(bookmarkValue, where);
		} else {

			//普通标签，直接创建一个元素
			XWPFRun run = this._para.createRun();
			run.setText(bookmarkValue);
			switch (where) {
			case BookMark.INSERT_AFTER:
				this.insertAfterBookmark(run);
				break;
			case BookMark.INSERT_BEFORE:
				this.insertBeforeBookmark(run);
				break;
			case BookMark.REPLACE:
				this.replaceBookmark(run);
				break;
			}
		}
	}

	/** 
	 * Inserts some text into a Word document in a position that is immediately 
	 * after a named bookmark. 
	 * 
	 * Bookmarks can take two forms, they can either simply mark a location 
	 * within a document or they can do this but contain some text. The 
	 * difference is obvious from looking at some XML markup. The simple 
	 * placeholder bookmark will look like this; 
	 * 
	 * <pre>
	 * 
	 * <w:bookmarkStart w:name="AllAlone" w:id="0"/><w:bookmarkEnd w:id="0"/>
	 * 
	 * </pre>
	 * 
	 * Simply a pair of tags where one tag has the name bookmarkStart, the other 
	 * the name bookmarkEnd and both share matching id attributes. In this case, 
	 * the text will simply be inserted into the document at a point immediately 
	 * after the bookmarkEnd tag. No styling will be applied to the text, it 
	 * will simply inherit the documents defaults. 
	 * 
	 * The more complex case looks like this; 
	 * 
	 * <pre>
	 * 
	 * <w:bookmarkStart w:name="InStyledText" w:id="3"/>
	 *   <w:r w:rsidRPr="00DA438C">
	 *     <w:rPr>
	 *       <w:rFonts w:hAnsi="Engravers MT" w:ascii="Engravers MT" w:cs="Arimo"/>
	 *       <w:color w:val="FF0000"/>
	 *     </w:rPr>
	 *     <w:t>text</w:t>
	 *   </w:r>
	 * <w:bookmarkEnd w:id="3"/>
	 * 
	 * </pre>
	 * 
	 * Here, the user has selected the word 'text' and chosen to insert a 
	 * bookmark into the document at that point. So, the bookmark tags 'contain' 
	 * a character run that is styled. Inserting any text after this bookmark, 
	 * it is important to ensure that the styling is preserved and copied over 
	 * to the newly inserted text. 
	 * 
	 * The approach taken to dealing with both cases is similar but slightly 
	 * different. In both cases, the code simply steps along the document nodes 
	 * until it finds the bookmarkEnd tag whose ID matches that of the 
	 * bookmarkStart tag. Then, it will look to see if there is one further node 
	 * following the bookmarkEnd tag. If there is, it will insert the text into 
	 * the paragraph immediately in front of this node. If, on the other hand, 
	 * there are no more nodes following the bookmarkEnd tag, then the new run 
	 * will simply be positioned at the end of the paragraph. 
	 * 
	 * Styles are dealt with by 'looking' for a 'w:rPr' element whilst iterating 
	 * through the nodes. If one is found, its details will be captured and 
	 * applied to the run before the run is inserted into the paragraph. If 
	 * there are multiple runs between the bookmarkStart and bookmarkEnd tags 
	 * and these have different styles applied to them, then the style applied 
	 * to the last run before the bookmarkEnd tag - if any - will be cloned and 
	 * applied to the newly inserted text. 
	 * 
	 * @param run An instance of the XWPFRun class that encapsulates the text 
	 * that is to be inserted into the document following the bookmark. 
	 */
	private void insertAfterBookmark(XWPFRun run) {
		Node nextNode = null;
		Node insertBeforeNode = null;
		Node styleNode = null;
		int bookmarkStartID = 0;
		int bookmarkEndID = -1;

		// Capture the id of the bookmarkStart tag. The code will step through 
		// the document nodes 'contained' within the start and end tags that have 
		// matching id numbers. 
		bookmarkStartID = this._ctBookmark.getId().intValue();

		// Get the node for the bookmark start tag and then enter a loop that 
		// will step from one node to the next until the bookmarkEnd tag with 
		// a matching id is fouind. 
		nextNode = this._ctBookmark.getDomNode();
		while (bookmarkStartID != bookmarkEndID) {

			// Get the next node along and check to see if it is a bookmarkEnd 
			// tag. If it is, get its id so that the containing while loop can 
			// be terminated once the correct end tag is found. Note that the 
			// id will be obtained as a String and must be converted into an 
			// integer. This has been coded to fail safely so that if an error 
			// is encuntered converting the id to an int value, the while loop 
			// will still terminate. 
			nextNode = nextNode.getNextSibling();
			if (nextNode.getNodeName().contains(BookMark.BOOKMARK_END_TAG)) {
				try {
					bookmarkEndID = Integer.parseInt(nextNode.getAttributes().getNamedItem(BookMark.BOOKMARK_ID_ATTR_NAME).getNodeValue());
				} catch (NumberFormatException nfe) {
					bookmarkEndID = bookmarkStartID;
				}
			} // If we are not dealing with a bookmarkEnd node, are we dealing 
				// with a run node that MAY contains styling information. If so, 
				// then get that style information from the run. 
			else {
				if (nextNode.getNodeName().equals(BookMark.RUN_NODE_NAME)) {
					styleNode = this.getStyleNode(nextNode);
				}
			}
		}

		// After the while loop completes, it should have located the correct 
		// bookmarkEnd tag but we cannot perform an insert after only an insert 
		// before operation and must, therefore, get the next node. 
		insertBeforeNode = nextNode.getNextSibling();

		// Style the newly inserted text. Note that the code copies or clones 
		// the style it found in another run, failure to do this would remove the 
		// style from one node and apply it to another. 
		if (styleNode != null) {
			run.getCTR().getDomNode().insertBefore(styleNode.cloneNode(true), run.getCTR().getDomNode().getFirstChild());
		}

		// Finally, check to see if there was a node after the bookmarkEnd 
		// tag. If there was, then this code will insert the run in front of 
		// that tag. If there was no node following the bookmarkEnd tag then the 
		// run will be inserted at the end of the paragarph and this was taken 
		// care of at the point of creation. 
		if (insertBeforeNode != null) {
			this._para.getCTP().getDomNode().insertBefore(run.getCTR().getDomNode(), insertBeforeNode);
		}
	}

	/** 
	 * Inserts some text into a Word document immediately in front of the 
	 * location of a bookmark. 
	 * 
	 * This case is slightly more straightforward than inserting after the 
	 * bookmark. For example, it is possible only to insert a new node in front 
	 * of an existing node. When inserting after the bookmark, then end node had 
	 * to be located whereas, in this case, the node is already known, it is the 
	 * CTBookmark itself. The only information that must be discovered is 
	 * whether there is a run immediately in front of the boookmarkStart tag and 
	 * whether that run is styled. If there is and if it is, then this style 
	 * must be cloned and applied the text which will be inserted into the 
	 * paragraph. 
	 * 
	 * @param run An instance of the XWPFRun class that encapsulates the text 
	 * that is to be inserted into the document following the bookmark. 
	 */
	private void insertBeforeBookmark(XWPFRun run) {
		Node insertBeforeNode = null;
		Node childNode = null;
		Node styleNode = null;

		// Get the dom node from the bookmarkStart tag and look for another 
		// node immediately preceding it. 
		insertBeforeNode = this._ctBookmark.getDomNode();
		childNode = insertBeforeNode.getPreviousSibling();

		// If a node is found, try to get the styling from it. 
		if (childNode != null) {
			styleNode = this.getStyleNode(childNode);

			// If that previous node was styled, then apply this style to the 
			// text which will be inserted. 
			if (styleNode != null) {
				run.getCTR().getDomNode().insertBefore(styleNode.cloneNode(true), run.getCTR().getDomNode().getFirstChild());
			}
		}

		// Insert the text into the paragraph immediately in front of the 
		// bookmarkStart tag. 
		this._para.getCTP().getDomNode().insertBefore(run.getCTR().getDomNode(), insertBeforeNode);
	}

	/** 
	 * Replace the text - if any - contained between the bookmarkStart and it's 
	 * matching bookmarkEnd tag with the text specified. The technique used will 
	 * resemble that employed when inserting text after the bookmark. In short, 
	 * the code will iterate along the nodes until it encounters a matching 
	 * bookmarkEnd tag. Each node encountered will be deleted unless it is the 
	 * final node before the bookmarkEnd tag is encountered and it is a 
	 * character run. If this is the case, then it can simply be updated to 
	 * contain the text the users wishes to see inserted into the document. If 
	 * the last node is not a character run, then it will be deleted, a new run 
	 * will be created and inserted into the paragraph between the bookmarkStart 
	 * and bookmarkEnd tags. 
	 * 
	 * @param run An instance of the XWPFRun class that encapsulates the text 
	 * that is to be inserted into the document following the bookmark. 
	 */
	private void replaceBookmark(XWPFRun run) {
		Node nextNode = null;
		Node styleNode = null;
		Node lastRunNode = null;
//		Node toDelete = null;
//		NodeList childNodes = null;
		Stack<Node> nodeStack = null;
//		boolean textNodeFound = false;
//		boolean foundNested = true;
		int bookmarkStartID = 0;
		int bookmarkEndID = -1;
//		int numChildNodes = 0;

		nodeStack = new Stack<Node>();
		bookmarkStartID = this._ctBookmark.getId().intValue();
		nextNode = this._ctBookmark.getDomNode();
		nodeStack.push(nextNode);

		// Loop through the nodes looking for a matching bookmarkEnd tag 
		while (bookmarkStartID != bookmarkEndID) {
			nextNode = nextNode.getNextSibling();
			nodeStack.push(nextNode);

			// If an end tag is found, does it match the start tag? If so, end 
			// the while loop. 
			if (nextNode.getNodeName().contains(BookMark.BOOKMARK_END_TAG)) {
				try {
					bookmarkEndID = Integer.parseInt(nextNode.getAttributes().getNamedItem(BookMark.BOOKMARK_ID_ATTR_NAME).getNodeValue());
				} catch (NumberFormatException nfe) {
					bookmarkEndID = bookmarkStartID;
				}
			}
			//else { 
			// Place a reference to the node on the nodeStack 
			//    nodeStack.push(nextNode); 
			//} 
		}

		// If the stack of nodes found between the bookmark tags is not empty 
		// then they have to be removed. 
		if (!nodeStack.isEmpty()) {

			// Check the node at the top of the stack. If it is a run, get it's 
			// style - if any - and apply to the run that will be replacing it. 
			//lastRunNode = nodeStack.pop(); 
			lastRunNode = nodeStack.peek();

			if ((lastRunNode.getNodeName().equals(BookMark.RUN_NODE_NAME))) {
				styleNode = this.getStyleNode(lastRunNode);
				if (styleNode != null) {
					run.getCTR().getDomNode().insertBefore(styleNode.cloneNode(true), run.getCTR().getDomNode().getFirstChild());
				}
			}

			// Delete any and all node that were found in between the start and 
			// end tags. This is slightly safer that trying to delete the nodes 
			// as they are found while stepping through them in the loop above. 

			// If we are peeking, then this line can be commented out.             
			//this._para.getCTP().getDomNode().removeChild(lastRunNode); 
			this.deleteChildNodes(nodeStack);
		}

		// Place the text into position, between the bookmark tags. 
		this._para.getCTP().getDomNode().insertBefore(run.getCTR().getDomNode(), nextNode);
	}

	/** 
	 * When replacing the bookmark's text, it is necessary to delete any nodes 
	 * that are found between matching start and end tags. Complications occur 
	 * here because it is possible to have bookmarks nested within bookmarks to 
	 * almost any level and it is important to not remove any inner or nested 
	 * bookmarks when replacing the contents of an outer or containing 
	 * bookmark. This code successfully handles the simplest occurrence - where 
	 * one bookmark completely contains another - but not more complex cases 
	 * where one bookmark overlaps another in the markup. That is still to do. 
	 * 
	 * @param nodeStack An instance of the Stack class that encapsulates 
	 * references to any and all nodes found between the opening and closing 
	 * tags of a bookmark. 
	 */
	private void deleteChildNodes(Stack<Node> nodeStack) {
		Node toDelete = null;
		int bookmarkStartID = 0;
		int bookmarkEndID = 0;
		boolean inNestedBookmark = false;

		// The first element in the list will be a bookmarkStart tag and that 
		// must not be deleted. 
		for (int i = 1; i < nodeStack.size(); i++) {

			// Get an element. If it is another bookmarkStart tag then 
			// again, we do not want to delete it, it's matching end tag 
			// or any nodes that fall inbetween. 
			toDelete = nodeStack.elementAt(i);
			if (toDelete.getNodeName().contains(BookMark.BOOKMARK_START_TAG)) {
				bookmarkStartID = Integer.parseInt(toDelete.getAttributes().getNamedItem(BookMark.BOOKMARK_ID_ATTR_NAME).getNodeValue());
				inNestedBookmark = true;
			} else if (toDelete.getNodeName().contains(BookMark.BOOKMARK_END_TAG)) {
				bookmarkEndID = Integer.parseInt(toDelete.getAttributes().getNamedItem(BookMark.BOOKMARK_ID_ATTR_NAME).getNodeValue());
				if (bookmarkEndID == bookmarkStartID) {
					inNestedBookmark = false;
				}
			} else {
				if (!inNestedBookmark) {
					this._para.getCTP().getDomNode().removeChild(toDelete);
				}
			}
		}
	}

	/** 
	 * Recover styling information - if any - from another document node. Note 
	 * that it is only possible to accomplish this if the node is a run (w:r) 
	 * and this could be tested for in the code that calls this method. However, 
	 * a check is made in the calling code as to whether a style has been found 
	 * and only if a style is found is it applied. This method always returns 
	 * null if it does not find a style making that checking process easier. 
	 * 
	 * @param parentNode An instance of the Node class that encapsulates a 
	 * reference to a document node. 
	 * @return An instance of the Node class that encapsulates the styling 
	 * information applied to a character run. Note that if no styling 
	 * information is found in the run OR if the node passed as an argument to 
	 * the parentNode parameter is NOT a run, then a null value will be 
	 * returned. 
	 */
	private Node getStyleNode(Node parentNode) {
		Node childNode = null;
		Node styleNode = null;
		if (parentNode != null) {

			// If the node represents a run and it has child nodes then 
			// it can be processed further. Note, whilst testing the code, it 
			// was observed that although it is possible to get a list of a nodes 
			// children, even when a node did have children, trying to obtain this 
			// list would often return a null value. This is the reason why the 
			// technique of stepping from one node to the next is used here. 
			if (parentNode.getNodeName().equalsIgnoreCase(BookMark.RUN_NODE_NAME) && parentNode.hasChildNodes()) {

				// Get the first node and catch it's reference for return if 
				// the first child node is a style node (w:rPr). 
				childNode = parentNode.getFirstChild();
				if (childNode != null && childNode.getNodeName().equals("w:rPr")) {
					styleNode = childNode;
				} else {
					// If the first node was not a style node and there are other 
					// child nodes remaining to be checked, then step through 
					// the remaining child nodes until either a style node is 
					// found or until all child nodes have been processed. 
					while (childNode != null && (childNode = childNode.getNextSibling()) != null) {
						if (childNode.getNodeName().equals(BookMark.STYLE_NODE_NAME)) {
							styleNode = childNode;
							// Note setting to null here if a style node is 
							// found in order order to terminate any further 
							// checking 
							childNode = null;
						}
					}
				}
			}
		}
		return (styleNode);
	}

	/** 
	 * Get the text - if any - encapsulated by this bookmark. The creator of a 
	 * Word document can chose to select one or more items of text and then 
	 * insert a bookmark at that location. The highlighted text will appear 
	 * between the square brackets that denote the location of a bookmark in the 
	 * document's text and they will be returned by a call to this method. 
	 * 
	 * @return An instance of the String class encapsulating any text that 
	 * appeared between the opening and closing square bracket associated with 
	 * this bookmark. 
	 * @throws XmlException Thrown if a problem is encountered parsing the XML 
	 * markup recovered from the document in order to construct a CTText 
	 * instance which may required to obtain the bookmarks text. 
	 */
	public String getBookmarkText() throws XmlException {
		StringBuilder builder = null;
		// Are we dealing with a bookmarked table cell? If so, the entire 
		// contents of the cell - if anything - must be recovered and returned. 
		if (this._tableCell != null) {
			builder = new StringBuilder(this._tableCell.getText());
		} else {
			builder = this.getTextFromBookmark();
		}
		return (builder == null ? null : builder.toString());
	}

	/** 
	 * There are two types of bookmarks. One is a simple placeholder whilst the 
	 * second is still a placeholder but it 'contains' some text. In the second 
	 * instance, the creator of the document has selected some text and then 
	 * chosen to insert a bookmark there and the difference if obvious when 
	 * looking at the XML markup. 
	 * 
	 * The simple case; 
	 * 
	 * <pre>
	 * 
	 * <w:bookmarkStart w:name="AllAlone" w:id="0"/><w:bookmarkEnd w:id="0"/>
	 * 
	 * </pre>
	 * 
	 * The more complex case; 
	 * 
	 * <pre>
	 * 
	 * <w:bookmarkStart w:name="InStyledText" w:id="3"/>
	 *   <w:r w:rsidRPr="00DA438C">
	 *     <w:rPr>
	 *       <w:rFonts w:hAnsi="Engravers MT" w:ascii="Engravers MT" w:cs="Arimo"/>
	 *       <w:color w:val="FF0000"/>
	 *     </w:rPr>
	 *     <w:t>text</w:t>
	 *   </w:r>
	 * <w:bookmarkEnd w:id="3"/>
	 * 
	 * </pre>
	 * 
	 * This method assumes that the user wishes to recover the content from any 
	 * character run that appears in the markup between a matching pair of 
	 * bookmarkStart and bookmarkEnd tags; thus, using the example above again, 
	 * this method would return the String 'text' to the user. It is possible 
	 * however for a bookmark to contain more than one run and for a bookmark to 
	 * contain other bookmarks. In both of these cases, this code will return 
	 * the text contained within any and all runs that appear in the XML markup 
	 * between matching bookmarkStart and bookmarkEnd tags. The term 'matching 
	 * bookmarkStart and bookmarkEndtags' here means tags whose id attributes 
	 * have matching value. 
	 * 
	 * @return An instance of the StringBuilder class encapsulating the text 
	 * recovered from any character run elements found between the bookmark's 
	 * start and end tags. If no text is found then a null value will be 
	 * returned. 
	 * @throws XmlException Thrown if a problem is encountered parsing the XML 
	 * markup recovered from the document in order to construct a CTText 
	 * instance which may be required to obtain the bookmarks text. 
	 */
	private StringBuilder getTextFromBookmark() throws XmlException {
		int startBookmarkID = 0;
		int endBookmarkID = -1;
		Node nextNode = null;
//		Node childNode = null;
//		CTText text = null;
		StringBuilder builder = null;
//		String rawXML = null;

		// Get the ID of the bookmark from it's start tag, the DOM node from the 
		// bookmark (to make looping easier) and initialise the StringBuilder. 
		startBookmarkID = this._ctBookmark.getId().intValue();
		nextNode = this._ctBookmark.getDomNode();
		builder = new StringBuilder();

		// Loop through the nodes held between the bookmark's start and end 
		// tags. 
		while (startBookmarkID != endBookmarkID) {

			// Get the next node and, if it is a bookmarkEnd tag, get it's ID 
			// as matching ids will terminate the while loop.. 
			nextNode = nextNode.getNextSibling();
			if (nextNode.getNodeName().contains(BookMark.BOOKMARK_END_TAG)) {

				// Get the ID attribute from the node. It is a String that must 
				// be converted into an int. An exception could be thrown and so 
				// the catch clause will ensure the loop ends neatly even if the 
				// value might be incorrect. Must inform the user. 
				try {
					endBookmarkID = Integer.parseInt(nextNode.getAttributes().getNamedItem(BookMark.BOOKMARK_ID_ATTR_NAME).getNodeValue());
				} catch (NumberFormatException nfe) {
					endBookmarkID = startBookmarkID;
				}
			} else {
				// This is not a bookmarkEnd node and can processed it for any 
				// text it may contain. Note the check for both type - it must 
				// be a run - and contain children. Interestingly, it seems as 
				// though the node may contain children and yet the call to 
				// nextNode.getChildNodes() will still return an empty list, 
				// hence the need to step through the child nodes. 
				if (nextNode.getNodeName().equals(BookMark.RUN_NODE_NAME) && nextNode.hasChildNodes()) {
					// Get the text from the child nodes. 
					builder.append(this.getTextFromChildNodes(nextNode));
				}
			}
		}
		return (builder);
	}

	/** 
	 * Iterates through all and any children of the Node whose reference will be 
	 * passed as an argument to the node parameter, and recover the contents of 
	 * any text nodes. Testing revealed that a node can be called a text node 
	 * and yet report it's type as being something different, an element node 
	 * for example. Calling the getNodeValue() method on a text node will return 
	 * the text the node encapsulates but doing the same on an element node will 
	 * not. In fact, the call will simply return a null value. As a result, this 
	 * method will test the nodes name to catch all text nodes - those whose 
	 * name is to 'w:t' and then it's type. If the type is reported to be a text 
	 * node, it is a trivial task to get at it's contents. However, if the type 
	 * is not reported as a text type, then it is necessary to parse the raw XML 
	 * markup for the node to recover it's value. 
	 * 
	 * @param node An instance of the Node class that encapsulates a reference 
	 * to a node recovered from the document being processed. It should be 
	 * passed a reference to a character run - 'w:r' - node. 
	 * @return An instance of the String class that encapsulates the text 
	 * recovered from the nodes children, if they are text nodes. 
	 * @throws XmlException Thrown if a problem is encountered parsing the XML 
	 * markup recovered from the document in order to construct the CTText 
	 * instance which may be required to obtain the bookmarks text. 
	 */
	private String getTextFromChildNodes(Node node) throws XmlException {
		NodeList childNodes = null;
		Node childNode = null;
		CTText text = null;
		StringBuilder builder = new StringBuilder();
		int numChildNodes = 0;

		// Get a list of chid nodes from the node passed to the method and 
		// find out how many children there are in the list. 
		childNodes = node.getChildNodes();
		numChildNodes = childNodes.getLength();

		// Iterate through the children one at a time - it is possible for a 
		// run to ciontain zero, one or more text nodes - and recover the text 
		// from an text type child nodes. 
		for (int i = 0; i < numChildNodes; i++) {

			// Get a node and check it's name. If this is 'w:t' then process as 
			// text type node. 
			childNode = childNodes.item(i);

			if (childNode.getNodeName().equals(BookMark.TEXT_NODE_NAME)) {

				// If the node reports it's type as txet, then simply call the 
				// getNodeValue() method to get at it's text. 
				if (childNode.getNodeType() == Node.TEXT_NODE) {
					builder.append(childNode.getNodeValue());
				} else {
					// Correct the type by parsing the node's XML markup and 
					// creating a CTText object. Call the getStringValue() 
					// method on that to get the text. 
					text = CTText.Factory.parse(childNode);
					builder.append(text.getStringValue());
				}
			}
		}
		return (builder.toString());
	}

	private void handleBookmarkedCells(String bookmarkValue, int where) {
		List<XWPFParagraph> paraList = null;
//		List<XWPFRun> runs = null;
		XWPFParagraph para = null;
//		XWPFRun readRun = null;
		// Get a list if paragraphs from the table cell and remove any and all. 
		paraList = this._tableCell.getParagraphs();
		for (int i = 0; i < paraList.size(); i++) {
			this._tableCell.removeParagraph(i);
		}
		para = this._tableCell.addParagraph();
		para.createRun().setText(bookmarkValue);
	}
}