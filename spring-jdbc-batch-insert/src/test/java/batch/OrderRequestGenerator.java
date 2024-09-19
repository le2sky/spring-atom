package batch;

import org.testcontainers.shaded.org.apache.commons.lang3.RandomStringUtils;
import org.testcontainers.shaded.org.apache.commons.lang3.RandomUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class OrderRequestGenerator {

    private static final Long REQUEST_COUNT = 10_000L;

    public static List<OrderRequest> generateRequests() {
        List<OrderRequest> orderRequests = new ArrayList<>();

        for (int i = 0; i < REQUEST_COUNT; i++) {
            List<OrderLineItemRequest> orderLineItemRequests = new ArrayList<>();

            for (int j = 0; j < RandomUtils.nextInt(1, 5); j++) {
                OrderLineItemRequest itemRequest = new OrderLineItemRequest(
                        RandomUtils.nextLong(1, 500),
                        RandomUtils.nextLong(1, 500)
                );

                orderLineItemRequests.add(itemRequest);
            }

            OrderRequest orderRequest = new OrderRequest(
                    RandomStringUtils.randomAlphabetic(6),
                    orderLineItemRequests
            );

            orderRequests.add(orderRequest);
        }

        return orderRequests;
    }

    public static List<OrderRequest> generateRequestsWithoutItem() {
        List<OrderRequest> orderRequests = new ArrayList<>();

        for (int i = 0; i < REQUEST_COUNT; i++) {
            OrderRequest orderRequest = new OrderRequest(
                    RandomStringUtils.randomAlphabetic(6),
                    Collections.emptyList()
            );

            orderRequests.add(orderRequest);
        }

        return orderRequests;
    }
}
