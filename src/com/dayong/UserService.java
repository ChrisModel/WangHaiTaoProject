package com.dayong;
@Service
public class UserService {
	@AutoWired
	private UserDao dao;
	public UserService() {
		System.out.println("UserService���󴴽��ɹ���");
	}
	public void show() {
		System.out.println("show����;"+"����UserService�Զ�ע�����Զ���dao��"+dao);
	}
	@Override
	public String toString() {
		
		return "����UserService;"+"UserService�Զ�ע�����Զ���dao��"+dao;
	}
}
