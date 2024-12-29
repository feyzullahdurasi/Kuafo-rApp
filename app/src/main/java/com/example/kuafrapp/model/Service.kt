import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "services",
    indices = [Index(value = ["service_id"], unique = true)]
)
data class Service(
    @PrimaryKey val id: Int,
    val serviceType: String,
    val features: List<ServiceFeature>
)