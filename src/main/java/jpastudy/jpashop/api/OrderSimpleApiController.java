package jpastudy.jpashop.api;

import jpastudy.jpashop.domain.Address;
import jpastudy.jpashop.domain.Order;
import jpastudy.jpashop.domain.OrderSearch;
import jpastudy.jpashop.domain.OrderStatus;
import jpastudy.jpashop.repository.OrderRepository;
import jpastudy.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpastudy.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
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
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

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
     * V3. 엔티티를 조회해서 DTO로 변환(fetch join 사용함)
     * fetch join으로 쿼리 1번 호출(패치조인 사용)
     */
    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(toList());
        return result;
    }

    /**
     * V4. JPA에서 DTO로 바로 조회
     * Query결과를 엔티티 대신 DTO에 저장하여 조회
     * - 쿼리 1번 호출
     * - select 절에서 원하는 데이터만 선택해서 조회
     * DTO가 너무 많아지게 되는 단점이 있다.
     * 특정화면에 특정 데이터를 원할때 가끔 씀
     */
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> ordersV4() {
        return orderSimpleQueryRepository.findOrderDtos();
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
