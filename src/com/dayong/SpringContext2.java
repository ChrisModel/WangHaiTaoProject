package com.dayong;
/**
 * SpringContext�Ĵ��������̫���ˣ�
 * �����SpringContext����н������
 * */

import java.io.File;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import ognl.Ognl;

import java.util.Set;


public class SpringContext2 {
	private Map<String, Object> map=new HashMap<String, Object>();
	public static void main(String[] args) {
		SpringContext2	s=new SpringContext2();
		s.autoScan();
		UserController userController=s.getObjectInMap(UserController.class);
		userController.show();
		UserService userService=s.getObjectInMap(UserService.class);
		userService.show();
		UserDao userDao=s.getObjectInMap(UserDao.class);
		System.out.println(userDao.toString());
		
	}
	
	private <E> E getObjectInMap(Class<E> c) {
		String name=c.getName();
		E e=(E) map.get(name);
		return  e;
	}
	private void autoScan() {
		StringBuilder sb=new StringBuilder();
		//����������Ӧע��Ķ��󣬲�����map����
		scan(new File("bin"), sb);
		System.out.println(map);
		//��map���������еĶ����Զ�ע������ֵ
		InjectionValueToField();
		//�鿴���ж�������ֵ�Ƿ���ȷע��
		System.err.println(map);
	}

	private void InjectionValueToField() {
		Set<Entry<String, Object>> set=map.entrySet();
		Iterator<Entry<String,Object>> iterator=set.iterator();
		while(iterator.hasNext()) {
			Entry<String,Object> entry=iterator.next();
			Object o=entry.getValue();
			Class<? extends Object> c=o.getClass();
			Field[] fields=c.getDeclaredFields();
			for(Field f:fields) {
				if(f.isAnnotationPresent(Value.class)) {
					injectionValue(o, f);
				}else if(f.isAnnotationPresent(AutoWired.class)) {
					injectionObject(o, c, f);
				}
			}
		}
	}
	/*�������ܵ�һ���������Զ�ɨ�����еİ����࣬����������*/
	private void scan(File file ,StringBuilder dir) {
		File[] files=file.listFiles();
		if(files.length==0) {
			return;
		}//24.228
		for(File f:files) {
			//��Ϊ������Ҫ�����õ��ļ�(��)�����֣���˴˴�����ֲ����������Ա㷴��ʹ��
			String name=f.getName();
			if(f.isDirectory()) {
				if(!(dir.length()==0)) {
					dir.append(".");
				}
				dir.append(name);
				scan(f, dir);
				//�������ļ��к�ָ�dir
				int i=dir.lastIndexOf(".");
//				if(i!=-1) {
//					dir.delete(i,dir.length());
//				}else {
//					dir.delete(0,dir.length());
//				}
				//����һ�д����ͬ����������
				dir.delete(i==-1?0:i,dir.length());
			}else {
				//ƴ�������õ����ļ���
				String fName=dir.toString()+"."+name;
				//��ʼ���������õ����ļ�
				if(fName.startsWith("com")&&fName.endsWith(".class")) {
					try {
						//ȥ����׺.class
						fName=fName.substring(0, fName.lastIndexOf(".class"));
						Class<? extends Object> c=Class.forName(fName);
						if(c.isAnnotationPresent(Component.class)
								||c.isAnnotationPresent(Controller.class)
								||c.isAnnotationPresent(Service.class)) {
							Object o=c.newInstance();
							map.put(fName,o);
						}
					} catch (Exception e) {
						System.err.println("scan���䴴������ʧ�ܣ�");
					}
				}
			}
		}
		
	}
	private void injectionValue(Object o,Field f) {
		f.setAccessible(true);
		Value v=f.getAnnotation(Value.class);
		String ognl;
		if("".equals(v.ognl())) {
			ognl=v.value();
		}else {
			ognl=v.ognl();
		}
		try {
			f.set(o, Configure.getStringByOgnl(ognl));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void injectionObject(Object o, Class<? extends Object> c,Field f) {
		f.setAccessible(true);
		try {
			f.set(o,map.get(f.getType().getName()));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
