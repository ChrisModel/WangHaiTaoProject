package com.dayong;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import ognl.Ognl;
import ognl.OgnlException;

/**
 * 功能：
 * 1.解析yml格式的配置文件，自动封装成三层map对象
 * 2.通过ognl字符串获取到指定的值
 * */
@Component
public class Configure {
	private static Map<String, Map<String, String>> map;
	static {
		initMap();
	}
	private static void initMap() {
		File file=new File("bin"+File.separator+"application.yml");
		//System.out.println(file.getAbsolutePath());
		//由jackson配合yml插件 处理 yml格式的配置文件数据
		ObjectMapper m=new ObjectMapper(new YAMLFactory());
		try {
			map=m.readValue(file,Map.class);
		} catch (Exception e) {
			System.out.println("读取yml文件失败");
			e.printStackTrace();
		}
		
	}
	public static String getStringByOgnl(String expression) {
		//处理表达式取出，以获取完整的表达式，${a.b.c}
		expression=expression.replaceAll("\\s+", "");
		expression=expression.substring(2,expression.length()-1);
		//System.out.println(expression);
		//使用ognl工具去三层map结构中获取表达式的值
		Object s="";
		try {
			s=Ognl.getValue(expression,map);
		} catch (OgnlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s.toString();
	}
	
}
