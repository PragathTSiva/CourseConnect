package edu.illinois.cs.cs124.ay2023.mp.activities;

import android.os.Bundle;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.illinois.cs.cs124.ay2023.mp.R;
import edu.illinois.cs.cs124.ay2023.mp.application.CourseableApplication;
import edu.illinois.cs.cs124.ay2023.mp.helpers.ResultMightThrow;
import edu.illinois.cs.cs124.ay2023.mp.models.Course;
import edu.illinois.cs.cs124.ay2023.mp.models.Rating;
import edu.illinois.cs.cs124.ay2023.mp.models.Summary;
import java.util.function.Consumer;

public class CourseActivity extends AppCompatActivity
    implements RatingBar.OnRatingBarChangeListener {
  @Override
  protected void onCreate(@Nullable Bundle unused) {
    super.onCreate(unused);

    // Load this activity's layout and set the title
    setContentView(R.layout.activity_course);

    // Set up our UI
    TextView descriptionTextView = findViewById(R.id.description);
    RatingBar ratingBar = findViewById(R.id.rating);
    runOnUiThread(
        () -> {
          // Retrieve the intent that started this activity, get the summary, and deserialize
          String strsummary = getIntent().getStringExtra("summary");
          ObjectMapper mapper = new ObjectMapper();
          Summary summary;
          try {
            summary = mapper.readValue(strsummary, Summary.class);
          } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
          }
          // Once we have a summary, make the request using the client for course details
          CourseableApplication application = (CourseableApplication) getApplication();
          Consumer<ResultMightThrow<Course>> courseCallback =
              (result) -> {
                Course course = result.getValue();
                try {
                  String desc = course.getDescription();
                  String title = summary.toString();
                  descriptionTextView.setText(title + "\n\n\n\n" + desc);
                } catch (Exception e) {
                  e.printStackTrace();
                }
              };
          application.getClient().getCourse(summary, courseCallback);
          // Once the request completes, update the UI with details about the course

          Consumer<ResultMightThrow<Rating>> ratingCallback =
              (result) -> {
                try {
                  Rating rating = result.getValue();
                  ratingBar.setRating(rating.getRating());
                } catch (Exception e) {
                  e.printStackTrace();
                }
              };
          application.getClient().getRating(summary, ratingCallback);

          ratingBar.setOnRatingBarChangeListener((ratingBar1, rating, fromUser) -> {
            Rating setRating = new Rating(summary, rating);
            CourseableApplication application1 = (CourseableApplication) getApplication();
            application1.getClient().postRating(setRating, ratingCallback);
          });
        });
  }

  @Override
  public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
  }
}
