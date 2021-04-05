package com.wy.third.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.function.Predicate;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.QName;
import org.dom4j.Text;
import org.dom4j.XPath;
import org.dom4j.bean.BeanElement;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultElement;
import org.dom4j.tree.DefaultText;

import com.wy.bean.BeanTool;
import com.wy.collection.CollectionTool;
import com.wy.collection.ListTool;
import com.wy.collection.MapTool;
import com.wy.io.file.FileTool;
import com.wy.lang.AssertTool;
import com.wy.lang.StrTool;
import com.wy.util.ArrayTool;
import com.wy.util.CharsetTool;

/**
 * XML工具类,使用dom4j2.1.3,默认字符编码UTF-8
 * 
 * 若需要使用xpath查找指定节点,需要引入jaxen.jar
 * 
 * Namespace命名空间相关文章:<br>
 * {@link https://www.ibm.com/developerworks/cn/xml/x-nmspccontext/}
 * 
 * XPath表达式相关文章:<br>
 * {@link http://www.w3school.com.cn/xpath/xpath_syntax.asp}
 * 
 * <pre>
 * 1.选取节点
 * nodename->选取当前节点的所有子节点
 * /->绝对路径,从根节点选取
 * //->从匹配选择的当前节点选择文档中的节点,而不考虑他们的位置
 * .->选取当前节点
 * ..->选取当前节点的父节点
 * {@code @}->选取属性
 * 
 * book:选取book元素的所有子节点
 * /book:选取根元素book
 * book/it:选取book下名为it的所有子元素
 * //book:选取所有的book子元素,不管他们在文档中的位置
 * book//it:选取book下名为it的所有后代元素,不管他们在book下的什么位置
 * //@book:选取所有名为book的元素的属性
 * 
 * 2.通配符
 * *->通配符,匹配任何元素节点
 * {@code @*}->通配符,匹配任何属性节点
 * node()->通配符,匹配任何类型的节点
 * 
 * /book/*:选取book元素的所有子节点
 * //*:选取文档中的所有元素
 * //book[@*]:选取所有带有属性的book元素
 * 
 * 3.谓语
 * local-name()->元素名
 * namespace-uri()->命名空间
 * last()->最后一个元素
 * position()->相当于子元素索引,也可以直接写数字
 * 
 * /book/it[1]:选取book子元素的第一个it元素
 * /book/it[position()<3]:选取最前面的两个属于book子元素的it元素
 * /book/it[last()]:选取book子元素的最后一个it元素
 * /book/it[last()-1]:选取book子元素的倒数第二个it元素
 * //title[@lang]:选取所有拥有lang属性的title元素,不管位置
 * //title[@lang='zh-cn']:选取所有title元素,要求这些元素拥有值为zh-cn的lang属性,不管位置
 * /book/it[price>35.00]:选取book下的it的price元素,且price的值须大于35.00,该值为text或value
 * /book/it[price>35.00]/title:选取book下的it下的title元素,且it的子元素price的值须大于35.00,该值为text或value
 * 
 * 4.选取若干路径
 * |:或,可以同时匹配多个xpath
 * 
 * //book/title | //book/price:选取所有book元素的title和price元素
 * //title | //price:选取所有文档中的title和price元素
 * /book/it/title|//price:选取所有属于book元素的it元素的title元素,以及文档中所有的price元素
 * 
 * 5.xpath轴,可定义某个相对于当前节点的节点集
 * 
 * ancestor:选取当前节点的所有上级,上上级等一直到根元素
 * ancestor-or-self:选取当前节点的所有上级直到根元素以及当前节点本身
 * attribute:选取当前节点的所有属性
 * child:选取当前节点的所有子元素
 * descendant:递归选取当前节点的所有后代元素
 * descendant-or-self:递归选取当前节点的所有后代元素以及当前节点本身
 * following:选取文档中当前节点的结束标签之后的所有节点
 * namespace:选取当前节点的所有命名空间节点
 * parent:选取当前节点的父节点
 * preceding:选取文档中当前节点的开始标签之前的所有节点
 * preceding-sibling:选取当前节点之前的所有同级节点
 * self:选取当前节点
 * 
 * 6.步,需要和xpath轴一起使用->xpath轴::节点[谓语]
 * 
 * child::book:选取所有属于当前节点的子元素的book节点
 * attribute::lang:选取当前节点的lang属性
 * child::*:选取当前节点的所有子元素
 * attribute::*:选取当前节点的所有属性
 * child::text():选取当前节点的所有文本子节点
 * child::node():选取当前节点的所有子节点
 * descendant::book:选取当前节点的所有book后代
 * ancestor::book:选择当前节点的所有book先辈
 * ancestor-or-self::book:选取当前节点的所有book先辈以及当前节点,假如此节点是book节点的话
 * child::* /child::price:选取当前节点的所有price元素(星号和/之间没有空格,次数会转义)
 * 
 * 7.xpath运算符
 * |:计算两个节点集->//book | //cd->返回所有带有 book 和 ck 元素的节点集
 * +:加法->6 + 4->10
 * -:减法->6 - 4->2
 * *:乘法->6 * 4->24
 * div:除法->8 div 4->2
 * =:等于->price=9.80->如果price是9.80,返回true;如果price是9.90,返回fasle
 * !=:不等于->price!=9.80->如果price是9.90,返回true;如果price是9.80,返回fasle
 * <:小于->price<9.80->如果price是9.00,返回true;如果price是9.90,返回fasle
 * <=:小于或等于->price<=9.80->如果price是9.00,返回true;如果price是9.90,返回fasle
 * >:大于->price>9.80->如果price是9.90,返回true;如果price是9.80,返回fasle
 * >=:大于或等于->price>=9.80->如果price是9.90,返回true;如果price是9.70,返回fasle
 * or:或->price=9.80 or price=9.70->如果price是9.80,返回true;如果price是9.50,返回fasle
 * and:与->price>9.00 and price<9.90->如果price是9.80,返回true;如果price是8.50,返回fasle
 * mod:取余->5 mod 2->1
 * 
 * </pre>
 * 
 * @author 飞花梦影
 * @date 2021-03-11 23:20:56
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class XmlTools {

	/** XML格式化输出默认缩进量 */
	public static final int INDENT_DEFAULT = 2;

	private XmlTools() {}

	/**
	 * 向根节点中添加子节点,无Namespace
	 * 
	 * @param document Document文档模型
	 * @param node 子节点
	 */
	public static void appendNode(Document document, Node node) {
		document.add(node);
	}

	/**
	 * 向指定节点中添加子节点,无Namespace
	 * 
	 * @param element 父节点
	 * @param child 子节点
	 */
	public static void appendNode(Element element, Node child) {
		element.add(child);
	}

	/**
	 * 向指定节点中添加子节点,无Namespace
	 * 
	 * @param element 父节点
	 * @param child 子节点名
	 */
	public static void appendNode(Element element, String child) {
		element.addElement(child);
	}

	/**
	 * 向指定节点中添加子节点,无Namespace
	 * 
	 * @param parent 父节点
	 * @param child 子节点
	 */
	public static void appendNode(Node parent, Node child) {
		parent.getDocument().add(child);
	}

	/**
	 * 向指定节点中添加文本,无Namespace
	 * 
	 * @param parent 父节点
	 * @param text 文本内容
	 */
	public static void appendText(Node parent, String text) {
		parent.setText(text);
	}

	/**
	 * 将Bean转换为XML Document
	 *
	 * @param bean Bean对象
	 * @return Document文档模型
	 */
	public static <T> Document beanToXml(T bean) {
		return beanToXml(bean, null);
	}

	/**
	 * 将Bean转换为XML Document
	 *
	 * @param bean Bean对象
	 * @param namespace 命名空间,可为null
	 * @return Document文档模型
	 */
	public static <T> Document beanToXml(T bean, String namespace) {
		if (null == bean) {
			return null;
		}
		return mapToXml(BeanTool.beanToMap(bean), bean.getClass().getSimpleName(), namespace);
	}

	/**
	 * 清空XmlScans中所有的值,释放内存
	 * 
	 * @param xmlScans {@link XmlScan}快速解析对象
	 */
	public static void close(XmlScan xmlScans) {
		if (null != xmlScans) {
			xmlScans.getAttributes().clear();
			xmlScans.getElements().clear();
			xmlScans.getNamespaces().clear();
			xmlScans.getNodes().clear();
			xmlScans.getTexts().clear();
		}
	}

	/**
	 * 获得指定元素节点的所有属性,无Namespace
	 * 
	 * @param element 元素节点
	 * @return List<Attribute>
	 */
	public static List<Attribute> getAttrs(Element element) {
		return element.attributes();
	}

	/**
	 * 获得指定元素节点的指定属性值,无Namespace
	 * 
	 * @param element 元素节点
	 * @param attrName 属性名
	 * @return 属性值
	 */
	public static String getAttrValue(Element element, String attrName) {
		// element.attribute(attrName).getText(); // 同getValue()
		return element.attribute(attrName).getValue();
	}

	/**
	 * 从当前节点中获得第一个直接指定子节点,不能递归获取子节点,无Namespace
	 * 
	 * @param element 父节点元素
	 * @param childName 子节点元素名称
	 * @return 子节点
	 */
	public static Element getElement(Element element, String childName) {
		return element.element(childName);
	}

	/**
	 * 递归获得文档中从根元素开始查找的指定元素,无Namespace
	 * 
	 * @param result 结果
	 * @param parent 上级元素节点
	 * @param predicate 过滤方法
	 * @return 符合条件的元素节点
	 */
	public static void getElements(ArrayList<Element> result, Element parent, Predicate<Element> predicate) {
		if (null == parent) {
			return;
		}
		List<Element> elements = parent.elements();
		if (ListTool.isNotEmpty(elements)) {
			for (Element element : elements) {
				if (predicate.test(element)) {
					result.add(element);
				}
				getElements(result, element, predicate);
			}
		}
	}

	/**
	 * 从Document中获得根元素的第一层子元素,不能递归获取子节点,无Namespace
	 * 
	 * @param document Document 文档模型
	 * @return List<Element>
	 */
	public static List<Element> getElements(Document document) {
		Element rootElement = document.getRootElement();
		if (null == rootElement) {
			return null;
		}
		return rootElement.elements();
	}

	/**
	 * 递归获得文档中从根元素开始查找的指定元素,无Namespace
	 * 
	 * @param document {@link Document}文档模型
	 * @param predicate 过滤方法
	 * @return 符合条件的元素节点
	 */
	public static List<Element> getElements(Document document, Predicate<Element> predicate) {
		Element rootElement = document.getRootElement();
		if (predicate.test(rootElement)) {
			return ListTool.newArrayList(rootElement);
		}
		ArrayList<Element> result = ListTool.newArrayList();
		getElements(result, rootElement, predicate);
		return result;
	}

	/**
	 * 从Document中获得根元素的第一层指定子元素,不能递归获取子节点,无Namespace
	 * 
	 * @param document Document 文档模型
	 * @param elementName 子节点元素名称
	 * @return List<Element>
	 */
	public static List<Element> getElements(Document document, String elementName) {
		Element rootElement = document.getRootElement();
		if (null == rootElement) {
			return null;
		}
		if (rootElement.getName().equals(elementName)) {
			return ListTool.newArrayList(rootElement);
		}
		return rootElement.elements(elementName);
	}

	/**
	 * 从当前节点中获得所有直接指定子节点,不能递归获取子节点,无Namespace
	 * 
	 * @param element 父节点元素
	 * @param childName 子节点元素名称
	 * @return 子节点
	 */
	public static List<Element> getElements(Element element, String childName) {
		return element.elements(childName);
	}

	/**
	 * XPath导航,找出Document或者任意节点Node,如:Attribute,Element或者ProcessingInstruction等
	 * 
	 * @param filePath xml文件绝对路径
	 * @param xpath xpath表达式
	 * @return List<Node>
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static List<Node> getByXPath(String filePath, String xpath) throws IOException, DocumentException {
		return getByXPath(new File(filePath), xpath);
	}

	/**
	 * XPath导航,找出Document或者任意节点Node,如:Attribute,Element或者ProcessingInstruction等
	 * 
	 * @param file xml文件
	 * @param xpath xpath表达式
	 * @return List<Node>
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static List<Node> getByXPath(File file, String xpath) throws IOException, DocumentException {
		return getByXPath(read(file), xpath);
	}

	/**
	 * XPath导航,找出Document或者任意节点Node,如:Attribute,Element或者ProcessingInstruction等
	 * 
	 * <pre>
	 * xpath:表达式的值,只能带有//,/,:或|,默认的命名空间可以不写,但是非默认的命名空间需要写完整
	 * 但默认的命名空间最好由程序确定,否则无法正确是否为默认的命名空间
	 * eg:/beans/bean/mvc:annotaion-driven->完整的表达式:/beans:beans/beans:bean/mvc:annoation-driven
	 * </pre>
	 * 
	 * @param document Document文档模型
	 * @param xpath xpath表达式
	 * @return List<Node>
	 */
	public static List<Node> getByXPath(Document document, String xpath) {
		return getByXPath(document, xpath, new String[] {});
	}

	/**
	 * 根据属性名查找节点,每个节点必须唯一,暂时未解决重复节点问题 FIXME
	 * 
	 * @param xpath 表达式的值
	 * @param attr 多属性名,和xpath中每一个表达式对应
	 * @return List<Node>
	 */
	public static List<Node> getByXPath(Document document, String xpath, String... attrs) {
		if (StrTool.isBlank(xpath) || document == null) {
			return null;
		}
		XmlScan xmlScans = treeWalk(document);
		// 若没有命名空间,则直接可使用xpath进行寻找节点
		if (xmlScans.getNamespaces().size() == 0) {
			return getByXPathNoNS(document, xpath);
		}
		// 防止有默认命名空间,getnamespceforprefix将无法获取前缀,需要在调用了treewalk之后
		Map<String, Namespace> nameSpace = getNameSpace(xmlScans);
		// 如果有,则需要检测每个标签的命名空间
		Element root = document.getRootElement();
		// xpath所需命名空间
		Map<String, String> map = new HashMap<>();
		// 获得xpath标签的数组
		String[] tags = xpath.split("/+|//+");
		// 利用Arrays.aslist转换来的数组无法进行增删操作,不构造函数重新赋值
		List<String> listTags = new ArrayList<>(Arrays.asList(tags));
		// 为了避免因为第一次切割产生空值,需要对空值进行剔除
		for (int j = listTags.size() - 1; j >= 0; j--) {
			if (StrTool.isBlank(listTags.get(j))) {
				listTags.remove(j);
			}
		}
		// 循环获得标签的命名空间uri以及前缀
		for (int i = 0; i < listTags.size(); i++) {
			String tag = listTags.get(i);
			// 命名空间前缀
			String tagPrefix = "";
			// 若带冒号需要先拿前面的命名空间进行处理
			if (tag.contains(":")) {
				tagPrefix = tag.split(":")[0];
			} else {
				tagPrefix = tag;
			}
			// 在每一个表达式后面加上对应的属性
			String attr = attrs.length > i ? attrs[i] : "";
			// 根据命名空间前缀取得命名空间
			Namespace ns = root.getNamespaceForPrefix(tagPrefix);
			// 默认命名空间前缀
			if (ns == null) {
				map.put("default", nameSpace.get("default").getURI());
				xpath = xpath.replaceAll(tag, "default:" + tag + createAttrVal(attr));
			} else {
				map.put(ns.getPrefix(), ns.getURI());
				xpath = xpath.replaceAll(tag, tag + createAttrVal(attr));
			}
		}
		XPath xPath = document.createXPath(xpath);
		xPath.setNamespaceURIs(map);
		return xPath.selectNodes(document);
	}

	/**
	 * 获得单个标签的命名空间,若是没有指定命名空间前缀,以当前标签的名称为命名空间的前缀 FIXME
	 * 
	 * @param xmlScans {@link XmlScan}
	 */
	private static Map<String, Namespace> getNameSpace(XmlScan xmlScans) {
		Map<String, Namespace> namespace_prefix_ns = new HashMap<>();
		for (Namespace ns : xmlScans.getNamespaces()) {
			String prefix = ns.getPrefix();
			if (StrTool.isBlank(prefix)) {
				namespace_prefix_ns.put("default", ns);
			} else {
				namespace_prefix_ns.put(ns.getPrefix(), ns);
			}
		}
		return namespace_prefix_ns;
	}

	/**
	 * 精准获得当前文档中所有带指定属性名的节点
	 * 
	 * @param document Document文档模型
	 * @param attrs 属性名列表
	 * @return List<Node>
	 */
	public static List<Node> getByAttrs(Document document, XmlScan xmlScans, String... attrs) {
		return getNodesByAttr(document, xmlScans, true, true, attrs);
	}

	/**
	 * 获得当前文档中所有带指定属性名的节点,此方法比通过xpath寻找要快,更消耗内存
	 * 
	 * @param document Document文档模型
	 * @param flag 是否精准读取,true->精准读取,false->模糊读取
	 * @param attrs 属性名列表
	 * @return List<Node>
	 */
	public static List<Node> getByAttrs(Document document, XmlScan xmlScans, boolean flag, String... attrs) {
		return getNodesByAttr(document, xmlScans, flag, true, attrs);
	}

	/**
	 * 精准获得当前文档中所有带指定属性值的节点
	 * 
	 * @param document Document文档模型
	 * @param vals 属性值列表
	 * @return List<Node>
	 */
	public static List<Node> getByVals(Document document, XmlScan xmlScans, String... vals) {
		return getNodesByAttr(document, xmlScans, true, false, vals);
	}

	/**
	 * 获得当前文档中所有带指定属性值的节点,此方法比通过xpath寻找要快,更消耗内存
	 * 
	 * @param document Document文档模型
	 * @param flag 是否精准读取,true->精准读取,false->模糊读取
	 * @param vals 属性值列表
	 * @return List<Node>
	 */
	public static List<Node> getByVals(Document document, XmlScan xmlScans, boolean flag, String... vals) {
		return getNodesByAttr(document, xmlScans, flag, false, vals);
	}

	/**
	 * 从文档中通过属性名或值快速查找节点,单一的,只能都是属性名或都是属性值
	 * 
	 * @param document Document文档模型
	 * @param flag 是否精确查找,true->精准查找,false->模糊查找
	 * @param key 是否为属性名查找,true->属性名查找.false->属性值查找
	 * @param params 属性名或属性值列表
	 * @return List<Node>
	 */
	private static List<Node> getNodesByAttr(Document document, XmlScan xmlScans, boolean flag, boolean key,
			String... params) {
		treeWalk(document);
		ArrayList<Node> nodes = new ArrayList<>();
		for (String attr : params) {
			for (Node node : xmlScans.getAttributes()) {
				Attribute attribute = (Attribute) node;
				String desVal = key ? attribute.getName() : attribute.getValue();
				if (!flag && desVal.contains(attr)) {
					nodes.add(node);
				} else if (flag && desVal.equalsIgnoreCase(attr)) {
					nodes.add(node);
				}
			}
		}
		return nodes;
	}

	/**
	 * 没有命名空间,直接利用xpath寻找指定属性名元素节点
	 * 
	 * @param document Document文档模型
	 * @param attrs 属性名
	 * @return List<Node>
	 */
	public static List<Node> getByXPathAttrsNoNS(Document document, String... attrs) {
		return StrTool.isAnyBlank(attrs) ? null : getByXPathNoNS(document, null, attrs);
	}

	/**
	 * 没有命名空间,直接利用xpath寻找指定元素节点
	 * 
	 * @param document Document文档模型
	 * @param xpath xpath表达式的完整路径
	 * @return List<Node>
	 */
	public static List<Node> getByXPathNoNS(Document document, String xpath) {
		return document.selectNodes(xpath);
	}

	/**
	 * 没有命名空间,直接利用xpath寻找指定元素节点 FIXME
	 * 
	 * @param document Document文档模型
	 * @param xpath 完整路径时,attrs必须为null,否则报错,不完整路径时,属性名可由attrs填写,但必须和xpath的分段表达式
	 * @param attr 节点所带的属性名,若是有多个属性,必须对应xpath的分段表达式,否则查不出来
	 * @return List<Node>
	 */
	public static List<Node> getByXPathNoNS(Document document, String xpath, String... attrs) {
		// 没有xpath,只有属性值的时候,取得所有对应的属性值
		if (StrTool.isBlank(xpath)) {
			List<Node> list = new ArrayList<>();
			for (String attr : attrs) {
				if (StrTool.isNotBlank(attr)) {
					list.addAll(document.selectNodes(String.format("//@%s", attr)));
				}
			}
			return list;
		}
		// 完整的xpath路径
		if (StrTool.isAnyBlank(attrs)) {
			return document.selectNodes(xpath);
		}
		// 获得处理后的xpath
		List<String> list = new ArrayList<>();
		for (String attr : attrs) {
			list.add(createAttrVal(attr));
		}
		String desPath = createXPath(xpath, list);
		return document.selectNodes(desPath);
	}

	/**
	 * 没有命名空间,利用属性名来查询节点
	 * 
	 * @param document Document文档模型
	 * @param xpath xpath表达式
	 * @param map 属性键值对,必须与xpath的分段表达式相对应
	 * @return List<Node>
	 */
	public static List<Node> getByXPathNoNS(Document document, String xpath, LinkedHashMap<String, String> map) {
		if (document == null || StrTool.isBlank(xpath)) {
			return null;
		}
		// 完整的xpath路径
		if (MapTool.isEmpty(map)) {
			return document.selectNodes(xpath);
		}
		// 获得处理后的xpath
		String desPath = createXPath(xpath, map);
		return document.selectNodes(desPath);
	}

	/**
	 * 修改XML指定节点的值 FIXME
	 * 
	 * @param source 原xml文件
	 * @param nodes 要修改的节点,xpath路径
	 * @param attributename 属性名称
	 * @param value 新值
	 * @param target 输出文件路径及文件名 如果输出文件为null,则默认为原xml文件
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static void modifyDocument(File source, String nodes, String attributename, String value, String target)
			throws DocumentException, IOException {
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(source);
		List<?> list = document.selectNodes(nodes);
		Iterator<?> iter = list.iterator();
		while (iter.hasNext()) {
			Attribute attribute = (Attribute) iter.next();
			if (attribute.getName().equals(attributename)) {
				attribute.setValue(value);
			}
		}
		XMLWriter output;
		// FIXME 是否需要输出,后者修改后可以直接反应到文件中
		if (target != null) {
			// 指定输出文件
			output = new XMLWriter(new FileWriter(new File(target)));
		} else {
			// 输出文件为原文件
			output = new XMLWriter(new FileWriter(source));
		}
		output.write(document);
		output.close();
	}

	/**
	 * 新建一个Attribute,无Namespace
	 * 
	 * @param element Attribute所属元素
	 * @param name 属性名
	 * @param value 属性值
	 * @return Attrbute
	 */
	public static Attribute newAttribute(Element element, String name, String value) {
		return DocumentHelper.createAttribute(element, name, value);
	}

	/**
	 * 新建一个Attribute,有Namespace
	 * 
	 * @param element Attribute所属元素
	 * @param name 带命名空间的属性名
	 * @param value 属性值
	 * @return Attrbute
	 */
	public static Attribute newAttribute(Element element, QName qname, String value) {
		return DocumentHelper.createAttribute(element, qname, value);
	}

	/**
	 * 新建一个Document文档模型,无命名空间
	 * 
	 * @return {@link Document}文档模型
	 */
	public static Document newDocument() {
		return DocumentHelper.createDocument();
	}

	/**
	 * 新建一个Document文档模型,同时指定根元素,无命名空间
	 * 
	 * @param root 根元素名
	 * @return {@link Document}文档模型
	 */
	public static Document newDocument(String root) {
		return DocumentHelper.createDocument(DocumentHelper.createElement(root));
	}

	/**
	 * 新建一个XML元素,无命名空间,同使用{@link DefaultElement}
	 * 
	 * @param elementName 元素的标签名
	 * @return DefaultElement 元素
	 */
	public static Element newElement(String elementName) {
		return DocumentHelper.createElement(elementName);
	}

	/**
	 * 根据Bean新建一个XML元素,无命名空间
	 * 
	 * @param elementName 元素的标签名
	 * @param bean Bean实例
	 * @return BeanElement 元素
	 */
	public static <T> Element newElement(String elementName, T bean) {
		return new BeanElement(elementName, bean);
	}

	/**
	 * 新建一个文本节点
	 * 
	 * @param text 文本
	 * @return DefaultText 节点
	 */
	public static Text newText(String text) {
		return DocumentHelper.createText(text);
	}

	/**
	 * 新建一个文本节点,并将其添加到父节点中
	 * 
	 * @param element 父节点元素
	 * @param text 文本
	 * @return DefaultText 节点
	 */
	public static DefaultText newText(Element element, String text) {
		return new DefaultText(element, text);
	}

	/**
	 * 将Map转换为XML,无Namespace
	 *
	 * @param data Map类型数据
	 * @param rootName 根节点名
	 * @return Document
	 */
	public static Document mapToXml(Map<?, ?> data, String rootName) {
		return mapToXml(data, rootName, null);
	}

	/**
	 * 将Map转换为XML
	 *
	 * @param data Map类型数据
	 * @param rootName 根节点名
	 * @param namespace 命名空间
	 * @return Document
	 */
	public static Document mapToXml(Map<?, ?> data, String rootName, String namespace) {
		final Document document = newDocument(rootName);
		mapToXml(data, document.getRootElement(), null);
		return document;
	}

	/**
	 * 将Map转换为XML
	 *
	 * @param map map数据,value只能是字符串或Map
	 * @param element 根节点
	 * @param function 子元素标签名处理方法
	 * @return Document
	 */
	public static void mapToXml(Map<?, ?> map, Element element, Function<Object, String> function) {
		for (Map.Entry<?, ?> entry : map.entrySet()) {
			if (null == entry.getKey()) {
				continue;
			}
			element.addElement(null == function ? String.valueOf(entry.getKey()) : function.apply(entry.getKey()));
			Class<?> clsValue = entry.getValue().getClass();
			if (Map.class.isAssignableFrom(clsValue)) {
				mapToXml((Map<?, ?>) entry.getValue(), element, function);
			} else if (Iterator.class.isAssignableFrom(clsValue)) {
				element.addText(CollectionTool.join(",", (Iterator<?>) entry.getValue()));
			} else if (clsValue.isArray()) {
				element.addText(ArrayTool.join(",", (Object[]) entry.getValue()));
			} else {
				element.addText(String.valueOf(entry.getValue()));
			}
		}
	}

	/**
	 * 将字符串解析为XML
	 * 
	 * @param text 需要解析的文本
	 * @return Document 文档模型
	 * @throws DocumentException
	 */
	public static Document parse(String text) throws DocumentException {
		return DocumentHelper.parseText(text);
	}

	/**
	 * 解析本地的XML文件,默认解析注释
	 * 
	 * @param file XML文件
	 * @return Document文档模型
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static final Document read(File file) throws IOException, DocumentException {
		return read(file, false);
	}

	/**
	 * 解析本地的XML文件,解析完之后自动关闭流
	 * 
	 * @param file XML文件
	 * @param ignoreDoc 是否忽略注释,true->忽略,false->不忽略
	 * @return Document文档模型
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static final Document read(File file, boolean ignoreDoc) throws IOException, DocumentException {
		try (InputStream is = FileTool.newInputStream(file);) {
			return read(is, ignoreDoc);
		}
	}

	/**
	 * 解析XML文件的字节输入流,需要调用方法关闭流,默认解析注释
	 * 
	 * @param is 输入流
	 * @return Document文档模型
	 * @throws DocumentException
	 */
	public static final Document read(InputStream is) throws DocumentException {
		return read(is, false);
	}

	/**
	 * 解析XML文件的字节输入流,需要调用方法关闭流
	 * 
	 * @param is 输入流
	 * @param ignoreDoc 是否忽略注释,true->忽略,false->不忽略
	 * @return Document文档模型
	 * @throws DocumentException
	 */
	public static final Document read(InputStream is, boolean ignoreDoc) throws DocumentException {
		SAXReader reader = new SAXReader();
		reader.setIgnoreComments(ignoreDoc);
		return reader.read(AssertTool.notNull(is));
	}

	/**
	 * 解析本地的XML文件,默认解析注释
	 * 
	 * @param filePath XML文件绝对路径
	 * @return Document文档模型
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static final Document read(String filePath) throws IOException, DocumentException {
		return read(FileTool.checkFile(filePath), false);
	}

	/**
	 * 解析本地的XML文件
	 * 
	 * @param filePath XML文件绝对路径
	 * @param ignoreDoc 是否忽略注释,true->忽略,false->不忽略
	 * @return Document文档模型
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static final Document read(String filePath, boolean ignoreDoc) throws IOException, DocumentException {
		return read(FileTool.checkFile(filePath), ignoreDoc);
	}

	/**
	 * 解析URL中的的XML文档
	 * 
	 * @param url URL资源地址
	 * @return Document文档模型
	 * @throws DocumentException
	 */
	public static final Document read(URL url) throws DocumentException {
		SAXReader reader = new SAXReader();
		return reader.read(url);
	}

	/**
	 * 解析URL中的的XML文档
	 * 
	 * @param url URL资源地址
	 * @param dtd 是否进行dtd验证,true->验证,有网的时候可用,false->不验证,断网时可用
	 * @return Document文档模型
	 * @throws DocumentException
	 */
	public static final Document read(URL url, boolean dtd) throws DocumentException {
		SAXReader reader = new SAXReader(dtd);
		return reader.read(url);
	}

	/**
	 * 解析较大的XML文件,未实现 FIXME
	 * 
	 * @param path xml文件绝对路径
	 * @return List<Element>
	 */
	public static List<Element> readLarge(String filePath) {
		SAXReader reader = new SAXReader();
		List<Element> result = new ArrayList<>();
		reader.addHandler(filePath, new ElementHandler() {

			@Override
			public void onStart(ElementPath elementPath) {
				Element element = elementPath.getCurrent();
				if (!"bean".equals(element.getName())) {
					element.detach();
				}
			}

			@Override
			public void onEnd(ElementPath elementPath) {
				result.add(elementPath.getCurrent());
			}
		});
		return result;
	}

	/**
	 * 删除节点 FIXME
	 * 
	 * @param source xml源文件
	 * @param xpath 需要删除的节点,xpath路径
	 * @param target 新文件
	 * @throws DocumentException
	 * @throws IOException
	 */
	public static void removeNode(File source, String xpath, File target) throws DocumentException, IOException {
		SAXReader saxReader = new SAXReader();
		Document document = saxReader.read(source);
		List<Node> list = document.selectNodes(xpath);
		for (Node node : list) {
			document.remove((Element) node);
		}
		// FIXME 是否需要写入新文件,或是直接在源文件上已经修改
		XMLWriter writer = new XMLWriter(new FileWriter(target));
		writer.write(document);
		writer.close();
	}

	/**
	 * 遍历解析文档,不需要为每一个节点创建迭代对象
	 * 
	 * @param document {@link Document}文档模型
	 * @return {@link XmlScan}快速解析对象
	 */
	public static XmlScan treeWalk(Document document) {
		XmlScan xmlScans = new XmlScan();
		xmlScans.setRootElement(document.getRootElement());
		treeWalk(xmlScans, document.getRootElement());
		return xmlScans;
	}

	/**
	 * 遍历解析元素,快速循环
	 * 
	 * @param xmlScans 快速解析对象
	 * @param element 元素节点
	 */
	private static void treeWalk(XmlScan xmlScans, Element element) {
		xmlScans.getNodes().add(element);
		xmlScans.getElements().add(element);
		if (ListTool.isNotEmpty(element.attributes())) {
			xmlScans.getAttributes().addAll(element.attributes());
		}
		xmlScans.getNamespaces().addAll(element.declaredNamespaces());
		Iterator<Node> iterator = element.nodeIterator();
		while (iterator.hasNext()) {
			Node node = iterator.next();
			if (node instanceof Element) {
				treeWalk(xmlScans, (Element) node);
			}
			if (node instanceof Text) {
				xmlScans.getTexts().add((Text) node);
			}
		}
	}

	/**
	 * 对xpath进行分段处理,去掉空值,将xpath分段所对应的属性名值加上 FIXME
	 * 
	 * @param attrs:xpath分段之后所对应的名值对,名不可为空,值可为空;名为空时,对应的xpath分段将不做任务处理
	 *        若分段表达式对应的一些名值对没有,则可传一个EMPTY@+一个随机数的值作为key,值可不传,如EMPTY@1234
	 * @Fixed 此处没有解决重复标签的属性名相同的问题
	 */
	private static String createXPath(String xpath, LinkedHashMap<String, String> map) {
		// 处理attrs,LinkedHashMap是一个有序的集合
		List<String> attrs = new ArrayList<>();
		Iterator<Entry<String, String>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Entry<String, String> entry = it.next();
			String key = entry.getKey();
			// 对空属性做处理
			if (key.contains("EMPTY@")) {
				attrs.add("");
			} else {
				attrs.add(createAttrVal(key, entry.getValue()));
			}
		}
		return createXPath(xpath, attrs);
	}

	/**
	 * 对xpath进行分段处理,去掉空值,将xpath分段所对应的属性名加上 FIXME
	 */
	private static String createXPath(String xpath, List<String> attrs) {
		String[] tags = xpath.split("/+|//+");
		// 利用Arrays.aslist转换来的数组无法进行增删操作,不构造函数重新赋值
		List<String> list = new ArrayList<>(Arrays.asList(tags));
		// 为了避免因为第一次切割产生空值,需要对空值进行剔除
		for (int i = list.size() - 1; i >= 0; i--) {
			if (StrTool.isBlank(list.get(i))) {
				list.remove(i);
			}
		}
		// 将属性全部拼接到对应的位置,因为不能改变路径,暂时只能用笨办法,每次都截取字符串,改变匹配的字符串,依次截取下去
		List<String> desList = new ArrayList<>();
		int index = 0;
		for (int i = 0; i < list.size(); i++) {
			String tag = list.get(i);
			// 对应的属性名,防止数组越界
			String attr = attrs.size() > i ? attrs.get(i) : "";
			// 从上次的位置开始查找符合的标签
			int first = xpath.indexOf(tag, index);
			// 截取符合的标签并拼上对应的属性名
			String des = String.format("%s%s", xpath.substring(index, first + tag.length()), attr);
			// 改变下次开始寻找的下标
			index = first + tag.length();
			desList.add(des);
		}
		return String.join("", desList);
	}

	/**
	 * 格式化属性名 FIXME
	 */
	private static final String createAttrVal(String attr) {
		return createAttrVal(attr, null);
	}

	/**
	 * 格式化属性名值 FIXME
	 */
	private static final String createAttrVal(String attr, String value) {
		return StrTool.isBlank(attr) ? ""
				: StrTool.isBlank(value) ? String.format("[@%s]", attr) : String.format("[@%s='%s']", attr, value);
	}

	/**
	 * 将XML优雅格式化并写入文件,使用XML自带字符编码
	 * 
	 * @param file 文件
	 * @param document Document文档模型
	 * @throws IOException
	 */
	public static void write(File file, Document document) throws IOException {
		write(file, document, true, document.getXMLEncoding());
	}

	/**
	 * 将XML写入文件,使用XML自带字符编码
	 * 
	 * @param file 文件
	 * @param document Document文档模型
	 * @param pretty 是否优雅格式化输出,true->优雅格式,false->默认紧凑格式
	 * @throws IOException
	 */
	public static void write(File file, Document document, boolean pretty) throws IOException {
		write(file, document, pretty, document.getXMLEncoding());
	}

	/**
	 * 将XML格式化并写入文件,使用指定编码
	 * 
	 * @param file 文件
	 * @param document Document文档模型
	 * @param pretty 是否优雅格式化输出,true->优雅格式,false->默认紧凑格式
	 * @param charset 字符编码
	 * @throws IOException
	 */
	public static void write(File file, Document document, boolean pretty, Charset charset) throws IOException {
		XMLWriter xmlWriter = null;
		try (OutputStreamWriter osw = FileTool.newOutputStreamWriter(file, CharsetTool.defaultCharset(charset));) {
			if (pretty) {
				OutputFormat format = OutputFormat.createPrettyPrint();
				// 紧凑的格式,format也可以不要
				// format = OutputFormat.createCompactFormat();
				format.setEncoding(CharsetTool.defaultCharset(charset).name());
				// 设置换行
				format.setNewlines(pretty);
				// 生成缩进
				format.setIndent(pretty);
				// 使用4个空格进行缩进,可以兼容文本编辑器
				format.setIndent(" ");
				format.setIndentSize(4);
				xmlWriter = new XMLWriter(osw, format);
			} else {
				xmlWriter = new XMLWriter(osw);
			}
			if (null != xmlWriter) {
				xmlWriter.write(document);
				xmlWriter.flush();
				xmlWriter.close();
			}
		}
	}

	/**
	 * 将XML写入文件,使用指定编码
	 * 
	 * @param file 文件
	 * @param document Document文档模型
	 * @param pretty 是否优雅格式化输出,true->优雅格式,false->默认紧凑格式
	 * @param charsetName 字符编码名
	 * @throws IOException
	 */
	public static void write(File file, Document document, boolean pretty, String charsetName) throws IOException {
		write(file, document, pretty, CharsetTool.defaultCharset(charsetName));
	}

	/**
	 * 将XML写入文件
	 * 
	 * @param filePath 文件绝对路径
	 * @param document Document文档模型
	 * @param pretty 是否优雅格式化输出,true->优雅格式,false->默认紧凑格式
	 * @param charsetName 字符编码名
	 * @throws IOException
	 */
	public static void write(String filePath, Document document) throws IOException {
		write(filePath, document, true, document.getXMLEncoding());
	}

	/**
	 * 将XML写入文件
	 * 
	 * @param filePath 文件绝对路径
	 * @param document Document文档模型
	 * @param pretty 是否优雅格式化输出,true->优雅格式,false->默认紧凑格式
	 * @param charse 字符编码
	 * @throws IOException
	 */
	public static void write(String filePath, Document document, boolean pretty) throws IOException {
		write(filePath, document, pretty, CharsetTool.defaultCharset());
	}

	/**
	 * 将XML写入文件
	 * 
	 * @param filePath 文件绝对路径
	 * @param document Document文档模型
	 * @param pretty 是否优雅格式化输出,true->优雅格式,false->默认紧凑格式
	 * @param charse 字符编码
	 * @throws IOException
	 */
	public static void write(String filePath, Document document, boolean pretty, Charset charse) throws IOException {
		write(FileTool.checkFile(filePath), document, pretty, CharsetTool.defaultCharset(charse));
	}

	/**
	 * 将XML写入文件
	 * 
	 * @param filePath 文件绝对路径
	 * @param document Document文档模型
	 * @param pretty 是否优雅格式化输出,true->优雅格式,false->默认紧凑格式
	 * @param charsetName 字符编码名
	 * @throws IOException
	 */
	public static void write(String filePath, Document document, boolean pretty, String charsetName)
			throws IOException {
		write(filePath, document, pretty, CharsetTool.defaultCharset(charsetName));
	}

	/**
	 * 写入一个XML文件,有Namespace
	 * 
	 * @param filePath xml文件地址绝对路径
	 * @param rootXmlBean {@link XmlBean}根元素
	 * @param xmlBeans {@link XmlBean}其他元素
	 * @throws IOException
	 */
	public static void writeNS(String filePath, XmlBean rootXmlBean, List<XmlBean> xmlBeans) throws IOException {
		Document document = DocumentHelper.createDocument();
		document.setXMLEncoding(CharsetTool.defaultCharset().name());
		// 添加根元素
		Element root = document.addElement(rootXmlBean.getElementName());
		writeNS(root, rootXmlBean);
		// 添加子元素
		if (ListTool.isNotEmpty(xmlBeans)) {
			for (XmlBean xmlbean : xmlBeans) {
				// 生成本节点
				Element element = writeNSElement(root, xmlbean);
				// 添加子节点
				writeNSChild(element, xmlbean);
			}
		}
		// 写入文件中
		write(filePath, document, true, CharsetTool.defaultCharset());
	}

	/**
	 * 在元素中添加Namespace和Attribute,Text
	 * 
	 * @param element 元素节点
	 * @param xmlBean xml节点信息
	 */
	private static void writeNS(Element element, XmlBean xmlBean) {
		// 增加命名空间
		List<Namespace> namespaces = xmlBean.getNamespaces();
		for (Namespace namespace : namespaces) {
			element.add(namespace);
		}
		// 增加属性
		List<Attribute> attributes = xmlBean.getAttributes();
		for (Attribute attribute : attributes) {
			element.add(attribute);
		}
		// 增加文本
		element.addText(null == xmlBean.getText() ? xmlBean.getText().getText() : xmlBean.getTextValue());
	}

	/**
	 * 写入元素节点的子节点元素
	 * 
	 * @param parent 上级元素节点
	 * @param xmlBean 下级元素节点信息
	 * @return {@link Element} 下级元素节点
	 */
	private static Element writeNSElement(Element parent, XmlBean xmlBean) {
		AssertTool.notNull(xmlBean);
		AssertTool.notBlank(xmlBean.getElementName());
		// 新增根元素子节点
		Element element = parent.addElement(xmlBean.getElementName());
		writeNS(element, xmlBean);
		return element;
	}

	/**
	 * 添加子节点
	 * 
	 * @param parent 父节点
	 * @param parentXmlBean 上级节点信息
	 */
	private static void writeNSChild(Element parent, XmlBean parentXmlBean) {
		List<XmlBean> childrens = parentXmlBean.getChildren();
		if (null != childrens && childrens.size() > 0) {
			for (XmlBean xmlBean : childrens) {
				Element element = writeNSElement(parent, xmlBean);
				writeNSChild(element, xmlBean);
			}
		}
	}

	/**
	 * 将XML文档转换为String,字符编码使用XML文档中的编码,获取不到则使用UTF-8
	 *
	 * @param doc XML文档
	 * @param charset 字符编码
	 * @param pretty 是否格式化输出,true->是,false->否
	 * @return XML字符串
	 * @throws IOException
	 */
	public static String writeToStr(Node node, Charset charset, boolean pretty) throws IOException {
		final StringWriter writer = new StringWriter();
		return writeToStr(node, writer, charset, pretty);
	}

	/**
	 * 将XML文档转换为String,字符编码使用XML文档中的编码,获取不到则使用UTF-8
	 *
	 * @param doc XML文档
	 * @param charset 字符编码
	 * @param pretty 是否格式化输出,true->是,false->否
	 * @return XML字符串
	 * @throws IOException
	 */
	public static String writeToStr(Node node, StringWriter writer, Charset charset, boolean pretty)
			throws IOException {
		Document document = node.getDocument();
		XMLWriter xmlWriter = null;
		if (pretty) {
			OutputFormat outputFormat = OutputFormat.createPrettyPrint();
			outputFormat.setEncoding(charset.name());
			xmlWriter = new XMLWriter(writer, outputFormat);
		} else {
			xmlWriter = new XMLWriter(writer);
		}
		xmlWriter.write(document);
		xmlWriter.close();
		return writer.toString();
	}
}