package com.dayong;
/**
 * 功能：
 * 1.IOC：自动扫描文件夹下所有的类，并根据相应的注解创建对象、自动注入对象、自动注入属性值
 * 2.封装map对象，对外提供通过类名 拿到 相应对象使用的功能
 * */

import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.activation.FileDataSource;




public class SpringContext {
	private Map<String,Object> map=new HashMap<String, Object>();
	public static void main(String[] args) {
		SpringContext springContext=new SpringContext();
		springContext.autoScan();
		Map<String,Object> map=springContext.map;
		Set<Entry<String,Object>> set=map.entrySet();
		for (Entry<String, Object> entry : set) {
			Object object=entry.getValue();
			if(entry.getKey().equals("com.dayong.UserController")) {
				UserController userController=(UserController)object;
				userController.show();
			}else if(entry.getKey().equals("com.dayong.UserService")) {
				UserService userService=(UserService)object;
				userService.show();
			}else if(entry.getKey().equals("com.dayong.UserDao")) {
				UserDao userDao=(UserDao)object;
				userDao.toString();
			}
		}
		
	}
	public void autoScan() {
		scan(new File("bin"),new StringBuilder());
		System.out.println("autoScan执行完毕后，查看最终map："+map.toString());
	}
	private void scan(File file,StringBuilder dir) {
		File[] files=file.listFiles();
		if(files.length==0) {
			return;
		}
		for(File f:files) {
			String fileName=f.getName();
			if(f.isDirectory()) {
				if(!"".equals(dir.toString())) {
					dir.append('.');
				}
				dir.append(fileName);
				scan(f, dir);
				int index=dir.lastIndexOf(".");
				if(index!=-1) {
					dir.delete(index, dir.length());
				}else {
					dir.delete(0, dir.length());
				}
			}else {
				String name=dir.toString()+"."+fileName;
				//System.err.println(name);
				//处理所有.class结尾的类文件
				if(name.startsWith("com")&&name.indexOf(".class")!=-1) {
					name=name.substring(0, name.lastIndexOf("."));
					try {
						//根据类名进行处理，根据注解创建相应的对象，自动给属性注入对象
						Class c=Class.forName(name);
						if(c.getAnnotation(Component.class)!=null) {
							injection(name, c);
						}else if(c.getAnnotation(Controller.class)!=null) {
							injection(name, c);
						}else if(c.getAnnotation(Service.class)!=null) {
							injection(name, c);
						}
//						Field[] fields=c.getDeclaredFields();
//						Method[] methods=c.getDeclaredMethods();
//						Object o=c.newInstance();
//						map.put(name,o);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			
		}
	}
	private void injection( String name,Class c) {
		try {
			Object object=c.newInstance();
			map.put(name,object);
			Field[] fields=c.getDeclaredFields();
			for(Field fi:fields) {
				fi.setAccessible(true);
				if(fi.getAnnotation(AutoWired.class)!=null) {
					fi.set(object, fi.getType().newInstance());
//					if(name.equals("com.dayong.UserService")) {
//						UserService userService=(UserService)object;
//						System.out.println("9999999999");
//						userService.show();
//					}
				}else if(fi.getAnnotation(Value.class)!=null) {
					Value v=fi.getAnnotation(Value.class);
					if("".equals(v.ognl())) {
						fi.set(object, Configure.getStringByOgnl(v.value()));
					}else {
						fi.set(object, Configure.getStringByOgnl(v.ognl()));
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
