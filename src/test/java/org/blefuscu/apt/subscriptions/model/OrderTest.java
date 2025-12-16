package org.blefuscu.apt.subscriptions.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.Test;

public class OrderTest {

	@Test
	public void testOrderBuilderShouldReturnABuiltOrderWithExpectedRequiredFields() {
		Order order = new Order.OrderBuilder(1, LocalDate.of(2025, 8, 21), "test@address.com").build();

		assertThat(order).isExactlyInstanceOf(Order.class);
		assertThat(order.getOrderId()).isEqualTo(1);
		assertThat(order.getOrderDate()).isEqualTo(LocalDate.of(2025, 8, 21));
		assertThat(order.getCustomerEmail()).isEqualTo("test@address.com");
	}

	@Test
	public void testOrderBuilderShouldSetGivenOrderFields() {
		Order order = new Order.OrderBuilder(1, LocalDate.of(2025, 8, 21), "test@address.com")
				.setShippingFirstName("Dario").setShippingLastName("Neri").build();

		assertThat(order.getOrderId()).isEqualTo(1);
		assertThat(order.getOrderDate()).isEqualTo(LocalDate.of(2025, 8, 21));
		assertThat(order.getCustomerEmail()).isEqualTo("test@address.com");
		assertThat(order.getShippingFirstName()).isEqualTo("Dario");
		assertThat(order.getShippingLastName()).isEqualTo("Neri");
	}

}
