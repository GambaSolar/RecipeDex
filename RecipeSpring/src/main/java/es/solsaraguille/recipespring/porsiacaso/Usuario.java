package es.solsaraguille.recipespring.porsiacaso;


import jakarta.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "usuarioid")
    private Integer id;

    @Column(name = "username", nullable = false, length = 50)
    private String username;

    @Column(name = "contrasena", nullable = false)
    private String contrasena;

    @Column(name = "correo", nullable = false, length = 100)
    private String correo;
}