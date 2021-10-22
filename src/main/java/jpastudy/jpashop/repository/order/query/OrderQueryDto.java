package jpastudy.jpashop.repository.order.query;

import jpastudy.jpashop.domain.Address;
import jpastudy.jpashop.domain.OrderStatus;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;
import java.util.List;

@Data
@EqualsAndHashCode(of = "orderId")
public class OrderQueryDto {
    private Long orderId;  //주문번호
    private String name; // 회원이름
    private LocalDateTime orderDate; // 주문날짜
    private OrderStatus orderStatus; // 주문상태
    private Address address; //배송주소
    private List<OrderItemQueryDto> orderItems; // 의존관계인 OrderItem을 저장한 OrderItemQueryDto

    public OrderQueryDto(Long orderId, String name, LocalDateTime orderDate,
                         OrderStatus orderStatus, Address address) {
        this.orderId = orderId; //주문번호
        this.name = name; // 회원이름
        this.orderDate = orderDate; //주문날짜
        this.orderStatus = orderStatus; // 주문 상태
        this.address = address; // 배송주소
    }
}
