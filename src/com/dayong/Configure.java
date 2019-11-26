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
 * ���ܣ�
 * 1.����yml��ʽ�������ļ����Զ���װ������map����
 * 2.ͨ��ognl�ַ�����ȡ��ָ����ֵ
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
		//��jackson���yml��� ���� yml��ʽ�������ļ�����
		ObjectMapper m=new ObjectMapper(new YAMLFactory());
		try {
			map=m.readValue(file,Map.class);
		} catch (Exception e) {
			System.out.println("��ȡyml�ļ�ʧ��");
			e.printStackTrace();
		}
		
	}
	public static String getStringByOgnl(String expression) {
		//������ʽȡ�����Ի�ȡ�����ı��ʽ��${a.b.c}
		expression=expression.replaceAll("\\s+", "");
		expression=expression.substring(2,expression.length()-1);
		//System.out.println(expression);
		//ʹ��ognl����ȥ����map�ṹ�л�ȡ���ʽ��ֵ
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
