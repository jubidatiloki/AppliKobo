package procorp.applikobo.Models;

public class Profil {

    private int id;
    private String mail;
    private String pseudo;

    public Profil() {
    }

    public Profil(String mail, String pseudo) {
        this.mail = mail;
        this.pseudo = pseudo;
    }

    public int getId() {
        return id;
    }

    public String getMail() {
        return mail;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    @Override
    public String toString() {
        return "Profil{" +
                "id=" + id +
                ", mail='" + mail + '\'' +
                ", pseudo='" + pseudo + '\'' +
                '}';
    }
}
