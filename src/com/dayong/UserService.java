package com.dayong;
@Service
public class UserService {
	@AutoWired
	private UserDao dao;
	public UserService() {
		System.out.println("UserService���󴴽��ɹ���");
	}
	public void show() {
		System.out.println("UserService�Զ�ע�����Զ���dao��"+dao+"**");
	}
	@Override
	public String toString() {
		
		return "����UserService��"+super.toString();
	}
}
