package day18.spring;
//spring�����Ķ���,�����Զ�����ʵ��,����ʵ������һ������

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
	 * �Զ���binĿ¼��,ɨ�����е�class�ļ�,������ʵ��,����map����
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
	 * ���� "aa.bb.cc"
	 * 
	 */
	public void autoScan() throws Exception {
		String path = SpringContext.class.getResource("/").getPath();
		path = URLDecoder.decode(path, "UTF-8");
		
		StringBuilder pkg = new StringBuilder();
		//ɨ��Ŀ¼, pkg�����������
		scan(new File(path), pkg);
		
		//����Զ�װ��,�������е�ʵ��,��ʵ����Ҫ�Ķ��������,ע���ȥ
		autowire();
	}
	/*
	 * map
	 * 
	 *         key                        value
	 * day18.spring.UserDao           UserDaoʵ��
	 * day18.spring.UserService       UserServiceʵ��
	 * day18.spring.UserController    UserControllerʵ��
	 */
	private void autowire() throws Exception {
		Collection<Object> values = map.values(); //������е�ʵ��
		for (Object obj : values) {//������Щʵ��
			Class<? extends Object> c = obj.getClass();//������ʵ����"�����"
			Field[] a = c.getDeclaredFields();//���������е����г�Ա����
			for (Field f : a) {//����ÿ������
				//��������ϴ���@Value, @Autowiredע��,Ҫִ���Զ�ע��(����ֵ)
				if (f.isAnnotationPresent(Value.class)) {
					//ע����������
					injectValue(c, obj, f);
				} else if(f.isAnnotationPresent(Autowired.class)) {
					//ע�����
					injectObject(c, obj, f);
				}
			}
		}
	}

	private void injectValue(Class<? extends Object> c, Object obj, Field f) throws Exception {
		Value value = f.getAnnotation(Value.class);//��ñ����ϵ�@Valueע��
		String ognl = value.ognl();//��ȡognl���ʽ
		if (ognl.equals("")) {     //���û��ognl
			ognl = value.value();  //��value��ognl
		}
		
		String v = Configure.get(ognl); //�������ļ���ȡ��������
		f.setAccessible(true);          //˽�б������Ա�����
		f.set(obj, v);                  //����������v,���浽����f
	}
	private void injectObject(Class<? extends Object> c, Object obj, Field f) throws Exception {
		Class<?> type = f.getType(); //��ó�Ա����������
		Object o2 = getObject(type); //������,��ȡ�������͵�ʵ��
		
		f.setAccessible(true);       //˽�б����ɷ���
		f.set(obj, o2);              //��o2���浽objʵ����f����
	}
	private void scan(File dir, StringBuilder pkg) {
		File[] files = dir.listFiles();
		if (files == null) {
			return;
		}
		for (File f : files) {
			String n = f.getName();//����ļ���,������Ŀ¼��
			
			if (f.isFile()) {//f���ļ�
				//�����½�ʵ��
				if (n.endsWith(".class")) {//ֻ����class�ļ�
					// "A.class" --ɾ����׺--> "A" --ƴ����--> "aa.bb.cc.A"
					String className = pkg+"."+n.substring(0, n.length()-6);     
					try {
						//�����½�ʵ��
						Class<?> c = Class.forName(className);
						//���������@Component,@Service��@Controllerע��Ŵ���ʵ��
						if (c.isAnnotationPresent(Component.class) ||
							c.isAnnotationPresent(Service.class) ||
							c.isAnnotationPresent(Controller.class)) {
							Object obj = c.newInstance();
							//��ʵ������map����
							map.put(className, obj);
							System.out.println(map);
						}
					} catch (Exception e) {
						System.out.println(className+"���ܴ���ʵ��");
					}
				}
			} else {//f���ļ���
				//����ǿմ�ֱ��ƴĿ¼��,�����������ƴһ������ƴĿ¼��
				if (pkg.length()!=0) {
					pkg.append(".");
				}
				pkg.append(n);
				System.out.println(pkg);
				//�ݹ鴦��f��Ŀ¼
				scan(f, pkg);
				//��Ŀ¼�Ѿ��������
				//����е�����һ����λ�õ�ĩβɾ����,���û�е�ֱ�����
				int index = pkg.lastIndexOf(".");//���һ�����λ��,�Ҳ�����-1
				if (index==-1) {
					pkg.delete(0, pkg.length());//���
				} else {
					pkg.delete(index, pkg.length());//ɾ�����һ�ΰ���
				}
				System.out.println(pkg);
			}
		}
	}
	
	public <T> T getObject(Class<T> c) {
		String n = c.getName(); //��ò�����������������
		return (T) map.get(n); //��������Ϊ��,��ȡ��Ӧ��ʵ��
	} 
	
	public static void main(String[] args) throws Exception {
		//�Զ�ɨ��֮ǰ,�ȼ�����������
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













