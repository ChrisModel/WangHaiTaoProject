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
	
	//������������,��yml�������ݽ���,����һ��map����
	public static void load() throws Exception {
		//��ȡ�ļ�������·��
		String path = Configure.class.getResource("/application.yml").getPath();
		path = URLDecoder.decode(path, "UTF-8");
		//ʹ�� jackson + yaml ���,����������yaml��ʽ����
		ObjectMapper m = new ObjectMapper(new YAMLFactory());
		//��ȡ�����ļ�,�����ݴ����һ�� map ����
		map = m.readValue(new File(path), Map.class);
	}
	
	/*
	 * "${spring.datasource.driver}"  ---- OGNL 
	 */
	public static String get(String exp) throws Exception {
		//�ո��滻�ɿ�
		exp = exp.replaceAll("\\s+", "");
		//��${}ȥ��
		exp = exp.substring(2, exp.length()-1);
		
		//��ognl����,ʹ�����Ա��ʽ��map��ȡ����
		String value = (String) Ognl.getValue(exp, map);
		return value;
	}
	
	public static void main(String[] args) throws Exception {
		Configure.load();
		
		System.out.println(map);
		System.out.println(Configure.get("${spring.datasource.driver}"));
	}
}









