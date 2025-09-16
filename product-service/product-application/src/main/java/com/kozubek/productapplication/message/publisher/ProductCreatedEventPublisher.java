package com.kozubek.productapplication.message.publisher;

import com.kozubek.commonapplication.saga.PublisherMeesage;
import com.kozubek.productdomain.event.ProductCreatedEvent;

public interface ProductCreatedEventPublisher extends PublisherMeesage<ProductCreatedEvent> {
}
