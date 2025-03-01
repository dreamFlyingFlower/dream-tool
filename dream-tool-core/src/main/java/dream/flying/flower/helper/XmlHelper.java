package dream.flying.flower.helper;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import dream.flying.flower.ConstString;
import dream.flying.flower.bean.BeanHelper;
import dream.flying.flower.collection.MapHelper;
import dream.flying.flower.io.IOHelper;
import dream.flying.flower.io.file.FileHelper;
import dream.flying.flower.lang.AssertHelper;
import dream.flying.flower.lang.StrHelper;

/**
 * Xml工具类,使用JDK自带工具,效率较DOM4J低
 * 
 * Namespace命名空间相关文章:<br>
 * {@link https://www.ibm.com/developerworks/cn/xml/x-nmspccontext/}
 * 
 * XPath相关文章:<br>
 * {@link http://www.w3school.com.cn/xpath/xpath_syntax.asp}
 * 
 * 格式化输出相关文章:<br>
 * {@link https://stackoverflow.com/questions/139076/how-to-pretty-print-xml-from-java}
 * 
 * @author 飞花梦影
 * @date 2021-03-06 00:06:52
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class XmlHelper {

	/** XML中无效的字符 正则 */
	public static final String INVALID_REGEX = "[\\x00-\\x08\\x0b-\\x0c\\x0e-\\x1f]";

	/** XML中注释的内容 正则 */
	public static final String COMMENT_REGEX = "(?s)<!--.+?-->";

	/** XML格式化输出默认缩进量 */
	public static final int INDENT_DEFAULT = 2;

	/** 默认的DocumentBuilderFactory实现 */
	private static String defaultDocumentBuilderFactory =
			"com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl";

	/** 是否打开命名空间支持 */
	private static boolean namespaceAware = true;

	/** Sax读取器工厂缓存 */
	private static SAXParserFactory factory;

	/** 禁用默认的DocumentBuilderFactory,禁用后如果有第三方的实现,如oracle的xdb包中的xmlparse,将会自动加载实现 */
	synchronized public static void disableDefaultDocumentBuilderFactory() {
		defaultDocumentBuilderFactory = null;
	}

	/**
	 * 设置是否打开命名空间支持,默认打开
	 *
	 * @param namespaceAware 是否命名空间支持
	 */
	synchronized public static void setNamespaceAware(boolean namespaceAware) {
		XmlHelper.namespaceAware = namespaceAware;
	}

	/**
	 * 在给定节点上创建子节点
	 *
	 * @param node 给定节点
	 * @param tagName 标签名
	 * @return 子节点
	 */
	public static Element appendChild(Node node, String tagName) {
		return appendChild(node, tagName, null);
	}

	/**
	 * 在给定节点上创建子节点
	 *
	 * @param node 给定节点
	 * @param tagName 标签名
	 * @param namespace 命名空间,无传null
	 * @return 子节点
	 */
	public static Element appendChild(Node node, String tagName, String namespace) {
		final Document doc = getOwnerDocument(node);
		final Element child =
				(null == namespace) ? doc.createElement(tagName) : doc.createElementNS(namespace, tagName);
		node.appendChild(child);
		return child;
	}

	/**
	 * 在给定节点上创建文本子节点
	 *
	 * @param node 给定节点
	 * @param text 文本
	 * @return 子节点
	 */
	public static Node appendText(Node node, CharSequence text) {
		return appendText(getOwnerDocument(node), node, text);
	}

	/**
	 * 在给定节点上创建文本子节点
	 *
	 * @param doc {@link Document}
	 * @param node 给定节点
	 * @param text 文本内容
	 * @return 增加的子节点,即Text节点
	 */
	private static Node appendText(Document doc, Node node, CharSequence text) {
		return node.appendChild(doc.createTextNode(StrHelper.toString(text)));
	}

	/**
	 * 关闭XXE,避免漏洞攻击<br>
	 * see:
	 * https://www.owasp.org/index.php/XML_External_Entity_(XXE)_Prevention_Cheat_Sheet#JAXP_DocumentBuilderFactory.2C_SAXParserFactory_and_DOM4J
	 *
	 * @param dbf DocumentBuilderFactory
	 * @return DocumentBuilderFactory
	 */
	private static DocumentBuilderFactory disableXXE(DocumentBuilderFactory dbf) {
		String feature;
		try {
			// This is the PRIMARY defense. If DTDs (doctypes) are disallowed, almost all
			// XML entity
			// attacks are
			// prevented
			// Xerces 2 only -
			// http://xerces.apache.org/xerces2-j/features.html#disallow-doctype-decl
			feature = "http://apache.org/xml/features/disallow-doctype-decl";
			dbf.setFeature(feature, true);
			// If you can't completely disable DTDs, then at least do the following:
			// Xerces 1 -
			// http://xerces.apache.org/xerces-j/features.html#external-general-entities
			// Xerces 2 -
			// http://xerces.apache.org/xerces2-j/features.html#external-general-entities
			// JDK7+ - http://xml.org/sax/features/external-general-entities
			feature = "http://xml.org/sax/features/external-general-entities";
			dbf.setFeature(feature, false);
			// Xerces 1 -
			// http://xerces.apache.org/xerces-j/features.html#external-parameter-entities
			// Xerces 2 -
			// http://xerces.apache.org/xerces2-j/features.html#external-parameter-entities
			// JDK7+ - http://xml.org/sax/features/external-parameter-entities
			feature = "http://xml.org/sax/features/external-parameter-entities";
			dbf.setFeature(feature, false);
			// Disable external DTDs as well
			feature = "http://apache.org/xml/features/nonvalidating/load-external-dtd";
			dbf.setFeature(feature, false);
			// and these as well, per Timothy Morgan's 2014 paper: "XML Schema, DTD, and
			// Entity
			// Attacks"
			dbf.setXIncludeAware(false);
			dbf.setExpandEntityReferences(false);
		} catch (ParserConfigurationException e) {
			// ignore
		}
		return dbf;
	}

	/**
	 * 去除XML文本中的无效字符
	 *
	 * @param xmlContent XML文本
	 * @return 当传入为null时返回null
	 */
	public static String cleanInvalid(String xmlContent) {
		if (xmlContent == null) {
			return null;
		}
		return xmlContent.replaceAll(INVALID_REGEX, "");
	}

	/**
	 * 去除XML文本中的注释内容
	 *
	 * @param xmlContent XML文本
	 * @return 当传入为null时返回null
	 */
	public static String cleanComment(String xmlContent) {
		if (xmlContent == null) {
			return null;
		}
		return xmlContent.replaceAll(COMMENT_REGEX, ConstString.EMPTY);
	}

	/**
	 * 创建 DocumentBuilder
	 *
	 * @return DocumentBuilder
	 * @throws ParserConfigurationException
	 */
	public static DocumentBuilder createDocumentBuilder() throws ParserConfigurationException {
		return createDocumentBuilderFactory().newDocumentBuilder();
	}

	/**
	 * 创建{@link DocumentBuilderFactory}
	 * 
	 * <pre>
	 * 默认使用"com.sun.org.apache.xerces.internal.jaxp.DocumentBuilderFactoryImpl"
	 * 如果使用第三方实现,请调用{@link #disableDefaultDocumentBuilderFactory()}
	 * </pre>
	 *
	 * @return {@link DocumentBuilderFactory}
	 */
	public static DocumentBuilderFactory createDocumentBuilderFactory() {
		final DocumentBuilderFactory factory;
		if (StrHelper.isNotEmpty(defaultDocumentBuilderFactory)) {
			factory = DocumentBuilderFactory.newInstance(defaultDocumentBuilderFactory, null);
		} else {
			factory = DocumentBuilderFactory.newInstance();
		}
		factory.setNamespaceAware(namespaceAware);
		return disableXXE(factory);
	}

	/**
	 * 创建XML文档,默认UTF-8编码
	 *
	 * @return XML文档
	 * @throws ParserConfigurationException
	 */
	public static Document createXml() throws ParserConfigurationException {
		return createDocumentBuilder().newDocument();
	}

	/**
	 * 创建XML文档,默认UTF-8编码
	 *
	 * @param rootElementName 根节点名称
	 * @return XML文档
	 * @throws ParserConfigurationException
	 */
	public static Document createXml(String rootElementName) throws ParserConfigurationException {
		return createXml(rootElementName, null);
	}

	/**
	 * 创建XML文档,默认UTF-8编码
	 *
	 * @param rootElementName 根节点名称
	 * @param namespace 命名空间,无则传null
	 * @return XML文档
	 * @throws ParserConfigurationException
	 */
	public static Document createXml(String rootElementName, String namespace) throws ParserConfigurationException {
		final Document doc = createXml();
		doc.appendChild(null == namespace ? doc.createElement(rootElementName)
				: doc.createElementNS(namespace, rootElementName));
		return doc;
	}

	/**
	 * 创建XPath
	 *
	 * @return {@link XPath}
	 */
	public static XPath createXPath() {
		return XPathFactory.newInstance().newXPath();
	}

	/**
	 * 根据节点名获得第一个子节点
	 *
	 * @param element 节点
	 * @param tagName 节点名
	 * @return 节点中的值
	 */
	public static String elementText(Element element, String tagName) {
		Element child = getElement(element, tagName);
		return child == null ? null : child.getTextContent();
	}

	/**
	 * 根据节点名获得第一个子节点
	 *
	 * @param element 节点
	 * @param tagName 节点名
	 * @param defaultValue 默认值
	 * @return 节点中的值
	 */
	public static String elementText(Element element, String tagName, String defaultValue) {
		Element child = getElement(element, tagName);
		return child == null ? defaultValue : child.getTextContent();
	}

	/**
	 * 转义XML特殊字符:
	 *
	 * <pre>
	 * &amp; (ampersand) 替换为 &amp;amp;
	 * &lt; (小于) 替换为 &amp;lt;
	 * &gt; (大于) 替换为 &amp;gt;
	 * &quot; (双引号) 替换为 &amp;quot;
	 * </pre>
	 *
	 * @param string 被替换的字符串
	 * @return 替换后的字符串
	 */
	public static String escape(String string) {
		return EscapeHelper.escape(string);
	}

	/**
	 * 格式化XML输出
	 *
	 * @param doc {@link Document} XML文档
	 * @return 格式化后的XML字符串
	 * @throws TransformerException
	 */
	public static String format(Document doc) throws TransformerException {
		return toStr(doc, true);
	}

	/**
	 * 格式化XML输出
	 *
	 * @param xmlStr XML字符串
	 * @return 格式化后的XML字符串
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws TransformerException
	 */
	public static String format(String xmlStr)
			throws SAXException, IOException, ParserConfigurationException, TransformerException {
		return format(parseXml(xmlStr));
	}

	/**
	 * 通过XPath方式读取XML节点等信息
	 *
	 * @param expression XPath表达式
	 * @param source 资源,可以是Docunent,Node节点等
	 * @param returnType 返回类型,{@link javax.xml.xpath.XPathConstants}
	 * @return 匹配返回类型的值
	 * @throws XPathExpressionException
	 */
	public static Object getByXPath(String expression, Object source, QName returnType)
			throws XPathExpressionException {
		NamespaceContext nsContext = null;
		if (source instanceof Node) {
			nsContext = new UniversalNamespaceCache((Node) source, false);
		}
		return getByXPath(expression, source, returnType, nsContext);
	}

	/**
	 * 通过XPath方式读取XML节点等信息
	 *
	 * @param expression XPath表达式
	 * @param source 资源,可以是Docunent,Node节点等
	 * @param returnType 返回类型,{@link javax.xml.xpath.XPathConstants}
	 * @param nsContext {@link NamespaceContext}
	 * @return 匹配返回类型的值
	 * @throws XPathExpressionException
	 */
	public static Object getByXPath(String expression, Object source, QName returnType, NamespaceContext nsContext)
			throws XPathExpressionException {
		final XPath xPath = createXPath();
		if (null != nsContext) {
			xPath.setNamespaceContext(nsContext);
		}
		if (source instanceof InputSource) {
			return xPath.evaluate(expression, (InputSource) source, returnType);
		} else {
			return xPath.evaluate(expression, source, returnType);
		}
	}

	/**
	 * 根据节点名获得第一个子节点
	 *
	 * @param element 节点
	 * @param tagName 节点名
	 * @return 节点
	 */
	public static Element getElement(Element element, String tagName) {
		final NodeList nodeList = element.getElementsByTagName(tagName);
		if (nodeList == null || nodeList.getLength() < 1) {
			return null;
		}
		int length = nodeList.getLength();
		for (int i = 0; i < length; i++) {
			Element childEle = (Element) nodeList.item(i);
			if (childEle == null || childEle.getParentNode() == element) {
				return childEle;
			}
		}
		return null;
	}

	/**
	 * 根据节点名获得子节点列表
	 *
	 * @param element 节点
	 * @param tagName 节点名,如果节点名为空,返回所有子节点
	 * @return 节点列表
	 */
	public static List<Element> getElements(Element element, String tagName) {
		final NodeList nodeList =
				StrHelper.isBlank(tagName) ? element.getChildNodes() : element.getElementsByTagName(tagName);
		return transElements(element, nodeList);
	}

	/**
	 * 通过XPath方式读取XML节点等信息
	 *
	 * @param expression XPath表达式
	 * @param source 资源,可以是Docunent,Node节点等
	 * @return 匹配返回类型的值
	 * @throws XPathExpressionException
	 */
	public static Element getElementByXPath(String expression, Object source) throws XPathExpressionException {
		return (Element) getNodeByXPath(expression, source);
	}

	/**
	 * 通过XPath方式读取XML的NodeList
	 *
	 * @param expression XPath表达式
	 * @param source 资源,可以是Docunent,Node节点等
	 * @return NodeList
	 * @throws XPathExpressionException
	 */
	public static NodeList getNodeListByXPath(String expression, Object source) throws XPathExpressionException {
		return (NodeList) getByXPath(expression, source, XPathConstants.NODESET);
	}

	/**
	 * 通过XPath方式读取XML节点等信息
	 *
	 * @param expression XPath表达式
	 * @param source 资源,可以是Docunent,Node节点等
	 * @return 匹配返回类型的值
	 * @throws XPathExpressionException
	 */
	public static Node getNodeByXPath(String expression, Object source) throws XPathExpressionException {
		return (Node) getByXPath(expression, source, XPathConstants.NODE);
	}

	/**
	 * 获取节点所在的Document
	 *
	 * @param node 节点
	 * @return {@link Document}
	 */
	public static Document getOwnerDocument(Node node) {
		return (node instanceof Document) ? (Document) node : node.getOwnerDocument();
	}

	/**
	 * 获得XML文档根节点
	 *
	 * @param doc {@link Document}
	 * @return 根节点
	 * @see Document#getDocumentElement()
	 */
	public static Element getRootElement(Document doc) {
		return (null == doc) ? null : doc.getDocumentElement();
	}

	/**
	 * 给定节点是否为{@link Element} 类型节点
	 *
	 * @param node 节点
	 * @return 是否为{@link Element} 类型节点
	 */
	public static boolean isElement(Node node) {
		return (null != node) && Node.ELEMENT_NODE == node.getNodeType();
	}

	/**
	 * 将String类型的XML转换为XML文档
	 *
	 * @param xmlStr xml字符串
	 * @return XML文档
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public static Document parseXml(String xmlStr) throws SAXException, IOException, ParserConfigurationException {
		AssertHelper.notBlank(xmlStr, "XML content string is empty !");
		return readXml(new StringReader(cleanInvalid(xmlStr)));
	}

	/**
	 * Sax方式读取XML,若contentHandler为{@link DefaultHandler},其接口都会被处理
	 *
	 * @param file XML源文件,使用后自动关闭
	 * @param contentHandler XML流处理器,用于按照Element处理xml
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public static void readBySax(File file, ContentHandler contentHandler)
			throws IOException, ParserConfigurationException, SAXException {
		try (InputStream in = FileHelper.newInputStream(file);) {
			readBySax(new InputSource(in), contentHandler);
		}
	}

	/**
	 * Sax方式读取XML,若contentHandler为{@link DefaultHandler},其接口都会被处理
	 *
	 * @param reader XML源Reader,使用后自动关闭
	 * @param contentHandler XML流处理器,用于按照Element处理xml
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public static void readBySax(Reader reader, ContentHandler contentHandler)
			throws IOException, ParserConfigurationException, SAXException {
		try {
			readBySax(new InputSource(reader), contentHandler);
		} finally {
			IOHelper.close(reader);
		}
	}

	/**
	 * Sax方式读取XML,若contentHandler为{@link DefaultHandler},其接口都会被处理
	 *
	 * @param source XML源流,使用后自动关闭
	 * @param contentHandler XML流处理器,用于按照Element处理xml
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public static void readBySax(InputStream source, ContentHandler contentHandler)
			throws IOException, ParserConfigurationException, SAXException {
		try {
			readBySax(new InputSource(source), contentHandler);
		} finally {
			source.close();
		}
	}

	/**
	 * Sax方式读取XML,若contentHandler为{@link DefaultHandler},其接口都会被处理
	 *
	 * @param source XML源,可以是文件,流,路径等
	 * @param contentHandler XML流处理器,用于按照Element处理xml
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 */
	public static void readBySax(InputSource source, ContentHandler contentHandler)
			throws ParserConfigurationException, SAXException, IOException {
		// 1.获取解析工厂
		if (null == factory) {
			factory = SAXParserFactory.newInstance();
			factory.setValidating(false);
			factory.setNamespaceAware(namespaceAware);
		}
		// 2.从解析工厂获取解析器
		final SAXParser parse;
		XMLReader reader;
		parse = factory.newSAXParser();
		if (contentHandler instanceof DefaultHandler) {
			parse.parse(source, (DefaultHandler) contentHandler);
			return;
		}
		// 3.得到解读器
		reader = parse.getXMLReader();
		reader.setContentHandler(contentHandler);
		reader.parse(source);
	}

	/**
	 * 从XML中读取序列化对象
	 *
	 * @param <T> 对象类型
	 * @param source XML文件
	 * @return 对象
	 * @throws IOException
	 */
	public static <T> T readObjectFromXml(File source) throws IOException {
		return readObjectFromXml(new InputSource(FileHelper.newInputStream(source)));
	}

	/**
	 * 从XML中读取序列化对象
	 *
	 * @param <T> 对象类型
	 * @param xmlStr XML内容
	 * @return 对象
	 */
	public static <T> T readObjectFromXml(String xmlStr) {
		return readObjectFromXml(new InputSource(new StringReader(xmlStr)));
	}

	/**
	 * 从XML中读取序列化对象
	 *
	 * @param <T> 对象类型
	 * @param source {@link InputSource}
	 * @return 对象
	 */
	@SuppressWarnings("unchecked")
	public static <T> T readObjectFromXml(InputSource source) {
		Object result;
		XMLDecoder xmldec = null;
		try {
			xmldec = new XMLDecoder(source);
			result = xmldec.readObject();
		} finally {
			if (null != xmldec) {
				xmldec.close();
			}
		}
		return (T) result;
	}

	/**
	 * 读取解析XML文件
	 *
	 * @param file XML文件
	 * @return XML文档对象
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public static Document readXml(File file) throws IOException, SAXException, ParserConfigurationException {
		AssertHelper.notNull(file, "Xml file must not be null !");
		FileHelper.checkFile(file);
		file = file.getCanonicalFile();
		try (BufferedInputStream in = FileHelper.newBufferedInputStream(file);) {
			return readXml(in);
		}
	}

	/**
	 * 读取解析XML文件
	 * 
	 * <pre>
	 * 1.若内容以"&lt;"开头,认为该字符串是一个XML内容,直接读取
	 * 2.若不是以"&lt;"开头,按照路径处理,相对路径相对于clasapath
	 * </pre>
	 *
	 * @param pathOrContent 内容或路径
	 * @return XML文档对象
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public static Document readXml(String pathOrContent)
			throws IOException, SAXException, ParserConfigurationException {
		if (pathOrContent.startsWith("<")) {
			return parseXml(pathOrContent);
		}
		return readXml(new File(pathOrContent));
	}

	/**
	 * 读取解析XML文件,编码在XML中定义
	 *
	 * @param inputStream xml字节输入流
	 * @return XML文档对象
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public static Document readXml(InputStream inputStream)
			throws SAXException, IOException, ParserConfigurationException {
		return readXml(new InputSource(inputStream));
	}

	/**
	 * 读取解析XML文件
	 *
	 * @param reader xml字符输入流
	 * @return XML文档对象
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public static Document readXml(Reader reader) throws SAXException, IOException, ParserConfigurationException {
		return readXml(new InputSource(reader));
	}

	/**
	 * 读取解析XML文件,字符编码在XML中定义
	 *
	 * @param source {@link InputSource}
	 * @return XML文档对象
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public static Document readXml(InputSource source) throws SAXException, IOException, ParserConfigurationException {
		final DocumentBuilder builder = createDocumentBuilder();
		return builder.parse(source);
	}

	/**
	 * 将XML文档写出
	 *
	 * @param source 源
	 * @param result 目标
	 * @param charset 字符编码
	 * @param indent 格式化输出中缩进量,小于1表示不格式化输出
	 * @throws TransformerException
	 */
	public static void transform(Source source, Result result, String charset, int indent) throws TransformerException {
		transform(source, result, charset, indent, false);
	}

	/**
	 * 将XML文档写出
	 * 
	 * @param source 源
	 * @param result 目标
	 * @param charset 编码
	 * @param indent 格式化输出中缩进量,小于1表示不格式化输出
	 * @param omitXmlDeclaration 是否输出xml通常开头的版本和字符集,true->输出,false->不输出
	 * @throws TransformerException
	 */
	public static void transform(Source source, Result result, String charset, int indent, boolean omitXmlDeclaration)
			throws TransformerException {
		final TransformerFactory factory = TransformerFactory.newInstance();
		final Transformer xformer = factory.newTransformer();
		if (indent > 0) {
			xformer.setOutputProperty(OutputKeys.INDENT, "yes");
			xformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes");
			xformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", String.valueOf(indent));
		}
		if (StrHelper.isNotBlank(charset)) {
			xformer.setOutputProperty(OutputKeys.ENCODING, charset);
		}
		if (omitXmlDeclaration) {
			xformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		}
		xformer.transform(source, result);
		// TransformerFactory factory = TransformerFactory.newInstance();
		// // stylesheet样式
		// Transformer transformer = factory.newTransformer(new
		// StreamSource(stylesheet));
		// DocumentSource source = new DocumentSource(document);
		// DocumentResult result = new DocumentResult();
		// transformer.transform(source, result);
		// // 返回经过样式化的文档
		// return result.getDocument();
	}

	/**
	 * 将NodeList转换为Element列表
	 *
	 * @param nodeList NodeList
	 * @return Element列表
	 */
	public static List<Element> transElements(NodeList nodeList) {
		return transElements(null, nodeList);
	}

	/**
	 * 将NodeList转换为Element列表, 非Element节点将被忽略
	 *
	 * @param parentEle 父节点,如果指定将返回此节点的所有直接子节点,null返回所有就节点
	 * @param nodeList NodeList
	 * @return Element列表
	 */
	public static List<Element> transElements(Element parentEle, NodeList nodeList) {
		int length = nodeList.getLength();
		final ArrayList<Element> elements = new ArrayList<>(length);
		Node node;
		Element element;
		for (int i = 0; i < length; i++) {
			node = nodeList.item(i);
			if (Node.ELEMENT_NODE == node.getNodeType()) {
				element = (Element) nodeList.item(i);
				if (parentEle == null || element.getParentNode() == parentEle) {
					elements.add(element);
				}
			}
		}
		return elements;
	}

	/**
	 * 将XML文档写入到文件,使用Document中的编码
	 *
	 * @param doc XML文档
	 * @param absolutePath 文件绝对路径,不存在会自动创建
	 * @throws IOException
	 * @throws TransformerException
	 */
	public static void toFile(Document doc, String absolutePath) throws IOException, TransformerException {
		toFile(doc, absolutePath, null);
	}

	/**
	 * 将XML文档写入到文件
	 *
	 * @param doc XML文档
	 * @param path 文件路径绝对路径或相对ClassPath路径,不存在会自动创建
	 * @param charset 字符编码
	 * @throws IOException
	 * @throws TransformerException
	 */
	public static void toFile(Document doc, String path, String charset) throws IOException, TransformerException {
		if (StrHelper.isBlank(charset)) {
			charset = doc.getXmlEncoding();
		}
		BufferedWriter writer = null;
		try (BufferedWriter writer2 =
				FileHelper.newBufferedWriter(new File(path), CharsetHelper.defaultCharset(charset), false);) {
			write(doc, writer, charset, INDENT_DEFAULT);
		}
	}

	/**
	 * 将XML文档转换为String,字符编码使用XML文档中的编码,获取不到则使用UTF-8
	 *
	 * @param doc XML文档
	 * @return XML字符串
	 * @throws TransformerException
	 */
	public static String toStr(Node doc) throws TransformerException {
		return toStr(doc, false);
	}

	/**
	 * 将XML文档转换为String,字符编码使用XML文档中的编码,获取不到则使用UTF-8
	 * 
	 * @param doc XML文档
	 * @return XML字符串
	 * @throws TransformerException
	 */
	public static String toStr(Document doc) throws TransformerException {
		return toStr((Node) doc);
	}

	/**
	 * 将XML文档转换为String,字符编码使用XML文档中的编码,获取不到则使用UTF-8
	 *
	 * @param doc XML文档
	 * @param isPretty 是否格式化输出
	 * @return XML字符串
	 * @throws TransformerException
	 */
	public static String toStr(Node doc, boolean isPretty) throws TransformerException {
		return toStr(doc, StandardCharsets.UTF_8.displayName(), isPretty);
	}

	/**
	 * 将XML文档转换为String,字符编码使用XML文档中的编码,获取不到则使用UTF-8
	 *
	 * @param doc XML文档
	 * @param isPretty 是否格式化输出
	 * @return XML字符串
	 * @throws TransformerException
	 */
	public static String toStr(Document doc, boolean isPretty) throws TransformerException {
		return toStr((Node) doc, isPretty);
	}

	/**
	 * 将XML文档转换为String,字符编码使用XML文档中的编码,获取不到则使用UTF-8
	 *
	 * @param doc XML文档
	 * @param charset 编码
	 * @param isPretty 是否格式化输出
	 * @return XML字符串
	 * @throws TransformerException
	 */
	public static String toStr(Node doc, String charset, boolean isPretty) throws TransformerException {
		return toStr(doc, charset, isPretty, false);
	}

	/**
	 * 将XML文档转换为String,字符编码使用XML文档中的编码,获取不到则使用UTF-8
	 *
	 * @param doc XML文档
	 * @param charset 编码
	 * @param isPretty 是否格式化输出
	 * @return XML字符串
	 * @throws TransformerException
	 */
	public static String toStr(Document doc, String charset, boolean isPretty) throws TransformerException {
		return toStr((Node) doc, charset, isPretty);
	}

	/**
	 * 将XML文档转换为String,字符编码使用XML文档中的编码,获取不到则使用UTF-8
	 *
	 * @param doc XML文档
	 * @param charset 编码
	 * @param isPretty 是否格式化输出
	 * @param omitXmlDeclaration 是否输出 xml Declaration
	 * @return XML字符串
	 * @throws TransformerException
	 */
	public static String toStr(Node doc, String charset, boolean isPretty, boolean omitXmlDeclaration)
			throws TransformerException {
		final StringWriter writer = new StringWriter();
		write(doc, writer, charset, isPretty ? INDENT_DEFAULT : 0, omitXmlDeclaration);
		return writer.toString();
	}

	/**
	 * 反转义XML特殊字符:
	 *
	 * @param string 被替换的字符串
	 * @return 替换后的字符串
	 */
	public static String unescape(String string) {
		return EscapeHelper.unescape(string);
	}

	/**
	 * 将XML文档写出
	 *
	 * @param node {@link Node} XML文档节点或文档本身
	 * @param writer 写出的Writer,Writer决定了输出XML的编码
	 * @param charset 字符编码
	 * @param indent 格式化输出中缩进量,小于1表示不格式化输出
	 * @throws TransformerException
	 */
	public static void write(Node node, Writer writer, String charset, int indent) throws TransformerException {
		transform(new DOMSource(node), new StreamResult(writer), charset, indent);
	}

	/**
	 * 将XML文档写出
	 *
	 * @param node {@link Node} XML文档节点或文档本身
	 * @param writer 写出的Writer,Writer决定了输出XML的编码
	 * @param charset 字符编码
	 * @param indent 格式化输出中缩进量,小于1表示不格式化输出
	 * @param omitXmlDeclaration 是否输出xml通常开头的版本和字符集,true->输出,false->不输出
	 * @throws TransformerException
	 */
	public static void write(Node node, Writer writer, String charset, int indent, boolean omitXmlDeclaration)
			throws TransformerException {
		transform(new DOMSource(node), new StreamResult(writer), charset, indent, omitXmlDeclaration);
	}

	/**
	 * 将XML文档写出
	 *
	 * @param node {@link Node} XML文档节点或文档本身
	 * @param out 写出的Writer,Writer决定了输出XML的编码
	 * @param charset 字符编码
	 * @param indent 格式化输出中缩进量,小于1表示不格式化输出
	 * @throws TransformerException
	 */
	public static void write(Node node, OutputStream out, String charset, int indent) throws TransformerException {
		transform(new DOMSource(node), new StreamResult(out), charset, indent);
	}

	/**
	 * 将XML文档写出
	 *
	 * @param node {@link Node} XML文档节点或文档本身
	 * @param out 写出的Writer,Writer决定了输出XML的编码
	 * @param charset 字符编码
	 * @param indent 格式化输出中缩进量,小于1表示不格式化输出
	 * @param omitXmlDeclaration 是否输出xml通常开头的版本和字符集,true->输出,false->不输出
	 * @throws TransformerException
	 */
	public static void write(Node node, OutputStream out, String charset, int indent, boolean omitXmlDeclaration)
			throws TransformerException {
		transform(new DOMSource(node), new StreamResult(out), charset, indent, omitXmlDeclaration);
	}

	/**
	 * 将可序列化的对象转换为XML写入文件,已经存在的文件将被覆盖<br>
	 *
	 * @param dest 目标文件
	 * @param bean 对象
	 * @throws IOException
	 */
	public static void writeObjectAsXml(File dest, Object bean) throws IOException {
		XMLEncoder xmlenc = null;
		try {
			xmlenc = new XMLEncoder(FileHelper.newOutputStream(dest));
			xmlenc.writeObject(bean);
		} finally {
			// 关闭XMLEncoder会相应关闭OutputStream
			xmlenc.close();
		}
	}

	/**
	 * XML转Java Bean
	 *
	 * @param <T> bean类型
	 * @param node XML节点
	 * @param bean bean类
	 * @return bean
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */
	public static <T> T xmlToBean(Node node, Class<T> bean) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		final Map<String, Object> map = xmlToMap(node);
		if (null != map && map.size() == 1) {
			return BeanHelper.mapToBean(map.get(bean.getSimpleName()), bean);
		}
		return BeanHelper.mapToBean(map, bean);
	}

	/**
	 * XML格式字符串转换为Map
	 *
	 * @param xmlStr XML字符串
	 * @return XML数据转换后的Map
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public static Map<String, Object> xmlToMap(String xmlStr)
			throws SAXException, IOException, ParserConfigurationException {
		return xmlToMap(xmlStr, new HashMap<>());
	}

	/**
	 * XML格式字符串转换为Map
	 *
	 * @param node XML节点
	 * @return XML数据转换后的Map
	 */
	public static Map<String, Object> xmlToMap(Node node) {
		return xmlToMap(node, new HashMap<>());
	}

	/**
	 * XML格式字符串转换为Map<br>
	 * 只支持第一级别的XML,不支持多级XML
	 *
	 * @param xmlStr XML字符串
	 * @param result 结果Map类型
	 * @return XML数据转换后的Map
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public static Map<String, Object> xmlToMap(String xmlStr, Map<String, Object> result)
			throws SAXException, IOException, ParserConfigurationException {
		final Document doc = parseXml(xmlStr);
		final Element root = getRootElement(doc);
		root.normalize();
		return xmlToMap(root, result);
	}

	/**
	 * XML节点转换为Map
	 *
	 * @param node XML节点
	 * @param result 结果Map类型
	 * @return XML数据转换后的Map
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Object> xmlToMap(Node node, Map<String, Object> result) {
		if (null == result) {
			result = new HashMap<>();
		}
		final NodeList nodeList = node.getChildNodes();
		final int length = nodeList.getLength();
		Node childNode;
		Element childEle;
		for (int i = 0; i < length; ++i) {
			childNode = nodeList.item(i);
			if (false == isElement(childNode)) {
				continue;
			}
			childEle = (Element) childNode;
			final Object value = result.get(childEle.getNodeName());
			Object newValue;
			if (childEle.hasChildNodes()) {
				// 子节点继续递归遍历
				final Map<String, Object> map = xmlToMap(childEle);
				if (MapHelper.isNotEmpty(map)) {
					newValue = map;
				} else {
					newValue = childEle.getTextContent();
				}
			} else {
				newValue = childEle.getTextContent();
			}
			if (null != newValue) {
				if (null != value) {
					if (value instanceof List) {
						((List<Object>) value).add(newValue);
					} else {
						result.put(childEle.getNodeName(), Arrays.asList(value, newValue));
					}
				} else {
					result.put(childEle.getNodeName(), newValue);
				}
			}
		}
		return result;
	}

	/**
	 * 全局命名空间上下文
	 * 
	 * @author 飞花梦影
	 * @date 2021-03-11 15:15:14
	 * @git {@link https://github.com/dreamFlyingFlower}
	 */
	public static class UniversalNamespaceCache implements NamespaceContext {

		private static final String DEFAULT_NS = "DEFAULT";

		private final HashMap<String, String> prefixUri = new HashMap<String, String>();

		/**
		 * This constructor parses the document and stores all namespaces it can find.
		 * If toplevelOnly is true, only namespaces in the root are used.
		 *
		 * @param node source Node
		 * @param toplevelOnly restriction of the search to enhance performance
		 */
		public UniversalNamespaceCache(Node node, boolean toplevelOnly) {
			examineNode(node.getFirstChild(), toplevelOnly);
		}

		/**
		 * A single node is read, the namespace attributes are extracted and stored.
		 *
		 * @param node to examine
		 * @param attributesOnly, if true no recursion happens
		 */
		private void examineNode(Node node, boolean attributesOnly) {
			final NamedNodeMap attributes = node.getAttributes();
			if (null != attributes) {
				for (int i = 0; i < attributes.getLength(); i++) {
					Node attribute = attributes.item(i);
					storeAttribute(attribute);
				}
			}

			if (false == attributesOnly) {
				final NodeList childNodes = node.getChildNodes();
				if (null != childNodes) {
					Node item;
					for (int i = 0; i < childNodes.getLength(); i++) {
						item = childNodes.item(i);
						if (item.getNodeType() == Node.ELEMENT_NODE)
							examineNode(item, false);
					}
				}
			}
		}

		/**
		 * This method looks at an attribute and stores it, if it is a namespace
		 * attribute.
		 *
		 * @param attribute to examine
		 */
		private void storeAttribute(Node attribute) {
			if (null == attribute) {
				return;
			}
			// examine the attributes in namespace xmlns
			if (XMLConstants.XMLNS_ATTRIBUTE_NS_URI.equals(attribute.getNamespaceURI())) {
				// Default namespace xmlns="uri goes here"
				if (XMLConstants.XMLNS_ATTRIBUTE.equals(attribute.getNodeName())) {
					prefixUri.put(DEFAULT_NS, attribute.getNodeValue());
				} else {
					// The defined prefixes are stored here
					prefixUri.put(attribute.getLocalName(), attribute.getNodeValue());
				}
			}

		}

		/**
		 * This method is called by XPath. It returns the default namespace, if the
		 * prefix is null or "".
		 *
		 * @param prefix to search for
		 * @return uri
		 */
		@Override
		public String getNamespaceURI(String prefix) {
			if (prefix == null || prefix.equals(XMLConstants.DEFAULT_NS_PREFIX)) {
				return prefixUri.get(DEFAULT_NS);
			} else {
				return prefixUri.get(prefix);
			}
		}

		/**
		 * This method is not needed in this context, but can be implemented in a
		 * similar way.
		 */
		@Override
		public String getPrefix(String namespaceURI) {
			return MapHelper.reverse(prefixUri).get(namespaceURI);
		}

		@Override
		public Iterator<String> getPrefixes(String namespaceURI) {
			// Not implemented
			return null;
		}
	}
}