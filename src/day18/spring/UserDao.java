package day18.spring;
@Component
public class UserDao {
	//����ע��,�������ļ���ȡ��������,��Ϊurl������ֵ
	@Value("${spring.datasource.url}")
	private String url;
	@Value("${spring.datasource.username}")
	private String username;
	@Value("${spring.datasource.password}")
	private String password;
	@Value("${spring.datasource.driver}")
	private String driver;
	
	public void test() {
		System.out.println("\n--�Զ���������--------------");
		System.out.println(this.url);
		System.out.println(this.username);
		System.out.println(this.password);
		System.out.println(this.driver);
	}
}
