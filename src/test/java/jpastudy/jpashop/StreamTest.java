package jpastudy.jpashop;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class StreamTest {
    @Test
    public  void stream() {
        List<User> users = List.of(new User("성윤", 20), new User("나루", 15), new User("블랑", 10));
        // User의 Name 추출해서 List<String> 으로 변환해서 출력하세요
        List<String> nameList =
                            users.stream() // 저장형태가 Stream<User>으로 변환 유저형태가 스트림 형태로 변형
                                    .map(user -> user.getName())  // 리턴 타입이 Stream<String> 으로 됨
//                                    .map(User :: getName) // 위에 대신 이렇게 써도 됨
                                    .collect(Collectors.toList());  // List<String>으로 최종 변환
       nameList.forEach(name -> System.out.println(name));
       System.out.println("=======================");
       nameList.forEach(System.out::println); //위에 대신 이렇게 써도됨

        //20살 이상인 User의 Name 추출해서 List<String>으로 변환해서 출력해라.
        System.out.println("==========20살 이상=============");
        users.stream()
                .filter(user -> user.getAge() >= 20)
 //                .forEach(System.out::println);  StreamTest.User(name=성윤, age=20) 이렇게 나옴
                .forEach(user-> System.out.println(user.getName()));
        System.out.println("=======================");
        List<String> names = users.stream()
                                                            .filter(user -> user.getAge() >= 20) // Stream<User>
                                                            .map(user -> user.getName()) //Stream<String>
                                                            .collect(Collectors.toList()); // List<String>
        names.forEach(System.out::println);
        System.out.println("=======================");
        //User 들의 나이 합계
        // Stream은 매번 만들어야함 재사용이 안됨
        int sum = users.stream()  // 리턴 타입 : Stream<User>
                .mapToInt(user -> user.getAge()) // intStream
                .sum();
        System.out.println("나이합계 : " + sum);
    }

    @Data
    @AllArgsConstructor
    static  class User {
        private String name;
        private  int age;
    }
}
