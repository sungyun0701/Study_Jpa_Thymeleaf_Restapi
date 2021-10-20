package jpastudy.jpashop.domain;

import jpastudy.jpashop.domain.item.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@SpringBootTest
@Transactional
class EntityTest {
    @Autowired
    EntityManager em;

    // @Rollback(value = true)를 하면 실제 db에 저장이 안된다.
    @Test
    @Rollback(value = false)
    public  void entity() throws  Exception{
        // Member 생성
        Member member = new Member();
        member.setName("SungYun");
        Address address = new Address("서울","동작","12345");
        //Member 에 Address를 저장
        member.setAddress(address);
        //영속성 컨텍스트에 저장
        em.persist(member);

        //Order생성
        Order order = new Order();
        //Order와 Member 연결
        order.setMember(member);

        //Delivery 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        //em.persist(delivery) Casecade쓰기 때문에 쓸 필요없음
        //Order와 Delivery 연결
        order.setDelivery(delivery);

        //Item - Book 생성
        Book book = new Book();
        book.setName("iShark");
        book.setPrice(30000);
        book.setStockQuantity(19);
        book.setAuthor("구름");
        book.setIsbn("12340-ab");
        //영속성 컨텍스트에 저장
        em.persist(book);

        //OrderItem 생성
        OrderItem orderItem = new OrderItem();
        orderItem.setCount(2);
        orderItem.setOrderPrice(60000);
        orderItem.setItem(book);

        //Order와 OrderItem연결
        order.addOrderItem(orderItem);

        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.ORDER);

        //영속성 컨텍스트에 저장
        em.persist(order);
    }

}