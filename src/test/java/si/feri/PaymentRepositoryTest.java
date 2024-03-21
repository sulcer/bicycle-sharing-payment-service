package si.feri;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import si.feri.dto.UpdatePaymentDto;
import si.feri.model.Payment;
import si.feri.repository.PaymentRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;

@QuarkusTest
public class PaymentRepositoryTest {

    @Inject
    PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        paymentRepository.getCollection().deleteMany(new org.bson.Document());
    }

    @Test
    void testSave() {
        Payment payment = new Payment();
        payment.setUserId("1");
        payment.setDescription("Test");
        payment.setAmount(100);
        payment.setCreatedAt(java.time.LocalDateTime.now().toString());
        payment.setUpdatedAt("");

        Payment savedPayment = paymentRepository.save(payment);

        assertEquals(payment.getUserId(), savedPayment.getUserId());
        assertEquals(payment.getDescription(), savedPayment.getDescription());
        assertEquals(payment.getAmount(), savedPayment.getAmount());
        assertEquals(payment.getCreatedAt(), savedPayment.getCreatedAt());
        assertEquals(payment.getUpdatedAt(), savedPayment.getUpdatedAt());
    }

    @Test
    void testGetOne() {
        Payment payment = new Payment();
        payment.setUserId("1");
        payment.setDescription("Test");
        payment.setAmount(100);
        payment.setCreatedAt(java.time.LocalDateTime.now().toString());
        payment.setUpdatedAt("");

        Payment savedPayment = paymentRepository.save(payment);

        Payment foundPayment = paymentRepository.findById(savedPayment.get_id());

        assertEquals(savedPayment.getUserId(), foundPayment.getUserId());
        assertEquals(savedPayment.getDescription(), foundPayment.getDescription());
        assertEquals(savedPayment.getAmount(), foundPayment.getAmount());
        assertEquals(savedPayment.getCreatedAt(), foundPayment.getCreatedAt());
        assertEquals(savedPayment.getUpdatedAt(), foundPayment.getUpdatedAt());
    }

    @Test
    void testGetAll() {
        Payment payment1 = new Payment();
        payment1.setUserId("1");
        payment1.setDescription("Test");
        payment1.setAmount(100);
        payment1.setCreatedAt(java.time.LocalDateTime.now().toString());
        payment1.setUpdatedAt("");

        Payment payment2 = new Payment();
        payment2.setUserId("2");
        payment2.setDescription("Test");
        payment2.setAmount(100);
        payment2.setCreatedAt(java.time.LocalDateTime.now().toString());
        payment2.setUpdatedAt("");

        paymentRepository.save(payment1);
        paymentRepository.save(payment2);

        assertEquals(2, paymentRepository.getAll().size());
    }

    @Test
    void testUpdate() {
        Payment payment = new Payment();
        payment.setUserId("1");
        payment.setDescription("Test");
        payment.setAmount(100);
        payment.setCreatedAt(java.time.LocalDateTime.now().toString());
        payment.setUpdatedAt("");

        Payment savedPayment = paymentRepository.save(payment);

        int amount = 200;
        String description = "Updated";

        UpdatePaymentDto updatePaymentDto = new UpdatePaymentDto();
        updatePaymentDto.set_id(savedPayment.get_id());
        updatePaymentDto.setAmount(amount);
        updatePaymentDto.setDescription(description);

        Payment updatedPayment = paymentRepository.update(updatePaymentDto);

        assertEquals(savedPayment.getUserId(), updatedPayment.getUserId());
        assertEquals(description, updatedPayment.getDescription());
        assertEquals(amount, updatedPayment.getAmount());
    }

    @Test
    void testDelete() {
        Payment payment = new Payment();
        payment.setUserId("1");
        payment.setDescription("Test");
        payment.setAmount(100);
        payment.setCreatedAt(java.time.LocalDateTime.now().toString());
        payment.setUpdatedAt("");

        Payment savedPayment = paymentRepository.save(payment);

        paymentRepository.delete(savedPayment.get_id());

        assertEquals(0, paymentRepository.getAll().size());
    }
}
