package com.wy.third.xml;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.Text;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * XML命名空间相关
 * 
 * @author 飞花梦影
 * @date 2021-03-18 09:47:27
 * @git {@link https://github.com/dreamFlyingFlower}
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class XmlBean implements Serializable {

	private static final long serialVersionUID = -4591056606249675292L;

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
}