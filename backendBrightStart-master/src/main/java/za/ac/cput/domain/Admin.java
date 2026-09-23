
package za.ac.cput.domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
@DiscriminatorValue("Admin")
public class Admin {
    @Id
    protected Long adminId;
    protected String username;
    protected String password;
    protected String email;

    protected Admin() {}

    private Admin(Builder builder) {
        this.adminId = builder.adminId;
        this.username = builder.username;
        this.password = builder.password;
        this.email = builder.email;

    }

    public Long getAdminId() {
        return adminId;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "adminId=" + adminId +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public static class Builder {
        protected Long adminId;
        protected String username;
        protected String password;
        protected String email;

        public Builder setAdminId(Long adminId) {
            this.adminId = adminId;
            return this;
        }

        public Builder setUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder setPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder setEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder copy(Admin admin) {
            this.adminId = admin.adminId;
            this.username = admin.username;
            this.password = admin.password;
            this.email = admin.email;
            return this;
        }

        public Admin build() {
            return new Admin(this);
        }
    }
}
