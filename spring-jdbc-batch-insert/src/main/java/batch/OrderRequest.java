package batch;

import java.util.List;

record OrderRequest(String address, List<OrderLineItemRequest> orderLineItemRequests) {
}
