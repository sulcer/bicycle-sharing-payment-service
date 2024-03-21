package si.feri.repository;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import org.bson.Document;
import si.feri.dto.UpdatePaymentDto;
import si.feri.model.Payment;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class PaymentRepository {

    @Inject
    MongoClient mongoClient;

    MongoCollection<Document> getCollection() {
        return mongoClient.getDatabase("payments").getCollection("payments");
    }

    public Payment save(Payment payment) {
        var doc = payment.toDocument();
        getCollection().insertOne(doc);
        return Payment.fromDocument(doc);
    }

    public Payment findById(String id) {
        var objectId = new org.bson.types.ObjectId(id);
        return Payment.fromDocument(Objects.requireNonNull(getCollection().find(new Document("_id", objectId)).first()));
    }

    public Payment update(UpdatePaymentDto payment) {
        var objectId = new org.bson.types.ObjectId(payment.get_id());
        var filtered = new Document("_id", objectId);

        var update = new Document("$set", new Document("amount", payment.getAmount()).append("description", payment.getDescription()));

        getCollection().updateOne(filtered, update);
        return findById(payment.get_id());
    }

    public List<Payment> getAll() {
        List<Payment> payments = new ArrayList<>();
        try (MongoCursor<Document> cursor = getCollection().find().iterator()) {
            while (cursor.hasNext()) {
                payments.add(Payment.fromDocument(cursor.next()));
            }
        }
        return payments;
    }

    public void delete(String id) {
        getCollection().deleteOne(new Document("_id", new org.bson.types.ObjectId(id)));
    }
}
