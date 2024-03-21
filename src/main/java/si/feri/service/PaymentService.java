package si.feri.service;
import io.quarkus.grpc.GrpcService;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import si.feri.*;
import si.feri.model.Payment;
import si.feri.repository.PaymentRepository;
import java.util.List;
import java.util.logging.Logger;

@GrpcService
public class PaymentService implements PaymentGrpcService {

    @Inject
    PaymentRepository paymentRepository;

    private static final Logger log = Logger.getLogger(PaymentService.class.getName());

    @Override
    public Uni<CreatePaymentResponse> createPayment(CreatePaymentRequest request) {
        var payment = new Payment();

        var createdAt = java.time.LocalDateTime.now().toString();

        payment.setUserId(request.getUserId());
        payment.setDescription(request.getDescription());
        payment.setAmount(request.getAmount());
        payment.setCreatedAt(createdAt);
        payment.setUpdatedAt("");

        payment = paymentRepository.save(payment);

        log.info("Payment created: " + payment.get_id());

        return Uni.createFrom().item(CreatePaymentResponse.newBuilder().setId(payment.get_id()).build());
    }

    @Override
    public Uni<GetPaymentResponse> getPayment(GetPaymentRequest request) {
        var payment = paymentRepository.findById(request.getId());

        log.info("Payment retrieved: " + payment.get_id());

        return Uni.createFrom().item(() -> GetPaymentResponse.newBuilder()
                .setId(payment.get_id())
                .setUserId(payment.getUserId())
                .setDescription(payment.getDescription())
                .setAmount(payment.getAmount())
                .setCreatedAt(payment.getCreatedAt())
                .setUpdatedAt(payment.getUpdatedAt())
                .build());
    }

    @Override
    public Uni<GetAllPaymentsResponse> getAllPayments(GetAllPaymentsRequest request) {
        List<Payment> payments = paymentRepository.getAll();

        log.info("Payments retrieved: " + payments.size());

        var response = GetAllPaymentsResponse.newBuilder();

        payments.forEach(payment -> response.addPayments(GetPaymentResponse.newBuilder()
                .setId(payment.get_id())
                .setUserId(payment.getUserId())
                .setDescription(payment.getDescription())
                .setAmount(payment.getAmount())
                .setCreatedAt(payment.getCreatedAt())
                .setUpdatedAt(payment.getUpdatedAt())
                .build()));

        return Uni.createFrom().item(response.build());
    }

    @Override
    public Uni<UpdatePaymentResponse> updatePayment(UpdatePaymentRequest request) {
        var payment = new si.feri.dto.UpdatePaymentDto();

        var updatedAt = java.time.LocalDateTime.now().toString();

        payment.set_id(request.getId());
        payment.setAmount(request.getAmount());
        payment.setDescription(request.getDescription());
        payment.setUpdatedAt(updatedAt);

        var updatedPayment = paymentRepository.update(payment);

        log.info("Payment updated: " + updatedPayment.get_id());

        return Uni.createFrom().item(UpdatePaymentResponse.newBuilder().setMessage("Updated " + payment.get_id()).build());
    }

    @Override
    public Uni<DeletePaymentResponse> deletePayment(DeletePaymentRequest request) {
        paymentRepository.delete(request.getId());

        log.info("Payment deleted: " + request.getId());

        return Uni.createFrom().item(DeletePaymentResponse.newBuilder().setMessage("Deleted " + request.getId()).build());
    }
}
