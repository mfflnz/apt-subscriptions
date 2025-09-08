package org.blefuscu.apt.subscriptions.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.blefuscu.apt.subscriptions.repository.OrderMongoRepository.ORDER_COLLECTION_NAME;
import static org.blefuscu.apt.subscriptions.repository.OrderMongoRepository.SUBSCRIPTIONS_DB_NAME;

import java.net.InetSocketAddress;
import java.time.LocalDate;

import org.blefuscu.apt.subscriptions.model.Order;
import org.bson.Document;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import de.bwaldvogel.mongo.MongoServer;
import de.bwaldvogel.mongo.backend.memory.MemoryBackend;

public class OrderMongoRepositoryTest {

	private static MongoServer server;
	private static InetSocketAddress serverAddress;

	private MongoClient client;
	private OrderMongoRepository orderRepository;
	private MongoCollection<Document> orderCollection;

	@BeforeClass
	public static void setUpBeforeClass() {
		server = new MongoServer(new MemoryBackend());
		serverAddress = server.bind();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		server.shutdown();
	}

	@Before
	public void setUp() {
		client = new MongoClient(new ServerAddress(serverAddress));
		orderRepository = new OrderMongoRepository(client, SUBSCRIPTIONS_DB_NAME, ORDER_COLLECTION_NAME);
		MongoDatabase database = client.getDatabase(SUBSCRIPTIONS_DB_NAME);
		database.drop();
		orderCollection = database.getCollection(ORDER_COLLECTION_NAME);
	}

	@After
	public void tearDown() {
		client.close();
	}

	@Test
	public void testFindAllWhenCollectionIsEmpty() {
		assertThat(orderRepository.findAll()).isEmpty();
	}

	@Test
	public void testFindAllWhenCollectionIsNotEmpty() {
		addTestOrderToDatabase(1, "2025-09-05", "customer@address.com");
		addTestOrderToDatabase(2, "2025-09-06", "other@address.com");
		assertThat(orderRepository.findAll()).containsExactly(
				new Order.OrderBuilder(1, LocalDate.of(2025, 9, 5), "customer@address.com").build(),
				new Order.OrderBuilder(2, LocalDate.of(2025, 9, 6), "other@address.com").build());
	}

