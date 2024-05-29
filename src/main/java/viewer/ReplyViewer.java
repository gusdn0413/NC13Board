package viewer;

import connector.ConnectionMaker;
import controller.ReplyController;
import lombok.Setter;
import model.ReplyDTO;
import util.ScannerUtil;

import java.util.List;
import java.util.Scanner;

@Setter
public class ReplyViewer {

    private Scanner scanner;
    private ConnectionMaker connectionMaker;

    public ReplyViewer(ConnectionMaker connectionMaker,Scanner scanner) {
        this.connectionMaker = connectionMaker;
        this.scanner=scanner;
    }

    public void showMenu(int loginId, int boardId) {
        String message = "1. 댓글 달기 2. 댓글 목록 보기 3. 뒤로 가기";
        while (true) {
            int userChoice = ScannerUtil.nextInt(scanner, message);
            if (userChoice == 1) {
                insert(loginId, boardId);
            } else if (userChoice == 2) {
                printList(boardId);
            } else if (userChoice == 3) {
                System.out.println("게시판으로 돌아갑니다");
                break;
            }
        }
    }

    private void insert(int loginId, int boardId) {
        ReplyDTO replyDTO = new ReplyDTO();

        String message = "댓글을 입력하세요";
        String content = ScannerUtil.nextLine(scanner, message);

        replyDTO.setWriterId(loginId);
        replyDTO.setBoardId(boardId);
        replyDTO.setContent(content);

        ReplyController replyController = new ReplyController(connectionMaker);
        replyController.insert(replyDTO);
    }

    private void printList(int boardId) {
        ReplyController replyController = new ReplyController(connectionMaker);
        List<ReplyDTO> list = replyController.selectAll(boardId);
        for (ReplyDTO replyDTO : list) {
            System.out.println("게시글 id : " + replyDTO.getBoardId());
            System.out.println("작성자 id : " + replyDTO.getWriterId());
            System.out.println("작성자 닉네임 : " + replyDTO.getNickname());
            System.out.println("댓글 내용 : " + replyDTO.getContent());
            System.out.println("============================");
        }
        if (list.isEmpty()) {
            System.out.println("등록된 댓글이 없습니다");
        }
    }
}
