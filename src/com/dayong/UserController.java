package com.dayong;
@Controller
public class UserController {
	@AutoWired
	private UserService service;
	public UserController() {
		System.out.println("UserController��������ɹ���");
	}
	public void show() {
		System.out.println("show����;"+"չʾUserController�����Զ�ע���service��"+service);
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		
		return "����UserController;"+"չʾUserController�����Զ�ע���service��"+service;
	}
}