	@Test
	public void testFindByDateRangeWhenCollectionIsEmpty() {
		assertThat(orderRepository.findByDateRange(LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 5))).isEmpty();
	}

	@Test
	public void testFindByDateRangeWhenNoOrderIsFound() {
		addTestOrderToDatabase(1, "2025-08-01", "customer@address.com");
		addTestOrderToDatabase(2, "2025-08-05", "other@address.com");
		assertThat(orderRepository.findByDateRange(LocalDate.of(2025, 9, 1), LocalDate.of(2025, 9, 4))).isEmpty();
	}

	@Test
	public void testFindByDateRangeWhenSomeOrderIsFound() {
		addTestOrderToDatabase(1, "2025-08-01", "customer@address.com");
		addTestOrderToDatabase(2, "2025-08-05", "other@address.com");
		addTestOrderToDatabase(3, "2025-08-07", "another@address.com");
		assertThat(orderRepository.findByDateRange(LocalDate.of(2025, 8, 1), LocalDate.of(2025, 8, 4)))
				.containsExactly(new Order.OrderBuilder(1, LocalDate.of(2025, 8, 1), "customer@address.com").build());
	}

	@Test
	public void testFindByDateRangeWhenDateRangeIsIncorrect() {
		assertThatThrownBy(() -> orderRepository.findByDateRange(LocalDate.of(2025, 8, 2), LocalDate.of(2025, 8, 1)))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Error: End date must be later than start date");
	}

	@Test
	public void testFindByDateRangeWhenStartDateIsEqualToEndDate() {
		addTestOrderToDatabase(1, "2025-08-01", "customer@address.com");
		addTestOrderToDatabase(2, "2025-08-01", "other@address.com");
		assertThat(orderRepository.findByDateRange(LocalDate.of(2025, 8, 1), LocalDate.of(2025, 8, 1))).containsExactly(
				new Order.OrderBuilder(1, LocalDate.of(2025, 8, 1), "customer@address.com").build(),
				new Order.OrderBuilder(2, LocalDate.of(2025, 8, 1), "other@address.com").build());
	}

	private void addTestOrderToDatabase(int orderId, String orderDate, String customerEmail) {
		orderCollection.insertOne(new Document().append("order_id", orderId).append("order_date", orderDate)
				.append("customer_email", customerEmail).append("order_number", 12345).append("status", "processing")
				.append("paid_date", "2025-09-10").append("billing_first_name", "Anna")
				.append("billing_last_name", "Rossi").append("billing_company", "")
				.append("billing_email", "cliente1@address.com").append("billing_phone", "'+39333123456")
				.append("billing_address_1", "vicolo Corto 1").append("billing_address_2", "")
				.append("billing_postcode", "40134").append("billing_city", "Bologna").append("billing_state", "BO")
				.append("billing_country", "IT").append("shipping_first_name", "Anna")
				.append("shipping_last_name", "Rossi").append("shipping_company", "").append("shipping_phone", "")
				.append("shipping_address_1", "Tabaccheria Tabucchi, via B. Cellini 10")
				.append("shipping_address_2", "").append("shipping_postcode", "73030").append("shipping_city", "Lecce")
				.append("shipping_state", "LE").append("shipping_country", "IT").append("customer_note", "")
				.append("wt_import_key", "12345").append("tax_items", "")
				.append("shipping_items",
						"items:Gli asini – nuova serie · 121 · luglio-agosto 2025 &times; 1|method_id:flat_rate|taxes:a:1:{s:5:\"total\";a:0:{}}")
				.append("fee_items", "").append("coupon_items", "").append("refund_items", "")
				.append("order_notes",
						"content:Stripe payment intent created (Payment Intent ID: fGz2WAt4dcsOemDvYUP1IH8Wc7N)|date:2025-08-05 11:11:03|customer:|added_by:system||content:Lo stato dell'ordine è cambiato da In attesa di pagamento a In lavorazione.|date:2025-08-05 11:11:07|customer:|added_by:system||content:Transazione Stripe completata (ID Transazione: fGz2WAt4dsPpomDvYUP1IH8WTnN)|date:2025-08-05 11:11:07|customer:|added_by:system")
				.append("download_permissions", 1).append("'meta:_wc_order_attribution_device_type'", "Mobile")
				.append("'meta:_wc_order_attribution_referrer'", "https://www.google.com/")
				.append("'meta:_wc_order_attribution_session_count'", 1)
				.append("'meta:_wc_order_attribution_session_entry'", "https://gliasinirivista.org/")
				.append("'meta:_wc_order_attribution_session_pages'", 6)
				.append("'meta:_wc_order_attribution_session_start_time'", "2025-08-05 09:01:16")
				.append("'meta:_wc_order_attribution_source_type'", "organic")
				.append("'meta:_wc_order_attribution_user_agent'",
						"Mozilla/5.0 (iPhone; CPU iPhone OS 16_1_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) GSA/123.4.567898765 Mobile/15E148 Safari/604.1")
				.append("'meta:_wc_order_attribution_utm_source'", "google").append("'meta:_ppcp_paypal_fees'", 0.34)
				.append("'meta:_stripe_currency'", "\"EUR\"").append("'meta:_stripe_fee'", 0.49)
				.append("'meta:_stripe_net'", 15.51)
				.append("line_item_1",
						"name:Gli asini – nuova serie · 121 · luglio-agosto 2025|product_id:31529|sku:|quantity:1|total:13.00|sub_total:13.00")
				.append("line_item_2", "").append("line_item_3", "").append("line_item_4", "").append("line_item_5", "")
				.append("order_confirmed", true).append("order_net_total", 15.30).append("first_issue", 112)
				.append("last_issue", 117));

	}

}
