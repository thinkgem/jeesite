/**
 * Copyright (c) 2013-Now http://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 */
package com.jeesite.common.utils.excel;

import com.jeesite.common.callback.MethodCallback;
import com.jeesite.common.codec.EncodeUtils;
import com.jeesite.common.collect.ListUtils;
import com.jeesite.common.collect.MapUtils;
import com.jeesite.common.lang.DateUtils;
import com.jeesite.common.lang.ObjectUtils;
import com.jeesite.common.lang.StringUtils;
import com.jeesite.common.reflect.ReflectUtils;
import com.jeesite.common.utils.excel.annotation.ExcelField;
import com.jeesite.common.utils.excel.annotation.ExcelField.Type;
import com.jeesite.common.utils.excel.annotation.ExcelFields;
import com.jeesite.common.utils.excel.fieldtype.FieldType;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.formula.eval.ErrorEval;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

/**
 * 导入Excel文件（支持“XLS”和“XLSX”格式）
 * @author ThinkGem
 * @version 2020-3-5
 */
public class ExcelImport implements Closeable {
	
	private static final Logger log = LoggerFactory.getLogger(ExcelImport.class);
			
	/**
	 * 工作薄对象
	 */
	private final Workbook wb;
	
	/**
	 * 工作表对象
	 */
	private Sheet sheet;
	
	/**
	 * 标题行数
	 */
	private int headerNum;
	
	/**
	 * 存储字段类型临时数据
	 */
	private final Map<Class<? extends FieldType>, FieldType> fieldTypes = MapUtils.newHashMap();

	@SuppressWarnings("rawtypes")
	private static Class dictUtilsClass = null;

	/**
	 * 构造函数
	 * @param fileName 导入文件对象，读取第一个工作表
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public ExcelImport(String fileName) throws InvalidFormatException, IOException {
		this(new File(fileName));
	}

	/**
	 * 构造函数
	 * @param file 导入文件对象，读取第一个工作表
	 * @throws InvalidFormatException 
	 * @throws IOException 
	 */
	public ExcelImport(File file) throws InvalidFormatException, IOException {
		this(file, 0, 0);
	}
	/**
	 * 构造函数
	 * @param fileName 导入文件对象，读取第一个工作表
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public ExcelImport(String fileName, int headerNum) throws InvalidFormatException, IOException {
		this(new File(fileName), headerNum);
	}
	
	/**
	 * 构造函数
	 * @param file 导入文件对象，读取第一个工作表
	 * @param headerNum 标题行数，数据行号=标题行数+1
	 * @throws InvalidFormatException 
	 * @throws IOException 
	 */
	public ExcelImport(File file, int headerNum) 
			throws InvalidFormatException, IOException {
		this(file, headerNum, 0);
	}

	/**
	 * 构造函数
	 * @param fileName 导入文件对象
	 * @param headerNum 标题行数，数据行号=标题行数+1
	 * @param sheetIndexOrName 工作表编号或名称，从0开始
	 * @throws InvalidFormatException
	 * @throws IOException
	 */
	public ExcelImport(String fileName, int headerNum, Object sheetIndexOrName)
			throws InvalidFormatException, IOException {
		this(new File(fileName), headerNum, sheetIndexOrName);
	}

	/**
	 * 构造函数
	 * @param file 导入文件对象
	 * @param headerNum 标题行数，数据行号=标题行数+1
	 * @param sheetIndexOrName 工作表编号或名称，从0开始
	 * @throws InvalidFormatException 
	 * @throws IOException 
	 */
	public ExcelImport(File file, int headerNum, Object sheetIndexOrName) 
			throws InvalidFormatException, IOException {
		this(file.getName(), new FileInputStream(file), headerNum, sheetIndexOrName);
	}
	
	/**
	 * 构造函数
	 * @param multipartFile 导入文件对象
	 * @param headerNum 标题行数，数据行号=标题行数+1
	 * @param sheetIndexOrName 工作表编号或名称，从0开始
	 * @throws InvalidFormatException 
	 * @throws IOException 
	 */
	public ExcelImport(MultipartFile multipartFile, int headerNum, Object sheetIndexOrName) 
			throws InvalidFormatException, IOException {
		this(multipartFile.getOriginalFilename(), multipartFile.getInputStream(), headerNum, sheetIndexOrName);
	}

	/**
	 * 构造函数
	 * @param fileName 导入文件对象
	 * @param headerNum 标题行数，数据行号=标题行数+1
	 * @param sheetIndexOrName 工作表编号或名称
	 * @throws InvalidFormatException 
	 * @throws IOException 
	 */
	public ExcelImport(String fileName, InputStream is, int headerNum, Object sheetIndexOrName) 
			throws InvalidFormatException, IOException {
		if (StringUtils.isBlank(fileName)){
			throw new ExcelException("导入文档为空!");
		}else if(fileName.toLowerCase().endsWith("xls")){    
			this.wb = new HSSFWorkbook(is);    
        }else if(fileName.toLowerCase().endsWith("xlsx")){  
        	this.wb = new XSSFWorkbook(is);
        }else{  
        	throw new ExcelException("文档格式不正确!");
        }
		this.setSheet(sheetIndexOrName, headerNum);
		log.debug("Initialize success.");
	}
	
