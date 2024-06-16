package dream.flying.flower;

/**
 * Escape转义字符
 *
 * @author 飞花梦影
 * @date 2022-05-15 15:03:33
 * @git {@link https://gitee.com/dreamFlyingFlower}
 */
public interface ConstEscape {

	/** &转义-> &amp; */
	String AMP = "&amp;";

	/** 单引号转义-> &#039;,在新版本中使用->&apos; */
	String APOS = "&#039;";

	/** 大于号转义-> &gt; */
	String GT = "&gt;";

	/** 小于号转义-> &lt; */
	String LT = "&lt;";

	/** 空格转义-> &nbsp; */
	String NBSP = "&nbsp;";

	/** 双引号转义-> &quot; */
	String QUOTE = "&quot;";
}