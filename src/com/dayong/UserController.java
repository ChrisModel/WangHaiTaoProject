package com.dayong;
@Controller
public class UserController {
	@AutoWired
	private UserService service;
	public UserController() {
		System.out.println("UserController��������ɹ���");
	}
	public void show() {
		System.out.println("չʾUserController�����Զ�ע���service��"+service.toString());
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		
		return "����UserController��"+super.toString();
	}
}
