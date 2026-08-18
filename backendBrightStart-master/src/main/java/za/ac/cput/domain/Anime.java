// Annotated Anime.java
package za.ac.cput.domain;

import jakarta.persistence.*;

@Entity
public class Anime {
    @Id
    private int animeId;
    private String title;
    private String genre;
    private String description;
    private int releaseYear;
    private String studio;
    private String imageUrl;


    protected Anime() {}

    public Anime(Builder builder) {
        this.animeId = builder.animeId;
        this.title = builder.title;
        this.genre = builder.genre;
        this.description = builder.description;
        this.releaseYear = builder.releaseYear;
        this.studio = builder.studio;
        this.imageUrl = builder.imageUrl;
    }

    public int getAnimeId() { return animeId; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public String getDescription() { return description; }
    public int getReleaseYear() { return releaseYear; }
    public String getStudio() { return studio; }
    public String getImageUrl() { return imageUrl; }

    @Override
    public String toString() {
        return "Anime{" +
                "animeId=" + animeId +
                ", title='" + title + '\'' +
                ", genre='" + genre + '\'' +
                ", description='" + description + '\'' +
                ", releaseYear=" + releaseYear +
                ", studio='" + studio + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }

    public static class Builder {
        private int animeId;
        private String title;
        private String genre;
        private String description;
        private int releaseYear;
        private String studio;
        private String imageUrl;

        public Builder setAnimeId(int animeId) {
            this.animeId = animeId; return this;
        }
        public Builder setTitle(String title) {
            this.title = title; return this;
        }
        public Builder setGenre(String genre) {
            this.genre = genre; return this;
        }
        public Builder setDescription(String description) {
            this.description = description; return this;
        }
        public Builder setReleaseYear(int releaseYear) {
            this.releaseYear = releaseYear; return this;
        }
        public Builder setStudio(String studio) {
            this.studio = studio; return this;
        }
        public Builder setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl; return this;
        }
        public Builder copy(Anime anime) {
            this.animeId = anime.getAnimeId();
            this.title = anime.getTitle();
            this.genre = anime.getGenre();
            this.description = anime.getDescription();
            this.releaseYear = anime.getReleaseYear();
            this.studio = anime.getStudio();
            this.imageUrl = anime.getImageUrl();
            return this;
        }
        public Anime build() { return new Anime(this); }
    }
}
