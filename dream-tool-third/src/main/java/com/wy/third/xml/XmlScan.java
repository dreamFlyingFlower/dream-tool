package com.wy.third.xml;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.Text;

/**
 * Xml文件扫描,大量使用同一个Xml文件中的信息时使用,容易内存溢出,慎用
 * 
 * @author 飞花梦影
 * @date 2021-03-18 10:06:34
 * @git {@link https://github.com/dreamFlyingFlower}
 */
public class XmlScan {

	/**
	 * Xml中所有Attribute
	 */
	private List<Attribute> attributes = new ArrayList<>();

	/**
	 * Xml中所有Element节点
	 */
	private List<Element> elements = new ArrayList<>();

	/**
	 * Xml中所有Namespace
	 */
	private Set<Namespace> namespaces = new HashSet<>();

	/**
	 * Xml中所有节点,包括Element,Text,Namespace
	 */
	private List<Node> nodes = new ArrayList<>();

	/**
	 * 根元素
	 */
	private Element rootElement;

	/**
	 * Xml中所有Text节点
	 */
	private List<Text> texts = new ArrayList<>();

	public List<Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(List<Attribute> attributes) {
		this.attributes = attributes;
	}

	public List<Element> getElements() {
		return elements;
	}

	public void setElements(List<Element> elements) {
		this.elements = elements;
	}

	public Set<Namespace> getNamespaces() {
		return namespaces;
	}

	public void setNamespaces(Set<Namespace> namespaces) {
		this.namespaces = namespaces;
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	public Element getRootElement() {
		return rootElement;
	}

	public void setRootElement(Element rootElement) {
		this.rootElement = rootElement;
	}

	public List<Text> getTexts() {
		return texts;
	}

	public void setTexts(List<Text> texts) {
		this.texts = texts;
	}
}