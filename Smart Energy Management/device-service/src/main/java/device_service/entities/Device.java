    package device_service.entities;

    import jakarta.persistence.*;
    import org.hibernate.annotations.GenericGenerator;
    import com.fasterxml.jackson.annotation.JsonProperty; // pentru a ne asigura că userId este mapat corect

    import java.io.Serial;
    import java.io.Serializable;
    import java.util.UUID;
    @Table(name="device-info")

    @Entity
    public class Device implements Serializable {



        @Serial
        private static final long serialVersionUID = 1L;
        @Id
        @GeneratedValue(generator = "uuid2")
        @GenericGenerator(name = "uuid2", strategy = "uuid2")
        // No need to use @Type for UUID with Postgres, just define as UUID in column
        @Column(name = "id", nullable = false, updatable = false, columnDefinition = "uuid")
        private UUID id;

        @Column(name = "description", nullable = false)
        private String description;

        @Column(name = "address", nullable = false)
        private String address;

        @Column(name = "maxHourlyConsumption")
        private Double maxHourlyConsumption;

        @Column(name = "consumption", nullable = false)
        private Double consumption;

        @Column(name = "user_id", nullable = false)
        @JsonProperty("userId") // Asigură maparea între JSON și entitate
        private UUID userId;

        public Device( String description, String address, Double consumption,UUID userId, Double maxHourlyConsumption) {
            this.description = description;
            this.address = address;
            this.consumption = consumption;
            this.userId= userId;
            this.maxHourlyConsumption = maxHourlyConsumption;
        }

        public UUID getUserID() {
            return userId;
        }

        public void setUserID(UUID userID) {
            this.userId = userID;
        }

        public Device() {

        }

        public Double getMaxHourlyConsumption() {
            return maxHourlyConsumption;
        }

        public void setMaxHourlyConsumption(Double maxHourlyConsumption) {
            this.maxHourlyConsumption = maxHourlyConsumption;
        }


        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public Double getConsumption() {
            return consumption;
        }

        public void setConsumption(Double consumption) {
            this.consumption = consumption;
        }
    }
