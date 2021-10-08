package com.sollertia.noticeboard.testdata;

import com.sollertia.noticeboard.model.Board;
import com.sollertia.noticeboard.model.Dat;
import com.sollertia.noticeboard.model.RoleEnum;
import com.sollertia.noticeboard.model.User;
import com.sollertia.noticeboard.repository.BoardRepository;
import com.sollertia.noticeboard.repository.DatRepository;
import com.sollertia.noticeboard.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


@Component
public class TestDataRunner implements ApplicationRunner {

   @Autowired
   PasswordEncoder passwordEncoder;

   @Autowired
   UserRepository userRepository;

   @Autowired
   BoardRepository boardRepository;

   @Autowired
   DatRepository datRepository;


   @Override
   public void run(ApplicationArguments args) throws Exception {
// 테스트 User 생성
       User testUser1 = new User("원빈", passwordEncoder.encode("123"), RoleEnum.USER);
       User testUser2 = new User("다혜", passwordEncoder.encode("123"), RoleEnum.USER);
       testUser1 = userRepository.save(testUser1);
       testUser2 = userRepository.save(testUser2);

      for(int i=0; i<10; i++){
          createTestBoard(testUser1);
      }
       for(int i=0; i<8; i++){
           createTestDat(testUser1);
       }
       for(int i=0; i<10; i++){
           createTestBoard(testUser2);
       }
       for(int i=0; i<5; i++){
           createTestDat(testUser2);
       }


   }
       // 테스트 board 생성
       private void createTestBoard(User user){

           Board board = new Board();
           board.setTitle("1");
           board.setContent("내용");
           board.setWriter(user.getUsername());

           boardRepository.save(board);

       }

       // 테스트 Dat 생성
       private void createTestDat(User user){

           Dat dat = new Dat();
           dat.setContent("내용이다");
           dat.setWriter(user.getUsername());
           dat.setBoardId(25L);

           datRepository.save(dat);
       }

}
