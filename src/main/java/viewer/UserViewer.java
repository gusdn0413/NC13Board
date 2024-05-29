package viewer;

import connector.ConnectionMaker;
import connector.MySqlConnectionMaker;
import controller.UserController;
import model.UserDTO;
import util.ScannerUtil;

import java.sql.SQLException;
import java.util.Scanner;

public class UserViewer {
    
    private final Scanner scanner = new Scanner(System.in);
    private UserDTO logIn;
    private ConnectionMaker connectionMaker;

    public UserViewer() {
        connectionMaker = new MySqlConnectionMaker();
    }

    public void showIndex() {
        String message = "1. 로그인 2. 회원 가입 3. 종료";
        while (true) {
            int userChoice = ScannerUtil.nextInt(scanner, message);
            if (userChoice == 1) {
                auth();
                if (logIn != null) {
                    // 회원 메뉴 실행
                    showMenu();
                }
            }
            if (userChoice == 2) {
                register();
            }
            if (userChoice == 3) {
                try {
                    UserController userController = new UserController(connectionMaker);
                    userController.initialize();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                break;
            }
        }
    }

    private void showMenu() {
        String message = "1. 게시판으로 2. 회원 정보 수정 3. 로그 아웃";
        while (logIn != null) {
            int userChoice = ScannerUtil.nextInt(scanner, message);
            if (userChoice == 1) {
                BoardViewer boardViewer = new BoardViewer(connectionMaker, scanner);
                boardViewer.showMenu(logIn.getId());
            } else if (userChoice == 2) {
                printInfo();
            } else if (userChoice == 3) {
                logIn = null;
                System.out.println("성공적으로 로그아웃");
            }
        }
    }

    private void auth() {
        String message;
        message = "아이디를 입력해주세요.";
        String username = ScannerUtil.nextLine(scanner, message);

        message = "비밀번호를 입력해주세요.";
        String password = ScannerUtil.nextLine(scanner, message);

        UserController userController = new UserController(connectionMaker);
        logIn = userController.auth(username, password);

        if (logIn == null) {
            System.out.println("잘못 입력하셨습니다. 로그인 정보를 다시 확인해주세요.");
        }
    }

    private void register() {

        String message = "사용하실 아이디를 입력해주세요";
        String username = ScannerUtil.nextLine(scanner, message);

        message = "사용할 비밀번호를 입력해주세요";
        String password = ScannerUtil.nextLine(scanner, message);

        message = "사용할 닉네임을 입력해주세요";
        String nickname = ScannerUtil.nextLine(scanner, message);

        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(username);
        userDTO.setPassword(password);
        userDTO.setNickname(nickname);
        UserController userController = new UserController(connectionMaker);
        boolean registered = userController.register(userDTO);
        if (!registered) {
            System.out.println("중복된 회원입니다.");
        } else {
            System.out.println("회원가입 성공");
        }
    }

    private void printInfo() {
        System.out.println("============================");
        System.out.println(logIn.getNickname() + " 회원님의 정보");
        System.out.println("============================");
        System.out.println("id : " + logIn.getId());
        System.out.println("닉네임 : " + logIn.getNickname());
        System.out.println("============================");
        String message = "1. 회원 정보 수정 2. 회원 탈퇴 3. 뒤로 가기";

        int userChoice = ScannerUtil.nextInt(scanner, message);
        if (userChoice == 1) {
            update();
        } else if (userChoice == 2) {
            delete();

        } else if (userChoice == 3) {

        }
    }

    private void update() {
        String message = "새로운 닉네임을 입력해주세요";
        String newNickname = ScannerUtil.nextLine(scanner, message);
        message = "새로운 비밀번호를 입력해주세요";
        String newPassword = ScannerUtil.nextLine(scanner, message);
        message = "기존 비밀번호를 입력해주세요";
        String oldPassword = ScannerUtil.nextLine(scanner, message);

        if (oldPassword.equals(logIn.getPassword())) {
            logIn.setNickname(newNickname);
            logIn.setPassword(newPassword);

            UserController userController = new UserController(connectionMaker);
            userController.update(logIn);
        } else {
            System.out.println("기존 비밀번호와 달라서 수정할 수 없습니다");
        }

    }

    private void delete() {
        String message = "정말로 탈퇴하시겠습니가? Y/N";
        String answer = ScannerUtil.nextLine(scanner, message);
        if (answer.equalsIgnoreCase("Y")) {
            message = "비밀번호를 입력해주세요";
            String password = ScannerUtil.nextLine(scanner, message);
            if (password.equals(logIn.getPassword())) {
                UserController userController = new UserController(connectionMaker);
                userController.delete(logIn.getId());
                logIn = null;
            }
        }
    }
}
