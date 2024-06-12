package domain.user.model

import jakarta.persistence.*


@Entity
@Table(name ="tutor") //todo
class Tutor(

    @Column(name="nickname", nullable=false)
    var nickname:String,

    @Column(name="email", nullable=false)
    val email:String,

    @Column(name="password", nullable=false)
    var password:String,

    @Column(name="description", nullable=false)
    var description:String,

    @Column(name="career", nullable=false)
    var career:String,

    @Column(name="prev_passwords", nullable=true)
    val prev_passwords:String,
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id:Long? =null

}