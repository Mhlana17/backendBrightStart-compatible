package za.ac.cput.factory;

import org.junit.jupiter.api.Test;
import za.ac.cput.domain.CustomerOrder;
import za.ac.cput.domain.Customer;
import za.ac.cput.domain.OrderItem;
import za.ac.cput.domain.Product;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CustomerOrderFactoryTest {

    @Test
    void testCreateOrderSuccess() {
        // Create a customer
        Customer customer = new Customer.Builder()
                .setUserId(1L)
                .setFirstName("John")
                .setLastName("Doe")
                .setEmail("john@email.com")
                .setAddress("123 Main Street, Johannesburg")
                .setPhoneNumber("+27831234567")
                .build();

        // Create products
        Product product1 = new Product.Builder()
                .setProductId(1L)
                .setName("Naruto Hoodie")
                .setPrice(450.00)
                .setStock(10)
                .build();

        Product product2 = new Product.Builder()
                .setProductId(2L)
                .setName("One Piece T-shirt")
                .setPrice(225.00)
                .setStock(15)
                .build();

        // Create order items
        OrderItem item1 = new OrderItem.Builder()
                .setProduct(product1)
                .setQuantity(1)
                .setUnitPrice(450.00)
                .build();

        OrderItem item2 = new OrderItem.Builder()
                .setProduct(product2)
                .setQuantity(2)
                .setUnitPrice(225.00)
                .build();

        List<OrderItem> orderItems = Arrays.asList(item1, item2);

        // Create order
        CustomerOrder order = CustomerOrderFactory.createOrder(
                customer,
                orderItems,
                "CREDIT_CARD",
                "123 Main Street, Johannesburg, 2000"
        );

        assertNotNull(order);
        assertEquals("CONFIRMED", order.getStatus());
        assertEquals("CREDIT_CARD", order.getPaymentMethod());
        assertEquals("123 Main Street, Johannesburg, 2000", order.getShippingAddress());
        assertEquals(900.00, order.getTotalAmount()); // 450 + (2 * 225)
        assertNotNull(order.getOrderNumber());
        assertTrue(order.getOrderNumber().startsWith("ORD-"));
        assertEquals(2, order.getOrderItems().size());
    }

    @Test
    void testCreateOrderWithNullCustomer() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            CustomerOrderFactory.createOrder(null, null, "CREDIT_CARD", "123 Main St");
        });
        assertEquals("Customer cannot be null", exception.getMessage());
    }

    @Test
    void testCreateOrderWithEmptyOrderItems() {
        Customer customer = new Customer.Builder()
                .setUserId(1L)
                .setFirstName("John")
                .setLastName("Doe")
                .setEmail("john@email.com")
                .build();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            CustomerOrderFactory.createOrder(customer, null, "CREDIT_CARD", "123 Main St");
        });
        assertEquals("Order must contain at least one item", exception.getMessage());
    }

    @Test
    void testCreateOrderWithInvalidPaymentMethod() {
        Customer customer = new Customer.Builder()
                .setUserId(1L)
                .setFirstName("John")
                .setLastName("Doe")
                .setEmail("john@email.com")
                .build();

        Product product = new Product.Builder()
                .setProductId(1L)
                .setName("Test Product")
                .setPrice(100.00)
                .setStock(10)
                .build();

        OrderItem item = new OrderItem.Builder()
                .setProduct(product)
                .setQuantity(1)
                .setUnitPrice(100.00)
                .build();

        List<OrderItem> orderItems = Arrays.asList(item);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            CustomerOrderFactory.createOrder(customer, orderItems, "INVALID_METHOD", "123 Main St");
        });
        assertEquals("Invalid payment method", exception.getMessage());
    }

    @Test
    void testCreateOrderWithInvalidAddress() {
        Customer customer = new Customer.Builder()
                .setUserId(1L)
                .setFirstName("John")
                .setLastName("Doe")
                .setEmail("john@email.com")
                .build();

        Product product = new Product.Builder()
                .setProductId(1L)
                .setName("Test Product")
                .setPrice(100.00)
                .setStock(10)
                .build();

        OrderItem item = new OrderItem.Builder()
                .setProduct(product)
                .setQuantity(1)
                .setUnitPrice(100.00)
                .build();

        List<OrderItem> orderItems = Arrays.asList(item);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            CustomerOrderFactory.createOrder(customer, orderItems, "CREDIT_CARD", "Short");
        });
        assertEquals("Shipping address must be between 10-200 characters", exception.getMessage());
    }

    @Test
    void testCreateOrderWithInvalidQuantity() {
        Customer customer = new Customer.Builder()
                .setUserId(1L)
                .setFirstName("John")
                .setLastName("Doe")
                .setEmail("john@email.com")
                .build();

        Product product = new Product.Builder()
                .setProductId(1L)
                .setName("Test Product")
                .setPrice(100.00)
                .setStock(10)
                .build();

        OrderItem item = new OrderItem.Builder()
                .setProduct(product)
                .setQuantity(0) // Invalid quantity
                .setUnitPrice(100.00)
                .build();

        List<OrderItem> orderItems = Arrays.asList(item);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            CustomerOrderFactory.createOrder(customer, orderItems, "CREDIT_CARD", "Valid Address Street");
        });
        assertTrue(exception.getMessage().contains("Invalid quantity"));
    }

    @Test
    void testCreateOrderWithIdSuccess() {
        Customer customer = new Customer.Builder()
                .setUserId(1L)
                .setFirstName("Jane")
                .setLastName("Smith")
                .setEmail("jane@email.com")
                .setAddress("456 Oak Avenue, Cape Town")
                .setPhoneNumber("+27839876543")
                .build();

        Product product = new Product.Builder()
                .setProductId(1L)
                .setName("Dragon Ball Cap")
                .setPrice(250.00)
                .setStock(20)
                .build();

        OrderItem item = new OrderItem.Builder()
                .setProduct(product)
                .setQuantity(1)
                .setUnitPrice(250.00)
                .build();

        List<OrderItem> orderItems = Arrays.asList(item);

        CustomerOrder order = CustomerOrderFactory.createOrderWithId(
                1L, customer, orderItems, "PAYPAL", "456 Oak Avenue, Cape Town, 8001"
        );

        assertNotNull(order);
        assertEquals(1L, order.getOrderId());
        assertEquals("CONFIRMED", order.getStatus());
        assertEquals(250.00, order.getTotalAmount());
    }

    @Test
    void testCreateOrderWithInvalidId() {
        Customer customer = new Customer.Builder()
                .setUserId(1L)
                .setFirstName("John")
                .setLastName("Doe")
                .setEmail("john@email.com")
                .build();

        Product product = new Product.Builder()
                .setProductId(1L)
                .setName("Test Product")
                .setPrice(100.00)
                .setStock(10)
                .build();

        OrderItem item = new OrderItem.Builder()
                .setProduct(product)
                .setQuantity(1)
                .setUnitPrice(100.00)
                .build();

        List<OrderItem> orderItems = Arrays.asList(item);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            CustomerOrderFactory.createOrderWithId(-1L, customer, orderItems, "CREDIT_CARD", "Valid Address Street");
        });
        assertEquals("Order ID must be valid", exception.getMessage());
    }
}