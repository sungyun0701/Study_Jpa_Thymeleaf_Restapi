package jpastudy.jpashop.api;

import jpastudy.jpashop.domain.Address;
import jpastudy.jpashop.domain.Order;
import jpastudy.jpashop.domain.OrderSearch;
import jpastudy.jpashop.domain.OrderStatus;
import jpastudy.jpashop.repository.OrderRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAll(new OrderSearch());
        all.forEach(order -> {
            order.getMember().getName(); // Member를 Lazy Loading 강제 초기화
            order.getDelivery().getAddress();//Delivery를 Laza Loading 강제초기화
        });
        return all;
    }

    /**
     * V2: 엔티티를 DTO로 변환은 성공
     * 문제점 : 지연로딩(Lazy Loading_으로 쿼리 N번 호출
     * N+1 문제
     * Order 2건, Member 2건, Delivery 2건
     * order 1건, member2, delivery 2
     * 해결방법 : 패치조인
     */
    @GetMapping("/api/v2/simple-orders")
    public  List<SimpleOrderDto> ordersV2(){
        List<Order> orders = orderRepository.findAll(new OrderSearch());
        return orders.stream()  //Stream<Order>
                .map(order -> new SimpleOrderDto(order)) //Stream<SimpleOrderDto>
                .collect(toList()); // List<SimpleOrderDto>

    }

    /**
     * V3. 패치 조인 사용
     * @return
     */
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(toList());
        return result;
    }

    // 응답과 요청에 사용할 DTO Inner Class 선언
    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;
        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName(); //Lazy 강제 초기화
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress(); //Lazy 강제 초기화
        }
    }

}
