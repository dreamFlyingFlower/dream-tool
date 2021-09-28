package com.wy.third.crawler;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Objects;

import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.wy.collection.CollectionTool;

/**
 * 爬虫工具测试类
 *
 * @author 飞花梦影
 * @date 2021-09-26 14:38:04
 * @git {@link https://github.com/dreamFlyingFlower }
 */
public class CrawlerTools {

	public static void main(String[] args) {
		CloseableHttpClient httpClient = HttpClients.createDefault();
		CloseableHttpResponse response = null;
		HttpGet request = new HttpGet("https://www.autohome.com.cn/suv/#pvareaid=3311952");
		request.setHeader("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
		// HttpHost proxy = new HttpHost("60.13.42.232", 9999);
		// RequestConfig config = RequestConfig.custom().setProxy(proxy).build();
		// request.setConfig(config);
		try (BufferedWriter bos =
				new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File("D:\\downloads\\test.txt"))))) {
			response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
				HttpEntity httpEntity = response.getEntity();
				String html = EntityUtils.toString(httpEntity, "utf-8");
				Document document = Jsoup.parse(html);
				Element element = document.getElementById("tab-content");
				Elements postItems = element.getElementsByClass("uibox");
				// 循环处理每篇博客
				for (Element postItem : postItems) {
					// 像jquery选择器一样,获取文章标题元素
					Elements titleEle = postItem.select(".rank-list-ul");
					for (Element tempDiv : titleEle) {
						Elements lis = tempDiv.getElementsByTag("li");
						for (Element element2 : lis) {
							StringBuilder sb = new StringBuilder("名称:");
							Elements elements = element2.select("h4>a");
							if (CollectionTool.isNotEmpty(elements)) {
								sb.append(elements.get(0).text());
							}
							sb.append("||");
							Element selectFirst = element2.selectFirst("div");
							if (Objects.nonNull(selectFirst)) {
								sb.append(selectFirst.text());
							}
							bos.write(sb.toString());
							bos.newLine();
						}
					}
					// System.out.println("文章地址:" + titleEle.attr("href"));
					// Elements footEle = postItem.select(".post_item_foot a[class='lightblue']");
				}
			} else {
				System.out.println("异常");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			HttpClientUtils.closeQuietly(response);
			HttpClientUtils.closeQuietly(httpClient);
		}
	}
}