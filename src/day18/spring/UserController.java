package day18.spring;
@Controller
public class UserController {
	//�Զ�װ��
	//�Զ���SpringContext,��ȡUserServiceʵ��,���浽�������
	@Autowired
	private UserService userService;
	
	public void test() {
		System.out.println("\n--����������-----------------");
		System.out.println("����ҵ�����"+userService);
		userService.test();
	}
}
