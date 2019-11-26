package com.dayong;

@Component
public class UserDao {
	public UserDao() {
		System.out.println("UserDao创建对象成功！");
	}
	@Value("${spring.datasource.driver}")
	private String driver;
	@Value("${spring.datasource.url}")
	private String url;
	@Value("${spring.datasource.username}")
	private String username;
	@Value("${spring.datasource.password}")
	private String password;
	
	@Override
	public String toString() {
		
		return "UserDao [driver=" + driver + ", url=" + url + ", username=" + username + ", password=" + password + "]";
	}

}
