package org.blefuscu.apt.subscriptions.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.swing.fixture.Containers.showInFrame;
import static org.blefuscu.apt.subscriptions.repository.OrderMongoRepository.ORDER_COLLECTION_NAME;
import static org.blefuscu.apt.subscriptions.repository.OrderMongoRepository.SUBSCRIPTIONS_DB_NAME;
import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import javax.swing.DefaultListModel;

import org.assertj.swing.edt.GuiActionRunner;
import org.assertj.swing.fixture.FrameFixture;
import org.blefuscu.apt.subscriptions.model.Order;
import org.blefuscu.apt.subscriptions.repository.OrderMongoRepository;
import org.blefuscu.apt.subscriptions.repository.OrderRepository;
import org.blefuscu.apt.subscriptions.view.ListSwingView;
import org.blefuscu.apt.subscriptions.view.MessageSwingView;
import org.blefuscu.apt.subscriptions.view.OrderSwingView;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testcontainers.mongodb.MongoDBContainer;

import com.mongodb.MongoClient;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;

public class SubscriptionsControllerTestcontainersNoMocksIT {

	private SubscriptionsController subscriptionsController;

	private ListSwingView listView;

	private OrderSwingView orderView;

	private MessageSwingView messageView;

	private OrderRepository orderRepository;
	private ExportController exportController;

	private static MongoDBContainer mongoDBContainer;
	private MongoClient client;

	private FrameFixture window;

	@BeforeClass
	public static void setUpBeforeClass() {
		mongoDBContainer = new MongoDBContainer("mongo:5");
		mongoDBContainer.start();
	}

	@Before
	public void setUp() {

		listView = new ListSwingView();
		orderView = new OrderSwingView();
		messageView = new MessageSwingView();
		exportController = new ExportController(messageView);
		client = new MongoClient(new ServerAddress(mongoDBContainer.getHost(), mongoDBContainer.getMappedPort(27017)));
		orderRepository = new OrderMongoRepository(client, SUBSCRIPTIONS_DB_NAME, ORDER_COLLECTION_NAME);
		subscriptionsController = new SubscriptionsController(listView, orderView, messageView, orderRepository,
				exportController);

		MongoDatabase database = client.getDatabase(SUBSCRIPTIONS_DB_NAME);
		database.drop();

	}

	@After
	public void tearDown() {
		client.close();

	}

	@AfterClass
	public static void tearDownAfterClass() {
		mongoDBContainer.stop();
	}

	@Test
	public void testRequestAllOrders() {
		Order order = new Order.OrderBuilder(1, LocalDate.of(2025, 11, 11), "customer@email.com").setOrderNumber(12345)
				.setPaidDate(LocalDate.of(2025, 9, 10)).setStatus("processing").setShippingTotal(3.0)
				.setShippingTotal(3.0).setShippingTaxTotal(0.0).setFeeTotal(0.0).setFeeTaxTotal(0.0).setTaxTotal(0.0)
				.setCartDiscount(0.0).setOrderDiscount(0.0).setDiscountTotal(0.0).setOrderTotal(16.0)
				.setOrderSubtotal(13.0).setOrderKey("wc_order_yo6dmEfz4tlg").setOrderCurrency("EUR")
				.setPaymentMethod("stripe").setPaymentMethodTitle("Carta di credito")
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
				.setLineItem2("").setLineItem3("").setLineItem4("").setLineItem5("").setOrderNetTotal(15.30)
				.setFirstIssue(112).setLastIssue(117).build();
		DefaultListModel<Order> listOrdersModel = new DefaultListModel<>();
		listOrdersModel.addElement(order);

		orderRepository.save(order);
		subscriptionsController.requestOrders();

		assertThat(listOrdersModel.elementAt(0)).isEqualTo(listView.getListOrdersModel().elementAt(0));

	}

	@Test
	public void testReadFromDatabaseWhenDatesAreProvided() {

		Order order1 = new Order.OrderBuilder(1, LocalDate.of(2025, 11, 1), "customer@email.com").setOrderNumber(12345)
				.setPaidDate(LocalDate.of(2025, 9, 10)).setStatus("processing").setShippingTotal(3.0)
				.setShippingTotal(3.0).setShippingTaxTotal(0.0).setFeeTotal(0.0).setFeeTaxTotal(0.0).setTaxTotal(0.0)
				.setCartDiscount(0.0).setOrderDiscount(0.0).setDiscountTotal(0.0).setOrderTotal(16.0)
				.setOrderSubtotal(13.0).setOrderKey("wc_order_yo6dmEfz4tlg").setOrderCurrency("EUR")
				.setPaymentMethod("stripe").setPaymentMethodTitle("Carta di credito")
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
				.setLineItem2("").setLineItem3("").setLineItem4("").setLineItem5("").setOrderNetTotal(15.30)
				.setFirstIssue(112).setLastIssue(117).build();

		Order order2 = new Order.OrderBuilder(2, LocalDate.of(2025, 11, 2), "customer@email.com").setOrderNumber(12345)
				.setPaidDate(LocalDate.of(2025, 9, 10)).setStatus("processing").setShippingTotal(3.0)
				.setShippingTotal(3.0).setShippingTaxTotal(0.0).setFeeTotal(0.0).setFeeTaxTotal(0.0).setTaxTotal(0.0)
				.setCartDiscount(0.0).setOrderDiscount(0.0).setDiscountTotal(0.0).setOrderTotal(16.0)
				.setOrderSubtotal(13.0).setOrderKey("wc_order_yo6dmEfz4tlg").setOrderCurrency("EUR")
				.setPaymentMethod("stripe").setPaymentMethodTitle("Carta di credito")
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
				.setLineItem2("").setLineItem3("").setLineItem4("").setLineItem5("").setOrderNetTotal(15.30)
				.setFirstIssue(112).setLastIssue(117).build();

		orderRepository.save(order1);
		orderRepository.save(order2);
		subscriptionsController.requestOrders(LocalDate.of(2025, 9, 1), LocalDate.of(2025, 11, 1));

		DefaultListModel<Order> listOrdersModel = new DefaultListModel<>();
		listOrdersModel.addElement(order1);
		listOrdersModel.addElement(order2);

		assertThat(listOrdersModel.elementAt(0)).isEqualTo(listView.getListOrdersModel().elementAt(0));
		assertEquals(1, listView.getListOrdersModel().getSize());
		assertEquals("1 order found", messageView.getMessageTextBox().getText());

	}

