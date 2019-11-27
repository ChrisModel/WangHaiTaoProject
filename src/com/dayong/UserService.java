package com.dayong;
@Service
public class UserService {
	@AutoWired
	private UserDao dao;
	public UserService() {
		System.out.println("UserService对象创建成功！");
	}
	public void show() {
		System.out.println("show方法;"+"我是UserService自动注入属性对象dao："+dao);
	}
	@Override
	public String toString() {
		
		return "我是UserService;"+"UserService自动注入属性对象dao："+dao;
	}
}
