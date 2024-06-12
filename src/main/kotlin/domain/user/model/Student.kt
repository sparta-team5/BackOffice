package domain.user.model
import jakarta.persistence.*

@Entity
@Table(name ="student") //todo
class Student(

    @Column(name="nickname", nullable=false)
    val nickname:String,

    @Column(name="email", nullable=false)
    val email:String,

    @Column(name="password", nullable=true)
    val password:String,

    @Column(name="provider_name", nullable=true)
    val provider_name:String,

    @Column(name="provider_id", nullable=true)
    val provider_id:String,

    @Column(name="prev_passwords", nullable=true)
    val prev_passwords:List<String>,
    ) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long? =null

}