package day18.spring;
@Component
public class UserDao {
	//根据注解,从配置文件获取配置数据,并为url变量赋值
	@Value("${spring.datasource.url}")
	private String url;
	@Value("${spring.datasource.username}")
	private String username;
	@Value("${spring.datasource.password}")
	private String password;
	@Value("${spring.datasource.driver}")
	private String driver;
	
	public void test() {
		System.out.println("\n--自动填充的数据--------------");
		System.out.println(this.url);
		System.out.println(this.username);
		System.out.println(this.password);
		System.out.println(this.driver);
	}
}
