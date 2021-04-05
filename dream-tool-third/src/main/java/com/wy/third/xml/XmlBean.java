package com.wy.third.xml;

import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Text;

/**
 * XML命名空间相关
 * 
 * @author 飞花梦影
 * @date 2021-03-18 09:47:27
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class XmlBean {

	private List<Attribute> attributes;

	private List<Map<String, String>> attributeMaps;

	private List<XmlBean> children;

	private Element element;

	private String elementName;

	private boolean isRoot;

	private Namespace namespace;

	private List<Namespace> namespaces;

	private List<Map<String, String>> namespaceMaps;

	private Text text;

	private String textValue;

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	public List<Map<String, String>> getAttributeMaps() {
		return attributeMaps;
	}

	public void setAttributeMaps(List<Map<String, String>> attributeMaps) {
		this.attributeMaps = attributeMaps;
	}

	public List<XmlBean> getChildren() {
		return children;
	}

	public void setChildren(List<XmlBean> children) {
		this.children = children;
	}

	public Element getElement() {
		return element;
	}

	public void setElement(Element element) {
		this.element = element;
	}

	public String getElementName() {
		return elementName;
	}

	public void setElementName(String elementName) {
		this.elementName = elementName;
	}

	public boolean isRoot() {
		return isRoot;
	}

	public void setRoot(boolean isRoot) {
		this.isRoot = isRoot;
	}

	public Namespace getNamespace() {
		return namespace;
	}

	public void setNamespace(Namespace namespace) {
		this.namespace = namespace;
	}

	public List<Namespace> getNamespaces() {
		return namespaces;
	}

	public void setNamespaces(List<Namespace> namespaces) {
		this.namespaces = namespaces;
	}

	public List<Map<String, String>> getNamespaceMaps() {
		return namespaceMaps;
	}

	public void setNamespaceMaps(List<Map<String, String>> namespaceMaps) {
		this.namespaceMaps = namespaceMaps;
	}

	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}

	public String getTextValue() {
		return textValue;
	}

	public void setTextValue(String textValue) {
		this.textValue = textValue;
	}

}