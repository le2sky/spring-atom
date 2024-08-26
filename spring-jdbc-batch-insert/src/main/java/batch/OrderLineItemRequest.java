package batch;

import lombok.Getter;

@Getter
class OrderLineItemRequest {

    private Long orderId;
    private Long productId;
    private Long quantity;

    public OrderLineItemRequest(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public void setOrderId(Long id) {
        this.orderId = id;
    }
}
