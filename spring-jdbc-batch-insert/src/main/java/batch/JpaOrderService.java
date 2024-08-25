package batch;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
class JpaOrderService {

    private final OrderRepository orderRepository;

    @Transactional
    public void createOrder(List<OrderRequest> requests) {
        List<Order> list = requests.stream()
                .map(this::mapToOrder)
                .toList();

        orderRepository.saveAll(list);
    }

    private Order mapToOrder(OrderRequest orderRequest) {
        Order order = new Order(orderRequest.address());
        List<OrderLineItemRequest> lineItemRequests = orderRequest.orderLineItemRequests();
        lineItemRequests.forEach(item -> {
            OrderLineItem orderLineItem = new OrderLineItem(item.productId(), item.quantity(), order);
            order.addItem(orderLineItem);
        });

        return order;
    }
}
