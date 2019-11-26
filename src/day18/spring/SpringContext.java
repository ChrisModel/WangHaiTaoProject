package day18.spring;
//spring上下文对象,用来自动创建实例,并把实例存入一个集合

import java.io.File;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SpringContext {
	private Map<String, Object> map = 
			new HashMap<String, Object>();
	/*
	 * 自动在bin目录下,扫描所有的class文件,并创建实例,放入map集合
	 * [bin]
	 *    |- [aa]
	 *        |- A.class
	 *        |- B.class
	 *        |- [bb]
	 *             |- C.class
	 *             |- D.class
	 *             |- xxx.yml
	 *             |- [cc]
	 *                  |- E.class
	 *        |- F.class
	 * 
	 * 包名 "aa.bb.cc"
	 * 
	 */
	public void autoScan() throws Exception {
		String path = SpringContext.class.getResource("/").getPath();
		path = URLDecoder.decode(path, "UTF-8");
		
		StringBuilder pkg = new StringBuilder();
		//扫描目录, pkg用来处理包名
		scan(new File(path), pkg);
		
		//完成自动装配,遍历所有的实例,把实例需要的对象或数据,注射进去
		autowire();
	}
	/*
	 * map
	 * 
	 *         key                        value
	 * day18.spring.UserDao           UserDao实例
	 * day18.spring.UserService       UserService实例
	 * day18.spring.UserController    UserController实例
	 */
	private void autowire() throws Exception {
		Collection<Object> values = map.values(); //获得所有的实例
		for (Object obj : values) {//遍历这些实例
			Class<? extends Object> c = obj.getClass();//获得这个实例的"类对象"
			Field[] a = c.getDeclaredFields();//获得这个类中的所有成员变量
			for (Field f : a) {//遍历每个变量
				//如果变量上存在@Value, @Autowired注解,要执行自动注入(即赋值)
				if (f.isAnnotationPresent(Value.class)) {
					//注入配置数据
					injectValue(c, obj, f);
				} else if(f.isAnnotationPresent(Autowired.class)) {
					//注入对象
					injectObject(c, obj, f);
				}
			}
		}
	}

	private void injectValue(Class<? extends Object> c, Object obj, Field f) throws Exception {
		Value value = f.getAnnotation(Value.class);//获得变量上的@Value注解
		String ognl = value.ognl();//获取ognl表达式
		if (ognl.equals("")) {     //如果没有ognl
			ognl = value.value();  //用value当ognl
		}
		
		String v = Configure.get(ognl); //从配置文件获取配置数据
		f.setAccessible(true);          //私有变量可以被访问
		f.set(obj, v);                  //把配置数据v,保存到变量f
	}
	private void injectObject(Class<? extends Object> c, Object obj, Field f) throws Exception {
		Class<?> type = f.getType(); //获得成员变量的类型
		Object o2 = getObject(type); //用类型,获取这种类型的实例
		
		f.setAccessible(true);       //私有变量可访问
		f.set(obj, o2);              //把o2保存到obj实例的f变量
	}
	private void scan(File dir, StringBuilder pkg) {
		File[] files = dir.listFiles();
		if (files == null) {
			return;
		}
		for (File f : files) {
			String n = f.getName();//获得文件名,或者是目录名
			
			if (f.isFile()) {//f是文件
				//反射新建实例
				if (n.endsWith(".class")) {//只处理class文件
					// "A.class" --删掉后缀--> "A" --拼包名--> "aa.bb.cc.A"
					String className = pkg+"."+n.substring(0, n.length()-6);     
					try {
						//反射新建实例
						Class<?> c = Class.forName(className);
						//如果类上有@Component,@Service或@Controller注解才创建实例
						if (c.isAnnotationPresent(Component.class) ||
							c.isAnnotationPresent(Service.class) ||
							c.isAnnotationPresent(Controller.class)) {
							Object obj = c.newInstance();
							//把实例放入map集合
							map.put(className, obj);
							System.out.println(map);
						}
					} catch (Exception e) {
						System.out.println(className+"不能创建实例");
					}
				}
			} else {//f是文件夹
				//如果是空串直接拼目录名,如果有内容先拼一个点再拼目录名
				if (pkg.length()!=0) {
					pkg.append(".");
				}
				pkg.append(n);
				System.out.println(pkg);
				//递归处理f子目录
				scan(f, pkg);
				//子目录已经处理完毕
				//如果有点把最后一个点位置到末尾删除掉,如果没有点直接清空
				int index = pkg.lastIndexOf(".");//最后一个点的位置,找不到是-1
				if (index==-1) {
					pkg.delete(0, pkg.length());//清空
				} else {
					pkg.delete(index, pkg.length());//删除最后一段包名
				}
				System.out.println(pkg);
			}
		}
	}
	
	public <T> T getObject(Class<T> c) {
		String n = c.getName(); //获得参数类对象的完整类名
		return (T) map.get(n); //用类名作为键,提取对应的实例
	} 
	
	public static void main(String[] args) throws Exception {
		//自动扫描之前,先加载配置数据
		Configure.load();
		
		SpringContext c = new SpringContext();
		c.autoScan();
		
		//UserDao dao = c.getObject(UserDao.class);
		//dao.test();
		
		//UserService serv = c.getObject(UserService.class);
		//serv.test();
		
		UserController cont = c.getObject(UserController.class);
		cont.test();
	}
}













