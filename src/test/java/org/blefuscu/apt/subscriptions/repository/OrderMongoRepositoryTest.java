package org.blefuscu.apt.subscriptions.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.blefuscu.apt.subscriptions.repository.OrderMongoRepository.ORDER_COLLECTION_NAME;
import static org.blefuscu.apt.subscriptions.repository.OrderMongoRepository.SUBSCRIPTIONS_DB_NAME;
import static org.blefuscu.apt.subscriptions.repository.OrderMongoRepository.DATE_TIME_FORMATTER;

import java.net.InetSocketAddress;
import java.time.LocalDate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

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
	public static void tearDownAfterClass() {
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
		assertThat(orderRepository.findAll()).containsExactly(new Order.OrderBuilder(1, LocalDate.of(2025, 9, 5),
				"customer@address.com").setOrderNumber(12345).setPaidDate(LocalDate.of(2025, 9, 10))
				.setStatus("processing").setShippingTotal(3.0).setShippingTotal(3.0).setShippingTaxTotal(0.0)
				.setFeeTotal(0.0).setFeeTaxTotal(0.0).setTaxTotal(0.0).setCartDiscount(0.0).setOrderDiscount(0.0)
				.setDiscountTotal(0.0).setOrderTotal(16.0).setOrderSubtotal(13.0).setOrderKey("wc_order_yo6dmEfz4tlg")
				.setOrderCurrency("EUR").setPaymentMethod("stripe").setPaymentMethodTitle("Carta di credito")
				.setTransactionId("fGz2WAt4dcsOemDvYUP1IH8WTnN").setCustomerIpAddress("0.0.0.0")
				.setCustomerUserAgent(
						"Mozilla/5.0 (iPhone; CPU iPhone OS 16_1_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) GSA/373.1.772062114 Mobile/15E148 Safari/604.1")
				.setShippingMethod("Spedizione con corriere tracciabile Gls (3-4 gg lavorativi)").setCustomerId(1234)
				.setCustomerUser("1234").setBillingFirstName("Anna").setBillingLastName("Rossi").setBillingCompany("")
				.setBillingEmail("cliente1@address.com").setBillingPhone("'+39333123456")
				.setBillingAddress1("vicolo Corto 1").setBillingAddress2("").setBillingPostcode("40134")
				.setBillingCity("Bologna").setBillingState("BO").setBillingCountry("IT").setShippingFirstName("Anna")
				.setShippingLastName("Rossi").setShippingCompany("").setShippingPhone("")
				.setShippingAddress1("Tabaccheria Tabucchi, via B. Cellini 10").setShippingAddress2("")
				.setShippingPostcode("73030").setShippingCity("Lecce").setShippingState("LE").setShippingCountry("IT")
				.setCustomerNote("").setWtImportKey("12345").setTaxItems("")
				.setShippingItems(
						"items:Gli asini – nuova serie · 121 · luglio-agosto 2025 &times; 1|method_id:flat_rate|taxes:a:1:{s:5:\"total\";a:0:{}}")
				.setFeeItems("").setCouponItems("").setRefundItems("")
				.setOrderNotes(
						"content:Stripe payment intent created (Payment Intent ID: fGz2WAt4dcsOemDvYUP1IH8Wc7N)|date:2025-08-05 11:11:03|customer:|added_by:system||content:Lo stato dell'ordine è cambiato da In attesa di pagamento a In lavorazione.|date:2025-08-05 11:11:07|customer:|added_by:system||content:Transazione Stripe completata (ID Transazione: fGz2WAt4dsPpomDvYUP1IH8WTnN)|date:2025-08-05 11:11:07|customer:|added_by:system")
				.setDownloadPermissions("1").setMetaWcOrderAttributionDeviceType("Mobile")
				.setMetaWcOrderAttributionReferrer("https://www.google.com/").setMetaWcOrderAttributionSessionCount("1")
				.setMetaWcOrderAttributionSessionEntry("https://gliasinirivista.org/")
				.setMetaWcOrderAttributionSessionPages("6")
				.setMetaWcOrderAttributionSessionStartTime("2025-08-05 09:01:16").setMetaWcOrderAttributionSourceType(
						"organic")
				.setMetaWcOrderAttributionUserAgent(
						"Mozilla/5.0 (iPhone; CPU iPhone OS 16_1_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) GSA/123.4.567898765 Mobile/15E148 Safari/604.1")
				.setMetaWcOrderAttributionUtmSource("google").setMetaPpcpPaypalFees(0.34).setMetaStripeCurrency(
						"\"EUR\"")
				.setMetaStripeFee(0.49).setMetaStripeNet(15.51).setLineItem1(
						"name:Gli asini – nuova serie · 121 · luglio-agosto 2025|product_id:31529|sku:|quantity:1|total:13.00|sub_total:13.00")
				.setLineItem2("").setLineItem3("").setLineItem4("").setLineItem5("").setOrderConfirmed(true)
				.setOrderNetTotal(15.30).setFirstIssue(112).setLastIssue(117).build(),
				new Order.OrderBuilder(2, LocalDate.of(2025, 9, 6), "other@address.com").setOrderNumber(12345)
						.setPaidDate(LocalDate.of(2025, 9, 10)).setStatus("processing").setShippingTotal(3.0)
						.setShippingTotal(3.0).setShippingTaxTotal(0.0).setFeeTotal(0.0).setFeeTaxTotal(0.0)
						.setTaxTotal(0.0).setCartDiscount(0.0).setOrderDiscount(0.0).setDiscountTotal(0.0)
						.setOrderTotal(16.0).setOrderSubtotal(13.0).setOrderKey("wc_order_yo6dmEfz4tlg")
						.setOrderCurrency("EUR").setPaymentMethod("stripe").setPaymentMethodTitle("Carta di credito")
						.setTransactionId("fGz2WAt4dcsOemDvYUP1IH8WTnN").setCustomerIpAddress("0.0.0.0")
						.setCustomerUserAgent(
								"Mozilla/5.0 (iPhone; CPU iPhone OS 16_1_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) GSA/373.1.772062114 Mobile/15E148 Safari/604.1")
						.setShippingMethod("Spedizione con corriere tracciabile Gls (3-4 gg lavorativi)")
						.setCustomerId(1234).setCustomerUser("1234").setBillingFirstName("Anna")
						.setBillingLastName("Rossi").setBillingCompany("").setBillingEmail("cliente1@address.com")
						.setBillingPhone("'+39333123456").setBillingAddress1("vicolo Corto 1").setBillingAddress2("")
						.setBillingPostcode("40134").setBillingCity("Bologna").setBillingState("BO")
						.setBillingCountry("IT").setShippingFirstName("Anna").setShippingLastName("Rossi")
						.setShippingCompany("").setShippingPhone("")
						.setShippingAddress1("Tabaccheria Tabucchi, via B. Cellini 10").setShippingAddress2("")
						.setShippingPostcode("73030").setShippingCity("Lecce").setShippingState("LE")
						.setShippingCountry("IT").setCustomerNote("").setWtImportKey("12345").setTaxItems("")
						.setShippingItems(
								"items:Gli asini – nuova serie · 121 · luglio-agosto 2025 &times; 1|method_id:flat_rate|taxes:a:1:{s:5:\"total\";a:0:{}}")
						.setFeeItems("").setCouponItems("").setRefundItems("")
						.setOrderNotes(
								"content:Stripe payment intent created (Payment Intent ID: fGz2WAt4dcsOemDvYUP1IH8Wc7N)|date:2025-08-05 11:11:03|customer:|added_by:system||content:Lo stato dell'ordine è cambiato da In attesa di pagamento a In lavorazione.|date:2025-08-05 11:11:07|customer:|added_by:system||content:Transazione Stripe completata (ID Transazione: fGz2WAt4dsPpomDvYUP1IH8WTnN)|date:2025-08-05 11:11:07|customer:|added_by:system")
						.setDownloadPermissions("1").setMetaWcOrderAttributionDeviceType("Mobile")
						.setMetaWcOrderAttributionReferrer("https://www.google.com/")
						.setMetaWcOrderAttributionSessionCount("1")
						.setMetaWcOrderAttributionSessionEntry("https://gliasinirivista.org/")
						.setMetaWcOrderAttributionSessionPages("6")
						.setMetaWcOrderAttributionSessionStartTime("2025-08-05 09:01:16")
						.setMetaWcOrderAttributionSourceType("organic")
						.setMetaWcOrderAttributionUserAgent(
								"Mozilla/5.0 (iPhone; CPU iPhone OS 16_1_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) GSA/123.4.567898765 Mobile/15E148 Safari/604.1")
						.setMetaWcOrderAttributionUtmSource("google").setMetaPpcpPaypalFees(0.34)
						.setMetaStripeCurrency("\"EUR\"").setMetaStripeFee(0.49).setMetaStripeNet(15.51)
						.setLineItem1(
								"name:Gli asini – nuova serie · 121 · luglio-agosto 2025|product_id:31529|sku:|quantity:1|total:13.00|sub_total:13.00")
						.setLineItem2("").setLineItem3("").setLineItem4("").setLineItem5("").setOrderConfirmed(true)
						.setOrderNetTotal(15.30).setFirstIssue(112).setLastIssue(117).build());
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
		assertThat(orderRepository.findByDateRange(LocalDate.of(2025, 8, 1), LocalDate.of(2025, 8, 4))).containsExactly(
				new Order.OrderBuilder(1, LocalDate.of(2025, 8, 1), "customer@address.com").setOrderNumber(12345)
						.setPaidDate(LocalDate.of(2025, 9, 10)).setStatus("processing").setShippingTotal(3.0)
						.setShippingTotal(3.0).setShippingTaxTotal(0.0).setFeeTotal(0.0).setFeeTaxTotal(0.0)
						.setTaxTotal(0.0).setCartDiscount(0.0).setOrderDiscount(0.0).setDiscountTotal(0.0)
						.setOrderTotal(16.0).setOrderSubtotal(13.0).setOrderKey("wc_order_yo6dmEfz4tlg")
						.setOrderCurrency("EUR").setPaymentMethod("stripe").setPaymentMethodTitle("Carta di credito")
						.setTransactionId("fGz2WAt4dcsOemDvYUP1IH8WTnN").setCustomerIpAddress("0.0.0.0")
						.setCustomerUserAgent(
								"Mozilla/5.0 (iPhone; CPU iPhone OS 16_1_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) GSA/373.1.772062114 Mobile/15E148 Safari/604.1")
						.setShippingMethod("Spedizione con corriere tracciabile Gls (3-4 gg lavorativi)")
						.setCustomerId(1234).setCustomerUser("1234").setBillingFirstName("Anna")
						.setBillingLastName("Rossi").setBillingCompany("").setBillingEmail("cliente1@address.com")
						.setBillingPhone("'+39333123456").setBillingAddress1("vicolo Corto 1").setBillingAddress2("")
						.setBillingPostcode("40134").setBillingCity("Bologna").setBillingState("BO")
						.setBillingCountry("IT").setShippingFirstName("Anna").setShippingLastName("Rossi")
						.setShippingCompany("").setShippingPhone("")
						.setShippingAddress1("Tabaccheria Tabucchi, via B. Cellini 10").setShippingAddress2("")
						.setShippingPostcode("73030").setShippingCity("Lecce").setShippingState("LE")
						.setShippingCountry("IT").setCustomerNote("").setWtImportKey("12345").setTaxItems("")
						.setShippingItems(
								"items:Gli asini – nuova serie · 121 · luglio-agosto 2025 &times; 1|method_id:flat_rate|taxes:a:1:{s:5:\"total\";a:0:{}}")
						.setFeeItems("").setCouponItems("").setRefundItems("")
						.setOrderNotes(
								"content:Stripe payment intent created (Payment Intent ID: fGz2WAt4dcsOemDvYUP1IH8Wc7N)|date:2025-08-05 11:11:03|customer:|added_by:system||content:Lo stato dell'ordine è cambiato da In attesa di pagamento a In lavorazione.|date:2025-08-05 11:11:07|customer:|added_by:system||content:Transazione Stripe completata (ID Transazione: fGz2WAt4dsPpomDvYUP1IH8WTnN)|date:2025-08-05 11:11:07|customer:|added_by:system")
						.setDownloadPermissions("1").setMetaWcOrderAttributionDeviceType("Mobile")
						.setMetaWcOrderAttributionReferrer("https://www.google.com/")
						.setMetaWcOrderAttributionSessionCount("1")
						.setMetaWcOrderAttributionSessionEntry("https://gliasinirivista.org/")
						.setMetaWcOrderAttributionSessionPages("6")
						.setMetaWcOrderAttributionSessionStartTime("2025-08-05 09:01:16")
						.setMetaWcOrderAttributionSourceType("organic")
						.setMetaWcOrderAttributionUserAgent(
								"Mozilla/5.0 (iPhone; CPU iPhone OS 16_1_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) GSA/123.4.567898765 Mobile/15E148 Safari/604.1")
						.setMetaWcOrderAttributionUtmSource("google").setMetaPpcpPaypalFees(0.34)
						.setMetaStripeCurrency("\"EUR\"").setMetaStripeFee(0.49).setMetaStripeNet(15.51)
						.setLineItem1(
								"name:Gli asini – nuova serie · 121 · luglio-agosto 2025|product_id:31529|sku:|quantity:1|total:13.00|sub_total:13.00")
						.setLineItem2("").setLineItem3("").setLineItem4("").setLineItem5("").setOrderConfirmed(true)
						.setOrderNetTotal(15.30).setFirstIssue(112).setLastIssue(117).build());
	}

	@Test
	public void testFindByDateRangeWhenDateRangeIsIncorrect() {
		LocalDate fromDate = LocalDate.of(2025, 8, 2);
		LocalDate toDate = LocalDate.of(2025, 8, 1);
		assertThatThrownBy(() -> orderRepository.findByDateRange(fromDate, toDate))
				.isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Error: End date must be later than start date");
	}

	@Test
	public void testFindByDateRangeWhenStartDateIsEqualToEndDate() {
		addTestOrderToDatabase(1, "2025-08-01", "customer@address.com");
		addTestOrderToDatabase(2, "2025-08-01", "other@address.com");
		assertThat(orderRepository.findByDateRange(LocalDate.of(2025, 8, 1), LocalDate.of(2025, 8, 1))).containsExactly(
				new Order.OrderBuilder(1, LocalDate.of(2025, 8, 1), "customer@address.com").setOrderNumber(12345)
						.setPaidDate(LocalDate.of(2025, 9, 10)).setStatus("processing").setShippingTotal(3.0)
						.setShippingTotal(3.0).setShippingTaxTotal(0.0).setFeeTotal(0.0).setFeeTaxTotal(0.0)
						.setTaxTotal(0.0).setCartDiscount(0.0).setOrderDiscount(0.0).setDiscountTotal(0.0)
						.setOrderTotal(16.0).setOrderSubtotal(13.0).setOrderKey("wc_order_yo6dmEfz4tlg")
						.setOrderCurrency("EUR").setPaymentMethod("stripe").setPaymentMethodTitle("Carta di credito")
						.setTransactionId("fGz2WAt4dcsOemDvYUP1IH8WTnN").setCustomerIpAddress("0.0.0.0")
						.setCustomerUserAgent(
								"Mozilla/5.0 (iPhone; CPU iPhone OS 16_1_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) GSA/373.1.772062114 Mobile/15E148 Safari/604.1")
						.setShippingMethod("Spedizione con corriere tracciabile Gls (3-4 gg lavorativi)")
						.setCustomerId(1234).setCustomerUser("1234").setBillingFirstName("Anna")
						.setBillingLastName("Rossi").setBillingCompany("").setBillingEmail("cliente1@address.com")
						.setBillingPhone("'+39333123456").setBillingAddress1("vicolo Corto 1").setBillingAddress2("")
						.setBillingPostcode("40134").setBillingCity("Bologna").setBillingState("BO")
						.setBillingCountry("IT").setShippingFirstName("Anna").setShippingLastName("Rossi")
						.setShippingCompany("").setShippingPhone("")
						.setShippingAddress1("Tabaccheria Tabucchi, via B. Cellini 10").setShippingAddress2("")
						.setShippingPostcode("73030").setShippingCity("Lecce").setShippingState("LE")
						.setShippingCountry("IT").setCustomerNote("").setWtImportKey("12345").setTaxItems("")
						.setShippingItems(
								"items:Gli asini – nuova serie · 121 · luglio-agosto 2025 &times; 1|method_id:flat_rate|taxes:a:1:{s:5:\"total\";a:0:{}}")
						.setFeeItems("").setCouponItems("").setRefundItems("")
						.setOrderNotes(
								"content:Stripe payment intent created (Payment Intent ID: fGz2WAt4dcsOemDvYUP1IH8Wc7N)|date:2025-08-05 11:11:03|customer:|added_by:system||content:Lo stato dell'ordine è cambiato da In attesa di pagamento a In lavorazione.|date:2025-08-05 11:11:07|customer:|added_by:system||content:Transazione Stripe completata (ID Transazione: fGz2WAt4dsPpomDvYUP1IH8WTnN)|date:2025-08-05 11:11:07|customer:|added_by:system")
						.setDownloadPermissions("1").setMetaWcOrderAttributionDeviceType("Mobile")
						.setMetaWcOrderAttributionReferrer("https://www.google.com/")
						.setMetaWcOrderAttributionSessionCount("1")
						.setMetaWcOrderAttributionSessionEntry("https://gliasinirivista.org/")
						.setMetaWcOrderAttributionSessionPages("6")
						.setMetaWcOrderAttributionSessionStartTime("2025-08-05 09:01:16")
						.setMetaWcOrderAttributionSourceType("organic").setMetaWcOrderAttributionUserAgent(
								"Mozilla/5.0 (iPhone; CPU iPhone OS 16_1_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) GSA/123.4.567898765 Mobile/15E148 Safari/604.1")
						.setMetaWcOrderAttributionUtmSource("google").setMetaPpcpPaypalFees(0.34).setMetaStripeCurrency(
								"\"EUR\"")
						.setMetaStripeFee(0.49).setMetaStripeNet(15.51).setLineItem1(
								"name:Gli asini – nuova serie · 121 · luglio-agosto 2025|product_id:31529|sku:|quantity:1|total:13.00|sub_total:13.00")
						.setLineItem2("").setLineItem3("").setLineItem4("").setLineItem5("").setOrderConfirmed(true)
						.setOrderNetTotal(15.30).setFirstIssue(112).setLastIssue(117).build(),
				new Order.OrderBuilder(2, LocalDate.of(2025, 8, 1), "other@address.com").setOrderNumber(12345)
						.setPaidDate(LocalDate.of(2025, 9, 10)).setStatus("processing").setShippingTotal(3.0)
						.setShippingTotal(3.0).setShippingTaxTotal(0.0).setFeeTotal(0.0).setFeeTaxTotal(0.0)
						.setTaxTotal(0.0).setCartDiscount(0.0).setOrderDiscount(0.0).setDiscountTotal(0.0)
						.setOrderTotal(16.0).setOrderSubtotal(13.0).setOrderKey("wc_order_yo6dmEfz4tlg")
						.setOrderCurrency("EUR").setPaymentMethod("stripe").setPaymentMethodTitle("Carta di credito")
						.setTransactionId("fGz2WAt4dcsOemDvYUP1IH8WTnN").setCustomerIpAddress("0.0.0.0")
						.setCustomerUserAgent(
								"Mozilla/5.0 (iPhone; CPU iPhone OS 16_1_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) GSA/373.1.772062114 Mobile/15E148 Safari/604.1")
						.setShippingMethod("Spedizione con corriere tracciabile Gls (3-4 gg lavorativi)")
						.setCustomerId(1234).setCustomerUser("1234").setBillingFirstName("Anna")
						.setBillingLastName("Rossi").setBillingCompany("").setBillingEmail("cliente1@address.com")
						.setBillingPhone("'+39333123456").setBillingAddress1("vicolo Corto 1").setBillingAddress2("")
						.setBillingPostcode("40134").setBillingCity("Bologna").setBillingState("BO")
						.setBillingCountry("IT").setShippingFirstName("Anna").setShippingLastName("Rossi")
						.setShippingCompany("").setShippingPhone("")
						.setShippingAddress1("Tabaccheria Tabucchi, via B. Cellini 10").setShippingAddress2("")
						.setShippingPostcode("73030").setShippingCity("Lecce").setShippingState("LE")
						.setShippingCountry("IT").setCustomerNote("").setWtImportKey("12345").setTaxItems("")
						.setShippingItems(
								"items:Gli asini – nuova serie · 121 · luglio-agosto 2025 &times; 1|method_id:flat_rate|taxes:a:1:{s:5:\"total\";a:0:{}}")
						.setFeeItems("").setCouponItems("").setRefundItems("")
						.setOrderNotes(
								"content:Stripe payment intent created (Payment Intent ID: fGz2WAt4dcsOemDvYUP1IH8Wc7N)|date:2025-08-05 11:11:03|customer:|added_by:system||content:Lo stato dell'ordine è cambiato da In attesa di pagamento a In lavorazione.|date:2025-08-05 11:11:07|customer:|added_by:system||content:Transazione Stripe completata (ID Transazione: fGz2WAt4dsPpomDvYUP1IH8WTnN)|date:2025-08-05 11:11:07|customer:|added_by:system")
						.setDownloadPermissions("1").setMetaWcOrderAttributionDeviceType("Mobile")
						.setMetaWcOrderAttributionReferrer("https://www.google.com/")
						.setMetaWcOrderAttributionSessionCount("1")
						.setMetaWcOrderAttributionSessionEntry("https://gliasinirivista.org/")
						.setMetaWcOrderAttributionSessionPages("6")
						.setMetaWcOrderAttributionSessionStartTime("2025-08-05 09:01:16")
						.setMetaWcOrderAttributionSourceType("organic")
						.setMetaWcOrderAttributionUserAgent(
								"Mozilla/5.0 (iPhone; CPU iPhone OS 16_1_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) GSA/123.4.567898765 Mobile/15E148 Safari/604.1")
						.setMetaWcOrderAttributionUtmSource("google").setMetaPpcpPaypalFees(0.34)
						.setMetaStripeCurrency("\"EUR\"").setMetaStripeFee(0.49).setMetaStripeNet(15.51)
						.setLineItem1(
								"name:Gli asini – nuova serie · 121 · luglio-agosto 2025|product_id:31529|sku:|quantity:1|total:13.00|sub_total:13.00")
						.setLineItem2("").setLineItem3("").setLineItem4("").setLineItem5("").setOrderConfirmed(true)
						.setOrderNetTotal(15.30).setFirstIssue(112).setLastIssue(117).build());
	}

	@Test
	public void testFindByIdWhenOrderIsNotFound() {
		assertThat(orderRepository.findById(4)).isNull();
	}

	@Test
	public void testFindByIdWhenOrderIsFound() {
		addTestOrderToDatabase(1, "2025-09-09", "customer@address.com");
		assertThat(orderRepository.findById(1)).isEqualTo(new Order.OrderBuilder(1, LocalDate.of(2025, 9, 9),
				"customer@address.com").setOrderNumber(12345).setPaidDate(LocalDate.of(2025, 9, 10))
				.setStatus("processing").setShippingTotal(3.0).setShippingTotal(3.0).setShippingTaxTotal(0.0)
				.setFeeTotal(0.0).setFeeTaxTotal(0.0).setTaxTotal(0.0).setCartDiscount(0.0).setOrderDiscount(0.0)
				.setDiscountTotal(0.0).setOrderTotal(16.0).setOrderSubtotal(13.0).setOrderKey("wc_order_yo6dmEfz4tlg")
				.setOrderCurrency("EUR").setPaymentMethod("stripe").setPaymentMethodTitle("Carta di credito")
				.setTransactionId("fGz2WAt4dcsOemDvYUP1IH8WTnN").setCustomerIpAddress("0.0.0.0")
				.setCustomerUserAgent(
						"Mozilla/5.0 (iPhone; CPU iPhone OS 16_1_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) GSA/373.1.772062114 Mobile/15E148 Safari/604.1")
				.setShippingMethod("Spedizione con corriere tracciabile Gls (3-4 gg lavorativi)").setCustomerId(1234)
				.setCustomerUser("1234").setBillingFirstName("Anna").setBillingLastName("Rossi").setBillingCompany("")
				.setBillingEmail("cliente1@address.com").setBillingPhone("'+39333123456")
				.setBillingAddress1("vicolo Corto 1").setBillingAddress2("").setBillingPostcode("40134")
				.setBillingCity("Bologna").setBillingState("BO").setBillingCountry("IT").setShippingFirstName("Anna")
				.setShippingLastName("Rossi").setShippingCompany("").setShippingPhone("")
				.setShippingAddress1("Tabaccheria Tabucchi, via B. Cellini 10").setShippingAddress2("")
				.setShippingPostcode("73030").setShippingCity("Lecce").setShippingState("LE").setShippingCountry("IT")
				.setCustomerNote("").setWtImportKey("12345").setTaxItems("")
				.setShippingItems(
						"items:Gli asini – nuova serie · 121 · luglio-agosto 2025 &times; 1|method_id:flat_rate|taxes:a:1:{s:5:\"total\";a:0:{}}")
				.setFeeItems("").setCouponItems("").setRefundItems("")
				.setOrderNotes(
						"content:Stripe payment intent created (Payment Intent ID: fGz2WAt4dcsOemDvYUP1IH8Wc7N)|date:2025-08-05 11:11:03|customer:|added_by:system||content:Lo stato dell'ordine è cambiato da In attesa di pagamento a In lavorazione.|date:2025-08-05 11:11:07|customer:|added_by:system||content:Transazione Stripe completata (ID Transazione: fGz2WAt4dsPpomDvYUP1IH8WTnN)|date:2025-08-05 11:11:07|customer:|added_by:system")
				.setDownloadPermissions("1").setMetaWcOrderAttributionDeviceType("Mobile")
				.setMetaWcOrderAttributionReferrer("https://www.google.com/").setMetaWcOrderAttributionSessionCount("1")
				.setMetaWcOrderAttributionSessionEntry("https://gliasinirivista.org/")
				.setMetaWcOrderAttributionSessionPages("6")
				.setMetaWcOrderAttributionSessionStartTime("2025-08-05 09:01:16")
				.setMetaWcOrderAttributionSourceType("organic")
				.setMetaWcOrderAttributionUserAgent(
						"Mozilla/5.0 (iPhone; CPU iPhone OS 16_1_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) GSA/123.4.567898765 Mobile/15E148 Safari/604.1")
				.setMetaWcOrderAttributionUtmSource("google").setMetaPpcpPaypalFees(0.34)
				.setMetaStripeCurrency("\"EUR\"").setMetaStripeFee(0.49).setMetaStripeNet(15.51)
				.setLineItem1(
						"name:Gli asini – nuova serie · 121 · luglio-agosto 2025|product_id:31529|sku:|quantity:1|total:13.00|sub_total:13.00")
				.setLineItem2("").setLineItem3("").setLineItem4("").setLineItem5("").setOrderConfirmed(true)
				.setOrderNetTotal(15.30).setFirstIssue(112).setLastIssue(117).build());
	}

	@Test
	public void testUpdateWhenOrderToUpdateIsFound() {
		addTestOrderToDatabase(1, "2025-09-09", "customer@address.com");

		assertThat(StreamSupport.stream(orderCollection.find().spliterator(), false)
				.map(d -> new Order.OrderBuilder(d.getInteger("order_id"),
						LocalDate.parse(d.getString("order_date"), DATE_TIME_FORMATTER), d.getString("customer_email"))
						.build())
				.collect(Collectors.toList()))
				.containsExactly(new Order.OrderBuilder(1, LocalDate.of(2025, 9, 9), "customer@address.com").build());

		Order updatedOrder = new Order.OrderBuilder(1, LocalDate.of(2025, 9, 9), "updated@address.com").build();

		orderRepository.update(1, updatedOrder);

		assertThat(StreamSupport.stream(orderCollection.find().spliterator(), false)
				.map(d -> new Order.OrderBuilder(d.getInteger("order_id"),
						LocalDate.parse(d.getString("order_date"), DATE_TIME_FORMATTER), d.getString("customer_email"))
						.build())
				.collect(Collectors.toList()))
				.containsExactly(new Order.OrderBuilder(1, LocalDate.of(2025, 9, 9), "updated@address.com").build());

	}

	@Test
	public void testUpdateWhenOrderToUpdateIsNotFound() {
		assertThat(orderCollection.find()).isEmpty();
		addTestOrderToDatabase(2, "2025-09-09", "customer@email.com");
		Order updatedOrder = new Order.OrderBuilder(5, LocalDate.of(2025, 9, 9), "some@address.com").build();
		assertThatThrownBy(() -> orderRepository.update(1, updatedOrder)).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Error: Order not found");
	}

	@Test
	public void testDeleteWhenOrderToDeleteIsFound() {
		assertThat(orderCollection.find()).isEmpty();
		addTestOrderToDatabase(1, "2025-09-09", "customer@address.com");
		orderRepository.delete(1);
		assertThat(orderCollection.find()).isEmpty();

	}

	@Test
	public void testDeleteWhenOrderToDeleteIsNotFound() {
		assertThat(orderCollection.find()).isEmpty();
		assertThatThrownBy(() -> orderRepository.delete(1)).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Error: Order not found");

	}

	@Test
	public void testSaveOrderWhenOrderIdIsNotPresent() {
		Order order = new Order.OrderBuilder(1, LocalDate.of(2025, 11, 11), "customer@email.com")
				.setPaidDate(LocalDate.of(2025, 11, 12)).build();
		orderRepository.save(order);

		assertThat(StreamSupport.stream(orderCollection.find().spliterator(), false)
				.map(d -> new Order.OrderBuilder(d.getInteger("order_id"),
						LocalDate.parse(d.getString("order_date"), DATE_TIME_FORMATTER), d.getString("customer_email"))
						.build())
				.collect(Collectors.toList()))
				.containsExactly(new Order.OrderBuilder(1, LocalDate.of(2025, 11, 11), "customer@email.com").build());

	}

	@Test
	public void testSaveOrderWhenOrderIdIsAlreadyPresent() {
		Order order1 = new Order.OrderBuilder(31, LocalDate.of(2025, 11, 11), "customer@email.com")
				.setPaidDate(LocalDate.of(2025, 11, 12)).build();
		Order order2 = new Order.OrderBuilder(31, LocalDate.of(2025, 11, 15), "other@email.com")
				.setPaidDate(LocalDate.of(2025, 11, 15)).build();
		orderRepository.save(order1);

		assertThat(StreamSupport.stream(orderCollection.find().spliterator(), false)
				.map(d -> new Order.OrderBuilder(d.getInteger("order_id"),
						LocalDate.parse(d.getString("order_date"), DATE_TIME_FORMATTER), d.getString("customer_email"))
						.build())
				.collect(Collectors.toList()))
				.containsExactly(new Order.OrderBuilder(31, LocalDate.of(2025, 11, 11), "customer@email.com").build());

		assertThatThrownBy(() -> orderRepository.save(order2)).isInstanceOf(IllegalArgumentException.class)
				.hasMessage("Error: Order with id 31 already in database");
	}

	private void addTestOrderToDatabase(int orderId, String orderDate, String customerEmail) {
		orderCollection.insertOne(new Document().append("order_id", orderId).append("order_date", orderDate)
				.append("customer_email", customerEmail).append("order_number", 12345).append("status", "processing")
				.append("shipping_total", 3.0).append("shipping_tax_total", 0.0).append("fee_total", 0.0)
				.append("fee_tax_total", 0.0).append("tax_total", 0.0).append("cart_discount", 0.0)
				.append("order_discount", 0.0).append("discount_total", 0.0).append("order_total", 16.0)
				.append("order_subtotal", 13.0).append("order_key", "wc_order_yo6dmEfz4tlg")
				.append("order_currency", "EUR").append("payment_method", "stripe")
				.append("payment_method_title", "Carta di credito")
				.append("transaction_id", "fGz2WAt4dcsOemDvYUP1IH8WTnN").append("customer_ip_address", "0.0.0.0")
				.append("customer_user_agent",
						"Mozilla/5.0 (iPhone; CPU iPhone OS 16_1_1 like Mac OS X) AppleWebKit/605.1.15 (KHTML, like Gecko) GSA/373.1.772062114 Mobile/15E148 Safari/604.1")
				.append("shipping_method", "Spedizione con corriere tracciabile Gls (3-4 gg lavorativi)")
				.append("customer_id", 1234).append("customer_user", "1234").append("paid_date", "2025-09-10")
				.append("billing_first_name", "Anna").append("billing_last_name", "Rossi").append("billing_company", "")
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
				.append("download_permissions", "1").append("'meta:_wc_order_attribution_device_type'", "Mobile")
				.append("'meta:_wc_order_attribution_referrer'", "https://www.google.com/")
				.append("'meta:_wc_order_attribution_session_count'", "1")
				.append("'meta:_wc_order_attribution_session_entry'", "https://gliasinirivista.org/")
				.append("'meta:_wc_order_attribution_session_pages'", "6")
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
