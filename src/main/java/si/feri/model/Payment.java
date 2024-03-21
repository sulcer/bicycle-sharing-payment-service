package si.feri.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {
    private String _id;
    private String userId;
    private String description;
    private double amount;
    private String createdAt;
    private String updatedAt;

    public static Payment fromDocument(Document doc) {
        var payment = new Payment();

        payment.set_id(doc.getObjectId("_id").toString());
        payment.setUserId(doc.getString("userId"));
        payment.setDescription(doc.getString("description"));
        payment.setAmount(doc.getDouble("amount"));
        payment.setCreatedAt(doc.getString("createdAt"));
        payment.setUpdatedAt(doc.getString("updatedAt"));

        return payment;
    }

    public Document toDocument() {
        var doc = new Document();

        doc.append("userId", userId);
        doc.append("description", description);
        doc.append("amount", amount);
        doc.append("createdAt", createdAt);
        doc.append("updatedAt", updatedAt);

        return doc;
    }
}
