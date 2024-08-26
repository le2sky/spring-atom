package batch;

import java.util.List;
import lombok.Getter;

@Getter
class OrderRequest {

    private Long id;
    private String address;
    private List<OrderLineItemRequest> orderLineItemRequests;

    public OrderRequest(String address, List<OrderLineItemRequest> orderLineItemRequests) {
        this.address = address;
        this.orderLineItemRequests = orderLineItemRequests;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderLineItemRequest> getOrderLineItemRequestsWithPk() {
        orderLineItemRequests.forEach(it -> it.setOrderId(id));

        return orderLineItemRequests;
    }
}