	/**
	 * 添加到 annotationList
	 */
	private void addAnnotation(List<Object[]> annotationList, ExcelField ef, Object fOrM, Type type, String... groups){
//		if (ef != null && (ef.type()==0 || ef.type()==type)){
		if (ef != null && (ef.type() == Type.ALL || ef.type() == type)){
			if (groups!=null && groups.length>0){
				boolean inGroup = false;
				for (String g : groups){
					if (inGroup){
						break;
					}
					for (String efg : ef.groups()){
						if (StringUtils.equals(g, efg)){
							inGroup = true;
							annotationList.add(new Object[]{ef, fOrM});
							break;
						}
					}
				}
			}else{
				annotationList.add(new Object[]{ef, fOrM});
			}
		}
	}

	/**
	 * 获取当前工作薄
	 * @author ThinkGem
	 */
	public Workbook getWorkbook() {
		return wb;
	}

	/**
	 * 获取当前工作表
	 */
	public Sheet getSheet() {
		return sheet;
	}

	/**
	 * 获取当前标题行数
	 */
	public int getHeaderNum() {
		return headerNum;
	}

	/**
	 * 设置当前工作表和标题行数
	 * @author ThinkGem
	 */
	public void setSheet(Object sheetIndexOrName, int headerNum) {
		if (sheetIndexOrName instanceof Integer || sheetIndexOrName instanceof Long){
			this.sheet = this.wb.getSheetAt(ObjectUtils.toInteger(sheetIndexOrName));
		}else{
			this.sheet = this.wb.getSheet(ObjectUtils.toString(sheetIndexOrName));
		}
		if (this.sheet == null){
			throw new ExcelException("没有找到‘"+sheetIndexOrName+"’工作表!");
		}
		this.headerNum = headerNum;
	}

	/**
	 * 获取行对象
	 * @return 返回Row对象，如果空行返回null
	 */
	public Row getRow(int rownum){
		Row row = this.sheet.getRow(rownum);
		if (row == null){
			return null;
		}
		// 验证是否是空行，如果空行返回null
		short cellNum = 0;
		short emptyNum = 0;
		Iterator<Cell> it = row.cellIterator();
		while (it.hasNext()) {
			cellNum++;
			Cell cell = it.next();
			if (StringUtils.isBlank(cell.toString())) {
				emptyNum++;
			}
		}
		if (cellNum == emptyNum) {
			return null;
		}
		return row;
	}

	/**
	 * 获取数据行号
	 */
	public int getDataRowNum(){
		return headerNum;
	}
	
	/**
	 * 获取最后一个数据行号
	 */
	public int getLastDataRowNum(){
		//return this.sheet.getLastRowNum() + headerNum;
		return this.sheet.getLastRowNum() + 1;
	}
	
	/**
	 * 获取最后一个列号
	 */
	public int getLastCellNum(){
		Row row = this.getRow(headerNum);
		return row == null ? 0 : row.getLastCellNum();
	}
	
	/**
	 * 获取单元格值
	 * @param row 获取的行
	 * @param column 获取单元格列号
	 * @return 单元格值
	 */
	public Object getCellValue(Row row, int column){
		if (row == null){
			return row;
		}
		Object val = "";
		try{
			Cell cell = row.getCell(column);
			if (cell != null){
				if (cell.getCellType() == CellType.NUMERIC){
					val = cell.getNumericCellValue();
					if (DateUtil.isCellDateFormatted(cell)) {
						val = DateUtil.getJavaDate((Double) val); // POI Excel 日期格式转换
					}else{
						if (Math.abs((double)val % 1) > 1E-10){
							val = new DecimalFormat("0.00").format(val);
						}else{
							val = new DecimalFormat("0").format(val);
						}
					}
				}else if (cell.getCellType() == CellType.STRING) {
					val = cell.getStringCellValue();
				}else if (cell.getCellType() == CellType.FORMULA){
					try {
						val = cell.getStringCellValue();
					} catch (Exception e) {
						FormulaEvaluator evaluator = cell.getSheet().getWorkbook()
								.getCreationHelper().createFormulaEvaluator();
						evaluator.evaluateFormulaCell(cell);
						CellValue cellValue = evaluator.evaluate(cell);
						switch (cellValue.getCellType()) {
						case NUMERIC:
							val = cellValue.getNumberValue();
							break;
						case STRING:
							val = cellValue.getStringValue();
							break;
						case BOOLEAN:
							val = cellValue.getBooleanValue();
							break;
						case ERROR:
							val = ErrorEval.getText(cellValue.getErrorValue());
							break;
						default:
							val = cell.getCellFormula();
						}
					}
				}else if (cell.getCellType() == CellType.BOOLEAN){
					val = cell.getBooleanCellValue();
				}else if (cell.getCellType() == CellType.ERROR){
					val = cell.getErrorCellValue();
				}
			}
		}catch (Exception e) {
			return val;
		}
		return val;
	}
	
