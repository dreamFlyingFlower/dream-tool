package com.wy.third.xml;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Node;
import org.dom4j.Text;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Xml文件扫描,大量使用同一个Xml文件中的信息时使用,容易内存溢出,慎用
 * 
 * @author 飞花梦影
 * @date 2021-03-18 10:06:34
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class XmlScan implements Serializable {

	private static final long serialVersionUID = 554826552987103736L;

	/**
	 * Xml中所有Attribute
	 */
	@Builder.Default
	private List<Attribute> attributes = new ArrayList<>();

	/**
	 * Xml中所有Element节点
	 */
	@Builder.Default
	private List<Element> elements = new ArrayList<>();

	/**
	 * Xml中所有Namespace
	 */
	@Builder.Default
	private Set<Namespace> namespaces = new HashSet<>();

	/**
	 * Xml中所有节点,包括Element,Text,Namespace
	 */
	@Builder.Default
	private List<Node> nodes = new ArrayList<>();

	/**
	 * 根元素
	 */
	private Element rootElement;

	/**
	 * Xml中所有Text节点
	 */
	@Builder.Default
	private List<Text> texts = new ArrayList<>();
}