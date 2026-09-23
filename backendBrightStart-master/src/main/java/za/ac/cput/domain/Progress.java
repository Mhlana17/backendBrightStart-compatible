package za.ac.cput.domain;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "learner_progress")
public class Progress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long progressId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "learner_id", nullable = false, unique = true)
    private User learner;

    @Column(nullable = false)
    private Integer reading = 0;

    @Column(nullable = false)
    private Integer writing = 0;

    @Column(nullable = false)
    private Integer comprehension = 0;

    @Column(nullable = false)
    private Integer speaking = 0;

    @Column(nullable = false)
    private Integer overallProgress = 0;

    private String status;

    @Column(length = 1000)
    private String comment;

    private String tutor;

    private LocalDate commentDate;

    protected Progress() {}

    public Progress(User learner) {
        this.learner = learner;
    }

    public Long getProgressId() {
        return progressId;
    }

    public User getLearner() {
        return learner;
    }

    public Integer getReading() {
        return reading;
    }

    public Integer getWriting() {
        return writing;
    }

    public Integer getComprehension() {
        return comprehension;
    }

    public Integer getSpeaking() {
        return speaking;
    }

    public Integer getOverallProgress() {
        return overallProgress;
    }

    public String getStatus() {
        return status;
    }

    public String getComment() {
        return comment;
    }

    public String getTutor() {
        return tutor;
    }

    public LocalDate getCommentDate() {
        return commentDate;
    }

    public void update(
            Integer reading,
            Integer writing,
            Integer comprehension,
            Integer speaking,
            String status,
            String comment,
            String tutor
    ) {

        this.reading = validateMark(reading);
        this.writing = validateMark(writing);
        this.comprehension = validateMark(comprehension);
        this.speaking = validateMark(speaking);

        /*
         * Overall Progress is automatically calculated
         * from the four individual skill marks.
         */
        this.overallProgress = calculateOverallProgress();

        this.status = status;
        this.comment = comment;
        this.tutor = tutor;
        this.commentDate = LocalDate.now();
    }

    private Integer calculateOverallProgress() {

        double average = (
                reading
                        + writing
                        + comprehension
                        + speaking
        ) / 4.0;

        return (int) Math.round(average);
    }

    private Integer validateMark(Integer value) {

        if (value == null || value < 0 || value > 100) {
            throw new IllegalArgumentException(
                    "Progress marks must be between 0 and 100."
            );
        }

        return value;
    }
}