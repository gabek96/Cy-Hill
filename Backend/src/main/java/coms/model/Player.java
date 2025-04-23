package coms.model;

import jakarta.persistence.*;
import org.springframework.web.bind.annotation.*;
import coms.services.*;
import coms.model.Stats;
import java.util.*;
import jakarta.persistence.OneToMany;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.transaction.annotation.Transactional;

/* Example person
{
    "name": "John Doe",
    "birthday": "1990-05-15",
    "email": "john.doe@example.com",
    "password": "securePassword123",
    "team" : "LAS" //note that this is saved in the stats not the player
}
 */
@Entity
@Table(name = "player")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String birthday;
    private String email;
    private String password;
    private String date_created;
    private boolean is_admin;
    private boolean is_active;
    private String emailToken;
    private int forgotPasswordToekn;

    //cascade deletes stats when player is deleted
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "stat_id")
    private Stats stats;

    public Player() {
    }


    public Player(String name, String birthday, String email, String password, String date_created) {
        this.name = name;
        this.birthday = birthday;
        this.email = email;
        this.password = password;
        this.date_created = date_created;
        this.is_admin = false;
        this.is_active = false;
        this.emailToken = "none";
        this.forgotPasswordToekn = 0;
    }

    //setters and getters
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getBirthday() {return birthday;}
    public void setBirthday(String birthday) {this.birthday = birthday;}

    public int getForgotPasswordToekn() {return forgotPasswordToekn;}
    public void setForgotPasswordToekn(int token) {this.forgotPasswordToekn = token;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public String getDateCreated() {return date_created;}
    public void setDateCreated(String date_created) {this.date_created = date_created;}

    public boolean getIsAdmin() {return is_admin;}
    public void setIsAdmin(boolean is_admin) {this.is_admin = is_admin;}

    public boolean getIsActive() {return is_active;}
    public void setIsActive(boolean is_active) {this.is_active = is_active;}

    public String getToken() {return emailToken;}
    public void setToken(String token) {this.emailToken = token;}

    public Stats getStats() {return this.stats;}
    public void setStats(Stats stats) {this.stats = stats;}


    @Override
    public String toString() {
        return id + " "
                + birthday + " "
                + date_created + " "
                + email + " "
                + is_admin + " "
                + name + " "
                + password + " "
                ;
    }

}