	@Test
	public void testRequestOrderDetails() {
		Order order1 = new Order.OrderBuilder(1, LocalDate.of(2025, 11, 1), "customer@email.com").setOrderNumber(12345)
				.setPaidDate(LocalDate.of(2025, 9, 10)).setStatus("processing").setShippingTotal(3.0)
				.setShippingTotal(3.0).setShippingTaxTotal(0.0).setFeeTotal(0.0).setFeeTaxTotal(0.0).setTaxTotal(0.0)
				.setCartDiscount(0.0).setOrderDiscount(0.0).setDiscountTotal(0.0).setOrderTotal(16.0)
				.setOrderSubtotal(13.0).setOrderKey("wc_order_yo6dmEfz4tlg").setOrderCurrency("EUR")
				.setPaymentMethod("stripe").setPaymentMethodTitle("Carta di credito")
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
				.setLineItem2("").setLineItem3("").setLineItem4("").setLineItem5("").setOrderNetTotal(15.30)
				.setFirstIssue(112).setLastIssue(117).build();

		orderRepository.save(order1);
		GuiActionRunner.execute(() -> {
			orderView.setSubscriptionsController(subscriptionsController);
			return orderView;
		});
		window = showInFrame(orderView);

		subscriptionsController.fetchOrderDetails(1);

		assertEquals("1", orderView.getOrderIdTextBox().getText());
		assertEquals("2025-11-01", orderView.getOrderDateTextBox().getText());
		assertEquals("customer@email.com", orderView.getEmailTextBox().getText());

		window.cleanUp();

	}

	@Test
	public void testUpdateOrder() {
		Order order1 = new Order.OrderBuilder(1, LocalDate.of(2025, 11, 1), "customer@email.com").setOrderNumber(12345)
				.setPaidDate(LocalDate.of(2025, 9, 10)).setStatus("processing").setShippingTotal(3.0)
				.setShippingTotal(3.0).setShippingTaxTotal(0.0).setFeeTotal(0.0).setFeeTaxTotal(0.0).setTaxTotal(0.0)
				.setCartDiscount(0.0).setOrderDiscount(0.0).setDiscountTotal(0.0).setOrderTotal(16.0)
				.setOrderSubtotal(13.0).setOrderKey("wc_order_yo6dmEfz4tlg").setOrderCurrency("EUR")
				.setPaymentMethod("stripe").setPaymentMethodTitle("Carta di credito")
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
				.setLineItem2("").setLineItem3("").setLineItem4("").setLineItem5("").setOrderNetTotal(15.30)
				.setFirstIssue(112).setLastIssue(117).build();

		orderRepository.save(order1);
		GuiActionRunner.execute(() -> {
			orderView.setSubscriptionsController(subscriptionsController);
			return orderView;
		});
		window = showInFrame(orderView);

		subscriptionsController.fetchOrderDetails(1);

		assertEquals("1", orderView.getOrderIdTextBox().getText());
		assertEquals("2025-11-01", orderView.getOrderDateTextBox().getText());
		assertEquals("customer@email.com", orderView.getEmailTextBox().getText());

		Order updatedOrder = new Order.OrderBuilder(1, LocalDate.of(2025, 11, 1), "updated@email.com")
				.setOrderNumber(12345).setPaidDate(LocalDate.of(2025, 9, 10)).setStatus("processing")
				.setShippingTotal(3.0).setShippingTotal(3.0).setShippingTaxTotal(0.0).setFeeTotal(0.0)
				.setFeeTaxTotal(0.0).setTaxTotal(0.0).setCartDiscount(0.0).setOrderDiscount(0.0).setDiscountTotal(0.0)
				.setOrderTotal(16.0).setOrderSubtotal(13.0).setOrderKey("wc_order_yo6dmEfz4tlg").setOrderCurrency("EUR")
				.setPaymentMethod("stripe").setPaymentMethodTitle("Carta di credito")
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
				.setLineItem2("").setLineItem3("").setLineItem4("").setLineItem5("").setOrderNetTotal(15.30)
				.setFirstIssue(112).setLastIssue(117).build();

		subscriptionsController.updateOrder(1, updatedOrder);

		subscriptionsController.fetchOrderDetails(1);

		assertEquals("1", orderView.getOrderIdTextBox().getText());
		assertEquals("2025-11-01", orderView.getOrderDateTextBox().getText());
		assertEquals("updated@email.com", orderView.getEmailTextBox().getText());

		window.cleanUp();
	}

}
