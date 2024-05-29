package viewer;

import connector.ConnectionMaker;
import controller.BoardController;
import lombok.Setter;
import model.BoardDTO;
import util.ScannerUtil;

import java.util.List;
import java.util.Scanner;

@Setter
public class BoardViewer {

    private ReplyViewer replyViewer;
    private ConnectionMaker connectionMaker;
    private Scanner scanner;

    public BoardViewer(ConnectionMaker connectionMaker, Scanner scanner) {
        this.connectionMaker = connectionMaker;
        this.scanner = scanner;
    }

    public void showMenu(int loginId) {
        String message = "1. 글 작성하기 2. 글 목록 보기 3. 뒤로 가기";
        while (true) {
            int userChoice = ScannerUtil.nextInt(scanner, message);
            if (userChoice == 1) {
                insert(loginId);
            } else if (userChoice == 2) {
                printList(loginId);
            } else if (userChoice == 3) {
                System.out.println("메인 화면으로 돌아갑니다");
                break;
            }
        }
    }

    private void insert(int loginId) {
        BoardDTO boardDTO = new BoardDTO();

        String message = "글의 제목을 입력해주세요";
        boardDTO.setTitle(ScannerUtil.nextLine(scanner, message));

        message = "글의 내용을 입력해주세요";
        boardDTO.setContent(ScannerUtil.nextLine(scanner, message));
        boardDTO.setWriterId(loginId);

        BoardController boardController = new BoardController(connectionMaker);
        boardController.register(boardDTO);
    }

    private void printList(int loginId) {
        BoardController boardController = new BoardController(connectionMaker);
        List<BoardDTO> list = boardController.selectAll();
        for (BoardDTO boardDTO : list) {
            System.out.println("게시글 id : " + boardDTO.getId());
            System.out.println("글 제목 : " + boardDTO.getTitle());
            System.out.println("작성자 id : " + boardDTO.getWriterId());
            System.out.println("작성자 닉네임 : " + boardDTO.getNickname());
        }
        if (list.isEmpty()) {
            System.out.println("등록된 게시글이 없습니다");
        } else {
            String message = "상세보기할 글의 번호나 뒤로 가실려면 0을 입력해주세요";
            int boardId = ScannerUtil.nextInt(scanner, message);
            while (boardController.selectOne(boardId) == null) {
                System.out.println("잘못 입력하셨습니다.");
                boardId = ScannerUtil.nextInt(scanner, message);
            }
            if (boardId == 0) {

            }
            if (boardId != 0) {
                printOne(loginId, boardId);
            }
        }
    }

    private void printOne(int loginId, int boardId) {
        BoardController boardController = new BoardController(connectionMaker);
        BoardDTO boardDTO = boardController.selectOne(boardId);
        System.out.println("글 제목 : " + boardDTO.getTitle());
        System.out.println("글 번호 : " + boardDTO.getId());
        System.out.println("글 작성자 : " + boardDTO.getWriterId());
        System.out.println("글 내용 : " + boardDTO.getContent());
        System.out.println("============================");

        if (loginId == boardDTO.getWriterId()) {
            String message = "1. 수정 2. 삭제 3. 댓글 4. 뒤로가기";
            int userChoice = ScannerUtil.nextInt(scanner, message, 1, 4);
            if (userChoice == 1) {
                update(boardId);
            } else if (userChoice == 2) {
                delete(boardId);
            } else if (userChoice == 3) {
                replyViewer.showMenu(loginId, boardId);
            } else if (userChoice == 4) {
                printList(loginId);
            }
        } else {
            String message = "1. 댓글 2. 뒤로 가기";
            int userChoice = ScannerUtil.nextInt(scanner, message, 1, 2);
            if (userChoice == 1) {
                replyViewer.showMenu(loginId, boardId);
            } else if (userChoice == 2) {
                printList(loginId);
            }
        }
    }

    private void update(int boardId) {
        String message = "새로운 제목을 입력해주세요";
        String newTitle = ScannerUtil.nextLine(scanner, message);
        message = "새로운 내용을 입력해주세요";
        String newContent = ScannerUtil.nextLine(scanner, message);

        BoardController boardController = new BoardController(connectionMaker);
        BoardDTO boardDTO = boardController.selectOne(boardId);
        boardDTO.setTitle(newTitle);
        boardDTO.setContent(newContent);
        boardController.update(boardDTO);
    }

    private void delete(int boardId) {
        String message = "정말 삭제하시겠습니까? Y/N";
        String answer = ScannerUtil.nextLine(scanner, message);
        if (answer.equalsIgnoreCase("Y")) {
            BoardController boardController = new BoardController(connectionMaker);
            boardController.delete(boardId);
            System.out.println("삭제되었습니다");
        } else if (answer.equalsIgnoreCase("N")) {
            System.out.println("삭제를 취소합니다.");
        }
    }
}
