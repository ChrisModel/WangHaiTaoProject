package com.dayong;
@Controller
public class UserController {
	@AutoWired
	private UserService service;
	public UserController() {
		System.out.println("UserController创建对象成功！");
	}
	public void show() {
		System.out.println("show方法;"+"展示UserController类中自动注入的service："+service);
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		
		return "我是UserController;"+"展示UserController类中自动注入的service："+service;
	}
}
