package edu.illinois.cs.cs124.ay2023.mp.models;

public class Rating {
  public static final float NOT_RATED = -1.0f;
  private float rating = NOT_RATED;

  private Summary summary;

  public Rating() {}

  public Rating(Summary setSummary, float setRating) {
    summary = setSummary;
    rating = setRating;
  }

  public Rating(Summary setSummary) {
    summary = setSummary;
  }

  public Summary getSummary() {
    return summary;
  }

  public void setRating(float setRating) {
    rating = setRating;
  }

  public float getRating() {
    return rating;
  }
}
