package day18.spring;

import java.io.File;
import java.net.URLDecoder;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import ognl.Ognl;
import ognl.OgnlException;

public class Configure {
	private static Map<String, Map> map;
	
	//用来加载配置,把yml配置数据解析,生成一个map集合
	public static void load() throws Exception {
		//获取文件的完整路径
		String path = Configure.class.getResource("/application.yml").getPath();
		path = URLDecoder.decode(path, "UTF-8");
		//使用 jackson + yaml 插件,来解析处理yaml格式数据
		ObjectMapper m = new ObjectMapper(new YAMLFactory());
		//读取配置文件,把数据处理成一个 map 对象
		map = m.readValue(new File(path), Map.class);
	}
	
	/*
	 * "${spring.datasource.driver}"  ---- OGNL 
	 */
	public static String get(String exp) throws Exception {
		//空格替换成空
		exp = exp.replaceAll("\\s+", "");
		//把${}去掉
		exp = exp.substring(2, exp.length()-1);
		
		//用ognl工具,使用属性表达式从map提取数据
		String value = (String) Ognl.getValue(exp, map);
		return value;
	}
	
	public static void main(String[] args) throws Exception {
		Configure.load();
		
		System.out.println(map);
		System.out.println(Configure.get("${spring.datasource.driver}"));
	}
}