	/**
	 * 获取导入数据列表
	 * @param cls 导入对象类型
	 * @param groups 导入分组
	 */
	public <E> List<E> getDataList(Class<E> cls, String... groups) throws Exception{
		return getDataList(cls, false, groups);
	}
	
	/**
	 * 获取导入数据列表
	 * @param cls 导入对象类型
	 * @param isThrowException 遇见错误是否抛出异常
	 * @param groups 导入分组
	 */
	public <E> List<E> getDataList(Class<E> cls, final boolean isThrowException, String... groups) throws Exception{
		return getDataList(cls, params -> {
			if (isThrowException){
				Exception ex = (Exception)params[0];
				int rowNum = (int)params[1];
				int columnNum = (int)params[2];
				throw new ExcelException("Get cell value ["+rowNum+","+columnNum+"]", ex);
			}
			return null;
		}, groups);
	}
	/**
	 * 获取导入数据列表
	 * @param cls 导入对象类型
	 * @param exceptionCallback 遇见异常时回调方法
	 * @param groups 导入分组
	 */
	@SuppressWarnings("unchecked")
	public <E> List<E> getDataList(Class<E> cls, MethodCallback exceptionCallback, String... groups) throws Exception{
		List<Object[]> annotationList = ListUtils.newArrayList();
		// Get class annotation
		ExcelFields cfs = cls.getAnnotation(ExcelFields.class);
		if (cfs != null && cfs.value() != null){
			for (ExcelField ef : cfs.value()){
				addAnnotation(annotationList, ef, cls, Type.IMPORT, groups);
			}
		}
		// Get constructor annotation
		Constructor<?>[] cs = cls.getConstructors();
		for (Constructor<?> c : cs) {
			ExcelFields efs = c.getAnnotation(ExcelFields.class);
			if (efs != null && efs.value() != null){
				for (ExcelField ef : efs.value()){
					addAnnotation(annotationList, ef, c, Type.IMPORT, groups);
				}
			}
		}
		// Get annotation field 
		Field[] fs = cls.getDeclaredFields();
		for (Field f : fs){
			ExcelFields efs = f.getAnnotation(ExcelFields.class);
			if (efs != null && efs.value() != null){
				for (ExcelField ef : efs.value()){
					addAnnotation(annotationList, ef, f, Type.IMPORT, groups);
				}
			}
			ExcelField ef = f.getAnnotation(ExcelField.class);
			addAnnotation(annotationList, ef, f, Type.IMPORT, groups);
			ReflectUtils.makeAccessible(f);
		}
		// Get annotation method
		Method[] ms = cls.getDeclaredMethods();
		for (Method m : ms){
			ExcelFields efs = m.getAnnotation(ExcelFields.class);
			if (efs != null && efs.value() != null){
				for (ExcelField ef : efs.value()){
					addAnnotation(annotationList, ef, m, Type.IMPORT, groups);
				}
			}
			ExcelField ef = m.getAnnotation(ExcelField.class);
			addAnnotation(annotationList, ef, m, Type.IMPORT, groups);
			ReflectUtils.makeAccessible(m);
		}
		// Field sorting
		Collections.sort(annotationList, new Comparator<Object[]>() {
			@Override
			public int compare(Object[] o1, Object[] o2) {
				return Integer.valueOf(((ExcelField)o1[0]).sort()).compareTo(
						Integer.valueOf(((ExcelField)o2[0]).sort()));
			};
		});
		//log.debug("Import column count:"+annotationList.size());
		// Get excel data
		List<E> dataList = ListUtils.newArrayList();
		for (int i = this.getDataRowNum(); i < this.getLastDataRowNum(); i++) {
			E e = (E)cls.getDeclaredConstructor().newInstance();
			Row row = this.getRow(i);
			if (row == null){
				continue;
			}
			StringBuilder sb = new StringBuilder();
			for (int j = 0; j < annotationList.size(); j++){//Object[] os : annotationList){
				Object[] os = annotationList.get(j);
				ExcelField ef = (ExcelField)os[0];
				int column = (ef.column() != -1) ? ef.column() : j;
				Object val = this.getCellValue(row, column);
				if (val != null){
					// If is dict type, get dict value
					if (StringUtils.isNotBlank(ef.dictType())){
						try{
							if (dictUtilsClass == null) {
								dictUtilsClass = Class.forName("com.jeesite.modules.sys.utils.DictUtils");
							}
							val = ReflectUtils.invokeMethodByAsm(dictUtilsClass, "getDictValues",
									ef.dictType(), val.toString(), "");
							//val = dictUtilsClass.getMethod("getDictValues", String.class, String.class,
							//			String.class).invoke(null, ef.dictType(), val.toString(), "");
							//val = DictUtils.getDictValue(val.toString(), ef.dictType(), "");
							//log.debug("Dictionary type value: ["+i+","+colunm+"] " + val);
						} catch (Exception ex) {
							log.info("Get cell value [{},{}] error: {}", i, column, ex.toString());
							val = null;
						}
					}
					// Get param type and type cast
					Class<?> valType = Class.class;
					if (os[1] instanceof Field){
						valType = ((Field)os[1]).getType();
					}else if (os[1] instanceof Method){
						Method method = ((Method)os[1]);
						if ("get".equals(method.getName().substring(0, 3))){
							valType = method.getReturnType();
						}else if("set".equals(method.getName().substring(0, 3))){
							valType = ((Method)os[1]).getParameterTypes()[0];
						}
					}
					//log.debug("Import value type: ["+i+","+column+"] " + valType);
					try {
						if (StringUtils.isNotBlank(ef.attrName())){
							if (ef.fieldType() != FieldType.class){
								FieldType ft = getFieldType(ef.fieldType());
								val = ft.getValue(ObjectUtils.toString(val));
							}
						}else{
							if (val != null){
								if (valType == String.class){
									String s = String.valueOf(val.toString());
									if(StringUtils.endsWith(s, ".0")){
										val = StringUtils.substringBefore(s, ".0");
									}else{
										val = String.valueOf(val.toString());
									}
								}else if (valType == Integer.class){
									val = Double.valueOf(val.toString()).intValue();
								}else if (valType == Long.class){
									val = Double.valueOf(val.toString()).longValue();
								}else if (valType == Double.class){
									val = Double.valueOf(val.toString());
								}else if (valType == Float.class){
									val = Float.valueOf(val.toString());
								}else if (valType == BigDecimal.class){
									val = new BigDecimal(val.toString());
								}else if (valType == Date.class){
									if (val instanceof String){
										val = DateUtils.parseDate(val);
									}else if (val instanceof Double){
										val = DateUtil.getJavaDate((Double)val); // POI Excel 日期格式转换
									}
								}else{
									if (ef.fieldType() != FieldType.class){
										FieldType ft = getFieldType(ef.fieldType());
										val = ft.getValue(ObjectUtils.toString(val));
									}else{
										// 如果没有指定 fieldType，切自行根据类型查找相应的转换类（com.jeesite.common.utils.excel.fieldtype.值的类名+Type）
										Class<? extends FieldType> fieldType = (Class<? extends FieldType>)Class.forName(this.getClass().getName().replaceAll(this.getClass().getSimpleName(), 
												"fieldtype."+valType.getSimpleName()+"Type"));
										FieldType ft = getFieldType(fieldType);
										val = ft.getValue(ObjectUtils.toString(val));
									}
								}
							}
						}
					} catch (Exception ex) {
						log.info("Get cell value ["+i+","+column+"] error: " + ex.toString());
						val = null;
						// 参数：Exception ex, int rowNum, int columnNum
						exceptionCallback.execute(ex, i, column);
					}
					// 导入的数据进行 xss 过滤
					if (val != null && val instanceof String) {
						val = EncodeUtils.xssFilter(val.toString());
					}
					// set entity value
					if (StringUtils.isNotBlank(ef.attrName())){
						ReflectUtils.invokeSetter(e, ef.attrName(), val);
					}else{
						if (os[1] instanceof Field){
							ReflectUtils.invokeSetter(e, ((Field)os[1]).getName(), val);
						}else if (os[1] instanceof Method){
							String mthodName = ((Method)os[1]).getName();
							if ("get".equals(mthodName.substring(0, 3))){
								mthodName = "set"+mthodName.substring(3);
							}
							ReflectUtils.invokeMethodByAsm(e, mthodName, val);
						}
					}
				}
				sb.append(val+", ");
			}
			dataList.add(e);
			log.debug("Read success: [{}] {}", i, sb.toString());
		}
		return dataList;
	}
	
	private FieldType getFieldType(Class<? extends FieldType> fieldType) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		FieldType ft = fieldTypes.get(fieldType);
		if (ft == null) {
			ft = fieldType.getDeclaredConstructor().newInstance();
			fieldTypes.put(fieldType, ft);
		}
		return ft;
	}
	
	@Override
	public void close() throws IOException {
		fieldTypes.clear();
		try {
			wb.close();
		} catch (IOException e) {
			log.error(e.getMessage(), e);
		}
	}

}
